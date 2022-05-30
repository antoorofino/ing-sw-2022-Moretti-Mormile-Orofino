package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Stores information about island
 */
public class Island implements Serializable {
    private Map<Piece, Integer> studentsOnIsland;
    private int ID;
    private int size;
    private Player islandOwner;
    private int flagNoInfluence;

    /**
     * Constructor: build Island
     * @param islandID identifier of island
     */
    public Island(int islandID){
        this.studentsOnIsland = new HashMap<Piece, Integer>();
        for(Piece piece : Piece.values()){
            studentsOnIsland.put(piece,0);
        }
        this.ID=islandID;
        this.islandOwner = null;
        this.size = 1;
        this.flagNoInfluence = 0;
    }

    /**
     * Checks if tower is already build, so if the island has an owner
     * @return true if island has an owner
     */
    public boolean towerIsAlreadyBuild(){
        if(islandOwner == null)
            return false;
        else
            return true;
    }

    /**
     * Gets number of students on island
     * @param s type of student
     * @return number of students on island
     */
    public int getNumStudents(Piece s){
        return studentsOnIsland.get(s);
    }

    /**
     * Adds student on island
     * @param s student to add
     */
    public void addStudent(Piece s){
        studentsOnIsland.put(s,studentsOnIsland.get(s)+1);
    }

    /**
     * Gets island owner
     * @return island owner
     */
    public Player getIslandOwner(){
        return islandOwner;
    }

    /**
     * Get size of island
     * @return size of island
     */
    public int getSize(){
        return size;
    }

    /**
     * Calculate the influence on teacher
     * @param teacher to have the influence
     * @param towerCount true if towers are important to calculate influence
     * @param invalidColor color that isn't influent
     */

    public boolean calculateInfluence(TeachersHandler teacher, boolean towerCount, Piece invalidColor, Player extraPoints){
        int count;
        Player player;
        HashMap<Player, Integer> scores = new HashMap<Player, Integer>();
        if(this.flagNoInfluence>0){
            removeFlagNoInfluence();
            return false;
        }
        // punteggio extra
        if(extraPoints!=null){
            scores.put(extraPoints,2);
        }
        for (Piece piece: Piece.values()) { // assegno punteggio per pedine colori
            if(!piece.equals(invalidColor)){ // se è valido
                player = teacher.getTeacherOwner(piece);
                if(player!=null){
                    count = getCount(player,scores);
                    scores.put(player, count + getNumStudents(piece));
                }
            }
        }

        if(towerCount){
            player = getIslandOwner();
            if(player!=null) {
                count = getCount(player,scores);
                scores.put(player, count + getSize());
            }
        }

        System.out.print("Punteggi influenza: ");
        for (Player p : scores.keySet()) {
            System.out.print(p.getNickname() + ":" + scores.get(p) + "     ");
        }

        // find the new owner
        for (Player p : scores.keySet()) {
            player = getIslandOwner();
            if(player != null) {
                if (!p.equals(player) && scores.get(p) > scores.get(player)) {
                    player.addTower(getSize());
                    p.removeTower(getSize());
                    islandOwner = p;
                }
            }else{
                if(scores.get(p)!=0){
                    p.removeTower(getSize());
                    islandOwner = p;
                }
            }
        }
        return true;
    }

    public int getCount(Player player,HashMap<Player, Integer> map){
        if (!map.containsKey(player))
            map.put(player, 0);
        return map.get(player);
    }

    public int getID(){
        return this.ID;
    }

    public void addFlagNoInfluence(){
        this.flagNoInfluence++;
    }

    public void removeFlagNoInfluence(){
        this.flagNoInfluence--;
    }

    public int getFlagNoInfluence(){
        return this.flagNoInfluence;
    }

    public void increaseSize(){ this.size++;}

    public void decreaseID(){this.ID--;}

}
