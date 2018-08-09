package bbs.com.xinfeng.bbswork.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.SparseArray;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.base.BaseActivity;
import bbs.com.xinfeng.bbswork.databinding.ActivityTest2Binding;
import bbs.com.xinfeng.bbswork.domin.CollectionBean;
import bbs.com.xinfeng.bbswork.domin.ErrorBean;
import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.presenter.Test2Presenter;
import bbs.com.xinfeng.bbswork.ui.adapter.Test2Adapter;

public class Test2Activity extends BaseActivity<ActivityTest2Binding, Test2Presenter> implements NetContract.INetView, NetContract.IRefreshAndLoadMoreView {
    private static final int NORMAL = 1;
    private static final int LOADMORE = 2;
    private CollectionBean bean;
    private Test2Adapter test2Adapter;

    @Override
    public void showLoading(int tag) {

    }

    @Override
    public void hideLoading(int tag) {

    }

    @Override
    public void showError(ErrorBean errorBean, int tag) {
        mBinding.swipeRefresh.setRefreshing(false);
    }

    @Override
    protected Test2Presenter creatPresenter() {
        return new Test2Presenter(this, this);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected int initView(Bundle savedInstanceState) {
        return R.layout.activity_test2;
    }



    @Override
    protected void initData(Bundle savedInstanceState) {
        test2Adapter = new Test2Adapter(R.layout.item_test1, null);
        mBinding.recycleTest2.setLayoutManager(new LinearLayoutManager(provideActivity(), LinearLayoutManager.VERTICAL, false));
        mBinding.recycleTest2.setAdapter(test2Adapter);
        test2Adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.getCollectMoredata(LOADMORE);
            }
        });

        mPresenter.getCollectRefreshdata(NORMAL);

        mBinding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getCollectRefreshdata(NORMAL);
            }
        });
    }

    @Override
    public void setData(ErrorBean errorBean, int tag) {
        switch (tag) {
            case NORMAL:
                mBinding.swipeRefresh.setRefreshing(false);
                bean = (CollectionBean) errorBean;
                mPresenter.resetPageTotal(bean.getPageTotal(), bean.getPage());
                if (bean.getList() == null || bean.getList().size() <= 0)
                    noData();
                else
                    test2Adapter.setNewData(bean.getList());
                if (Integer.parseInt(bean.getPageTotal()) <= 1) {
                    test2Adapter.setEnableLoadMore(false);
                } else {
                    test2Adapter.setEnableLoadMore(true);
                }
                break;
            case LOADMORE:
                bean = (CollectionBean) errorBean;
                mPresenter.resetPageTotal(bean.getPageTotal(), bean.getPage());
                test2Adapter.addData(bean.getList());
                test2Adapter.loadMoreComplete();
                break;
        }
    }

    @Override
    public void progress(int precent, int tag) {

    }

    @Override
    public void noData() {
        mBinding.emptyView.llayoutEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public void noMoreData() {
        if (test2Adapter != null)
            test2Adapter.loadMoreEnd();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        SparseArray<String> stringSparseArray = new SparseArray<>();
    }

}
