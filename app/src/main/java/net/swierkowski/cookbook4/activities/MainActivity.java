package net.swierkowski.cookbook4.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;


import net.swierkowski.cookbook4.R;
import net.swierkowski.cookbook4.db.DatabaseAccess;

public class MainActivity extends AppCompatActivity {

    public static final String MY_SETTINGS = "MySettings";
    private CheckBox isVeganCb;
    CheckBox isGlutenFreeCb;
    CheckBox isLactoseFreeCb;
    private DatabaseAccess mDbAccess;
    private Cursor mCursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences settings = getSharedPreferences(MY_SETTINGS, MODE_PRIVATE);
        isVeganCb = (CheckBox)findViewById(R.id.cb_vegan);
        isVeganCb.setChecked(settings.getBoolean("vegan",false));
        isGlutenFreeCb = (CheckBox)findViewById(R.id.cb_gluten);
        isGlutenFreeCb.setChecked(settings.getBoolean("gluten",false));
        isLactoseFreeCb = (CheckBox)findViewById(R.id.cb_lactose);
        isLactoseFreeCb.setChecked(settings.getBoolean("lactose",false));

    }

    public void onExclude(View view) {
        Intent intent = new Intent(this,ExcludedProductsListActivity.class);
        startActivity(intent);
    }

    public void onSearch(View view) {
        mDbAccess = DatabaseAccess.getInstance(this);
        mDbAccess.open();

        int intVegan=0;
        int intGlutenFree=0;
        int intLactoseFree=0;

        if(isVeganCb.isChecked()) intVegan = 1;
        if(isGlutenFreeCb.isChecked()) intGlutenFree = 1;
        if(isLactoseFreeCb.isChecked()) intLactoseFree = 1;

        mCursor = mDbAccess.getRecipes(intVegan,intGlutenFree,intLactoseFree);

        if(mCursor.getCount()!=0){
            Intent intent = new Intent(this,RecipesListActivity.class);
            intent.putExtra("key","onSearch");
            startActivity(intent);
        } else {
            Toast.makeText(this,"Brak przepisów spełniających kryteria",Toast.LENGTH_SHORT).show();
        }
        mDbAccess.close();
    }

    public void onGetFavorites(View view){
        mDbAccess = DatabaseAccess.getInstance(this);
        mDbAccess.open();
        mCursor = mDbAccess.getFavoritesRecipes();
        Log.e("CURSOR COUNT","count = "+mCursor.getCount());

        if(mCursor.getCount()!=0){
            Intent intent = new Intent(this,RecipesListActivity.class);
            intent.putExtra("key","onGetFavorites");
            startActivity(intent);
        } else {
            Toast.makeText(this,"Brak zapisanych przepisów",Toast.LENGTH_SHORT).show();
        }
        mDbAccess.close();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveRestrictionPreferences();
    }


    private void saveRestrictionPreferences(){
        SharedPreferences settings = getSharedPreferences(MY_SETTINGS,MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        Boolean isVegan =  isVeganCb.isChecked();
        Boolean isGlutenFree = isGlutenFreeCb.isChecked();
        Boolean isLactoseFree = isLactoseFreeCb.isChecked();

        editor.putBoolean("vegan",isVegan);
        editor.putBoolean("gluten",isGlutenFree);
        editor.putBoolean("lactose",isLactoseFree);
        editor.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
