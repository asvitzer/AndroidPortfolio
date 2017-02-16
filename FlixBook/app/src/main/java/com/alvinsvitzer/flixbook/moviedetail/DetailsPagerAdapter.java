package com.alvinsvitzer.flixbook.moviedetail;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.alvinsvitzer.flixbook.R;

/**
 * Created by Alvin on 2/15/17.
 */

public class DetailsPagerAdapter extends FragmentPagerAdapter{

    final int PAGE_COUNT = 3;
    private String tabTitles[];
    private Context mContext;
    private String mApiKey;
    private Parcelable mMovieParcelable;

    public DetailsPagerAdapter(FragmentManager fm, Context context, String apiKey, Parcelable movieParcelable) {
        super(fm);
        mContext = context;
        tabTitles = new String[]{mContext.getString(R.string.tab_title_details)
                                 ,mContext.getString(R.string.tab_title_people)
                                 ,mContext.getString(R.string.tab_title_reviews)};
        mApiKey = apiKey;
        mMovieParcelable = movieParcelable;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {

        return MovieOverviewFragment.newInstance(mMovieParcelable);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }


}
