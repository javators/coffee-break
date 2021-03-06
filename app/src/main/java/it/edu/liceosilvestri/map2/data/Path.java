package it.edu.liceosilvestri.map2.data;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Path {


    private String mId;
    private String mRootId;
    private String mName;
    private String mDescription;
    private String mDescriptionLong;
    private String mDuration;
    private String mLength;
    private String mSuitableFor;
    private String mColor;
    private Point[] mPointArray;
    private String[] mPoiIdArray;

    private CoordinateGroup mBounds;


    //private LatLngBounds mBounds;


    Path(String id){
        this.mId = id;
    }
    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getDescriptionLong() {
        return mDescriptionLong;
    }

    public String getDuration() {
        return mDuration;
    }

    public String getLength() {
        return mLength;
    }

    public String getSuitableFor() {
        return mSuitableFor;
    }

    public int getColor() {
        try {
            return Color.parseColor(mColor);
        }
        catch (Exception e) {
            return Color.BLACK;
        }
    }

    public Point[] getPointArray() {
        return mPointArray;
    }


    public String[] getPoiIdArray() {
        return mPoiIdArray;
    }

    public CoordinateGroup getBounds() {
        return mBounds;
    }

    void load() {

        Context ctx = AppDatabase.getContext();
        AssetManager am = ctx.getAssets();
        try {
            InputStream is = am.open("data/paths/" + mId + ".xml");

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            /*
                <path id="percorsoid">
                    <name>Nome percorso</name>
                    <description>Descrizione percorso che compare in ActivityPaths</description>
                    <description_long>Descriozione percorso che compare in ActivityPath</description_long>
                    <duration>Durata del percorso a piedi, es: 1-5h</duration>
                    <length>Lunghezza del percorso a piedi, es: 1,2km</length>
                    <suitable_for>Tipologia di utenti a cui è adatto il percorso, es: famiglie, gruppi</suitable_for>

                    <segment>
                        <type>Codice che identifica il tipo di segmento da disegnare, es: Standard</type>
                        <start_coord>Coordinate del punto di partenza, es: 40.819638, 14.331037</start_coord>
                        <end_coord>Coordinate del punto di partenza, es: 40.810192, 14.336756</end_coord>
                    </segment>
                    <segment>
                        <type>Codice che identifica il tipo di segmento da disegnare, es: Standard</type>
                        <start_coord>Coordinate del punto di partenza, es: 40.777777, 77.331037</start_coord>
                        <end_coord>Coordinate del punto di partenza, es: 77.810192, 14.777777</end_coord>
                    </segment>
                    <poi id="puntoid1" />
                    <poi id="puntoid2" />
                </path>

             */
            Document document = builder.parse(is);
            Element root = document.getDocumentElement();

            mRootId = root.getAttribute("id");
            mName = root.getElementsByTagName("name").item(0).getTextContent();
            mDescription = root.getElementsByTagName("description").item(0).getTextContent();
            mDescriptionLong = root.getElementsByTagName("description_long").item(0).getTextContent();
            mDuration = root.getElementsByTagName("duration").item(0).getTextContent();
            mLength = root.getElementsByTagName("length").item(0).getTextContent();
            mSuitableFor = root.getElementsByTagName("suitable_for").item(0).getTextContent();
            mColor = root.getElementsByTagName("color").item(0).getTextContent();

            NodeList nList = document.getElementsByTagName("point");
            int k;

            mBounds = new CoordinateGroup();

            if (nList.getLength() > 0) {

                mPointArray = new Point[nList.getLength()];
                k = 0;

                for (int i = 0; i < nList.getLength(); i++) {
                    Node node = nList.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        //Print each employee's detail
                        Element eElement = (Element) node;
                        String coord = eElement.getAttribute("coord");

                        Point seg = new Point(coord);

                        mPointArray[k++] = seg;


                        String[] latlongstr = coord.split(", ");
                        if (latlongstr.length == 2) {
                            double lat = Double.parseDouble(latlongstr[0]);
                            double lng = Double.parseDouble(latlongstr[1]);
                            mBounds.addPoint(lat, lng);
                        }

                    }
                }
            }


            NodeList nList2 = document.getElementsByTagName("poi");
            k=0;

            if (nList2.getLength() > 0) {

                mPoiIdArray = new String[nList2.getLength()];
                k = 0;

                for (int i = 0; i < nList2.getLength(); i++) {
                    Node node = nList2.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        //Print each employee's detail
                        Element eElement = (Element) node;
                        String poiId = eElement.getAttribute("id");

                        mPoiIdArray[k++] = poiId;
                    }
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }


    }


    public static class Point {

        private final String mCoord;

        private Point(String coord) {
            this.mCoord = coord;
        }

        public double getCoordLat() {
            return Double.parseDouble(mCoord.split(", ")[0]);
        }

        public double getCoordLng() {
            return Double.parseDouble(mCoord.split(", ")[1]);
        }
    }

}
