package it.edu.liceosilvestri.map2.data;

import android.content.Context;
import android.support.annotation.NonNull;

public class AppDatabase {
    private static volatile AppDatabase _instance;
    private static volatile Context _context;

    private AppDatabase(@NonNull Context context) {
        _context = context.getApplicationContext();
    }

    public static AppDatabase get(@NonNull Context context) {
        AppDatabase localInstance = _instance;
        if (localInstance == null) {
            synchronized(AppDatabase.class) {
                localInstance = _instance;
                if (localInstance == null) {
                    _instance = localInstance = new AppDatabase(context);
                }
            }
        }
        return localInstance;
    }

    public static Context getContext() {
        if (_instance == null)
            throw new RuntimeException("AppDatabase not initialized");
        return _context;
    }
}
