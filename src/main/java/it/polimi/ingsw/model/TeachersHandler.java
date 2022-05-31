package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TeachersHandler implements Serializable {

    private Map<Piece,Player> teachers;

    public TeachersHandler(){
        teachers = new HashMap<>();
    }

    public void calculateTeacher(ArrayList<Player> players, boolean geq){
        int countOwner;
        int countPossibleOwner;
        Player currentOwner;
        //teachers = new HashMap<>(); // Reset teachers
        for (Piece piece : Piece.values()) { // per ogni pedina
            for (Player p : players) {
                countPossibleOwner = p.getPlayerBoard().getNumOfStudentsRoom(piece);
                if (!teacherIsAssigned(piece)){
                    if(countPossibleOwner>0)
                        teachers.put(piece, p);

                }else{
                    currentOwner = getTeacherOwner(piece); // current owner can be null
                    if (!currentOwner.equals(p)) {    // non è il proprietario attuale
                        countOwner = currentOwner.getPlayerBoard().getNumOfStudentsRoom(piece);
                        if (countPossibleOwner > countOwner || ((countOwner == countPossibleOwner) && geq))
                            teachers.put(piece, p);
                    }
                }
            }
        }
    }


    public boolean teacherIsAssigned(Piece teacher){
        if(this.teachers.get(teacher)==(null))
            return false;
        return true;
    }

    public int teachersControlled(Player player){
        return (int) teachers.values().stream().filter(p -> p.equals(player)).count();
    }

    public Player getTeacherOwner(Piece teacher){
        return this.teachers.get(teacher);
    }

    public Map<Piece, Player> getTeachers() {
        return new HashMap<>(teachers);
    }
}
