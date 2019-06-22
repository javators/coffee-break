package it.edu.liceosilvestri.map2;

import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.devs.vectorchildfinder.VectorChildFinder;

import it.edu.liceosilvestri.map2.data.Util;

public class BarAction {

    private final AppCompatActivity mCurrentActivity;
    private final String mCurrentActivityName;
    private View mCustomView;
    private ImageView mIcon;
    private TextView mTitle;
    private ImageButton mMapButton;


    public BarAction(AppCompatActivity a) {
        this.mCurrentActivity = a;
        this.mCurrentActivityName = a.getLocalClassName();
    }

    public void startWorking(String title, int iconResid, boolean showMapSwitcher){
        ActionBar abar = mCurrentActivity.getSupportActionBar();
        abar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        abar.setDisplayShowCustomEnabled(true);
        abar.setCustomView(R.layout.action_bar);
        this.mCustomView = abar.getCustomView();
        this.mIcon = (ImageView) mCustomView.findViewById(R.id.imgBarIcon);
        this.mTitle = (TextView) mCustomView.findViewById(R.id.txtBarTitle);
        this.mMapButton = (ImageButton)  mCustomView.findViewById(R.id.imgBarMap);


        Toolbar parent =(Toolbar) abar.getCustomView().getParent();
        parent.setPadding(0,0,0,0);//for tab otherwise give space in tab
        parent.setContentInsetsAbsolute(0,0);
        parent.getLayoutParams().height = Util.dpToPx(41);


        if (iconResid == 0)
            mIcon.setVisibility(View.INVISIBLE);
        else
            mIcon.setImageResource(iconResid);

        if (title == null)
            title = mCurrentActivity.getString(R.string.app_name);

        mTitle.setText(title);
        mTitle.setTextColor(mCurrentActivity.getColor(R.color.actionBarTitle));
        int iconColor = mCurrentActivity.getColor(R.color.actionBarIcons);

        VectorChildFinder vector = new VectorChildFinder(mCurrentActivity, R.drawable.ic_map_off_24dp, mMapButton);
        vector.findPathByName("map").setFillColor(iconColor);
        vector.findPathByName("rect").setFillColor(iconColor);

        if (showMapSwitcher) {

            Guideline mv = mCurrentActivity.findViewById(R.id.guidelineMapView);
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) mv.getLayoutParams();
            float initialMapGuidelinePercent = params.guidePercent;

            mMapButton.setOnClickListener(v -> {
                View btmNavigator = mCurrentActivity.findViewById(R.id.include);
                int navTop = btmNavigator.getTop();
                float parentHeight = ((View) btmNavigator.getParent()).getHeight();

                if (params.guidePercent == initialMapGuidelinePercent) {
                    params.guidePercent = navTop/parentHeight;
                    this.mMapButton.setImageResource(R.drawable.ic_map_on_24dp);
                    this.mMapButton.setColorFilter(iconColor);
                }
                else {
                    params.guidePercent = initialMapGuidelinePercent;
                    this.mMapButton.setColorFilter(null);
                    //Non c'è il setImageResource, poiché è implicito nel costruttore sottostante
                    VectorChildFinder vec = new VectorChildFinder(mCurrentActivity, R.drawable.ic_map_off_24dp, mMapButton);
                    vec.findPathByName("map").setFillColor(iconColor);
                    vec.findPathByName("rect").setFillColor(iconColor);
                }

                mv.setLayoutParams(params);

            });
        }
        else
            mMapButton.setVisibility(View.INVISIBLE);

    }

}
