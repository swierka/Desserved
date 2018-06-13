package net.swierkowski.cookbook4.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import net.swierkowski.cookbook4.R;
import net.swierkowski.cookbook4.db.DatabaseAccess;
import net.swierkowski.cookbook4.listAdapters.RecipesListAdapter;
import net.swierkowski.cookbook4.fragments.IngredientsFragment;
import net.swierkowski.cookbook4.model.Recipe;

import java.util.ArrayList;

import static net.swierkowski.cookbook4.activities.MainActivity.MY_SETTINGS;

public class RecipesListActivity  extends AppCompatActivity implements IngredientsFragment.OnFragmentInteractionListener {

    private GridView mGridView;
    private ArrayList<Recipe> mRecipes;
    private RecipesListAdapter mAdapter;
    private DatabaseAccess mDbAccess;
    private Cursor mCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipes_list_grid);


        mGridView = (GridView)findViewById(R.id.gridView);
        mRecipes = new ArrayList<>();
        mAdapter = new RecipesListAdapter(this,R.layout.recipes_list_grid_item,mRecipes);
        mGridView.setAdapter(mAdapter);
        displayGridView();
    }

    private void displayGridView() {
        mDbAccess = DatabaseAccess.getInstance(this);
        mDbAccess.open();

        String buttonMethod = getIntent().getExtras().getString("key");

        if(buttonMethod.equals("onSearch")) {
            mCursor = mDbAccess.getRecipes(isVeganGetInt(), isGlutenFreeGetInt(), isLactoseFreeGetInt());
        }
        else if(buttonMethod.equals("onGetFavorites")) {
            mCursor = mDbAccess.getFavoritesRecipes();
        }

        mRecipes.clear();
        if(mCursor.getCount()!=0) {
            do {
                long id = Long.parseLong(mCursor.getString(0));
                String name = mCursor.getString(1);
                String image = mCursor.getString(2);
                mRecipes.add(new Recipe(id, name, image));
            } while  (mCursor.moveToNext());
        }

        mAdapter.notifyDataSetChanged();

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Long idData = mRecipes.get(position).getId();
                Intent intent = new Intent(RecipesListActivity.this,RecipeDetailsActivity.class);
                intent.putExtra(RecipeDetailsActivity.RECIPE_ID_STRING,idData);
                startActivity(intent);
            }
        });
        mDbAccess.close();
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


    @Override
    public void onFragmentInteraction(Uri uri) {
    }


}
