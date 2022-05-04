package it.polimi.ingsw.model;

import it.polimi.ingsw.util.exception.SpecificCharacterNotFoundException;
import it.polimi.ingsw.util.exception.SpecificCloudNotFoundException;
import it.polimi.ingsw.util.GameMode;
import it.polimi.ingsw.util.TowerColor;

import java.util.ArrayList;
import java.util.Random;

public class GameModel {
    private final PlayersHandler playerHandler;
    private final Bag studentsBag;
    private final TeachersHandler teacherHandler;
    private final ArrayList<Cloud> clouds;
    private final IslandsHandler islandHandler;
    private int coins;
    private final ArrayList<Character> characters;

    public GameModel(){
        this.playerHandler = new PlayersHandler();
        this.characters = new ArrayList<>();
        this.coins = 20;
        this.clouds = new ArrayList<>();
        this.islandHandler = new IslandsHandler();
        this.teacherHandler = new TeachersHandler();
        this.studentsBag = new Bag();
    }

    public void setupGame(GameMode mode){
        Random rand = new Random();
        int numPlayers = this.playerHandler.getNumPlayers();
        int numTowers, numEntranceStudents;
        ArrayList<TowerColor> colors = new ArrayList<>();
        colors.add(TowerColor.BLACK);
        colors.add(TowerColor.WHITE);
        this.islandHandler.setupIslands();
        if(numPlayers == 2){
            numTowers = 8;
            numEntranceStudents = 6;
        } else if(numPlayers == 3){
            numTowers = 6;
            numEntranceStudents = 9;
            colors.add(TowerColor.GRAY);
        } else {
            throw new IllegalStateException("Unexpected value numPlayers: " + numPlayers);
        }
        int index = 0, randomIndex;
        for(Player player: this.getPlayerHandler().getPlayers()){
            player.setNumOfTower(numTowers);
            player.addCards(AssistantCard.createDeck(index++));
            randomIndex = rand.nextInt(colors.size());
            player.setPlayerColor(colors.get(randomIndex));
            colors.remove(randomIndex);
            player.getPlayerBoard().addToEntrance(this.studentsBag.popStudents(numEntranceStudents));
        }
        if(mode == GameMode.EXPERT){
            for(Player player: this.getPlayerHandler().getPlayers()){
                player.setCoin(1);
                this.coins--;
            }
            //TODO: add three characters cards
        }
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

    public void addCloud(Cloud newCloud)throws SpecificCloudNotFoundException {
        if(this.playerHandler.getPlayers().size() ==this.clouds.size()) throw new SpecificCloudNotFoundException("Cannot add cloud");
        this.clouds.add(newCloud);
    }

    public Cloud getCloudByID(int islandID) throws SpecificCloudNotFoundException {
        for(Cloud cloud : this.clouds){
            if(cloud.getCloudID() == islandID)
                return  cloud;
        }
        throw new SpecificCloudNotFoundException("Cannot found island with this id");
    }

    public IslandsHandler getIslandHandler() {
        return islandHandler;
    }

    public boolean coinsAreEnough(){
        return this.coins > 0;
    }

    public void getCoin(){
        //TODO controlla se va bene togliere eccezione qui
        if(coinsAreEnough())
            coins--;
    }

    public ArrayList<Character> getCharacters() {
        return new ArrayList<>(characters);
    }

    public void addCharacter(Character c){
        this.characters.add(c);
    }

    public Character getCharacterFromID(int id) throws SpecificCharacterNotFoundException {
        for(Character c : this.characters){
            if(c.getID() == id)
                return c;
        }
        throw new SpecificCharacterNotFoundException("Cannot find character with this ID");
    }
}
