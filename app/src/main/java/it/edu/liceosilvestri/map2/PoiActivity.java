package it.edu.liceosilvestri.map2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import it.edu.liceosilvestri.map2.data.Path;
import it.edu.liceosilvestri.map2.data.Poi;
import it.edu.liceosilvestri.map2.data.Pois;

public class PoiActivity extends AppCompatActivity {

    private MapView mMapView;
    private GoogleMap mGmap;

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi);


        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        Poi poi = Pois.get(getApplicationContext()).getPoiBy(id);

        if (poi == null) {
            ((TextView) findViewById(R.id.txtName)).setText("Punto [" + id + "] non trovato");
        }
        else {

            ((TextView) findViewById(R.id.txtName)).setText(poi.getNameLong());
            ((TextView) findViewById(R.id.txtAddress)).setText(poi.getAddress());

            ImageView iv = findViewById(R.id.imgPathIcon);
            iv.setImageResource(poi.getCategory().getIconResourceId());

            LinearLayout linla = findViewById(R.id.layoutPoiPaths);
            Path[] paths = poi.getPathArray();
            if (paths != null && paths.length > 0) {
                for (Path ftpu : paths) {
                    ImageView ivi = new ImageView(getApplicationContext());
                    ivi.setImageResource(R.drawable.ic_timeline_black_24dp);
                    ivi.setColorFilter(ftpu.getColor());
                    ivi.setOnClickListener(vi -> {
                        String pathid = ftpu.getId();
                        Intent i = new Intent(this, PathActivity.class);
                        i.putExtra("id", pathid);
                        startActivity(i);
                    });
                    /*
                    Button but = new Button(getApplicationContext());
                    but.setText(ftpu.getName());

                    but.setOnClickListener(vi -> {
                        String pathid = ftpu.getId();
                        Intent i = new Intent(this, PathActivity.class);
                        i.putExtra("id", pathid);
                        startActivity(i);
                    });

                    linla.addView(but);
                    */
                    linla.addView(ivi);
                }
            }




            Bundle mapViewBundle = null;
            if (savedInstanceState != null) {
                mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
            }

            mMapView = findViewById(R.id.mapView);
            mMapView.onCreate(mapViewBundle);


            mMapView.getMapAsync(gmap -> {

                mGmap = gmap;

                MarkerOptions mop = poi.getGoogleMarker();
                mGmap.moveCamera(CameraUpdateFactory.newLatLngZoom(mop.getPosition(), 16));
                Marker m = mGmap.addMarker(mop);

                 /*

                 //per disabilitare il click
                mGmap.setOnMarkerClickListener((marker)->{
                    return false; //true: evento consumato -> non mostra titolo e snippet
                });

                //per disabilitare infowindow

                mGmap.setOnInfoWindowClickListener(marker -> marker.hideInfoWindow());

                */




            });

            poi.getExtra().inflateView(findViewById(R.id.layoutExtra));

        }


        /*
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(android.R.layout.list_item_recyclerView, parent, false);

        View v = findViewById(R.id.poiCategoryData);

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


}
