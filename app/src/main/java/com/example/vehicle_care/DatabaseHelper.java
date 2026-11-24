
package com.example.vehicle_care;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "vehiclecare.db";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_VEHICLES = "vehicles";
    private static final String TABLE_SERVICES = "service_records";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createVehiclesTable =
                "CREATE TABLE vehicles (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "car_name TEXT, " +
                        "registration TEXT UNIQUE, " +
                        "model TEXT, " +
                        "manufacturing_year INTEGER, " +
                        "fuel_type TEXT)";

        db.execSQL(createVehiclesTable);

        createServiceTable(db);
    }

    private void createServiceTable(SQLiteDatabase db) {

        String createServicesTable =
                "CREATE TABLE service_records (" +
                        "service_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "vehicle_id INTEGER, " +
                        "service_date TEXT, " +
                        "service_type TEXT, " +
                        "service_cost REAL, " +
                        "description TEXT, " +
                        "FOREIGN KEY(vehicle_id) REFERENCES vehicles(id) " +
                        "ON DELETE CASCADE)";

        db.execSQL(createServicesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,
                          int oldVersion,
                          int newVersion) {

        if (oldVersion < 2) {
            createServiceTable(db);
        }
    }

    // Insert vehicle
    public boolean insertVehicle(String carName,
                                 String registration,
                                 String model,
                                 int year,
                                 String fuelType) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("car_name", carName);
        values.put("registration", registration);
        values.put("model", model);
        values.put("manufacturing_year", year);
        values.put("fuel_type", fuelType);

        long result = db.insert(TABLE_VEHICLES, null, values);

        return result != -1;
    }

    // Get all vehicles for dashboard
    public ArrayList<String> getAllVehicles() {

        ArrayList<String> vehicleList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM vehicles ORDER BY id DESC",
                null
        );

        if (cursor.moveToFirst()) {
            do {
                String carName = cursor.getString(1);
                String registration = cursor.getString(2);
                String model = cursor.getString(3);
                String year = cursor.getString(4);
                String fuelType = cursor.getString(5);

                String vehicleDetails =
                        carName + "\n" +
                                "Registration: " + registration + "\n" +
                                "Model: " + model + "\n" +
                                "Year: " + year + "\n" +
                                "Fuel: " + fuelType;

                vehicleList.add(vehicleDetails);

            } while (cursor.moveToNext());
        }

        cursor.close();

        return vehicleList;
    }

    // Get vehicle names for service screen
    public ArrayList<String> getVehicleNames() {

        ArrayList<String> vehicleNames = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT car_name, registration FROM vehicles ORDER BY id DESC",
                null
        );

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);
                String registration = cursor.getString(1);

                vehicleNames.add(name + " - " + registration);

            } while (cursor.moveToNext());
        }

        cursor.close();

        return vehicleNames;
    }

    // Get vehicle IDs
    public ArrayList<Integer> getVehicleIds() {

        ArrayList<Integer> vehicleIds = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT id FROM vehicles ORDER BY id DESC",
                null
        );

        if (cursor.moveToFirst()) {
            do {
                vehicleIds.add(cursor.getInt(0));
            } while (cursor.moveToNext());
        }

        cursor.close();

        return vehicleIds;
    }

    // Insert service record
    public boolean insertService(int vehicleId,
                                 String serviceDate,
                                 String serviceType,
                                 double serviceCost,
                                 String description) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("vehicle_id", vehicleId);
        values.put("service_date", serviceDate);
        values.put("service_type", serviceType);
        values.put("service_cost", serviceCost);
        values.put("description", description);

        long result = db.insert(TABLE_SERVICES, null, values);

        return result != -1;
    }
    // Get all service records
    public ArrayList<String> getAllServiceRecords() {

        ArrayList<String> serviceList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT vehicles.car_name, vehicles.registration, " +
                        "service_records.service_date, " +
                        "service_records.service_type, " +
                        "service_records.service_cost, " +
                        "service_records.description " +
                        "FROM service_records " +
                        "INNER JOIN vehicles " +
                        "ON service_records.vehicle_id = vehicles.id " +
                        "ORDER BY service_records.service_id DESC",
                null
        );

        if (cursor.moveToFirst()) {
            do {
                String carName = cursor.getString(0);
                String registration = cursor.getString(1);
                String date = cursor.getString(2);
                String type = cursor.getString(3);
                String cost = cursor.getString(4);
                String description = cursor.getString(5);

                String serviceDetails =
                        carName + " (" + registration + ")\n" +
                                "Date: " + date + "\n" +
                                "Service: " + type + "\n" +
                                "Cost: " + cost + "\n" +
                                "Description: " + description;

                serviceList.add(serviceDetails);

            } while (cursor.moveToNext());
        }

        cursor.close();

        return serviceList;
    }
}
