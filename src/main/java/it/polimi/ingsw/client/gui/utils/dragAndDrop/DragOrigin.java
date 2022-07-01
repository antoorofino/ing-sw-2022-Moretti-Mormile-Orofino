package it.polimi.ingsw.client.gui.utils.dragAndDrop;

/**
 * Type of drag origin
 */
public enum DragOrigin {
    /**
     * Drag starts from entrance
     */
    ENTRANCE,
    /**
     * Drag starts from dining room
     */
    DINING,
    /**
     * Drag starts from island
     */
    ISLAND,
    /**
     * Drag starts from character, only in expert mode
     */
    CHARACTER
}
