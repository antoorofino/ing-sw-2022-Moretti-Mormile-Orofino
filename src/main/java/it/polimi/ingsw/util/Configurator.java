package it.polimi.ingsw.util;

import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.server.rules.Rules;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.List;

public class Configurator {
    private static final int socketTimeout = 20000;
    private static final int serverPort = 8090;
    private static final boolean debug = true;
    private static final String serverIp = "127.0.0.1";

    public static int getSocketTimeout() {
        return Configurator.socketTimeout;
    }

    public static int getHeartbeatInterval(){
        return Configurator.socketTimeout/4;
    }

    public static int getServerPort(){
        return Configurator.serverPort;
    }

    public static String getServerIp(){
        return Configurator.serverIp;
    }

    public static List<Character> getAllCharactersCards(GameModel game) {
        String fileName = "/Characters.xml";
        List<Character> characters = new ArrayList<>();
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(Configurator.class.getResourceAsStream(fileName));
            doc.getDocumentElement().normalize();
            NodeList list = doc.getElementsByTagName("character");
            for (int temp = 0; temp < list.getLength(); temp++) {
                Node node = list.item(temp);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    int id = Integer.parseInt(element.getAttribute("id"));
                    String name = element.getElementsByTagName("name").item(0).getTextContent();
                    String description = element.getElementsByTagName("description").item(0).getTextContent();
                    String rules = element.getElementsByTagName("rules").item(0).getTextContent();
                    int cost = Integer.parseInt(element.getElementsByTagName("cost").item(0).getTextContent());
                    if (Configurator.debug)
                        cost = 1;
                    characters.add(new Character(
                            name,
                            description,
                            cost,
                            id,
                            (Rules) Class.forName(rules).getConstructor(GameModel.class).newInstance(game)
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return characters;
    }

    public static boolean isDebug(){
        return Configurator.debug;
    }
}
