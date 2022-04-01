package it.polimi.ingsw.Model;

import java.util.ArrayList;

public class GameModel {
    private PlayersHandler playerHandler;
    private Bag studentsBag;
    private TeachersHandler teacherHandler;
    private ArrayList<Cloud> clouds;
    private IslandsHandler islandHandler;
    private int coins;
    private ArrayList<Character> characters;

    GameModel(GameMode mode){
        //TODO implement the creation and setting of classes
    }

    public PlayersHandler getPlayerHandler() {
        return playerHandler;
    }

    public Bag getStudentsBag() {
        return studentsBag;
    }

    public TeachersHandler getTeacherHandler() {
        return teacherHandler;
    }

    public ArrayList<Cloud> getClouds() {
        return clouds;
    }

    //TODO implement method addCloud
    public void addCloud(Cloud newCloud){}

    //TODO implement method getCloudByID
    public Cloud getCloudByID(int islandID){
        return null;
    }

    public IslandsHandler getIslandHandler() {
        return islandHandler;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int numCoins){this.coins=numCoins;}

    public ArrayList<Character> getCharacters() {
        return characters;
    }

    public void addCharacter(Character c){this.characters.add(c);}

    //TODO implement method getCharacterFromID
    public Character getCharacterFromID(int id){
        return null;
    }
}
