package net.swierkowski.cookbook4.listAdapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import net.swierkowski.cookbook4.R;
import net.swierkowski.cookbook4.model.Ingredient;
import net.swierkowski.cookbook4.model.Product;

public class IngredientAdapter extends CursorAdapter {

    private Ingredient mIngredient;

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
        return LayoutInflater.from(context).inflate(R.layout.fragment_ingredients_cell, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView amount = (TextView)view.findViewById(R.id.skladnik_ilosc);

        String amountName = cursor.getString(1);

        mIngredient = new Ingredient();
        mIngredient.setAmount(amountName);
        amount.setText(mIngredient.getAmount());
    }



}

