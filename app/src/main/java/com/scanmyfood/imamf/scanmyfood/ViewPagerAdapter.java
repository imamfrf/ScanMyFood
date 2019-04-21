package com.scanmyfood.imamf.scanmyfood;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

class ViewPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    public ViewPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int i) {

        switch (i) {
            case 0:
                FoodDescFragment tab1 = new FoodDescFragment();
                return tab1;
            case 1:
                MapsFragment tab2 = new MapsFragment();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0){
            return "Facts";
        }
        else if (position == 1){
            return "Maps";
        }
        return "OBJECT " + (position + 1);
    }
}


