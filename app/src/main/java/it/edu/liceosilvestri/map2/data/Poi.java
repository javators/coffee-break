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

public class Poi {


    private String mId;

    private String mRootId;
    private String mName;
    private String mDescription;
    private String  mNameLong;
    private String mCategoryId;
    private String mCoord;
    private String mSuitableFor;
    private String[] mPathIdArray;


    Poi(String id, Context ctx){
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

    public String getNameLong() {
        return mNameLong;
    }

    public String getCategoryId() {
        return mCategoryId;
    }

    public String getCoord() {
        return mCoord;
    }

    public String getSuitableFor() {
        return mSuitableFor;
    }

    public String[] getPoiIdArray() {
        return mPathIdArray;
    }

    private void load(Context ctx) {
        int i;
        String files[];

        AssetManager am = ctx.getAssets();
        try {
            InputStream is = am.open("data/poi/" + mId + ".xml");

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            /*
                poi id="puntoid">
                    <name>Nome breve che appare sulle mappe</name>
                    <name_long>Nome lungo che appare in ActivityPoi</name_long>
                    <description>Descrizione breve che compare sulle mappe</description>
                    <category>Categoria di appartenenza del POI, es: Bar, Museo, Stazione, Parco, Parcheggio</category>
                    <suitable_for>Tipologia di utenti a cui è adatto il percorso, es: famiglie, gruppi</suitable_for>
                    <coord>Coordinate del POI, es: 40.811397, 14.342874</coord>
                    <address>Indirzzo del POI se presente, es: via Università, 100</address>

                    <path id="percorsoid1" />
                    <path id="percorsoid2" />
                </poi>
             */
            Document document = builder.parse(is);
            Element root = document.getDocumentElement();

            mRootId = root.getAttribute("id");
            mName = root.getElementsByTagName("name").item(0).getTextContent();
            mDescription = root.getElementsByTagName("description").item(0).getTextContent();
            mNameLong = root.getElementsByTagName("name_long").item(0).getTextContent();
            mCategoryId = root.getElementsByTagName("category").item(0).getTextContent();
            mCoord = root.getElementsByTagName("coord").item(0).getTextContent();
            mSuitableFor = root.getElementsByTagName("suitable_for").item(0).getTextContent();

            NodeList nList2 = document.getElementsByTagName("pois");
            int k=0;

            if (nList2.getLength() > 0) {

                mPathIdArray = new String[nList2.getLength()];
                k = 0;

                for (i = 0; i < nList2.getLength(); i++) {
                    Node node = nList2.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        //Print each employee's detail
                        Element eElement = (Element) node;
                        String poiId = root.getAttribute("id");

                        mPathIdArray[k++] = poiId;
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


}
