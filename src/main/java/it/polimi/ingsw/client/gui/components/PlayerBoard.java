package it.polimi.ingsw.client.gui.components;

import it.polimi.ingsw.client.GUIView;
import it.polimi.ingsw.client.gui.utils.dragAndDrop.*;
import it.polimi.ingsw.client.gui.utils.PieceCssStyleHelper;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Piece;
import it.polimi.ingsw.network.messages.SetAction;
import it.polimi.ingsw.util.Action;
import it.polimi.ingsw.util.ActionType;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * Manages graphical assets and event for player board
 */
public class PlayerBoard extends ComponentGUI {
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

    /**
     * Constructor: loads the fxml for player board
     */
    public PlayerBoard() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/fxml/playerBoard.fxml"));
        fxmlLoader.setController(this);
        try {
            root = fxmlLoader.load();
        } catch (IOException ignored) {
        }
    }

    /**
     * Sets graphical assets for the board
     * @param board player's board
     * @param teachersList teacher list
     * @param isActive true if player can drop element
     * @param swapArea swap area
     */
    public void setBoard(Board board, List<Piece> teachersList, boolean isActive, SwapArea swapArea) {
        // Clear entrance
        for (Node piece : entrance.getChildren()) {
            piece.getStyleClass().clear();
            piece.setOnDragDetected(Event::consume);
        }
        // Clear dining rooms
        for (Node room : diningRoom.getChildren()) {
            for (Node piece : ((Pane) room).getChildren()) {
                piece.getStyleClass().clear();
                piece.setOnDragDetected(Event::consume);
            }
        }
        // Clear teachers
        for (Node piece : teachers.getChildren()) {
            piece.getStyleClass().clear();
        }
        // Set entrance students
        List<Piece> entranceArray = board.getStudentsEntrance();
        for (int i = 0; i < entranceArray.size(); i++) {
            entrance.getChildren().get(i).getStyleClass().add(PieceCssStyleHelper.pieceToClassName(entranceArray.get(i)));
            if (isActive)
                setOnDragEntranceStudentHandlers(entrance.getChildren().get(i), swapArea);
        }
        // Set dining room students
        Map<Piece, Integer> studentsMap = board.getStudentsRoom();
        for(Piece piece : studentsMap.keySet()) {
            for (int i = 0; i < studentsMap.get(piece); i++) {
                getDiningRoomByPiece(piece).getChildren().get(i).getStyleClass().add(PieceCssStyleHelper.pieceToClassName(piece));
                if (isActive) {
                    setOnDragDiningStudentsHandlers(getDiningRoomByPiece(piece).getChildren().get(i), swapArea);
                }
            }
        }
        if (isActive)
            setDiningRoomDropHandlers();
        // Set teachers
        for(Piece teacher : teachersList) {
            switch (teacher) {
                case FROG:
                    teacherGreen.getStyleClass().add(PieceCssStyleHelper.teacherToClassName(teacher));
                    break;
                case DRAGON:
                    teacherRed.getStyleClass().add(PieceCssStyleHelper.teacherToClassName(teacher));
                    break;
                case GNOME:
                    teacherYellow.getStyleClass().add(PieceCssStyleHelper.teacherToClassName(teacher));
                    break;
                case FAIRY:
                    teacherPurple.getStyleClass().add(PieceCssStyleHelper.teacherToClassName(teacher));
                    break;
                case UNICORN:
                    teacherBlue.getStyleClass().add(PieceCssStyleHelper.teacherToClassName(teacher));
                    break;
            }
        }
    }

    /**
     * Adds event to make possible drag student from entrance
     * @param s student
     * @param swapArea swap area to make a swap
     */
    private void setOnDragEntranceStudentHandlers(Node s, SwapArea swapArea) {
        Supplier<Boolean> entranceStudentsAreActive = () -> {
            if (data.getPossibleActions().contains(ActionType.MOVE_STUDENT_TO_ISLAND) || data.getPossibleActions().contains(ActionType.MOVE_STUDENT_TO_DININGROOM))
                return true;
            if (data.getPlayer().getActiveCharacter() != null && data.getPlayer().getRoundActions().getActionsList().stream()
                    .map(Action::getActionType).filter(t -> t == ActionType.ACTIVATED_CHARACTER).findFirst().orElse(null) == null) {
                switch (data.getPlayer().getActiveCharacter().getID()) {
                    case 7:
                    case 10:
                        return true;
                }
            }
            return false;
        };
        // Set interaction features if enabled
        if (entranceStudentsAreActive.get()) {
            s.setOnDragDetected(e -> {
                if (s.getStyleClass().size() > 0) { // Active drag only if there is  a student
                    Dragboard db = s.startDragAndDrop(TransferMode.ANY);
                    DragAndDropInfo ddi =  new DragAndDropInfo(DragOrigin.ENTRANCE,
                            DragType.PIECE,
                            PieceCssStyleHelper.classNameToPiece(s.getStyleClass().get(0))
                    );
                    db.setContent(DragAndDropUtils.toClipboardContent(ddi));
                    db.setDragView(DragAndDropUtils.getDragBoardStudentImage(PieceCssStyleHelper.classNameToPiece(s.getStyleClass().get(0))));
                    s.getStyleClass().clear();
                }
                e.consume();
            });
            s.setOnDragDone(e -> {
                Dragboard db = e.getDragboard();
                DragAndDropInfo ddi = DragAndDropUtils.fromString(db.getString());
                switch (ddi.getDestination()) {
                    case DINING:
                        GUIView.getServerHandler().send(new SetAction(data.getPlayer().getNickname(),
                                new Action(ActionType.MOVE_STUDENT_TO_DININGROOM, ddi.getPiece())
                        ));
                        break;
                    case ISLAND:
                        GUIView.getServerHandler().send(new SetAction(data.getPlayer().getNickname(),
                                new Action(ActionType.MOVE_STUDENT_TO_ISLAND, ddi.getPiece(), ddi.getIslandId())
                        ));
                        break;
                    case SWAP_AREA:
                        swapArea.setPiece1(ddi.getPiece());
                        break;
                    case NONE:
                        s.getStyleClass().clear();
                        s.getStyleClass().add(PieceCssStyleHelper.pieceToClassName(ddi.getPiece()));
                        s.getStyleClass().add("student-hover");
                }
                e.consume();
            });
            s.getStyleClass().add("student-hover");
        }
    }

    /**
     * Adds event to make possible drag student from dining room
     * @param s student
     * @param swapArea swap area to make a swap
     */
    private void setOnDragDiningStudentsHandlers(Node s, SwapArea swapArea) {
        Supplier<Boolean> diningStudentsAreActive = () -> data.getPlayer().getActiveCharacter() != null && data.getPlayer().getRoundActions().getActionsList().stream()
                .map(Action::getActionType).filter(t -> t == ActionType.ACTIVATED_CHARACTER).findFirst().orElse(null) == null &&
                data.getPlayer().getActiveCharacter().getID() == 10;
        // Set interaction features if enabled
        if (diningStudentsAreActive.get()) {
            s.setOnDragDetected(e -> {
                if (s.getStyleClass().size() > 0) { // Active drag only if there is  a student
                    Dragboard db = s.startDragAndDrop(TransferMode.ANY);
                    DragAndDropInfo ddi =  new DragAndDropInfo(DragOrigin.DINING,
                            DragType.PIECE,
                            PieceCssStyleHelper.classNameToPiece(s.getStyleClass().get(0))
                    );
                    db.setContent(DragAndDropUtils.toClipboardContent(ddi));
                    db.setDragView(DragAndDropUtils.getDragBoardStudentImage(PieceCssStyleHelper.classNameToPiece(s.getStyleClass().get(0))));
                    s.getStyleClass().clear();
                }
                e.consume();
            });
            s.setOnDragDone(e -> {
                Dragboard db = e.getDragboard();
                DragAndDropInfo ddi = DragAndDropUtils.fromString(db.getString());
                if (ddi.getDestination() == DropDestination.SWAP_AREA) {
                    swapArea.setPiece2(ddi.getPiece());
                } else {
                    s.getStyleClass().clear();
                    s.getStyleClass().add(PieceCssStyleHelper.pieceToClassName(ddi.getPiece()));
                    s.getStyleClass().add("student-hover");
                }
                e.consume();
            });
            s.getStyleClass().add("student-hover");
        }
    }

    /**
     * Manages drop event in dining room
     */
    private void setDiningRoomDropHandlers() {
        // Single logic checks
        Supplier<Boolean> diningAcceptsEntranceStudent = () -> data.getPossibleActions().contains(ActionType.MOVE_STUDENT_TO_DININGROOM);
        Supplier<Boolean> diningAcceptsCharacterStudent = () -> data.getPlayer().getActiveCharacter() != null && data.getPlayer().getActiveCharacter().getID() == 11;
        // Full logic check
        BiConsumer<DragAndDropInfo, Runnable> check = (ddi, run) -> {
            if (ddi.getType() == DragType.PIECE) {
                if (ddi.getOrigin() == DragOrigin.ENTRANCE && diningAcceptsEntranceStudent.get())
                    run.run();
                else if (ddi.getOrigin() == DragOrigin.CHARACTER && diningAcceptsCharacterStudent.get())
                    run.run();
            }
        };

        diningRoom.setOnDragEntered(e -> {
            Dragboard db = e.getDragboard();
            DragAndDropInfo ddi = DragAndDropUtils.fromString(db.getString());
            check.accept(ddi, () -> {
                diningRoom.getStyleClass().add("dining-room-hover");
                getDiningRoomByPiece(ddi.getPiece()).getStyleClass().add("dining-room-hover");
            });
            e.consume();
        });
        diningRoom.setOnDragExited(e -> {
            Dragboard db = e.getDragboard();
            DragAndDropInfo ddi = DragAndDropUtils.fromString(db.getString());
            check.accept(ddi, () -> {
                diningRoom.getStyleClass().remove("dining-room-hover");
                getDiningRoomByPiece(ddi.getPiece()).getStyleClass().remove("dining-room-hover");
            });
            e.consume();
        });
        diningRoom.setOnDragOver(e -> {
            Dragboard db = e.getDragboard();
            DragAndDropInfo ddi = DragAndDropUtils.fromString(db.getString());
            check.accept(ddi, () -> e.acceptTransferModes(TransferMode.MOVE));
            e.consume();
        });
        diningRoom.setOnDragDropped(e -> {
            Dragboard db = e.getDragboard();
            DragAndDropInfo ddi = DragAndDropUtils.fromString(db.getString());
            ddi.setDestination(DropDestination.DINING);
            db.clear();
            db.setContent(DragAndDropUtils.toClipboardContent(ddi));
            e.consume();
        });
    }

    /**
     * Gets the pane where player can drop student in dining room
     * @param piece type student
     * @return corresponding pane
     */
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
