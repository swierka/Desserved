package net.swierkowski.cookbook4.activities;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;

import net.swierkowski.cookbook4.R;
import net.swierkowski.cookbook4.fragments.DescriptionFragment;
import net.swierkowski.cookbook4.fragments.IngredientsFragment;
import net.swierkowski.cookbook4.fragments.PagerAdapter;
import net.swierkowski.cookbook4.fragments.SummaryFragment;

public class RecipeDetailsActivity extends AppCompatActivity implements SummaryFragment.OnFragmentInteractionListener,
        IngredientsFragment.OnFragmentInteractionListener, DescriptionFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_detail);


        TabLayout tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("W skrócie"));
        tabLayout.addTab(tabLayout.newTab().setText("Składniki"));
        tabLayout.addTab(tabLayout.newTab().setText("Opis"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        final ViewPager viewPager= (ViewPager)findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
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
