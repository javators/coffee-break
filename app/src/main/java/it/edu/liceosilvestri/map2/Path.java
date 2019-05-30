package it.edu.liceosilvestri.map2;

public class Path {
    private String mName;
    private String mDescription;
    private String mDescriptionLong;
    private String mDuration;
    private String mLength;
    private String[] mSuitableFor;
    private String[] mPois;
    private Segment[] mSegments;
    private String id;

    public Path(String id){
        this.id = id;
    }

    static public class Segment {

        float startCoordX, startCoordY, endCoordX, endCoord;
        String type;

    }
}
