package com.gmail.kol.c.arindam.grocerystore.data;

import android.provider.BaseColumns;

//class to hold product table details
public final class ProductInfo {

    //empty constructor
    private ProductInfo() {}

    //product information,i.e., table name & column names
    public static final class ProductData implements BaseColumns {
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
        //product buying price, data type - integer
        public final static String COLUMN_PRODUCT_COST = "cost_price";
    }

}
