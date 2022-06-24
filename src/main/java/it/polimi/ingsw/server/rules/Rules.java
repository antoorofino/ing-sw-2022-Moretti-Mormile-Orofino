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

public class Rules implements Serializable {

	protected GameModel game;

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
	 *
	 * @param action
	 * @return
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
	 *
	 * @param action
	 * @return
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
	 *
	 * @param action
	 * @return
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
	 *
	 * @param action
	 * @return
	 */
	protected boolean doMoveMother(Action action){
		if (getCurrentPlayer().getLastCardUsed().getMovements() >= action.getInteger()){
			game.getIslandHandler().moveMotherNature(action.getInteger());
			calculateInfluence();
			return true;
		}
		return false;
	}

	/**
	 *
	 * @param action
	 * @return
	 */
	protected boolean doChooseCloud(Action action) {
		Cloud cloud = null;
		try {
			cloud = game.getCloudByID(action.getInteger());
		} catch (SpecificCloudNotFoundException e) {
			System.out.println(e.getMessage());
			return false;
		}
		if(cloud.getStudents().isEmpty())
			return false;
		getCurrentPlayer().getPlayerBoard().addToEntrance(cloud.getStudents());
		cloud.addStudents(new ArrayList<Piece>());
		return true;
	}

	/**
	 *
	 */
	protected void calculateInfluence() {
		Island currentIsland = null;
		currentIsland = game.getIslandHandler().getCurrentIsland();
		currentIsland.calculateInfluence(game.getTeacherHandler(), true, null,null);
		game.getIslandHandler().mergeIslands();
	}

	/**
	 *
	 */
	protected void controlTeacher() {
		game.getTeacherHandler().calculateTeacher(game.getPlayerHandler().getPlayers(), false);
	}

	/**
	 *
	 * @return
	 */
	protected Player getCurrentPlayer() {
		return game.getPlayerHandler().getCurrentPlayer();
	}

}
