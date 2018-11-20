package com.gmail.kol.c.arindam.grocerystore;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.gmail.kol.c.arindam.grocerystore.data.ProductInfo.ProductData;

import org.w3c.dom.Text;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    //uri for existing product
    private Uri mCurrentUri;

    private EditText nameEditText;
    private EditText priceEditText;
    private EditText qtyEditText;
    private EditText supplierNameEditText;
    private EditText supplierPhoneEditText;

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        nameEditText = findViewById(R.id.edit_text_name);
        priceEditText = findViewById(R.id.edit_text_price);
        qtyEditText = findViewById(R.id.edit_text_qty);
        supplierNameEditText = findViewById(R.id.edit_text_sup_name);
        supplierPhoneEditText = findViewById(R.id.edit_text_sup_ph);

        nameEditText.addTextChangedListener(textWatcher);

        Intent intent = getIntent();
        mCurrentUri = intent.getData();

        if (mCurrentUri == null) {
            setTitle(R.string.title_add_product);
            invalidateOptionsMenu();
        } else {
            setTitle(R.string.title_edit_product);
            getLoaderManager().initLoader(0,null,this);
        }
    }

    //create menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor,menu);
        return true;
    }

    //disable delete menu item
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mCurrentUri == null) {
            MenuItem menuItem = menu.findItem(R.id.delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save :
                saveProduct();
                finish();
                return true;
            case R.id.delete :
                deleteProduct();
                finish();
                return true;
            case android.R.id.home :

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //get data from edit text & insert same to database
    //as there is no data validation do not enter blank data
    public void saveProduct() {


        String productName = nameEditText.getText().toString().trim();
        int productPrice = Integer.parseInt(priceEditText.getText().toString().trim());
        int productStock = Integer.parseInt(qtyEditText.getText().toString().trim());
        String supplierName = supplierNameEditText.getText().toString().trim();
        String supplierPhone = supplierPhoneEditText.getText().toString().trim();

        //create new content value object & enter data in it
        ContentValues mContentValues = new ContentValues();
        mContentValues.put(ProductData.COLUMN_PRODUCT_NAME, productName);
        mContentValues.put(ProductData.COLUMN_PRODUCT_PRICE, productPrice);
        mContentValues.put(ProductData.COLUMN_PRODUCT_STOCK, productStock);
        mContentValues.put(ProductData.COLUMN_SUPPLIER_NAME, supplierName);
        mContentValues.put(ProductData.COLUMN_SUPPLIER_CONTACT, supplierPhone);

        if (mCurrentUri == null) {
            //create new row of data by inserting content value to table
            Uri newUri = getContentResolver().insert(ProductData.CONTENT_URI, mContentValues);
        } else {
            int rowId = getContentResolver().update(mCurrentUri, mContentValues,null,null);
        }
    }

    public void deleteProduct() {
        int rowId = getContentResolver().delete(mCurrentUri,null,null);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String [] projection = {ProductData.COLUMN_PRODUCT_ID,
                ProductData.COLUMN_PRODUCT_NAME,
                ProductData.COLUMN_PRODUCT_PRICE,
                ProductData.COLUMN_PRODUCT_STOCK,
                ProductData.COLUMN_SUPPLIER_NAME,
                ProductData.COLUMN_SUPPLIER_CONTACT};
        return new CursorLoader(this, mCurrentUri,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        //get data from cursor and show in text view
        if (cursor.moveToFirst()) {
            int nameColumnindex = cursor.getColumnIndex(ProductData.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(ProductData.COLUMN_PRODUCT_PRICE);
            int qtyColumnIndex = cursor.getColumnIndex(ProductData.COLUMN_PRODUCT_STOCK);
            int supplierNameColumnindex = cursor.getColumnIndex(ProductData.COLUMN_SUPPLIER_NAME);
            int supplierPhoneColumnindex = cursor.getColumnIndex(ProductData.COLUMN_SUPPLIER_CONTACT);

            String currentProductName = cursor.getString(nameColumnindex);
            int currentProductPrice = cursor.getInt(priceColumnIndex);
            int currentProductQty = cursor.getInt(qtyColumnIndex);
            String currentSupplierName = cursor.getString(supplierNameColumnindex);
            String currentSupplierPhone = cursor.getString(supplierPhoneColumnindex);

            nameEditText.setText(currentProductName);
            priceEditText.setText(Integer.toString(currentProductPrice));
            qtyEditText.setText(Integer.toString(currentProductQty));
            supplierNameEditText.setText(currentSupplierName);
            supplierPhoneEditText.setText(currentSupplierPhone);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //clear all the edit text input
        nameEditText.getText().clear();
        priceEditText.getText().clear();
        qtyEditText.getText().clear();
        supplierNameEditText.getText().clear();
        supplierPhoneEditText.getText().clear();
    }
}
