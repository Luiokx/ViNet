package com.salvatierra.vinet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.ProgressBar;

import com.salvatierra.vinet.model.DataManager;

public class first_loading extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_loading);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ProgressBar load = (ProgressBar) findViewById(R.id.progressBarload);

        DataManager dbHelper = new DataManager(first_loading.this);

        dbHelper.syncWithMySQLServer();

        Intent window = new Intent(first_loading.this, MainNav.class);
        startActivity(window);
        finish();
    }
}