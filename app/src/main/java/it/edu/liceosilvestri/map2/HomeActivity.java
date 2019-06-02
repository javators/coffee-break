package it.edu.liceosilvestri.map2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button btn = findViewById(R.id.btnOpenPoi);
        btn.setOnClickListener(v -> {
            String id = ((EditText) findViewById(R.id.editTextPoiId)).getText().toString();
            Intent i = new Intent(this, PoiActivity.class);
            i.putExtra("id", id);
            startActivity(i);
        });

        Button btn2 = findViewById(R.id.btnOpenPath);
        btn2.setOnClickListener(v -> {
            String id = ((EditText) findViewById(R.id.editTextPathId)).getText().toString();
            Intent i = new Intent(this, PathActivity.class);
            i.putExtra("id", id);
            startActivity(i);
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        BottomNavigator bnav = new BottomNavigator(this);
        bnav.startWorking();

    }

}
