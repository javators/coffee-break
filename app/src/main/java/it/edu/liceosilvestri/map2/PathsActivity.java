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
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import it.edu.liceosilvestri.map2.data.Path;
import it.edu.liceosilvestri.map2.data.Paths;

public class PathsActivity extends AppCompatActivity {

    private MapView mMapView;

    private GoogleMap mGmap;
    private boolean mGlobalLayoutReady = false;
    private boolean mMapReady = false;



    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paths);


        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        mMapView = findViewById(R.id.mapView);
        mMapView.onCreate(mapViewBundle);


        mMapView.getViewTreeObserver().addOnGlobalLayoutListener( () -> {
            if (mMapReady)
                putDataOnMap();

            mGlobalLayoutReady = true;
        });

        mMapView.getMapAsync((GoogleMap gmap) -> {

            mGmap = gmap;
            if (mGlobalLayoutReady)
                putDataOnMap();

            mMapReady = true;

        });


        ListView lv = findViewById(R.id.listViewPaths);
        lv.setAdapter(this.new PathAdapter());

        lv.setOnItemClickListener((adapterView, view, position, longid) -> {
            String pathid = Paths.get(PathsActivity.this).getPathAt(position).getId();

            Intent i = new Intent(getApplicationContext(), PathActivity.class);
            i.putExtra("id", pathid);
            PathsActivity.this.startActivity(i);
        });
    }



    private void putDataOnMap () {

        mGmap.getUiSettings().setZoomGesturesEnabled(true);
        mGmap.getUiSettings().setZoomControlsEnabled(true);

        Paths paths = Paths.get(PathsActivity.this);

        LatLngBounds bounds = paths.getBounds().getRectangle();
        mGmap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));

        for (Path path : paths) {

            PolylineOptions poly = new PolylineOptions();

            for (Path.Point pt : path.getPointArray())
                poly.add(new LatLng(pt.getCoordLat(), pt.getCoordLng()));

            poly.clickable(true);
            poly.color(path.getColor());

            Polyline pl = mGmap.addPolyline(poly);
            pl.setTag(path.getId());

                        /*

                        for (int i = 0; i < path.getPoiIdArray().length; i++) {
                            String poiid = path.getPoiIdArray()[i];
                            Poi p = Pois.get(this).getPoiBy(poiid);
                            if (p != null) {
                                MarkerOptions mop = p.getGoogleMarker();
                                Marker m = mGmap.addMarker(mop);
                                m.setTitle("" + (i + 1) + ". " + m.getTitle());
                                m.setTag(poiid);
                            }
                        }
                        */


                        /*
                        mGmap.setOnInfoWindowClickListener((mark) -> {
                            String poiid = (String) mark.getTag();
                            Intent i = new Intent(PathsActivity.this, PoiActivity.class);
                            i.putExtra("id", poiid);
                            PathsActivity.this.startActivity(i);
                        });
                        */

            mGmap.setOnPolylineClickListener( p ->{
                String pathid = (String) p.getTag();

                Intent intent = new Intent(PathsActivity.this, PathActivity.class);
                intent.putExtra("id", pathid);
                PathsActivity.this.startActivity(intent);

            });


                /*
                mGmap.setOnMarkerClickListener((mark)->{
                    return false; //true: evento consumato -> non mostra titolo e snippet
                });
                */
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



    class PathAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        public PathAdapter() {
            super();
            inflater = (LayoutInflater) PathsActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return Paths.get(PathsActivity.this).getLength();
        }

        @Override
        public Object getItem(int position) {
            return Paths.get(PathsActivity.this).getPathAt(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;

            if(view==null) {

                Path path = Paths.get(PathsActivity.this).getPathAt(position);

                view = inflater.inflate(R.layout.item_path , parent, false);


                ((TextView) view.findViewById(R.id.txtName)).setText(path.getName());
                ((TextView) view.findViewById(R.id.txtDescription)).setText(path.getDescription());



                /*
                ((TextView) findViewById(R.id.txtName)).setText(mPath.getName());
                ((TextView) findViewById(R.id.txtDescriptionLong)).setText(mPath.getDescriptionLong());
                ((TextView) findViewById(R.id.txtDuration)).setText(mPath.getDuration());
                ((TextView) findViewById(R.id.txtLength)).setText(mPath.getLength());
                ((TextView) findViewById(R.id.txtSuitableFor)).setText(mPath.getSuitableFor());

                */


                ImageView iv = view.findViewById(R.id.imgIcon);
                iv.setColorFilter(path.getColor());
            }
            return view;

        }
    }
}

