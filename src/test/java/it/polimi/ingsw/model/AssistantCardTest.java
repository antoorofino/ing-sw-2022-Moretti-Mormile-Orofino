package it.polimi.ingsw.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AssistantCardTest {
    AssistantCard assistantCard;
    @BeforeEach
    public void setUp() {
    }

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
}
