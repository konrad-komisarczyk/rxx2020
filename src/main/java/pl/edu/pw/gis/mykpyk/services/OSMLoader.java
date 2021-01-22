package pl.edu.pw.gis.mykpyk.services;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class OSMLoader {
    private static final String OVERPASS_API = "http://www.overpass-api.de/api/interpreter";
    private static final String OPENSTREETMAP_API_06 = "http://www.openstreetmap.org/api/0.6/";
    public static Document testDoc;

    public OSMLoader() {
    }

    public static Document getNodesViaOverpass(String queryString) throws IOException, ParserConfigurationException, SAXException {
        String hostname = OVERPASS_API;

        URL osm = new URL(hostname);
        HttpURLConnection connection = (HttpURLConnection) osm.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        DataOutputStream printout = new DataOutputStream(connection.getOutputStream());
        printout.writeBytes("data=" + URLEncoder.encode(queryString, "utf-8"));
        printout.flush();
        printout.close();

        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
        return (testDoc = docBuilder.parse(connection.getInputStream()));
    }

    /**
     *
     * @param n latitude of the northern border
     * @param e longitude of the eastern border
     * @param s latitude of the southern border
     * @param w longitude of the western border
     * @param key key for OpenStreetMap objects searched
     * @param value value for OpenStreetMap objects searched
     * @return the xml document containing the queries nodes
     */
    public static ElementsLocations getCoordinatesOfObjects(double n, double e, double s, double w,
                                                            String key, String value)  {
        String latLoaded, lonLoaded;
        double latAsDouble, lonAsDouble;
        String query = "<osm-script>\n" +
                "<query into=\"_\" type=\"node\">\n" +
                "<has-kv k=\"" + key + "\" modv=\"\" v=\"" + value + "\"/>\n" +
                "<bbox-query s=\"" + s +"\" w=\"" + w + "\" n=\"" + n + "\" e=\"" + e + "\"/>\n" +
                "</query>\n" +
                "<print e=\"\" from=\"_\" geometry=\"skeleton\" ids=\"yes\" limit=\"\" mode=\"body\" n=\"\" order=\"id\" s=\"\" w=\"\"/>\n" +
                "</osm-script>";
        Document doc = null;
        try {
            doc = OSMLoader.getNodesViaOverpass(query);
        } catch (IOException ioException) {
            System.out.println("@@@ IOException in OSMLoader");
            return null;
        } catch (ParserConfigurationException parserConfigurationException) {
            System.out.println("@@@ ParserConfigurationException in OSMLoader");
            return null;
        } catch (SAXException saxException) {
            System.out.println("@@@ SAXException in OSMLoader");
            return null;
        }

        ArrayList<Double> lats = new ArrayList<>(); // output array of lattitudes
        ArrayList<Double> lons = new ArrayList<>(); // output array of longitudes

        NodeList nodeList = doc.getChildNodes().item(0).getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node currentNode = nodeList.item(i);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                //calls this method for all the children which is Element
                if (currentNode.getAttributes().getLength() == 3) {
                    latLoaded = currentNode.getAttributes().getNamedItem("lat").toString();
                    latAsDouble = Double.parseDouble(latLoaded.substring(5, latLoaded.length()-1));
                    lats.add(latAsDouble);
                    lonLoaded = currentNode.getAttributes().getNamedItem("lon").toString();
                    lonAsDouble = Double.parseDouble(lonLoaded.substring(5, lonLoaded.length()-1));
                    lons.add(lonAsDouble);
                }
            }
        }
        return new ElementsLocations(lats, lons);
    }
}
