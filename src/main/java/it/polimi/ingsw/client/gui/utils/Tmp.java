package it.polimi.ingsw.client.gui.utils;

import it.polimi.ingsw.model.Piece;

public class Tmp {
    private static final String STUDENT_GREEN_BACKGROUND = "student-green-background";
    private static final String STUDENT_RED_BACKGROUND = "student-red-background";
    private static final String STUDENT_YELLOW_BACKGROUND = "student-yellow-background";
    private static final String STUDENT_PURPLE_BACKGROUND = "student-purple-background";
    private static final String STUDENT_BLUE_BACKGROUND = "student-blue-background";
    private static final String TEACHER_GREEN_BACKGROUND = "teacher-green-background";
    private static final String TEACHER_RED_BACKGROUND = "teacher-red-background";
    private static final String TEACHER_YELLOW_BACKGROUND = "teacher-yellow-background";
    private static final String TEACHER_PURPLE_BACKGROUND = "teacher-purple-background";
    private static final String TEACHER_BLUE_BACKGROUND = "teacher-blue-background";

    public static Piece classNameToPiece(String className) {
        switch (className) {
            case STUDENT_GREEN_BACKGROUND:
                return Piece.FROG;
            case STUDENT_RED_BACKGROUND:
                return Piece.DRAGON;
            case STUDENT_YELLOW_BACKGROUND:
                return Piece.GNOME;
            case STUDENT_PURPLE_BACKGROUND:
                return Piece.FAIRY;
            case STUDENT_BLUE_BACKGROUND:
                return Piece.UNICORN;
        }
        throw new RuntimeException("Invalid class name");
    }

    public static String pieceToClassName(Piece piece) {
        switch (piece) {
            case FROG:
                return STUDENT_GREEN_BACKGROUND;
            case DRAGON:
                return STUDENT_RED_BACKGROUND;
            case GNOME:
                return STUDENT_YELLOW_BACKGROUND;
            case FAIRY:
                return STUDENT_PURPLE_BACKGROUND;
            case UNICORN:
                return STUDENT_BLUE_BACKGROUND;
        }
        throw new RuntimeException("Invalid piece");
    }

    public static String teacherToClassName(Piece piece) {
        switch (piece) {
            case FROG:
                return TEACHER_GREEN_BACKGROUND;
            case DRAGON:
                return TEACHER_RED_BACKGROUND;
            case GNOME:
                return TEACHER_YELLOW_BACKGROUND;
            case FAIRY:
                return TEACHER_PURPLE_BACKGROUND;
            case UNICORN:
                return TEACHER_BLUE_BACKGROUND;
        }
        throw new RuntimeException("Invalid piece");
    }
}
