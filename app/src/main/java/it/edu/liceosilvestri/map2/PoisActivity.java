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

import it.edu.liceosilvestri.map2.data.Categories;
import it.edu.liceosilvestri.map2.data.Category;
import it.edu.liceosilvestri.map2.data.Poi;

public class PoisActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pois);

        ExpandableListView listView = findViewById(R.id.expListViewPois);

        PoisAdapter adapter = new PoisAdapter(getApplicationContext());
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
        private Categories mCategories;

        public PoisAdapter(Context ctx) {
            this.mContext = ctx;
            this.mCategories = Categories.get(ctx);
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

            tvCategory.setText(getGroup(groupPosition).getName());
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

            TextView tvName = rowView.findViewById(R.id.txtPoiItemName);
            tvName.setText(getChild(groupPosition, childPosition).getName());

            return rowView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }
}
