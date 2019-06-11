package it.edu.liceosilvestri.map2.data;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.annotation.DrawableRes;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

import it.edu.liceosilvestri.map2.R;

public class Util {
    public static boolean loadAssetIntoImageView(ImageView view, String assetFileName) {
        AssetManager am = view.getContext().getAssets();
        InputStream is = null;
        try{
            is = am.open(assetFileName);
        }catch(IOException e){
            e.printStackTrace();
        }

        Bitmap bitmap = BitmapFactory.decodeStream(is);
        view.setImageBitmap(bitmap);
        return true;
    }




    public static class ImagePager extends PagerAdapter {
        private Context context;
        private String[] mImageArray;

        public ImagePager(Context context, String[] imageArray) {
            this.context = context;
            this.mImageArray = imageArray;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_image, null);

            /*
            for (String filename : mImageArray) {
                ImageView imageView = view.findViewById(R.id.image);
                Util.loadAssetIntoImageView(imageView, filename);
            }
            */

            String filename = mImageArray[position];
            ImageView imageView = view.findViewById(R.id.image);
            Util.loadAssetIntoImageView(imageView, filename);

            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object view) {
            container.removeView((View) view);
        }

        @Override
        public int getCount() {
            return mImageArray.length;
        }

        /*
        Used to determine whether the page view is associated with object key returned by instantiateItem.
        Since here view only is the key we return view==object
        */
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return object == view;
        }

    }


    public static Bitmap createCustomMarkerSimple(Context context, @DrawableRes int resource) {

        View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_simple, null);

        ImageView markerImage = (ImageView) marker.findViewById(R.id.imgCategory);
        markerImage.setImageResource(resource);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        marker.setLayoutParams(new ViewGroup.LayoutParams(32, ViewGroup.LayoutParams.WRAP_CONTENT));
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        marker.draw(canvas);

        return bitmap;
    }

    public static Bitmap createCustomMarkerWithText(int layoutid, @DrawableRes int resource, String text) {

        Context context = AppDatabase.getContext();
        View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(layoutid, null);

        ImageView categoryImage = (ImageView) marker.findViewById(R.id.imgCategory);
        categoryImage.setImageResource(resource);
        ImageView markImage = (ImageView) marker.findViewById(R.id.imgMarker);
        markImage.setImageResource(R.drawable.ic_custom_place_black_24dp);

        TextView txt_name = (TextView)marker.findViewById(R.id.txtName);
        txt_name.setText(text);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        marker.setLayoutParams(new ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT));
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        marker.draw(canvas);

        return bitmap;
    }


}
