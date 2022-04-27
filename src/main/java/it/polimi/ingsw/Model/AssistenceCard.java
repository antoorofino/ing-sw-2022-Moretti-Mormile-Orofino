package it.polimi.ingsw.Model;

import java.util.ArrayList;

/**
 * Stores information about an assistence card of the game
 */

public class AssistenceCard {
    private final int cardValue;
    private final int movements;
    private final int cardID;

    public static ArrayList<AssistenceCard> createDeck(int offset){
        ArrayList<AssistenceCard> deck = new ArrayList<>();
        for(int i = 1; i <= 10; i++){
            deck.add(new AssistenceCard(i, (i+1)/2,i+(offset*10)));
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

    public AssistenceCard(int cardValue, int movements, int id){
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

    public boolean isSameValue(AssistenceCard card){
        return cardValue == card.cardValue;
    }
}
