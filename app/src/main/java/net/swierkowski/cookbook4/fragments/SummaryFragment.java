package net.swierkowski.cookbook4.fragments;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import net.swierkowski.cookbook4.R;
import net.swierkowski.cookbook4.activities.RecipeDetailsActivity;
import net.swierkowski.cookbook4.db.DatabaseAccess;
import net.swierkowski.cookbook4.model.Recipe;
import net.swierkowski.cookbook4.model.Tag;

import java.util.ArrayList;


public class SummaryFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Long mRecipeId;
    private Cursor mCursor;
    private TextView czasPrzygotowaniaTv;
    private TextView czasPieczeniaTv;
    private TextView opisTv;
    private TextView tagiTv;
    private ArrayList<Tag> mTagsList = new ArrayList<>();
    private Recipe mRecipe;
    private String tagsString;
    private DatabaseAccess mDbAccess;

    public SummaryFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_summary, container, false);
        mRecipeId = RecipeDetailsActivity.RECIPE_ID;

        mDbAccess = DatabaseAccess.getInstance(getContext());
        mDbAccess.open();

        //getting views
        czasPrzygotowaniaTv = (TextView) rootView.findViewById(R.id.sum_czas_przygotowania_ile);
        czasPieczeniaTv = (TextView) rootView.findViewById(R.id.sum_czas_pieczenia_ile);
        opisTv = (TextView) rootView.findViewById(R.id.sum_short_desc);
        tagiTv = (TextView) rootView.findViewById(R.id.sum_tagi);
        TextView tagiTv = (TextView) rootView.findViewById(R.id.sum_tagi);

        //getting most desc, cook and prep time from db
        mCursor = mDbAccess.getSummaryForRecipe(mRecipeId.toString());

        long idRec = Long.parseLong(mCursor.getString(0));
        String shortDesc = mCursor.getString(1);
        int prepTime  = mCursor.getInt(2);
        int cookTime  = mCursor.getInt(3);

        //getting tags from db
        mCursor = mDbAccess.getTagsForRecipe(mRecipeId.toString());

        do {
            mTagsList.add(new Tag(mCursor.getString(0)));
        } while (mCursor.moveToNext());

        //creating Recipe VO
        mRecipe = new Recipe();
        mRecipe.setId(idRec);
        mRecipe.setShortDescription(shortDesc);
        mRecipe.setPrepTime(prepTime);
        mRecipe.setTags(mTagsList);
        mRecipe.setCookingTime(cookTime);
        tagsString = getStringFromTagsArray(mRecipe.getTags());

        //binding data with views
        if(mRecipe.equals(null)) {
                Log.e("RECIPE","Recipe is NULL!! :(((");
        } else {
                opisTv.setText(mRecipe.getShortDescription());
                czasPrzygotowaniaTv.setText(String.valueOf(mRecipe.getPrepTime()));
                czasPieczeniaTv.setText(String.valueOf(mRecipe.getCookingTime()));
                tagiTv.setText(tagsString);
        }

        //favorite button
        mCursor = mDbAccess.isRecipeFavorite(mRecipeId.toString());
        int isFavorite = mCursor.getInt(0);
        mDbAccess.close();

        final ImageView favoriteEmptyBtn = (ImageView)rootView.findViewById(R.id.sum_favorite_empty);
        final ImageView favoriteFullBtn = (ImageView)rootView.findViewById(R.id.sum_favorite_full);

        if(isFavorite==1){
            setImagesVisibility(favoriteFullBtn,favoriteEmptyBtn);
        } else {
            setImagesVisibility(favoriteEmptyBtn,favoriteFullBtn);
        }

        favoriteEmptyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDbAccess.open();
                mDbAccess.updateRecipeWithFavorite(mRecipeId,1);
                setImagesVisibility(favoriteFullBtn,favoriteEmptyBtn);
                mDbAccess.close();
            }
        });

        favoriteFullBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDbAccess.open();
                mDbAccess.updateRecipeWithFavorite(mRecipeId,0);
                setImagesVisibility(favoriteEmptyBtn,favoriteFullBtn);
                mDbAccess.close();
            }
        });

        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private String getStringFromTagsArray(ArrayList<Tag> tags){
        StringBuilder builder = new StringBuilder();

        for (Tag tag : tags) {
            builder.append(tag+" ");
        }

        tagsString = builder.toString();
        return tagsString;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mListener = null;
    }

    public void setImagesVisibility(ImageView firstVisible, ImageView secondInvisible){
        firstVisible.setVisibility(View.VISIBLE);
        secondInvisible.setVisibility(View.INVISIBLE);
    }
}
