package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.GUIView;
import it.polimi.ingsw.client.gui.components.IslandGUI;
import it.polimi.ingsw.client.gui.components.PlayerBoard;
import it.polimi.ingsw.client.gui.components.PlayerDashboard;
import it.polimi.ingsw.client.gui.utils.ClientData;
import it.polimi.ingsw.client.gui.utils.GUISwitcher;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.IslandsHandler;
import it.polimi.ingsw.model.Piece;
import it.polimi.ingsw.util.GameMode;
import it.polimi.ingsw.util.exception.PlayerException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
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

   private PlayerBoard playerBoardController;
   private PlayerDashboard playerDashboard;

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

      playerDashboard = new PlayerDashboard();
      playerDashboardPane.getChildren().add(playerDashboard.getRoot());

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
      boolean[] emptyRows = new boolean[5];
      Arrays.fill(emptyRows, true);
      boolean[] emptyColumns = new boolean[11];
      Arrays.fill(emptyColumns, true);
      boolean mother;
      IslandGUI islandGUI;
      //int[] dim = {1,3,4,1,1,2};
      for(Island island:islands){
      //for(int j = 0; j < dim.length; j++){
         mother = island.getID() == handler.getMotherNature();
         //for(int i = 0; i < dim[j]; i++){
         for(int i = 0; i < island.getSize(); i++){
            islandGUI = new IslandGUI();
            island.addStudent(Piece.UNICORN);
            island.addStudent(Piece.DRAGON);
            island.addStudent(Piece.GNOME);
            island.addStudent(Piece.FAIRY);
            island.addStudent(Piece.FROG);
            if(island.getIslandOwner() != null)
               islandGUI.setTower(island.getIslandOwner().getTowerColor());
            if(island.getSize()==1 || i==1) {
               islandGUI.setStudents(island.getStudentsOnIsland());
               if(mother)
                  islandGUI.setMotherNature();
            }
            //TODO: mother nature
            if(i!=0){
               y += relativePosition[index][0];
               x += relativePosition[index][1];
            }else{
               y = absolutePositions[index][0];
               x = absolutePositions[index][1];
            }
            emptyColumns[x] = false;
            emptyRows[y] = false;
            GridPane.setConstraints(islandGUI.getRoot(), x, y);
            islandsGridPane.getChildren().add(islandGUI.getRoot());
            index = (index + 1)%12;
         }
         islandsGridPane.getRowConstraints().clear();
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
      }
   }

   public void showGameHandler() {

      CLIIslandBoard(clientData.getGame().getIslandHandler());

      Board board;
      try {
         board = clientData.getGame().getPlayerHandler().getPlayerById(GUIView.getPlayerId()).getPlayerBoard();
         playerDashboard.setPlayerInfo(clientData.getGame().getPlayerHandler().getPlayerById(GUIView.getPlayerId()), clientData.getGameInfo().getGameMode());
      } catch (PlayerException e) {
         throw new RuntimeException(e);
      }
      playerBoardController.setEntranceStudents(board.getStudentsEntrance());
      playerBoardController.setDiningRoom(board.getStudentsRoom());
      ensureActive();
   }

}
