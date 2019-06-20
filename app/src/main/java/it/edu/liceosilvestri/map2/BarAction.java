package it.edu.liceosilvestri.map2;

import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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
        mTitle.setTextColor(Color.BLACK);

        if (showMapSwitcher) {

            Guideline mv = (Guideline) mCurrentActivity.findViewById(R.id.guideline3);
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) mv.getLayoutParams();
            float initialMapGuidelinePercent = params.guidePercent;

            mMapButton.setOnClickListener(v -> {

                if (params.guidePercent == initialMapGuidelinePercent) {
                    params.guidePercent = 0.9f;
                    this.mMapButton.setColorFilter(Color.LTGRAY);
                }
                else {
                    params.guidePercent = initialMapGuidelinePercent;
                    this.mMapButton.setColorFilter(Color.BLACK);
                }

                mv.setLayoutParams(params);

                          });
        }
        else
            mMapButton.setVisibility(View.GONE);

    }

}
