package it.polimi.ingsw.client.gui.utils.dragAndDrop;

import it.polimi.ingsw.model.Piece;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;

import java.io.*;
import java.util.Base64;

public class DragAndDropUtils {
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
}
