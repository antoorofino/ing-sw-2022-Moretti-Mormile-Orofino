package it.polimi.ingsw.client.gui.utils.dragAndDrop;

import it.polimi.ingsw.model.Piece;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;

import java.io.*;
import java.util.Base64;

/**
 * Helper class used to store and retrieve drag and drop event information
 */
public class DragAndDropUtils {
    /**
     * Converts a DragAndDropInfo object to a ClipboardContent
     * @param ddi the object to convert
     * @return ClipboardContent containing the information
     */
    public static ClipboardContent toClipboardContent(DragAndDropInfo ddi) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(ddi);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ClipboardContent cc = new ClipboardContent();
        cc.putString(Base64.getEncoder().encodeToString(baos.toByteArray()));
        return cc;
    }

    /**
     * Retires the DragAndDropInfo from the DragBoard string
     * @param s DragBoard string
     * @return DragAndDropInfo object
     */
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

    /**
     * Gets the proper image to set the drag and drop cursor depending on the student moved
     * @param piece the student moved
     * @return the Image of the student piece
     */
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
}
