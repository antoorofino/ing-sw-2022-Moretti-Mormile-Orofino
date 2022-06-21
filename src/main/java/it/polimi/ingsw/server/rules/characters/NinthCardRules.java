package it.polimi.ingsw.server.rules.characters;

import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.Piece;
import it.polimi.ingsw.server.rules.ExpertRules;
import it.polimi.ingsw.util.Action;
import it.polimi.ingsw.util.ActionType;
import it.polimi.ingsw.util.RoundActions;

public class NinthCardRules extends ExpertRules {
	protected Piece invalidColor;
	/**
	 * Create the game rules
	 *
	 * @param game obj that contains the game status
	 */

	public NinthCardRules(GameModel game) {
		super(game);
	}

	@Override
	protected RoundActions askToActivateCharacter(RoundActions previousAction) {
		RoundActions action_character = new RoundActions();
		action_character.add(new Action(ActionType.COLOR_NO_INFLUENCE));
		return action_character;
	}

	@Override
	protected boolean activateCharacter(Action action){
		invalidColor = action.getPrincipalPiece();
		if(invalidColor == null)
			return false;
		getCurrentPlayer().registerAction(new Action(ActionType.ACTIVATED_CHARACTER));
		return true;
	}

	@Override
	protected boolean islandHaveNoEntry(Island island){
		return !island.calculateInfluence(game.getTeacherHandler(), true, invalidColor,null);
	}
}
