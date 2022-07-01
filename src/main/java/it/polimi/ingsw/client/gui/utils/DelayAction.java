package it.polimi.ingsw.client.gui.utils;

import javafx.animation.PauseTransition;
import javafx.util.Duration;

/**
 * Class used to add delay to an action execution
 */
public class DelayAction {
    private static final int DELAY_TIME_IN_SECONDS = 1;

    /**
     * Adds delay before executing the runnable
     * @param action runnable function executed after delay
     */
    public static void executeLater(Runnable action) {
        PauseTransition pause = new PauseTransition(Duration.seconds(DELAY_TIME_IN_SECONDS));
        pause.setOnFinished(e -> action.run());
        pause.setCycleCount(1);
        pause.play();
    }
}
