package net.swierkowski.cookbook4.fragments;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import net.swierkowski.cookbook4.R;

public class IngredientAdapter extends CursorAdapter {


    public IngredientAdapter(Context context, Cursor c) {
        super(context, c);
    }

    public IngredientAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    public IngredientAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.ingredients_cell, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView id = (TextView)view.findViewById(R.id.id_list_recipes);
        TextView name = (TextView)view.findViewById(R.id.skladnik_nazwa);
        TextView amount = (TextView)view.findViewById(R.id.skladnik_ilosc);

        String ingredientName = cursor.getString(cursor.getColumnIndexOrThrow("nazwa_przepisy"));
        name.setText(ingredientName);
        String amountName = cursor.getString(cursor.getColumnIndexOrThrow("ilosc"));
        amount.setText(amountName);

        String idName = cursor.getString(cursor.getInt(1));
        id.setText(idName);
    }

}

