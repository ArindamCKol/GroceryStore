package com.gmail.kol.c.arindam.grocerystore;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gmail.kol.c.arindam.grocerystore.data.ProductInfo.ProductData;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    ProductCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set floating action button & onclick open product editor
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        //set list view
        ListView productListView = findViewById(R.id.product_list);

        //create empty view
        View emptyView = findViewById(R.id.empty_text_view);
        productListView.setEmptyView(emptyView);

        //set up cursor adapter with null value & attach to list view
        adapter = new ProductCursorAdapter(this,null);
        productListView.setAdapter(adapter);

        //on product list item click, open that item in the editor activity
        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);

                //put id of clicked product in uri & pass it to editor activity
                Uri currentUri = ContentUris.withAppendedId(ProductData.CONTENT_URI, id);
                intent.setData(currentUri);

                startActivity(intent);
            }
        });

        //start loader
        getLoaderManager().initLoader(0,null,this);
    }

    //create menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    //when menu item clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //delete table
            case R.id.delete_table :
                getContentResolver().delete(ProductData.CONTENT_URI,null,null);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //load product data from database table to cursor in background thread
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String [] projection = {ProductData.COLUMN_PRODUCT_ID,
                ProductData.COLUMN_PRODUCT_NAME,
                ProductData.COLUMN_PRODUCT_PRICE,
                ProductData.COLUMN_PRODUCT_STOCK};
        return new CursorLoader(this, ProductData.CONTENT_URI,projection,null,null,null);
    }

    //when loading complete add cursor to the list view
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    //if data reset change list view adapter to null
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}