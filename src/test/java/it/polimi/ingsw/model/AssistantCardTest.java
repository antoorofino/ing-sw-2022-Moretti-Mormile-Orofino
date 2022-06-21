package it.polimi.ingsw.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AssistantCardTest {
    AssistantCard assistantCard;

    @AfterEach
    public void tearDown() {
        assistantCard = null;
    }

    @Test
    public void getCardIDTest(){
        assistantCard = new AssistantCard(1,1,0);
        assertEquals(0, assistantCard.getCardID());
    }
    @Test
    public void getCardValueTest(){
        assistantCard = new AssistantCard(1,1,1);
        assertEquals(1, assistantCard.getCardValue());
    }

    @Test
    public void getMovementsTest(){
        assistantCard = new AssistantCard(1,10,1);
        assertEquals(10, assistantCard.getMovements());
    }

    @Test
    public void isSameValueTest(){
        assistantCard = new AssistantCard(1,10,1);
        AssistantCard card2 = new AssistantCard(1,10,2);
        assertTrue(assistantCard.isSameValue(card2));
    }

    @Test
    public void toStringtest(){
        assistantCard = new AssistantCard(1,10,1);
        String string = new String("cardValue=" + 1 +
                ", movements=" + 10 +
                ", cardID=" + 1);
        assertEquals(string,assistantCard.toString());
    }
}
