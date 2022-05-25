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
	protected void calculateInfluence() {
		int currentMother = game.getIslandHandler().getMotherNature();
		Island currentIsland = null;
		try {
			currentIsland = game.getIslandHandler().getIslandByID(currentMother);
			if (!currentIsland.calculateInfluence(game.getTeacherHandler(), false, null,null)) {
				try {
					game.getCharacterFromID(5).addIslandFlag();
				} catch (SpecificCharacterNotFoundException e) {
					System.out.println(e.getMessage());
				}
			}
			game.getIslandHandler().mergeIsland();
		} catch (SpecificIslandNotFoundException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
