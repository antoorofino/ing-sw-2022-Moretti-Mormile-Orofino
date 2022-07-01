package it.polimi.ingsw.model;

import it.polimi.ingsw.server.rules.Rules;
import it.polimi.ingsw.util.exception.SpecificStudentNotFoundException;
import it.polimi.ingsw.util.exception.StudentNotPresentException;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Stores information about a character game's card
 */
public class Character implements Serializable {
    private final String name;
    private final String description;
    private final int ID;
    private final Rules rules;
    private final ArrayList<Piece> studentsOnCard;
    private int islandFlag;
    private int cost;
    private boolean first;

    /**
     * Constructor: builds the Character Card
     * @param name          Name of character
     * @param description   Character's description
     * @param cost          Cost of the card
     * @param ID            Identifier of the card
     * @param rules         Card's ability
     */
    public Character(String name, String description, int cost, int ID,Rules rules){
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.ID=ID;
        this.rules = rules;
        this.studentsOnCard = new ArrayList<Piece>();
        this.first = true;
    }

    /**
     * Initializes of Character card in case of EXPERT mode
     * @param bag bag
     */
    public void init(Bag bag){
        switch (ID) {
            case 1:
            case 11:
                studentsOnCard.addAll(bag.popStudents(4));
                break;
            case 5:
                islandFlag = 4;
                break;
            case 7:
                studentsOnCard.addAll(bag.popStudents(6));
                break;
            default:
        }
    }

    /**
     * Gets name of character
     * @return character's name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets description of character
     * @return character's description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets ID of character
     * @return character's ID
     */
    public int getID() {
        return ID;
    }

    /**
     * Gets rules of character
     * @return character's rules that specify card's ability
     */
    public Rules getRules() {
        return rules;
    }

    /**
     * Gets island flag
     * @return island flag that
     */
    public int getIslandFlag() {
        return islandFlag;
    }

    /**
     * Sets island flag
     * @param num
     */
    public void setIslandFlag(int num){
        this.islandFlag=num;
    }

    /**
     * Increases number of island flag
     */
    public void addIslandFlag(){
        this.islandFlag++;
    }

    /**
     * Removes one island flag
     */
    public void removeIslandFlag() {
        this.islandFlag--;
    }

    /**
     * Adds students on character
     * @param students contains students that I'll put on character
     */
    public void addStudents(ArrayList<Piece> students){
        this.studentsOnCard.addAll(students);
    }

    /**
     * Gets students on character
     * @return list of students on character
     */
    public ArrayList<Piece> getStudents() {
        return new ArrayList<>(studentsOnCard);
    }

    /**
     * Removes specify student from character
     * @param student student to remove
     * @throws StudentNotPresentException not students on card
     * @throws SpecificStudentNotFoundException not specified student on card
     */
    public void delStudent(Piece student) throws StudentNotPresentException, SpecificStudentNotFoundException {
        if(studentsOnCard.isEmpty()) throw new StudentNotPresentException("There aren't students on this cards");
        if(!studentsOnCard.remove(student))  throw new SpecificStudentNotFoundException("There isn't the specific student on this cards");
    }

    /**
     * Increases cost to activate character's ability
     */
    public void increaseCost(){
        if(first)
            this.cost++;
        first = false;
    }

    /**
     * Gets cost to activate character's ability
     * @return cost to activate character's ability
     */
    public int getCost(){return this.cost;}

}
