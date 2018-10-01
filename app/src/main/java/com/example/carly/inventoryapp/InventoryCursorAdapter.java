package com.example.carly.inventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.carly.inventoryapp.data.InventoryContract;

/**
 * Inventory Cursor Adapter is an adapter for a list view that uses a Cursor of product data
 * as its data source.
 */
public class InventoryCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new InventoryCursorAdapter.
     */
    public InventoryCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the product data to the given list item layout.
     */
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView priceTextView = (TextView) view.findViewById(R.id.price);
        TextView quantityTextView = (TextView) view.findViewById(R.id.quantity);

        // Find the columns of product attributes that we're interested in.
        int nameColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY);

        // Read the product attributes from the Cursor for the current product.
        String productName = cursor.getString(nameColumnIndex);
        String productPrice = cursor.getString(priceColumnIndex);
        String productQuantity = cursor.getString(quantityColumnIndex);

        // Update the TextViews with the attributes for the current product
        nameTextView.setText(productName);
        priceTextView.setText(productPrice);
        quantityTextView.setText(productQuantity);

        // These will be used in sale method. They're final, as we are calling
        // MainActivity class directly
        final int id = cursor.getInt(cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry._ID));
        final int qty = Integer.parseInt(productQuantity);

        // Sell an item
        view.findViewById(R.id.sell_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InventoryActivity inventoryActivity = (InventoryActivity) context;
                inventoryActivity.sellProduct(id, qty);
            }
        });
    }
}
