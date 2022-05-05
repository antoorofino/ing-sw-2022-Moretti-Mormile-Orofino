package it.polimi.ingsw.server.rules.characters;

import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.server.rules.ExpertRules;
import it.polimi.ingsw.util.exception.SpecificIslandNotFoundException;

public class EighthCardRules extends ExpertRules {
	/**
	 * Create the game rules
	 *
	 * @param game obj that contains the game status
	 */
	public EighthCardRules(GameModel game) {
		super(game);
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

		currentIsland.calculateInfluence(game.getTeacherHandler(), false, null,getCurrentPlayer());
	}
}
