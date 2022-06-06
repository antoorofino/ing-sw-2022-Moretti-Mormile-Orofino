package it.polimi.ingsw.client.gui.utils;

import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class DelayAction {
    private static final int DELAY_TIME = 1; // in seconds
    public static void executeLater(Runnable action) {
        PauseTransition pause = new PauseTransition(Duration.seconds(DELAY_TIME));
        pause.setOnFinished(e -> {
            action.run();
        });
        pause.setCycleCount(1);
        pause.play();
    }
}
