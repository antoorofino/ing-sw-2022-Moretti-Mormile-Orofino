package it.polimi.ingsw.Model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerColorTest {
    @Test
    public void getPlayerColorByNameTest(){
        assertEquals(PlayerColor.BLACK,PlayerColor.getPlayerColorByName("black"));
    }

    @Test
    public void toStringTest(){
        assertEquals("Black",PlayerColor.getPlayerColorByName("black").toString());
    }
}
