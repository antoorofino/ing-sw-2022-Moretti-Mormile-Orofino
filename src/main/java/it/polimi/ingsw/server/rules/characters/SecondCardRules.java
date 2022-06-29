package it.polimi.ingsw.server.rules.characters;

import it.polimi.ingsw.server.rules.ExpertRules;
import it.polimi.ingsw.model.GameModel;

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
	protected void controlTeacher() {
		game.getTeacherHandler().calculateTeacher(game.getPlayerHandler().getPlayers(), true);
	}
}
