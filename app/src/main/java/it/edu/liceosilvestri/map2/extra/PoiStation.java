package it.edu.liceosilvestri.map2.extra;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Element;

import it.edu.liceosilvestri.map2.R;
import it.edu.liceosilvestri.map2.data.Poi;

public class PoiStation implements Poi.Extra {
    private Poi mPoi;

    private String mDescriptionLong;
    private String mWebsite;

    @Override
    public void setPoi(Poi p) {
        mPoi = p;
    }

    @Override
    public void loadFromXml(Element extraNode) {

        /*
        <extra>
            <description_long></description_long>
            <website></website>
        </extra>
        */

        mDescriptionLong = extraNode.getElementsByTagName("description_long").item(0).getTextContent();
        mWebsite = extraNode.getElementsByTagName("website").item(0).getTextContent();


    }

    public String getDescriptionLong() {
        return mDescriptionLong;
    }


    public String getWebsite() {
        return mWebsite;
    }



    @Override
    public void inflateView(ViewGroup vg) {
        LayoutInflater inflater = LayoutInflater.from(vg.getContext());
        inflater.inflate(R.layout.extra_restaurant, vg);

        ((TextView) vg.findViewById(R.id.txtWebsite)).setText(mWebsite);
        ((TextView) vg.findViewById(R.id.txtDescription)).setText(mDescriptionLong);

    }
}

