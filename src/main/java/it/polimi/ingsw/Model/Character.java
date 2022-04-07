package it.polimi.ingsw.Model;

import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Exception.SpecificStudentNotFoundException;
import it.polimi.ingsw.Exception.StudentNotPresentException;

import java.util.ArrayList;

public class Character {
    private String name;
    private String description;
    private int ID;
    private Rules rules;
    private ArrayList<Piece> studentsOnCard;
    private int islandFlag;
    private int cost;

    public Character(String name, String description, int cost, int ID,Rules rules){
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.ID=ID;
        this.rules = rules;
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

    public Rules getRules() {
        return rules;
    }

    public int getIslandFlag() {
        return islandFlag;
    }

    public void setIslandFlag(int num){
        this.islandFlag=num;
    }

    public void addStudents(ArrayList<Piece> students){
        this.studentsOnCard.addAll(students);
    }

    public ArrayList<Piece> getStudents() {
        return new ArrayList<>(studentsOnCard);
    }

    public void delStudent(Piece student) throws StudentNotPresentException, SpecificStudentNotFoundException {
        if(studentsOnCard.isEmpty()) throw new StudentNotPresentException("There aren't students on this cards");
        if(!studentsOnCard.remove(student))  throw new SpecificStudentNotFoundException("There isn't the specific student on this cards");
    }

    public void increaseCost(){
        this.cost++;
    }
}
