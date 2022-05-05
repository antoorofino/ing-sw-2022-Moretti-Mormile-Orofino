package it.polimi.ingsw.server.rules.characters;

import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.Piece;
import it.polimi.ingsw.server.rules.ExpertRules;
import it.polimi.ingsw.util.Action;
import it.polimi.ingsw.util.ActionType;
import it.polimi.ingsw.util.RoundActions;
import it.polimi.ingsw.util.exception.SpecificIslandNotFoundException;;

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
	protected RoundActions ActionsCharacter(RoundActions previousAction) {
		RoundActions action_character = new RoundActions();
		action_character.add(new Action(ActionType.CHOOSE_COLOR));
		return action_character;
	}

	@Override
	protected boolean activateCharacter(Action action){
		if(action.getActionType()!=ActionType.CHOOSE_COLOR)
			return false;
		invalidColor = action.getPrincipalPiece();
		getCurrentPlayer().registerAction(new Action(ActionType.ACTIVATED_CHARACTER));
		return true;
	}

	@Override
	protected void calculateInfluence() {
		int currentMother = game.getIslandHandler().getMotherNature();
		Island currentIsland = null;
		try {
			currentIsland = game.getIslandHandler().getIslandByID(currentMother);
		} catch (SpecificIslandNotFoundException e) {
			e.printStackTrace();
		}
		currentIsland.calculateInfluence(game.getTeacherHandler(), false, invalidColor,null);
	}

}
