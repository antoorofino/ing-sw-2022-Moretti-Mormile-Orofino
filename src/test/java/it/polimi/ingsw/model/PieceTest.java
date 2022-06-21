package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PieceTest {

    @Test
    public void getPieceByColorTest(){
        assertEquals(Piece.UNICORN,Piece.getPieceByColor("blue"));
        assertNull(Piece.getPieceByColor("brown"));
    }

    @Test
    public void toStringTest(){
        assertEquals("Blue",Piece.UNICORN.toString());
    }

}
