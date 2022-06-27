package it.polimi.ingsw.client.gui.components;

import it.polimi.ingsw.client.gui.utils.Tmp;
import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.model.Piece;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class CharacterCardGUI {
    @FXML
    private Pane characterImagePane;
    @FXML
    private VBox characterPiecesVBox;
    @FXML
    private Label costLabel;
    private Parent root;

    public CharacterCardGUI() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/fxml/characterCard.fxml"));
        fxmlLoader.setController(this);
        try {
            root = fxmlLoader.load();
        } catch (IOException ignored) {
        }
    }

    public Parent getRoot() {
        return root;
    }

    @FXML
    private void initialize() {
        clear();
    }

    private void clear() {
        characterImagePane.getStyleClass().clear();
        for (Node piece : characterPiecesVBox.getChildren())
            piece.getStyleClass().clear();
        costLabel.setText("");
    }

    private void clearEmptyPanes() {
        characterPiecesVBox.getChildren().removeIf(node -> node.getStyleClass().size() == 0);
    }

    private void setStudentsClickable() {
        for (Node student : characterPiecesVBox.getChildren()) {
            student.getStyleClass().add("student-hover");
            student.setOnMouseClicked(e -> {
                //TODO
            });
        }
    }

    private void setStudentsDraggable() {
        for (Node student : characterPiecesVBox.getChildren()) {
            student.getStyleClass().add("student-hover");
            student.setOnDragDetected(e -> {
                //TODO
            });
            student.setOnDragDone(e -> {
                //TODO
            });
        }
    }

    private void setTilesDraggable() {
        for (Node tile : characterPiecesVBox.getChildren()) {
            tile.getStyleClass().add("student-hover");
            tile.setOnDragDetected(e -> {
                //TODO
            });
            tile.setOnDragDone(e -> {
                //TODO
            });
        }
    }

    private void setStudents(List<Piece> students, int characterId, boolean isActive) {
        // Set student pieces
        for (int i = 0; i < students.size(); i++) {
            characterPiecesVBox.getChildren().get(i).getStyleClass().add(Tmp.pieceToClassName(students.get(i)));
            characterPiecesVBox.getChildren().get(i).setScaleX(1.3);
            characterPiecesVBox.getChildren().get(i).setScaleY(1.3);
        }
        clearEmptyPanes();
        // Add interaction features if card is active
        if (isActive) {
            switch (characterId) {
                case 1:
                case 7:
                case 10:
                case 11:
                    setStudentsDraggable();
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
            characterPiecesVBox.getChildren().get(i).setScaleX(1);
            characterPiecesVBox.getChildren().get(i).setScaleY(1);
        }
        clearEmptyPanes();
        // Add interaction features if card is active
        if (isActive)
            setTilesDraggable();
    }

    public void setCharacterCard(Character card, boolean isActive) {
        clear();
        // Set character image
        characterImagePane.getStyleClass().add("character-" + card.getID());
        // Set character price
        costLabel.setText(String.valueOf(card.getCost()));
        // Add pieces to card
        switch (card.getID()) {
            case 9:
            case 12:
                setStudents(Arrays.asList(Piece.FROG, Piece.DRAGON, Piece.GNOME, Piece.FAIRY, Piece.UNICORN), card.getID(), isActive);
                break;
            case 1:
            case 7:
            case 10:
            case 11:
                setStudents(card.getStudents(), card.getID(), isActive);
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
