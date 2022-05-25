package it.polimi.ingsw.server.rules;

import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.util.Action;
import it.polimi.ingsw.util.ActionType;
import it.polimi.ingsw.util.GameMode;
import it.polimi.ingsw.util.RoundActions;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.util.exception.SpecificCharacterNotFoundException;
import it.polimi.ingsw.util.exception.SpecificIslandNotFoundException;

import java.util.Arrays;

public class ExpertRules extends Rules {
	/**
	 * Create the game rules
	 *
	 * @param game obj that contains the game status
	 */
	public ExpertRules(GameModel game) {
		super(game);
	}

	@Override
	public RoundActions nextPossibleActions() {
		RoundActions roundActions = getCurrentPlayer().getRoundActions();
		RoundActions nextPossibleActions = super.nextPossibleActions(); // basic action
		if (!roundActions.hasChooseCharacter())
			nextPossibleActions.add(new Action(ActionType.CHOOSE_CHARACTER));
		else if (!roundActions.hasActivatedCharacter()) {
			nextPossibleActions = ActionsCharacter(nextPossibleActions);
		}
		return nextPossibleActions;
	}

	@Override
	public boolean doAction(Action action) {
		if (action.getActionType().getMode().equals(GameMode.BASIC)) // basic mode action
			return super.doAction(action);
		if (action.getActionType() == ActionType.CHOOSE_CHARACTER)
			return useCharacter(action);
		return activateCharacter(action);
	}

	protected boolean useCharacter(Action action) {
		Character character = null;
		try {
			character = game.getCharacterFromID(action.getID());
			if (((character.getID() == 5) && (character.getIslandFlag() == 0)) || (!getCurrentPlayer().coinsAreEnough(character.getCost())))
				return false; // no entry tile on card
		} catch (SpecificCharacterNotFoundException e) {
			return false;
		}
		getCurrentPlayer().setActiveCharacter(character);
		getCurrentPlayer().removeCoin(character.getCost());
		character.increaseCost();
		return true;
	}

	// if card needs an activation by player the following methods should be overrided
	protected RoundActions ActionsCharacter(RoundActions previousAction) {
		getCurrentPlayer().registerAction(new Action(ActionType.ACTIVATED_CHARACTER));
		return previousAction;
	}

	protected boolean activateCharacter(Action action) {
		return true;
	}

	@Override
	protected void calculateInfluence() { // entry tiles should be restore to card 5
		int currentMother = game.getIslandHandler().getMotherNature();
		Island currentIsland = null;
		try {
			currentIsland = game.getIslandHandler().getIslandByID(currentMother);
			if (!currentIsland.calculateInfluence(game.getTeacherHandler(), true, null, null)) {
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

