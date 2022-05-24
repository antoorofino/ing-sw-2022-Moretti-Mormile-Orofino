package it.polimi.ingsw.util;

import java.awt.*;

public enum TowerColor {
    BLACK("Black", new Color(75, 75, 75)),
    GRAY("Gray", new Color(190, 190, 190)),
    WHITE("White", new Color(255, 255, 255));

    private final String name;
    private final Color color;

    TowerColor(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public static TowerColor getPlayerColorByName(String color) {
        try{
            return TowerColor.valueOf(color.toUpperCase());
        }catch (IllegalArgumentException e){
            return null;
        }
    }

    @Override
    public String toString() {
        return name;
    }
}