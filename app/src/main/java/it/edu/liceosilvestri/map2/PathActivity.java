package it.edu.liceosilvestri.map2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import it.edu.liceosilvestri.map2.data.Path;
import it.edu.liceosilvestri.map2.data.Paths;
import it.edu.liceosilvestri.map2.data.Poi;
import it.edu.liceosilvestri.map2.data.Pois;

public class PathActivity extends AppCompatActivity {


    private MapView mMapView;
    private GoogleMap mGmap;

    private Path mPath;

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        mPath = Paths.get(getApplicationContext()).getPathBy(id);

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


            mMapView.getMapAsync(gmap -> {

                mGmap = gmap;

                PolylineOptions poly = new PolylineOptions();

                for (Path.Point pt : mPath.getPointArray())
                    poly.add(new LatLng(pt.getCoordLat(), pt.getCoordLng()));

                poly.clickable(false);
                poly.color(mPath.getColor());

                Polyline pl = mGmap.addPolyline(poly);


                for (int i=0; i< mPath.getPoiIdArray().length; i++) {
                    String poiid = mPath.getPoiIdArray()[i];
                    Poi p =Pois.get(this).getPoiBy(poiid);
                    if (p != null) {
                        MarkerOptions mop = p.getGoogleMarker();
                        Marker m = mGmap.addMarker(mop);
                        m.setTitle("" + (i+1) + ". " + m.getTitle());
                        m.setTag(poiid);
                    }
                }


                LatLng erc = new LatLng(40.818, 14.335);

                mGmap.moveCamera(CameraUpdateFactory.newLatLngZoom(erc, 14));


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
            });

            ListView lv = findViewById(R.id.listViewPois);
            lv.setAdapter(this.new PoiAdapter());

            lv.setOnItemClickListener((adapterView, view, position, longid) -> {
                String poiid = mPath.getPoiIdArray()[position];

                Intent i = new Intent(getApplicationContext(), PoiActivity.class);
                i.putExtra("id", poiid);
                PathActivity.this.startActivity(i);
            });
        }

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
            return mPath.getPoiIdArray().length;
        }

        @Override
        public Object getItem(int position) {
            return mPath.getPoiIdArray()[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;

            if(view==null) {
                view = inflater.inflate(R.layout.item_poi_of_path , parent, false);

                String poiid = mPath.getPoiIdArray()[position];
                Poi p = Pois.get(PathActivity.this).getPoiBy(poiid);

                ((TextView) view.findViewById(R.id.txtName)).setText("" + (position+1) + ". " + p.getNameLong());

                ImageView iv = view.findViewById(R.id.imgIcon);
                iv.setImageResource(p.getCategory().getIconResourceId());
            }
            return view;

        }
    }
}

