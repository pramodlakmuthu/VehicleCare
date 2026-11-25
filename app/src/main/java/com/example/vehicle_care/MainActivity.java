
package com.example.vehicle_care;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button btnAddVehicle;
    private LinearLayout vehicleContainer;
    private LinearLayout btnServiceHistory;
    private LinearLayout btnMaintenanceReminders;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAddVehicle = findViewById(R.id.btnAddVehicle);
        vehicleContainer = findViewById(R.id.vehicleContainer);
        btnServiceHistory = findViewById(R.id.btnServiceHistory);
        btnMaintenanceReminders =
                findViewById(R.id.btnMaintenanceReminders);

        databaseHelper = new DatabaseHelper(this);

        // Open Add Vehicle screen
        btnAddVehicle.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    AddVehicleActivity.class
            );
            startActivity(intent);
        });

        // Open Service History screen
        btnServiceHistory.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    ServiceHistoryActivity.class
            );
            startActivity(intent);
        });

        // Open Maintenance Reminders screen
        btnMaintenanceReminders.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    MaintenanceRemindersActivity.class
            );
            startActivity(intent);
        });

        displayVehicles();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (databaseHelper != null && vehicleContainer != null) {
            displayVehicles();
        }
    }

    private void displayVehicles() {

        vehicleContainer.removeAllViews();

        ArrayList<String> vehicles =
                databaseHelper.getAllVehicles();

        if (vehicles.isEmpty()) {

            TextView emptyMessage = new TextView(this);
            emptyMessage.setText("No vehicles added yet.");
            emptyMessage.setTextSize(16);
            emptyMessage.setPadding(8, 12, 8, 12);

            vehicleContainer.addView(emptyMessage);

        } else {

            for (String vehicle : vehicles) {

                TextView vehicleText = new TextView(this);

                vehicleText.setText(vehicle);
                vehicleText.setTextSize(16);
                vehicleText.setPadding(20, 20, 20, 20);

                vehicleText.setBackgroundResource(
                        android.R.drawable.dialog_holo_light_frame
                );

                LinearLayout.LayoutParams params =
                        new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );

                params.setMargins(0, 8, 0, 8);

                vehicleContainer.addView(vehicleText, params);
            }
        }
    }
}
