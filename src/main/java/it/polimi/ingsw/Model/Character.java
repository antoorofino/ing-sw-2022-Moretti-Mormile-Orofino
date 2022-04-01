package it.polimi.ingsw.Model;

import java.util.ArrayList;

public class Character {
    private String name;
    private String description;
    private int ID;
    //private Rules rules;
    private ArrayList<Piece> studentsOnCard;
    private int islandFlag;
    private int cost;

    public Character(String name, String description, int cost, int ID/*,Rules rules*/){
        this.name = name;
        this.description = description;
        this.cost=cost;
        this.ID=ID;
        //this.rules = rules;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getID() {
        return ID;
    }

    /*public Rules getRules() {
        return rules;
    }*/

    public ArrayList<Piece> getStudents() {
        return studentsOnCard;
    }

    public int getIslandFlag() {
        return islandFlag;
    }

    public void setIslandFlag(int num){
        this.islandFlag=num;
    }

    //TODO implement method delStudent
    public void delStudent(Piece student){}

    //TODO implement method increaseCost
    public void increaseCost(){}
}
