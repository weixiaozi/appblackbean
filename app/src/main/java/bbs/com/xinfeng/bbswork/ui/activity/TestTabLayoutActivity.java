package bbs.com.xinfeng.bbswork.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.os.Bundle;

import java.util.Arrays;
import java.util.List;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.base.BaseActivity;
import bbs.com.xinfeng.bbswork.databinding.ActivityTestTabLayoutBinding;
import bbs.com.xinfeng.bbswork.domin.ErrorBean;
import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.presenter.TestPresenter;
import bbs.com.xinfeng.bbswork.ui.fragment.MessageFragment;
import bbs.com.xinfeng.bbswork.ui.fragment.HomeFragment;

public class TestTabLayoutActivity extends BaseActivity<ActivityTestTabLayoutBinding, TestPresenter> implements NetContract.INetView {

    @Override
    protected TestPresenter creatPresenter() {
        return new TestPresenter(this);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected int initView(Bundle savedInstanceState) {
        return R.layout.activity_test_tab_layout;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mBinding.tabViewpager.setAdapter(new TabAdapter(Arrays.asList("tab1", "tab2", "tab3", "tab4", "tab5", "tab6", "tab7", "tab8", "tab9", "tab10"), Arrays.asList(new MessageFragment(),
                new HomeFragment(), new MessageFragment(), new HomeFragment(), new MessageFragment(), new HomeFragment(), new MessageFragment(), new HomeFragment(), new MessageFragment(), new HomeFragment())));
        mBinding.tablayout.setupWithViewPager(mBinding.tabViewpager);
    }

    @Override
    public void showLoading(int tag) {

    }

    @Override
    public void hideLoading(int tag) {

    }

    @Override
    public void showError(ErrorBean errorBean, int tag) {

    }

    @Override
    public void setData(ErrorBean errorBean, int tag) {

    }

    @Override
    public void progress(int precent, int tag) {

    }

    class TabAdapter extends FragmentStatePagerAdapter {
        private List<String> tabs;
        private List<Fragment> fragmentList;

        public TabAdapter(List<String> tabs, List<Fragment> fragmentList) {
            super(TestTabLayoutActivity.this.getSupportFragmentManager());
            this.tabs = tabs;
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs.get(position);
        }
    }
}
