package com.example.trafficroadworks.allActivity.fragmentViewPager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.example.trafficroadworks.allFragments.IncidentItemViewFragment;
import com.example.trafficroadworks.allFragments.PlannedRoadWorksItemViewFragment;
import com.example.trafficroadworks.allFragments.RoadworksItemViewFragment;


public class TrafficFirstPageViewPager extends FragmentPagerAdapter {

    //for binding fragment with view pager in Traffic first page

    public TrafficFirstPageViewPager(@NonNull FragmentManager fm) {
        super(fm);
    }

    // all the item fragment available in the view pager

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {

            //attach fragment to view pager

            case 0:
                return new IncidentItemViewFragment();
            case 1:
                return new RoadworksItemViewFragment();
            case 2:
                return new PlannedRoadWorksItemViewFragment();
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
                return "INCIDENTS";
            case 1:
                return "ROADWORKS";
            case 2:
                return "Planned Road Work";
            default:
                return null;
        }
    }

    // return the item count of fragment to view pager

    @Override
    public int getCount() {
        return 3;
    }

}
