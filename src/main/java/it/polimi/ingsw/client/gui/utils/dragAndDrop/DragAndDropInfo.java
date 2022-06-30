package it.polimi.ingsw.client.gui.utils.dragAndDrop;

import it.polimi.ingsw.model.Piece;

import java.io.Serializable;

/**
 * Stores information about drag and drop event
 */
public class DragAndDropInfo implements Serializable {
    private final DragOrigin origin;
    private DropDestination destination;
    private final DragType type;
    private Piece piece;
    private int islandId;
    private int steps;

    /**
     * Constructor: Builds object that contains info for drag and drop event
     * @param origin where starts drag and drop
     * @param type type of movements
     * @param piece specify on which type of piece we do drag and drop
     */
    public DragAndDropInfo(DragOrigin origin, DragType type, Piece piece) {
        this.origin = origin;
        this.type = type;
        this.piece = piece;
        this.destination = DropDestination.NONE;
    }


    /**
     * Constructor: Builds object that contains info for drag and drop event. Used for drag and drop of mother nature
     * @param origin where starts drag and drop
     * @param type type of movements
     * @param islandId specify island, in case player moves students id of destination island, in case players moves mother nature origin
     */
    public DragAndDropInfo(DragOrigin origin, DragType type, int islandId) {
        this.origin = origin;
        this.type = type;
        this.islandId = islandId;
        this.destination = DropDestination.NONE;
    }

    /**
     * Constructor: Builds object that contains info for drag and drop event.
     * @param origin where starts drag and drop
     * @param type type of movements
     */
    public DragAndDropInfo(DragOrigin origin, DragType type) {
        this.origin = origin;
        this.type = type;
        this.destination = DropDestination.NONE;
    }

    /**
     * Gets the origin of drag and drop event
     * @return where starts drag and drop event
     */
    public DragOrigin getOrigin() {
        return origin;
    }

    /**
     * Gets the destination of drag and drop event
     * @return where ends drag and drop event
     */
    public DropDestination getDestination() {
        return destination;
    }

    /**
     * Sets destination of drag and drop event
     * @param destination where ends drag and drop event
     */
    public void setDestination(DropDestination destination) {
        this.destination = destination;
    }

    /**
     * Gets type of movements
     * @return type of movements
     */

    public DragType getType() {
        return type;
    }

    /**
     * Gets type of piece on the player did drag and drop
     * @return piece that player moves
     */
    public Piece getPiece() {
        return piece;
    }

    /**
     * Gets the id of destination island in case player moves students, origin island in case player moves mother nature
     * @return island id
     */
    public int getIslandId() {
        return islandId;
    }

    /**
     * Sets the id of destination island in case player moves students, origin island in case player moves mother nature
     * @param islandId island id
     */
    public void setIslandId(int islandId) {
        this.islandId = islandId;
    }

    /**
     * Sets type of piece
     * @param piece type of piece on which the player does drag and drop
     */
    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    /**
     * Gets how many steps mother nature does when player does drag and drop
     * @return number of steps
     */
    public int getSteps() {
        return steps;
    }

    /**
     * Sets how many steps mother nature does when player does drag and drop
     * @param steps number of steps
     */
    public void setSteps(int steps) {
        this.steps = steps;
    }
}
