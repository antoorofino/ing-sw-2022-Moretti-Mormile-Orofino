package it.polimi.ingsw.server.rules.characters;

import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.server.rules.ExpertRules;
import it.polimi.ingsw.util.Action;
import it.polimi.ingsw.util.ActionType;
import it.polimi.ingsw.util.RoundActions;
import it.polimi.ingsw.util.exception.SpecificStudentNotFoundException;
import it.polimi.ingsw.util.exception.StudentNotPresentException;

import java.util.ArrayList;
import java.util.Arrays;

public class SeventhCardRules extends ExpertRules {

	protected int movedPieces;
	/**
	 * Create the game rules
	 *
	 * @param game obj that contains the game status
	 */
	public SeventhCardRules(GameModel game) {
		super(game);
		movedPieces = 0;
	}

	@Override
	protected RoundActions askToActivateCharacter(RoundActions previousAction) {
		RoundActions action_character = new RoundActions();
		action_character.add(new Action(ActionType.STUDENT_FROM_CARD_TO_ENTRANCE));
		return action_character;
	}

	@Override
	protected boolean activateCharacter(Action action){
		if(action.getActionType()!= ActionType.STUDENT_FROM_CARD_TO_ENTRANCE)
			return false;
		if(action.getInteger()==-1) { // user doesn't want to move again
			getCurrentPlayer().registerAction(new Action(ActionType.ACTIVATED_CHARACTER));
			return true;
		}
		try {
			getCurrentPlayer().getActiveCharacter().delStudent(action.getPrincipalPiece());
			getCurrentPlayer().getActiveCharacter().addStudents(new ArrayList<>(Arrays.asList(action.getOptionalPiece())));
			getCurrentPlayer().getPlayerBoard().addToEntrance(new ArrayList<>(Arrays.asList(action.getPrincipalPiece())));
			getCurrentPlayer().getPlayerBoard().removeFromEntrance(action.getOptionalPiece());
		} catch (SpecificStudentNotFoundException | StudentNotPresentException e) {
			return false;
		}
		movedPieces++;
		if(movedPieces==3)
			getCurrentPlayer().registerAction(new Action(ActionType.ACTIVATED_CHARACTER));
		return true;
	}

}
