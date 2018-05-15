package net.swierkowski.cookbook4;

import android.app.Activity;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;

import net.swierkowski.cookbook4.db.MyCursorAdapter;
import net.swierkowski.cookbook4.db.RecipesDbAdapter;
import net.swierkowski.cookbook4.model.Product;

import java.util.ArrayList;

public class ExcludedProductsListActivity extends Activity {

    private RecipesDbAdapter dbHelper;
    private SimpleCursorAdapter dataAdapter;
    private MyCursorAdapter mMyCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        dbHelper = new RecipesDbAdapter(this);
        dbHelper.open();

        Boolean isProductsTableEmpty = dbHelper.isProductsEmpty();
        if(isProductsTableEmpty==true) {
            dbHelper.insertSomeProducts();
        }

        displayListView();
    }

    private void displayListView() {
        Cursor cursor = dbHelper.fetchAllProducts();

        final String[] columns = new String[]{
                RecipesDbAdapter.Products._ID,
                RecipesDbAdapter.Products.COLUMN_NAME_PRODUCTS_NAME,
                RecipesDbAdapter.Products.COLUMN_NAME_PRODUCTS_RESTRICTION
        };

        int[] to = new int[]{
                R.id.id,
                R.id.name,
                R.id.checkBox_product
        };

        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.product,
                cursor,
                columns,
                to,
                0);

        final ListView lista = (ListView) findViewById(R.id.listaProduktow);
        mMyCursorAdapter = new MyCursorAdapter(this, cursor);
        lista.setAdapter(mMyCursorAdapter);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);
                String productIdString = cursor.getString(cursor.getInt(1));
                long productId = Long.parseLong(productIdString);
                int isRestricted = cursor.getInt(cursor.getColumnIndexOrThrow("restrykcje"));
                changeRestriction(productId,isRestricted);
            }
        });

        lista.setAdapter(mMyCursorAdapter);

        //mMyCursorAdapter.changeCursor(cursor);

        EditText myFilter = (EditText) findViewById(R.id.filter);
        myFilter.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                mMyCursorAdapter.getFilter().filter(s.toString());
            }
        });

        mMyCursorAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return dbHelper.fetchProductsByName(constraint.toString());
            }
        });
        
    }

    private void changeRestriction(long id, int isExcluded){
        if(isExcluded==1){
            dbHelper.updateProduct(id,0);
        } else {
            dbHelper.updateProduct(id, 1);
        }
        updateDisplay();
    }

    private void updateDisplay(){
        mMyCursorAdapter.notifyDataSetChanged();
        dbHelper = new RecipesDbAdapter(this);
        dbHelper.open();
        displayListView();
    }

    @Override
    public void onStop(){
        super.onStop();
        dbHelper.close();
    }

}



