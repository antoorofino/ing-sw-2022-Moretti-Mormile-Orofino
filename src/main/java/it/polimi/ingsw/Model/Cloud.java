package it.polimi.ingsw.Model;

import java.util.ArrayList;

public class Cloud {
    private ArrayList<Piece> studentsClouds;
    private int cloudID;

    public ArrayList<Piece> getStudents() {
        return studentsClouds;
    }

    public int getCloudID() {
        return cloudID;
    }

    //TODO implement method addStudent
    public void addStudents(ArrayList<Piece> students){}
}
