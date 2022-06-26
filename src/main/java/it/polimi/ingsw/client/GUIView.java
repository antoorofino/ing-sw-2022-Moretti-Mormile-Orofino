package it.polimi.ingsw.client;

import it.polimi.ingsw.client.gui.utils.ClientData;
import it.polimi.ingsw.client.gui.utils.GUISwitcher;
import it.polimi.ingsw.client.gui.utils.DelayAction;
import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.network.messages.AskGameListMessage;
import it.polimi.ingsw.util.GameListInfo;
import it.polimi.ingsw.util.RoundActions;
import it.polimi.ingsw.util.TowerColor;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;

public class GUIView extends Application implements View {
    private static ServerHandler serverHandler;
    private static String playerId;
    private final GUISwitcher switcher = GUISwitcher.getInstance();
    private final ClientData clientData = ClientData.getInstance();

    /**
     * @param serverHandler The serverHandler
     */
    @Override
    public void setServerHandler(ServerHandler serverHandler) {
        GUIView.serverHandler = serverHandler;
    }

    /**
     *
     */
    public static ServerHandler getServerHandler() {
        return serverHandler;
    }

    public static String getPlayerId() {
        return GUIView.playerId;
    }

    /**
     *
     */
    @Override
    public void run() {
        launch();
    }

    /**
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) {
        stage.setTitle("Eriantys");
        //TODO: add app icon
        stage.setMinHeight(750.0);
        stage.setMinWidth(900.0);
        stage.setHeight(750.0);
        stage.setWidth(900.0);
        stage.setScene(new Scene(new Group()));
        switcher.initialise(stage);
        switcher.setDefaultController();
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        serverHandler.close();
        super.stop();
    }

    /**
     * @param playerId
     */
    @Override
    public void setPlayerId(String playerId) {
        Platform.runLater(() -> {
            GUIView.playerId = playerId;
            DelayAction.executeLater(() -> {
                switcher.getLobbySceneController().activate();
                serverHandler.send(new AskGameListMessage(playerId));
            });
        });
    }

    /**
     *
     */
    @Override
    public void askNewGameName() {
        Platform.runLater(() -> {
            switcher.getLobbySceneController().newGameNameHandler();
        });
    }

    /**
     *
     */
    @Override
    public void askNewGameChoice() {
        Platform.runLater(() -> {
            switcher.getLobbySceneController().gameAlreadyFullHandler();
            serverHandler.send(new AskGameListMessage(playerId));
        });
    }

    /**
     * @param isFirstRequest
     */
    @Override
    public void askNickname(boolean isFirstRequest) {
        Platform.runLater(() -> {
            DelayAction.executeLater(() -> {
                switcher.getPlayerInfoSceneController().askNicknameHandler(isFirstRequest);
            });
        });
    }

    /**
     * @param possibleColors  All the possible Color
     * @param isFirstRequest
     */
    @Override
    public void askTowerColor(List<TowerColor> possibleColors, boolean isFirstRequest) {
        Platform.runLater(() -> {
            switcher.getPlayerInfoSceneController().askTowerColor(possibleColors, isFirstRequest);
        });
    }

    /**
     * @param cards All the possible cards
     * @param game
     */
    @Override
    public void askAssistantCard(List<AssistantCard> cards, GameModel game) {
        clientData.setGame(game);
        clientData.setPossibleCards(cards);
        Platform.runLater(() -> {
            switcher.getGameMainSceneController().setMessage("It's your turn, play an assistant card");
            switcher.getGameMainSceneController().showGameHandler();
        });
    }

    /**
     * @param roundActions    All the possible actions
     * @param isInvalidAction
     */
    @Override
    public void askAction(RoundActions roundActions, boolean isInvalidAction) {
        Platform.runLater(() -> {
            switcher.getGameMainSceneController().setMessage("It's your turn, do an  (TODO)");
            //TODO: not yet implemented
        });
    }

    /**
     * @param gamesList
     */
    @Override
    public void showGamesList(List<GameListInfo> gamesList) {
        Platform.runLater(() -> {
            switcher.getLobbySceneController().gameListHandler(gamesList);
        });
    }

    /**
     * @param game
     * @param firstPlayerNickname
     */
    @Override
    public void showGameStart(GameModel game, String firstPlayerNickname) {
        clientData.setGame(game);
        Platform.runLater(() -> {
            switcher.getGameMainSceneController().showGameHandler();
            if (!firstPlayerNickname.equals(clientData.getPlayer().getNickname()))
                switcher.getGameMainSceneController().setMessage(firstPlayerNickname + " starts the match");
            DelayAction.executeLater(() -> switcher.getPlayerInfoSceneController().showGameStart());
        });
    }

    /**
     * @param game
     */
    @Override
    public void showGame(GameModel game) {
        clientData.setGame(game);
        Platform.runLater(() -> {
            if (game.getPlayerHandler().getCurrentPlayer().getNickname().equals(clientData.getPlayer().getNickname()))
                switcher.getGameMainSceneController().setMessage("It is your turn");
            else
                switcher.getGameMainSceneController().setMessage("It is " + game.getPlayerHandler().getCurrentPlayer().getNickname() + "'s turn");
            switcher.getGameMainSceneController().showGameHandler();
        });
    }

    /**
     *
     */
    @Override
    public void showLastRound() {
        Platform.runLater(() -> {
            //TODO: not yet implemented
        });
    }

    /**
     *
     */
    @Override
    public void showQueuedMessage() {
        Platform.runLater(() -> {
            DelayAction.executeLater(() -> switcher.getPlayerInfoSceneController().showQueuing());
        });
    }

    /**
     * @param winnerNickname The nickname of the winner
     */
    @Override
    public void showGameEndMessage(String winnerNickname) {
        Platform.runLater(() -> {
            //TODO: not yet implemented
        });
    }

    /**
     *
     */
    @Override
    public void showConnectionErrorMessage() {
        Platform.runLater(() -> {
            switcher.getDisconnectedSceneController().disconnectedHandler("Server unreachable");
            serverHandler.close();
        });
    }

    /**
     * @param playerDisconnected
     */
    @Override
    public void showDisconnection(String playerDisconnected) {
        Platform.runLater(() -> {
            switcher.getDisconnectedSceneController().disconnectedHandler(playerDisconnected + " disconnected");
            serverHandler.close();
        });
    }

    @Override
    public void showErrorOnConnection() {
        Platform.runLater(() -> {
            switcher.getHomeSceneController().connectionErrorHandler();
        });
    }
}
