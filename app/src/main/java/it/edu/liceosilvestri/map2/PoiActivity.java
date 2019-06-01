package it.edu.liceosilvestri.map2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import it.edu.liceosilvestri.map2.data.Path;
import it.edu.liceosilvestri.map2.data.Poi;
import it.edu.liceosilvestri.map2.data.Pois;

public class PoiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi);


        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        Poi p = Pois.get(getApplicationContext()).getPoiBy(id);

        if (p == null) {
            //TODO manage
        }
        else {

            ((TextView) findViewById(R.id.txtName)).setText(p.getNameLong());
            ((TextView) findViewById(R.id.txtAddress)).setText(p.getAddress());

            LinearLayout linla = findViewById(R.id.layoutPoiPaths);
            Path[] paths = p.getPathArray();
            if (paths != null && paths.length > 0) {
                for (Path ftpu : paths) {
                    Button but = new Button(getApplicationContext());
                    but.setText(ftpu.getName());
                    but.setOnClickListener(vi -> {
                        String pathid = ftpu.getId();

                    });

                    linla.addView(but);
                }
            }

            MapView mv = findViewById(R.id.mapView);
            mv.getMapAsync(gmap -> {

                MarkerOptions mop = p.getGoogleMarker();
                gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(mop.getPosition(), 14));
                Marker m = gmap.addMarker(mop);

                //TODO: disabilitare infowindow

                //gmap.setOnInfoWindowClickListener(marker -> marker.hideInfoWindow());

            });

            p.getExtra().inflateView(findViewById(R.id.layoutExtra));

        }


        /*
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(android.R.layout.list_item_recyclerView, parent, false);

        View v = findViewById(R.id.poiCategoryData);

*/

    }



    @Override
    protected void onStart() {
        super.onStart();



    }
}
