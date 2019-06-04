package it.edu.liceosilvestri.map2.extra;

import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import it.edu.liceosilvestri.map2.R;
import it.edu.liceosilvestri.map2.data.Poi;
import it.edu.liceosilvestri.map2.data.Util;

public class PoiMuseum implements Poi.Extra {
    private Poi mPoi;

    private String mDescriptionLong;
    private String mOpeningTimes;
    private String mWebsite;
    private String[] mImageArray;

    @Override
    public void setPoi(Poi p) {
        mPoi = p;
    }

    @Override
    public void loadFromXml(Element extraNode) {

        /*
        <extra>
            <description_long></description_long>
            <opening_times></opening_times>
            <website></website>
            <image>img/museum.jpg</image>
        </extra>
        */

        mDescriptionLong = extraNode.getElementsByTagName("description_long").item(0).getTextContent();
        mOpeningTimes = extraNode.getElementsByTagName("opening_times").item(0).getTextContent();
        mWebsite = extraNode.getElementsByTagName("website").item(0).getTextContent();

        NodeList nList = extraNode.getElementsByTagName("image");
        int k=0;

        if (nList.getLength() > 0) {

            mImageArray = new String[nList.getLength()];
            k = 0;

            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    //Print each employee's detail
                    Element eElement = (Element) node;
                    String imgfilename = eElement.getTextContent();

                    mImageArray[k++] = imgfilename;

                }
            }
        }

    }

    public String getDescriptionLong() {
        return mDescriptionLong;
    }

    public String getOpeningTimes() {
        return mOpeningTimes;
    }

    public String getWebsite() {
        return mWebsite;
    }

    public String[] getImageArray() {
        return mImageArray;
    }


    @Override
    public void inflateView(ViewGroup vg) {
        LayoutInflater inflater = LayoutInflater.from(vg.getContext());
        inflater.inflate(R.layout.extra_museum, vg);

        ((TextView) vg.findViewById(R.id.txtOpeningTimes)).setText(mOpeningTimes);
        ((TextView) vg.findViewById(R.id.txtWebsite)).setText(mWebsite);
        ((TextView) vg.findViewById(R.id.txtDescription)).setText(mDescriptionLong);

        Util.ImagePager ip = new Util.ImagePager(vg.getContext(), mImageArray);
        ViewPager vp = vg.findViewById(R.id.viewPager);
        vp.setAdapter(ip);

    }

}
