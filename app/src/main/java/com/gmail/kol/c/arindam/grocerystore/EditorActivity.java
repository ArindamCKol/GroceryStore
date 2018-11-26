package com.gmail.kol.c.arindam.grocerystore;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.gmail.kol.c.arindam.grocerystore.data.ProductInfo.ProductData;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

    //uri for existing product
    private Uri mCurrentUri;

    //declare edit text variables
    private EditText nameEditText;
    private EditText priceEditText;
    private EditText qtyEditText;
    private EditText supplierNameEditText;
    private EditText supplierPhoneEditText;

    //declare button variables
    private Button increaseQty;
    private Button deacreaseQty;
    private FloatingActionButton callSupplier;

    //to check data changed status
    private boolean hasDataChanged = false;

    //text watcher to check text entered in edit text
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //change the status to data changed
            hasDataChanged = true;
        }

        @Override
        public void afterTextChanged(Editable s) { }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        //link edit text variables to xml widgets
        nameEditText = findViewById(R.id.edit_text_name);
        priceEditText = findViewById(R.id.edit_text_price);
        qtyEditText = findViewById(R.id.edit_text_qty);
        supplierNameEditText = findViewById(R.id.edit_text_sup_name);
        supplierPhoneEditText = findViewById(R.id.edit_text_sup_ph);

        //add text change watcher to edit text widget
        nameEditText.addTextChangedListener(textWatcher);
        priceEditText.addTextChangedListener(textWatcher);
        qtyEditText.addTextChangedListener(textWatcher);
        supplierNameEditText.addTextChangedListener(textWatcher);
        supplierPhoneEditText.addTextChangedListener(textWatcher);

        //link button variables with xml widgets
        increaseQty = findViewById(R.id.increase_qty);
        deacreaseQty = findViewById(R.id.decrease_qty);
        callSupplier = findViewById(R.id.call_supplier);

        //add onclick listener to buttons
        increaseQty.setOnClickListener(this);
        deacreaseQty.setOnClickListener(this);
        callSupplier.setOnClickListener(this);

        //get uri for existing product
        Intent intent = getIntent();
        mCurrentUri = intent.getData();

        //select appropriate title for activity
        if (mCurrentUri == null) {
            setTitle(R.string.title_add_product);
            callSupplier.setVisibility(View.GONE); //new product does not have phone no.
            invalidateOptionsMenu(); //disable delete menu item for new product
        } else {
            setTitle(R.string.title_edit_product);
            callSupplier.setVisibility(View.VISIBLE);
            getLoaderManager().initLoader(0,null,this); //load existing product in cursor loader
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

    //take proper action after menu item selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save :
                if (hasDataChanged == true) {
                    showSaveDialog(); // data is changed/entered, show save dialog box
                } else {
                    //show message that nothing to save
                    if (mCurrentUri == null) {
                        Toast.makeText(this, R.string.no_data, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, R.string.no_change, Toast.LENGTH_LONG).show();
                    }
                }
                return true;
            case R.id.delete :
                showDeleteDialog(); //show delete dialog box
                return true;
            case android.R.id.home :
                onBackPressed(); //call back pressed function
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //get data from edit text & insert/update same to database
    public void saveProduct() {
        //get string values from edit text
        String productName = nameEditText.getText().toString().trim();
        String productPriceText = priceEditText.getText().toString().trim();
        String productStockText = qtyEditText.getText().toString().trim();
        String supplierName = supplierNameEditText.getText().toString().trim();
        String supplierPhone = supplierPhoneEditText.getText().toString().trim();
        //variable for integer data
        int productPrice;
        int productStock = Integer.parseInt(productStockText);

        //check string string values from edit text, if they are empty, then show appropriate message and return without insert/update
        if (TextUtils.isEmpty(productName)) {
            Toast.makeText(this, getString(R.string.error_message,getString(R.string.name_text)), Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(productPriceText)) {
            Toast.makeText(this, getString(R.string.error_message,getString(R.string.price_text)), Toast.LENGTH_LONG).show();
            return;
        } else {
            productPrice = Integer.parseInt(productPriceText); //string is not empty cast then to integer variable
        }

        if (TextUtils.isEmpty(supplierName)) {
            Toast.makeText(this, getString(R.string.error_message,getString(R.string.supplier_name_text)), Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(supplierPhone)) {
            Toast.makeText(this, getString(R.string.error_message,getString(R.string.supplier_phone_text)), Toast.LENGTH_LONG).show();
            return;
        }

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

            //check uri if insert successful. Show appropriate message
            if (newUri == null) {
                Toast.makeText(this, R.string.product_insert_failed, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, R.string.product_inserted, Toast.LENGTH_LONG).show();
            }
        } else {
            //update existing product
            int rowId = getContentResolver().update(mCurrentUri, mContentValues,null,null);

            //check row id if update successful. Show appropriate message
            if (rowId == 0) {
                Toast.makeText(this, R.string.product_update_fail, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, R.string.product_updated, Toast.LENGTH_LONG).show();
            }
        }
        //close editor activity
        finish();
    }

    //delete existing product
    public void deleteProduct() {
        int rowId = getContentResolver().delete(mCurrentUri,null,null);

        //check if delete successful. Show appropriate message
        if (rowId == 0) {
            Toast.makeText(this, R.string.product_delete_fail, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, R.string.product_deleted, Toast.LENGTH_LONG).show();
        }
        //close editor activity
        finish();
    }

    //on back/up button pressed
    @Override
    public void onBackPressed() {
        if (hasDataChanged == true) {
            showDiscardDialog(); //if data changed/entered, show discard dialog box
        } else {
            super.onBackPressed(); //close editor activity
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        //projection for SQL query
        String [] projection = {ProductData.COLUMN_PRODUCT_ID,
                ProductData.COLUMN_PRODUCT_NAME,
                ProductData.COLUMN_PRODUCT_PRICE,
                ProductData.COLUMN_PRODUCT_STOCK,
                ProductData.COLUMN_SUPPLIER_NAME,
                ProductData.COLUMN_SUPPLIER_CONTACT};
        //load product details in background thread
        return new CursorLoader(this, mCurrentUri,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        //if there is no data in cursor return
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        //get data from cursor and show in edit text view
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
            priceEditText.setText(String.valueOf(currentProductPrice));
            qtyEditText.setText(String.valueOf(currentProductQty));
            supplierNameEditText.setText(currentSupplierName);
            supplierPhoneEditText.setText(currentSupplierPhone);
            //set text change status to false
            hasDataChanged = false;
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

    public void showSaveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_title_save).setCancelable(false);
        builder.setPositiveButton(R.string.dialog_button_save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveProduct(); //as user want to save the details, call save product function
            }
        });

        builder.setNegativeButton(R.string.dialog_button_edit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); //close dialog, do nothing
            }
        });

        //attach builder to dialog box and show it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_title_delete).setCancelable(false);
        builder.setPositiveButton(R.string.dialog_button_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteProduct(); //user want to delete product. call delete function
            }
        });

        builder.setNegativeButton(R.string.dialog_button_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); //close dialog, do nothing
            }
        });

        //attach builder to dialog box and show it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void showDiscardDialog () {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_title_discard).setCancelable(false);
        builder.setPositiveButton(R.string.dialog_button_discard, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish(); //discard changes and end editor activity
            }
        });

        builder.setNegativeButton(R.string.dialog_button_edit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); //close dialog, do nothing
            }
        });

        //attach builder to dialog box and show it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View view) {
        int productQty = Integer.parseInt(qtyEditText.getText().toString().trim());
        switch (view.getId()) {
            case R.id.increase_qty :
                productQty++;
                qtyEditText.setText(String.valueOf(productQty));
                return;
            case R.id.decrease_qty :
                if (productQty>0) {
                    productQty--;
                    qtyEditText.setText(String.valueOf(productQty));
                }
                return;
            case R.id.call_supplier :
                dialNumber();
                return;
        }
    }

    //call phone number
    public void dialNumber () {
        String phoneNumber = supplierPhoneEditText.getText().toString().trim();

        if (TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(this, "Phone number is Empty", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
