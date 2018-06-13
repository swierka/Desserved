package net.swierkowski.cookbook4.fragments;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.swierkowski.cookbook4.R;

import net.swierkowski.cookbook4.activities.RecipeDetailsActivity;
import net.swierkowski.cookbook4.db.DatabaseAccess;
import net.swierkowski.cookbook4.model.Recipe;


public class DescriptionFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private TextView mInstruction;
    private Long mRecipeId;
    private Cursor mCursor;
    private Recipe mRecipe;
    private DatabaseAccess mDbAccess;

    public DescriptionFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_description, container, false);
        mRecipeId = RecipeDetailsActivity.RECIPE_ID;

        //getting long desc from db
        mDbAccess = DatabaseAccess.getInstance(getContext());
        mDbAccess.open();
        mCursor = mDbAccess.getDescriptionForRecipeId(mRecipeId.toString());
        String longDesc = mCursor.getString(1);

        //creating Recipe VO
        mRecipe = new Recipe();
        mRecipe.setDescription(longDesc);

        //binding data with a view
        mInstruction = (TextView)rootView.findViewById(R.id.desc_fragment_tv);
        mInstruction.setText(Html.fromHtml(mRecipe.getDescription()));

        //closing db
        mDbAccess.close();

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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
