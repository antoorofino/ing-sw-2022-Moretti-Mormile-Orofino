package it.polimi.ingsw.util;

public enum ActionType {
    MOVE_STUDENT_TO_ISLAND(GameMode.BASIC," Muovi studente da ingresso a isola",1),
    MOVE_STUDENT_TO_DININGROOM(GameMode.BASIC," Muovi studente da ingresso a sala da pranzo",2),
    MOVE_MOTHER_NATURE(GameMode.BASIC," Muovi madre natura",3),
    CHOOSE_CLOUD(GameMode.BASIC," Scegli nuvola",4),
    END(GameMode.BASIC," Termina turno",-1), // no command by user

    CHOOSE_CHARACTER(GameMode.EXPERT," Usa carta personaggio",5),
    ACTIVATED_CHARACTER(GameMode.EXPERT," Attiva carta personaggio",-1), // no command by user

    STUDENT_FROM_CARD_TO_ISLAND(GameMode.EXPERT," Sposta studente da carta a isola",6),// 1 character
    DOUBLE_INFLUENCE(GameMode.EXPERT," Sposta madre natura extra", 7), // 3 character
    NO_INFLUENCE(GameMode.EXPERT," Scegli isola per posizionare no entry",8), // 5 character
    STUDENT_FROM_CARD_TO_ENTRANCE(GameMode.EXPERT," Sposta studente da carta a ingresso",9), // 7 character

    COLOR_NO_INFLUENCE(GameMode.EXPERT," Scegli colore non valido nel calcolo influenza",10), // 9 character
    STUDENT_FROM_ENTRANCE_TO_DINING(GameMode.EXPERT," Sposta da ingresso a sala da pranzo",11), // 10 character
    STUDENT_FROM_CARD_TO_DINING(GameMode.EXPERT," Sposta da carta a sala da pranzo",12), // 11 character
    STUDENT_FROM_DINING_TO_BAG(GameMode.EXPERT," Scegli colore dello studente da riposizionare nel sacchetto",13);  //12 character

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
