package com.example.carly.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carly.inventoryapp.data.InventoryContract.InventoryEntry;
import com.example.carly.inventoryapp.data.InventoryProvider;

/**
 * Displays the inventory list.
 */
public class InventoryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /** Tag for the log messages **/
    public static final String LOG_TAG = InventoryProvider.class.getSimpleName();

    private static final int INVENTORY_LOADER =0;

    InventoryCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InventoryActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        // Find the ListView which will be populated with the pet data
        ListView productListView = (ListView) findViewById(R.id.list);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        productListView.setEmptyView(emptyView);

        // Setup an Adapter to create a list item for each row of pet data in the Cursor.
        // There is no pet data yet (until the loader finishes) so pass in null for the Cursor.
        mCursorAdapter = new InventoryCursorAdapter(this, null);
        productListView.setAdapter(mCursorAdapter);

        // Setup item click listener
        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Create new intent to go to {@link EditorActivity}
                Intent intent = new Intent(InventoryActivity.this, EditorActivity.class);

                // Form the content URI that represents the specific product that was clicked on,
                Uri currentProductUri = ContentUris.withAppendedId(InventoryEntry.CONTENT_URI, id);

                // Set the URI on the data field of the intent
                intent.setData(currentProductUri);

                // Launch the {@link EditorActivity} to display the data for the current product.
                startActivity(intent);
            }
        });

        // Kick off the loader
        getLoaderManager().initLoader(INVENTORY_LOADER, null, this);
    }

    /**
     * Helper method to insert test data into the database.
     */
    private void insertProduct() {

        // Create ContentValues object, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(InventoryEntry.COLUMN_PRODUCT_NAME, "Shovel");
        values.put(InventoryEntry.COLUMN_PRODUCT_PRICE, 30.00);
        values.put(InventoryEntry.COLUMN_PRODUCT_QUANTITY, 10);
        values.put(InventoryEntry.COLUMN_SUPPLIER_NAME, "Dig Big Co.");
        values.put(InventoryEntry.COLUMN_SUPPLIER_PHONE, 111231234);

        Uri newUri = getContentResolver().insert(InventoryEntry.CONTENT_URI, values);

        Log.e(LOG_TAG, "NOTE: Product dummy data inserted.");
    }

    /**
     * Helper method to delete all products in the database
     */
    private void deleteAllProducts() {
        int rowsDeleted = getContentResolver().delete(InventoryEntry.CONTENT_URI, null, null);
        Log.v("InventoryActivity", rowsDeleted + " rows deleted from inventory database");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options & add menu item to app bar
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertProduct();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllProducts();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                InventoryEntry._ID,
                InventoryEntry.COLUMN_PRODUCT_NAME,
                InventoryEntry.COLUMN_PRODUCT_PRICE,
                InventoryEntry.COLUMN_PRODUCT_QUANTITY};

        // This loader will execute the ContentProvider's query method on a background thread.
        return new CursorLoader(this,        // Parent activity context
                InventoryEntry.CONTENT_URI,                // Provider content URI to query
                projection,                       // Columns to include in the resulting Cursor
                null,                      // No selection clause
                null,                   // No selection arguments
                null);                      // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Update with this new cursor containing updated pet data
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Callback called when the data needs to be deleted
        mCursorAdapter.swapCursor(null);
    }

    public void sellProduct(int productId, int quantity) {

        // Decrement item quantity
        if (quantity >= 1) {
            quantity--;
            // Construct new uri and content values
            Uri updateUri = ContentUris.withAppendedId(InventoryEntry.CONTENT_URI, productId);
            ContentValues values = new ContentValues();
            values.put(InventoryEntry.COLUMN_PRODUCT_QUANTITY, quantity);
            int rowsUpdated = getContentResolver().update(
                    updateUri,
                    values,
                    null,
                    null);
            if (rowsUpdated == 1) {
                Toast.makeText(this, R.string.sale_success, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.sale_failed, Toast.LENGTH_SHORT).show();
            }

        } else {
            //  Out of stock
            Toast.makeText(this, R.string.out_of_stock, Toast.LENGTH_LONG).show();
        }
    }

}
