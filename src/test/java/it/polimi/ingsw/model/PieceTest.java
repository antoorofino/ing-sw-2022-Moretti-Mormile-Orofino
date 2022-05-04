package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PieceTest {
    @Test

    public void getPieceByNameTest(){
        assertEquals(Piece.UNICORN,Piece.getPieceByName("unicorn"));
    }

    @Test
    public void getColorTest(){
        assertEquals(new Color(255,27,27),Piece.DRAGON.getColor());
    }

}
