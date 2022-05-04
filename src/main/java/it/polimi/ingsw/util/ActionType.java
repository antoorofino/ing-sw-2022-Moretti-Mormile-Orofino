package it.polimi.ingsw.util;

public enum ActionType {
    MOVE_STUDENT_TO_ISLAND("Basic"),
    MOVE_STUDENT_TO_DININGROOM("Basic"),
    MOVE_MOTHER_NATURE("Basic"),
    CHOOSE_CLOUD("Basic"),
    END("Basic"),
    //LOSE("Basic"),
    USE_CHARACTER("Expert");

    private final String mode;

    ActionType(String mode) {
        this.mode = mode;
    }

    public String getMode() {
        return this.mode;
    }
}
