package it.polimi.ingsw.client.gui.utils;

import it.polimi.ingsw.model.Piece;

public class Tmp {
    private static final String greenClassName= "student-green-background";
    private static final String redClassName = "student-red-background";
    private static final String yellowClassName = "student-yellow-background";
    private static final String purpleClassName = "student-purple-background";
    private static final String blueClassName = "student-blue-background";

    public static Piece classNameToPiece(String className) {
        switch (className) {
            case greenClassName:
                return Piece.FROG;
            case redClassName:
                return Piece.DRAGON;
            case yellowClassName:
                return Piece.GNOME;
            case purpleClassName:
                return Piece.FAIRY;
            case blueClassName:
                return Piece.UNICORN;
        }
        throw new RuntimeException("Invalid class name");
    }

    public static String pieceToClassName(Piece piece) {
        switch (piece) {
            case FROG:
                return greenClassName;
            case DRAGON:
                return redClassName;
            case GNOME:
                return yellowClassName;
            case FAIRY:
                return purpleClassName;
            case UNICORN:
                return blueClassName;
        }
        throw new RuntimeException("Invalid piece");
    }
}
