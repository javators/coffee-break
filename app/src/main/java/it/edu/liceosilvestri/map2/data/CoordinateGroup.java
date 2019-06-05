package it.edu.liceosilvestri.map2.data;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class CoordinateGroup {

    private double mUpperLat;
    private double  mLowerLat;
    private double mLeftMostLng;
    private double mRightMostLng;

    public CoordinateGroup() {
        mUpperLat = 0;
        mRightMostLng = 0;
        mLowerLat = 90;
        mLeftMostLng = 90;
    }

    public LatLngBounds getRectangle() {
        LatLngBounds bounds = new LatLngBounds(new LatLng(mLowerLat, mLeftMostLng), new LatLng(mUpperLat, mRightMostLng));
        return  bounds;
    }

    public void addPoint(double lat, double lng) {
        if (lat > mUpperLat) mUpperLat = lat;
        if (lat < mLowerLat) mLowerLat = lat;
        if (lng < mLeftMostLng) mLeftMostLng = lng;
        if (lng > mRightMostLng) mRightMostLng = lng;
    }
}
