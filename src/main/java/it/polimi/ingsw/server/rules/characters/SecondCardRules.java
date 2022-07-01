package it.polimi.ingsw.server.rules.characters;

import it.polimi.ingsw.server.rules.ExpertRules;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.util.Action;
import it.polimi.ingsw.util.ActionType;
import it.polimi.ingsw.util.RoundActions;

/**
 * Change the rules of the game when card number 2 is activated
 */
public class SecondCardRules extends ExpertRules {

	/**
	 * Create the game rules
	 * @param game obj that contains the game status
	 */
	public SecondCardRules(GameModel game) {
		super(game);
	}

	@Override
	protected RoundActions askToActivateCharacter(RoundActions previousAction) {
		controlTeacher();
		getCurrentPlayer().registerAction(new Action(ActionType.ACTIVATED_CHARACTER));
		return previousAction;
	}
	@Override
	protected void controlTeacher() {
		game.getTeacherHandler().calculateTeacher(game.getPlayerHandler().getPlayers(), true);
	}
}
