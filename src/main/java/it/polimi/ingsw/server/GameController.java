package it.polimi.ingsw.server;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.server.rules.ExpertRules;
import it.polimi.ingsw.server.rules.Rules;
import it.polimi.ingsw.util.*;
import it.polimi.ingsw.util.exception.CardException;
import it.polimi.ingsw.util.exception.DisconnectionException;
import it.polimi.ingsw.util.exception.PlayerException;

import java.util.*;
import java.util.stream.Collectors;

public class GameController {
    private final GameModel game;
    private final VirtualView virtualView;
    private GameStatus status;
    private boolean isCardChosen;
    private boolean endImmediately;
    private final Rules rules;

    public GameController(GameListInfo gameInfo) {
        this.game = new GameModel(gameInfo);
        this.virtualView = new VirtualView();
        this.status = GameStatus.ACCEPT_PLAYERS;
        this.isCardChosen = false;
        this.endImmediately = false;
        if(gameInfo.getGameMode() == GameMode.BASIC){
            this.rules = new Rules(game);
        } else {
            this.rules = new ExpertRules(game);
        }
    }

    public GameModel getGame(){
        return game;
    }

    public void addClientHandler(ClientHandler clientHandler) {
        virtualView.addClientHandler(clientHandler);
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
        PlayersHandler ph = game.getPlayerHandler();
        ph.addPlayer(new Player(playerId));
        if (ph.getPlayers().size() == ph.getNumPlayers())
            status = GameStatus.WAITING_ALL_PLAYERS_INFO;
    }

