package it.edu.liceosilvestri.map2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import it.edu.liceosilvestri.map2.data.MapLoadStatus;
import it.edu.liceosilvestri.map2.data.Path;
import it.edu.liceosilvestri.map2.data.Paths;
import it.edu.liceosilvestri.map2.data.Poi;
import it.edu.liceosilvestri.map2.data.Pois;

public class PathActivity extends AppCompatActivity {

    private MapView mMapView;
    private GoogleMap mGmap;
    private MapLoadStatus mMapStatus;
    private Path mPath;
    private boolean mShowAll;
    private Map<Poi, Marker> mPoiToMarkerTable;
    private ArrayList<Poi> mRelevantPoisInPath;
    private ArrayList<Poi> mAllPoisInPath;
    private HashMap<Poi, Integer> mPoiOrdinalInPath;

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path);

        BarAction mybar = new BarAction(this);
        mybar.startWorking(null, 0, true);

        //lo stato è salvato dal widget.
        Switch sw = (Switch) findViewById(R.id.switchRelevanceAll);
        mShowAll = sw.isChecked();

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        mPath = Paths.get().getPathBy(id);

        if (mPath == null) {
            ((TextView) findViewById(R.id.txtName)).setText("Percorso [" + id + "] non trovato");
        }
        else {

            ((TextView) findViewById(R.id.txtName)).setText(mPath.getName());
            ((TextView) findViewById(R.id.txtDescriptionLong)).setText(mPath.getDescriptionLong());
            ((TextView) findViewById(R.id.txtDuration)).setText(mPath.getDuration());
            ((TextView) findViewById(R.id.txtLength)).setText(mPath.getLength());
            ((TextView) findViewById(R.id.txtSuitableFor)).setText(mPath.getSuitableFor());

            ImageView iv = findViewById(R.id.imgPathIcon);
            iv.setColorFilter(mPath.getColor());



            Bundle mapViewBundle = null;
            if (savedInstanceState != null) {
                mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
            }

            mMapView = findViewById(R.id.mapView);
            mMapView.onCreate(mapViewBundle);
            mMapStatus = MapLoadStatus.getInitialStatus(savedInstanceState);
            int poicount = mPath.getPoiIdArray().length;
            mRelevantPoisInPath = new ArrayList<>(poicount);
            mAllPoisInPath = new ArrayList<>(poicount);
            mPoiOrdinalInPath = new HashMap<>(poicount);
            int ordinal = 1;

            for (String poiid : mPath.getPoiIdArray()) {
                Poi p = Pois.get().getPoiBy(poiid);
                if (p != null) {
                    mAllPoisInPath.add(p);
                    if (p.getRelevance() > 1) {
                        mRelevantPoisInPath.add(p);
                        mPoiOrdinalInPath.put(p, ordinal++);
                    }
                }
            }



            mMapView.getViewTreeObserver().addOnGlobalLayoutListener( () -> {

                mMapStatus = mMapStatus.nextAfterLayoutReadyEvent();
                checkPutDataOnMap();

            });

            mMapView.getMapAsync((GoogleMap gmap) -> {

                mGmap = gmap;
                mMapStatus = mMapStatus.nextAfterMapReadyEvent();
                checkPutDataOnMap();

            });


            ListView lv = findViewById(R.id.listViewPois);
            lv.setAdapter(this.new PoiAdapter());

            lv.setOnItemClickListener((adapterView, view, position, longid) -> {
                String poiid = mPath.getPoiIdArray()[position];

                Intent i = new Intent(getApplicationContext(), PoiActivity.class);
                i.putExtra("id", poiid);
                PathActivity.this.startActivity(i);
            });


            sw.setOnClickListener(v -> changeMarkerVisibility(((Switch)v).isChecked()));
        }

    }

    private void changeMarkerVisibility(boolean checked) {
        for (Poi p : mPoiToMarkerTable.keySet()) {
            mPoiToMarkerTable.get(p).setVisible(checked || p.getRelevance() > 1);
        }
        mShowAll = checked;

        ListView lv = findViewById(R.id.listViewPois);
        BaseAdapter ba = (BaseAdapter) lv.getAdapter();
        ba.notifyDataSetChanged();
    }

    private void checkPutDataOnMap() {
        if (mMapStatus.canLoad()) {
            putDataOnMap();
            mMapStatus = mMapStatus.nextAfterLoaded();
        }
    }

    private void putDataOnMap() {

        mGmap.getUiSettings().setZoomGesturesEnabled(true);
        mGmap.getUiSettings().setZoomControlsEnabled(true);
        mGmap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.no_business_style_json));

        LatLngBounds bounds = mPath.getBounds().getRectangle();
        mGmap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));


        PolylineOptions poly = new PolylineOptions();

        for (Path.Point pt : mPath.getPointArray())
            poly.add(new LatLng(pt.getCoordLat(), pt.getCoordLng()));

        poly.clickable(false);
        poly.color(mPath.getColor());

        Polyline pl = mGmap.addPolyline(poly);

        int posInPath = 1;
        mPoiToMarkerTable = new HashMap<>(mPath.getPoiIdArray().length);

        for (Poi p : mAllPoisInPath) {
            Marker m = p.addMarkerToMap(mGmap, Poi.MapType.PATH, "" + posInPath);

            if (p.getRelevance() > 1) {
                //cambia il titolo
                m.setTitle("" + posInPath + ". " + m.getTitle());
                posInPath++;
            }
            else
                m.setVisible(false);

            mPoiToMarkerTable.put(p, m);

        }


        mGmap.setOnInfoWindowClickListener((mark)->{
            String poiid = (String) mark.getTag();
            Intent i = new Intent(PathActivity.this, PoiActivity.class);
            i.putExtra("id", poiid);
            PathActivity.this.startActivity(i);
        });

        /*
        mGmap.setOnMarkerClickListener((mark)->{
            return false; //true: evento consumato -> non mostra titolo e snippet
        });
        */

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



    class PoiAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        public PoiAdapter() {
            super();
            inflater = (LayoutInflater) PathActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mShowAll ? mAllPoisInPath.size() : mRelevantPoisInPath.size();
        }

        @Override
        public Object getItem(int position) {
            return mShowAll ? mAllPoisInPath.get(position) : mRelevantPoisInPath.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;

            if (view == null)
                view = inflater.inflate(R.layout.item_poi_of_path, parent, false);

            Poi p = mShowAll ? mAllPoisInPath.get(position) : mRelevantPoisInPath.get(position);

            TextView tv = (TextView) view.findViewById(R.id.txtName);

            String title;
            if (p.getRelevance() > 1) {
                //cambia il titolo
                int ordinalPos = mPoiOrdinalInPath.get(p);
                tv.setText("" + ordinalPos + ". " + p.getNameLong());
                tv.setTypeface(null, Typeface.BOLD);
                //tv.setTextColor(Color.BLACK);

            }
            else {
                tv.setText("    " + p.getNameLong());
                tv.setTypeface(null, Typeface.ITALIC);
            }

            ImageView iv = view.findViewById(R.id.imgBarIcon);
            iv.setImageResource(p.getCategory().getIconResourceId());


            return view;
        }
    }
}

