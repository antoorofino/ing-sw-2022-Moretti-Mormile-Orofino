package it.polimi.ingsw.client.gui.components;

import it.polimi.ingsw.client.gui.utils.ClientData;
import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.Player;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PlayedCardsPopUp {
    @FXML
    private HBox cardsHBox;
    Parent root;

    public PlayedCardsPopUp() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/fxml/playedCardsPopUp.fxml"));
        fxmlLoader.setController(this);
        try {
            root = fxmlLoader.load();
        } catch (IOException ignored) {
        }
    }

    public Parent getRoot() {
        return root;
    }

    public boolean setCards() {
        Map<String, AssistantCard> cards = new HashMap<>();
        ClientData data = ClientData.getInstance();
        for(Player player : data.getGame().getPlayerHandler().getPlayers()) {
            if (player.getLastCardUsed() != null)
                cards.put(player.getNickname(), player.getLastCardUsed());
        }
        if (cards.keySet().size() > 0) {
            cardsHBox.getChildren().clear();
            for (String nickname : cards.keySet()) {
                AssistantCardGUI cardGUI = new AssistantCardGUI();
                cardGUI.initialize(cards.get(nickname).getCardID());
                cardGUI.setPlayedByNickname(nickname);
                cardsHBox.getChildren().add(cardGUI.getRoot());
            }
            return false;
        } else {
            return true;
        }
    }
}
