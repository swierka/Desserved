package net.swierkowski.cookbook4.activities;

import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import net.swierkowski.cookbook4.R;
import net.swierkowski.cookbook4.db.DatabaseAccess;
import net.swierkowski.cookbook4.fragments.DescriptionFragment;
import net.swierkowski.cookbook4.fragments.IngredientsFragment;
import net.swierkowski.cookbook4.fragments.PagerAdapter;
import net.swierkowski.cookbook4.fragments.SummaryFragment;


public class RecipeDetailsActivity extends AppCompatActivity implements SummaryFragment.OnFragmentInteractionListener,
        IngredientsFragment.OnFragmentInteractionListener, DescriptionFragment.OnFragmentInteractionListener {

    public static final String RECIPE_ID_STRING = "recipeId";
    public static long RECIPE_ID;
    private DatabaseAccess mDbAccess;
    private Cursor mCursor;
    private long mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_detail);

        if (getIntent().getExtras() != null) {
            RECIPE_ID = getIntent().getLongExtra(RECIPE_ID_STRING, 0);
        }

        ImageView imageTv = (ImageView)findViewById(R.id.mainImage);
        TextView nameTv = (TextView)findViewById(R.id.mainName);

        //getting recipe name and photo from db
        mDbAccess = DatabaseAccess.getInstance(this);
        mDbAccess.open();
        mId = RECIPE_ID;
        mCursor =  mDbAccess.getImageAndNameForRecipeId(String.valueOf(mId));

        String name = mCursor.getString(0);
        String image = mCursor.getString(1);

        //assiging data to views
        nameTv.setText(name);
        int id = getResources().getIdentifier(image,"drawable",getPackageName());
        Drawable drawable = getResources().getDrawable(id);
        imageTv.setImageDrawable(drawable);

        mDbAccess.close();

        //tab layout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("W skrócie"));
        tabLayout.addTab(tabLayout.newTab().setText("Składniki"));
        tabLayout.addTab(tabLayout.newTab().setText("Opis"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
