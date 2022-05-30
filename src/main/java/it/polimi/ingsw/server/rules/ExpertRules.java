package it.polimi.ingsw.server.rules;

import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.util.Action;
import it.polimi.ingsw.util.ActionType;
import it.polimi.ingsw.util.GameMode;
import it.polimi.ingsw.util.RoundActions;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.util.exception.SpecificCharacterNotFoundException;
import it.polimi.ingsw.util.exception.SpecificIslandNotFoundException;

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
		if (!roundActions.hasChooseCharacter()) {
			if (!roundActions.hasChooseCloud())
				nextPossibleActions.add(new Action(ActionType.CHOOSE_CHARACTER));
		}else if (!roundActions.hasActivatedCharacter()) {
			nextPossibleActions = askToActivateCharacter(nextPossibleActions);
		}
		return nextPossibleActions;
	}

	@Override
	public boolean doAction(Action action) {
		if (action.getActionType().getMode().equals(GameMode.BASIC)) // basic mode action
			return super.doAction(action);
		if (action.getActionType() == ActionType.CHOOSE_CHARACTER)
			return chosenCharacter(action);
		else
			return activateCharacter(action);
	}

	@Override
	public boolean doMoveDiningRoom(Action action){
		if(!super.doMoveDiningRoom(action))
			return false;
		if((getCurrentPlayer().getPlayerBoard().getNumOfStudentsRoom(action.getPrincipalPiece()) % 3 == 0)&&(game.coinsAreEnough())){
			game.getCoin();
			getCurrentPlayer().addCoin();
		}
		return true;
	}

	/**
	 * Check if player can use the chosen caracter
	 * @param action
	 * @return
	 */
	protected boolean chosenCharacter(Action action) {
		Character character = null;
		try {
			character = game.getCharacterFromID(action.getInteger());
			if(!getCurrentPlayer().coinsAreEnough(character.getCost()))
				return false;
			switch(character.getID()){
				case 4:
				case 6:
				case 8:
				case 9:
					if(getCurrentPlayer().getRoundActions().hasMovedMother())
						return false;
					break;
				case 5:
					if(character.getIslandFlag() == 0)
						return false;
					break;
				case 7:
					if(getCurrentPlayer().getPlayerBoard().getStudentsEntrance().isEmpty())
						return false;
					break;
				case 10:
					if(getCurrentPlayer().getPlayerBoard().getStudentsEntrance().isEmpty())
						return false;
					if(getCurrentPlayer().getPlayerBoard().getStudentsRoom().isEmpty())
						return false;
					break;
				default:
					break;
			}
		} catch (SpecificCharacterNotFoundException e) {
			return false;
		}
		getCurrentPlayer().setActiveCharacter(character);
		getCurrentPlayer().removeCoin(character.getCost());
		character.increaseCost();
		return true;
	}

	// if card needs an activation by player the following methods should be overrided
	protected RoundActions askToActivateCharacter(RoundActions previousAction) {
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
			if (islandHaveNoEntry(currentIsland))
				restoreNoEntry();
			game.getIslandHandler().mergeIsland();
		} catch (SpecificIslandNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	protected boolean islandHaveNoEntry(Island island){
		return !island.calculateInfluence(game.getTeacherHandler(), true, null, null);
	}

	protected void restoreNoEntry(){
		try {
			game.getCharacterFromID(5).addIslandFlag();
		} catch (SpecificCharacterNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}
}

