package com.example.carly.inventoryapp.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.ContentUris;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.carly.inventoryapp.data.InventoryContract.InventoryEntry;

/*
 * Content provider for Inventory app.
 */
public class InventoryProvider extends ContentProvider {

    /** Tag for log messages **/
    public static final String LOG_TAG = InventoryProvider.class.getSimpleName();

    /** Database helper object **/
    private InventoryDbHelper mDbHelper;

    /**
     * Initialize the provider and the database helper object.
     */
    @Override
    public boolean onCreate() {
        mDbHelper = new InventoryDbHelper(getContext());
        return true;
    }

    /**
     * Perform the query for the given URI. Use the given projection, selection, selection
     * arguments, and sort order.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                cursor = database.query(InventoryEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case PRODUCT_ID:
                selection = InventoryEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};

                cursor = database.query(InventoryEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        // Set notification URI on the Cursor so we know what content URI the Cursor was created for.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the cursor
        return cursor;
    }

    /**
     * Insert new data into the provider with the given ContentValues.
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return insertProduct(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**
     * Insert a new product into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertProduct(Uri uri, ContentValues values) {

        // Check that the name is not null
        String name = values.getAsString(InventoryEntry.COLUMN_PRODUCT_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Product requires a name.");
        }

        // Check that the price is not null
        Double price = values.getAsDouble(InventoryEntry.COLUMN_PRODUCT_PRICE);
        if (price == null) {
            throw new IllegalArgumentException("Product requires a price.");
        }

        // Check that the quantity is not null
        Integer quantity = values.getAsInteger(InventoryEntry.COLUMN_PRODUCT_QUANTITY);
        if (quantity == null) {
            throw new IllegalArgumentException("Product requires a quantity.");
        }

        // Check that the supplier name is not null
        String supplierName = values.getAsString(InventoryEntry.COLUMN_SUPPLIER_NAME);
        if (supplierName == null) {
            throw new IllegalArgumentException("Product requires a supplier name.");
        }

        // Check that the supplier phone is not null
        String supplierPhone = values.getAsString(InventoryEntry.COLUMN_SUPPLIER_PHONE);
        if (supplierPhone == null) {
            throw new IllegalArgumentException("Product requires a supplier phone number.");
        }

        // Get writable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Insert the new product with the given values
        long id = database.insert(InventoryEntry.TABLE_NAME, null, values);

        // If the ID is -1, then insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
        }

        // Notify all listeners that the data has changed for the pet content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Once the ID of the new row is known, return the new URI with the ID appended to the end of it.
        return ContentUris.withAppendedId(uri, id);
    }

    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return updateProduct(uri, contentValues, selection, selectionArgs);
            case PRODUCT_ID:
                // Extract the ID from the URI so you know which row to update.
                // Selection will be "_id=?" and selection args will be a String
                // array containing the actual ID
                selection = InventoryEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                return updateProduct(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    /**
     * Update product in the database with the given content values & apply the changes. Return the
     * number of rows that were successfully updated.
     */
    private int updateProduct(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // If the COLUMN_PRODUCT_NAME key is present, check that the value is not null.
        if (values.containsKey(InventoryEntry.COLUMN_PRODUCT_NAME)) {
            String name = values.getAsString(InventoryEntry.COLUMN_PRODUCT_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Product requires a name.");
            }
        }

        // If the COLUMN_PRODUCT_PRICE key is present, check that the value is not null.
        if (values.containsKey(InventoryEntry.COLUMN_PRODUCT_PRICE)) {
            Double price = values.getAsDouble(InventoryEntry.COLUMN_PRODUCT_PRICE);
            if (price == null) {
                throw new IllegalArgumentException("Product requires a price.");
            }
        }

        // If the COLUMN_PRODUCT_QUANTITY key is present, check that the value is not null.
        if (values.containsKey(InventoryEntry.COLUMN_PRODUCT_QUANTITY)) {
            Integer quantity = values.getAsInteger(InventoryEntry.COLUMN_PRODUCT_QUANTITY);
            if (quantity == null) {
                throw new IllegalArgumentException("Product requires a quantity.");
            }
        }

        // If the COLUMN_SUPPLIER_NAME key is present, check that the value is not null.
        if (values.containsKey(InventoryEntry.COLUMN_SUPPLIER_NAME)) {
            String supplierName = values.getAsString(InventoryEntry.COLUMN_SUPPLIER_NAME);
            if (supplierName == null) {
                throw new IllegalArgumentException("Product requires a supplier name.");
            }
        }

        // If the COLUMN_SUPPLIER_PHONE key is present, check that the value is not null.
        if (values.containsKey(InventoryEntry.COLUMN_SUPPLIER_PHONE)) {
            String supplierPhone = values.getAsString(InventoryEntry.COLUMN_SUPPLIER_PHONE);
            if (supplierPhone == null) {
                throw new IllegalArgumentException("Product supplier needs a phone number.");
            }
        }

        // If there are no values to update, then don't try to update the database.
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data.
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(InventoryEntry.TABLE_NAME, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the given
        // URI has changed.
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows updated.
        return rowsUpdated;
    }

    /**
     * Delete the data at the given selection and selection arguments
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Track the number of rows that were deleted
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(InventoryEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PRODUCT_ID:
                // Delete a single row given by the ID in the URI
                selection = InventoryEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(InventoryEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        // If 1 or more rows were deleted, then notify all listeners that the data at the given
        // URI has changed.
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows deleted
        return rowsDeleted;
    }

    /**
     * Returns the MIME type of data for the content URI
     */
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return InventoryEntry.CONTENT_LIST_TYPE;
            case PRODUCT_ID:
                return InventoryEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri + " with match " + match);
        }
    }

    /** URI matcher code for the content URI for the inventory table */
    private static final int PRODUCTS = 100;

    /** URI matcher code for the content URI for a single item in the inventory table */
    private static final int PRODUCT_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * Use NO_MATCH as the input in this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is to run the first time anything is called from this class.
    static {
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_PRODUCTS, PRODUCTS);
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_PRODUCTS + "/#", PRODUCT_ID);

    }

}
