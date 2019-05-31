package it.edu.liceosilvestri.map2;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PoisActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pois);

        ExpandableListView listView = findViewById(R.id.expListViewPois);

        //TODO Prendere i dati dall'XML dei POIs

        PoisAdapter adapter = new PoisAdapter(getApplicationContext(), new ArrayList<>(), new HashMap<>());
        listView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        BottomNavigator bnav = new BottomNavigator(this);
        bnav.startWorking();
    }

    private class PoisAdapter extends BaseExpandableListAdapter {
        private Context mContext;
        private List<String> mCategories;
        private HashMap<String, List<String>> mMap;

        //TODO Sostituire String con Poi
        public PoisAdapter(Context ctx, List<String> categories, HashMap<String, List<String>> map) {
            this.mContext = ctx;
            this.mCategories = categories;
            this.mMap = map;
        }

        @Override
        public int getGroupCount() {
            return mCategories.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return mMap.get(mCategories.get(groupPosition)).size();
        }

        @Override
        public String getGroup(int groupPosition) {
            return null;
        }

        @Override
        //TODO Dovrebbe essere Poi
        public String getChild(int groupPosition, int childPosition) {
            return mMap.get(mCategories.get(groupPosition)).get(childPosition);
        }

        @Override
        //TODO
        public long getGroupId(int groupPosition) {
            return 0;
        }

        @Override
        //TODO
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        //TODO Dopo aver implementato i due metodi sopra, questo dovrà restituire true
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

            tvCategory.setText(getGroup(groupPosition));
            switchVisible.setOnCheckedChangeListener((CompoundButton cButton, boolean b) -> {
                //TODO Cambiare visibilità del marker
            });

            return rowView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View rowView, ViewGroup parent) {
            if (rowView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowView = inflater.inflate(R.layout.item_poi, parent, false);
            }

            //TODO Aggiungere layout e inflating corrispondente

            return rowView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }
}
