package it.polimi.ingsw.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AssistenceCardTest {
    AssistenceCard assistenceCard;
    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
        assistenceCard = null;
    }

    @Test
    public void getCardIDTest(){
        assistenceCard = new AssistenceCard(1,1,0);
        assertEquals(0,assistenceCard.getCardID());
    }
    @Test
    public void getCardValueTest(){
        assistenceCard = new AssistenceCard(1,1,1);
        assertEquals(1,assistenceCard.getCardValue());
    }

    @Test
    public void getMovementsTest(){
        assistenceCard = new AssistenceCard(1,10,1);
        assertEquals(10,assistenceCard.getMovements());
    }
}
