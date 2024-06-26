package it.polimi.ingsw.client.gui.components;

import it.polimi.ingsw.client.GUIView;
import it.polimi.ingsw.client.gui.controllers.CharacterInfoPaneController;
import it.polimi.ingsw.client.gui.utils.PieceCssStyleHelper;
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
     * Constructor: loads the fxml for the character card
     */
    public CharacterCardGUI() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/fxml/characterCard.fxml"));
        fxmlLoader.setController(this);
        try {
            root = fxmlLoader.load();
        } catch (IOException ignored) {
        }
    }

    /**
     * Initialization done by fxml loader
     */
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
                                        PieceCssStyleHelper.classNameToPiece(student.getStyleClass().get(0)))
                        ));
                        break;
                    case 12:
                        GUIView.getServerHandler().send(new SetAction(
                                data.getPlayer().getNickname(),
                                new Action(ActionType.STUDENT_FROM_DINING_TO_BAG,
                                        PieceCssStyleHelper.classNameToPiece(student.getStyleClass().get(0)))
                        ));
                        break;
                }
            });
        }
    }

    /**
     * Adds the drag feature to students piece
     * @param swapArea swapArea controller
     */
    private void setStudentsDraggable(SwapArea swapArea) {
        for (Node s : characterPiecesVBox.getChildren()) {
            s.getStyleClass().add("student-hover");
            s.setOnDragDetected(e -> {
                if (s.getStyleClass().size() > 0) { // Active drag only if there is a student
                    Dragboard db = s.startDragAndDrop(TransferMode.ANY);
                    DragAndDropInfo ddi =  new DragAndDropInfo(DragOrigin.CHARACTER,
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
                        s.getStyleClass().add(PieceCssStyleHelper.pieceToClassName(ddi.getPiece()));
                        s.getStyleClass().add("student-hover");
                }
                e.consume();
            });
        }
    }

    /**
     * Adds the drag feature to no-tile pieces
     */
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

    /**
     * Initializes the character graphic component. If the card is active then activates interaction features
     * @param students student pieces to show on the card side
     * @param characterId the id of the card
     * @param isActive true if the card is active, false otherwise
     * @param swapArea swapArea controller
     */
    private void setStudents(List<Piece> students, int characterId, boolean isActive, SwapArea swapArea) {
        // Set student pieces
        for (int i = 0; i < students.size(); i++) {
            characterPiecesVBox.getChildren().get(i).getStyleClass().add(PieceCssStyleHelper.pieceToClassName(students.get(i)));
        }
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

    /**
     * Initializes the character graphic component. If the card is active then activates interaction features
     * @param num number of no-tile pieces
     * @param isActive true if the card is active, false otherwise
     */
    private void setTiles(int num, boolean isActive) {
        // Set tile pieces
        for (int i = 0; i < num; i++) {
            characterPiecesVBox.getChildren().get(i).getStyleClass().add("no-tile");
        }
        // Add interaction features if card is active
        if (isActive)
            setTilesDraggable();
    }

    /**
     *
     * @param card
     * @param isActive true if the card is active, false otherwise
     * @param swapArea swap area controller
     * @param characterInfoPaneController info pane controller
     */
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
