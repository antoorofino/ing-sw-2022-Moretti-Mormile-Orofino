package it.polimi.ingsw.util;

import it.polimi.ingsw.util.Action;
import it.polimi.ingsw.util.ActionType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RoundActions  implements Serializable {
    private final ArrayList<Action> actionList;

    public RoundActions() {
        actionList = new ArrayList<>();
    }

    public List<Action> getActionsList() {
        return new ArrayList<>(actionList);
    }

    public int hasMovedStudents() {
        int counter = 0;
        for (Action action : actionList)
            if ((action.getActionType() == ActionType.MOVE_STUDENT_TO_ISLAND) || (action.getActionType() == ActionType.MOVE_STUDENT_TO_DININGROOM))
                counter++;
        return counter;
    }

    public boolean hasMovedMother() {
        return actionList.contains(ActionType.MOVE_MOTHER_NATURE);
    }

    public boolean hasChooseCloud() {
        return actionList.contains(ActionType.CHOOSE_CLOUD);
    }

    public boolean hasChooseCharacter(){
        return actionList.contains(ActionType.USE_CHARACTER);
    }

    public boolean hasEnded() {
        return actionList.contains(ActionType.END);
    }

    public boolean isEmpty() {
        return actionList.size() == 0;
    }

    public void add(Action action) {
        actionList.add(action);
    }

}


