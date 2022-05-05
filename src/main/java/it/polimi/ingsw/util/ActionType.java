package it.polimi.ingsw.util;

public enum ActionType {
    MOVE_STUDENT_TO_ISLAND(GameMode.BASIC),
    MOVE_STUDENT_TO_DININGROOM(GameMode.BASIC),
    MOVE_MOTHER_NATURE(GameMode.BASIC),
    CHOOSE_CLOUD(GameMode.BASIC),
    END(GameMode.BASIC),

    CHOOSE_CHARACTER(GameMode.EXPERT),
    ACTIVATED_CHARACTER(GameMode.EXPERT),

    STUDENT_FROM_CARD_TO_ISLAND(GameMode.EXPERT),// 1 character
    CHOOSE_ISLAND(GameMode.EXPERT), // 3,5 character
    STUDENT_FROM_CARD_TO_ENTRANCE(GameMode.EXPERT), // 7 character
    CHOOSE_COLOR(GameMode.EXPERT), // 9,12 character
    STUDENT_FROM_ENTRANCE_TO_DINING(GameMode.EXPERT), // 10 character
    STUDENT_FROM_CARD_TO_DINING(GameMode.EXPERT); // 11 character

    private final GameMode mode;

    ActionType(GameMode mode) {
        this.mode = mode;
    }

    public GameMode getMode() {
        return this.mode;
    }
}
