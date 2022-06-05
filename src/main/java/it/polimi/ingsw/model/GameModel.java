package it.polimi.ingsw.model;

import it.polimi.ingsw.util.Configurator;
import it.polimi.ingsw.util.GameListInfo;
import it.polimi.ingsw.util.exception.SpecificCharacterNotFoundException;
import it.polimi.ingsw.util.exception.SpecificCloudNotFoundException;
import it.polimi.ingsw.util.GameMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Stores information of Game model
 */
public class GameModel implements Serializable {
    private final String name;
    private final PlayersHandler playerHandler;
    private final Bag studentsBag;
    private final TeachersHandler teacherHandler;
    private final ArrayList<Cloud> clouds;
    private final IslandsHandler islandHandler;
    private int coins;
    private final ArrayList<Character> characters;
    private final GameMode gameMode;

    /**
     * Constructor : build game model
     */
    public GameModel(GameListInfo gameInfo) {
        this.name = gameInfo.getGameName();
        this.playerHandler = new PlayersHandler(gameInfo.getNumPlayers());
        this.characters = new ArrayList<>();
        this.coins = 20;
        this.clouds = new ArrayList<>();
        this.islandHandler = new IslandsHandler();
        this.teacherHandler = new TeachersHandler();
        this.studentsBag = new Bag();
        this.gameMode = gameInfo.getGameMode();
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
        for(Player player: getPlayerHandler().getPlayers()){
            player.setNumOfTower(numTowers);
            player.addCards(AssistantCard.createDeck());
            player.getPlayerBoard().addToEntrance(studentsBag.popStudents(numEntranceStudents));
            if (Configurator.isDebug())
                player.setCoin(10);
            else
                player.setCoin(1);
            this.coins--;
        }
        for (int i = 1; i <= numPlayers; i++) {
            clouds.add(new Cloud(i));
        }
        if(gameMode == GameMode.EXPERT){
            Random random = new Random();
            List<Character> list = Configurator.getAllCharactersCards(this);
            if (Configurator.isDebug()) {
                characters.addAll(list);
                for (Character character : characters)
                    character.init(studentsBag);
            } else {
                for (int i = 0; i < 3; i++) {
                    int randInt = random.nextInt(list.size());
                    characters.add(list.get(randInt));
                    list.get(randInt).init(studentsBag);
                    list.remove(randInt);
                }
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
     * Gets TeachersHandler
     * @return TeachersHandler
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

    //TODO: remove, useless
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

    //TODO: remove this method, it's useless
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

    /**
     * Gets game mode
     * @return gameMode
     */
    public GameMode getGameMode() {
        return gameMode;
    }

    public String getGameName() {
        return name;
    }

    public void cloudsRefill() {
        for (Cloud cloud : clouds) {
            cloud.addStudents(studentsBag.popStudents(playerHandler.getNumPlayers()+1));
        }
    }
}
