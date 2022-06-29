package it.polimi.ingsw.server.rules;

import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.util.Action;
import it.polimi.ingsw.util.ActionType;
import it.polimi.ingsw.util.GameMode;
import it.polimi.ingsw.util.RoundActions;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.util.exception.SpecificCharacterNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Contains the rules of the game when choosing expert mode
 */
public class ExpertRules extends Rules {

	/**
	 * Create the game rules
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
			nextPossibleActions.add(new Action(ActionType.INFO_CHARACTER));
			if (!roundActions.hasChooseCloud())
				nextPossibleActions.add(new Action(ActionType.CHOOSE_CHARACTER));
		}else if (!roundActions.hasActivatedCharacter()) {
			nextPossibleActions = askToActivateCharacter(nextPossibleActions);
		}
		return nextPossibleActions;
	}

	@Override
	public boolean doAction(Action action) {
		List<ActionType> possibleAction = nextPossibleActions().getActionsList()
				.stream().map(Action::getActionType).collect(Collectors.toList());
		if(!possibleAction.contains(action.getActionType()))
			return false;
		if (action.getActionType().getMode().equals(GameMode.BASIC)) // basic mode action
			return super.doAction(action);
		if (action.getActionType() == ActionType.CHOOSE_CHARACTER)
			return chosenCharacter(action);
		else
			return activateCharacter(action);
	}

	@Override
	protected boolean doMoveDiningRoom(Action action){
		if(!super.doMoveDiningRoom(action))
			return false;
		if((getCurrentPlayer().getPlayerBoard().getNumOfStudentsRoom(action.getPrincipalPiece()) % 3 == 0)&&(game.coinsAreEnough())){
			game.getCoin();
			getCurrentPlayer().addCoin();
		}
		return true;
	}

	/**
	 * Check if player can use the chosen character
	 * @param action contains the id of chosen character
	 * @return true if is valid action
	 */
	protected boolean chosenCharacter(Action action) {
		Character character;
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
					// check students in entrance
					if(getCurrentPlayer().getPlayerBoard().getStudentsEntrance().isEmpty())
						return false;
					// check students in dining room
					boolean check = false;
					Map<Piece,Integer> studentsRoom = getCurrentPlayer().getPlayerBoard().getStudentsRoom();
					for (Integer value: studentsRoom.values()) {
						if (value != 0) {
							check  = true;
						}
					}
					if(!check)
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

	@Override
	protected void calculateInfluence() { // entry tiles should be restore to card 5
		Island currentIsland;
		currentIsland = game.getIslandHandler().getCurrentIsland();
		if (islandHaveNoEntry(currentIsland))
			restoreNoEntry();
		game.getIslandHandler().mergeIslands();
	}

	/**
	 * Returns true if the selected island contains no entry tiles
	 * @param island the selected island
	 * @return true if the selected island contains no entry tile
	 */
	protected boolean islandHaveNoEntry(Island island){
		return !island.calculateInfluence(game.getTeacherHandler(), true, null, null);
	}

	/**
	 * Put the no entry tile back on card number 5
	 */
	protected void restoreNoEntry(){
		try {
			game.getCharacterFromID(5).addIslandFlag();
		} catch (SpecificCharacterNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Asks the player to make further moves to use the chosen character card
	 * @param previousAction the possible actions of the player
	 * @return the new possible actions of the player
	 */
	protected RoundActions askToActivateCharacter(RoundActions previousAction) {
		getCurrentPlayer().registerAction(new Action(ActionType.ACTIVATED_CHARACTER));
		return previousAction;
	}

	/**
	 * Called by the moves that activate character cards
	 * @param action the player's action
	 * @return true if the action is valid
	 */
	protected boolean activateCharacter(Action action) {
		return false;
	}
}

