package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.GUIView;
import it.polimi.ingsw.client.gui.components.*;
import it.polimi.ingsw.client.gui.utils.ClientData;
import it.polimi.ingsw.client.gui.utils.GUISwitcher;
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
   public GridPane islandsGridPane;
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

   private PlayerBoard playerBoardController;
   private PlayerDashboard playerDashboardController;
   private final YourCardsPopUp yourCardsPopUp = new YourCardsPopUp();
   private final List<BoardPopUp> boardPopUps = new ArrayList<>();
   private final PlayedCardsPopUp playedCardsPopUp = new PlayedCardsPopUp();

   private final GUISwitcher switcher = GUISwitcher.getInstance();
   private final ClientData clientData = ClientData.getInstance();


   @FXML
   public void initialize() {
       //TODO: bind grid pane width to match height

      switcher.getPrimaryStage().widthProperty().addListener((observable, odlValue, newValue) -> {
         Double gridWidth = islandsGridPane.getHeight()/5 * 11;
         double anchorValue = (((Double) newValue) - gridWidth)/2.0;
         if (anchorValue < 10)
            anchorValue = 10.0;
         AnchorPane.setRightAnchor(islandsGridPane,anchorValue);
         AnchorPane.setLeftAnchor(islandsGridPane,anchorValue);
      });

      islandsGridPane.heightProperty().addListener((observable, odlValue, newValue)  -> {
         Double gridWidth = ((Double) newValue)/5 * 11;
         Double stageWidth = switcher.getPrimaryStage().getWidth();
         double anchorValue = (stageWidth - gridWidth)/2.0;
         if (anchorValue < 10)
            anchorValue = 10.0;
         AnchorPane.setRightAnchor(islandsGridPane,anchorValue);
         AnchorPane.setLeftAnchor(islandsGridPane,anchorValue);
      });

      playerBoardController = new PlayerBoard();
      playerBoard.getChildren().add(playerBoardController.getRoot());

      playerDashboardController = new PlayerDashboard();
      playerDashboardPane.getChildren().add(playerDashboardController.getRoot());

      /*
      Map<Piece, Integer> map = new HashMap<>();
      map.put(Piece.DRAGON, 4);
      playerBoardController.setDiningRoom(map);
      playerBoardController.setTeachers(Arrays.asList(Piece.DRAGON));
      playerBoardController.setEntranceStudents(Arrays.asList(Piece.DRAGON, Piece.FROG, Piece.FROG, Piece.FAIRY));
      DelayAction.executeLater(() -> playerBoardController.setTeachers(Arrays.asList(Piece.FROG)));
       */
      /*
      student1.setOnDragDetected(e -> {
         if (student1.getStyleClass().size() > 0) {
            Dragboard db = student1.startDragAndDrop(TransferMode.ANY);
            ClipboardContent cc = new ClipboardContent();
            cc.putString(student1.getStyleClass().get(0));
            student1.getStyleClass().clear();
            db.setContent(cc);
            db.setDragView(new Image(String.valueOf(getClass().getResource("/gui/images/student_blue.png")), 20.0, 20.0, true, true));
         }
         e.consume();
      });

      student1.setOnDragDone(e -> {
         if (!e.getDragboard().hasString()) {
            System.out.println("success");
         } else {
            System.out.println("error");
            student1.getStyleClass().add(e.getDragboard().getString());
         }
         e.consume();
      });

      islandsGridPane.setOnDragOver(e -> {
         if (e.getDragboard().hasString()) {
            e.acceptTransferModes( TransferMode.COPY_OR_MOVE );
         }
         e.consume();
      });

      islandsGridPane.setOnDragEntered(e -> {
         islandsGridPane.getStyleClass().add("hover-selected");
         e.consume();
      });

      islandsGridPane.setOnDragExited(e -> {
         islandsGridPane.getStyleClass().remove("hover-selected");
         e.consume();
      });

      islandsGridPane.setOnDragDropped(e -> {
         e.getDragboard().clear();
         e.setDropCompleted(true);
         e.consume();
      });
       */

      //CLIIslandBoardTest(Arrays.asList(1,1,1,1,1,1,1,1,1,1,1,1));
      //CLIIslandBoardTest(Arrays.asList(2,1,3,1,4,1));
      //CLIIslandBoardTest(Arrays.asList(1,2,2,1,2,1,3));

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

   private void updateIslands(IslandsHandler handler) {

   }

   private void CLIIslandBoard(IslandsHandler handler) {
      ArrayList<Island> islands = handler.getIslands();
      int index = (12 - handler.getCountsLastMerge())%12; // countsLastMerge
      final int[][] absolutePositions = { { 0, 1 }, { 0, 3 }, { 0, 5 }, { 0, 7 }, { 0, 9 }, { 2, 10 }, { 4, 9 }, { 4, 7 }, { 4, 5 }, { 4, 3 }, { 4, 1 }, { 2, 0 } };
      final int[][] relativePosition = { { -1, 0 }, { 0, 1 }, { 0, 1 }, { 0, 1 }, { 0, 1 }, { 0, 1 }, { 1, 0 }, { 0, -1 }, { 0, -1 }, { 0, -1 }, { 0, -1 }, { 0, -1 }};
      int x = 0;
      int y = 0;
      //boolean[] emptyRows = new boolean[5];
      //Arrays.fill(emptyRows, true);
      //boolean[] emptyColumns = new boolean[11];
      //Arrays.fill(emptyColumns, true);
      boolean mother;
      IslandGUI islandGUI;
      for(Island island:islands){
         mother = island.getID() == handler.getMotherNature();
         for(int i = 0; i < island.getSize(); i++){
            islandGUI = new IslandGUI();
            if(island.getIslandOwner() != null)
               islandGUI.setTower(island.getIslandOwner().getTowerColor());
            if(island.getSize()==1 || i==1) {
               islandGUI.setStudents(island.getStudentsOnIsland());
               if(mother)
                  islandGUI.setMotherNature();
            }
            if(i!=0){
               y += relativePosition[index][0];
               x += relativePosition[index][1];
            }else{
               y = absolutePositions[index][0];
               x = absolutePositions[index][1];
            }
            //emptyColumns[x] = false;
            //emptyRows[y] = false;
            GridPane.setConstraints(islandGUI.getRoot(), x, y);
            islandsGridPane.getChildren().add(islandGUI.getRoot());
            index = (index + 1)%12;
         }
         /*islandsGridPane.getRowConstraints().clear();
         RowConstraints rowConstraints;
         for (boolean emptyRow : emptyRows) {
            rowConstraints = new RowConstraints();
            if (!emptyRow) {
               rowConstraints.setVgrow(Priority.ALWAYS);
            } else {
               rowConstraints.setMinHeight(20.0);
               rowConstraints.setVgrow(Priority.NEVER);
            }
            islandsGridPane.getRowConstraints().add(rowConstraints);
         }
         islandsGridPane.getColumnConstraints().clear();
         ColumnConstraints columnConstraints;
         for (boolean emptyColumn : emptyColumns) {
            columnConstraints = new ColumnConstraints();
            if (!emptyColumn) {
               columnConstraints.setHgrow(Priority.ALWAYS);
            } else {
               columnConstraints.setMinWidth(20.0);
               columnConstraints.setHgrow(Priority.NEVER);
            }
            islandsGridPane.getColumnConstraints().add(columnConstraints);
         }
          */
      }
   }

   public void showGameHandler() {

      //FIXME: should be moved somewhere else
      //Remove
      if(clientData.getGame().getGameMode() == GameMode.BASIC) {
         buttonsVBox.getChildren().remove(charactersButton);
      }

      //Islands grid update
      CLIIslandBoard(clientData.getGame().getIslandHandler());

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

}
