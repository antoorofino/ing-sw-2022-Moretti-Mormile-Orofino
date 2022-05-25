package it.polimi.ingsw.model;

import it.polimi.ingsw.util.Configurator;
import it.polimi.ingsw.util.exception.SpecificCharacterNotFoundException;
import it.polimi.ingsw.util.exception.SpecificCloudNotFoundException;
import it.polimi.ingsw.util.GameMode;
import it.polimi.ingsw.util.TowerColor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Stores information of Game model
 */
public class GameModel implements Serializable {
    private String name;
    private final PlayersHandler playerHandler;
    private final Bag studentsBag;
    private final TeachersHandler teacherHandler;
    private final ArrayList<Cloud> clouds;
    private final IslandsHandler islandHandler;
    private int coins;
    private final ArrayList<Character> characters;
    private GameMode gameMode;

    /**
     * Constructor : build game model
     */
    public GameModel(){
        this.name = "";
        this.playerHandler = new PlayersHandler();
        this.characters = new ArrayList<>();
        this.coins = 20;
        this.clouds = new ArrayList<>();
        this.islandHandler = new IslandsHandler();
        this.teacherHandler = new TeachersHandler();
        this.studentsBag = new Bag();
        this.gameMode = GameMode.NOT_CHOSEN;
    }

    /**
     * Sets up the game
     */
    public void setupGame(){
        int numPlayers = this.playerHandler.getNumPlayers();
        int numTowers, numEntranceStudents;
        this.islandHandler.setupIslands();
        if(numPlayers == 2){
            numTowers = 8;
            numEntranceStudents = 6;
        } else if(numPlayers == 3){
            numTowers = 6;
            numEntranceStudents = 9;
        } else {
            throw new IllegalStateException("Unexpected value numPlayers: " + numPlayers);
        }
        int index = 0;
        for(Player player: this.getPlayerHandler().getPlayers()){
            player.setNumOfTower(numTowers);
            player.addCards(AssistantCard.createDeck(index++));
            player.getPlayerBoard().addToEntrance(this.studentsBag.popStudents(numEntranceStudents));
        }
        for (int i = 1; i <= numPlayers; i++) {
            clouds.add(new Cloud(i));
        }
        if(gameMode == GameMode.EXPERT){
            for(Player player: this.getPlayerHandler().getPlayers()){
                player.setCoin(1);
                this.coins--;
            }
            Random random = new Random();
            List<Character> list = Configurator.getAllCharactersCards(this);
            for (int i = 0; i < 3; i++) {
                int randInt = random.nextInt(list.size());
                characters.add(list.get(randInt));
                list.remove(randInt);
            }
        }
    }

    /**
     * Gets player handler
     * @return player handler
     */
    public PlayersHandler getPlayerHandler() {
        return playerHandler;
    }

    /**
     * Gets bag
     * @return bag
     */
    public Bag getStudentsBag() {
        return studentsBag;
    }

    /**
     * Gets teacher handler
     * @return teaher handler
     */
    public TeachersHandler getTeacherHandler() {
        return teacherHandler;
    }

    /**
     * Gets list of clouds
     * @return list of clouds
     */
    public ArrayList<Cloud> getClouds() {
        return new ArrayList<>(clouds);
    }

    /**
     * Adds new cloud
     * @param newCloud cloud to add
     * @throws SpecificCloudNotFoundException
     */
    public void addCloud(Cloud newCloud)throws SpecificCloudNotFoundException {
        if(this.playerHandler.getPlayers().size() ==this.clouds.size()) throw new SpecificCloudNotFoundException("Cannot add cloud");
        this.clouds.add(newCloud);
    }

    /**
     * Gets cloud by ID
     * @param cloudID identifier of cloud
     * @return specify cloud which has the same ID
     * @throws SpecificCloudNotFoundException
     */
    public Cloud getCloudByID(int cloudID) throws SpecificCloudNotFoundException {
        for(Cloud cloud : this.clouds){
            if(cloud.getCloudID() == cloudID)
                return  cloud;
        }
        throw new SpecificCloudNotFoundException("Cannot found cloud with this id");
    }

    /**
     * Gets island handler
     * @return island handler
     */
    public IslandsHandler getIslandHandler() {
        return islandHandler;
    }

    /**
     * Checks if coins are enough
     * @return true if coins > 0, else false
     */
    public boolean coinsAreEnough(){
        return this.coins > 0;
    }

    /**
     * Remove a coin if are enough
     */
    public void getCoin(){
        //TODO controlla se va bene togliere eccezione qui
        if(coinsAreEnough())
            coins--;
    }

    /**
     * Gets all characters
     * @return list of characters
     */
    public ArrayList<Character> getCharacters() {
        return new ArrayList<>(characters);
    }

    /**
     * Adds new character
     * @param c character to add
     */
    public void addCharacter(Character c){
        this.characters.add(c);
    }

    /**
     * Gets character with specify ID
     * @param id identifier of Character
     * @return Character with specify ID
     * @throws SpecificCharacterNotFoundException
     */
    public Character getCharacterFromID(int id) throws SpecificCharacterNotFoundException {
        for(Character c : this.characters){
            if(c.getID() == id)
                return c;
        }
        throw new SpecificCharacterNotFoundException("Cannot find character with this ID");
    }

    public boolean gameAcceptPlayers(){
        return playerHandler.getNumPlayers() != 0 &&
                gameMode != GameMode.NOT_CHOSEN &&
                playerHandler.getPlayers().size() != playerHandler.getNumPlayers();
    }

    /**
     * Gets game mode
     * @return gameMode
     */
    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode mode) {
        this.gameMode = mode;
    }

    public String getGameName() {
        return name;
    }

    public void setGameName(String name) {
        this.name = name;
    }

    public void cloudsRefill() {
        for (Cloud cloud : clouds) {
            cloud.addStudents(studentsBag.popStudents(playerHandler.getNumPlayers()+1));
        }
    }
}
