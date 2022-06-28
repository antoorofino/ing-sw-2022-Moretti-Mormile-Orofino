package it.polimi.ingsw.client.gui.utils.dragAndDrop;

import it.polimi.ingsw.model.Piece;

import java.io.Serializable;

public class DragAndDropInfo implements Serializable {
    private final DragOrigin origin;
    private DropDestination destination;
    private final DragType type;
    private Piece piece;
    private int islandId;
    private int steps;

    public DragAndDropInfo(DragOrigin origin, DragType type, Piece piece) {
        this.origin = origin;
        this.type = type;
        this.piece = piece;
        this.destination = DropDestination.NONE;
    }

    public DragAndDropInfo(DragOrigin origin, DragType type, int islandId) {
        this.origin = origin;
        this.type = type;
        this.islandId = islandId;
        this.destination = DropDestination.NONE;
    }

    public DragAndDropInfo(DragOrigin origin, DragType type) {
        this.origin = origin;
        this.type = type;
        this.destination = DropDestination.NONE;
    }

    public DragOrigin getOrigin() {
        return origin;
    }

    public DropDestination getDestination() {
        return destination;
    }

    public void setDestination(DropDestination destination) {
        this.destination = destination;
    }

    public DragType getType() {
        return type;
    }

    public Piece getPiece() {
        return piece;
    }

    public int getIslandId() {
        return islandId;
    }

    public void setIslandId(int islandId) {
        this.islandId = islandId;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }
}
