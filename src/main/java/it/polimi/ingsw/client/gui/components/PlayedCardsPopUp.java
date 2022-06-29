package it.polimi.ingsw.client.gui.components;

import it.polimi.ingsw.client.gui.utils.ClientData;
import it.polimi.ingsw.model.Player;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
        ClientData data = ClientData.getInstance();
        List<Player> playersOrderedByCardNumber = data.getGame().getPlayerHandler().getPlayers().stream()
                .filter(p -> p.getLastCardUsed() != null)
                .sorted(Comparator.comparingInt(p -> p.getLastCardUsed().getCardID()))
                .collect(Collectors.toList());
        if (playersOrderedByCardNumber.size() > 0) {
            cardsHBox.getChildren().clear();
            for (Player player : playersOrderedByCardNumber) {
                AssistantCardGUI cardGUI = new AssistantCardGUI();
                cardGUI.initialize(player.getLastCardUsed().getCardID());
                cardGUI.setPlayedByNickname(player.getNickname().equals(data.getPlayer().getNickname()) ? "You" : player.getNickname());
                cardsHBox.getChildren().add(cardGUI.getRoot());
            }
            return false;
        } else {
            return true;
        }
    }
}
