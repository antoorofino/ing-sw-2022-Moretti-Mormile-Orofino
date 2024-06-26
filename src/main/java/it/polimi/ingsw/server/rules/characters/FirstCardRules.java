package it.polimi.ingsw.server.rules.characters;

import it.polimi.ingsw.server.rules.ExpertRules;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.util.Action;
import it.polimi.ingsw.util.ActionType;
import it.polimi.ingsw.util.RoundActions;
import it.polimi.ingsw.util.exception.SpecificIslandNotFoundException;
import it.polimi.ingsw.util.exception.SpecificStudentNotFoundException;
import it.polimi.ingsw.util.exception.StudentNotPresentException;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Changes the rules of the game when card number 1 is activated
 */
public class FirstCardRules extends ExpertRules
{
	/**
	 * Constructor: Creates the game rules
	 * @param game obj that contains the game status
	 */
	public FirstCardRules(GameModel game) {
		super(game);
	}

	@Override
	protected RoundActions askToActivateCharacter(RoundActions previousAction) {
		RoundActions action_character = new RoundActions();
		action_character.add(new Action(ActionType.STUDENT_FROM_CARD_TO_ISLAND));
		return action_character;
	}

	@Override
	protected boolean activateCharacter(Action action){
		try {
			getCurrentPlayer().getActiveCharacter().delStudent(action.getPrincipalPiece());
		} catch (SpecificStudentNotFoundException|StudentNotPresentException e) {
			return false;
		}

		try {
			game.getIslandHandler().getIslandByID(action.getInteger()).addStudent(action.getPrincipalPiece());
		} catch (SpecificIslandNotFoundException e) {
			// roolback function
			getCurrentPlayer().getActiveCharacter().addStudents(new ArrayList<>(Arrays.asList(action.getPrincipalPiece())));
			return false;
		}
		// refill the card
		getCurrentPlayer().getActiveCharacter().addStudents(game.getStudentsBag().popStudents(1));
		getCurrentPlayer().registerAction(new Action(ActionType.ACTIVATED_CHARACTER));
		return true;
	}
}
