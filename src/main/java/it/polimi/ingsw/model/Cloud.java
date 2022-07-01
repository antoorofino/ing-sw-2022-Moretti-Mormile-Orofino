package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Stores information about a cloud
 */
public class Cloud implements Serializable {
    private ArrayList<Piece> studentsClouds;
    private int cloudID;

    /**
     * Constructor: builds cloud
     * @param ID identifier of cloud
     */
    public Cloud(int ID){
        this.studentsClouds = new ArrayList<Piece>();
        this.cloudID=ID;
    }

    /**
     * Adds students on cloud
     * @param students list of students to add on cloud
     */
    public void addStudents(List<Piece> students){
        this.studentsClouds = new ArrayList<>(students);
    }

    /**
     * Gets students on cloud
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

}
