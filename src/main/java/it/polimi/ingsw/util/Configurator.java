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

/**
 * Stores information used to configure a game
 */
public class Configurator {
    private static final int SOCKET_TIMEOUT = 2000;
    private static final int DEFAULT_SERVER_PORT = 8090;
    private static final int DEFAULT_VERBOSE_LEVEL = 1;
    private static final String DEFAULT_SERVER_IP = "127.0.0.1";

    /**
     * Gets the timeout of connection
     * @return timeout of connection
     */
    public static int getSocketTimeout() {
        return Configurator.SOCKET_TIMEOUT;
    }

    /**
     * Gets the frequency of ping's messages
     * @return frequency of ping's messages
     */
    public static int getHeartbeatInterval(){
        return Configurator.SOCKET_TIMEOUT /4;
    }

    /**
     * Gets default server port
     * @return default server port
     */
    public static int getDefaultServerPort(){
        return Configurator.DEFAULT_SERVER_PORT;
    }

    /**
     * Gets default verbose log level
     * @return default verbose level
     */
    public static int getDefaultVerboseLevel(){
        return Configurator.DEFAULT_VERBOSE_LEVEL;
    }

    /**
     * Gets default server ip
     * @return default server ip
     */
    public static String getDefaultServerIp(){
        return Configurator.DEFAULT_SERVER_IP;
    }

    /**
     * Gets the list of characters from xml file
     * @param game game
     * @return list of characters
     */
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
}
