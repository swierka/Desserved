package net.swierkowski.cookbook4;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;

public class ExcludedProductsListActivity extends AppCompatActivity {

    private RecipesDbAdapter dbHelper;
    private SimpleCursorAdapter dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        dbHelper = new RecipesDbAdapter(this);
        dbHelper.open();

        // Czyścimy dane
        dbHelper.deleteAllProducts();

        // Dodajemy przykladowe dane
        dbHelper.insertSomeProducts();

        // Tworzymy listę na podstawie danych w bazie SQLite
        displayListView();
    }


    private void displayListView() {
        Cursor cursor = dbHelper.fetchAllProducts();

        String[] columns = new String[] {
                RecipesDbAdapter.Produkty._ID,
                RecipesDbAdapter.Produkty.COLUMN_NAME_NAZWA
        };

        int[] to = new int[] {
                R.id.id,
                R.id.name
        };

        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.product,
                cursor,
                columns,
                to,
                0);


        ListView lista = (ListView) findViewById(R.id.listaProduktow);

        lista.setAdapter(dataAdapter);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> listView, View view,
                                        int position, long id) {
                    Cursor cursor = (Cursor) listView.getItemAtPosition(position);
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("nazwa"));
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
                    dataAdapter.getFilter().filter(s.toString());
                }
            });

            dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
                public Cursor runQuery(CharSequence constraint) {
                    return dbHelper.fetchProductsByName(constraint.toString());
                }
            });

    }
}
