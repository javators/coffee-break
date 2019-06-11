package it.edu.liceosilvestri.map2.data;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import it.edu.liceosilvestri.map2.R;

public class Poi {


    private String mId;

    private String mRootId;
    private String mName;
    private String mDescription;
    private String  mNameLong;
    private String mCategoryId;
    private Category mCategory;
    private String mCoord;
    private String mSuitableFor;
    private Path[] mPathArray;
    private String mAddress;
    private Extra mExtra;
    private MarkerOptions mMop;
    private HashMap<String, Marker> mMarkers;
    private int mRelevance;


    Poi(String id, Context ctx){
        this.mId = id;
        load(ctx);
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

    public Category getCategory() {
        return mCategory;
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
            InputStream is = am.open("data/pois/" + mId + ".xml");

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            /*
                poi id="puntoid">
                    <name>Nome breve che appare sulle mappe</name>
                    <name_long>Nome lungo che appare in ActivityPoi</name_long>
                    <description>Descrizione breve che compare sulle mappe</description>
                    <category id="monument">Categoria di appartenenza del POI, es: Bar, Museo, Stazione, Parco, Parcheggio</category>
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
            mCoord = root.getElementsByTagName("coord").item(0).getTextContent();
            mAddress = root.getElementsByTagName("address").item(0).getTextContent();
            mSuitableFor = root.getElementsByTagName("suitable_for").item(0).getTextContent();
            mRelevance = 2;
            try {
                mRelevance = Integer.parseInt(root.getElementsByTagName("relevance").item(0).getTextContent());
            } catch (Exception e) {}

            Element cat = (Element) root.getElementsByTagName("category").item(0);
            mCategoryId = cat.getAttribute("id");

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
                        String pathId = eElement.getAttribute("id");


                        mPathArray[k++] = Paths.get().getPathBy(pathId);

                    }
                }
            }

            Element extraNode = (Element) root.getElementsByTagName("extra").item(0);

            mCategory = Categories.get().getCategoryBy(mCategoryId);
            String className = mCategory.getManagedBy();

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

    public enum MapType {
        POI,
        PATH,
        POIS;

        private MarkerStyle getStyle(int relevance) {
            switch (this) {
                case POI:
                    return MarkerStyle.POI;
                case PATH:
                    return relevance>1 ? MarkerStyle.PATH_RELEVANT_WITH_TEXT : MarkerStyle.PATH;
                case POIS:
                    return relevance>1 ? MarkerStyle.POIS_RELEVANT : MarkerStyle.POIS;
                default :
                    return MarkerStyle.POI;
            }
        }
    }

    public enum MarkerStyle {
        POI(R.layout.custom_marker_simple),
        PATH_RELEVANT_WITH_TEXT(R.layout.custom_marker_with_text_inside),
        PATH(R.layout.custom_marker_simple),
        POIS_RELEVANT(R.layout.custom_marker_simple),
        POIS(R.layout.custom_marker_simple);

        private int mLayoutid;
        private MarkerStyle(int layoutid) {
            mLayoutid = layoutid;
        }

        public int getLayoutid() {
            return mLayoutid;
        }
        public String makeCacheid(String text) {
            if (text == null)
                return "" + this.ordinal();
            else
                return this.ordinal() + text;
        }
    }

    /* view methods */
    public Marker addMarkerToMap(GoogleMap gmap, MapType mapType, String inMarkerText) {
        if (mMarkers == null)
            mMarkers = new HashMap<>();

        MarkerStyle style = mapType.getStyle(mRelevance);
        String cacheid = style.makeCacheid(inMarkerText);

        if (mMarkers.containsKey(cacheid)) {
            Marker m = (Marker) mMarkers.get(cacheid);
            //resetta il titolo, che può essere stato modificato...
            m.setTitle(mName);
            return m;
        }
        else {
            LatLng lalo = new LatLng(getCoordLat(), getCoordLng());

            Bitmap myicon = Util.createCustomMarkerWithText(style.getLayoutid(), this.getCategory().getIconResourceId(), inMarkerText);

            MarkerOptions mop = new MarkerOptions()
                    .position(lalo)
                    .icon(BitmapDescriptorFactory.fromBitmap(myicon))
                    //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    .alpha(0.7f)
                    .snippet(mDescription)
                    .title(mName);


            Marker m = gmap.addMarker(mop);
            //m.setTitle("" + (i+1) + ". " + m.getTitle());
            m.setTag(mId);

            mMarkers.put(cacheid, m);
            return m;

        }
    }

    /*

    public MarkerOptions getGoogleMarker() {
        if (mMarkers == null)
            mMarkers = new HashMap<>();

        if (mMarkers.containsKey("_simple_"))
            return (MarkerOptions) mMarkers.get("_simple_");
        else {
            LatLng lalo = new LatLng(getCoordLat(), getCoordLng());

            MarkerOptions mop = new MarkerOptions()
                    .position(lalo)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    .alpha(0.7f)
                    .snippet(mDescription)
                    .title(mName);

            mMarkers.put("_simple_", mop);
            return mop;
        }
    }


    public MarkerOptions getGoogleMarker(String text) {
        if (mMarkers == null)
            mMarkers = new HashMap<>();

        if (mMarkers.containsKey("_with_text_" + text))
            return (MarkerOptions) mMarkers.get("_with_text_" + text);
        else {
            LatLng lalo = new LatLng(getCoordLat(), getCoordLng());

            Bitmap myicon = Util.createCustomMarkerWithText(R.layout.custom_marker_with_text_inside, this.getCategory().getIconResourceId(), text);

            MarkerOptions mop = new MarkerOptions()
                    .position(lalo)
                    .icon(BitmapDescriptorFactory.fromBitmap(myicon))
                    //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    .alpha(0.7f)
                    .snippet(mDescription)
                    .title(mName);


            mMarkers.put("_with_text_" + text, mop);
            return mop;
        }
    }
*/


    public interface Extra {
        public void setPoi(Poi p);
        public void loadFromXml(Element node);
        public void inflateView(ViewGroup vg);

    }



}
