package it.polimi.ingsw.client.gui.controllers;


import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

public class MineBoardController extends SceneController{
    @FXML
    GridPane green1;

    public void moveToIsland(){
        green1.getStyleClass().clear();
    }
    public void moveToRoom(){
        green1.getStyleClass().add("student-green-background");
    }
}
