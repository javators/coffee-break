package it.edu.liceosilvestri.map2.data;


import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class Category {

    private Context mCtx;
    private String mId;
    private String mName;
    private String mNameSingular;
    private String mIcon;
    private String mManagedBy;
    private ArrayList<Poi> mPoiArray;


    Category(Context ctx, String id, String name, String nameSingular, String icon, String managedBy) {
        this.mId = id;
        this.mName = name;
        this.mNameSingular = nameSingular;
        this.mIcon = icon;
        this.mManagedBy = managedBy;
        this.mCtx = ctx;
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getNameSingular() {
        return mNameSingular;
    }

    public String getIcon() {
        return mIcon;
    }

    public String getManagedBy() {
        return mManagedBy;
    }

    public List<Poi> getPois() {
        if (mPoiArray == null) {
            Pois ps = Pois.get(mCtx);
            mPoiArray = new ArrayList<>(ps.getLength());
            for (Poi p: ps) {
                if (p.getCategoryId().equals(mId))
                    mPoiArray.add(p);
            }
        }
        return mPoiArray;
    }
}