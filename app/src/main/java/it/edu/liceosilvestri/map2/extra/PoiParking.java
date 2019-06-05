package it.edu.liceosilvestri.map2.extra;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Element;

import it.edu.liceosilvestri.map2.R;
import it.edu.liceosilvestri.map2.data.Poi;

public class PoiParking implements Poi.Extra {
    private Poi mPoi;

    private String mDescriptionLong;
    private String mOpeningTimes;

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
        </extra>
        */

        mDescriptionLong = extraNode.getElementsByTagName("description_long").item(0).getTextContent();
        mOpeningTimes = extraNode.getElementsByTagName("opening_times").item(0).getTextContent();


    }

    public String getDescriptionLong() {
        return mDescriptionLong;
    }

    public String getOpeningTimes() {
        return mOpeningTimes;
    }


    @Override
    public void inflateView(ViewGroup vg) {
        LayoutInflater inflater = LayoutInflater.from(vg.getContext());
        inflater.inflate(R.layout.extra_parking, vg);

        ((TextView) vg.findViewById(R.id.txtOpeningTimes)).setText(mOpeningTimes);
        ((TextView) vg.findViewById(R.id.txtDescription)).setText(mDescriptionLong);

    }

}
