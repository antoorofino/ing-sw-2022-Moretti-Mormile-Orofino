package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.GUIView;
import it.polimi.ingsw.client.gui.components.*;
import it.polimi.ingsw.client.gui.utils.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.util.GameMode;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.util.*;

public class GameMainSceneController extends SceneController {
   @FXML
   public Label messageTextLabel;
   @FXML
   public AnchorPane islandsAnchorPane;
   @FXML
   public Pane playerDashboardPane;
   @FXML
   public Pane playerBoard;
   @FXML
   public VBox buttonsVBox;
   @FXML
   public Button charactersButton;
   @FXML
   public Button boardsButton;
   @FXML
   public Button playedCardsButton;
   @FXML
   public Button yourCardsButton;
   @FXML
   public PopUpContainerController popUpController;
   private IslandsGridGUI islandsGridGUI;
   private PlayerBoard playerBoardController;
   private PlayerDashboard playerDashboardController;
   private YourCardsPopUp yourCardsPopUp;
   private List<BoardPopUp> boardPopUps;
   private PlayedCardsPopUp playedCardsPopUp;

   private final GUISwitcher switcher = GUISwitcher.getInstance();
   private final ClientData clientData = ClientData.getInstance();

   @FXML
   public void initialize() {
      //Creat GUI components and bind their roots to the layout
      islandsGridGUI = new IslandsGridGUI();
      AnchorPane.setTopAnchor(islandsGridGUI.getRoot(), 0.0);
      AnchorPane.setRightAnchor(islandsGridGUI.getRoot(), 0.0);
      AnchorPane.setBottomAnchor(islandsGridGUI.getRoot(), 0.0);
      AnchorPane.setLeftAnchor(islandsGridGUI.getRoot(), 0.0);
      islandsAnchorPane.getChildren().add(islandsGridGUI.getRoot());

      playerBoardController = new PlayerBoard();
      playerBoard.getChildren().add(playerBoardController.getRoot());

      playerDashboardController = new PlayerDashboard();
      playerDashboardPane.getChildren().add(playerDashboardController.getRoot());

      //Create popUps
      yourCardsPopUp = new YourCardsPopUp();
      boardPopUps = new ArrayList<>();
      playedCardsPopUp = new PlayedCardsPopUp();

      //Set onClicked listener for left buttons
      charactersButton.setOnMouseClicked(e -> {
         alertPaneController.showError("NOT YET IMPLEMENTED");
      });

      boardsButton.setOnMouseClicked(e -> {
         popUpController.clear();
         boardPopUpInitialize();
         for (BoardPopUp boardPopUp : boardPopUps)
            popUpController.add(boardPopUp.getRoot());
         popUpController.display();
      });

      yourCardsButton.setOnMouseClicked(e -> {
         popUpController.clear();
         yourCardsPopUp.setCards();
         popUpController.add(yourCardsPopUp.getRoot());
         popUpController.display();
      });

      playedCardsButton.setOnMouseClicked(e -> {
         popUpController.clear();
         playedCardsPopUp.setCards();
         popUpController.add(playedCardsPopUp.getRoot());
         popUpController.display();
      });
   }

   private void boardPopUpInitialize() {
      if (boardPopUps.size() == 0) {
         for (int i = 1; i < clientData.getGame().getPlayerHandler().getNumPlayers(); i++) {
            boardPopUps.add(new BoardPopUp());
         }
      }
      int j = 0;
      for(Player player : clientData.getGame().getPlayerHandler().getPlayers()) {
         if (!player.getId().equals(GUIView.getPlayerId())) {
            boardPopUps.get(j).setPlayerInfo(player, clientData.getGame().getGameMode(), clientData.getGame().getTeacherHandler());
            j++;
         }
      }
   }

   public void showGameHandler() {
      //FIXME: should be moved somewhere else ?
      if(clientData.getGame().getGameMode() == GameMode.BASIC) {
         buttonsVBox.getChildren().remove(charactersButton);
      }

      //Islands grid update
      islandsGridGUI.setGrid();

      //Player dashboard update
      playerDashboardController.setPlayerInfo(clientData.getPlayer(), clientData.getGame().getGameMode());

      //Player board update
      playerBoardController.setBoard(clientData.getPlayer().getPlayerBoard(),
              clientData.getGame().getTeacherHandler().getTeachersByPlayerId(GUIView.getPlayerId()));

      //PopUps update
      boardPopUpInitialize();
      yourCardsPopUp.setCards();
      playedCardsButton.setDisable(playedCardsPopUp.setCards());
   }

   public void setMessage(String message) {
      messageTextLabel.setText(message);
   }

   public void showError(String message) {
      alertPaneController.showError(message);
   }
}
