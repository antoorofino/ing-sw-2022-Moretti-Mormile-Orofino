package it.polimi.ingsw.util;

import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TowerColorTest {
    @Test
    public void getPlayerColorByNameTest(){
        assertEquals(TowerColor.BLACK, TowerColor.getPlayerColorByName("black"));
    }

    @Test
    public void toStringTest(){
        assertEquals("Black", Objects.requireNonNull(TowerColor.getPlayerColorByName("black")).toString());
    }
}
