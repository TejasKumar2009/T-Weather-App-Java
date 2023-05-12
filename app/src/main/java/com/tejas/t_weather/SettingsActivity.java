package com.tejas.t_weather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {
    Spinner spinnerTu;
    Button save_btn;
    ImageView back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Finding ids
        spinnerTu = findViewById(R.id.spinner_tu);
        save_btn = findViewById(R.id.save_btn);
        back_btn = findViewById(R.id.back_btn);

        // Getting Data from Intent
        Intent intent = getIntent();
        boolean is_celsius = intent.getBooleanExtra("is_celsius", true);

        String[] temprature_units_c = new String[] {"Celsius", "Fahrenheit"};
        String[] temprature_units_f = new String[] {"Fahrenheit", "Celsius"};

        // Temprature Units Settings
        if (is_celsius) {
            ArrayAdapter<String> spinnerTu_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, temprature_units_c);
            spinnerTu_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerTu.setAdapter(spinnerTu_adapter);
        }
        else {
            ArrayAdapter<String> spinnerTu_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, temprature_units_f);
            spinnerTu_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerTu.setAdapter(spinnerTu_adapter);
        }

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedText = spinnerTu.getSelectedItem().toString();

                SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
                SharedPreferences.Editor settingsEditor = sharedPreferences.edit();
                settingsEditor.putString("temprature_unit", selectedText);
                settingsEditor.apply();
                Toast.makeText(SettingsActivity.this, "Settings Saved! Please Re-open app to see the changes!", Toast.LENGTH_SHORT).show();
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsActivity.this.finish();
            }
        });

    }
}