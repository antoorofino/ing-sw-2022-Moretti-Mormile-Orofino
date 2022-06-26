package it.polimi.ingsw.client.gui.components;

import it.polimi.ingsw.client.GUIView;
import it.polimi.ingsw.client.gui.utils.ClientData;
import it.polimi.ingsw.network.messages.SetAssistantCard;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class YourCardsPopUp {
    @FXML
    private GridPane cardsGrid;
    @FXML
    private Button playCardButton;
    private Parent root;
    private final List<AssistantCardGUI> assistantCardGUIS =  new ArrayList<>();
    private int selectedCard;
    private final ClientData data = ClientData.getInstance();

    public YourCardsPopUp() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/fxml/yourCardsPopUp.fxml"));
        fxmlLoader.setController(this);
        try {
            root = fxmlLoader.load();
        } catch (IOException ignored) {
        }
        this.selectedCard = 0;
    }

    @FXML
    private void initialize() {
        playCardButton.setOnMouseClicked(e -> {
            if(selectedCard != 0) {
                GUIView.getServerHandler().send(new SetAssistantCard(
                        data.getPlayer().getNickname(),
                        data.getPossibleCards().stream().filter(c -> c.getCardID() == selectedCard).findFirst().get()
                ));
                data.setPossibleCards(null);
            }
        });

        for (int i = 1; i <= 10; i++) {
            AssistantCardGUI cardGUI = new AssistantCardGUI();
            assistantCardGUIS.add(cardGUI);
            GridPane.setConstraints(cardGUI.getRoot(), (i - 1) % 5, i <= 5 ? 0 : 1);
            cardsGrid.getChildren().add(cardGUI.getRoot());
        }
    }

    public void setCards() {
        playCardButton.setDisable(true);
        if (selectedCard != 0)
            assistantCardGUIS.get(selectedCard - 1).deselect();
        AssistantCardGUI cardGUI;
        for (int i = 1; i <= 10; i++) {
            cardGUI = assistantCardGUIS.get(i-1);
            cardGUI.initialize(i);
            int finalI = i;
            if(data.getPlayer().getDeck().stream().filter(c -> c.getCardID() == finalI).findFirst().orElse(null) == null) {
                cardGUI.setAlreadyPlayed();
            } else if (data.getPossibleCards() != null && data.getPossibleCards().stream().filter(c -> c.getCardID() == finalI).findFirst().orElse(null) == null) {
                cardGUI.setNotAvailable();
            } else {
                cardGUI.setOnClickedListener(id -> {
                    if (selectedCard == 0) { // No previous card selected
                        selectedCard = id;
                        assistantCardGUIS.get(id - 1).select();
                        playCardButton.setDisable(data.getPossibleCards() == null);
                    } else if(selectedCard == id) { //Click on the same card
                        selectedCard = 0;
                        assistantCardGUIS.get(id - 1).deselect();
                        playCardButton.setDisable(true);
                    } else {
                        assistantCardGUIS.get(selectedCard - 1).deselect();
                        selectedCard = id;
                        assistantCardGUIS.get(id - 1).select();
                        playCardButton.setDisable(data.getPossibleCards() == null);
                    }
                });
            }
        }
    }

    public Parent getRoot() {
        return root;
    }
}
