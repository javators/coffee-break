package it.edu.liceosilvestri.map2.data;


import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class Category {

    private String mId;
    private String mName;
    private String mNameSingular;
    private String mIcon;
    private int mIconResourceId;
    private String mManagedBy;
    private ArrayList<Poi> mPoiArray;
    private ArrayList<ArrayList<Poi>> mPoiArrayPerRelevance;

    private int mRelevance;


    Category(Context ctx, String id, String name, String nameSingular, String icon, String managedBy, int relevance) {
        this.mId = id;
        this.mName = name;
        this.mNameSingular = nameSingular;
        this.mIcon = icon;
        this.mIconResourceId = ctx.getResources().getIdentifier(icon, "drawable", ctx.getPackageName());
        this.mManagedBy = managedBy;
        this.mRelevance = relevance;
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

    public int getRelevance() {
        return mRelevance;
    }

    public int getIconResourceId() {
        return mIconResourceId;
    }

    public String getManagedBy() {
        return mManagedBy;
    }

    public List<Poi> getPois() {
        if (mPoiArray == null) {
            Pois ps = Pois.get();
            mPoiArray = new ArrayList<>();
            mPoiArrayPerRelevance = new ArrayList<>(3);

            for (int i=0; i<3; i++)
                mPoiArrayPerRelevance.add(new ArrayList<>());

            for (Poi p: ps) {
                if (p.getCategoryId().equals(mId)) {
                    int rel = p.getRelevance();
                    if (rel > 0 && rel <4 )
                        mPoiArrayPerRelevance.get(rel-1).add(p);
                }
            }

            for (int i=2; i>=0; i--)
                for (Poi p: mPoiArrayPerRelevance.get(i))
                    mPoiArray.add(p);

        }
        return mPoiArray;
    }
}