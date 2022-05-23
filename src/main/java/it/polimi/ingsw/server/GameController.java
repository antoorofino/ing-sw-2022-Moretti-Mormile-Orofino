package it.polimi.ingsw.server;

import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.server.rules.ExpertRules;
import it.polimi.ingsw.server.rules.Rules;
import it.polimi.ingsw.util.TowerColor;
import it.polimi.ingsw.util.exception.CardException;
import it.polimi.ingsw.util.exception.DisconnectionException;
import it.polimi.ingsw.util.exception.PlayerException;
import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.util.Action;
import it.polimi.ingsw.util.GameMode;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;

import java.util.*;
import java.util.stream.Collectors;

public class GameController {
    private final GameModel game;
    private final VirtualView virtualView;
    private boolean isActive;
    private boolean isCardChosen;
    private boolean endImmediately;
    private Rules rules;

    public GameController() {
        this.game = new GameModel();
        this.virtualView = new VirtualView();
        this.isActive = true;
        this.isCardChosen = false;
        this.endImmediately = false;
    }

    public GameModel getGame(){
        return game;
    }

    public VirtualView getVirtualView() {
        return virtualView;
    }

    public void setGameSettings(String playerId, GameMode mode, int numPlayers){
        System.out.println("game mode set on " + mode + " for game " + game.getGameName());
        game.setGameMode(mode);
        if(mode == GameMode.BASIC){
            this.rules = new Rules(game);
        } else {
            this.rules = new ExpertRules(game);
        }
        System.out.println("numPlayers set on " + numPlayers + " for game " + game.getGameName());
        game.getPlayerHandler().setNumPlayers(numPlayers);
        virtualView.getClientHandlerById(playerId).send(new AskTowerColor(getAvailableTowerColors(), true));
    }

    private boolean checkNickname(String requestedUsername) {
        for (Player player : game.getPlayerHandler().getPlayers()) {
            if (player.getNickname() != null && player.getNickname().equalsIgnoreCase(requestedUsername)) {
                return false;
            }
        }
        return true;
    }

    public void addPlayer(String playerId){
        game.getPlayerHandler().addPlayer(new Player(playerId));
    }

    public void setPlayerNickname(String playerId, String nickname){
        System.out.println(playerId + " has set his nickname: " + nickname);
        if (checkNickname(nickname)) {
            Optional<Player> player = game.getPlayerHandler().getPlayers().stream()
                    .filter( p -> Objects.equals(p.getId(), playerId))
                    .findFirst();
            player.ifPresent(p -> p.setNickname(nickname));
            if (game.getGameMode() == GameMode.NOT_CHOSEN){
                virtualView.getClientHandlerById(playerId).send(new AskGameSettings());
            } else {
                virtualView.getClientHandlerById(playerId).send(new AskTowerColor(getAvailableTowerColors(), true));
            }
        } else {
            virtualView.getClientHandlerById(playerId).send(new AskNickname(false));
        }
    }

    private List<TowerColor> getAvailableTowerColors(){
        ArrayList<TowerColor> possibleColors = new ArrayList<>();
        possibleColors.add(TowerColor.BLACK);
        possibleColors.add(TowerColor.WHITE);
        if (game.getPlayerHandler().getNumPlayers() == 3)
            possibleColors.add(TowerColor.GRAY);
        for (Player player : game.getPlayerHandler().getPlayers()){
            if (player.getTowerColor() != null)
                possibleColors.remove(player.getTowerColor());
        }
        return possibleColors;
    }

    public void setPlayerTowerColor(String playerId, TowerColor color){
        System.out.println(playerId + " has set his color: " + color);
        if (getAvailableTowerColors().contains(color)) {
            Optional<Player> player = game.getPlayerHandler().getPlayers().stream()
                    .filter( p -> Objects.equals(p.getId(), playerId))
                    .findFirst();
            player.ifPresent(p -> p.setTowerColor(color));
            virtualView.getClientHandlerById(playerId).send(new AckTowerColor());
            wakeUpController();
        } else {
            virtualView.getClientHandlerById(playerId).send(new AskTowerColor(getAvailableTowerColors(), false));
        }
    }

    public void gameRunner() throws InterruptedException, DisconnectionException {
        synchronized(this){
            while(!gameCanStart() && isRunning())
                this.wait();
        }

        //Game setup
        game.setupGame();

        boolean firstMessage = true;

        //Round handler
        while(!isGameEnd() && !endImmediately){
            //Planning phase
            game.getPlayerHandler().initialiseCurrentPlayerPlanningPhase();
            if (firstMessage) {
                virtualView.sendToEveryone(new GameStart(game, game.getPlayerHandler().getCurrentPlayer().getNickname()));
                firstMessage = false;
            }
            for(int i = 0; i < game.getPlayerHandler().getNumPlayers(); i++){
                virtualView.sendToEveryone(new UpdateCurrentPlayer(game.getPlayerHandler().getCurrentPlayer().getNickname()));
                List<AssistantCard> possibleCards = game.getPlayerHandler().getCurrentPlayer().getDeck().stream()
                        .filter(this::checkAssistantCard)
                        .collect(Collectors.toList());
                virtualView.getClientHandlerById(game.getPlayerHandler().getCurrentPlayer().getId()).send(new AskAssistantCard(possibleCards));
                synchronized (this) {
                    while (!isCardChosen) {
                        this.wait();
                    }
                    isCardChosen = false;
                }
                game.getPlayerHandler().nextPlayerByOrder();
            }

            //Action phase
            game.getPlayerHandler().initialiseCurrentPlayerActionPhase();
            int i = 0;
            while(i < game.getPlayerHandler().getNumPlayers() && !endImmediately){
                virtualView.sendToEveryone(new UpdateCurrentPlayer(game.getPlayerHandler().getCurrentPlayer().getNickname()));
                if(game.getPlayerHandler().getCurrentPlayer().getRoundActions().hasEnded()){
                    game.getPlayerHandler().getCurrentPlayer().resetRoundAction();
                    game.getPlayerHandler().getCurrentPlayer().setActiveCharacter(null);
                    game.getPlayerHandler().nextPlayerByAssistance();
                    i++;
                } else {
                    //TODO: update the map through the virtualView
                    //TODO: send possible actions to the current player
                    int currentActionsNumber = game.getPlayerHandler().getCurrentPlayer().getRoundActions().getActionsList().size();
                    synchronized (this) {
                        while (game.getPlayerHandler().getCurrentPlayer().getRoundActions().getActionsList().size() <= currentActionsNumber) {
                            this.wait();
                        }
                    }
                }
            }
        }
        try {
            manageWin();
        } catch (Exception ignored) {
            //FIXME: should be created a custom exception
            throw new RuntimeException("Double tie");
        }
    }

