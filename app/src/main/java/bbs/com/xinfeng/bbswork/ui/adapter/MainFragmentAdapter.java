package bbs.com.xinfeng.bbswork.ui.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import bbs.com.xinfeng.bbswork.base.BaseFragment;

/**
 * Created by dell on 2017/10/25.
 */

public class MainFragmentAdapter extends FragmentPagerAdapter {
    private List<BaseFragment> fragments;

    public MainFragmentAdapter(FragmentManager fm, List<BaseFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public BaseFragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

}
