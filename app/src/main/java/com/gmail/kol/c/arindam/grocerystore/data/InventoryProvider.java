package com.gmail.kol.c.arindam.grocerystore.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.gmail.kol.c.arindam.grocerystore.data.ProductInfo.ProductData;

import java.net.URI;

public class InventoryProvider extends ContentProvider {
    //URI matcher code product table
    private static final int PRODUCTS = 100;

    //URI matcher code single product
    private static final int PRODUCT_ID = 101;

    //UriMatcher object to match a content URI to a corresponding code
    private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    //static initializer
    static {
        //add uri for products table
        mUriMatcher.addURI(ProductInfo.CONTENT_AUTHORITY, ProductInfo.PATH_PRODUCTS, PRODUCTS);

        //add uri for single product in products table
        mUriMatcher.addURI(ProductInfo.CONTENT_AUTHORITY, ProductInfo.PATH_PRODUCTS + "/#", PRODUCT_ID);
    }

    private InventoryDBHelper mDBHelper;

    @Override
    public boolean onCreate() {
        mDBHelper = new InventoryDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        //open database to read
        SQLiteDatabase db = mDBHelper.getReadableDatabase();

        Cursor cursor;

        //switch to specific code depending upon uri
        int match = mUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                //return cursor for whole products table
                cursor = db.query(ProductData.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case PRODUCT_ID:
                //return cursor for a single product
                selection = ProductData._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                cursor = db.query(ProductData.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        //notify cursor loader that value of cursor changed
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        //return the cursor
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = mUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return ProductData.CONTENT_LIST_TYPE;
            case PRODUCT_ID:
                return ProductData.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    //insert single product to database
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        int match = mUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return insertProduct(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    //insert product in inventory
    private Uri insertProduct(Uri uri, ContentValues values) {
        //open database in write mode
        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        //insert to database
        long id = db.insert(ProductData.TABLE_NAME, null, values);
        //if the ID is -1, then the insertion failed and return null.
        if (id == -1) {
            return null;
        }

        //notify cursor loader that value of cursor changed
        getContext().getContentResolver().notifyChange(uri, null);

        //return uri with row id
        return ContentUris.withAppendedId(uri, id);
    }

    //update single values of product in database
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match = mUriMatcher.match(uri);
        switch (match) {
            case PRODUCT_ID:
                selection = ProductData._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updatePet(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    //update selected product details
    private int updatePet(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        //open database in write mode
        SQLiteDatabase database = mDBHelper.getWritableDatabase();

        //update sql command
        int rowsUpdated = database.update(ProductData.TABLE_NAME, values, selection, selectionArgs);

        //if updated, notify cursor loader that value of cursor changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        //return row number
        return rowsUpdated;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        //open database in write mode
        SQLiteDatabase database = mDBHelper.getWritableDatabase();

        //to store row number
        int rowsDeleted;

        //switch to specific code depending upon uri
        int match = mUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                //delete product table
                rowsDeleted = database.delete(ProductData.TABLE_NAME, selection, selectionArgs);
                break;
            case PRODUCT_ID:
                //delete a single row given by the ID in the URI
                selection = ProductData._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(ProductData.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        //if deleted, notify cursor loader that value of cursor changed
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        //return row number
        return rowsDeleted;
    }
}
