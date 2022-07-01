package it.polimi.ingsw.client.gui.components;

import it.polimi.ingsw.client.GUIView;
import it.polimi.ingsw.client.gui.controllers.PopUpContainerController;
import it.polimi.ingsw.network.messages.SetAssistantCard;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages graphical assets and event in the pop up that shows player's card
 */
public class YourCardsPopUp extends ComponentGUI {
    @FXML
    private GridPane cardsGrid;
    @FXML
    private Button playCardButton;
    private final List<AssistantCardGUI> assistantCardGUIS =  new ArrayList<>();
    private int selectedCard;

    /**
     * Constructor: load the fxml for pop up that shows player's card
     */
    public YourCardsPopUp() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/fxml/yourCardsPopUp.fxml"));
        fxmlLoader.setController(this);
        try {
            root = fxmlLoader.load();
        } catch (IOException ignored) {
        }
        this.selectedCard = 0;
    }

    /**
     * Initializes the pop up setting graphical assets and declaring event management
     * @param controller
     */
    public void initialize(PopUpContainerController controller) {
        playCardButton.setOnMouseClicked(e -> {
            GUIView.getServerHandler().send(new SetAssistantCard(
                    data.getPlayer().getNickname(),
                    data.getPossibleCards().stream().filter(c -> c.getCardID() == selectedCard).findFirst().get()
            ));
            controller.popUpPane.setVisible(false);
            data.setPossibleCards(null);
        });

        for (int i = 1; i <= 10; i++) {
            AssistantCardGUI cardGUI = new AssistantCardGUI();
            assistantCardGUIS.add(cardGUI);
            GridPane.setConstraints(cardGUI.getRoot(), (i - 1) % 5, i <= 5 ? 0 : 1);
            cardsGrid.getChildren().add(cardGUI.getRoot());
        }
    }

    /**
     * Manages graphical assets for each player's card
     */
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
                cardGUI.setNotAvailable();
            } else if (data.getPossibleCards() != null && data.getPossibleCards().stream().filter(c -> c.getCardID() == finalI).findFirst().orElse(null) == null) {
                cardGUI.setAlreadyPlayed();
            } else {
                cardGUI.setOnClickedListener(id -> {
                    if (selectedCard == 0) { // No previous card selected
                        selectedCard = id;
                        assistantCardGUIS.get(id - 1).select();
                        playCardButton.setDisable(data.getPossibleCards() == null);
                    } else if(selectedCard == id) { // Click on the same card
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
}
