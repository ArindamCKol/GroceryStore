package com.gmail.kol.c.arindam.grocerystore;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.kol.c.arindam.grocerystore.data.InventoryDBHelper;
import com.gmail.kol.c.arindam.grocerystore.data.ProductInfo.ProductData;

public class MainActivity extends AppCompatActivity {

    private InventoryDBHelper mDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDBHelper = new InventoryDBHelper(this);
        displayData();

        //on button click add data row in table
        Button insertProduct = findViewById(R.id.insert_button);
        insertProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertData();
                displayData();
            }
        });
    }

    //display data in a text view
    private void displayData () {
        SQLiteDatabase db = mDBHelper.getReadableDatabase();

        //projection for sql query
        String [] projection = {ProductData.COLUMN_PRODUCT_ID,
                                ProductData.COLUMN_PRODUCT_NAME,
                                ProductData.COLUMN_PRODUCT_PRICE,
                                ProductData.COLUMN_PRODUCT_STOCK,
                                ProductData.COLUMN_SUPPLIER_NAME,
                                ProductData.COLUMN_SUPPLIER_CONTACT,
                                ProductData.COLUMN_PRODUCT_COST};

        //sql query return cursor
        Cursor cursor = db.query(ProductData.TABLE_NAME,projection,null,null,null,null,null);

        TextView resultText = findViewById(R.id.result);

        //get data from cursor and show in text view
        try {
            resultText.setText("Product Table has " + cursor.getCount() + " products.\n\n");

            int nameColumnindex = cursor.getColumnIndex(ProductData.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(ProductData.COLUMN_PRODUCT_PRICE);
            int qtyColumnIndex = cursor.getColumnIndex(ProductData.COLUMN_PRODUCT_STOCK);
            int supplierNameColumnindex = cursor.getColumnIndex(ProductData.COLUMN_SUPPLIER_NAME);
            int supplierPhoneColumnindex = cursor.getColumnIndex(ProductData.COLUMN_SUPPLIER_CONTACT);

            while (cursor.moveToNext()) {
                String currentProductName = cursor.getString(nameColumnindex);
                int currentProductPrice = cursor.getInt(priceColumnIndex);
                int currentProductQty = cursor.getInt(qtyColumnIndex);
                String currentSupplierName = cursor.getString(supplierNameColumnindex);
                String currentSupplierPhone = cursor.getString(supplierPhoneColumnindex);

                resultText.append(currentProductName + " - " + currentProductPrice + " - " + currentProductQty + " - " +
                currentSupplierName + " - " + currentSupplierPhone + "\n");
            }
        } finally {
            cursor.close();
        }
    }

    //get data from edit text & insert same to database
    //as there is no data validation do not enter blank data
    private void insertData() {
        EditText nameEditText = findViewById(R.id.edit_text_name);
        EditText priceEditText = findViewById(R.id.edit_text_price);
        EditText qtyEditText = findViewById(R.id.edit_text_qty);
        EditText supplierNameEditText = findViewById(R.id.edit_text_sup_name);
        EditText supplierPhoneEditText = findViewById(R.id.edit_text_sup_ph);
        EditText costEditText = findViewById(R.id.edit_text_cost);

        String productName = nameEditText.getText().toString().trim();
        int productPrice = Integer.parseInt(priceEditText.getText().toString().trim());
        int productStock = Integer.parseInt(qtyEditText.getText().toString().trim());
        String supplierName = supplierNameEditText.getText().toString().trim();
        String supplierPhone = supplierPhoneEditText.getText().toString().trim();
        int productCost = Integer.parseInt(costEditText.getText().toString().trim());

        //create new content value object & enter data in it
        ContentValues mContentValues = new ContentValues();
        mContentValues.put(ProductData.COLUMN_PRODUCT_NAME, productName);
        mContentValues.put(ProductData.COLUMN_PRODUCT_PRICE, productPrice);
        mContentValues.put(ProductData.COLUMN_PRODUCT_STOCK, productStock);
        mContentValues.put(ProductData.COLUMN_SUPPLIER_NAME, supplierName);
        mContentValues.put(ProductData.COLUMN_SUPPLIER_CONTACT, supplierPhone);
        mContentValues.put(ProductData.COLUMN_PRODUCT_COST, productCost);

        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        //create new row of data by inserting content value to table
        db.insert(ProductData.TABLE_NAME, null, mContentValues);

        nameEditText.getText().clear();
        priceEditText.getText().clear();
        qtyEditText.getText().clear();
        supplierNameEditText.getText().clear();
        supplierPhoneEditText.getText().clear();
    }
}
