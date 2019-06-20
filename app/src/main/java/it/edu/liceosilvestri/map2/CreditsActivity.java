package it.edu.liceosilvestri.map2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.TextView;

public class CreditsActivity extends AppCompatActivity {
    private int mCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        findViewById(R.id.imageView4).setOnClickListener((view) -> {
            if (mCount == 7)
                startActivity(new Intent(getApplicationContext(), ValidatorActivity.class));
            else
                mCount++;
        });

        String text = getString(R.string.credits2);
        ((TextView) findViewById(R.id.textView7)).setText(Html.fromHtml(text));
    }


    @Override
    protected void onStart() {
        super.onStart();

        BottomNavigator bnav = new BottomNavigator(this);
        bnav.startWorking();

    }

}
