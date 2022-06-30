package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.components.CharacterCardGUI;
import it.polimi.ingsw.model.Character;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

/**
 * Controller of Character info pane
 */
public class CharacterInfoPaneController {
    @FXML
    public AnchorPane rootPane;
    @FXML
    public Text nameText;
    @FXML
    public Pane characterPane;
    @FXML
    public Text descriptionText;

    /**
     * Method used by the fxml loader to initialise the pane where player can see info about a specific character
     */
    @FXML
    public void initialize() {
        rootPane.setVisible(false);
    }

    /**
     * Sets the info about a specific character
     * @param card specific character card
     */
    public void setCharacterInfo(Character card) {
        nameText.setText(card.getName());
        characterPane.getChildren().clear();
        CharacterCardGUI cardGUI = new CharacterCardGUI();
        cardGUI.setCharacterCard(card, false, null, null);
        characterPane.getChildren().add(cardGUI.getRoot());
        descriptionText.setText(card.getDescription());
        rootPane.setVisible(true);
    }

    /**
     * Action activated when players click on icon close
     * Closed the pane where are shown the info of the character card
     */
    @FXML
    public void onCloseInfoClicked() {
        rootPane.setVisible(false);
    }
}
