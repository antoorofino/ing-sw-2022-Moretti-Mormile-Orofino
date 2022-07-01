package it.polimi.ingsw.server.rules.characters;

import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.server.rules.ExpertRules;

/**
 * Changes the rules of the game when card number 6 is activated
 */
public class SixthCardRules extends ExpertRules {

	/**
	 * Constructor: Creates the game rules
	 * @param game obj that contains the game status
	 */
	public SixthCardRules(GameModel game) {
		super(game);
	}

	@Override
	protected boolean islandHaveNoEntry(Island island){
		return !island.calculateInfluence(game.getTeacherHandler(), false, null,null);
	}
}
