package it.edu.liceosilvestri.map2.data;

import android.content.Context;
import android.content.res.AssetManager;
import android.view.View;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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
    private Path[] mPathArray;
    private String mAddress;
    private Extra mExtra;
    private MarkerOptions mMop;


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

    public double getCoordLat() {
        return Double.parseDouble(mCoord.split(", ")[0]);
    }

    public double getCoordLng() {
        return Double.parseDouble(mCoord.split(", ")[1]);
    }

    public String getSuitableFor() {
        return mSuitableFor;
    }

    public Path[] getPathArray() {
        return mPathArray;
    }

    public String getAddress() {
        return mAddress;
    }

    public Extra getExtra() {
        return mExtra;
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
            mAddress = root.getElementsByTagName("address").item(0).getTextContent();
            mSuitableFor = root.getElementsByTagName("suitable_for").item(0).getTextContent();


            NodeList nList = document.getElementsByTagName("path");
            int k=0;

            if (nList.getLength() > 0) {

                mPathArray = new Path[nList.getLength()];
                k = 0;

                for (i = 0; i < nList.getLength(); i++) {
                    Node node = nList.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        //Print each employee's detail
                        Element eElement = (Element) node;
                        String pathId = root.getAttribute("id");


                        mPathArray[k++] = Paths.get(ctx).getPathBy(pathId);

                    }
                }
            }

            Node extraNode = root.getElementsByTagName("extra").item(0);

            Category c = Categories.get(ctx).getCategoryBy(mCategoryId);
            String className = c.getManagedBy();

            try {

                mExtra = (Extra) Class.forName(className).newInstance();

            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


            mExtra.setPoi(this);
            mExtra.loadFromXml(extraNode);



        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }


    }


    /* view methods */
    public MarkerOptions getGoogleMarker() {
        if (mMop == null) {
            LatLng lalo = new LatLng(getCoordLat(), getCoordLng());

            mMop = new MarkerOptions()
                    .position(lalo)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                    .alpha(0.5f)
                    .snippet(mDescription)
                    .title(mName);
        }
        return mMop;
    }

    public interface Extra {
        public void setPoi(Poi p);
        public void loadFromXml(Node extraNode);
        public void inflateView(View v);

    }


}
