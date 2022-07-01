package it.polimi.ingsw.util;

import it.polimi.ingsw.util.Action;
import it.polimi.ingsw.util.ActionType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Stores information about action that player has done during his turn
 */
public class RoundActions  implements Serializable {
    private final ArrayList<Action> actionList;

    /**
     * Constructor: builds the list that contains all the actions done
     */
    public RoundActions() {
        actionList = new ArrayList<>();
    }

    /**
     * Gets all the actions already done
     * @return list of actions already done during this turn
     */
    public List<Action> getActionsList() {
        return new ArrayList<>(actionList);
    }

    /**
     * Gets number of student that player has moved
     * @return how many students the player has already moved
     */
    public int hasMovedStudents() {
        int counter = 0;
        for (Action action : actionList)
            if ((action.getActionType() == ActionType.MOVE_STUDENT_TO_ISLAND) || (action.getActionType() == ActionType.MOVE_STUDENT_TO_DININGROOM))
                counter++;
        return counter;
    }

    /**
     * Checks if player has already moved mother nature
     * @return true if player has already moved mother nature, otherwise false
     */
    public boolean hasMovedMother() {
        return containsAction(ActionType.MOVE_MOTHER_NATURE);
    }

    /**
     * Checks if player has chosen the cloud from where pick students
     * @return true if player has chosen the cloud, otherwise false
     */
    public boolean hasChooseCloud() {
        return containsAction(ActionType.CHOOSE_CLOUD);
    }

    /**
     * Checks if player has chosen a character
     * @return true if player has chosen a character, otherwise false
     */
    public boolean hasChooseCharacter(){
        return containsAction(ActionType.CHOOSE_CHARACTER);
    }

    /**
     * Checks if player has activated a character
     * @return true if player has activated a character, otherwise false
     */
    public boolean hasActivatedCharacter(){
        return containsAction(ActionType.ACTIVATED_CHARACTER);
    }

    /**
     * Checks if player has finished his turn
     * @return true if player has finished his turn, false otherwise
     */
    public boolean hasEnded() {
        return containsAction(ActionType.END);
    }

    /**
     * Checks if player has already done a specific action
     * @param actionType specific action
     * @return true if the action is done, otherwise false
     */
    private boolean containsAction(ActionType actionType){
        for (Action action : actionList)
            if (action.getActionType() == actionType)
                return true;
        return false;
    }

    /**
     * Checks if the action list is empty
     * @return true if it is empty
     */
    public boolean isEmpty() {
        return actionList.size() == 0;
    }

    /**
     * Adds an action to the list
     * @param action the action to add
     */
    public void add(Action action) {
        actionList.add(action);
    }
}


