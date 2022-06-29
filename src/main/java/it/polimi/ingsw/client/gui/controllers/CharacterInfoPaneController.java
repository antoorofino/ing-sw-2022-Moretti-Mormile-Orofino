package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.components.CharacterCardGUI;
import it.polimi.ingsw.model.Character;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class CharacterInfoPaneController {
    @FXML
    public AnchorPane rootPane;
    @FXML
    public Text nameText;
    @FXML
    public Pane characterPane;
    @FXML
    public Text descriptionText;

    @FXML
    public void initialize() {
        rootPane.setVisible(false);
    }

    public void setCharacterInfo(Character card) {
        nameText.setText(card.getName());
        characterPane.getChildren().clear();
        CharacterCardGUI cardGUI = new CharacterCardGUI();
        cardGUI.setCharacterCard(card, false, null, null);
        characterPane.getChildren().add(cardGUI.getRoot());
        descriptionText.setText(card.getDescription());
        rootPane.setVisible(true);
    }

    @FXML
    public void onCloseInfoClicked() {
        rootPane.setVisible(false);
    }
}
