package it.polimi.ingsw.client.gui.controllers;


import it.polimi.ingsw.client.GUIView;
import it.polimi.ingsw.client.gui.utils.GUISwitcher;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Control;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class MineBoardController extends SceneController{
    //FIXME : per ora tutto statico bisogna prendere dati veri della partita
    //TODO: mettere id nei componenti dell'FXML
    protected GridPane selectedPiece;
    protected Scene s;
    private int size=0;
    @FXML
    GridPane green1, green2, green3, green4, green5, green6, green7, green8, green9, green10,
             red1, red2, red3, red4, red5, red6, red7, red8, red9, red10,
             yellow1, yellow2, yellow3, yellow4, yellow5, yellow6, yellow7, yellow8, yellow9, yellow10,
             pink1, pink2, pink3, pink4, pink5, pink6, pink7, pink8, pink9, pink10,
             blue1, blue2, blue3, blue4, blue5, blue6, blue7, blue8, blue9, blue10,
             room1, room2, room3, room4, room5, room6, room7, room8, room9;
    //ArrayList<GridPane> studentsGreenRoom = new ArrayList<GridPane>();

    @FXML
    public void initialize() {
        addListen(room1, room2, room3, room4, room5, room6, room7, room8, room9);
        s = GUISwitcher.getInstance().getPrimaryStage().getScene();
        s.getRoot().applyCss();
        //buildArrayList();
        System.out.println("id"+GUIView.getPlayerId());

    }
    public void moveToIsland(){
        selectedPiece.getStyleClass().clear();
        selectedPiece = null;
    }
    public void moveToRoom(){

        //HP la sala sia vuota
        if(selectedPiece!=null){
            size++;
            String id="#";
            if(selectedPiece.getStyleClass().contains("student-green-background"))
                id = id + "g";
            id = id +size;
            GridPane firstFree = (GridPane) s.getRoot().lookup(id);
            firstFree.getStyleClass().add("student-green-background");
            selectedPiece.getStyleClass().clear();
            selectedPiece=null;
        }


    }

    /*private void buildArrayList(){
        /*--GREEN--
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
    }*/
    public void addListen(GridPane c1, GridPane c2, GridPane c3, GridPane c4, GridPane c5, GridPane c6, GridPane c7, GridPane c8, GridPane c9){
        c1.setOnMouseClicked(new MyEventHandler());
        c2.setOnMouseClicked(new MyEventHandler());
        c3.setOnMouseClicked(new MyEventHandler());
        c4.setOnMouseClicked(new MyEventHandler());
        c5.setOnMouseClicked(new MyEventHandler());
        c6.setOnMouseClicked(new MyEventHandler());
        c7.setOnMouseClicked(new MyEventHandler());
        c8.setOnMouseClicked(new MyEventHandler());
        c9.setOnMouseClicked(new MyEventHandler());
    }
    private class MyEventHandler implements EventHandler<Event>{
        String id;
        @Override
        public void handle(Event event) {
            resetLastPiece(selectedPiece);
            selectedPiece = (GridPane)event.getSource();
            //System.out.println(green2+"----"+selectedPiece);
            selectedPiece.getStyleClass().add("student-select");

//            System.out.println(green1+"-----"+s.getRoot().lookup("#g1"));
        }
    }

    public void resetLastPiece(GridPane s){
        String background = new String();
        if(s!=null) {

            //System.out.println(s.getStyleClass());
            //s.getStyleClass().clear();
            if(s.getStyleClass().contains("student-green-background"))
                background="student-green-background";
            else if(s.getStyleClass().contains("student-pink-background"))
                background="student-pink-background";
            else if(s.getStyleClass().contains("student-green-background"))
                background="student-red-background";
            else if(s.getStyleClass().contains("student-yellow-background"))
                background="student-yellow-background";
            else if(s.getStyleClass().contains("student-blue-background"))
                background="student-blue-background";
            s.getStyleClass().clear();
            s.getStyleClass().add(background);
        }
        else
            System.out.println("nothing selected before");

    }


}
