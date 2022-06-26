package it.polimi.ingsw.client.gui.components;

import it.polimi.ingsw.client.GUIView;
import it.polimi.ingsw.client.gui.utils.ClientData;
import it.polimi.ingsw.client.gui.utils.dragAndDrop.*;
import it.polimi.ingsw.client.gui.utils.Tmp;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Piece;
import it.polimi.ingsw.network.messages.SetAction;
import it.polimi.ingsw.util.Action;
import it.polimi.ingsw.util.ActionType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.*;
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

    @FXML
    private void initialize() {
        for (Node student : entrance.getChildren())
            setOnDragStudentHandlers(student);
        setDiningRoomDropListener();
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
            entrance.getChildren().get(i).getStyleClass().add("student-hover");
        }

        // Set dining room students
        Map<Piece, Integer> studentsMap = board.getStudentsRoom();
        for(Piece piece : studentsMap.keySet()) {
            for (int i = 0; i < studentsMap.get(piece); i++) {
                getDiningRoomByPiece(piece).getChildren().get(i).getStyleClass().add(Tmp.pieceToClassName(piece));
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

    private void setOnDragStudentHandlers(Node s) {
        s.setOnDragDetected(e -> {
            if (s.getStyleClass().size() > 0) { //Active drag only if there is  a student
                Dragboard db = s.startDragAndDrop(TransferMode.ANY);
                ClipboardContent cc = new ClipboardContent();
                DragAndDropInfo ddi =  new DragAndDropInfo(DragOrigin.ENTRANCE,
                        DragType.PIECE,
                        Tmp.classNameToPiece(s.getStyleClass().get(0))
                );
                cc.putString(DragAndDropUtils.toString(ddi));
                db.setContent(cc);
                db.setDragView(DragAndDropUtils.getDragBoardStudentImage(Tmp.classNameToPiece(s.getStyleClass().get(0))));
                s.getStyleClass().clear();
            }
            e.consume();
        });
        s.setOnDragDone(e -> {
            Dragboard db = e.getDragboard();
            DragAndDropInfo ddi = DragAndDropUtils.fromString(db.getString());
            switch (ddi.getDestination()) {
                case DINING:
                    GUIView.getServerHandler().send(new SetAction(ClientData.getInstance().getPlayer().getNickname(),
                            new Action(ActionType.MOVE_STUDENT_TO_DININGROOM, ddi.getPiece())
                    ));
                    break;
                case ISLAND:
                    GUIView.getServerHandler().send(new SetAction(ClientData.getInstance().getPlayer().getNickname(),
                            new Action(ActionType.MOVE_STUDENT_TO_ISLAND, ddi.getPiece(), ddi.getIslandId())
                    ));
                    break;
                default:
                    s.getStyleClass().clear();
                    s.getStyleClass().add(Tmp.pieceToClassName(ddi.getPiece()));
                    s.getStyleClass().add("student-hover");
            }
            e.consume();
        });
    }

    private void setDiningRoomDropListener() {
        diningRoom.setOnDragEntered(e -> {
            Dragboard db = e.getDragboard();
            DragAndDropInfo ddi = DragAndDropUtils.fromString(db.getString());
            if (ddi.getType() == DragType.PIECE && DragAndDropUtils.diningRoomAcceptsStudents()) {
                diningRoom.getStyleClass().add("dining-room-hover");
                getDiningRoomByPiece(ddi.getPiece()).getStyleClass().add("dining-room-hover");
            }
            e.consume();
        });
        diningRoom.setOnDragExited(e -> {
            Dragboard db = e.getDragboard();
            DragAndDropInfo ddi = DragAndDropUtils.fromString(db.getString());
            if (ddi.getType() == DragType.PIECE && DragAndDropUtils.diningRoomAcceptsStudents()) {
                diningRoom.getStyleClass().remove("dining-room-hover");
                getDiningRoomByPiece(ddi.getPiece()).getStyleClass().remove("dining-room-hover");
            }
            e.consume();
        });
        diningRoom.setOnDragOver(e -> {
            Dragboard db = e.getDragboard();
            DragAndDropInfo ddi = DragAndDropUtils.fromString(db.getString());
            if (ddi.getType() == DragType.PIECE && DragAndDropUtils.diningRoomAcceptsStudents()) {
                e.acceptTransferModes(TransferMode.MOVE);
            }
            e.consume();
        });
        diningRoom.setOnDragDropped(e -> {
            Dragboard db = e.getDragboard();
            DragAndDropInfo ddi = DragAndDropUtils.fromString(db.getString());
            ddi.setDestination(DropDestination.DINING);
            db.clear();
            ClipboardContent cc = new ClipboardContent();
            cc.putString(DragAndDropUtils.toString(ddi));
            db.setContent(cc);
            e.consume();
        });
    }

    private Pane getDiningRoomByPiece(Piece piece) {
        switch (piece) {
            case FROG:
                return diningRoomGreen;
            case DRAGON:
                return diningRoomRed;
            case GNOME:
                return diningRoomYellow;
            case FAIRY:
                return diningRoomPurple;
            case UNICORN:
                return diningRoomBlue;
            default:
                throw new RuntimeException("Invalid key in students map");
        }
    }
}
