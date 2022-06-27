package it.polimi.ingsw.client.gui.components;

import it.polimi.ingsw.client.gui.utils.Tmp;
import it.polimi.ingsw.model.Piece;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.List;

public class CloudGUI {
    @FXML
    private GridPane cloudGrid;
    private Parent root;

    public CloudGUI() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/fxml/cloud.fxml"));
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
        for (Node node : cloudGrid.getChildren())
            node.getStyleClass().clear();
    }

    public void setStudents(List<Piece> students) {
        clear();
        for (int i = 0; i < students.size(); i++) {
            cloudGrid.getChildren().get(i).getStyleClass().add(Tmp.pieceToClassName(students.get(i)));
        }
    }
}
