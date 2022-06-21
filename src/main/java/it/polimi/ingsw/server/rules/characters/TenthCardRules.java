package it.polimi.ingsw.server.rules.characters;

import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.server.rules.ExpertRules;
import it.polimi.ingsw.util.Action;
import it.polimi.ingsw.util.ActionType;
import it.polimi.ingsw.util.RoundActions;
import it.polimi.ingsw.util.exception.SpecificStudentNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;

public class TenthCardRules extends ExpertRules {

	protected int movedPieces;
	/**
	 * Create the game rules
	 *
	 * @param game obj that contains the game status
	 */
	public TenthCardRules(GameModel game) {
		super(game);
		movedPieces = 0;
	}

	@Override
	protected RoundActions askToActivateCharacter(RoundActions previousAction) {
		RoundActions action_character = new RoundActions();
		action_character.add(new Action(ActionType.STUDENT_FROM_ENTRANCE_TO_DINING));
		return action_character;
	}

	@Override
	protected boolean activateCharacter(Action action){
		if(action.getInteger()==-1) { // user doesn't want to move again
			getCurrentPlayer().registerAction(new Action(ActionType.ACTIVATED_CHARACTER));
			return true;
		}

		try {
			getCurrentPlayer().getPlayerBoard().removeFromEntrance(action.getPrincipalPiece());
			getCurrentPlayer().getPlayerBoard().addToEntrance(new ArrayList<>(Arrays.asList(action.getOptionalPiece())));
			getCurrentPlayer().getPlayerBoard().addStudentToRoom(action.getPrincipalPiece());
			getCurrentPlayer().getPlayerBoard().removeFromRoom(action.getOptionalPiece());
		} catch (SpecificStudentNotFoundException e) {
			return false;
		}
		movedPieces++;
		if(movedPieces==2)
			getCurrentPlayer().registerAction(new Action(ActionType.ACTIVATED_CHARACTER));
		game.getTeacherHandler().calculateTeacher(game.getPlayerHandler().getPlayers(), false);
		return true;
	}
}
