package com.gmail.kol.c.arindam.grocerystore.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gmail.kol.c.arindam.grocerystore.data.ProductInfo.ProductData;

public class InventoryDBHelper extends SQLiteOpenHelper {
    //database name
    private static final String DATABASE_NAME = "grocery.db";

    //database version
    private static final int DATABASE_VERSION = 1;

    public InventoryDBHelper(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

    //called when database is created first time, if database already exist then not called
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //SQL statement for create table
        String CREATE_PRODUCT_TABLE = "CREATE TABLE " + ProductData.TABLE_NAME + "("
                + ProductData.COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ProductData.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, "
                + ProductData.COLUMN_PRODUCT_PRICE + " INTEGER NOT NULL, "
                + ProductData.COLUMN_PRODUCT_STOCK + " INTEGER NOT NULL DEFAULT 0, "
                + ProductData.COLUMN_SUPPLIER_NAME + " TEXT, "
                + ProductData.COLUMN_SUPPLIER_CONTACT + " TEXT, "
                + ProductData.COLUMN_PRODUCT_COST + " INTEGER NOT NULL" + ");";

        //execute create table SQL statement
        sqLiteDatabase.execSQL(CREATE_PRODUCT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


}
