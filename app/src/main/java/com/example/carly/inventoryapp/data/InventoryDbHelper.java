package com.example.carly.inventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.carly.inventoryapp.data.InventoryContract.InventoryEntry;

public class InventoryDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = InventoryDbHelper.class.getSimpleName();

    /**
     * Database name
     */
    public static final String DATABASE_NAME = "inventory.db";

    /**
     * Database version (if the schema is changed, the database version must be incremented.)
     */
    public static final int DATABASE_VERSION = 1;

    /**
     * Constructor for a new instance of {@link InventoryDbHelper}.
     */
    public InventoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /** This is called when the database is created for the first time. */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a string using the SQL statement to create the table.
        String SQL_CREATE_INVENTORY_TABLE = "CREATE TABLE " + InventoryEntry.TABLE_NAME + " ("
                + InventoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + InventoryEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, "
                + InventoryEntry.COLUMN_PRODUCT_PRICE + " REAL NOT NULL DEFAULT 0.0, "
                + InventoryEntry.COLUMN_PRODUCT_QUANTITY + " INTEGER NOT NULL DEFAULT 0, "
                + InventoryEntry.COLUMN_SUPPLIER_NAME + " TEXT NOT NULL, "
                + InventoryEntry.COLUMN_SUPPLIER_PHONE + " INTEGER NOT NULL DEFAULT 0);";

        // Execute the SQL Statement
        db.execSQL(SQL_CREATE_INVENTORY_TABLE);
        Log.i(LOG_TAG, "Note: Inventory database created.");
    }

    /** This is called when the database is upgraded */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
