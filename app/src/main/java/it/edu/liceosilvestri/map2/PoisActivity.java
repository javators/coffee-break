package it.edu.liceosilvestri.map2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;

import it.edu.liceosilvestri.map2.data.Categories;
import it.edu.liceosilvestri.map2.data.Category;
import it.edu.liceosilvestri.map2.data.MapLoadStatus;
import it.edu.liceosilvestri.map2.data.Poi;
import it.edu.liceosilvestri.map2.data.Pois;

public class PoisActivity extends AppCompatActivity {

    private MapView mMapView;
    private GoogleMap mGmap;
    private MapLoadStatus mMapStatus;

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pois);

        ExpandableListView expListView = findViewById(R.id.expListViewPois);


        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        mMapView = findViewById(R.id.mapView);
        mMapView.onCreate(mapViewBundle);
        mMapStatus = MapLoadStatus.getInitialStatus(savedInstanceState);

        final Pois pois = Pois.get(getApplicationContext());
        final HashMap<Poi, Marker> map = new HashMap<>();


        mMapView.getViewTreeObserver().addOnGlobalLayoutListener( () -> {

            mMapStatus = mMapStatus.nextAfterLayoutReadyEvent();
            checkPutDataOnMap(map);

        });

        mMapView.getMapAsync((GoogleMap gmap) -> {

            mGmap = gmap;
            mMapStatus = mMapStatus.nextAfterMapReadyEvent();
            checkPutDataOnMap(map);

        });


        PoisAdapter adapter = new PoisAdapter(getApplicationContext(), map);
        expListView.setAdapter(adapter);

        expListView.setOnGroupClickListener((ExpandableListView listView, View view, int group, long l) -> {
            if (listView.isGroupExpanded(group))
                listView.collapseGroup(group);
            else
                listView.expandGroup(group);

            return true;
        });

        expListView.setOnChildClickListener((ExpandableListView listView, View view, int group, int child, long l) -> {
            Poi poi = (Poi) listView.getExpandableListAdapter().getChild(group, child);
            Intent intent = new Intent(getApplicationContext(), PoiActivity.class);
            String poiid = poi.getId();

            intent.putExtra("id", poiid);
            startActivity(intent);

            return true;
        });
    }

    private void checkPutDataOnMap(HashMap<Poi, Marker> map) {
        if (mMapStatus.canLoad()) {
            putDataOnMap(map);
            mMapStatus = mMapStatus.nextAfterLoaded();
        }
    }

    private void putDataOnMap(HashMap<Poi, Marker> map) {
        mGmap.getUiSettings().setZoomGesturesEnabled(true);
        mGmap.getUiSettings().setZoomControlsEnabled(true);

        MapView mapview = this.findViewById(R.id.mapView);

        LatLngBounds bounds = Pois.get(this).getBounds().getRectangle();
        mGmap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));

        for (Poi p : Pois.get(this)) {
            MarkerOptions mop = p.getGoogleMarker();
            Marker marker = mGmap.addMarker(mop);
            marker.setTag(p.getId());

            map.put(p, marker);
        }

        mGmap.setOnInfoWindowClickListener((Marker marker) -> {
            Intent intent = new Intent(getApplicationContext(), PoiActivity.class);
            String poiid = marker.getTag() == null ? "" : marker.getTag().toString();

            intent.putExtra("id", poiid);
            startActivity(intent);
        });
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
        mMapStatus.saveStatus(mapViewBundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();

        BottomNavigator bnav = new BottomNavigator(this);
        bnav.startWorking();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    }
    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }



    private class PoisAdapter extends BaseExpandableListAdapter {
        private final Context mContext;
        private final Categories mCategories;
        private final Map<Poi, Marker> mMap;

        public PoisAdapter(Context ctx, Map<Poi, Marker> map) {
            this.mContext = ctx;
            this.mCategories = Categories.get(ctx);
            this.mMap = map;
        }

        @Override
        public int getGroupCount() {
            return mCategories.getLength();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return mCategories.getCategoryAt(groupPosition).getPois().size();
        }

        @Override
        public Category getGroup(int groupPosition) {
            return mCategories.getCategoryAt(groupPosition);
        }

        @Override
        public Poi getChild(int groupPosition, int childPosition) {
            return mCategories.getCategoryAt(groupPosition).getPois().get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return 0;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View rowView, ViewGroup parent) {
            if (rowView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowView = inflater.inflate(R.layout.item_category, parent, false);
            }

            TextView tvCategory  = rowView.findViewById(R.id.txtPoiCategory);
            Switch switchVisible = rowView.findViewById(R.id.switchPoiCategoryVisible);
            ImageView iv = rowView.findViewById(R.id.imgCategoryIcon);

            Category category = getGroup(groupPosition);
            String name = category.getName();
            int size = category.getPois().size();
            tvCategory.setText(name + " ("+size+")");
            iv.setImageResource(category.getIconResourceId());

            final String categoryId = getGroup(groupPosition).getId();
            switchVisible.setChecked(true);
            switchVisible.setOnCheckedChangeListener((CompoundButton cButton, boolean b) -> {
                for (Poi p : mMap.keySet()) {
                    if (p.getCategoryId().equals(categoryId)) {
                        mMap.get(p).setVisible(b);
                    }
                }
            });

            return rowView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View rowView, ViewGroup parent) {
            if (rowView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowView = inflater.inflate(R.layout.item_poi, parent, false);
            }

            TextView tvName = rowView.findViewById(R.id.txtPoiItemName);
            tvName.setText(getChild(groupPosition, childPosition).getName());

            return rowView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
