package it.edu.liceosilvestri.map2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.edu.liceosilvestri.map2.data.XMLValidator;

public class ValidatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validator);

        BarAction mybar = new BarAction(this);
        mybar.startWorking(null, 0, false);

        Spinner spinnerFilename = findViewById(R.id.spinnerFilename);
        RadioGroup rgPath = findViewById(R.id.radioGroupValidator);
        Button btnValidate = findViewById(R.id.btnValidator);
        Button btnValidatePass = findViewById(R.id.btnValidatorPass);

        rgPath.setOnCheckedChangeListener((RadioGroup radioGroup, int id) -> {
            String path = "";

            switch (id) {
                case R.id.radioButtonPaths:
                    path = "data/paths";
                    break;
                case R.id.radioButtonPois:
                    path = "data/pois";
                    break;
            }

            String[] files;
            try {
                files = getAssets().list(path);
            }
            catch (IOException e) {
                files = new String[0];
                e.printStackTrace();
            }

            List<String> list = new ArrayList<>();
            Collections.addAll(list, files);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, list);
            spinnerFilename.setAdapter(adapter);
        });

        rgPath.check(R.id.radioButtonPaths);

        btnValidate.setOnClickListener((view) -> {
            validate(spinnerFilename, rgPath);
        });

        btnValidatePass.setOnClickListener((view) -> {
            if (spinnerFilename.getSelectedItemPosition()+1 < spinnerFilename.getAdapter().getCount())
                spinnerFilename.setSelection(spinnerFilename.getSelectedItemPosition()+1);
            else
                spinnerFilename.setSelection(0);

            validate(spinnerFilename, rgPath);
        });
    }

    private void validate(Spinner spinner, RadioGroup radioGroup) {
        String filename = spinner.getSelectedItem().toString();
        if (filename.contains(".xml"))
            filename = filename.substring(0, filename.indexOf(".xml"));

        String path = "";
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.radioButtonPaths:
                path = "data/paths";
                break;
            case R.id.radioButtonPois:
                path = "data/pois";
                break;
        }

        String res = XMLValidator.validateXML(getApplicationContext(), path, filename);
        TextView tvTitle = findViewById(R.id.txtValidatorFile);
        TextView tvRes   = findViewById(R.id.txtValidatorRes);

        if (res.equals("")) {
            tvTitle.setText("["  + filename + "]\nT'appost fra!");
            tvRes.setText("");
        }
        else {
            tvTitle.setText("["  + filename + "]\nErrore!");
            tvRes.setText(res);
        }
    }
}
