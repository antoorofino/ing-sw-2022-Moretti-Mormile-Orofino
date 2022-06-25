package it.polimi.ingsw.client.gui.components;

import it.polimi.ingsw.client.gui.utils.Tmp;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Piece;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class PlayerBoard {
    @FXML
    private Pane entrance;
    @FXML
    private Pane diningRoom;
    @FXML
    private Pane diningRoomGreen;
    @FXML
    private Pane diningRoomRed;
    @FXML
    private Pane diningRoomYellow;
    @FXML
    private Pane diningRoomPurple;
    @FXML
    private Pane diningRoomBlue;
    @FXML
    private Pane teachers;
    @FXML
    private Pane teacherGreen;
    @FXML
    private Pane teacherRed;
    @FXML
    private Pane teacherYellow;
    @FXML
    private Pane teacherPurple;
    @FXML
    private Pane teacherBlue;
    Parent root = null;

    public Parent getRoot() {
        return root;
    }

    public PlayerBoard() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/fxml/playerBoard.fxml"));
        fxmlLoader.setController(this);
        try {
            root = fxmlLoader.load();
        } catch (IOException ignored) {
        }
    }

    public void setBoard(Board board, List<Piece> teachersList) {
        // Clear entrance
        for (Node piece : entrance.getChildren()) {
            piece.getStyleClass().clear();
        }
        // Clear dining rooms
        for (Node room : diningRoom.getChildren()) {
            for (Node piece : ((Pane) room).getChildren()) {
                piece.getStyleClass().clear();
            }
        }
        // Clear teachers
        for (Node piece : teachers.getChildren()) {
            piece.getStyleClass().clear();
        }

        // Set entrance students
        List<Piece> entranceArray = board.getStudentsEntrance();
        for (int i = 0; i < entranceArray.size(); i++) {
            entrance.getChildren().get(i).getStyleClass().add(Tmp.pieceToClassName(entranceArray.get(i)));
        }

        // Set dining room students
        Map<Piece, Integer> studentsMap = board.getStudentsRoom();
        for(Piece piece : studentsMap.keySet()) {
            Pane diningRoom;
            switch (piece) {
                case FROG:
                    diningRoom = diningRoomGreen;
                    break;
                case DRAGON:
                    diningRoom = diningRoomRed;
                    break;
                case GNOME:
                    diningRoom = diningRoomYellow;
                    break;
                case FAIRY:
                    diningRoom = diningRoomPurple;
                    break;
                case UNICORN:
                    diningRoom = diningRoomBlue;
                    break;
                default:
                    throw new RuntimeException("Invalid key in students map");
            }
            for (int i = 0; i < studentsMap.get(piece); i++) {
                diningRoom.getChildren().get(i).getStyleClass().add(Tmp.pieceToClassName(piece));
            }
        }
        // Set teachers
        for(Piece teacher : teachersList) {
            switch (teacher) {
                case FROG:
                    teacherGreen.getStyleClass().add(Tmp.pieceToClassName(teacher));
                    break;
                case DRAGON:
                    teacherRed.getStyleClass().add(Tmp.pieceToClassName(teacher));
                    break;
                case GNOME:
                    teacherYellow.getStyleClass().add(Tmp.pieceToClassName(teacher));
                    break;
                case FAIRY:
                    teacherPurple.getStyleClass().add(Tmp.pieceToClassName(teacher));
                    break;
                case UNICORN:
                    teacherBlue.getStyleClass().add(Tmp.pieceToClassName(teacher));
                    break;
            }
        }
    }
}
