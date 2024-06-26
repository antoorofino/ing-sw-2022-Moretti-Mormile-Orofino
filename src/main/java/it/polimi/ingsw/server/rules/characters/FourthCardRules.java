package it.polimi.ingsw.server.rules.characters;

import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.server.rules.ExpertRules;
import it.polimi.ingsw.util.Action;

/**
 * Changes the rules of the game when card number 4 is activated
 */
public class FourthCardRules extends ExpertRules {

	/**
	 * Constructor: Creates the game rules
	 * @param game obj that contains the game status
	 */
	public FourthCardRules(GameModel game) {
		super(game);
	}

	@Override
	protected boolean doMoveMother(Action action){
		if (getCurrentPlayer().getLastCardUsed().getMovements() + 2 >= action.getInteger()){
			game.getIslandHandler().moveMotherNature(action.getInteger());
			calculateInfluence();
			return true;
		}
		return false;
	}
}
