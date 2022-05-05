package it.polimi.ingsw.model;

import java.util.ArrayList;

/**
 * Stores information about a cloud
 */

public class Cloud {
    private ArrayList<Piece> studentsClouds;
    private int cloudID;

    /**
     * Constructor: build cloud
     * @param ID identifier of cloud
     */
    public Cloud(int ID){
        this.studentsClouds = new ArrayList<Piece>();
        this.cloudID=ID;
    }

    /**
     * Get students on cloud
     * @return list of students on cloud
     */
    public ArrayList<Piece> getStudents() {
        return new ArrayList<>(studentsClouds);
    }

    /**
     * Gets cloud ID
     * @return cloud ID
     */
    public int getCloudID() {
        return cloudID;
    }

    /**
     * Adds students on cloud
     * @param students list of students to add on cloud
     */
    public void addStudents(ArrayList<Piece> students){
        this.studentsClouds = new ArrayList<>(students);
    }
}
