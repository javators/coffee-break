package it.edu.liceosilvestri.map2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.constraint.Guideline;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BottomNavigator {

    private final Activity mCurrentActivity;
    private final String mCurrentActivityName;

    public BottomNavigator(Activity a) {
        this.mCurrentActivity = a;
        this.mCurrentActivityName = a.getLocalClassName();
    }


    public void startWorking(){
        fillItem();
        setClickArea();
        setClickListeners();
    }

    private void setClickArea() {
        Guideline gl = mCurrentActivity.findViewById(R.id.guideline);
        View parent = (View) mCurrentActivity.findViewById(R.id.imgBtnNavHome).getParent();

        parent.post(new EditTouchArea(gl, parent));
    }

    private void setClickListeners() {
        ImageButton ibnHome = mCurrentActivity.findViewById(R.id.imgBtnNavHome);
        ImageButton ibnPaths = mCurrentActivity.findViewById(R.id.imgBtnNavPaths);
        ImageButton ibnPois = mCurrentActivity.findViewById(R.id.imgBtnNavPois);
        ImageButton ibnCredits = mCurrentActivity.findViewById(R.id.imgBtnNavCredits);


        View.OnClickListener ocl = item -> {
             switch (item.getId()) {
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
            mCurrentActivity.overridePendingTransition(0, 0);
        };

        ibnHome.setOnClickListener(ocl);
        ibnPaths.setOnClickListener(ocl);
        ibnPois.setOnClickListener(ocl);
        ibnCredits.setOnClickListener(ocl);
    }

    private void fillItem() {
        switch(mCurrentActivityName){
            case "HomeActivity":
                markItemSelected(mCurrentActivity.findViewById(R.id.imgBtnNavHome), mCurrentActivity.findViewById(R.id.txtNavHome));
                break;
            case "PathsActivity":
                markItemSelected(mCurrentActivity.findViewById(R.id.imgBtnNavPaths), mCurrentActivity.findViewById(R.id.txtNavPaths));
                break;
            case "PathActivity":
                markItemSelected(mCurrentActivity.findViewById(R.id.imgBtnNavPaths), mCurrentActivity.findViewById(R.id.txtNavPaths));
                break;
            case "PoisActivity":
                markItemSelected(mCurrentActivity.findViewById(R.id.imgBtnNavPois), mCurrentActivity.findViewById(R.id.txtNavPois));
                break;
            case "PoiActivity":
                markItemSelected(mCurrentActivity.findViewById(R.id.imgBtnNavPois), mCurrentActivity.findViewById(R.id.txtNavPois));
                break;
            case "CreditsActivity":
                markItemSelected(mCurrentActivity.findViewById(R.id.imgBtnNavCredits), mCurrentActivity.findViewById(R.id.txtNavCredits));
                break;
        }

    }

    private void markItemSelected(ImageButton imgBtn, TextView textView) {
        int color = ContextCompat.getColor(mCurrentActivity, R.color.colorHighlighting);

        imgBtn.setColorFilter(color);
        textView.setTextColor(color);
        textView.setTypeface(null, Typeface.BOLD);
    }



    private class EditTouchArea implements Runnable {
        private final Guideline mGuideline;
        private final View mParent;
        private TouchDelegateComposite mTouchDelegate;

        public EditTouchArea(Guideline gl, View parent) {
            mGuideline = gl;
            mParent = parent;
            mTouchDelegate = new TouchDelegateComposite(parent);
        }

        private void editArea(ImageButton img, TextView txt) {
            Rect rect = new Rect();
            img.getHitRect(rect);
            rect.top -= img.getTop();
            rect.left -= (txt.getWidth() - img.getWidth())/2;
            rect.bottom += (mGuideline.getTop() - img.getWidth() - img.getTop());
            rect.right += (txt.getWidth() - img.getWidth())/2;
            mTouchDelegate.addDelegate(new TouchDelegate(rect, img));
        }

        public void run() {
            ImageButton ibnHome = mCurrentActivity.findViewById(R.id.imgBtnNavHome);
            ImageButton ibnPaths = mCurrentActivity.findViewById(R.id.imgBtnNavPaths);
            ImageButton ibnPois = mCurrentActivity.findViewById(R.id.imgBtnNavPois);
            ImageButton ibnCredits = mCurrentActivity.findViewById(R.id.imgBtnNavCredits);

            TextView txtHome = mCurrentActivity.findViewById(R.id.txtNavHome);
            TextView txtPaths = mCurrentActivity.findViewById(R.id.txtNavPaths);
            TextView txtPois = mCurrentActivity.findViewById(R.id.txtNavPois);
            TextView txtCredits = mCurrentActivity.findViewById(R.id.txtNavCredits);

            editArea(ibnHome, txtHome);
            editArea(ibnPaths, txtPaths);
            editArea(ibnPois, txtPois);
            editArea(ibnCredits, txtCredits);
            mParent.setTouchDelegate(mTouchDelegate);
        }
    }

    public static class TouchDelegateComposite extends TouchDelegate {
        private final List<TouchDelegate> mDelegates = new ArrayList<>();
        private static final Rect emptyRect = new Rect();

        public TouchDelegateComposite(View view) {
            super(emptyRect, view);
        }

        public void addDelegate(TouchDelegate delegate) {
            if (delegate != null) {
                mDelegates.add(delegate);
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            boolean res = false;
            float x = event.getX();
            float y = event.getY();
            for (TouchDelegate delegate : mDelegates) {
                event.setLocation(x, y);
                res = delegate.onTouchEvent(event) || res;
            }
            return res;
        }
    }
}
