package net.swierkowski.cookbook4.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {

    int mNoOfTabs;

    public PagerAdapter(FragmentManager fm,int numberOfTabs){
        super(fm);
        this.mNoOfTabs=numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){

            case 0:
                SummaryFragment summary = new SummaryFragment();
                return summary;
            case 1:
                IngredientsFragment ingredients= new IngredientsFragment();
                return ingredients;
            case 2:
                DescriptionFragment description = new DescriptionFragment();
                return description;
            default:
                    return null;
        }
    }

    @Override
    public int getCount() {
        return mNoOfTabs;
    }
}
