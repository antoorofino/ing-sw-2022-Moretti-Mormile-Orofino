package it.polimi.ingsw.server.rules;


import it.polimi.ingsw.util.Action;
import it.polimi.ingsw.util.ActionType;
import it.polimi.ingsw.util.RoundActions;
import it.polimi.ingsw.util.exception.*;
import it.polimi.ingsw.model.*;

public class Rules {

	protected Player currentPlayer;

	//TODO: add constructor with GameModel as parameter
	/**
	 * Calculate the next possible action of the current player
	 *
	 * @param game
	 * @return ArrayList of Action that current player can do
	 */
	public RoundActions nextPossibleActions(GameModel game) {
		getCurrentPlayer(game);
		RoundActions roundActions = currentPlayer.getRoundActions();
		RoundActions nextPossibleActions = new RoundActions();

		if (roundActions.hasMovedStudents() < 3) {
			controlTeacher(game);
			nextPossibleActions.add(new Action(ActionType.MOVE_STUDENT_TO_DININGROOM));
			nextPossibleActions.add(new Action(ActionType.MOVE_STUDENT_TO_ISLAND));
		} else
			nextPossibleActions.add(new Action(ActionType.MOVE_MOTHER_NATURE));

		if (roundActions.hasMovedMother())
			calculateInfluence(game);
			nextPossibleActions.add(new Action(ActionType.CHOOSE_CLOUD));

		if (roundActions.hasChooseCloud())
			nextPossibleActions.add(new Action(ActionType.END));

		return nextPossibleActions;
	}

	public void doAction(Action action, GameModel game) throws InvalidInput {
		getCurrentPlayer(game);
		switch (action.getActionType()) {
			case MOVE_STUDENT_TO_DININGROOM:
				doMoveDiningRoom(action, game);
			case MOVE_STUDENT_TO_ISLAND:
				doMoveIsland(action, game);
			case MOVE_MOTHER_NATURE:
				doMoveMother(action, game);
			case CHOOSE_CLOUD:
				doChooseCloud(action, game);
		}
		currentPlayer.registerAction(action);
	}

	public void doMoveDiningRoom(Action action, GameModel game) {
		try {
			currentPlayer.getPlayerBoard().addStudentToRoom(action.getPrincipalPiece());
		} catch (SpecificStudentNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void doMoveIsland(Action action, GameModel game) {
		try {
			currentPlayer.getPlayerBoard().removeFromEntrance(action.getPrincipalPiece());
		} catch (SpecificStudentNotFoundException e) {
			e.printStackTrace();
		}

		try {
			game.getIslandHandler().getIslandByID(action.getID()).addStudent(action.getPrincipalPiece());
		} catch (SpecificIslandNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void doMoveMother(Action action, GameModel game) throws InvalidInput {
		if (!CanMooveMother(action, game)) throw new InvalidInput("Movimenti madre natura non consentiti");
		game.getIslandHandler().moveMotherNature(action.getID());
	}

	protected boolean CanMooveMother(Action action, GameModel game) {
		if (currentPlayer.getLastCardUsed().getMovements() > action.getID())
			return false;
		else
			return true;
	}


	public void doChooseCloud(Action action, GameModel game) {
		Cloud cloud = null;
		try {
			cloud = game.getCloudByID(action.getID());
		} catch (SpecificCloudNotFoundException e) {
			e.printStackTrace();
		}
		currentPlayer.getPlayerBoard().addToEntrance(cloud.getStudents());
	}


	protected void calculateInfluence(GameModel game) {
		int currentMother = game.getIslandHandler().getMotherNature();
		Island currentIsland = null;
		try {
			currentIsland = game.getIslandHandler().getIslandByID(currentMother);
		} catch (SpecificIslandNotFoundException e) {
			e.printStackTrace();
		}
		currentIsland.calculateInfluence(game.getTeacherHandler(), true, null);
	}

	protected void controlTeacher(GameModel game) {
		game.getTeacherHandler().calculateTeacher(game.getPlayerHandler().getPlayers(), false);
	}

	protected void getCurrentPlayer(GameModel game) {
		currentPlayer = game.getPlayerHandler().getCurrentPlayer();
	}

}
