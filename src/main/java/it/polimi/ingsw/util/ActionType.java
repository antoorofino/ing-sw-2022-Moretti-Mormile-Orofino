package it.polimi.ingsw.util;

public enum ActionType {
    MOVE_STUDENT_TO_ISLAND(GameMode.BASIC," Muovi studente da ingresso a isola",1),
    MOVE_STUDENT_TO_DININGROOM(GameMode.BASIC," Muovi studente da ingresso a sala da pranzo",2),
    MOVE_MOTHER_NATURE(GameMode.BASIC," Muovi madre natura",3),
    CHOOSE_CLOUD(GameMode.BASIC," Scegli isola",4),
    END(GameMode.BASIC," Termina turno",5),

    CHOOSE_CHARACTER(GameMode.EXPERT," Usa carta personaggio",6),
    ACTIVATED_CHARACTER(GameMode.EXPERT," Attiva carta personaggio",7),

    STUDENT_FROM_CARD_TO_ISLAND(GameMode.EXPERT," Sposta studente da carta a isola",8),// 1 character
    CHOOSE_ISLAND(GameMode.EXPERT," Scegli isola per posizionare no entry",9), // 3,5 character
    STUDENT_FROM_CARD_TO_ENTRANCE(GameMode.EXPERT," Sposta studente da carta a ingresso",10), // 7 character
    CHOOSE_COLOR(GameMode.EXPERT," Scegli colore non valido",11), // 9,12 character
    STUDENT_FROM_ENTRANCE_TO_DINING(GameMode.EXPERT," Sposta da ingresso a sala da pranzo",12), // 10 character
    STUDENT_FROM_CARD_TO_DINING(GameMode.EXPERT," Sposta da carta a sala da pranzo",13); // 11 character

    private final GameMode mode;
    private final String description;
    private final int command;

    ActionType(GameMode mode,String description,int command) {
        this.description = description;
        this.mode = mode;
        this.command = command;
    }

    public GameMode getMode() {
        return this.mode;
    }

    public int getCommand(){ return command;}

    public String getDescription(){ return description;}
}
