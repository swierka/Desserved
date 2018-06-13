package net.swierkowski.cookbook4.activities;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
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
        Cursor cursor = mDbAccess.fetchAllProducts();
        final ListView lista = (ListView) findViewById(R.id.listaProduktow);

        mExcludedProductsAdapter = new ExcludedProductsAdapter(this, cursor);
        lista.setAdapter(mExcludedProductsAdapter);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);
                String productIdString = cursor.getString(cursor.getInt(1));
                long productId = Long.parseLong(productIdString);
                int isRestricted = cursor.getInt(cursor.getColumnIndexOrThrow("productsRestriction"));
                changeRestriction(productId,isRestricted);
            }
        });

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

    private void changeRestriction(long id, int isExcluded){
        if(isExcluded==1){
            mDbAccess.updateProduct(id,0);
        } else {
            mDbAccess.updateProduct(id, 1);
        }
        updateDisplay();
    }

    private void updateDisplay(){
        /* Aktualizacja danych dla adaptera - nowy kursor */
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
            Toast.makeText(this, "Zapisano wykluczone produkty", Toast.LENGTH_SHORT).show();
        }
        mDbAccess.close();
    }
}



