package net.swierkowski.cookbook4.activities;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.Toast;

import net.swierkowski.cookbook4.R;
import net.swierkowski.cookbook4.db.DatabaseAccess;
import net.swierkowski.cookbook4.listAdapters.ExcludedProductsAdapter;


public class ExcludedProductsListActivity extends Activity {

    private ExcludedProductsAdapter mExcludedProductsAdapter;
    private int index;
    private DatabaseAccess mDbAccess;
    private Cursor mCursor;
    private ListView list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.excluded_layout);
        index=0;

        displayListView();
    }

    private void displayListView() {
        mDbAccess = DatabaseAccess.getInstance(this);
        mDbAccess.open();
        mCursor = mDbAccess.fetchAllProducts();
        list = (ListView) findViewById(R.id.listaProduktow);

        //getting Products from db
        mExcludedProductsAdapter = new ExcludedProductsAdapter(this, mCursor);
        list.setAdapter(mExcludedProductsAdapter);

        //setting a listener on a button
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                mCursor = (Cursor) listView.getItemAtPosition(position);
                String productIdString = mCursor.getString(mCursor.getInt(1));
                long productId = Long.parseLong(productIdString);
                int isRestricted = mCursor.getInt(mCursor.getColumnIndexOrThrow("productsRestriction"));
                changeRestriction(productId,isRestricted,position);
            }
        });

        //setting filter
        EditText myFilter = (EditText) findViewById(R.id.filter);
        myFilter.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                mExcludedProductsAdapter.getFilter().filter(s.toString());
            }
        });

        mExcludedProductsAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return mDbAccess.fetchProductsByName(constraint.toString());
            }
        });
    }
    //updating db based on restriction chosen
    private void changeRestriction(long id, int isExcluded, int position){
        if(isExcluded==1){
            mDbAccess.updateProduct(id,0);
        } else {
            mDbAccess.updateProduct(id, 1);
        }
        updateDisplay();
    }

    private void updateDisplay(){
        Cursor cursor = mDbAccess.fetchAllProducts();
        mExcludedProductsAdapter.changeCursor(cursor);
        index++;
    }

    @Override
    public void onStart() {
        super.onStart();
        mDbAccess.open();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (index > 0) {
            Toast.makeText(this, "Zapisano zmiany", Toast.LENGTH_SHORT).show();
        }
        mDbAccess.close();
    }

    //removing all marked restrictions + updating db
    public void onClear(View view) {
        mDbAccess.clearRestriction();
        updateDisplay();
        Toast.makeText(this,"Wyczyszczono wszystkie zaznaczenia",Toast.LENGTH_SHORT).show();
    }
}



