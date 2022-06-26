package it.polimi.ingsw.util;

/**
 * Stores information about type of action
 */
public enum ActionType {
    MOVE_STUDENT_TO_ISLAND(GameMode.BASIC," Move student from entrance to island"),
    MOVE_STUDENT_TO_DININGROOM(GameMode.BASIC," Move student from entrance to dining room"),
    MOVE_MOTHER_NATURE(GameMode.BASIC," Move mother nature"),
    CHOOSE_CLOUD(GameMode.BASIC," Choose a cloud"),
    END(GameMode.BASIC," End turn"), // no command by user

    CHOOSE_CHARACTER(GameMode.EXPERT," Buy a character card"),
    ACTIVATED_CHARACTER(GameMode.EXPERT," Activated character card"), // no command by user
    INFO_CHARACTER(GameMode.EXPERT, " Get info about characters card"),

    STUDENT_FROM_CARD_TO_ISLAND(GameMode.EXPERT," Move student from character card to island"),// 1 character
    DOUBLE_INFLUENCE(GameMode.EXPERT," Choose an island you want to resolve like mother nature was there"), // 3 character
    NO_INFLUENCE(GameMode.EXPERT," Choose island to place no-entry flag"), // 5 character

    STUDENT_FROM_CARD_TO_ENTRANCE(GameMode.EXPERT," Reverse students between card and entrance "), // 7 character
    COLOR_NO_INFLUENCE(GameMode.EXPERT," Choose a color that won't be taken in account for the influence calculus"), // 9 character
    STUDENT_FROM_ENTRANCE_TO_DINING(GameMode.EXPERT," Reverse student between entrance and dining room"), // 10 character
    STUDENT_FROM_CARD_TO_DINING(GameMode.EXPERT,"  Reverse students between card and dining room"), // 11 character
    STUDENT_FROM_DINING_TO_BAG(GameMode.EXPERT," Choose a student to put back into the bag (x3)");  //12 character

    private final GameMode mode;
    private final String description;

    /**
     * Constructor: Build an action
     * @param mode mode of game
     * @param description action description
     */
    ActionType(GameMode mode,String description) {
        this.description = description;
        this.mode = mode;
    }

    /**
     * Get game mode
     * @return game mode
     */
    public GameMode getMode() {
        return this.mode;
    }

    /**
     * Get description of action
     * @return description
     */
    public String getDescription(){ return description;}
}
