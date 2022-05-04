package it.polimi.ingsw.model;

import java.util.HashMap;
import java.util.Map;

public class Island {
    private Map<Piece, Integer> studentsOnIsland;
    private int ID;
    private int size;
    private Player islandOwner;
    private int flagNoInfluence;

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

    public boolean towerIsAlreadyBuild(){
        if(islandOwner == null)
            return false;
        else
            return true;
    }

    public int getNumStudents(Piece s){
        return studentsOnIsland.get(s).intValue();
    }

    public void addStudent(Piece s){
        studentsOnIsland.put(s,studentsOnIsland.get(s)+1);
    }

    public Player getIslandOwner(){
        return islandOwner;
    }

    public int getSize(){
        return size;
    }

    public void calculateInfluence(/*PlayersHandler playersHandler,*/ TeachersHandler teacher, boolean towerCount, Piece invalidColor){
        int count;
        Player player;
        HashMap<Player, Integer> scores = new HashMap<Player, Integer>();

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

        // find the new owner
        player = getIslandOwner();
        for (Player p : scores.keySet()) {
            if((player== null) || (!p.equals(player) && scores.get(p)>scores.get(player)))
                player = p;
        }

        if(getIslandOwner()==null || !getIslandOwner().equals(player) ){
            if(getIslandOwner()!=null)
                getIslandOwner().addTower(getSize());
            player.removeTower(getSize());
            islandOwner = player;
        }
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

}
