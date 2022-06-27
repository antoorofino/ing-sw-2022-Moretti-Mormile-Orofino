package it.polimi.ingsw.client.gui.components;

import it.polimi.ingsw.client.GUIView;
import it.polimi.ingsw.client.gui.utils.ClientData;
import it.polimi.ingsw.client.gui.utils.dragAndDrop.*;
import it.polimi.ingsw.model.Cloud;
import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.IslandsHandler;
import it.polimi.ingsw.network.messages.SetAction;
import it.polimi.ingsw.util.Action;
import it.polimi.ingsw.util.ActionType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class IslandsGridGUI {
    @FXML
    private GridPane islandsGridPane;
    private final List<String> islandsBackground;
    private final ClientData data = ClientData.getInstance();
    Parent root;

    public IslandsGridGUI() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/fxml/islandsGrid.fxml"));
        fxmlLoader.setController(this);
        try {
            root = fxmlLoader.load();
        } catch (IOException ignored) {
        }

        Random rand = new Random();
        List<String> styleList = Arrays.asList("island-1", "island-2", "island-3");
        islandsBackground = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            islandsBackground.add(styleList.get(rand.nextInt(styleList.size())));
        }
    }

    public Parent getRoot() {
        return root;
    }

    public void setGrid() {
        //Clear grid
        islandsGridPane.getChildren().clear();

        //Set clouds and islands
        setClouds();
        setIslands();
    }

    private void setClouds() {
        CloudGUI cloudGUI;
        int[][] cloudPositions = {{3,2}, {7,2}, {5,2}};
        List<Cloud> clouds = data.getGame().getClouds();
        for (int i = 0; i < clouds.size(); i++) {
            cloudGUI = new CloudGUI();
            cloudGUI.setStudents(clouds.get(i).getStudents());
            int finalI = i;
            cloudGUI.getRoot().setOnMouseClicked(e -> {
                if (ClientData.getInstance().getPossibleActions().contains(ActionType.CHOOSE_CLOUD))
                    GUIView.getServerHandler().send(new SetAction(
                            data.getPlayer().getNickname(),
                            new Action(ActionType.CHOOSE_CLOUD, clouds.get(finalI).getCloudID())
                    ));
                e.consume();
            });
            GridPane.setConstraints(cloudGUI.getRoot(), cloudPositions[i][0], cloudPositions[i][1]);
            islandsGridPane.getChildren().add(cloudGUI.getRoot());
        }
    }

    private void setIslands() {
        IslandsHandler handler = data.getGame().getIslandHandler();
        ArrayList<Island> islands = handler.getIslands();

        int index = (12 - handler.getCountsLastMerge())%12; // countsLastMerge
        final int[][] absolutePositions = { { 0, 1 }, { 0, 3 }, { 0, 5 }, { 0, 7 }, { 0, 9 }, { 2, 10 }, { 4, 9 }, { 4, 7 }, { 4, 5 }, { 4, 3 }, { 4, 1 }, { 2, 0 } };
        final int[][] relativePosition = { { -1, 0 }, { 0, 1 }, { 0, 1 }, { 0, 1 }, { 0, 1 }, { 0, 1 }, { 1, 0 }, { 0, -1 }, { 0, -1 }, { 0, -1 }, { 0, -1 }, { 0, -1 }};
        int x = 0;
        int y = 0;
        boolean mother;
        IslandGUI islandGUI;
        for(Island island : islands){
            mother = island.getID() == handler.getMotherNature();
            for(int i = 0; i < island.getSize(); i++){
                islandGUI = new IslandGUI();
                islandGUI.setIslandImage(islandsBackground.get(island.getID() + i));
                if(island.getIslandOwner() != null)
                    islandGUI.setTower(island.getIslandOwner().getTowerColor());
                if(island.getSize()==1 || i==1) {
                    islandGUI.setStudents(island.getStudentsOnIsland());
                    if(mother)
                        islandGUI.setMotherNature();
                }
                setIslandDropHandler(islandGUI, island.getID());
                if(i!=0){
                    y += relativePosition[index][0];
                    x += relativePosition[index][1];
                }else{
                    y = absolutePositions[index][0];
                    x = absolutePositions[index][1];
                }
                GridPane.setConstraints(islandGUI.getRoot(), x, y);
                islandsGridPane.getChildren().add(islandGUI.getRoot());
                index = (index + 1)%12;
            }
        }
    }

    private void setIslandDropHandler(IslandGUI islandGUI, int islandId) {
        //Set islands on drop handlers
        islandGUI.getRoot().setOnDragEntered(e -> {
            Dragboard db = e.getDragboard();
            DragAndDropInfo ddi = DragAndDropUtils.fromString(db.getString());
            if (ddi.getType() == DragType.PIECE && DragAndDropUtils.islandAcceptsStudents()) {
                islandGUI.getRoot().getStyleClass().add("island-hover");
            } else if(ddi.getType() == DragType.MOTHER && DragAndDropUtils.islandAcceptMotherNature(islandId)) {
                islandGUI.getRoot().getStyleClass().add("island-hover");
            }
            e.consume();
        });
        islandGUI.getRoot().setOnDragExited(e -> {
            Dragboard db = e.getDragboard();
            DragAndDropInfo ddi = DragAndDropUtils.fromString(db.getString());
            if (ddi.getType() == DragType.PIECE && DragAndDropUtils.islandAcceptsStudents()) {
                islandGUI.getRoot().getStyleClass().remove("island-hover");
            } else if(ddi.getType() == DragType.MOTHER && DragAndDropUtils.islandAcceptMotherNature(islandId)) {
                islandGUI.getRoot().getStyleClass().remove("island-hover");
            }
            e.consume();
        });
        islandGUI.getRoot().setOnDragOver(e -> {
            Dragboard db = e.getDragboard();
            DragAndDropInfo ddi = DragAndDropUtils.fromString(db.getString());
            if (ddi.getType() == DragType.PIECE && DragAndDropUtils.islandAcceptsStudents()) {
                e.acceptTransferModes(TransferMode.MOVE);
            } else if(ddi.getType() == DragType.MOTHER && DragAndDropUtils.islandAcceptMotherNature(islandId)) {
                e.acceptTransferModes(TransferMode.MOVE);
            }
            e.consume();
        });
        islandGUI.getRoot().setOnDragDropped(e -> {
            Dragboard db = e.getDragboard();
            DragAndDropInfo ddi = DragAndDropUtils.fromString(db.getString());
            ddi.setDestination(DropDestination.ISLAND);
            if (ddi.getType() == DragType.PIECE) {
                ddi.setIslandId(islandId);
            } else if(ddi.getType() == DragType.MOTHER) {
                ddi.setSteps(islandId > ddi.getIslandId() ?
                        islandId - ddi.getIslandId() :
                        islandId - ddi.getIslandId() + data.getGame().getIslandHandler().getIslands().size());
            }
            db.clear();
            ClipboardContent cc = new ClipboardContent();
            cc.putString(DragAndDropUtils.toString(ddi));
            db.setContent(cc);
            e.consume();
        });

        //Set mother nature on drag handlers
        Pane m = islandGUI.getMotherNatureImage();
        m.setOnDragDetected(e -> {
            if (m.getStyleClass().size() > 0) {
                Dragboard db = m.startDragAndDrop(TransferMode.ANY);
                ClipboardContent cc = new ClipboardContent();
                DragAndDropInfo ddi =  new DragAndDropInfo(DragOrigin.ISLAND,
                        DragType.MOTHER,
                        islandId
                );
                cc.putString(DragAndDropUtils.toString(ddi));
                m.getStyleClass().clear();
                db.setContent(cc);
                db.setDragView(new Image(String.valueOf(getClass().getResource("/gui/images/mother_nature.png")), 20.0, 20.0, true, true));
            }
            e.consume();
        });
        m.setOnDragDone(e -> {
            Dragboard db = e.getDragboard();
            DragAndDropInfo ddi = DragAndDropUtils.fromString(db.getString());
            if (ddi.getDestination() == DropDestination.NONE) {
                m.getStyleClass().clear();
                m.getStyleClass().add("mother-nature-background");
                m.getStyleClass().add("student-hover");
            } else {
                GUIView.getServerHandler().send(new SetAction(ClientData.getInstance().getPlayer().getNickname(),
                        new Action(ActionType.MOVE_MOTHER_NATURE, ddi.getSteps())
                ));
            }
            e.consume();
        });
    }
}
