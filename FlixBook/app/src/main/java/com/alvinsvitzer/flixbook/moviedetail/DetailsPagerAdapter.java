package com.alvinsvitzer.flixbook.moviedetail;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.alvinsvitzer.flixbook.R;
import com.alvinsvitzer.flixbook.moviedetail.pagerfragments.MovieOverviewFragment;

/**
 * Created by Alvin on 2/15/17.
 */

public class DetailsPagerAdapter extends FragmentPagerAdapter{

    final int PAGE_COUNT = 3;
    private String tabTitles[];

    public DetailsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);

        tabTitles = new String[]{context.getString(R.string.tab_title_details)
                                 ,context.getString(R.string.tab_title_people)
                                 ,context.getString(R.string.tab_title_reviews)};
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {


        switch (position) {
            case 0:
                MovieOverviewFragment tab1 = new MovieOverviewFragment();
                return tab1;
            case 1:
                MovieOverviewFragment tab2 =  new MovieOverviewFragment();
                return tab2;
            case 2:
                MovieOverviewFragment tab3 =  new MovieOverviewFragment();
                return tab3;
            default:
                return null;
        }

    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }


}
