
package com.example.vehicle_care;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddVehicleActivity extends AppCompatActivity {

    EditText etCarName, etRegistration, etModel, etYear;
    Spinner spFuelType;
    Button btnSaveVehicle;

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);

        etCarName = findViewById(R.id.etCarName);
        etRegistration = findViewById(R.id.etRegistration);
        etModel = findViewById(R.id.etModel);
        etYear = findViewById(R.id.etYear);

        spFuelType = findViewById(R.id.spFuelType);
        btnSaveVehicle = findViewById(R.id.btnSaveVehicle);

        databaseHelper = new DatabaseHelper(this);

        String[] fuelTypes = {
                "Petrol", "Diesel", "Hybrid", "Electric"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                fuelTypes
        );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spFuelType.setAdapter(adapter);

        btnSaveVehicle.setOnClickListener(v -> saveVehicle());
    }

    private void saveVehicle() {

        String carName = etCarName.getText().toString().trim();
        String registration = etRegistration.getText()
                .toString().trim().toUpperCase();
        String model = etModel.getText().toString().trim();
        String yearText = etYear.getText().toString().trim();

        String fuelType = spFuelType.getSelectedItem().toString();

        // Check empty fields
        if (carName.isEmpty() || registration.isEmpty()
                || model.isEmpty() || yearText.isEmpty()) {

            Toast.makeText(this,
                    "Please fill in all fields",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        int year;

        try {
            year = Integer.parseInt(yearText);

            if (year < 1886 || year > 2026) {
                Toast.makeText(this,
                        "Please enter a valid manufacturing year",
                        Toast.LENGTH_SHORT).show();
                return;
            }

        } catch (NumberFormatException e) {
            Toast.makeText(this,
                    "Please enter a valid year",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Save vehicle into SQLite database
        boolean isInserted = databaseHelper.insertVehicle(
                carName,
                registration,
                model,
                year,
                fuelType
        );

        if (isInserted) {

            Toast.makeText(this,
                    "Vehicle saved successfully!",
                    Toast.LENGTH_SHORT).show();

            finish();

        } else {

            Toast.makeText(this,
                    "Unable to save vehicle. Registration number may already exist.",
                    Toast.LENGTH_LONG).show();
        }
    }
}