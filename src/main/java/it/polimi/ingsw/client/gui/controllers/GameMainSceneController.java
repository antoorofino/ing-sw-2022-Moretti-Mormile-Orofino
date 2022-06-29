package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.GUIView;
import it.polimi.ingsw.client.gui.components.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.util.Action;
import it.polimi.ingsw.util.ActionType;
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
   public Pane activeCharacterPane;
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
   private SwapArea swapArea;
   private PlayerDashboard playerDashboardController;
   private CharactersPopUp charactersPopUp;
   private YourCardsPopUp yourCardsPopUp;
   private List<BoardPopUp> boardPopUps;
   private PlayedCardsPopUp playedCardsPopUp;

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

      swapArea = new SwapArea();

      playerDashboardController = new PlayerDashboard();
      playerDashboardPane.getChildren().add(playerDashboardController.getRoot());

      //Create popUps
      charactersPopUp = new CharactersPopUp();
      yourCardsPopUp = new YourCardsPopUp();
      boardPopUps = new ArrayList<>();
      playedCardsPopUp = new PlayedCardsPopUp();

      //Set onClicked listener for left buttons
      charactersButton.setOnMouseClicked(e -> {
         popUpController.clear();
         charactersPopUp.setCards();
         popUpController.add(charactersPopUp.getRoot());
         popUpController.display();
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
         for (int i = 1; i < data.getGame().getPlayerHandler().getNumPlayers(); i++) {
            boardPopUps.add(new BoardPopUp());
         }
      }
      int j = 0;
      for(Player player : data.getGame().getPlayerHandler().getPlayers()) {
         if (!player.getId().equals(GUIView.getPlayerId())) {
            boardPopUps.get(j).setPlayerInfo(player, data.getGame().getGameMode(), data.getGame().getTeacherHandler());
            j++;
         }
      }
   }

   private void setCharacterCard() {
      Character activeCard = data.getGame().getPlayerHandler().getPlayers().stream()
              .map(Player::getActiveCharacter)
              .filter(Objects::nonNull)
              .findFirst()
              .orElse(null);
      boolean isTheCurrentPlayer = data.getPlayer().getActiveCharacter() != null;
      if (activeCard != null) {
         boolean cardIsActive = isTheCurrentPlayer &&
                 data.getPlayer().getRoundActions().getActionsList().stream()
                         .map(Action::getActionType).filter(t -> t == ActionType.ACTIVATED_CHARACTER).findFirst().orElse(null) == null;
         // Show swap area if enabled
         if (cardIsActive && (data.getPlayer().getActiveCharacter().getID() == 7 || data.getPlayer().getActiveCharacter().getID() == 10 )) {
            swapArea.init();
            playerDashboardPane.getChildren().clear();
            playerDashboardPane.getChildren().add(swapArea.getRoot());
         }
         // Create the cart and set interactions features if this player has activated it
         CharacterCardGUI cardGUI = new CharacterCardGUI();
         cardGUI.setCharacterCard(activeCard, cardIsActive, swapArea);
         activeCharacterPane.getChildren().add(cardGUI.getRoot());
      }
   }

   public void showGameHandler() {
      // Islands grid update
      islandsGridGUI.setGrid();

      // Player dashboard update
      playerDashboardPane.getChildren().clear();
      playerDashboardPane.getChildren().add(playerDashboardController.getRoot());
      playerDashboardController.setPlayerInfo(data.getPlayer(), data.getGame().getGameMode());

      // Character cards
      if(data.getGame().getGameMode() == GameMode.BASIC) { // Basic mode
         buttonsVBox.getChildren().remove(charactersButton);
      } else { // Expert mode
         activeCharacterPane.getChildren().clear();
         setCharacterCard();
      }

      // Player board update
      playerBoardController.setBoard(data.getPlayer().getPlayerBoard(),
              data.getGame().getTeacherHandler().getTeachersByPlayerId(GUIView.getPlayerId()),
              true,
              swapArea
      );

      // PopUps update
      boardPopUpInitialize();
      yourCardsPopUp.setCards();
      playedCardsButton.setDisable(playedCardsPopUp.setCards());
      if (data.getGame().getGameMode() == GameMode.EXPERT)
         charactersPopUp.setCards();
   }

   public void setMessage(String message) {
      messageTextLabel.setText(message);
   }

   public void showError(String message) {
      alertPaneController.showError(message);
      showGameHandler();
   }
}
