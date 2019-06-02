package it.edu.liceosilvestri.map2.data;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
}
