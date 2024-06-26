package it.polimi.ingsw.util;


import it.polimi.ingsw.model.*;

import java.io.Serializable;

/**
 * Stores information about an action
 */
public class Action implements Serializable {
	private final ActionType actionType;
	private Piece principalPiece;
	private Piece optionalPiece;
	private int integer;

	/**
	 * Constructor: Creates an action
	 * @param actionType     type of action
	 * @param principalPiece piece that I move
	 */
	public Action(ActionType actionType, Piece principalPiece) {
		this.actionType = actionType;
		this.principalPiece = principalPiece;
	}

	/**
	 * Constructor: Creates an action
	 * @param actionType     type of action
	 * @param principalPiece piece that I move
	 * @param optionalPiece  secondary piece that will be moved
	 */
	public Action(ActionType actionType, Piece principalPiece, Piece optionalPiece) {
		this.actionType = actionType;
		this.principalPiece = principalPiece;
		this.optionalPiece = optionalPiece;
	}

	/**
	 * Constructor: Creates an action
	 * @param actionType     type of action
	 * @param principalPiece piece that I move
	 * @param integer        how many steps the piece is moved
	 */
	public Action(ActionType actionType, Piece principalPiece, int integer) {
		this.actionType = actionType;
		this.principalPiece = principalPiece;
		this.integer = integer;
	}

	/**
	 * Constructor: Creates an action
	 * @param actionType type of action
	 * @param integer    how many steps the piece is moved
	 */
	public Action(ActionType actionType, int integer) {
		this.actionType = actionType;
		this.integer = integer;
	}

	/**
	 * Constructor: Creates an action
	 * @param actionType type of action
	 */
	public Action(ActionType actionType) {
		this.actionType = actionType;
	}

	/**
	 * Gets type of action
	 * @return type of action
	 */
	public ActionType getActionType() {
		return this.actionType;
	}

	/**
	 * Gets the piece that will be moved
	 * @return the piece that will be moved
	 */
	public Piece getPrincipalPiece() {
		return this.principalPiece;
	}

	/**
	 * Gets the secondary piece that will be moved
	 * @return the secondary piece that will be moved
	 */
	public Piece getOptionalPiece() {
		return this.optionalPiece;
	}

	/**
	 * Gets how many steps the piece is moved
	 * @return how many steps the piece is moved
	 */
	public int getInteger() {
		return this.integer;
	}
}
