package it.polimi.ingsw.util;

public enum ActionType {
    MOVE_STUDENT_TO_ISLAND(GameMode.BASIC," Muovi studente da ingresso a isola"),
    MOVE_STUDENT_TO_DININGROOM(GameMode.BASIC," Muovi studente da ingresso a sala da pranzo"),
    MOVE_MOTHER_NATURE(GameMode.BASIC," Muovi madre natura"),
    CHOOSE_CLOUD(GameMode.BASIC," Scegli nuvola"),
    END(GameMode.BASIC," Termina turno"), // no command by user

    CHOOSE_CHARACTER(GameMode.EXPERT," Usa carta personaggio"),
    ACTIVATED_CHARACTER(GameMode.EXPERT," Attiva carta personaggio"), // no command by user

    STUDENT_FROM_CARD_TO_ISLAND(GameMode.EXPERT," Sposta studente da carta a isola"),// 1 character
    DOUBLE_INFLUENCE(GameMode.EXPERT," Sposta madre natura extra"), // 3 character
    NO_INFLUENCE(GameMode.EXPERT," Scegli isola per posizionare no entry"), // 5 character
    STUDENT_FROM_CARD_TO_ENTRANCE(GameMode.EXPERT," Sposta studente da carta a ingresso"), // 7 character

    COLOR_NO_INFLUENCE(GameMode.EXPERT," Scegli colore non valido nel calcolo influenza"), // 9 character
    STUDENT_FROM_ENTRANCE_TO_DINING(GameMode.EXPERT," Sposta da ingresso a sala da pranzo"), // 10 character
    STUDENT_FROM_CARD_TO_DINING(GameMode.EXPERT," Sposta da carta a sala da pranzo"), // 11 character
    STUDENT_FROM_DINING_TO_BAG(GameMode.EXPERT," Scegli colore dello studente da riposizionare nel sacchetto");  //12 character

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
