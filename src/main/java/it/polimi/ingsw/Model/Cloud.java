package it.polimi.ingsw.Model;

import java.util.ArrayList;

public class Cloud {
    private ArrayList<Piece> studentsClouds;
    private int cloudID;

    public Cloud(int ID){
        this.studentsClouds = new ArrayList<Piece>();
        this.cloudID=ID;
    }

    public ArrayList<Piece> getStudents() {
        return studentsClouds;
    }

    public int getCloudID() {
        return cloudID;
    }


    public void addStudents(ArrayList<Piece> students){
        if(students.isEmpty())
            students.addAll(students);
    }
}
