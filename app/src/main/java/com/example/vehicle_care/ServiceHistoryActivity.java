
package com.example.vehicle_care;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class ServiceHistoryActivity extends AppCompatActivity {

    Spinner spServiceVehicle, spServiceType;
    EditText etServiceDate, etServiceCost, etServiceDescription;
    Button btnSaveService;

    DatabaseHelper databaseHelper;

    ArrayList<Integer> vehicleIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_history);

        // Connect XML components
        spServiceVehicle = findViewById(R.id.spServiceVehicle);
        spServiceType = findViewById(R.id.spServiceType);
        etServiceDate = findViewById(R.id.etServiceDate);
        etServiceCost = findViewById(R.id.etServiceCost);
        etServiceDescription =
                findViewById(R.id.etServiceDescription);

        btnSaveService = findViewById(R.id.btnSaveService);

        databaseHelper = new DatabaseHelper(this);
        displayServiceRecords();

        // Load registered vehicles
        loadVehicles();

        // Load service types
        loadServiceTypes();

        // Select service date
        etServiceDate.setOnClickListener(v -> showDatePicker());

        // Save service record
        btnSaveService.setOnClickListener(v -> saveService());
    }

    private void loadVehicles() {

        ArrayList<String> vehicleNames =
                databaseHelper.getVehicleNames();

        vehicleIds = databaseHelper.getVehicleIds();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                vehicleNames
        );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spServiceVehicle.setAdapter(adapter);

        if (vehicleNames.isEmpty()) {
            btnSaveService.setEnabled(false);

            Toast.makeText(this,
                    "Please add a vehicle first.",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void loadServiceTypes() {

        String[] serviceTypes = {
                "Full Service",
                "Oil Change",
                "Repair",
                "Tire Replacement",
                "Brake Service",
                "Other"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                serviceTypes
        );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spServiceType.setAdapter(adapter);
    }

    private void showDatePicker() {

        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog =
                new DatePickerDialog(
                        this,
                        (view, selectedYear,
                         selectedMonth, selectedDay) -> {

                            String date = String.format(
                                    java.util.Locale.US,
                                    "%04d-%02d-%02d",
                                    selectedYear,
                                    selectedMonth + 1,
                                    selectedDay
                            );

                            etServiceDate.setText(date);
                        },
                        year,
                        month,
                        day
                );

        datePickerDialog.show();
    }

    private void saveService() {

        if (vehicleIds.isEmpty()) {
            Toast.makeText(this,
                    "Please add a vehicle first.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        String serviceDate =
                etServiceDate.getText().toString().trim();

        String serviceCostText =
                etServiceCost.getText().toString().trim();

        String description =
                etServiceDescription.getText().toString().trim();

        String serviceType =
                spServiceType.getSelectedItem().toString();

        if (serviceDate.isEmpty() || serviceCostText.isEmpty()) {

            Toast.makeText(this,
                    "Please enter service date and cost.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        double serviceCost;

        try {
            serviceCost = Double.parseDouble(serviceCostText);

            if (!Double.isFinite(serviceCost) || serviceCost < 0) {
                Toast.makeText(this,
                        "Please enter a valid service cost.",
                        Toast.LENGTH_SHORT).show();
                return;
            }

        } catch (NumberFormatException e) {

            Toast.makeText(this,
                    "Invalid service cost.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedPosition =
                spServiceVehicle.getSelectedItemPosition();

        if (selectedPosition < 0 ||
                selectedPosition >= vehicleIds.size()) {
            Toast.makeText(this,
                    "Please select a vehicle.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        int vehicleId = vehicleIds.get(selectedPosition);

        boolean isInserted = databaseHelper.insertService(
                vehicleId,
                serviceDate,
                serviceType,
                serviceCost,
                description
        );

        if (isInserted) {

            Toast.makeText(this,
                    "Service record saved successfully!",
                    Toast.LENGTH_SHORT).show();

            finish();

        } else {

            Toast.makeText(this,
                    "Unable to save service record.",
                    Toast.LENGTH_SHORT).show();
        }
    }
    private void displayServiceRecords() {

        LinearLayout serviceHistoryContainer =
                findViewById(R.id.serviceHistoryContainer);

        serviceHistoryContainer.removeAllViews();

        ArrayList<String> serviceRecords =
                databaseHelper.getAllServiceRecords();

        if (serviceRecords.isEmpty()) {

            TextView emptyMessage = new TextView(this);
            emptyMessage.setText("No service records available.");
            emptyMessage.setTextSize(16);

            serviceHistoryContainer.addView(emptyMessage);

        } else {

            for (String record : serviceRecords) {

                TextView recordText = new TextView(this);

                recordText.setText(record);
                recordText.setTextSize(16);
                recordText.setPadding(20, 20, 20, 20);
                recordText.setBackgroundResource(
                        android.R.drawable.dialog_holo_light_frame
                );

                LinearLayout.LayoutParams params =
                        new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );

                params.setMargins(0, 8, 0, 8);

                serviceHistoryContainer.addView(recordText, params);
            }
        }
    }
}
