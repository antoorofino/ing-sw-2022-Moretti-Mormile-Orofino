package it.polimi.ingsw.client.gui.components;

import it.polimi.ingsw.client.GUIView;
import it.polimi.ingsw.client.gui.utils.ClientData;
import it.polimi.ingsw.client.gui.utils.DelayAction;
import it.polimi.ingsw.client.gui.utils.Tmp;
import it.polimi.ingsw.client.gui.utils.dragAndDrop.*;
import it.polimi.ingsw.model.Piece;
import it.polimi.ingsw.network.messages.SetAction;
import it.polimi.ingsw.util.Action;
import it.polimi.ingsw.util.ActionType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.function.Supplier;

public class SwapArea {
    @FXML
    private Pane swapPane;
    @FXML
    private ImageView closeButton;
    @FXML
    private Pane piece1;
    @FXML
    private Pane piece2;
    @FXML
    private Pane piece1BackgroundPane;
    @FXML
    private Pane piece2BackgroundPane;
    @FXML
    private Label piece2textLabel;
    private Parent root;
    private final ClientData data = ClientData.getInstance();

    public SwapArea() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/fxml/swapArea.fxml"));
        fxmlLoader.setController(this);
        try {
            root = fxmlLoader.load();
        } catch (IOException ignored) {
        }
    }

    public Parent getRoot() {
        return root;
    }

    public void init() {
        clear();
        // Single logic checks
        Supplier<Boolean> swapAcceptsEntranceStudent = () -> piece1.getStyleClass().size() == 0;
        Supplier<Boolean> swapAcceptsDiningStudent = () -> data.getPlayer().getActiveCharacter().getID() == 10 && piece2.getStyleClass().size() == 0;
        Supplier<Boolean> swapAcceptsCharacterStudent = () -> data.getPlayer().getActiveCharacter().getID() == 7 && piece2.getStyleClass().size() == 0;
        // Set drop handlers
        swapPane.setOnDragEntered(e -> {
            Dragboard db = e.getDragboard();
            DragAndDropInfo ddi = DragAndDropUtils.fromString(db.getString());
            if (ddi.getType() == DragType.PIECE) {
                if (ddi.getOrigin() == DragOrigin.ENTRANCE && swapAcceptsEntranceStudent.get())
                    piece1BackgroundPane.getStyleClass().add("island-hover");
                else if (ddi.getOrigin() == DragOrigin.DINING && swapAcceptsDiningStudent.get())
                    piece2BackgroundPane.getStyleClass().add("island-hover");
                else if (ddi.getOrigin() == DragOrigin.CHARACTER && swapAcceptsCharacterStudent.get())
                    piece2BackgroundPane.getStyleClass().add("island-hover");
            }
            e.consume();
        });
        swapPane.setOnDragExited(e ->{
            Dragboard db = e.getDragboard();
            DragAndDropInfo ddi = DragAndDropUtils.fromString(db.getString());
            if (ddi.getType() == DragType.PIECE) {
                if (ddi.getOrigin() == DragOrigin.ENTRANCE && swapAcceptsEntranceStudent.get())
                    piece1BackgroundPane.getStyleClass().remove("island-hover");
                else if (ddi.getOrigin() == DragOrigin.DINING && swapAcceptsDiningStudent.get())
                    piece2BackgroundPane.getStyleClass().remove("island-hover");
                else if (ddi.getOrigin() == DragOrigin.CHARACTER && swapAcceptsCharacterStudent.get())
                    piece2BackgroundPane.getStyleClass().remove("island-hover");
            }
            e.consume();
        });
        swapPane.setOnDragOver(e -> {
            Dragboard db = e.getDragboard();
            DragAndDropInfo ddi = DragAndDropUtils.fromString(db.getString());
            if (ddi.getType() == DragType.PIECE) {
                if (ddi.getOrigin() == DragOrigin.ENTRANCE && swapAcceptsEntranceStudent.get())
                    e.acceptTransferModes(TransferMode.MOVE);
                else if (ddi.getOrigin() == DragOrigin.DINING && swapAcceptsDiningStudent.get())
                    e.acceptTransferModes(TransferMode.MOVE);
                else if (ddi.getOrigin() == DragOrigin.CHARACTER && swapAcceptsCharacterStudent.get())
                    e.acceptTransferModes(TransferMode.MOVE);
            }
            e.consume();
        });
        swapPane.setOnDragDropped(e -> {
            Dragboard db = e.getDragboard();
            DragAndDropInfo ddi = DragAndDropUtils.fromString(db.getString());
            if (ddi.getType() == DragType.PIECE) {
                if (ddi.getOrigin() == DragOrigin.ENTRANCE && swapAcceptsEntranceStudent.get())
                    ddi.setDestination(DropDestination.SWAP_AREA);
                else if (ddi.getOrigin() == DragOrigin.DINING && swapAcceptsDiningStudent.get())
                    ddi.setDestination(DropDestination.SWAP_AREA);
                else if (ddi.getOrigin() == DragOrigin.CHARACTER && swapAcceptsCharacterStudent.get())
                    ddi.setDestination(DropDestination.SWAP_AREA);
            }
            db.clear();
            db.setContent(DragAndDropUtils.toClipboardContent(ddi));
            e.consume();
        });
        // Set close button listener
        closeButton.setOnMouseClicked(e -> {
            switch (data.getPlayer().getActiveCharacter().getID()) {
                case 7:
                    GUIView.getServerHandler().send(new SetAction(
                            data.getPlayer().getNickname(),
                            new Action(ActionType.STUDENT_FROM_CARD_TO_ENTRANCE, -1)
                    ));
                    break;
                case 10:
                    GUIView.getServerHandler().send(new SetAction(
                            data.getPlayer().getNickname(),
                            new Action(ActionType.STUDENT_FROM_ENTRANCE_TO_DINING, -1)
                    ));
                    break;
            }
        });
    }

    private void clear() {
        piece1.getStyleClass().clear();
        piece2.getStyleClass().clear();
        switch (data.getPlayer().getActiveCharacter().getID()) {
            case 7:
                piece2textLabel.setText("Character");
                break;
            case 10:
                piece2textLabel.setText("Dining");
                break;
        }
    }

    public void setPiece1(Piece piece) {
        piece1.getStyleClass().add(Tmp.pieceToClassName(piece));
        sendSwap();
    }

    public void setPiece2(Piece piece) {
        piece2.getStyleClass().add(Tmp.pieceToClassName(piece));
        sendSwap();
    }

    private void sendSwap() {
        if (piece1.getStyleClass().size() > 0 && piece2.getStyleClass().size() > 0) {
            DelayAction.executeLater(() -> {
                switch (data.getPlayer().getActiveCharacter().getID()) {
                    case 7:
                        GUIView.getServerHandler().send(new SetAction(
                                data.getPlayer().getNickname(),
                                new Action(ActionType.STUDENT_FROM_CARD_TO_ENTRANCE,
                                        Tmp.classNameToPiece(piece2.getStyleClass().get(0)),
                                        Tmp.classNameToPiece(piece1.getStyleClass().get(0))
                                )
                        ));
                        break;
                    case 10:
                        GUIView.getServerHandler().send(new SetAction(
                                data.getPlayer().getNickname(),
                                new Action(ActionType.STUDENT_FROM_ENTRANCE_TO_DINING,
                                        Tmp.classNameToPiece(piece1.getStyleClass().get(0)),
                                        Tmp.classNameToPiece(piece2.getStyleClass().get(0))
                                )
                        ));
                        break;
                }
            });
        }
    }
}
