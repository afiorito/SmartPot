package com.smartpot.botanicaljournal.Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Anthony on 2017-11-05.
 */

public class DBHandler extends SQLiteOpenHelper {

    private static final String TAG = "TAG";

    // DB Config
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "botanical_journal";

    // TABLES
    private static final String TABLE_PLANTS = "plants";
    private static final String TABLE_LAST_WATERED = "last_watered_times";
    private static final String TABLE_MOISTURE_LEVEL = "moisture_levels";

    // COMMON COLUMNS
    private static final String COL_ID = "id";
    private static final String COL_CREATED_AT = "created_at";

    // PLANT COLUMNS
    private static final String COL_PLANT_NAME = "name";
    private static final String COL_PLANT_PHYLO = "phylogeny";
    private static final String COL_PLANT_BIRTH_DATE = "birth_date";
    private static final String COL_PLANT_NOTES = "notes";
    private static final String COL_PLANT_IMAGE = "image";
    private static final String COL_PLANT_POT_ID = "potId";

    // FOREIGN KEY FOR LAST_WATERED & MOISTURE_LEVEL
    private static final String FK_CONSTRAINT_PLANTS = "fk_plants";
    private static final String COL_PLANT_ID = "plant_id";

    // LAST WATERED COLUMNS
    private static final String COL_WATERED_VALUE = "value";

    // MOISTURE LEVEL COLUMNS
    private static final String COL_MOISTURE_VALUE = "value";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i(TAG, "CREATE");
        String createPlantsTable =  "CREATE TABLE " + TABLE_PLANTS + "("
                + COL_ID + " INTEGER PRIMARY KEY,"
                + COL_PLANT_NAME + " TEXT,"
                + COL_PLANT_PHYLO + " TEXT,"
                + COL_PLANT_BIRTH_DATE + " DATETIME,"
                + COL_PLANT_NOTES + " TEXT,"
                + COL_PLANT_IMAGE + " TEXT,"
                + COL_PLANT_POT_ID + " TEXT,"
                + COL_CREATED_AT + " DATETIME DEFAULT (strftime('%s', 'now'))"
                + ");";

        String createLastWateredTable =     "CREATE TABLE " + TABLE_LAST_WATERED + "("
                + COL_ID + " INTEGER PRIMARY KEY,"
                + COL_PLANT_ID + " INTEGER,"
                + COL_WATERED_VALUE + " DATETIME,"
                + COL_CREATED_AT + " DATETIME DEFAULT (strftime('%s', 'now')),"
                + "CONSTRAINT " + FK_CONSTRAINT_PLANTS
                + " FOREIGN KEY(" + COL_PLANT_ID + ")"
                + " REFERENCES " + TABLE_PLANTS + "(" + COL_ID + ")"
                + " ON DELETE CASCADE"
                + ");";

        String createMoistureLevelTable =     "CREATE TABLE " + TABLE_MOISTURE_LEVEL + "("
                + COL_ID + " INTEGER PRIMARY KEY,"
                + COL_PLANT_ID + " INTEGER,"
                + COL_MOISTURE_VALUE + " DATETIME,"
                + COL_CREATED_AT + " DATETIME DEFAULT (strftime('%s', 'now')),"
                + "CONSTRAINT " + FK_CONSTRAINT_PLANTS
                + " FOREIGN KEY(" + COL_PLANT_ID + ")"
                + " REFERENCES " + TABLE_PLANTS + "(" + COL_ID + ")"
                + " ON DELETE CASCADE"
                + ");";

        sqLiteDatabase.execSQL(createPlantsTable);
        sqLiteDatabase.execSQL(createLastWateredTable);
        sqLiteDatabase.execSQL(createMoistureLevelTable);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys = ON;");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PLANTS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_LAST_WATERED);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_MOISTURE_LEVEL);

