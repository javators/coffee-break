package it.edu.liceosilvestri.map2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class BottomNavigator {

    private final Activity mCurrentActivity;
    private final String mCurrentActivityName;

    public BottomNavigator (Activity a) {
        this.mCurrentActivity = a;
        this.mCurrentActivityName = a.getLocalClassName();
    }


    public void startWorking(){
        fillItem();
        setClickListeners();
    }


    private void setClickListeners() {
        ImageButton ibnHome = mCurrentActivity.findViewById(R.id.imgBtnNavHome);
        ImageButton ibnPaths = mCurrentActivity.findViewById(R.id.imgBtnNavPaths);
        ImageButton ibnPois = mCurrentActivity.findViewById(R.id.imgBtnNavPois);
        ImageButton ibnCredits = mCurrentActivity.findViewById(R.id.imgBtnNavCredits);


        View.OnClickListener ocl = item -> {
             switch(item.getId()) {
                 case R.id.imgBtnNavHome:
                     if (!mCurrentActivityName.equals("HomeActivity")) {
                         Intent intentHome = new Intent(mCurrentActivity, HomeActivity.class);
                         mCurrentActivity.startActivity(intentHome);
                     }
                     break;
                 case R.id.imgBtnNavPaths:
                     if (!mCurrentActivityName.equals("PathsActivity")) {
                         Intent intentPaths = new Intent(mCurrentActivity, PathsActivity.class);
                         mCurrentActivity.startActivity(intentPaths);
                     }
                     break;
                 case R.id.imgBtnNavPois:
                     if (!mCurrentActivityName.equals("PoisActivity")) {
                         Intent intentPois = new Intent(mCurrentActivity, PoisActivity.class);
                         mCurrentActivity.startActivity(intentPois);
                     }
                     break;
                 case R.id.imgBtnNavCredits:
                     if (!mCurrentActivityName.equals("CreditsActivity")) {
                         Intent intentCredits = new Intent(mCurrentActivity, CreditsActivity.class);
                         mCurrentActivity.startActivity(intentCredits);
                     }
                    break;
                default:
                    //none
            }
          };


        ibnHome.setOnClickListener(ocl);
        ibnPaths.setOnClickListener(ocl);
        ibnPois.setOnClickListener(ocl);
        ibnCredits.setOnClickListener(ocl);


    }

    private void fillItem() {
        switch(mCurrentActivityName){
            case "HomeActivity":
                markNavSelected(mCurrentActivity.findViewById(R.id.imgBtnNavHome), mCurrentActivity.findViewById(R.id.txtNavHome));
                break;
            case "PathsActivity":
                markNavSelected(mCurrentActivity.findViewById(R.id.imgBtnNavPaths), mCurrentActivity.findViewById(R.id.txtNavPaths));
                break;
            case "PathActivity":
                markNavSelected(mCurrentActivity.findViewById(R.id.imgBtnNavPaths), mCurrentActivity.findViewById(R.id.txtNavPaths));
                break;
            case "PoisActivity":
                markNavSelected(mCurrentActivity.findViewById(R.id.imgBtnNavPois), mCurrentActivity.findViewById(R.id.txtNavPois));
                break;
            case "PoiActivity":
                markNavSelected(mCurrentActivity.findViewById(R.id.imgBtnNavPois), mCurrentActivity.findViewById(R.id.txtNavPois));
                break;
            case "CreditsActivity":
                markNavSelected(mCurrentActivity.findViewById(R.id.imgBtnNavCredits), mCurrentActivity.findViewById(R.id.txtNavCredits));
                break;
        }

    }

    private void markNavSelected(ImageButton imgBtn, TextView textView) {
        textView.setTypeface(null, Typeface.BOLD);

    }


}
