package it.edu.liceosilvestri.map2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class PoisActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pois);
    }

    @Override
    protected void onStart() {
        super.onStart();

        BottomNavigator bnav = new BottomNavigator(this);
        bnav.startWorking();

    }

}
