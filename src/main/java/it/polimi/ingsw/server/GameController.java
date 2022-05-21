package it.polimi.ingsw.server;

import it.polimi.ingsw.network.messages.ShowDisconnectionMessage;
import it.polimi.ingsw.server.rules.ExpertRules;
import it.polimi.ingsw.server.rules.Rules;
import it.polimi.ingsw.util.exception.CardException;
import it.polimi.ingsw.util.exception.DisconnectionException;
import it.polimi.ingsw.util.exception.PlayerException;
import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.util.Action;
import it.polimi.ingsw.util.GameMode;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

public class GameController {
    private final GameModel game;
    private GameMode mode;
    private boolean isCardChosen;
    private boolean endImmediately;
    private Rules rules;
    private VirtualView virtualView;

    public GameController(GameModel game) {
        mode = GameMode.NOT_CHOSEN;
        this.game = game;
        this.isCardChosen = false;
        this.endImmediately = false;
    }

    public GameModel getGame(){
        return game;
    }

    public void setVirtualView(VirtualView v){
        this.virtualView = v;
    }

    public void setGameMode(GameMode mode){
        this.mode = mode;
        if(mode == GameMode.BASIC){
            this.rules = new Rules(game);
        } else {
            this.rules = new ExpertRules(game);
        }
        wakeUpController();
    }

    public void setNumPlayers(int numPlayers) {
        game.getPlayerHandler().setNumPlayers(numPlayers);
    }

    private boolean checkNickname(String requestedUsername) {
        for (Player player : game.getPlayerHandler().getPlayers()) {
            if (player.getNickname().equalsIgnoreCase(requestedUsername)) {
                return false;
            }
        }
        return true;
    }

    public boolean setPlayerInfo(String playerId, String nickname){
        System.out.println(playerId + " has set is nickname: " + nickname);
        if (checkNickname(nickname)) {
            Player player = new Player(playerId);
            player.setNickname(nickname);
            game.getPlayerHandler().addPlayer(player);
            wakeUpController();
            return true;
        }
        return false;
    }

    public void gameRunner() throws InterruptedException, DisconnectionException {
        synchronized(this){
            while(!gameCanStart() && isRunning())
                this.wait();
        }

        //Game setup
        game.setupGame(mode);

        //Round handler
        while(!isGameEnd() && !endImmediately){
            //Planning phase
            game.getPlayerHandler().initialiseCurrentPlayerPlanningPhase();
            for(int i = 0; i < game.getPlayerHandler().getNumPlayers(); i++){
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
        return mode != GameMode.NOT_CHOSEN &&
                game.getPlayerHandler().getPlayers().size() == game.getPlayerHandler().getNumPlayers();
    }

    private boolean isRunning() throws DisconnectionException {
        if (!game.isActive()) {
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

    public void wakeUpController() {
        synchronized (this) {
            this.notifyAll();
        }
    }
}
