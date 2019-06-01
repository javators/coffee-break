package it.edu.liceosilvestri.map2.data;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

public class Pois implements Iterable<Poi> {

    private static Pois __pois;
    private Poi mPoiArray[];
    private HashMap<String, Poi> mPoiMap;


    private Pois(Context ctx){

    }

    private void load(Context ctx) {
        int i;
        String files[];

        AssetManager am = ctx.getAssets();
        try {

            //conta  e carica i pois

            files = am.list("data/pois");
            if (files.length > 0) {
                mPoiArray = new Poi[files.length];
                mPoiMap = new HashMap<>(files.length);
                i=0;

                for (String s : files) {
                    int pos = s.indexOf(".");
                    if (pos > 0) {
                        String poiid = s.substring(0, pos);
                        Poi p = new Poi(poiid, ctx);
                        mPoiArray[i++] = p;
                        mPoiMap.put(poiid, p);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    public static Pois get(Context ctx) {
        if (__pois == null) {
            __pois = new Pois(ctx);
            __pois.load(ctx);
        }

        return __pois;
    }

    public int getLength(){
        return mPoiArray==null ? 0 : mPoiArray.length;
    }

    public Poi getPoiAt(int index) {
        if (mPoiArray != null && (index >= 0 && index < mPoiArray.length-1))
            return mPoiArray[index];
        else
            return null;
    }

    public Poi getPoiBy(String poiid) {
        return mPoiMap.get(poiid);
    }

    @NonNull
    @Override
    public Iterator<Poi> iterator() {
        return new Pois.PoiIterator();
    }


    public class PoiIterator implements Iterator<Poi> {

        private int mIndex=-1;

        @Override
        public boolean hasNext() {
            if (mPoiArray!=null && mIndex < mPoiArray.length-1)
                return true;
            else
                return false;
        }

        @Override
        public Poi next() {
            if (mPoiArray!=null &&mIndex < mPoiArray.length-1)
                return mPoiArray[++mIndex];
            else
                return null;
        }
    }
}