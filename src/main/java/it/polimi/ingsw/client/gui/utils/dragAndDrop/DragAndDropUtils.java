package it.polimi.ingsw.client.gui.utils.dragAndDrop;

import it.polimi.ingsw.client.gui.utils.ClientData;
import it.polimi.ingsw.model.Piece;
import it.polimi.ingsw.util.ActionType;
import javafx.scene.image.Image;

import java.io.*;
import java.util.Base64;

public class DragAndDropUtils {
    private static final ClientData data = ClientData.getInstance();
    public static String toString(DragAndDropInfo info) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(info);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    public static DragAndDropInfo fromString(String s) {
        byte[] data = Base64.getDecoder().decode(s);
        Object o = null;
        try {
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
            o = ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (DragAndDropInfo) o;
    }

    public static Image getDragBoardStudentImage(Piece piece) {
        String fileName = null;
        switch (piece) {
            case FROG:
                fileName = "student_green";
                break;
            case DRAGON:
                fileName = "student_red";
                break;
            case GNOME:
                fileName = "student_yellow";
                break;
            case FAIRY:
                fileName = "student_purple";
                break;
            case UNICORN:
                fileName = "student_blue";
                break;
        }
        return new Image(String.valueOf(DragAndDropUtils.class.getResource("/gui/images/" + fileName + ".png")), 20.0, 20.0, true, true);
    }

    public static boolean diningRoomAcceptsStudents() {
        return data.getPossibleActions().contains(ActionType.MOVE_STUDENT_TO_DININGROOM);
    }

    public static boolean islandAcceptsStudents() {
        return data.getPossibleActions().contains(ActionType.MOVE_STUDENT_TO_ISLAND);
    }

    public static boolean islandAcceptMotherNature(int islandId) {
        if (data.getPlayer().getLastCardUsed() == null)
            return false;
        int maxSteps = data.getPlayer().getLastCardUsed().getMovements();
        int mother = data.getGame().getIslandHandler().getMotherNature();
        int steps = islandId > mother ? islandId - mother : islandId - mother + data.getGame().getIslandHandler().getIslands().size();
        return data.getPossibleActions().contains(ActionType.MOVE_MOTHER_NATURE) && steps <= maxSteps && steps > 0;
    }
}
