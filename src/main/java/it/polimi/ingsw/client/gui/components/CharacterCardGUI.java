package it.polimi.ingsw.client.gui.components;

import it.polimi.ingsw.client.GUIView;
import it.polimi.ingsw.client.gui.controllers.CharacterInfoPaneController;
import it.polimi.ingsw.client.gui.utils.Tmp;
import it.polimi.ingsw.client.gui.utils.dragAndDrop.*;
import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.model.Piece;
import it.polimi.ingsw.network.messages.SetAction;
import it.polimi.ingsw.util.Action;
import it.polimi.ingsw.util.ActionType;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

//todo: complete javadoc
/**
 * Manages graphical assets for Character card
 */
public class CharacterCardGUI extends ComponentGUI {
    @FXML
    private Pane characterImagePane;
    @FXML
    private VBox characterPiecesVBox;
    @FXML
    private Label costLabel;
    @FXML
    private ImageView infoButton;

    /**
     * Constructor: load the fxml for the character card
     */
    public CharacterCardGUI() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/fxml/characterCard.fxml"));
        fxmlLoader.setController(this);
        try {
            root = fxmlLoader.load();
        } catch (IOException ignored) {
        }
    }

    //TODO: check if useless
    @FXML
    private void initialize() {
        clear();
    }

    /**
     * Removes css style from all the characters
     */
    private void clear() {
        characterImagePane.getStyleClass().clear();
        for (Node piece : characterPiecesVBox.getChildren())
            piece.getStyleClass().clear();
        costLabel.setText("");
    }

    private void clearEmptyPanes() {
        characterPiecesVBox.getChildren().removeIf(node -> node.getStyleClass().size() == 0);
    }

    /**
     * Adds event on click of student if character's ability required it
     */
    private void setStudentsClickable() {
        for (Node student : characterPiecesVBox.getChildren()) {
            student.getStyleClass().add("student-hover");
            student.setOnMouseClicked(e -> {
                switch (data.getPlayer().getActiveCharacter().getID()) {
                    case 9:
                        GUIView.getServerHandler().send(new SetAction(
                                data.getPlayer().getNickname(),
                                new Action(ActionType.COLOR_NO_INFLUENCE,
                                        Tmp.classNameToPiece(student.getStyleClass().get(0)))
                        ));
                        break;
                    case 12:
                        GUIView.getServerHandler().send(new SetAction(
                                data.getPlayer().getNickname(),
                                new Action(ActionType.STUDENT_FROM_DINING_TO_BAG,
                                        Tmp.classNameToPiece(student.getStyleClass().get(0)))
                        ));
                        break;
                }
            });
        }
    }

    //TODO: complete java doc
    /**
     *
     * @param swapArea
     */
    private void setStudentsDraggable(SwapArea swapArea) {
        for (Node s : characterPiecesVBox.getChildren()) {
            s.getStyleClass().add("student-hover");
            s.setOnDragDetected(e -> {
                if (s.getStyleClass().size() > 0) { // Active drag only if there is a student
                    Dragboard db = s.startDragAndDrop(TransferMode.ANY);
                    DragAndDropInfo ddi =  new DragAndDropInfo(DragOrigin.CHARACTER,
                            DragType.PIECE,
                            Tmp.classNameToPiece(s.getStyleClass().get(0))
                    );
                    db.setContent(DragAndDropUtils.toClipboardContent(ddi));
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
                        GUIView.getServerHandler().send(new SetAction(data.getPlayer().getNickname(),
                                new Action(ActionType.STUDENT_FROM_CARD_TO_DINING, ddi.getPiece())
                        ));
                        break;
                    case ISLAND:
                        GUIView.getServerHandler().send(new SetAction(data.getPlayer().getNickname(),
                                new Action(ActionType.STUDENT_FROM_CARD_TO_ISLAND, ddi.getPiece(), ddi.getIslandId())
                        ));
                        break;
                    case SWAP_AREA:
                        swapArea.setPiece2(ddi.getPiece());
                        break;
                    case NONE:
                        s.getStyleClass().clear();
                        s.getStyleClass().add(Tmp.pieceToClassName(ddi.getPiece()));
                        s.getStyleClass().add("student-hover");
                }
                e.consume();
            });
        }
    }

    private void setTilesDraggable() {
        for (Node tile : characterPiecesVBox.getChildren()) {
            tile.getStyleClass().add("student-hover");
            tile.setOnDragDetected(e -> {
                if (tile.getStyleClass().size() > 0) { // Active drag only if there is a tile
                    Dragboard db = tile.startDragAndDrop(TransferMode.ANY);
                    DragAndDropInfo ddi =  new DragAndDropInfo(DragOrigin.CHARACTER,
                            DragType.NO_TILE
                    );
                    db.setContent(DragAndDropUtils.toClipboardContent(ddi));
                    db.setDragView(new Image(String.valueOf(getClass().getResource("/gui/images/no_tile.png")), 20.0, 20.0, true, true));
                    tile.getStyleClass().clear();
                }
                e.consume();
            });
            tile.setOnDragDone(e -> {
                Dragboard db = e.getDragboard();
                DragAndDropInfo ddi = DragAndDropUtils.fromString(db.getString());
                if (ddi.getDestination() == DropDestination.ISLAND) {
                    GUIView.getServerHandler().send(new SetAction(
                            data.getPlayer().getNickname(),
                            new Action(ActionType.NO_INFLUENCE, ddi.getIslandId())
                    ));
                } else {
                    tile.getStyleClass().clear();
                    tile.getStyleClass().add("no-tile");
                    tile.getStyleClass().add("student-hover");
                }
                e.consume();
            });
        }
    }

    private void setStudents(List<Piece> students, int characterId, boolean isActive, SwapArea swapArea) {
        // Set student pieces
        for (int i = 0; i < students.size(); i++) {
            characterPiecesVBox.getChildren().get(i).getStyleClass().add(Tmp.pieceToClassName(students.get(i)));
        }
        clearEmptyPanes();
        // Add interaction features if card is active
        if (isActive) {
            switch (characterId) {
                case 1:
                case 7:
                case 10:
                case 11:
                    setStudentsDraggable(swapArea);
                    break;
                case 9:
                case 12:
                    setStudentsClickable();
                    break;
            }
        }
    }

    private void setTiles(int num, boolean isActive) {
        // Set tile pieces
        for (int i = 0; i < num; i++) {
            characterPiecesVBox.getChildren().get(i).getStyleClass().add("no-tile");
        }
        clearEmptyPanes();
        // Add interaction features if card is active
        if (isActive)
            setTilesDraggable();
    }

    public void setCharacterCard(Character card, boolean isActive, SwapArea swapArea, CharacterInfoPaneController characterInfoPaneController) {
        clear();
        // Set character image
        characterImagePane.getStyleClass().add("character-" + card.getID());
        // Set character price
        costLabel.setText(String.valueOf(card.getCost()));
        // Set info pane pane if provided
        if (characterInfoPaneController != null)
            infoButton.setOnMouseClicked(e-> characterInfoPaneController.setCharacterInfo(card));
        else
            infoButton.setVisible(false);
        // Add pieces to card
        switch (card.getID()) {
            case 9:
            case 12:
                setStudents(Arrays.asList(Piece.FROG, Piece.DRAGON, Piece.GNOME, Piece.FAIRY, Piece.UNICORN), card.getID(), isActive, swapArea);
                break;
            case 1:
            case 7:
            case 10:
            case 11:
                setStudents(card.getStudents(), card.getID(), isActive, swapArea);
                break;
            case 5:
                setTiles(card.getIslandFlag(), isActive);
                break;
        }
    }

    public void select() {
        characterImagePane.getStyleClass().add("assistant-selected");
    }

    public void deselect() {
        characterImagePane.getStyleClass().remove("assistant-selected");
    }

    public void setOnClickedListener(EventHandler<? super MouseEvent> listener){
        root.getStyleClass().add("character-card-hover");
        root.setOnMouseClicked(listener);
    }

}
