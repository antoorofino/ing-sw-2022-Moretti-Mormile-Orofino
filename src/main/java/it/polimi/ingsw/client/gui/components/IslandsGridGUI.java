package it.polimi.ingsw.client.gui.components;

import it.polimi.ingsw.client.GUIView;
import it.polimi.ingsw.client.gui.utils.dragAndDrop.*;
import it.polimi.ingsw.model.Cloud;
import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.IslandsHandler;
import it.polimi.ingsw.network.messages.SetAction;
import it.polimi.ingsw.util.Action;
import it.polimi.ingsw.util.ActionType;
import it.polimi.ingsw.util.GameMode;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * Grid to manage island on the scene
 */
public class IslandsGridGUI extends ComponentGUI {
    @FXML
    private GridPane islandsGridPane;
    private final List<String> islandsBackground;

    /**
     * Constructor: loads the fxml for island grid
     */
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

    /**
     * Puts components in the grid
     */
    public void setGrid() {
        //Clear grid
        islandsGridPane.getChildren().clear();

        //Set clouds and islands
        setClouds();
        setIslands();
    }

    /**
     * Puts clouds in the correct position in the window
     */
    private void setClouds() {
        CloudGUI cloudGUI;
        int[][] cloudPositions = {{3,2}, {7,2}, {5,2}};
        List<Cloud> clouds = data.getGame().getClouds();
        for (int i = 0; i < clouds.size(); i++) {
            cloudGUI = new CloudGUI();
            cloudGUI.setStudents(clouds.get(i).getStudents());
            int finalI = i;
            // Set interaction features if enabled
            if (data.getPossibleActions().contains(ActionType.CHOOSE_CLOUD) && (clouds.get(i).getStudents().size() > 0 || clouds.stream().noneMatch(c -> c.getStudents().size() != 0))) {
                cloudGUI.getRoot().getStyleClass().add("student-hover");
                cloudGUI.getRoot().setOnMouseClicked(e -> {
                    GUIView.getServerHandler().send(new SetAction(
                            data.getPlayer().getNickname(),
                            new Action(ActionType.CHOOSE_CLOUD, clouds.get(finalI).getCloudID())
                    ));
                    e.consume();
                });
            }
            GridPane.setConstraints(cloudGUI.getRoot(), cloudPositions[i][0], cloudPositions[i][1]);
            islandsGridPane.getChildren().add(cloudGUI.getRoot());
        }
    }

