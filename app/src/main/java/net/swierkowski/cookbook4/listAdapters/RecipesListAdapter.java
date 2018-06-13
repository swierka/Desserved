package net.swierkowski.cookbook4.listAdapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.swierkowski.cookbook4.R;
import net.swierkowski.cookbook4.model.Recipe;

import java.util.ArrayList;

public class RecipesListAdapter extends BaseAdapter{

    private Context mContext;
    private int mLayout;
    private ArrayList<Recipe> mRecipesList;


    public RecipesListAdapter(Context context, int layout, ArrayList<Recipe> recipesList) {
        mContext = context;
        mLayout = layout;
        mRecipesList = recipesList;
    }

    @Override
    public int getCount() {
        return mRecipesList.size();
    }

    @Override
    public Object getItem(int position) {
        return mRecipesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        ImageView mImage;
        TextView mName;

        public ViewHolder(ImageView image, TextView name) {
            mImage = image;
            mName = name;
        }
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        if(view==null){
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(mLayout,null);

            ImageView mImage = (ImageView)view.findViewById(R.id.grid_image_recipe);
            TextView mText = (TextView)view.findViewById(R.id.grid_name_recipe);

            ViewHolder holder = new ViewHolder(mImage,mText);

            view.setTag(holder);
        }
        ViewHolder holder = (ViewHolder)view.getTag();

        Recipe recipe = mRecipesList.get(position);

        holder.mName.setText(recipe.getName());

        int id = view.getResources().getIdentifier(mRecipesList.get(position).getPhoto(),"drawable",mContext.getPackageName());
        Drawable drawable = view.getResources().getDrawable(id);
        holder.mImage.setImageDrawable(drawable);

        return view;
    }
}