    public void setPlayerNickname(String playerId, String nickname){
        System.out.println("INFO: Player " + playerId + " has requested to set his nickname to: " + nickname);
        if (checkNickname(nickname)) {
            try {
                game.getPlayerHandler().getPlayerById(playerId).setNickname(nickname);
                virtualView.sendToPlayerId(
                        playerId,
                        new AskTowerColor(getAvailableTowerColors(), true)
                );
            } catch (PlayerException e) {
                System.out.println("FATAL ERROR: Player " + playerId + " not found in PlayersHandler ");
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("ERROR: Nickname " + nickname + " has been already chosen");
            virtualView.sendToPlayerId(
                    playerId,
                    new AskNickname(false)
            );
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
        System.out.println("INFO: Player " + playerId + " has requested to set his color to: " + color.toString());
        if (getAvailableTowerColors().contains(color)) {
            try {
                game.getPlayerHandler().getPlayerById(playerId).setTowerColor(color);
                if (game.getPlayerHandler().everyPlayerIsReadyToPlay()) {
                    status = GameStatus.READY_TO_START;
                    wakeUpController();
                }
                virtualView.sendToPlayerId(
                        playerId,
                        new AckTowerColor()
                );
            } catch (PlayerException e) {
                System.out.println("FATAL ERROR: Player " + playerId + " not found in PlayersHandler ");
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("ERROR: Color " + color + " has been already chosen");
            virtualView.sendToPlayerId(
                    playerId,
                    new AskTowerColor(getAvailableTowerColors(), false)
            );
        }
    }

    public void gameRunner() throws InterruptedException, DisconnectionException {
        synchronized(this){
            while(status == GameStatus.ACCEPT_PLAYERS || status == GameStatus.WAITING_ALL_PLAYERS_INFO)
                this.wait();
        }

        if (status == GameStatus.INACTIVE)
            throw new DisconnectionException();
        else
            status = GameStatus.ACTIVE;

        //Game setup
        game.setupGame();

        boolean firstMessage = true;

        //Round handler
        while(!isLastRound() && !endImmediately){
            //Planning phase
            game.cloudsRefill();
            game.getPlayerHandler().initialiseCurrentPlayerPlanningPhase();
            for(int i = 0; i < game.getPlayerHandler().getNumPlayers(); i++){
                if (firstMessage) {
                    virtualView.sendToEveryone(new GameStart(game, game.getPlayerHandler().getCurrentPlayer().getNickname()));
                    firstMessage = false;
                } else {
                    virtualView.sendToEveryone(new UpdateGameBoard(game));
                }
                List<AssistantCard> possibleCards = game.getPlayerHandler().getCurrentPlayer().getDeck().stream()
                        .filter(this::checkAssistantCard)
                        .collect(Collectors.toList());
                virtualView.getClientHandlerById(game.getPlayerHandler().getCurrentPlayer().getId()).send(new AskAssistantCard(possibleCards,game));
                synchronized (this) {
                    while (!isCardChosen) {
                        this.wait();
                    }
                    isCardChosen = false;
                }
                game.getPlayerHandler().nextPlayerByOrder();
            }

            if (isLastRound())
                virtualView.sendToEveryone(new ShowLastRound());

            //Action phase
            game.getPlayerHandler().initialiseCurrentPlayerActionPhase();
            virtualView.sendToEveryone(new UpdateGameBoard(game));
            int i = 0;
            while(i < game.getPlayerHandler().getNumPlayers() && !endImmediately){
                if(getCurrentPlayer().getRoundActions().hasEnded()){
                    System.out.println("Il player " + getCurrentPlayer().getNickname() + " ha terminato il turno");
                    getCurrentPlayer().resetRoundAction();
                    getCurrentPlayer().setActiveCharacter(null);
                    game.getPlayerHandler().nextPlayerByAssistance();
                    i++;
                } else {
                    sendPossibleActions(false);
                    int currentActionsNumber = getCurrentPlayer().getRoundActions().getActionsList().size();
                    synchronized (this) {
                        while (getCurrentPlayer().getRoundActions().getActionsList().size() <= currentActionsNumber) {
                            this.wait();
                        }
                    }
                    virtualView.sendToEveryone(new UpdateGameBoard(game));
                    System.out.println("Il player " + getCurrentPlayer().getNickname() +  " ha effettuato: ");
                    for (Action action : getCurrentPlayer().getRoundActions().getActionsList()) {
                        System.out.print(action.getActionType().toString() + " ");
                    }
                    System.out.println();
                }
            }
        }
        try {
            manageWin();
        } catch (Exception ignored) {
            // FIXME: what to do with double tie ?
            System.out.println("ERROR: double tie!!");
            virtualView.sendToEveryone(new ShowEndGame(""));
        }
    }

    private void sendPossibleActions(boolean isInvalidAction){
        Character activeCharacter = getCurrentPlayer().getActiveCharacter();
        if (activeCharacter == null) {
            virtualView.sendToPlayerId(
                    getCurrentPlayer().getId(),
                    new AskAction(rules.nextPossibleActions(), isInvalidAction)
            );
        } else {
            virtualView.sendToPlayerId(
                    getCurrentPlayer().getId(),
                    new AskAction(activeCharacter.getRules().nextPossibleActions(), isInvalidAction)
            );
        }
    }

    private Player getCurrentPlayer() {
        return game.getPlayerHandler().getCurrentPlayer();
    }

    private void manageWin() throws Exception {
        ArrayList<Player> orderedByTowers = new ArrayList<>(game.getPlayerHandler().getPlayers());
        Player winner;
        orderedByTowers.sort((p1, p2) -> {
            if(p1.getNumOfTowers() > p2.getNumOfTowers())
                return 1;
            else if(p1.getNumOfTowers() < p2.getNumOfTowers())
                return -1;
            return 0;
        });
        Player first = orderedByTowers.get(0);
        Player second = orderedByTowers.get(1);
        if(first.getNumOfTowers() != second.getNumOfTowers()){
            winner = first;
        } else {
            Player finalFirst = first;
            List<Player> orderedByTeacherControl = orderedByTowers
                    .stream()
                    .filter(p -> p.getNumOfTowers() == finalFirst.getNumOfTowers())
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
        System.out.println("INFO: Player " + winner.getNickname() + " won");
        virtualView.sendToEveryone(new ShowEndGame(winner.getNickname()));
        System.out.println("INFO: Controller of game " + game.getGameName() + " closed");
    }

    public void setAction(Action action, String nickname) {
        if(!checkIfIsCurrentPlayer(nickname))
            sendPossibleActions(false);
        Player thePlayer;
        try {
            thePlayer = game.getPlayerHandler().getPlayersByNickName(nickname);
        } catch (PlayerException e) {
            System.out.println("FATAL ERROR: Player " + nickname + " not found in PlayersHandler ");
            throw new RuntimeException(e);
        }
        boolean legalAction;
        if(thePlayer.getActiveCharacter() == null){
            legalAction = rules.doAction(action);
        } else {
            legalAction = thePlayer.getActiveCharacter().getRules().doAction(action);
        }
        if(!legalAction)
            sendPossibleActions(true);
        else{
            // register action
            game.getPlayerHandler().getCurrentPlayer().registerAction(action);
            if(action.getActionType() == ActionType.CHOOSE_CLOUD)
                game.getPlayerHandler().getCurrentPlayer().registerAction(new Action(ActionType.END));
        }
        for (Player player : game.getPlayerHandler().getPlayers())
            if (player.getNumOfTowers() == 0) {
                endImmediately = true;
                break;
            }
        if(game.getIslandHandler().getIslands().size() <= 3)
            endImmediately = true;
        wakeUpController();
    }

    public void setAssistantCard(String nickname, AssistantCard card){
        if(checkIfIsCurrentPlayer(nickname) && checkAssistantCard(card)){
            try{
                game.getPlayerHandler().getCurrentPlayer().setLastCardUsed(card);
                isCardChosen = true;
                wakeUpController();
                System.out.println("Set assistant card to " + nickname);
            } catch (CardException e){
                System.out.println(e.getMessage());
            }
        }
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

    private boolean checkIfIsCurrentPlayer(String nickname){
        return nickname.equalsIgnoreCase(game.getPlayerHandler().getCurrentPlayer().getNickname());
    }

    public boolean isLastRound(){
        //No more cards or no more students
        return game.getPlayerHandler().playerWithNoMoreCards() || game.getStudentsBag().isEmpty();
    }

    public void setAsDisconnected(String playerId) {
        try {
            String nickname = game.getPlayerHandler().getPlayerById(playerId).getNickname();
            virtualView.sendToEveryone(new ShowDisconnection(nickname));
            virtualView.closeAll();
            this.status = GameStatus.INACTIVE;
            wakeUpController();
        } catch (PlayerException ignored) {
        }
    }

    public GameStatus getStatus() {
        return status;
    }

    public void wakeUpController() {
        synchronized (this) {
            this.notifyAll();
        }
    }
}
