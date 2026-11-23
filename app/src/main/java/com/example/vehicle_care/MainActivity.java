
package com.example.vehicle_care;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Declare Add Vehicle button
    private Button btnAddVehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Connect Java with XML layout
        setContentView(R.layout.activity_main);

        // Connect button using its XML ID
        btnAddVehicle = findViewById(R.id.btnAddVehicle);

        // Handle Add Vehicle button click
        btnAddVehicle.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,
                    AddVehicleActivity.class);
            startActivity(intent);
        });
    }
}
