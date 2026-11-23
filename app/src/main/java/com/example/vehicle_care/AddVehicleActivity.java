
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

        btnSaveVehicle.setOnClickListener(v -> {
            String carName = etCarName.getText().toString().trim();
            String registration = etRegistration.getText().toString().trim();
            String model = etModel.getText().toString().trim();
            String year = etYear.getText().toString().trim();

            if (carName.isEmpty() || registration.isEmpty()
                    || model.isEmpty() || year.isEmpty()) {

                Toast.makeText(this,
                        "Please fill in all fields",
                        Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this,
                        "Vehicle details entered successfully!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
