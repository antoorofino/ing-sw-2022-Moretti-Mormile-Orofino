package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages the teachers
 */
public class TeachersHandler implements Serializable {
    private Map<Piece,Player> teachers;

    /**
     * Constructor: builds teacher handler
     */
    public TeachersHandler(){
        teachers = new HashMap<>();
    }

    /**
     * Calculates teacher's owner
     * @param players list of players
     * @param geq specify if calculate the owner with > if geq is false or >= if geq is true
     */
    public void calculateTeacher(ArrayList<Player> players, boolean geq){
        int countOwner;
        int countPossibleOwner;
        Player currentOwner;
        for (Piece piece : Piece.values()) { // foreach piece
            for (Player p : players) {
                countPossibleOwner = p.getPlayerBoard().getNumOfStudentsRoom(piece);
                if (!teacherIsAssigned(piece)){
                    if(countPossibleOwner>0)
                        teachers.put(piece, p);

                }else{
                    currentOwner = getTeacherOwner(piece); // current owner can be null
                    if (!currentOwner.equals(p)) {
                        countOwner = currentOwner.getPlayerBoard().getNumOfStudentsRoom(piece);
                        if (countPossibleOwner > countOwner || ((countOwner == countPossibleOwner) && geq))
                            teachers.put(piece, p);
                    }
                }
            }
        }
    }

    /**
     * Checks if teacher has an owner
     * @param teacher teacher
     * @return true if teacher has owner, otherwise false
     */
    public boolean teacherIsAssigned(Piece teacher){
        if(this.teachers.get(teacher)==(null))
            return false;
        return true;
    }

    /**
     * Counts how many teacher player control
     * @param player player
     * @return number of teacher that player control
     */
    public int teachersControlled(Player player){
        return (int) teachers.values().stream().filter(p -> p.equals(player)).count();
    }

    /**
     * Gets teacher's owner
     * @param teacher teacher
     * @return teacher's owner
     */
    public Player getTeacherOwner(Piece teacher){
        return this.teachers.get(teacher);
    }

    /**
     * Gets teachers list of the player by player id
     * @param playerId id of the player
     * @return list of teachers owned
     */
    public List<Piece> getTeachersByPlayerId(String playerId) {
        List<Piece> teachersArray = new ArrayList<>();
        for (Piece piece : teachers.keySet()) {
            if (teachers.get(piece).getId().equals(playerId))
                teachersArray.add(piece);
        }
        return teachersArray;
    }
}
