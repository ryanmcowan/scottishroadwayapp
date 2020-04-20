package com.example.trafficroadworks.allActivity.fragmentViewPager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.example.trafficroadworks.allFragments.ItemDetailsFragment;
import com.example.trafficroadworks.allFragments.ItemMapFragment;

public class TrafficSecondPageViewPager extends FragmentPagerAdapter {

    //for binding fragment with view pager in Traffic Second page

    public TrafficSecondPageViewPager(@NonNull FragmentManager fm) {
        super(fm);
    }

    // all the item fragment available in the view pager

    @NonNull
    @Override
    public Fragment getItem(int position) {

        //attach fragment to view pager

        switch (position) {
            case 0:
                return new ItemDetailsFragment();
            case 1:
                return new ItemMapFragment();

            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        //name of the fragment title which show in the viewpager tabLayout

        switch (position) {
            case 0:
                return "Details";
            case 1:
                return "Map";
            default:
                return null;
        }
    }

    // return the item count of fragment to view pager

    @Override
    public int getCount() {
        return 2;
    }

}
