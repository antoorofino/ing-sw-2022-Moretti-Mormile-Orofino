package it.polimi.ingsw.model;

import java.awt.*;
import java.io.Serializable;
import java.util.Random;

/**
 * Stores information of Piece
 */
public enum Piece implements Serializable {
    UNICORN("Blue",new Color(30,200,250),"Unicorn"),
    FAIRY("Purple", new Color(255,138,228),"Fairy"),
    GNOME("Yellow",new Color(245,223,111),"Gnome"),
    DRAGON("Red",new Color(255,27,27),"Dragon"),
    FROG("Green",new Color(50,205,11),"Frog");

    private final String colorString;
    private final Color color;
    private final String name;

    /**
     * Constructor: build piece
     * @param colorString string of piece's color
     * @param color piece's color
     * @param name name of piece
     */
    Piece(String colorString, Color color, String name){
        this.name=name;
        this.color=color;
        this.colorString=colorString;
    }

    /**
     * Get piece by color
     * @param color specific color
     * @return pice that has the specific color
     */
    public static Piece getPieceByColor(String color) {
        for (Piece p : Piece.values()) {
            if (p.colorString.equalsIgnoreCase(color)) {
                 return p;
            }
        }
        return null;
    }

    /**
     * Extract a random piece
     * @return random piece
     */
    public static Piece randomPiece(){
        Random random = new Random();
        return values()[random.nextInt(values().length)];
    }

    @Override
    public String toString() {
        return colorString;
    }
}
