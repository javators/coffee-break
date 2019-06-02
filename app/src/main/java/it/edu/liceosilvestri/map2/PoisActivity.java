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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;

import it.edu.liceosilvestri.map2.data.Categories;
import it.edu.liceosilvestri.map2.data.Category;
import it.edu.liceosilvestri.map2.data.Poi;
import it.edu.liceosilvestri.map2.data.Pois;

public class PoisActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pois);

        ExpandableListView expListView = findViewById(R.id.expListViewPois);
        MapView mv = findViewById(R.id.mapView);

        final Pois pois = Pois.get(getApplicationContext());
        final HashMap<Poi, Marker> map = new HashMap<>();

        mv.getMapAsync((GoogleMap gmap) -> {
            for (Poi p : pois) {
                MarkerOptions mop = p.getGoogleMarker();
                Marker marker = gmap.addMarker(mop);
                marker.setTag(p.getId());

                map.put(p, marker);
            }

            gmap.setOnInfoWindowClickListener((Marker marker) -> {
                Intent intent = new Intent(getApplicationContext(), PoiActivity.class);
                String poiid = marker.getTag() == null ? "" : marker.getTag().toString();

                intent.putExtra("id", poiid);
                startActivity(intent);
            });

            gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(getCenter(), 15));
        });
        mv.onCreate(savedInstanceState);
        mv.onStart();

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

    @Override
    protected void onStart() {
        super.onStart();

        BottomNavigator bnav = new BottomNavigator(this);
        bnav.startWorking();
    }

    private LatLng getCenter() {
        double lat = 0, lng = 0;

        Pois pois = Pois.get(getApplicationContext());
        for (Poi p : pois) {
            lat += p.getCoordLat();
            lng += p.getCoordLng();
        }

        lat /= pois.getLength();
        lng /= pois.getLength();

        return new LatLng(lat, lng);
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
            tvCategory.setText(name);
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