    /**
     * Puts islands in the correct position in the window
     */
    private void setIslands() {
        IslandsHandler handler = data.getGame().getIslandHandler();
        ArrayList<Island> islands = handler.getIslands();

        int index = (12 - handler.getCountsLastMerge()) % 12; // countsLastMerge
        final int[][] absolutePositions = { { 0, 1 }, { 0, 3 }, { 0, 5 }, { 0, 7 }, { 0, 9 }, { 2, 10 }, { 4, 9 }, { 4, 7 }, { 4, 5 }, { 4, 3 }, { 4, 1 }, { 2, 0 } };
        final int[][] relativePosition = { { -1, 0 }, { 0, 1 }, { 0, 1 }, { 0, 1 }, { 0, 1 }, { 0, 1 }, { 1, 0 }, { 0, -1 }, { 0, -1 }, { 0, -1 }, { 0, -1 }, { 0, -1 }};
        int x = 0;
        int y = 0;
        boolean mother;
        IslandGUI islandGUI;
        for (Island island : islands){
            mother = island.getID() == handler.getMotherNature();
            for (int i = 0; i < island.getSize(); i++){
                islandGUI = new IslandGUI();
                islandGUI.setIslandImage(islandsBackground.get(island.getID() + i));
                if (island.getIslandOwner() != null)
                    islandGUI.setTower(island.getIslandOwner().getTowerColor());
                if (island.getSize() == 1 || i == 1) {
                    islandGUI.setStudents(island.getStudentsOnIsland());
                    islandGUI.setNoTile(island.getFlagNoInfluence());
                    if(mother)
                        islandGUI.setMotherNature();
                }
                setIslandDropHandler(islandGUI.getRoot(), island.getID());
                //Set mother nature on drag handlers
                if (data.getPossibleActions().contains(ActionType.MOVE_MOTHER_NATURE))
                    setMotherNatureDragHandlers(islandGUI.getMotherNatureImage(), island.getID());
                if (data.getGame().getGameMode() == GameMode.EXPERT)
                    setIslandClickable(islandGUI.getRoot(), island.getID());
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

    /**
     * Adds event to make possible dropping elements on the island
     * @param islandPane island pane
     * @param islandId island id
     */
    private void setIslandDropHandler(Parent islandPane, int islandId) { //WIP
        // Single logic checks
        Supplier<Boolean> islandAcceptsEntranceStudent = () -> data.getPossibleActions().contains(ActionType.MOVE_STUDENT_TO_ISLAND);
        Supplier<Boolean> islandAcceptsCharacterStudent = () -> data.getPlayer().getActiveCharacter() != null && data.getPlayer().getActiveCharacter().getID() == 1;
        Supplier<Boolean> islandAcceptsMother  = () -> {
            if (data.getPlayer().getLastCardUsed() == null)
                return false;
            int maxSteps = data.getPlayer().getLastCardUsed().getMovements();
            if (data.getPlayer().getActiveCharacter() != null && data.getPlayer().getActiveCharacter().getID() == 4)
                maxSteps += 2;
            int mother = data.getGame().getIslandHandler().getMotherNature();
            int steps = islandId > mother ? islandId - mother : islandId - mother + data.getGame().getIslandHandler().getIslands().size();
            return data.getPossibleActions().contains(ActionType.MOVE_MOTHER_NATURE) && steps <= maxSteps && steps > 0;
        };
        Supplier<Boolean> islandAcceptsNoTile = () -> data.getPlayer().getActiveCharacter() != null && data.getPlayer().getActiveCharacter().getID() == 5;
        // Full logic check
        BiConsumer<DragAndDropInfo, Runnable> check = (ddi, run) -> {
            switch (ddi.getType()) {
                case PIECE:
                    if (ddi.getOrigin() == DragOrigin.ENTRANCE && islandAcceptsEntranceStudent.get())
                        run.run();
                    else if (ddi.getOrigin() == DragOrigin.CHARACTER && islandAcceptsCharacterStudent.get())
                        run.run();
                    break;
                case MOTHER:
                    if (islandAcceptsMother.get())
                        run.run();
                    break;
                case NO_TILE:
                    if (islandAcceptsNoTile.get())
                        run.run();
                    break;
            }
        };
        // Set drop handlers
        islandPane.setOnDragEntered(e -> {
            Dragboard db = e.getDragboard();
            DragAndDropInfo ddi = DragAndDropUtils.fromString(db.getString());
            check.accept(ddi, () -> islandPane.getStyleClass().add("island-hover"));
            e.consume();
        });
        islandPane.setOnDragExited(e -> {
            Dragboard db = e.getDragboard();
            DragAndDropInfo ddi = DragAndDropUtils.fromString(db.getString());
            check.accept(ddi, () -> islandPane.getStyleClass().remove("island-hover"));
            e.consume();
        });
        islandPane.setOnDragOver(e -> {
            Dragboard db = e.getDragboard();
            DragAndDropInfo ddi = DragAndDropUtils.fromString(db.getString());
            check.accept(ddi, () -> e.acceptTransferModes(TransferMode.MOVE));
            e.consume();
        });
        islandPane.setOnDragDropped(e -> {
            Dragboard db = e.getDragboard();
            DragAndDropInfo ddi = DragAndDropUtils.fromString(db.getString());
            ddi.setDestination(DropDestination.ISLAND);
            switch (ddi.getType()) {
                case PIECE:
                case NO_TILE:
                    ddi.setIslandId(islandId);
                    break;
                case MOTHER:
                    ddi.setSteps(islandId > ddi.getIslandId() ?
                            islandId - ddi.getIslandId() :
                            islandId - ddi.getIslandId() + data.getGame().getIslandHandler().getIslands().size()
                    );
            }
            db.clear();
            db.setContent(DragAndDropUtils.toClipboardContent(ddi));
            e.consume();
        });
    }

    /**
     * Adds event to make possible drag and drop of mother nature
     * @param motherPane mother nature
     * @param islandId origin island id
     */
    private void setMotherNatureDragHandlers(Parent motherPane, int islandId) {
        motherPane.setOnDragDetected(e -> {
            if (motherPane.getStyleClass().size() > 0) {
                Dragboard db = motherPane.startDragAndDrop(TransferMode.ANY);
                DragAndDropInfo ddi =  new DragAndDropInfo(DragOrigin.ISLAND,
                        DragType.MOTHER,
                        islandId
                );
                motherPane.getStyleClass().clear();
                db.setContent(DragAndDropUtils.toClipboardContent(ddi));
                db.setDragView(new Image(String.valueOf(getClass().getResource("/gui/images/mother_nature.png")), 20.0, 20.0, true, true));
            }
            e.consume();
        });
        motherPane.setOnDragDone(e -> {
            Dragboard db = e.getDragboard();
            DragAndDropInfo ddi = DragAndDropUtils.fromString(db.getString());
            if (ddi.getDestination() == DropDestination.ISLAND) {
                GUIView.getServerHandler().send(new SetAction(data.getPlayer().getNickname(),
                        new Action(ActionType.MOVE_MOTHER_NATURE, ddi.getSteps())
                ));
            } else {
                motherPane.getStyleClass().clear();
                motherPane.getStyleClass().add("mother-nature-background");
                motherPane.getStyleClass().add("student-hover");
            }
            e.consume();
        });
    }

    /**
     * Make it possible to click an island for character number 3 effect
     * @param islandPane pane of the island
     * @param islandId id of the island
     */
    private void setIslandClickable(Parent islandPane, int islandId) {
        boolean playerChoseCharacter = data.getPlayer().getActiveCharacter() != null;
        boolean playerActiveCharacter = data.getPlayer().getRoundActions().getActionsList().stream()
                .map(Action::getActionType).filter(t -> t == ActionType.ACTIVATED_CHARACTER).findFirst().orElse(null) != null;
        if (playerChoseCharacter && !playerActiveCharacter && data.getPlayer().getActiveCharacter().getID() == 3) {
            islandPane.getStyleClass().add("island-clickable-hover");
            islandPane.setOnMouseClicked(e -> {
                GUIView.getServerHandler().send(new SetAction(
                        data.getPlayer().getNickname(),
                        new Action(ActionType.DOUBLE_INFLUENCE, islandId)
                ));
                e.consume();
            });
        }
    }
}
