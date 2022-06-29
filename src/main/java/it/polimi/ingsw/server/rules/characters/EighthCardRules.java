package it.polimi.ingsw.server.rules.characters;

import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.server.rules.ExpertRules;

/**
 * Change the rules of the game when card number 8 is activated
 */
public class EighthCardRules extends ExpertRules {

	/**
	 * Create the game rules
	 * @param game obj that contains the game status
	 */
	public EighthCardRules(GameModel game) {
		super(game);
	}

	@Override
	protected boolean islandHaveNoEntry(Island island){
		return !island.calculateInfluence(game.getTeacherHandler(), true, null,getCurrentPlayer());
	}
}
