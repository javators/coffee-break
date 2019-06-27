package it.edu.liceosilvestri.map2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Button btnStart = findViewById(R.id.btnSplashStart);

        btnStart.setOnClickListener( view -> {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);

//            SharedPreferences.Editor editor = getSharedPreferences("DATA", MODE_PRIVATE).edit();
//            editor.putBoolean("first_time", false);
//            editor.apply();
        });

//        SharedPreferences sharedPrefs = getSharedPreferences("DATA", MODE_PRIVATE);
//        boolean b = sharedPrefs.getBoolean("first_time", true);
//
//        if (!b) {
//            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
//            startActivity(intent);
//        }
    }
}
