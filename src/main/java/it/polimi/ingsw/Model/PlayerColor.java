package it.polimi.ingsw.Model;

import java.awt.*;

public enum PlayerColor {
    BLACK("Black", new Color(75, 75, 75)),
    GRAY("Gray", new Color(190, 190, 190)),
    WHITE("White", new Color(255, 255, 255));

    private final String name;
    private final Color color;

    PlayerColor(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public PlayerColor getPlayerColorByName(String color) {
        return PlayerColor.valueOf(color.toUpperCase());
    }

    @Override
    public String toString() {
        return name;
    }
}