        onCreate(sqLiteDatabase);
    }

    /**
     * Add a plant to the database
     * @param plant the plant to be added
     * @return the id of the plant
     */
    public long addPlant(Plant plant) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COL_PLANT_NAME, plant.getName());
        cv.put(COL_PLANT_PHYLO, plant.getPhylogeny());
        cv.put(COL_PLANT_IMAGE, plant.getImagePath());
        if(plant.getBirthDate() != null) {
            cv.put(COL_PLANT_BIRTH_DATE, plant.getBirthDate().getTime());
        }
        cv.put(COL_PLANT_NOTES, plant.getNotes());
        cv.put(COL_PLANT_POT_ID, plant.getPotId());
        long id = db.insert(TABLE_PLANTS, COL_PLANT_BIRTH_DATE, cv);

        db.close();

        return id;
    }

    public void updatePlant(Plant plant) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COL_PLANT_NAME, plant.getName());
        cv.put(COL_PLANT_PHYLO, plant.getPhylogeny());
        cv.put(COL_PLANT_IMAGE, plant.getImagePath());
        if(plant.getBirthDate() != null) {
            cv.put(COL_PLANT_BIRTH_DATE, plant.getBirthDate().getTime());
        }
        cv.put(COL_PLANT_NOTES, plant.getNotes());
        cv.put(COL_PLANT_POT_ID, plant.getPotId());

        db.update(TABLE_PLANTS, cv, COL_ID + " = ?", new String[] {String.valueOf(plant.getId())});
    }

    /**
     * Get all the plants in the SQLite database
     * @return an ArrayList of plants from the database
     */
    public ArrayList<Plant> getPlants() {
        SQLiteDatabase db = getReadableDatabase();

        ArrayList<Plant> plants = new ArrayList<>();
        String queryCourses = "SELECT * FROM " + TABLE_PLANTS;

        Cursor c = db.rawQuery(queryCourses, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            long id = c.getLong(c.getColumnIndex(COL_ID));
            String name = c.getString(c.getColumnIndex(COL_PLANT_NAME));
            String phylogeny = c.getString(c.getColumnIndex(COL_PLANT_PHYLO));
            long date = c.getLong(c.getColumnIndex(COL_PLANT_BIRTH_DATE));
            Date birthDate = date == 0 ? null : new Date(date);
            String notes = c.getString(c.getColumnIndex(COL_PLANT_NOTES));
            Date lastWatered = getMostRecentLastWateredValue(id);
            int moistureLevel = getMostRecentMoistureValue(id);
            String imagePath = c.getString(c.getColumnIndex(COL_PLANT_IMAGE));
            String potId = c.getString(c.getColumnIndex(COL_PLANT_POT_ID));
            plants.add(new Plant(id, name, phylogeny, birthDate, notes, lastWatered, moistureLevel, imagePath, potId));
            c.moveToNext();
        }
        c.close();
        db.close();

        return plants;

    }

    private int getMostRecentMoistureValue(long id) {
        SQLiteDatabase db = getReadableDatabase();
        int moistureLevel = -1;

        String query = "SELECT id, value, MAX(created_at) as created_at FROM " + TABLE_MOISTURE_LEVEL
                + " WHERE plant_id = " + id
                + ";";

        Cursor c = db.rawQuery(query, null);

        if(c.moveToFirst()) {
            moistureLevel = c.getInt(c.getColumnIndex(COL_MOISTURE_VALUE));
        }

        c.close();
        db.close();

        return moistureLevel;

    }

    private Date getMostRecentLastWateredValue(long id) {
        SQLiteDatabase db = getReadableDatabase();
        Date lastWatered = null;

        String query = "SELECT id, value, MAX(created_at) as created_at FROM " + TABLE_LAST_WATERED
                + " WHERE plant_id = " + id
                + ";";

        Cursor c = db.rawQuery(query, null);

        if(c.moveToFirst()) {
            long date = c.getLong(c.getColumnIndex(COL_WATERED_VALUE));
            if (date > 0)
                lastWatered = new Date(date);
        }

        c.close();
        db.close();

        return lastWatered;
    }

    public void deletePlant(long id) {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("DELETE FROM " + TABLE_PLANTS + " WHERE " + COL_ID + " = " + id);
        db.close();
    }


    public void addMoistureLevelForPlant(Plant plant, int value) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COL_MOISTURE_VALUE, value);
        cv.put(COL_PLANT_ID, plant.getId());
        db.insert(TABLE_MOISTURE_LEVEL, null, cv);

        db.close();
    }

    public void addLastWateredTimeForPlant(Plant plant, Date date) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COL_WATERED_VALUE, date.getTime());
        cv.put(COL_PLANT_ID, plant.getId());
        Log.i("TAG", "CVVVV " + cv.getAsLong(COL_WATERED_VALUE) + "");
        db.insert(TABLE_LAST_WATERED, null, cv);
        Log.i("TAG", "ADDED: " + date.getTime());

        db.close();
    }

}
