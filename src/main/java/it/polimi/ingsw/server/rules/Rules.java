package it.polimi.ingsw.server.rules;

import it.polimi.ingsw.util.Action;
import it.polimi.ingsw.util.ActionType;
import it.polimi.ingsw.util.RoundActions;
import it.polimi.ingsw.util.exception.*;
import it.polimi.ingsw.model.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Contains the rules of the game when choosing expert mode
 */
public class Rules implements Serializable {
	protected GameModel game;

	/**
	 * Create the game rules
	 * @param game obj that contains the game status
	 */
	public Rules(GameModel game){
		this.game = game;
	}

	/**
	 * Calculate the next possible action of the current player
	 * @return ArrayList of Action that current player can do
	 */
	public RoundActions nextPossibleActions() {
		RoundActions roundActions = getCurrentPlayer().getRoundActions();
		RoundActions nextPossibleActions = new RoundActions();

		if (roundActions.hasMovedStudents() < game.getPlayerHandler().getNumPlayers() + 1) {
			nextPossibleActions.add(new Action(ActionType.MOVE_STUDENT_TO_ISLAND));
			nextPossibleActions.add(new Action(ActionType.MOVE_STUDENT_TO_DININGROOM));
		} else {
			if (!roundActions.hasMovedMother())
				nextPossibleActions.add(new Action(ActionType.MOVE_MOTHER_NATURE));
			else
				nextPossibleActions.add(new Action(ActionType.CHOOSE_CLOUD));
		}
		return nextPossibleActions;
	}

	/**
	 * Verify and perform action made by the player
	 * @param action the player's action
	 * @return true if the action is valid
	 */
	public boolean doAction(Action action){
		// check if he can do the action
		List<ActionType> possibleAction = nextPossibleActions().getActionsList()
				.stream().map(Action::getActionType).collect(Collectors.toList());
		if(!possibleAction.contains(action.getActionType()))
			return false;
		boolean returnValue = false;
		switch (action.getActionType()) {
			case MOVE_STUDENT_TO_DININGROOM:
				returnValue = doMoveDiningRoom(action);
				break;
			case MOVE_STUDENT_TO_ISLAND:
				returnValue = doMoveIsland(action);
				break;
			case MOVE_MOTHER_NATURE:
				returnValue = doMoveMother(action);
				break;
			case CHOOSE_CLOUD:
				returnValue = doChooseCloud(action);
				break;
		}
		return returnValue;
	}

	/**
	 * Moves the student from the entrance to the dining room
	 * @param action the player's action
	 * @return true if the action is valid
	 */
	protected boolean doMoveDiningRoom(Action action) {
		try {
			getCurrentPlayer().getPlayerBoard().removeFromEntrance(action.getPrincipalPiece());
		} catch (SpecificStudentNotFoundException e) {
			return false;
		}

		getCurrentPlayer().getPlayerBoard().addStudentToRoom(action.getPrincipalPiece());
		controlTeacher();
		return true;
	}

	/**
	 * Moves the student from the entrance to chosen island
	 * @param action the player's action
	 * @return true if the action is valid
	 */
	protected boolean doMoveIsland(Action action) {
		try {
			getCurrentPlayer().getPlayerBoard().removeFromEntrance(action.getPrincipalPiece());
		} catch (SpecificStudentNotFoundException e) {
			System.out.println(e.getMessage());
			return false;
		}

		try {
			game.getIslandHandler().getIslandByID(action.getInteger()).addStudent(action.getPrincipalPiece());
		} catch (SpecificIslandNotFoundException e) {
			System.out.println(e.getMessage());
			getCurrentPlayer().getPlayerBoard().addToEntrance(new ArrayList<>(Arrays.asList(action.getPrincipalPiece())));
			return false;
		}
		return true;
	}

	/**
	 * Moves mother nature
	 * @param action the player's action
	 * @return true if the action is valid
	 */
	protected boolean doMoveMother(Action action){
		if (getCurrentPlayer().getLastCardUsed().getMovements() >= action.getInteger() && action.getInteger() > 0){
			game.getIslandHandler().moveMotherNature(action.getInteger());
			calculateInfluence();
			return true;
		}
		return false;
	}

	/**
	 * Moves the student from the chosen cloud to the entrance
	 * @param action the player's action
	 * @return true if the action is valid
	 */
	protected boolean doChooseCloud(Action action) {
		Cloud cloud;
		try {
			cloud = game.getCloudByID(action.getInteger());
		} catch (SpecificCloudNotFoundException e) {
			System.out.println(e.getMessage());
			return false;
		}
		if(cloud.getStudents().isEmpty() && game.getClouds().stream().anyMatch(c -> c.getStudents().size() != 0))
			return false;
		getCurrentPlayer().getPlayerBoard().addToEntrance(cloud.getStudents());
		cloud.addStudents(new ArrayList<Piece>());
		return true;
	}

	/**
	 * Calls the calculation of the influence on the island where mother nature is present
	 */
	protected void calculateInfluence() {
		Island currentIsland = null;
		currentIsland = game.getIslandHandler().getCurrentIsland();
		currentIsland.calculateInfluence(game.getTeacherHandler(), true, null,null);
		game.getIslandHandler().mergeIslands();
	}

	/**
	 * Calls the teacher control method
	 */
	protected void controlTeacher() {
		game.getTeacherHandler().calculateTeacher(game.getPlayerHandler().getPlayers(), false);
	}

	/**
	 * Returns the current player
	 * @return the current player
	 */
	protected Player getCurrentPlayer() {
		return game.getPlayerHandler().getCurrentPlayer();
	}

}
