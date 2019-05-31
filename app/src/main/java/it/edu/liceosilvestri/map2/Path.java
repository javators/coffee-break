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

        public Segment(){

        }

    }

    public String getName() {
        return mName;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getDescriptionLong() {
        return mDescriptionLong;
    }

    public String getDuration() {
        return mDuration;
    }

    public String getLength() {
        return mLength;
    }

    public String[] getSuitableFor() {
        return mSuitableFor;
    }

    public String[] getPois() {
        return mPois;
    }

    public Segment[] getSegments() {
        return mSegments;
    }

    public String getId() {
        return id;
    }
}
