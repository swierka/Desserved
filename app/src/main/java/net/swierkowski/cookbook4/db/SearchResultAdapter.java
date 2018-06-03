package net.swierkowski.cookbook4.db;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.swierkowski.cookbook4.R;

public class SearchResultAdapter extends CursorAdapter {

    public SearchResultAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.search_results_cell, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameTv = (TextView) view.findViewById(R.id.name_list_recipes);

        TextView isVeganTv = (TextView) view.findViewById(R.id.isVegan_list_recipes);
        TextView isGlutenFreeTv = (TextView) view.findViewById(R.id.isGlutenFR_list_recipes);
        TextView isLactoseFreeTv = (TextView) view.findViewById(R.id.isLactoseFR_list_recipes);
        TextView idTv = (TextView) view.findViewById(R.id.id_list_recipes);

        ImageView isVeganImage = (ImageView) view.findViewById(R.id.check_vegan);
        ImageView isGlutenFreeImage = (ImageView) view.findViewById(R.id.check_gluten);
        ImageView isLactoseFreeImage = (ImageView) view.findViewById(R.id.check_lactose);

        TextView prepTimeTv = (TextView) view.findViewById(R.id.prepTime_list_recipes);
        TextView cookTimeTv = (TextView) view.findViewById(R.id.cookTime_list_recipes);

        String recipeName = cursor.getString(cursor.getColumnIndexOrThrow("nazwa_przepisy"));
        String id = cursor.getString(cursor.getInt(1));
        int isVeganInt = cursor.getInt(cursor.getColumnIndexOrThrow("weganskie"));
        int isGlutenFreeInt = cursor.getInt(cursor.getColumnIndexOrThrow("bez_glutenu"));
        int isLactoseFreeInt = cursor.getInt(cursor.getColumnIndexOrThrow("bez_laktozy"));
        int prepTimeInt = cursor.getInt(cursor.getColumnIndexOrThrow("czas_przygotowania"));
        int cookTimeInt = cursor.getInt(cursor.getColumnIndexOrThrow("czas_pieczenia"));

        nameTv.setText(recipeName);
        idTv.setText(id);

        if(isVeganInt==1){
            isVeganImage.setVisibility(View.VISIBLE);
        } else {
            isVeganTv.setTextColor(Color.LTGRAY);
        }

        if(isGlutenFreeInt==1){
            isGlutenFreeImage.setVisibility(View.VISIBLE);
        } else {
            isGlutenFreeTv.setTextColor(Color.LTGRAY);
        }

        if(isLactoseFreeInt==1){
            isLactoseFreeImage.setVisibility(View.VISIBLE);
        } else {
            isLactoseFreeTv.setTextColor(Color.LTGRAY);
        }

        prepTimeTv.setText(prepTimeInt+"");
        cookTimeTv.setText(cookTimeInt+"");

    }


}
