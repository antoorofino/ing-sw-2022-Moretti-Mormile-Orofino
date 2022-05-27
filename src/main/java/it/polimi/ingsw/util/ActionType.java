package it.polimi.ingsw.util;

public enum ActionType {
    MOVE_STUDENT_TO_ISLAND(GameMode.BASIC," Move student from entrance to island"),
    MOVE_STUDENT_TO_DININGROOM(GameMode.BASIC," Move student from entrance to dining room"),
    MOVE_MOTHER_NATURE(GameMode.BASIC," Move mother nature"),
    CHOOSE_CLOUD(GameMode.BASIC," Choose a cloud"),
    END(GameMode.BASIC," Termina turno"), // no command by user

    CHOOSE_CHARACTER(GameMode.EXPERT," Usa carta personaggio"),
    ACTIVATED_CHARACTER(GameMode.EXPERT," Buy a character card"), // no command by user

    STUDENT_FROM_CARD_TO_ISLAND(GameMode.EXPERT," Move student from character card to island"),// 1 character
    DOUBLE_INFLUENCE(GameMode.EXPERT," Move mother nature of extra steps"), // 3 character
    NO_INFLUENCE(GameMode.EXPERT," Choose island to place no-entry flag"), // 5 character
    STUDENT_FROM_CARD_TO_ENTRANCE(GameMode.EXPERT," Move student from character to "), // 7 character

    COLOR_NO_INFLUENCE(GameMode.EXPERT," Choose a color that won't be taken in account for the influence calculus"), // 9 character
    STUDENT_FROM_ENTRANCE_TO_DINING(GameMode.EXPERT," Move student from entrance to dining room"), // 10 character
    STUDENT_FROM_CARD_TO_DINING(GameMode.EXPERT," Move student from character to dining room"), // 11 character
    STUDENT_FROM_DINING_TO_BAG(GameMode.EXPERT," Choose a student to put back into the bag");  //12 character

    private final GameMode mode;
    private final String description;

    ActionType(GameMode mode,String description) {
        this.description = description;
        this.mode = mode;
    }

    public GameMode getMode() {
        return this.mode;
    }

    public String getDescription(){ return description;}
}
