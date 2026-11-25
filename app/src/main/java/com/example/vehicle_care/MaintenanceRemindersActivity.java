
package com.example.vehicle_care;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class MaintenanceRemindersActivity extends AppCompatActivity {

    Spinner spReminderVehicle;
    EditText etReminderTitle, etReminderDate;
    EditText etReminderDescription;
    Button btnSaveReminder;
    LinearLayout reminderContainer;

    DatabaseHelper databaseHelper;

    ArrayList<Integer> vehicleIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_reminders);

        spReminderVehicle = findViewById(R.id.spReminderVehicle);
        etReminderTitle = findViewById(R.id.etReminderTitle);
        etReminderDate = findViewById(R.id.etReminderDate);
        etReminderDescription =
                findViewById(R.id.etReminderDescription);

        btnSaveReminder = findViewById(R.id.btnSaveReminder);
        reminderContainer = findViewById(R.id.reminderContainer);

        databaseHelper = new DatabaseHelper(this);

        loadVehicles();
        displayReminders();

        etReminderDate.setOnClickListener(v -> showDatePicker());

        btnSaveReminder.setOnClickListener(v -> saveReminder());
    }

    // Load registered vehicles
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

        spReminderVehicle.setAdapter(adapter);

        if (vehicleNames.isEmpty()) {

            btnSaveReminder.setEnabled(false);

            Toast.makeText(this,
                    "Please add a vehicle first.",
                    Toast.LENGTH_LONG).show();
        }
    }

    // Show date picker
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

                            etReminderDate.setText(date);
                        },
                        year,
                        month,
                        day
                );

        datePickerDialog.show();
    }

    // Save reminder
    private void saveReminder() {

        if (vehicleIds == null || vehicleIds.isEmpty()) {
            Toast.makeText(this,
                    "Please add a vehicle first.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        String title =
                etReminderTitle.getText().toString().trim();

        String date =
                etReminderDate.getText().toString().trim();

        String description =
                etReminderDescription.getText().toString().trim();

        if (title.isEmpty() || date.isEmpty()) {

            Toast.makeText(this,
                    "Please enter reminder title and date.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedPosition =
                spReminderVehicle.getSelectedItemPosition();

        if (selectedPosition < 0 ||
                selectedPosition >= vehicleIds.size()) {

            Toast.makeText(this,
                    "Please select a vehicle.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        int vehicleId = vehicleIds.get(selectedPosition);

        boolean isInserted = databaseHelper.insertReminder(
                vehicleId,
                title,
                date,
                description
        );

        if (isInserted) {

            Toast.makeText(this,
                    "Reminder saved successfully!",
                    Toast.LENGTH_SHORT).show();

            etReminderTitle.setText("");
            etReminderDate.setText("");
            etReminderDescription.setText("");

            displayReminders();

        } else {

            Toast.makeText(this,
                    "Unable to save reminder.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    // Display saved reminders
    private void displayReminders() {

        reminderContainer.removeAllViews();

        ArrayList<String> reminders =
                databaseHelper.getAllReminders();

        if (reminders.isEmpty()) {

            TextView emptyMessage = new TextView(this);
            emptyMessage.setText("No reminders available.");
            emptyMessage.setTextSize(16);

            reminderContainer.addView(emptyMessage);

        } else {

            for (String reminder : reminders) {

                TextView reminderText = new TextView(this);

                reminderText.setText(reminder);
                reminderText.setTextSize(16);
                reminderText.setPadding(20, 20, 20, 20);

                reminderText.setBackgroundResource(
                        android.R.drawable.dialog_holo_light_frame
                );

                LinearLayout.LayoutParams params =
                        new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );

                params.setMargins(0, 8, 0, 8);

                reminderContainer.addView(reminderText, params);
            }
        }
    }
}
