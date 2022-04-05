package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exception.CharacterException;
import it.polimi.ingsw.Exception.CloudException;
import it.polimi.ingsw.Exception.CoinException;

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
        this.playerHandler = new PlayersHandler();
        this.characters = new ArrayList<Character>();
        this.coins = 20;
        this.clouds = new ArrayList<Cloud>();
        this.islandHandler = new IslandsHandler();
        this.teacherHandler = new TeachersHandler();
        this.studentsBag = new Bag();
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
        return new ArrayList<>(clouds);
    }

    public void addCloud(Cloud newCloud)throws CloudException {
        if(this.playerHandler.getPlayers().size()==this.clouds.size()) throw new CloudException("Cannot add cloud");
        this.clouds.add(newCloud);
    }

    public Cloud getCloudByID(int islandID) throws CloudException {
        for(Cloud cloud : this.clouds){
            if(cloud.getCloudID() == islandID)
                return  cloud;
        }
        throw new CloudException("Cannot found island with this id");
    }

    public IslandsHandler getIslandHandler() {
        return islandHandler;
    }

    public void getCoin() throws CoinException {
        if(coins==0) throw new CoinException("Not coins available");
        coins--;
    }

    public ArrayList<Character> getCharacters() {
        return new ArrayList<>(characters);
    }

    public void addCharacter(Character c){this.characters.add(c);}

    public Character getCharacterFromID(int id) throws CharacterException {
        for(Character c : this.characters){
            if(c.getID() == id)
                return c;
        }
        throw new CharacterException("Cannot find character with this ID");
    }
}
