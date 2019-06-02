package it.edu.liceosilvestri.map2.data;

import android.content.Context;
import android.content.res.AssetManager;

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
    private Path.Segment[] mSegmentArray;
    private String[] mPoiIdArray;


    Path(String id, Context ctx){
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

    public Segment[] getSegmentArray() {
        return mSegmentArray;
    }

    public String[] getPoiIdArray() {
        return mPoiIdArray;
    }


    void load(Context ctx) {

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

            NodeList nList = document.getElementsByTagName("segment");
            int k;

            if (nList.getLength() > 0) {

                mSegmentArray = new Path.Segment[nList.getLength()];
                k = 0;

                for (int i = 0; i < nList.getLength(); i++) {
                    Node node = nList.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        //Print each employee's detail
                        Element eElement = (Element) node;
                        String type = eElement.getAttribute("type");
                        String start = eElement.getAttribute("start_coord");
                        String end = eElement.getAttribute("end_coord");

                        Segment seg = new Segment(type, start, end);

                        mSegmentArray[k++] = seg;
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
                        String poiId = root.getAttribute("id");

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


    public static class Segment {

        private final String mType;
        private final String mStartCoord;
        private final String mEndCoord;

        private Segment(String type, String start, String end) {
            this.mType = type;
            this.mStartCoord = start;
            this.mEndCoord = end;
        }
    }

}
