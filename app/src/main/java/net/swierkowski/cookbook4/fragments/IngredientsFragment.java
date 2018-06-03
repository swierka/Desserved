package net.swierkowski.cookbook4.fragments;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import net.swierkowski.cookbook4.R;
import net.swierkowski.cookbook4.db.RecipesDbAdapter;
import net.swierkowski.cookbook4.db.SearchResultAdapter;
import net.swierkowski.cookbook4.model.Recipe;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link IngredientsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link IngredientsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IngredientsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String RECIPE_ID = "recipe_id";

    // TODO: Rename and change types of parameters
    private RecipesDbAdapter dbHelper;
    private SimpleCursorAdapter dataAdapter;
    private IngredientAdapter adapter;
    private ListView listView;
    private String mRecipeId;

    private OnFragmentInteractionListener mListener;

    public IngredientsFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment IngredientsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IngredientsFragment newInstance(String mRecipeId) {
        IngredientsFragment fragment = new IngredientsFragment();
        Bundle args = new Bundle();
        args.putString(RECIPE_ID, mRecipeId);
        fragment.setArguments(args);
        return fragment;
    }

/*
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_ingredients, container, false);

        dbHelper = new RecipesDbAdapter(getActivity());
        dbHelper.open();

        if (getArguments() != null) {
            mRecipeId = getArguments().getString(RECIPE_ID);
        }

        Cursor cursor = dbHelper.getIngredientsForRecipe(mRecipeId);
        listView=(ListView)view.findViewById(R.id.skladniki_lista);


        final String[] columns = new String[]{
                RecipesDbAdapter.Ingredients.COLUMN_NAME_INGREDIENTS_ID_RECIPE,
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
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
