package it.polimi.ingsw.Model;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Island {
    private Map<Piece, Integer> studentsOnIsland;
    private int ID;
    private int size;
    private Player islandOwner;
    private boolean flagNoInfluence;

    public Island(int islandID){
        this.studentsOnIsland = new HashMap<Piece, Integer>();
        this.ID=islandID;
        this.islandOwner = null;
        this.size = 1;
        this.flagNoInfluence = false;
    }

    public boolean towerIsAlreadyBuild(){
        return islandOwner.equals(null);
    }

    public int getNumStudents(Piece s){
        return studentsOnIsland.get(s);
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

    public void calculateInfluence(TeachersHandler teacher, boolean towerCount, Piece invalidColor){
        int count;
        Player player;
        HashMap<Player, Integer> scores = new HashMap<Player, Integer>();
        for (Piece piece: Piece.values()) { // assegno punteggio per pedine colori
            if(!piece.getColor().equals(invalidColor)){ // se è valido
                player = teacher.getTeacherOwner(piece);
                count = getCount(player,scores);
                scores.put(player, count + getNumStudents(piece));
            }
        }
        // count the tower
        if(towerCount){
            player = getIslandOwner();
            count = getCount(player,scores);
            scores.put(player, count + getSize());
        }

        // find the new owner
        player = getIslandOwner();
        for (Player p : scores.keySet()) {
            if(!p.equals(player) && scores.get(p)>scores.get(player))
                player = p;
        }

        if(!getIslandOwner().equals(player)){
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

    public void setFlagNoInfluence(){
        this.flagNoInfluence = true;
    }

    public void removeFlagNoInfluence(){
        this.flagNoInfluence = false;
    }

}
