package net.swierkowski.cookbook4.fragments;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import net.swierkowski.cookbook4.R;
import net.swierkowski.cookbook4.activities.RecipeDetailsActivity;
import net.swierkowski.cookbook4.listAdapters.IngredientAdapter;
import net.swierkowski.cookbook4.db.DatabaseAccess;


public class IngredientsFragment extends Fragment {

    private IngredientAdapter mIngredientAdapter;
    private ListView mListView;
    private Long mRecipeId;
    private Cursor mCursor;
    private DatabaseAccess mDbAccess;

    private OnFragmentInteractionListener mListener;

    public IngredientsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mDbAccess = DatabaseAccess.getInstance(getContext());
        mDbAccess.open();

        View rootView =  inflater.inflate(R.layout.fragment_ingredients, container, false);
        mListView=(ListView)rootView.findViewById(R.id.skladniki_lista);
        mRecipeId = RecipeDetailsActivity.RECIPE_ID;

        mCursor = mDbAccess.getIngredientsForRecipe(mRecipeId.toString());
        mIngredientAdapter = new IngredientAdapter(inflater.getContext(),mCursor);
        mListView.setAdapter(mIngredientAdapter);

        mDbAccess.close();
        return rootView;
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
