package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Stores information about an assistence card of the game
 */

public class AssistantCard implements Serializable {
    private final int cardValue;
    private final int movements;
    private final int cardID;

    public static ArrayList<AssistantCard> createDeck(){
        ArrayList<AssistantCard> deck = new ArrayList<>();
        for(int i = 1; i <= 10; i++){
            deck.add(new AssistantCard(i, (i+1)/2,i));
        }
        return deck;
    }
    /**
     * Constructor: build the Card
     *
     * @param cardValue Value of card
     * @param movements Value of steps that mother nature does
     * @param id        Identifier of the card
     */

    public AssistantCard(int cardValue, int movements, int id){
        this.cardID=id;
        this.cardValue=cardValue;
        this.movements=movements;
    }

    /**
     * Gets the ID of the card
     *
     * @return The ID of the card
     */
    public int getCardID(){
        return this.cardID;
    }

    /**
     * Gets the value of the card
     *
     * @return The value of the card
     */
    public int getCardValue(){
        return this.cardValue;
    }

    /**
     * Gets the value of the steps thant mother nature does whit this card
     *
     * @return The number of steps
     */

    public int getMovements(){
        return this.movements;
    }

    /**
     * Check if two assistant card has the same value
     * @param card assistant card
     * @return true if has the same value otherwise false
     */
    public boolean isSameValue(AssistantCard card){
        return cardValue == card.cardValue;
    }

    /**
     * Helper method to print assistant card
     * @return string with card value, movements, id
     */
    @Override
    public String toString() {
        return "cardValue=" + cardValue +
                ", movements=" + movements +
                ", cardID=" + cardID;
    }
}
