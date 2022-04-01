package it.polimi.ingsw.Controller.Rules;


import it.polimi.ingsw.Controller.Action;
import it.polimi.ingsw.Controller.ActionType;
import it.polimi.ingsw.Controller.RoundActions;
import it.polimi.ingsw.Exception.InvalidInput;
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
		// TODO insert try/catch for exception
		currentplayer.getPlayerBoard().addStudentToRoom(action.getPrincipalPiece());
	}

	public void doMoveIsland(Action action, GameModel game) {
		// TODO insert try/catch for exception
		currentplayer.getPlayerBoard().removeFromEntrance(action.getPrincipalPiece());
		game.getIslandHandler().getIslandByID(action.getID()).addStudent(action.getPrincipalPiece());
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
		Cloud cloud = game.getCloudByID(action.getID());
		currentplayer.getPlayerBoard().addToEntrance(cloud.getStudents());
	}


	protected void calculateInfluence(GameModel game) {
		int currentMother = game.getIslandHandler().getMotherNature();
		Island currentIsland = game.getIslandHandler().getIslandByID(currentMother);
		currentIsland.calculateInfluence(game.getTeacherHandler(), true, null);
	}

	protected void controlTeacher(GameModel game) {
		game.getTeacherHandler().calculateTeacher(game.getPlayerHandler().getPlayers(), false);
	}

	protected void getCurrentPlayer(GameModel game) {
		currentplayer = game.getPlayerHandler().getCurrentPlayer();
	}

}
