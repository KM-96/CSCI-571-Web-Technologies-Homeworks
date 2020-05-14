package org.usc.csci571.newsapp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.usc.csci571.newsapp.fragments.HeadlinesCards;
import org.usc.csci571.newsapp.utils.Constants;

public class HeadlinesPagerAdapter extends FragmentPagerAdapter {

    private int numberOfTabs;

    public HeadlinesPagerAdapter(@NonNull FragmentManager fm, int behavior, int numberOfTabs) {
        super(fm, behavior);
        this.numberOfTabs = numberOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return new HeadlinesCards(Constants.HEADLINES_TABS[position]);
    }

    @Override
    public int getCount() {
        return this.numberOfTabs;
    }
}
