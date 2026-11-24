package com.example.vehicle_care;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "vehiclecare.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_VEHICLES = "vehicles";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_CAR_NAME = "car_name";
    private static final String COLUMN_REGISTRATION = "registration";
    private static final String COLUMN_MODEL = "model";
    private static final String COLUMN_YEAR = "manufacturing_year";
    private static final String COLUMN_FUEL_TYPE = "fuel_type";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable = "CREATE TABLE " + TABLE_VEHICLES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CAR_NAME + " TEXT, " +
                COLUMN_REGISTRATION + " TEXT UNIQUE, " +
                COLUMN_MODEL + " TEXT, " +
                COLUMN_YEAR + " INTEGER, " +
                COLUMN_FUEL_TYPE + " TEXT)";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Database upgrade logic can be added when needed.
    }

    public boolean insertVehicle(String carName,
                                 String registration,
                                 String model,
                                 int year,
                                 String fuelType) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_CAR_NAME, carName);
        values.put(COLUMN_REGISTRATION, registration);
        values.put(COLUMN_MODEL, model);
        values.put(COLUMN_YEAR, year);
        values.put(COLUMN_FUEL_TYPE, fuelType);

        long result = db.insert(TABLE_VEHICLES, null, values);

        return result != -1;
    }

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

}