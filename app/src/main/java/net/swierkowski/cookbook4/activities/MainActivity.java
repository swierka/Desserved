package net.swierkowski.cookbook4.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import net.swierkowski.cookbook4.R;

public class MainActivity extends AppCompatActivity {
    public static final String MY_SETTINGS = "MySettings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnSearch = (Button)findViewById(R.id.button_search);
        setRoundedDrawable(btnSearch,Color.GRAY,Color.GRAY);
        Button btnExclude= (Button)findViewById(R.id.button_exclude);
        setRoundedDrawable(btnExclude,Color.GRAY,Color.GRAY);

        SharedPreferences settings = getSharedPreferences(MY_SETTINGS, MODE_PRIVATE);
        CheckBox isVeganCb = (CheckBox)findViewById(R.id.cb_vegan);
        isVeganCb.setChecked(settings.getBoolean("vegan",false));
        CheckBox isGlutenFreeCb = (CheckBox)findViewById(R.id.cb_gluten);
        isGlutenFreeCb.setChecked(settings.getBoolean("gluten",false));
        CheckBox isLactoseFreeCb = (CheckBox)findViewById(R.id.cb_lactose);
        isLactoseFreeCb.setChecked(settings.getBoolean("lactose",false));
    }

    public void onExclude(View view) {
        Intent intent = new Intent(this,ExcludedProductsListActivity.class);
        startActivity(intent);
    }

    public void onSearch(View view) {
        Intent intent = new Intent(this,SearchResultsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences settings = getSharedPreferences(MY_SETTINGS,MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        CheckBox isVeganCb = (CheckBox)findViewById(R.id.cb_vegan);
        Boolean isVegan =  isVeganCb.isChecked();

        CheckBox isGlutenFreeCb = (CheckBox)findViewById(R.id.cb_gluten);
        Boolean isGlutenFree = isGlutenFreeCb.isChecked();

        CheckBox isLactoseFreeCb = (CheckBox)findViewById(R.id.cb_lactose);
        Boolean isLactoseFree = isLactoseFreeCb.isChecked();

        editor.putBoolean("vegan",isVegan);
        editor.putBoolean("gluten",isGlutenFree);
        editor.putBoolean("lactose",isLactoseFree);
        editor.commit();
    }

    public static void setRoundedDrawable(Button button, int backgroundColor, int borderColor) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(30f);
        shape.setColor(backgroundColor);
        if (borderColor != 0){
            shape.setStroke((int)3f, borderColor);
        }
        button.setBackgroundDrawable(shape);
    }
}
