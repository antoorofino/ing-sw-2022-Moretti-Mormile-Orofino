package it.polimi.ingsw.server.rules;


import it.polimi.ingsw.util.Action;
import it.polimi.ingsw.util.ActionType;
import it.polimi.ingsw.util.RoundActions;
import it.polimi.ingsw.util.exception.*;
import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.Arrays;

public class Rules {

	protected GameModel game;

	public Rules(GameModel game){
		this.game = game;
	}

	/**
	 * Calculate the next possible action of the current player
	 *
	 * @return ArrayList of Action that current player can do
	 */
	public RoundActions nextPossibleActions() {
		RoundActions roundActions = getCurrentPlayer().getRoundActions();
		RoundActions nextPossibleActions = new RoundActions();

		if (roundActions.hasMovedStudents() < 3) {
			nextPossibleActions.add(new Action(ActionType.MOVE_STUDENT_TO_DININGROOM));
			nextPossibleActions.add(new Action(ActionType.MOVE_STUDENT_TO_ISLAND));
		} else {
			if (!roundActions.hasMovedMother())
				nextPossibleActions.add(new Action(ActionType.MOVE_MOTHER_NATURE));
			else {
				// FIXME: scommenta qua
				//nextPossibleActions.add(new Action(ActionType.CHOOSE_CLOUD));

			}
		}
		//if (roundActions.hasChooseCloud())
		//	nextPossibleActions.add(new Action(ActionType.END));

		return nextPossibleActions;
	}

	public boolean doAction(Action action){
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
				// FIXME: metti dopo choose cloud
				getCurrentPlayer().registerAction(new Action(ActionType.END));
				break;
			case CHOOSE_CLOUD:
				returnValue = doChooseCloud(action);
				break;
		}
		if(returnValue)
			getCurrentPlayer().registerAction(action);
		return returnValue;
	}

	protected boolean doMoveDiningRoom(Action action) {
		try {
			getCurrentPlayer().getPlayerBoard().addStudentToRoom(action.getPrincipalPiece());

		} catch (SpecificStudentNotFoundException e) {
			System.out.println(e.getMessage());
			return false;
		}

		try {
			getCurrentPlayer().getPlayerBoard().removeFromEntrance(action.getPrincipalPiece());
		} catch (SpecificStudentNotFoundException e) {
			getCurrentPlayer().getPlayerBoard().addToEntrance(new ArrayList<>(Arrays.asList(action.getPrincipalPiece())));
			return false;
		}
		controlTeacher();
		return true;
	}

	protected boolean doMoveIsland(Action action) {

		try {
			getCurrentPlayer().getPlayerBoard().removeFromEntrance(action.getPrincipalPiece());
		} catch (SpecificStudentNotFoundException e) {
			System.out.println(e.getMessage());
			return false;
		}

		try {
			game.getIslandHandler().getIslandByID(action.getID()).addStudent(action.getPrincipalPiece());
		} catch (SpecificIslandNotFoundException e) {
			// roolback function
			System.out.println(e.getMessage());
			getCurrentPlayer().getPlayerBoard().addToEntrance(new ArrayList<>(Arrays.asList(action.getPrincipalPiece())));
			return false;
		}
		//System.out.println("Muovo studente su isola");
		return true;
	}

	protected boolean doMoveMother(Action action){
		if (getCurrentPlayer().getLastCardUsed().getMovements() >= action.getID()){
			game.getIslandHandler().moveMotherNature(action.getID());
		//	System.out.println("Muovo madre natura");
			calculateInfluence();
			return true;
		}
		return false;
	}


	protected boolean doChooseCloud(Action action) {
		Cloud cloud = null;
		try {
			cloud = game.getCloudByID(action.getID());
		} catch (SpecificCloudNotFoundException e) {
			System.out.println(e.getMessage());
			return false;
		}
		getCurrentPlayer().getPlayerBoard().addToEntrance(cloud.getStudents());
	//	System.out.println("Assegno isola a board studente");
		return true;
	}


	protected void calculateInfluence() {

		int currentMother = game.getIslandHandler().getMotherNature();
		Island currentIsland = null;
		try {
			currentIsland = game.getIslandHandler().getIslandByID(currentMother);
		} catch (SpecificIslandNotFoundException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		currentIsland.calculateInfluence(game.getTeacherHandler(), true, null,null);
		//System.out.println("Calcolo influenza");
	}

	protected void controlTeacher() {
		//System.out.println("Calcolo influenza");
		game.getTeacherHandler().calculateTeacher(game.getPlayerHandler().getPlayers(), false);
	}

	protected Player getCurrentPlayer() {
		return game.getPlayerHandler().getCurrentPlayer();
	}

}
