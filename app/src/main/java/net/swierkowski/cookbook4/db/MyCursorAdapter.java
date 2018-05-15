package net.swierkowski.cookbook4.db;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.TextView;

import net.swierkowski.cookbook4.R;

public class MyCursorAdapter extends CursorAdapter {


    public MyCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.product, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView tvName = (TextView) view.findViewById(R.id.name);
        CheckBox cbRestriction = (CheckBox) view.findViewById(R.id.checkBox_product);
        // Extract properties from cursor
        String name = cursor.getString(cursor.getColumnIndexOrThrow("nazwa_produkty"));
        String productIdString = cursor.getString(cursor.getInt(1));
        long productId = Long.parseLong(productIdString);
        int restriction = cursor.getInt(cursor.getColumnIndexOrThrow("restrykcje"));
        // Populate fields with extracted properties
        tvName.setText(name);
        if(restriction==1){
            cbRestriction.setChecked(true);
        } else {cbRestriction.setChecked(false);}
    }
}
