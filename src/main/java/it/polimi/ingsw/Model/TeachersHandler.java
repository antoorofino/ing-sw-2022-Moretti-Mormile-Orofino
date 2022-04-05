package it.polimi.ingsw.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TeachersHandler {

    private Map<Piece,Player> teachers;

    public TeachersHandler(){
        teachers = new HashMap<Piece,Player>();
    }

    public void calculateTeacher(ArrayList<Player> players, boolean geq){
        int countOwner;
        int countPossibleOwner;
        Player currentOwner;
        for (Piece piece : Piece.values()) { // per ogni pedina
            currentOwner = getTeacherOwner(piece);
            for (Player p : players) {
                countPossibleOwner = p.getPlayerBoard().getNumOfStudentsRoom(piece);
                if (!teacherIsAssigned(piece))
                    if(countPossibleOwner>0)
                        teachers.put(piece, p);
                    else if (!currentOwner.equals(p)) {    // non è il proprietario attuale
                        countOwner = currentOwner.getPlayerBoard().getNumOfStudentsRoom(piece);
                        if (countPossibleOwner > countOwner || ((countOwner == countPossibleOwner) && geq))
                            teachers.put(piece, p);
                    }
            }
        }
    }

    public boolean teacherIsAssigned(Piece teacher){
        if(this.teachers.get(teacher).equals(null))
            return false;
        return true;
    }

    public Player getTeacherOwner(Piece teacher){
        return this.teachers.get(teacher);
    }

}
