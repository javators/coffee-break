package it.edu.liceosilvestri.map2.data;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.Iterator;



public class Paths implements Iterable<Path> {

    private static Paths __paths;
    private Path mPathArray[];

    private CoordinateGroup mBounds;

    private Paths(Context ctx){

    }

    private void load(Context ctx) {
        int i;
        String files[];

        AssetManager am = ctx.getAssets();
        try {
            //conta  e carica i path
            files = am.list("data/paths");

            if (files.length > 0) {
                mPathArray = new Path[files.length];
                i=0;
                mBounds = new CoordinateGroup();

                for (String s : files) {
                    int pos = s.indexOf(".");
                    if (pos > 0) {
                        String pathid = s.substring(0, pos);
                        Path p = new Path(pathid, ctx);
                        p.load(ctx);
                        mPathArray[i++] = p;
                        mBounds.addPoint(p.getBounds().getRectangle().southwest.latitude,  p.getBounds().getRectangle().southwest.longitude);
                        mBounds.addPoint(p.getBounds().getRectangle().northeast.latitude,  p.getBounds().getRectangle().northeast.longitude);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    public static Paths get(Context ctx) {
        if (__paths == null) {
            __paths = new Paths(ctx);
            __paths.load(ctx);
        }

        return __paths;
    }

    public Path getPathAt(int index) {
        if (mPathArray != null && (index >= 0 && index < mPathArray.length))
            return mPathArray[index];
        else
            return null;
    }

    public Path getPathBy(String id) {
        for (Path p : mPathArray) {
            if (p.getId().equals(id))
                return p;
        }
        return null;
    }


    public int getLength(){
        return mPathArray==null ? 0 : mPathArray.length;
    }

    public CoordinateGroup getBounds() {
        return mBounds;
    }

    @NonNull
    @Override
    public Iterator<Path> iterator() {
        return new PathIterator();
    }


    public class PathIterator implements Iterator<Path> {

        private int mIndex=-1;

        @Override
        public boolean hasNext() {
            if (mPathArray!=null && mIndex < mPathArray.length-1)
                return true;
            else
                return false;
        }

        @Override
        public Path next() {
            if (mPathArray!=null &&mIndex < mPathArray.length-1)
                return mPathArray[++mIndex];
            else
                return null;
        }
    }
}