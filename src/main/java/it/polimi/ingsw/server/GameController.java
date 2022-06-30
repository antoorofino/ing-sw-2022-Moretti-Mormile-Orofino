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

/**
 * Manages the interaction of the players with the game's rules
 */
public class GameController {
    private final GameModel game;
    private final VirtualView virtualView;
    private GameStatus status;
    private boolean isCardChosen;
    private boolean endImmediately;
    private final Rules rules;
    private final Logger logger;

    /**
     * Constructor: build game controller
     * @param gameInfo the game info such as number of players, mode and name
     * @param logger class to show formatted messages
     */
    public GameController(GameListInfo gameInfo, Logger logger) {
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
        this.logger = logger;
    }

    /**
     * Return the game model
     * @return the game model
     */
    public GameModel getGame(){
        return game;
    }

    /**
     * Adds a client network manager to the virtual view
     * @param clientHandler the client handler
     */
    public void addClientHandler(ClientHandler clientHandler) {
        virtualView.addClientHandler(clientHandler);
    }

    /**
     * Check that there is no player with the same nickname
     * @param requestedUsername the chosen nickname
     * @return true if it has not already been chosen
     */
    private boolean checkNickname(String requestedUsername) {
        for (Player player : game.getPlayerHandler().getPlayers()) {
            if (player.getNickname() != null && player.getNickname().equalsIgnoreCase(requestedUsername)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Adds a player to the game model
     * @param playerId
     */
    public void addPlayer(String playerId){
        PlayersHandler ph = game.getPlayerHandler();
        ph.addPlayer(new Player(playerId));
        if (ph.getPlayers().size() == ph.getNumPlayers())
            status = GameStatus.WAITING_ALL_PLAYERS_INFO;
    }

    /**
     * Sets the player's nickname
     * @param playerId the id of the player who wants to set the nickname
     * @param nickname the chosen nickname
     */
    public void setPlayerNickname(String playerId, String nickname){
        logger.log(4, 'g', "Player " + playerId + " has requested to set his nickname to: " + nickname);
        if (checkNickname(nickname)) {
            try {
                game.getPlayerHandler().getPlayerById(playerId).setNickname(nickname);
                virtualView.sendToPlayerId(
                        playerId,
                        new AskTowerColor(getAvailableTowerColors(), true)
                );
            } catch (PlayerException e) {
                logger.log(0, 'f', "Player " + playerId + " not found in PlayersHandler");
                throw new RuntimeException(e);
            }
        } else {
            logger.log(4, 'w', "Nickname " + nickname + " has been already chosen");
            virtualView.sendToPlayerId(
                    playerId,
                    new AskNickname(false)
            );
        }
    }

    /**
     * Returns the available colors of the towers
     * @return the available towers' colors
     */
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

    /**
     * Sets the color of the player's towers
     * @param playerId the id of the player who wants to set the tower's color
     * @param color the chosen color
     */
    public void setPlayerTowerColor(String playerId, TowerColor color){
        synchronized (this) {
            logger.log(4, 'g', "Player " + playerId + " has requested to set his color to: " + color.toString());
            if (getAvailableTowerColors().contains(color)) {
                try {
                    game.getPlayerHandler().getPlayerById(playerId).setTowerColor(color);
                    if (game.getPlayerHandler().everyPlayerIsReadyToPlay()) {
                        status = GameStatus.READY_TO_START;
                        notifyAll();
                    }
                    virtualView.sendToPlayerId(
                            playerId,
                            new AckTowerColor()
                    );
                } catch (PlayerException e) {
                    logger.log(0, 'f', "Player " + playerId + " not found in PlayersHandler");
                    throw new RuntimeException(e);
                }
            } else {
                logger.log(4, 'w', "Color " + color + " has been already chosen");
                virtualView.sendToPlayerId(
                        playerId,
                        new AskTowerColor(getAvailableTowerColors(), false)
                );
            }
        }
    }

    /**
     * Manages the alternation of planning and action phase from the beginning up to the victory
     * @throws InterruptedException exception thrown by thread in case of abort
     * @throws DisconnectionException exception thrown in case of disconnection
     */
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
            // set null lastCardUsed
            for (Player p:game.getPlayerHandler().getPlayers()) {
                p.resetLastCard();
            }
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
                    logger.log(4, 'g', "Player " + getCurrentPlayer().getNickname() + " ended his round");
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
                    StringBuilder actions = new StringBuilder();
                    for (Action action : getCurrentPlayer().getRoundActions().getActionsList()) {
                        actions.append(action.getActionType().toString()).append(" - ");
                    }
                    logger.log(4, 'g', "Player " + getCurrentPlayer().getNickname() + "'s actions: " + actions);
                }
                virtualView.sendToEveryone(new UpdateGameBoard(game));
            }
        }
        manageWin();
    }

    /**
     * Send possible actions to the current player
     * @param isInvalidAction true if he made a wrong action before
     */
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

    /**
     * Returns the current player
     * @return the current player
     */
    private Player getCurrentPlayer() {
        return game.getPlayerHandler().getCurrentPlayer();
    }

    /**
     * Manages the control and sending of victory messages
     */
    private void manageWin() {
        ArrayList<Player> orderedByTowers = new ArrayList<>(game.getPlayerHandler().getPlayers());
        Player winner;
        orderedByTowers.sort(Comparator.comparingInt(Player::getNumOfTowers));
        Player first = orderedByTowers.get(0);
        Player second = orderedByTowers.get(1);
        if(first.getNumOfTowers() != second.getNumOfTowers()){ // Winner found by numbers of towers
            winner = first;
            logger.log(4, 'g', "Player " + winner.getNickname() + " won");
            virtualView.sendToEveryone(new ShowEndGame(winner.getNickname()));
        } else { // Tie in numbers of towers found. Check teacher control
            Player finalFirst = first;
            List<Player> orderedByTeacherControl = orderedByTowers
                    .stream()
                    .filter(p -> p.getNumOfTowers() == finalFirst.getNumOfTowers())
                    .sorted((p1, p2) -> game.getTeacherHandler().teachersControlled(p2) - game.getTeacherHandler().teachersControlled(p1))
                    .collect(Collectors.toList());
            first = orderedByTeacherControl.get(0);
            second = orderedByTeacherControl.get(1);
            if (game.getTeacherHandler().teachersControlled(first) != game.getTeacherHandler().teachersControlled(second)) {
                winner = first;
                logger.log(4, 'g', "Player " + winner.getNickname() + " won");
                virtualView.sendToEveryone(new ShowEndGame(winner.getNickname()));
            } else {
                logger.log(4, 'g', "The winner could not be determined");
                virtualView.sendToEveryone(new ShowEndGame(null));
            }
        }
        logger.log(3, 'i', "Controller of game " + game.getGameName() + " terminated");
    }

    /**
     * Verify and perform action made by the player
     * @param action the player's action
     * @param nickname the nickname of the player who took the action
     */
    public void setAction(Action action, String nickname) {
        synchronized (this) {
            if (!checkIfIsCurrentPlayer(nickname))
                sendPossibleActions(false);
            Player thePlayer;
            try {
                thePlayer = game.getPlayerHandler().getPlayersByNickName(nickname);
            } catch (PlayerException e) {
                logger.log(0, 'f', "Player " + nickname + " not found in PlayersHandler");
                throw new RuntimeException(e);
            }
            boolean legalAction;
            if (thePlayer.getActiveCharacter() == null) {
                legalAction = rules.doAction(action);
            } else {
                legalAction = thePlayer.getActiveCharacter().getRules().doAction(action);
            }
            if (!legalAction)
                sendPossibleActions(true);
            else {
                // register action
                game.getPlayerHandler().getCurrentPlayer().registerAction(action);
                if (action.getActionType() == ActionType.CHOOSE_CLOUD)
                    game.getPlayerHandler().getCurrentPlayer().registerAction(new Action(ActionType.END));
            }
            for (Player player : game.getPlayerHandler().getPlayers())
                if (player.getNumOfTowers() == 0) {
                    endImmediately = true;
                    break;
                }
            if (game.getIslandHandler().getIslands().size() <= 3)
                endImmediately = true;
            notifyAll();
        }
    }

    /**
     * Sets the assistant card chosen by the player
     * @param nickname the nickname of the player who played the card
     * @param card the chosen card
     */
    public void setAssistantCard(String nickname, AssistantCard card){
        synchronized (this) {
            if (checkIfIsCurrentPlayer(nickname) && checkAssistantCard(card)) {
                try {
                    game.getPlayerHandler().getCurrentPlayer().setLastCardUsed(card);
                    isCardChosen = true;
                    logger.log(4, 'g', "Set assistant card with id " + card.getCardID() + " to " + nickname);
                    notifyAll();
                } catch (CardException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    /**
     * Checks if the player can play the chosen card
     * @param card the chosen card
     * @return true if he can play the chosen card
     */
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

    /**
     * Checks if the nickname is that of the current player
     * @param nickname the player's nickname
     * @return true if he is the current player
     */
    private boolean checkIfIsCurrentPlayer(String nickname){
        return nickname.equalsIgnoreCase(game.getPlayerHandler().getCurrentPlayer().getNickname());
    }

    /**
     * Checks if the players have run out of cards or the bag is empty
     * @return true if is te last round
     */
    public boolean isLastRound(){
        //No more cards or no more students
        return game.getPlayerHandler().playerWithNoMoreCards() || game.getStudentsBag().isEmpty();
    }

    /**
     * Sends player disconnect message and closes sockets
     * @param playerId id of the player who logged out
     */
    public void setAsDisconnected(String playerId) {
        synchronized (this) {
            try {
                String nickname = game.getPlayerHandler().getPlayerById(playerId).getNickname();
                virtualView.sendToEveryone(new ShowDisconnection(nickname));
                virtualView.closeAll();
                this.status = GameStatus.INACTIVE;
                notifyAll();
            } catch (PlayerException ignored) {
            }
        }
    }

    /**
     * Returns the game state
     * @return the game state
     */
    public GameStatus getStatus() {
        return status;
    }
}
