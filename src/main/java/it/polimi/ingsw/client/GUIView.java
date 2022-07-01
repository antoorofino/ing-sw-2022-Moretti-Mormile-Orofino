package it.polimi.ingsw.client;

import it.polimi.ingsw.client.gui.utils.ClientData;
import it.polimi.ingsw.client.gui.utils.GUISwitcher;
import it.polimi.ingsw.client.gui.utils.DelayAction;
import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.network.messages.AskGameListMessage;
import it.polimi.ingsw.util.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.List;

/**
 * Implement the game interface view for the gui
 */
public class GUIView extends Application implements View {
    private static ServerHandler serverHandler;
    private static String playerId;
    private final GUISwitcher switcher = GUISwitcher.getInstance();
    private final ClientData clientData = ClientData.getInstance();

    /**
     * Sets the server handler
     * @param serverHandler The serverHandler
     */
    @Override
    public void setServerHandler(ServerHandler serverHandler) {
        GUIView.serverHandler = serverHandler;
    }

    /**
     * Returns the server handler
     */
    public static ServerHandler getServerHandler() {
        return serverHandler;
    }

    /**
     * Returns the player id
     * @return the player id
     */
    public static String getPlayerId() {
        return GUIView.playerId;
    }

    @Override
    public void run() {
        launch();
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Eriantys");
        stage.setMinHeight(750.0);
        stage.setMinWidth(1000.0);
        stage.setHeight(750.0);
        stage.setWidth(1000.0);
        stage.setScene(new Scene(new Group()));
        switcher.initialise(stage);
        switcher.setDefaultController();
        stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/gui/images/app_icon.png"))));
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        serverHandler.close();
        super.stop();
    }

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

    @Override
    public void askNewGameName() {
        Platform.runLater(() -> {
            switcher.getLobbySceneController().newGameNameHandler();
        });
    }

    @Override
    public void askNewGameChoice() {
        Platform.runLater(() -> {
            switcher.getLobbySceneController().gameAlreadyFullHandler();
            serverHandler.send(new AskGameListMessage(playerId));
        });
    }

    @Override
    public void askNickname(boolean isFirstRequest) {
        Platform.runLater(() -> {
            DelayAction.executeLater(() -> {
                switcher.getPlayerInfoSceneController().askNicknameHandler(isFirstRequest);
            });
        });
    }

    @Override
    public void askTowerColor(List<TowerColor> possibleColors, boolean isFirstRequest) {
        Platform.runLater(() -> {
            switcher.getPlayerInfoSceneController().askTowerColor(possibleColors, isFirstRequest);
        });
    }

    @Override
    public void askAssistantCard(List<AssistantCard> cards, GameModel game) {
        clientData.setGame(game);
        clientData.setPossibleCards(cards);
        Platform.runLater(() -> {
            switcher.getGameMainSceneController().setMessage("It's your turn, choose an assistant card to play");
            switcher.getGameMainSceneController().showGameHandler();
        });
    }

    @Override
    public void askAction(RoundActions roundActions, boolean isInvalidAction) {
        clientData.setPossibleActions(roundActions);
        Platform.runLater(() -> {
            String message;
            if (clientData.getPossibleActions().contains(ActionType.MOVE_STUDENT_TO_ISLAND) || clientData.getPossibleActions().contains(ActionType.MOVE_STUDENT_TO_DININGROOM))
                message = "It's your turn, move your students on the entrance";
            else if (clientData.getPossibleActions().contains(ActionType.MOVE_MOTHER_NATURE))
                message = "It's your turn, move mother nature";
            else if (clientData.getPossibleActions().contains(ActionType.CHOOSE_CLOUD))
                message = "It's your turn, select a cloud";
            else if (clientData.getPlayer().getRoundActions().getActionsList().stream()
                            .map(Action::getActionType).filter(t -> t == ActionType.CHOOSE_CHARACTER).findFirst().orElse(null) != null &&
                    clientData.getPlayer().getRoundActions().getActionsList().stream()
                            .map(Action::getActionType).filter(t -> t == ActionType.ACTIVATED_CHARACTER).findFirst().orElse(null) == null)
                message = "It's your turn, use the character's effect";
            else
                message = "It's your turn, perform an action";
            switcher.getGameMainSceneController().setMessage(message);
            if (isInvalidAction)
                switcher.getGameMainSceneController().showError("Invalid action");
        });
    }

    @Override
    public void showGamesList(List<GameListInfo> gamesList) {
        Platform.runLater(() -> {
            switcher.getLobbySceneController().gameListHandler(gamesList);
        });
    }

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

    @Override
    public void showGame(GameModel game) {
        clientData.setGame(game);
        Platform.runLater(() -> {
            if (game.getPlayerHandler().getCurrentPlayer().getNickname().equals(clientData.getPlayer().getNickname()))
                switcher.getGameMainSceneController().setMessage("It is your turn");
            else {
                clientData.resetPossibleActions();
                switcher.getGameMainSceneController().setMessage("Waiting for " + game.getPlayerHandler().getCurrentPlayer().getNickname() + " to play");
            }
            switcher.getGameMainSceneController().showGameHandler();
        });
    }

    @Override
    public void showLastRound() {
        Platform.runLater(() -> {
           switcher.getGameMainSceneController().showError("This is going to be the last round!");
        });
    }

    @Override
    public void showQueuedMessage() {
        Platform.runLater(() -> {
            DelayAction.executeLater(() -> switcher.getPlayerInfoSceneController().showQueuing());
        });
    }

    @Override
    public void showGameEndMessage(String winnerNickname) {
        Platform.runLater(() -> {
            switcher.getWinningSceneController().endMatchHandler(winnerNickname);
            serverHandler.close();
        });
    }

    @Override
    public void showConnectionErrorMessage() {
        Platform.runLater(() -> {
            switcher.getDisconnectedSceneController().disconnectedHandler("Server unreachable");
            serverHandler.close();
        });
    }

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
