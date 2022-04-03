package it.polimi.ingsw.Controller.Rules;


import it.polimi.ingsw.Controller.Action;
import it.polimi.ingsw.Controller.ActionType;
import it.polimi.ingsw.Controller.RoundActions;
import it.polimi.ingsw.Exception.CloudException;
import it.polimi.ingsw.Exception.InvalidInput;
import it.polimi.ingsw.Exception.StudentException;
import it.polimi.ingsw.Model.*;

public class Rules {

	protected Player currentplayer;

	/**
	 * Calculate the next possible action of the current player
	 *
	 * @param game
	 * @return ArrayList of Action that current player can do
	 */
	public RoundActions nextPossibleActions(GameModel game) {
		getCurrentPlayer(game);
		RoundActions roundActions = currentplayer.getRoundActions();
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
		currentplayer.registerAction(action);
	}

	public void doMoveDiningRoom(Action action, GameModel game) {
		// TODO check try/catch for exception
		try {
			currentplayer.getPlayerBoard().addStudentToRoom(action.getPrincipalPiece());
		} catch (StudentException e) {
			e.printStackTrace();
		}
	}

	public void doMoveIsland(Action action, GameModel game) {
		// TODO check try/catch for exception
		try {
			currentplayer.getPlayerBoard().removeFromEntrance(action.getPrincipalPiece());
		} catch (StudentException e) {
			e.printStackTrace();
		}
		// TODO check try/catch
		try {
			game.getIslandHandler().getIslandByID(action.getID()).addStudent(action.getPrincipalPiece());
		} catch (CloudException e) {
			e.printStackTrace();
		}
	}

	public void doMoveMother(Action action, GameModel game) throws InvalidInput {
		if (!CanMooveMother(action, game)) throw new InvalidInput("Movimenti madre natura non consentiti");
		game.getIslandHandler().setMotherNature(action.getID());
	}

	protected boolean CanMooveMother(Action action, GameModel game) {
		if (currentplayer.getLastCardUsed().getMovements() > action.getID())
			return false;
		else
			return true;
	}


	public void doChooseCloud(Action action, GameModel game) {
		Cloud cloud = null;
		try {
			cloud = game.getCloudByID(action.getID());
		} catch (CloudException e) {
			e.printStackTrace();
		}
		currentplayer.getPlayerBoard().addToEntrance(cloud.getStudents());
	}


	protected void calculateInfluence(GameModel game) {
		int currentMother = game.getIslandHandler().getMotherNature();
		// TODO check try/catch
		Island currentIsland = null;
		try {
			currentIsland = game.getIslandHandler().getIslandByID(currentMother);
		} catch (CloudException e) {
			e.printStackTrace();
		}
		currentIsland.calculateInfluence(game.getTeacherHandler(), true, null);
	}

	protected void controlTeacher(GameModel game) {
		game.getTeacherHandler().calculateTeacher(game.getPlayerHandler().getPlayers(), false);
	}

	protected void getCurrentPlayer(GameModel game) {
		currentplayer = game.getPlayerHandler().getCurrentPlayer();
	}

}
