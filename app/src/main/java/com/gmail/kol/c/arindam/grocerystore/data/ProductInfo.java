package com.gmail.kol.c.arindam.grocerystore.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

//class to hold product table details
public final class ProductInfo {

    //empty constructor
    private ProductInfo() {}

    //unique content authority, package name
    public static final String CONTENT_AUTHORITY = "com.gmail.kol.c.arindam.grocerystore";

    //base Uri from content authority
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    //path for product table
    public static final String PATH_PRODUCTS = "products";

    //inner class for product information,i.e., table name & column names
    public static final class ProductData implements BaseColumns {
        //content Uri for product table
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PRODUCTS);

        //MIME type for a list of products
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/"
                                                      + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;

        //MIME type for a single product
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/"
                                                      + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;

        //name of database table
        public final static String TABLE_NAME = "products";

        //unique ID, for database use, data type - integer
        public final static String COLUMN_PRODUCT_ID = BaseColumns._ID;

        //product name, data type - text
        public final static String COLUMN_PRODUCT_NAME = "product_name";

        //product selling price, data type - integer
        public final static String COLUMN_PRODUCT_PRICE = "price";

        //product quantity in stock, data type - integer
        public final static String COLUMN_PRODUCT_STOCK = "quantity";

        //supplier name, data type - text
        public final static String COLUMN_SUPPLIER_NAME = "supplier_name";

        //supplier phone number, data type - text
        public final static String COLUMN_SUPPLIER_CONTACT = "supplier_phone_number";
    }
}