    private void manageWin() throws Exception {
        ArrayList<Player> orderedByTowers = new ArrayList<>(game.getPlayerHandler().getPlayers());
        Player winner;
        orderedByTowers.sort((p1, p2) -> {
            if(p1.getNumOfTower() > p2.getNumOfTower())
                return 1;
            else if(p1.getNumOfTower() < p2.getNumOfTower())
                return -1;
            return 0;
        });
        Player first = orderedByTowers.get(0);
        Player second = orderedByTowers.get(1);
        if(first.getNumOfTower() != second.getNumOfTower()){
            winner = first;
        } else {
            Player finalFirst = first;
            List<Player> orderedByTeacherControl = orderedByTowers
                    .stream()
                    .filter(p -> p.getNumOfTower() == finalFirst.getNumOfTower())
                    .sorted((p1, p2) -> {
                        if(game.getTeacherHandler().teachersControlled(p1) > game.getTeacherHandler().teachersControlled(p2))
                            return -1;
                        else if(game.getTeacherHandler().teachersControlled(p1) < game.getTeacherHandler().teachersControlled(p2))
                            return 1;
                        return 0;
                    })
                    .collect(Collectors.toList());
            winner = orderedByTeacherControl.get(0);

            first = orderedByTeacherControl.get(0);
            second = orderedByTeacherControl.get(1);
            if(game.getTeacherHandler().teachersControlled(first) == game.getTeacherHandler().teachersControlled(second))
                //FIXME: double tie!
                throw new Exception("Double tie");
        }
        //TODO: what to do with winner player
    }

    public boolean setAction(Action action, String nickname) throws PlayerException {
        if(!checkIfIsCurrentPlayer(nickname))
            return false;
        Player thePlayer = game.getPlayerHandler().getPlayersByNickName(nickname);
        boolean legalAction;
        if(thePlayer.getActiveCharacter() == null){
            legalAction = rules.doAction(action);
        } else {
            legalAction = thePlayer.getActiveCharacter().getRules().doAction(action);
        }
        if(!legalAction)
            return false;
        if(thePlayer.getNumOfTower() == 0)
            endImmediately = true;
        if(game.getIslandHandler().getIslands().size() <= 3)
            endImmediately = true;
        wakeUpController();
        return true;
    }

    public boolean setAssistantCard(String nickname, AssistantCard card){
        if(checkIfIsCurrentPlayer(nickname) && checkAssistantCard(card)){
            try{
                game.getPlayerHandler().getCurrentPlayer().setLastCardUsed(card);
                isCardChosen = true;
                wakeUpController();
                return true;
            } catch (CardException e){
                return false;
            }
        }
        return false;
    }

    private boolean checkAssistantCard(AssistantCard card){
        List<AssistantCard> alreadyPlayed = game.getPlayerHandler().cardsAlreadyPlayed();
        List<AssistantCard> playerDeck = game.getPlayerHandler().getCurrentPlayer().getDeck();
        if(alreadyPlayed.size() >= playerDeck.size() && alreadyPlayed.containsAll(playerDeck))
            return true;
        for(AssistantCard c : alreadyPlayed){
            if(c.isSameValue(card))
                return false;
        }
        return true;
    }

    public boolean checkIfIsCurrentPlayer(String nickname){
        return nickname.equalsIgnoreCase(game.getPlayerHandler().getCurrentPlayer().getNickname());
    }

    public boolean isGameEnd(){
        //No more cards or no more students
        return game.getPlayerHandler().playerWithNoMoreCards() || game.getStudentsBag().isEmpty();
    }

    private boolean gameCanStart(){
        return game.getPlayerHandler().everyPlayerIsReadyToPlay();
    }

    private boolean isRunning() throws DisconnectionException {
        if (!isActive) {
            throw new DisconnectionException();
        }
        return true;
    }

    public void setAsDisconnected(String playerId) {
        String nickname = "";
        try {
            nickname = game.getPlayerHandler().getPlayers().stream()
                    .filter(p -> Objects.equals(p.getId(), playerId))
                    .findFirst()
                    .get().getNickname();
        } catch (NoSuchElementException ignored) {
        }
        virtualView.sendToEveryone(new ShowDisconnectionMessage(nickname));
        virtualView.closeAll();
        wakeUpController();
    }

    public void setAsInactive(){
        this.isActive = false;
    }

    public void wakeUpController() {
        synchronized (this) {
            this.notifyAll();
        }
    }
}
