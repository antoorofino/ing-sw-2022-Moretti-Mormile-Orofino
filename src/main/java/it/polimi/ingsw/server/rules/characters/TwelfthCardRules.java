package it.polimi.ingsw.server.rules.characters;

import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.server.rules.ExpertRules;
import it.polimi.ingsw.util.Action;
import it.polimi.ingsw.util.ActionType;
import it.polimi.ingsw.util.RoundActions;
import it.polimi.ingsw.util.exception.SpecificStudentNotFoundException;

public class TwelfthCardRules extends ExpertRules {
	/**
	 * Create the game rules
	 *
	 * @param game obj that contains the game status
	 */
	public TwelfthCardRules(GameModel game) {
		super(game);
	}

	@Override
	protected RoundActions askToActivateCharacter(RoundActions previousAction) {
		RoundActions action_character = new RoundActions();
		action_character.add(new Action(ActionType.STUDENT_FROM_DINING_TO_BAG));
		return action_character;
	}

	@Override
	protected boolean activateCharacter(Action action){
		if(action.getActionType()!=ActionType.STUDENT_FROM_DINING_TO_BAG)
			return false;
		for (Player p: game.getPlayerHandler().getPlayers()) {
			for(int i=0;i<3;i++){
				try {
					p.getPlayerBoard().removeFromRoom(action.getPrincipalPiece());
					game.getStudentsBag().addStudent(action.getPrincipalPiece(),1); // restore in bag
					controlTeacher();
				} catch (SpecificStudentNotFoundException e) {
					break;
				}
			}
		}
		getCurrentPlayer().registerAction(new Action(ActionType.ACTIVATED_CHARACTER));
		return true;
	}
}
