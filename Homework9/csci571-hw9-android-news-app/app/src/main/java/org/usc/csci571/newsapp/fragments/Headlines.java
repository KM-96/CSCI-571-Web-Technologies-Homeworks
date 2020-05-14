package org.usc.csci571.newsapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.usc.csci571.newsapp.R;
import org.usc.csci571.newsapp.adapters.HeadlinesPagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class Headlines extends Fragment {
    private View view;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private HeadlinesPagerAdapter headlinesPagerAdapter;

    public Headlines() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_headlines_tab, container, false);
        init();
        return view;
    }

    private void init() {
        this.tabLayout = view.findViewById(R.id.headlines_tab_layout);
        this.viewPager = view.findViewById(R.id.headlines_view_pager);
        this.headlinesPagerAdapter = new HeadlinesPagerAdapter(getChildFragmentManager(),
                0, this.tabLayout.getTabCount());
        this.viewPager.setAdapter(this.headlinesPagerAdapter);
        this.tabLayout.addOnTabSelectedListener(this.onTabSelectedListener);
        this.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(this.tabLayout));
    }


    private TabLayout.OnTabSelectedListener onTabSelectedListener = new TabLayout.OnTabSelectedListener() {
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
    };
}