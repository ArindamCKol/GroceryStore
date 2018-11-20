package com.gmail.kol.c.arindam.grocerystore;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.gmail.kol.c.arindam.grocerystore.data.ProductInfo.ProductData;

//create list view adapter for cursor
public class ProductCursorAdapter extends CursorAdapter {
    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
    }

    //bind product data to list view
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView productName = view.findViewById(R.id.product_name);
        TextView productPrice = view.findViewById(R.id.product_price);
        TextView productQty = view.findViewById(R.id.product_qty);

        //get data from cursor and show in text view
        int nameColumnindex = cursor.getColumnIndex(ProductData.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(ProductData.COLUMN_PRODUCT_PRICE);
        int qtyColumnIndex = cursor.getColumnIndex(ProductData.COLUMN_PRODUCT_STOCK);
//        int supplierNameColumnindex = cursor.getColumnIndex(ProductData.COLUMN_SUPPLIER_NAME);
//        int supplierPhoneColumnindex = cursor.getColumnIndex(ProductData.COLUMN_SUPPLIER_CONTACT);

        String currentProductName = cursor.getString(nameColumnindex);
        int currentProductPrice = cursor.getInt(priceColumnIndex);
        int currentProductQty = cursor.getInt(qtyColumnIndex);
//        String currentSupplierName = cursor.getString(supplierNameColumnindex);
//        String currentSupplierPhone = cursor.getString(supplierPhoneColumnindex);

        productName.setText(currentProductName);
        productPrice.setText(Integer.toString(currentProductPrice));
        productQty.setText(Integer.toString(currentProductQty));
    }
}
