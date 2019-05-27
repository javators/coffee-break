package it.edu.liceosilvestri.map2;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.BottomNavigationView;

public class BottomNavigationManager {

    private Activity mCurrentActivity;
    private String mCurrentActivityName;
    private BottomNavigationView mBottomNavigationView;

    public BottomNavigationManager(Activity a, BottomNavigationView btv){
        this.mBottomNavigationView = btv;
        this.mCurrentActivity = a;
        this.mCurrentActivityName = a.getLocalClassName();
    }

    public void startWorking(){
        fillItem();
        setClickListeners();
        setReclickListeners();
    }

    private void setReclickListeners() {
        mBottomNavigationView.setOnNavigationItemReselectedListener(item -> {
            switch(mCurrentActivityName){
                case "PathActivity":
                    Intent intentPaths = new Intent(mCurrentActivity, PathsActivity.class);
                    mCurrentActivity.startActivity(intentPaths);
                    break;
                case "PoiActivity":
                    Intent intentPois = new Intent(mCurrentActivity, PoisActivity.class);
                    mCurrentActivity.startActivity(intentPois);
                    break;
            }
        });

    }

    private void setClickListeners() {
        mBottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            boolean managed = true;
            switch(item.getItemId()) {
                case R.id.navigation_home:
                    Intent intentHome = new Intent(mCurrentActivity, HomeActivity.class);
                    mCurrentActivity.startActivity(intentHome);
                    break;
                case R.id.navigation_paths:
                    Intent intentPaths = new Intent(mCurrentActivity, PathsActivity.class);
                    mCurrentActivity.startActivity(intentPaths);
                    break;
                case R.id.navigation_pois:
                    Intent intentPois = new Intent(mCurrentActivity, PoisActivity.class);
                    mCurrentActivity.startActivity(intentPois);
                    break;
                case R.id.navigation_credits:
                    Intent intentCredits = new Intent(mCurrentActivity, CreditsActivity.class);
                    mCurrentActivity.startActivity(intentCredits);
                    break;
                default:
                    managed = false;
            }

            mBottomNavigationView.clearAnimation();
            return managed;
        });
    }

     private void fillItem() {
        switch(mCurrentActivityName){
            case "HomeActivity":
                mBottomNavigationView.setSelectedItemId(R.id.navigation_home);
                break;
            case "PathsActivity":
                mBottomNavigationView.setSelectedItemId(R.id.navigation_paths);
                break;
            case "PathActivity":
                mBottomNavigationView.setSelectedItemId(R.id.navigation_paths);
                break;
            case "PoisActivity":
                mBottomNavigationView.setSelectedItemId(R.id.navigation_pois);
                break;
            case "PoiActivity":
                mBottomNavigationView.setSelectedItemId(R.id.navigation_pois);
                break;
            case "creditsActivity":
                mBottomNavigationView.setSelectedItemId(R.id.navigation_credits);
                break;
        }
        mBottomNavigationView.refreshDrawableState();
    }
}
