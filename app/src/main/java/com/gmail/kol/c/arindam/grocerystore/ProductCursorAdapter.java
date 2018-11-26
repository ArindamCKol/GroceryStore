package com.gmail.kol.c.arindam.grocerystore;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

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
    public void bindView(final View view, final Context context, Cursor cursor) {
        TextView productName = view.findViewById(R.id.product_name);
        TextView productPrice = view.findViewById(R.id.product_price);
        final TextView productQty = view.findViewById(R.id.product_qty);
        Button saleProduct = view.findViewById(R.id.sale_product);

        //get data from cursor and show in text view
        int idColumnIndex = cursor.getColumnIndex(ProductData.COLUMN_PRODUCT_ID);
        int nameColumnindex = cursor.getColumnIndex(ProductData.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(ProductData.COLUMN_PRODUCT_PRICE);
        int qtyColumnIndex = cursor.getColumnIndex(ProductData.COLUMN_PRODUCT_STOCK);

        final long currentProductId = cursor.getLong(idColumnIndex);
        String currentProductName = cursor.getString(nameColumnindex);
        int currentProductPrice = cursor.getInt(priceColumnIndex);
        final int[] currentProductQty = {cursor.getInt(qtyColumnIndex)};

        productName.setText(currentProductName);
        productPrice.setText(context.getString(R.string.list_view_price,currentProductPrice));
        productQty.setText(context.getString(R.string.list_view_qty,currentProductQty[0]));

        //if sell button clicked reduce quantity in list view and product table
        saleProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentProductQty[0] >0) {
                    currentProductQty[0]--;
                    Uri currentUri = ContentUris.withAppendedId(ProductData.CONTENT_URI, currentProductId);
                    ContentValues mContentValues = new ContentValues();
                    mContentValues.put(ProductData.COLUMN_PRODUCT_STOCK, currentProductQty[0]);
                    context.getContentResolver().update(currentUri, mContentValues,null,null);
                    productQty.setText(String.valueOf(currentProductQty[0]));
                } else {
                    Toast.makeText(context, R.string.no_stock, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
