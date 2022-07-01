package it.polimi.ingsw.client.gui.components;

import it.polimi.ingsw.client.gui.utils.PieceCssStyleHelper;
import it.polimi.ingsw.model.Piece;
import it.polimi.ingsw.util.ActionType;
import it.polimi.ingsw.util.TowerColor;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.Map;

/**
 * Manages graphical assets for island
 */
public class IslandGUI extends ComponentGUI {
    @FXML
    private GridPane islandBackgroundPane;
    @FXML
    private Pane greenImage;
    @FXML
    private Text greenText;
    @FXML
    private Pane redImage;
    @FXML
    private Text redText;
    @FXML
    private Pane yellowImage;
    @FXML
    private Text yellowText;
    @FXML
    private Pane purpleImage;
    @FXML
    private Text purpleText;
    @FXML
    private Pane blueImage;
    @FXML
    private Text blueText;
    @FXML
    private Pane motherNatureImage;
    @FXML
    private Pane towerImage;
    @FXML
    private Pane noTileImage;
    @FXML
    private Text noTileText;

    /**
     * Constructor: loads the fxml for the island
     */
    public IslandGUI() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/fxml/island.fxml"));
        fxmlLoader.setController(this);
        try {
            root = fxmlLoader.load();
        } catch (IOException ignored) {
        }
    }

    @FXML
    private void initialize() {
        clear();
    }

    /**
     * Resets all graphical assets for the island
     */
    private void clear() {
        islandBackgroundPane.getStyleClass().clear();
        greenImage.getStyleClass().clear();
        greenText.setText("");
        redImage.getStyleClass().clear();
        redText.setText("");
        yellowImage.getStyleClass().clear();
        yellowText.setText("");
        purpleImage.getStyleClass().clear();
        purpleText.setText("");
        blueImage.getStyleClass().clear();
        blueText.setText("");
        motherNatureImage.getStyleClass().clear();
        towerImage.getStyleClass().clear();
        noTileImage.getStyleClass().clear();
        noTileText.setText("");
    }

    /**
     * Sets image for island
     * @param bg string that identify the css class to add image
     */
    public void setIslandImage(String bg) {
        islandBackgroundPane.getStyleClass().add(bg);
    }

    /**
     * Puts students on island modifying class style in css
     * @param map map that contains how many students for each type
     */
    public void setStudents(Map<Piece, Integer> map) {
        for(Piece piece : map.keySet()) {
            switch (piece) {
                case FROG:
                    if(map.get(piece) > 0)
                        greenImage.getStyleClass().add(PieceCssStyleHelper.pieceToClassName(piece));
                    greenText.setText("x" + map.get(piece));
                    greenText.setVisible(map.get(piece) > 1);
                    break;
                case DRAGON:
                    if(map.get(piece) > 0)
                        redImage.getStyleClass().add(PieceCssStyleHelper.pieceToClassName(piece));
                    redText.setText("x" + map.get(piece));
                    redText.setVisible(map.get(piece) > 1);
                    break;
                case GNOME:
                    if(map.get(piece) > 0)
                        yellowImage.getStyleClass().add(PieceCssStyleHelper.pieceToClassName(piece));
                    yellowText.setText("x" + map.get(piece));
                    yellowText.setVisible(map.get(piece) > 1);
                    break;
                case FAIRY:
                    if(map.get(piece) > 0)
                        purpleImage.getStyleClass().add(PieceCssStyleHelper.pieceToClassName(piece));
                    purpleText.setText("x" + map.get(piece));
                    purpleText.setVisible(map.get(piece) > 1);
                    break;
                case UNICORN:
                    if(map.get(piece) > 0)
                        blueImage.getStyleClass().add(PieceCssStyleHelper.pieceToClassName(piece));
                    blueText.setText("x" + map.get(piece));
                    blueText.setVisible(map.get(piece) > 1);
                    break;
            }
        }
    }

    /**
     * Puts towers on island
     * @param color color of tower
     */
    public void setTower(TowerColor color) {
        switch (color) {
            case BLACK:
                towerImage.getStyleClass().add("tower-black-background");
                break;
            case WHITE:
                towerImage.getStyleClass().add("tower-white-background");
                break;
            case GRAY:
                towerImage.getStyleClass().add("tower-grey-background");
                break;
        }
    }
    //TODO: complete
    public void setMotherNature() {
        motherNatureImage.getStyleClass().add("mother-nature-background");
        if (data.getPossibleActions().contains(ActionType.MOVE_MOTHER_NATURE))
            motherNatureImage.getStyleClass().add("student-hover");
    }

    public Pane getMotherNatureImage() {
        return motherNatureImage;
    }

    public void setNoTile(int n) {
        if(n > 0)
            noTileImage.getStyleClass().add("no-tile");
        noTileText.setText("x" + n);
        noTileText.setVisible(n > 1);
    }
}
