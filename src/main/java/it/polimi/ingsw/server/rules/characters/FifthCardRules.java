package it.polimi.ingsw.server.rules.characters;

import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.server.rules.ExpertRules;
import it.polimi.ingsw.util.Action;
import it.polimi.ingsw.util.ActionType;
import it.polimi.ingsw.util.RoundActions;
import it.polimi.ingsw.util.exception.SpecificIslandNotFoundException;

public class FifthCardRules extends ExpertRules {
	/**
	 * Create the game rules
	 *
	 * @param game obj that contains the game status
	 */
	public FifthCardRules(GameModel game) {
		super(game);
	}

	@Override
	protected RoundActions ActionsCharacter(RoundActions previousAction) {
		RoundActions action_character = new RoundActions();
		action_character.add(new Action(ActionType.CHOOSE_ISLAND));
		return action_character;
	}
	@Override
	protected boolean activateCharacter(Action action){
		if(action.getActionType()!=ActionType.CHOOSE_ISLAND)
			return false;
		try {
			game.getIslandHandler().getIslandByID(action.getID()).addFlagNoInfluence();
			// FIXME: removeIslandFlag method does not exist
			getCurrentPlayer().getActiveCharacter().removeIslandFlag();
		} catch (SpecificIslandNotFoundException e) {
			return false;
		}
		getCurrentPlayer().registerAction(new Action(ActionType.ACTIVATED_CHARACTER));
		return true;
	}

}
