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
import net.swierkowski.cookbook4.db.RecipesDbAdapter;
import net.swierkowski.cookbook4.db.SearchResultAdapter;
import net.swierkowski.cookbook4.model.Recipe;


public class IngredientsFragment extends ListFragment {

    private static final String RECIPE_ID = "recipe_id";

    private RecipesDbAdapter dbHelper;
    private SimpleCursorAdapter dataAdapter;
    private IngredientAdapter adapter;
    private ListView listView;
    private String mRecipeId;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_ingredients, container, false);

        dbHelper = new RecipesDbAdapter(getActivity());
        dbHelper.open();

        if (getArguments() != null) {
            mRecipeId = getArguments().getString(RECIPE_ID);
        }

        Cursor cursor = dbHelper.getIngredientsForRecipe(mRecipeId);
        listView=(ListView)rootView.findViewById(R.id.skladniki_lista);


        final String[] columns = new String[]{
                RecipesDbAdapter.Recipes.COLUMN_NAME_RECIPES_ID,
                RecipesDbAdapter.Products.COLUMN_NAME_PRODUCTS_NAME,
                RecipesDbAdapter.Ingredients.COLUMN_NAME_INGREDIENTS_AMOUNT,
        };

        int[] to = new int[]{
                R.id.recipe_id,
                R.id.skladnik_nazwa,
                R.id.skladnik_ilosc,
        };

        dataAdapter = new SimpleCursorAdapter(
                getContext(), R.layout.ingredients_cell,
                cursor,
                columns,
                to,
                0);



        adapter = new IngredientAdapter(inflater.getContext(),cursor);
        listView.setAdapter(adapter);
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
        dbHelper.close();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
