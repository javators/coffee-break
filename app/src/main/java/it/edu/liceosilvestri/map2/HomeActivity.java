package it.edu.liceosilvestri.map2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import it.edu.liceosilvestri.map2.data.AppDatabase;
import it.edu.liceosilvestri.map2.data.Path;
import it.edu.liceosilvestri.map2.data.Paths;
import it.edu.liceosilvestri.map2.data.Poi;
import it.edu.liceosilvestri.map2.data.Pois;

public class HomeActivity extends AppCompatActivity {

    private ArrayList<Path> mPathsFound;
    private ArrayList<Poi> mPoisFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //start the database
        AppDatabase.get(this);

//        Button btn = findViewById(R.id.btnOpen);
//        btn.setOnClickListener(v -> {
//            String id = ((EditText) findViewById(R.id.editTextPoiId)).getText().toString();
//            Intent i = new Intent(getApplicationContext(), PoiActivity.class);
//            i.putExtra("id", id);
//            startActivity(i);
//        });
//
//        Button btn2 = findViewById(R.id.btnOpenPath);
//        btn2.setOnClickListener(v -> {
//            String id = ((EditText) findViewById(R.id.editTextPathId)).getText().toString();
//            Intent i = new Intent(getApplicationContext(), PathActivity.class);
//            i.putExtra("id", id);
//            startActivity(i);
//        });

        BarAction mybar = new BarAction(this);
        mybar.startWorking("APPortici", 0, false);


        mPathsFound = new ArrayList<>();
        mPoisFound = new ArrayList<>();

        ListView lv = findViewById(R.id.listViewResults);
        lv.setAdapter(this.new ResultAdapter());


        Button btn3 = findViewById(R.id.btnSearch);
        btn3.setOnClickListener( v -> {
            if (((EditText) findViewById(R.id.editTextSearch)).getText().toString().equals(""))
                return;

            mPathsFound = new ArrayList<>();
            mPoisFound = new ArrayList<>();

            String txt = ((EditText) findViewById(R.id.editTextSearch)).getText().toString().toUpperCase();
            for (Path ph : Paths.get())
                if (ph.getName().toUpperCase().contains(txt) || ph.getDescription().toUpperCase().contains(txt) || ph.getDescriptionLong().toUpperCase().contains(txt))
                    mPathsFound.add(ph);

            for (Poi p : Pois.get())
                if (p.getNameLong().toUpperCase().contains(txt) || p.getDescription().toUpperCase().contains(txt))
                    mPoisFound.add(p);

            ResultAdapter ad = (ResultAdapter) lv.getAdapter();

            ad.notifyDataSetChanged();

            if (mPathsFound.size() + mPoisFound.size() == 0)
            {
                ((TextView) findViewById(R.id.txtNoItemFound)).setText("Nessun punto di interesse trovato");
            }
            else {
                ((TextView) findViewById(R.id.txtNoItemFound)).setText("");
            }

        });

        lv.setOnItemClickListener((adapterView, view, position, id) -> {

            if (position < mPathsFound.size()) {
                Path ph = mPathsFound.get(position);
                String pathid = ph.getId();

                Intent i = new Intent(HomeActivity.this, PathActivity.class);
                i.putExtra("id", pathid);
                HomeActivity.this.startActivity(i);
            }
            else {
                Poi p = mPoisFound.get(position-mPathsFound.size());
                String poiid = p.getId();

                Intent i = new Intent(HomeActivity.this, PoiActivity.class);
                i.putExtra("id", poiid);
                HomeActivity.this.startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        BottomNavigator bnav = new BottomNavigator(this);
        bnav.startWorking();

    }




    class ResultAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        public ResultAdapter() {
            super();
            inflater = (LayoutInflater) HomeActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mPathsFound.size() + mPoisFound.size();
        }

        @Override
        public Object getItem(int position) {
            if (position<mPathsFound.size())
                return mPathsFound.get(position);
            else
                return mPoisFound.get(position-mPathsFound.size());
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;

            if (view == null)
                view = inflater.inflate(R.layout.item_search_result , parent, false);

            if (position < mPathsFound.size()) {
                Path ph = mPathsFound.get(position);

                ((TextView) view.findViewById(R.id.txtName)).setText(ph.getName());

                ImageView iv = view.findViewById(R.id.imgBarIcon);
                iv.setImageResource(R.drawable.ic_timeline_24dp);
                iv.setColorFilter(ph.getColor());
            }
            else {
                Poi p = mPoisFound.get(position-mPathsFound.size());

                ((TextView) view.findViewById(R.id.txtName)).setText(p.getNameLong());

                ImageView iv = view.findViewById(R.id.imgBarIcon);
                iv.setImageResource(p.getCategory().getIconResourceId());
                iv.setColorFilter(Color.BLACK);
            }

            return view;

        }
    }

}
