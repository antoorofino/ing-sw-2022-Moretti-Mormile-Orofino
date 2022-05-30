package it.polimi.ingsw.server.rules.characters;

import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.server.rules.ExpertRules;
import it.polimi.ingsw.util.exception.SpecificCharacterNotFoundException;
import it.polimi.ingsw.util.exception.SpecificIslandNotFoundException;

public class SixthCardRules extends ExpertRules {
	/**
	 * Create the game rules
	 *
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
