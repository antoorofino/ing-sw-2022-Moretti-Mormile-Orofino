package it.polimi.ingsw.client.gui.controllers;


import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class MineBoardController extends SceneController{
    @FXML
    GridPane green1, green2, green3, green4, green5, green6, green7, green8, green9, green10;
    ArrayList<GridPane> studentsGreenRoom = new ArrayList<GridPane>();

    @FXML
    public void initialize() {
        buildArrayList();
    }
    public void moveToIsland(){
        green1.getStyleClass().clear();
    }
    public void moveToRoom(){
        green1.getStyleClass().add("student-green-background");
    }

    private void buildArrayList(){
        /*--GREEN--*/
        studentsGreenRoom.add(green1);
        studentsGreenRoom.add(green2);
        studentsGreenRoom.add(green3);
        studentsGreenRoom.add(green4);
        studentsGreenRoom.add(green5);
        studentsGreenRoom.add(green6);
        studentsGreenRoom.add(green7);
        studentsGreenRoom.add(green8);
        studentsGreenRoom.add(green9);
        studentsGreenRoom.add(green10);
    }
}
