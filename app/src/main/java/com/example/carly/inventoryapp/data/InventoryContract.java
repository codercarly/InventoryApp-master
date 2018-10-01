package com.example.carly.inventoryapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class InventoryContract {

    /**
     * Constants for the Inventory Provider
     */
    // Empty constructor for {@link @InventoryContract} to prevent it from accidentally being instantiated.
    private InventoryContract() {}

    // Content Authority
    public static final String CONTENT_AUTHORITY = "com.example.carly.inventoryapp";

    // Scheme + Content Authority
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Type of data
    public static final String PATH_PRODUCTS = "products";

    /**
     * Inner class that defines constant values for the inventory database table
     */
    public static final class InventoryEntry implements BaseColumns {

        /**
         *  Complete content URI
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PRODUCTS);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of products
         */
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single product
         */
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "/" +PATH_PRODUCTS;
        /**
         * Name of database table
         */
        public static final String TABLE_NAME = "inventory";

        /**
         * Constants for the table columns.
         */
        // Column with key into location ID.
        public static final String _ID = BaseColumns._ID;

        // Column with product name.
        public static final String COLUMN_PRODUCT_NAME = "name";

        // Column with product price.
        public static final String COLUMN_PRODUCT_PRICE = "price";

        // Column with product quantity.
        public static final String COLUMN_PRODUCT_QUANTITY = "quantity";

        // Column with product supplier name.
        public static final String COLUMN_SUPPLIER_NAME = "supplier";

        // Column with product supplier phone.
        public static final String COLUMN_SUPPLIER_PHONE = "phone";

    }
}
