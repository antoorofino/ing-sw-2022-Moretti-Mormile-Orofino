package it.polimi.ingsw.Util;

import it.polimi.ingsw.util.TowerColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TowerColorTest {
    @Test
    public void getPlayerColorByNameTest(){
        assertEquals(TowerColor.BLACK, TowerColor.getPlayerColorByName("black"));
    }

    @Test
    public void toStringTest(){
        assertEquals("Black", TowerColor.getPlayerColorByName("black").toString());
    }
}
