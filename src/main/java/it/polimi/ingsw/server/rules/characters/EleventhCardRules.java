package it.polimi.ingsw.server.rules.characters;

import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.server.rules.ExpertRules;
import it.polimi.ingsw.util.Action;
import it.polimi.ingsw.util.ActionType;
import it.polimi.ingsw.util.RoundActions;
import it.polimi.ingsw.util.exception.SpecificStudentNotFoundException;
import it.polimi.ingsw.util.exception.StudentNotPresentException;

/**
 * Change the rules of the game when card number 11 is activated
 */
public class EleventhCardRules extends ExpertRules {

	/**
	 * Create the game rules
	 * @param game obj that contains the game status
	 */
	public EleventhCardRules(GameModel game) {
		super(game);
	}

	@Override
	protected RoundActions askToActivateCharacter(RoundActions previousAction) {
		RoundActions action_character = new RoundActions();
		action_character.add(new Action(ActionType.STUDENT_FROM_CARD_TO_DINING));
		return action_character;
	}

	@Override
	protected boolean activateCharacter(Action action){
		try {
			getCurrentPlayer().getActiveCharacter().delStudent(action.getPrincipalPiece());
		} catch (SpecificStudentNotFoundException | StudentNotPresentException e) {
			return false;
		}

		getCurrentPlayer().getPlayerBoard().addStudentToRoom(action.getPrincipalPiece());
		calculateInfluence();
		// refill the card
		getCurrentPlayer().getActiveCharacter().addStudents(game.getStudentsBag().popStudents(1));
		getCurrentPlayer().registerAction(new Action(ActionType.ACTIVATED_CHARACTER));
		return true;
	}
}
