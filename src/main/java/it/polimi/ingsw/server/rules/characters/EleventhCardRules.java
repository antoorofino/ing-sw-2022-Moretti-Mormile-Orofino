package it.polimi.ingsw.server.rules.characters;

import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.server.rules.ExpertRules;
import it.polimi.ingsw.util.Action;
import it.polimi.ingsw.util.ActionType;
import it.polimi.ingsw.util.RoundActions;
import it.polimi.ingsw.util.exception.SpecificIslandNotFoundException;
import it.polimi.ingsw.util.exception.SpecificStudentNotFoundException;
import it.polimi.ingsw.util.exception.StudentNotPresentException;

import java.util.ArrayList;
import java.util.Arrays;

public class EleventhCardRules extends ExpertRules {
	/**
	 * Create the game rules
	 *
	 * @param game obj that contains the game status
	 */
	public EleventhCardRules(GameModel game) {
		super(game);
	}


	@Override
	protected RoundActions ActionsCharacter(RoundActions previousAction) {
		RoundActions action_character = new RoundActions();
		action_character.add(new Action(ActionType.STUDENT_FROM_CARD_TO_DINING));
		return action_character;
	}

	@Override
	protected boolean activateCharacter(Action action){
		if(action.getActionType()!=ActionType.STUDENT_FROM_CARD_TO_DINING)
			return false;
		try {
			getCurrentPlayer().getActiveCharacter().delStudent(action.getPrincipalPiece());
		} catch (SpecificStudentNotFoundException | StudentNotPresentException e) {
			return false;
		}

		try {
			getCurrentPlayer().getPlayerBoard().addStudentToRoom(action.getPrincipalPiece());
		} catch (SpecificStudentNotFoundException e) {
			// rollback
			getCurrentPlayer().getActiveCharacter().addStudents(new ArrayList<>(Arrays.asList(action.getPrincipalPiece())));
			return false;
		}
		// refill the card
		getCurrentPlayer().getActiveCharacter().addStudents(game.getStudentsBag().popStudents(1));
		getCurrentPlayer().registerAction(new Action(ActionType.ACTIVATED_CHARACTER));
		return true;
	}
}