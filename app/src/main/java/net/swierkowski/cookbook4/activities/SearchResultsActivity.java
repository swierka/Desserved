package net.swierkowski.cookbook4.activities;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import net.swierkowski.cookbook4.R;
import net.swierkowski.cookbook4.db.ExcludedProductsAdapter;
import net.swierkowski.cookbook4.db.RecipesDbAdapter;
import net.swierkowski.cookbook4.db.SearchResultAdapter;

import static net.swierkowski.cookbook4.activities.MainActivity.MY_SETTINGS;

public class SearchResultsActivity extends AppCompatActivity {

    private RecipesDbAdapter dbHelper;
    private SimpleCursorAdapter dataAdapter;
    private SearchResultAdapter mSearchResultAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results);

        dbHelper = new RecipesDbAdapter(this);
        dbHelper.open();

        dbHelper.deleteAllRecipes();
        dbHelper.deleteAllIngredients();
        dbHelper.insertSomeRecipes();
        dbHelper.insertIngredients();

        displayListView();
        dbHelper.close();
    }


    private void displayListView() {

        Cursor cursor = dbHelper.getRecipes(isVeganGetInt(),isGlutenFreeGetInt(),isLactoseFreeGetInt());

        final String[] columns = new String[]{
                RecipesDbAdapter.Recipes.COLUMN_NAME_RECIPES_NAME,
                RecipesDbAdapter.Recipes.COLUMN_NAME_RECIPES_IS_VEGAN,
                RecipesDbAdapter.Recipes.COLUMN_NAME_RECIPES_IS_GLUTEN_FREE,
                RecipesDbAdapter.Recipes.COLUMN_NAME_RECIPES_IS_LACTOSE_FREE,
                RecipesDbAdapter.Recipes.COLUMN_NAME_RECIPES_PREP_TIME,
                RecipesDbAdapter.Recipes.COLUMN_NAME_RECIPES_COOKING_TIME,
        };

        int[] to = new int[]{
                R.id.name_list_recipes,
                R.id.isVegan_list_recipes,
                R.id.isGlutenFR_list_recipes,
                R.id.isLactoseFR_list_recipes,
                R.id.prepTime_list_recipes,
                R.id.cookTime_list_recipes,
        };

        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.search_results_cell,
                cursor,
                columns,
                to,
                0);

        final ListView results = (ListView) findViewById(R.id.wynikiWyszukiwania);
        mSearchResultAdapter = new SearchResultAdapter(this, cursor);
        results.setAdapter(mSearchResultAdapter);

        results.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {

            }
        });

    }

    private int isVeganGetInt(){
        SharedPreferences settings = getSharedPreferences(MY_SETTINGS, MODE_PRIVATE);
        boolean isVeganBo = settings.getBoolean("vegan",false);
        int isVegan=0;
        if(isVeganBo){isVegan=1;}
        return isVegan;
    }

    private int isLactoseFreeGetInt(){
        SharedPreferences settings = getSharedPreferences(MY_SETTINGS, MODE_PRIVATE);
        boolean isLactoseFreeBo = settings.getBoolean("lactose",false);
        int isLactoseFree = 0;
        if(isLactoseFreeBo){isLactoseFree=1;}
        return isLactoseFree;
    }

    private int isGlutenFreeGetInt(){
        SharedPreferences settings = getSharedPreferences(MY_SETTINGS, MODE_PRIVATE);
        boolean isGlutenFreeBo = settings.getBoolean("gluten",false);
        int isGlutenFree = 0;
        if(isGlutenFreeBo){isGlutenFree=1;}
        return isGlutenFree;
    }

}
