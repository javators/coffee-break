package it.edu.liceosilvestri.map2.data;

import android.os.Bundle;

public enum MapLoadStatus {
    NOT_LOADED,
    WAITING_FOR_MAP,
    WAITING_FOR_LAYOUT,
    LAYOUT_AND_MAP_READY,
    LOADED;

    private static final String MAP_LOAD_STATUS_BUNDLE_KEY = "MapLoadStatusBundleKey";

    public MapLoadStatus nextAfterMapReadyEvent() {
        switch (this) {
            case NOT_LOADED:
                return WAITING_FOR_LAYOUT;
            case WAITING_FOR_MAP:
                return LAYOUT_AND_MAP_READY;
            default:
                //don't change
                return this;
        }
    }

    public MapLoadStatus nextAfterLayoutReadyEvent() {
        switch (this) {
            case NOT_LOADED:
                return WAITING_FOR_MAP;
            case WAITING_FOR_LAYOUT:
                return LAYOUT_AND_MAP_READY;
            default:
                //don't change
                return this;
        }
    }

    public boolean canLoad() {
        return this==LAYOUT_AND_MAP_READY;
    }

    public MapLoadStatus nextAfterLoaded() {
        return LOADED;
    }

    public static MapLoadStatus getInitialStatus(Bundle savedInstanceState) {
        int statOrd = 0;
        if (savedInstanceState != null)
            statOrd = savedInstanceState.getInt(MAP_LOAD_STATUS_BUNDLE_KEY, 0);
        return MapLoadStatus.values()[statOrd];
    }

    public void saveStatus(Bundle outState) {
        outState.putInt(MAP_LOAD_STATUS_BUNDLE_KEY, this.ordinal());
    }
}
