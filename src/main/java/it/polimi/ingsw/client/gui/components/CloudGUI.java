package it.polimi.ingsw.client.gui.components;

import it.polimi.ingsw.client.gui.utils.Tmp;
import it.polimi.ingsw.model.Piece;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.List;

/**
 * Manages graphical assets for clouds
 */
public class CloudGUI extends ComponentGUI {
    @FXML
    private GridPane cloudGrid;

    /**
     * Constructor: load the fxml for cloud
     */
    public CloudGUI() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/fxml/cloud.fxml"));
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
     * Removes all the css class
     */
    private void clear() {
        for (Node node : cloudGrid.getChildren())
            node.getStyleClass().clear();
    }

    /**
     * Adds students to the cloud
     * @param students students
     */
    public void setStudents(List<Piece> students) {
        clear();
        for (int i = 0; i < students.size(); i++) {
            cloudGrid.getChildren().get(i).getStyleClass().add(Tmp.pieceToClassName(students.get(i)));
        }
    }
}
