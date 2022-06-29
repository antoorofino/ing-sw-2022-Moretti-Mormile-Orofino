package it.polimi.ingsw.client.gui.components;

import it.polimi.ingsw.client.GUIView;
import it.polimi.ingsw.client.gui.controllers.CharacterInfoPaneController;
import it.polimi.ingsw.client.gui.controllers.PopUpContainerController;
import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.network.messages.SetAction;
import it.polimi.ingsw.util.Action;
import it.polimi.ingsw.util.ActionType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CharactersPopUp extends ComponentGUI {
    @FXML
    private HBox cardsHBox;
    @FXML
    private Button buyCardButton;
    private final List<CharacterCardGUI> cardGUIS;
    private int selectedCard;

    public CharactersPopUp() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/fxml/charactersPopUp.fxml"));
        fxmlLoader.setController(this);
        try {
            root = fxmlLoader.load();
        } catch (IOException ignored) {
        }
        cardGUIS = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            CharacterCardGUI card = new CharacterCardGUI();
            cardGUIS.add(card);
            cardsHBox.getChildren().add(card.getRoot());
        }
    }

    public void initialize(PopUpContainerController containerController) {
        buyCardButton.setDisable(true);
        buyCardButton.setOnMouseClicked(e -> {
            GUIView.getServerHandler().send(new SetAction(
                    data.getPlayer().getNickname(),
                    new Action(ActionType.CHOOSE_CHARACTER, data.getGame().getCharacters().get(selectedCard).getID())
            ));
            containerController.popUpPane.setVisible(false);
            e.consume();
        });
    }

    public void setCards(CharacterInfoPaneController characterInfoPaneController) {
        List<Character> cards = data.getGame().getCharacters();
        buyCardButton.setDisable(true);
        if (selectedCard != -1)
            cardGUIS.get(selectedCard).deselect();
        selectedCard = -1;
        for (int i = 0; i < cardGUIS.size(); i++) {
            cardGUIS.get(i).setCharacterCard(cards.get(i), false, null, characterInfoPaneController);
            int finalI = i;
            cardGUIS.get(i).setOnClickedListener(e -> {
                if (selectedCard == -1) { // No previous card selected
                    selectedCard = finalI;
                    cardGUIS.get(finalI).select();
                    buyCardButton.setDisable(!data.getPossibleActions().contains(ActionType.CHOOSE_CHARACTER));
                } else if(selectedCard == finalI) { // Click on the same card
                    selectedCard = -1;
                    cardGUIS.get(finalI).deselect();
                    buyCardButton.setDisable(true);
                } else {
                    cardGUIS.get(selectedCard).deselect();
                    selectedCard = finalI;
                    cardGUIS.get(finalI).select();
                    buyCardButton.setDisable(!data.getPossibleActions().contains(ActionType.CHOOSE_CHARACTER));
                }
            });
        }
    }
}
