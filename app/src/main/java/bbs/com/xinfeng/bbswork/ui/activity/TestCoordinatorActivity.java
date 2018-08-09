package bbs.com.xinfeng.bbswork.ui.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.base.BaseActivity;
import bbs.com.xinfeng.bbswork.databinding.ActivityTestCoordinatorBinding;
import bbs.com.xinfeng.bbswork.domin.ErrorBean;
import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.presenter.TestPresenter;
import bbs.com.xinfeng.bbswork.ui.adapter.TestAdapter;
import bbs.com.xinfeng.bbswork.utils.ImageLoader;

public class TestCoordinatorActivity extends BaseActivity<ActivityTestCoordinatorBinding, TestPresenter> implements NetContract.INetView {
    private String url = "http://a.hiphotos.baidu.com/image/pic/item/6609c93d70cf3bc7176dd658db00baa1cd112a10.jpg";
    private String url1 = "http://f.hiphotos.baidu.com/image/pic/item/caef76094b36acaf0b35e44876d98d1000e99ca8.jpg";
    private String url2 = "http://d.hiphotos.baidu.com/image/pic/item/a8014c086e061d95a17f698c71f40ad162d9ca4d.jpg";
    private String url3 = "http://e.hiphotos.baidu.com/image/pic/item/c8177f3e6709c93d24085b43953df8dcd000548f.jpg";
    private String url4 = "http://g.hiphotos.baidu.com/image/pic/item/3b87e950352ac65c1e6b1bc7f1f2b21193138a13.jpg";
    private String url5 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509017612394&di=ea48ac8dad2e5096b76c866a0e970073&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fzhidao%2Fpic%2Fitem%2Fc2fdfc039245d688bb61de94a2c27d1ed21b249a.jpg";
    private String url6 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509017612392&di=68297110c72e28b90af28d3f35cf8266&imgtype=0&src=http%3A%2F%2Fimg.pconline.com.cn%2Fimages%2Fupload%2Fupc%2Ftx%2Fwallpaper%2F1210%2F08%2Fc1%2F14307187_1349676294934.jpg";
    private List<RecyclerView> viewList = new ArrayList<>();
    private List<String> tabList = new ArrayList<>();

    @Override
    protected TestPresenter creatPresenter() {
        return new TestPresenter(this);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected int initView(Bundle savedInstanceState) {
        return R.layout.activity_test_coordinator;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        setSupportActionBar(mBinding.toolbarCoordinator);
        new ImageLoader().setUrl(url).setmCacheStrategy(DiskCacheStrategy.ALL).into(mBinding.ivCoordinator).build();
        viewList.add(new RecyclerView(provideActivity()));
        viewList.add(new RecyclerView(provideActivity()));
        viewList.add(new RecyclerView(provideActivity()));

        tabList.add("No.1");
        tabList.add("No.2");
        tabList.add("No.3");
        mBinding.tabCoordinator.setTabMode(TabLayout.MODE_FIXED);
//        mBinding.tabCoordinator.addTab(mBinding.tabCoordinator.newTab().setText(tabList.get(0)));
//        mBinding.tabCoordinator.addTab(mBinding.tabCoordinator.newTab().setText(tabList.get(1)));
//        mBinding.tabCoordinator.addTab(mBinding.tabCoordinator.newTab().setText(tabList.get(2)));

        MyViewpagerAdapter pagerAdapter = new MyViewpagerAdapter();
        mBinding.viewpagerCoordinator.setAdapter(pagerAdapter);
        mBinding.tabCoordinator.setupWithViewPager(mBinding.viewpagerCoordinator);
        mBinding.tabCoordinator.setTabsFromPagerAdapter(pagerAdapter);
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

    class MyViewpagerAdapter extends PagerAdapter {
        List<String> datas;

        public MyViewpagerAdapter() {
            datas = new ArrayList<>();
            Flowable.range(1, 10).subscribe(new Consumer<Integer>() {
                @Override
                public void accept(Integer integer) throws Exception {
                    datas.add(url);
                    datas.add(url1);
                    datas.add(url2);
                    datas.add(url3);
                    datas.add(url4);
                    datas.add(url5);
                    datas.add(url6);
                }
            });

        }

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            RecyclerView recyclerView = viewList.get(position);
            TestAdapter testAdapter = new TestAdapter(provideActivity(), datas);
            recyclerView.setAdapter(testAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(provideActivity(), LinearLayoutManager.VERTICAL, false));

            container.addView(recyclerView);
            return recyclerView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewList.get(position));
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabList.get(position);
        }
    }
}
