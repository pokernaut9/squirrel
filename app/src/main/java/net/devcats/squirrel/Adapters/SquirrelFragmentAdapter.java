package net.devcats.squirrel.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import net.devcats.squirrel.R;

import java.util.List;

public class SquirrelFragmentAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private List<Fragment> mFragments;

    public SquirrelFragmentAdapter(Context context, FragmentManager fragmentManager, List<Fragment> fragments) {
        super(fragmentManager);
        mContext = context;
        mFragments = fragments;
    }

    public void updateAdapter(int position, Fragment newFragment) {
        mFragments.remove(position);
        mFragments.add(position, newFragment);

        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getStringArray(R.array.fragment_titles)[position];
    }

}