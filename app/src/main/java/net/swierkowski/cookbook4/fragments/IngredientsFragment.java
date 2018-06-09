package net.swierkowski.cookbook4.fragments;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import net.swierkowski.cookbook4.R;
import net.swierkowski.cookbook4.activities.MainActivity;
import net.swierkowski.cookbook4.db.RecipesDbAdapter;


public class IngredientsFragment extends Fragment {

    private static final String RECIPE_ID = "recipeId";

    private IngredientAdapter mIngredientAdapterdapter;
    private ListView mListView;
    private String mRecipeId;
    private Cursor mCursor;

    private OnFragmentInteractionListener mListener;

    public IngredientsFragment() {}

    public static IngredientsFragment newInstance(String mRecipeId) {
        IngredientsFragment fragment = new IngredientsFragment();
        Bundle args = new Bundle();
        args.putString(RECIPE_ID, mRecipeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_ingredients, container, false);
        mListView=(ListView)rootView.findViewById(R.id.skladniki_lista);

        if (getArguments() != null) {
            mRecipeId = getArguments().getString(RECIPE_ID);
        }

        mCursor = MainActivity.mDbHelper.getIngredientsForRecipe(mRecipeId);
        mIngredientAdapterdapter = new IngredientAdapter(inflater.getContext(),mCursor);

        mListView.setAdapter(mIngredientAdapterdapter);
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
