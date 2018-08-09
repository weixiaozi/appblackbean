package bbs.com.xinfeng.bbswork.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.base.BaseActivity;
import bbs.com.xinfeng.bbswork.databinding.ActivitySystemNotifyBinding;
import bbs.com.xinfeng.bbswork.domin.ErrorBean;
import bbs.com.xinfeng.bbswork.domin.MesReduceBus;
import bbs.com.xinfeng.bbswork.domin.SystemNoticesBean;
import bbs.com.xinfeng.bbswork.domin.TopicListBean;
import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.presenter.SystemNoticePresenter;
import bbs.com.xinfeng.bbswork.ui.adapter.SystemNoticeListAdapter;
import bbs.com.xinfeng.bbswork.ui.adapter.TopicListAdapter;
import bbs.com.xinfeng.bbswork.utils.ToastUtil;

public class SystemNotifyActivity extends BaseActivity<ActivitySystemNotifyBinding, SystemNoticePresenter> implements NetContract.INetView {
    private static final int noticelist_Tag = 300;
    private List<SystemNoticesBean.DataBean> mdatas = new ArrayList<>();
    private SystemNoticeListAdapter mAdapter;
    private int page = 1;
    private int noticetype;

    @Override
    protected SystemNoticePresenter creatPresenter() {
        return new SystemNoticePresenter(this);
    }

    @Override
    protected int initView(Bundle savedInstanceState) {
        return R.layout.activity_system_notify;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        noticetype = getIntent().getIntExtra("noticetype", 0);
        if (noticetype == 1) {
            mBinding.basebar.barTxtTitle.setText(R.string.mes_system);
        } else if (noticetype == 2) {
            mBinding.basebar.barTxtTitle.setText(R.string.mes_operate);
        }
        mBinding.basebar.barLeftClick.setVisibility(View.VISIBLE);
        mBinding.basebar.barLeftClick.setOnClickListener(v -> finish());
        initAdapter();
        loadMore();
        EventBus.getDefault().post(new MesReduceBus(getIntent().getIntExtra("jpushtype", 0)));
    }

    private void initAdapter() {
        mAdapter = new SystemNoticeListAdapter(mdatas);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        });
        mBinding.recycleNotices.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recycleNotices.setAdapter(mAdapter);
    }

    private void loadMore() {
        mPresenter.getNoticeList(noticetype, page, noticelist_Tag);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void showLoading(int tag) {
        if (tag == noticelist_Tag && mdatas.size() == 0)
            startLoading();
    }

    @Override
    public void hideLoading(int tag) {
        stopLoading();
    }

    @Override
    public void showError(ErrorBean errorBean, int tag) {
        if (errorBean.androidType.equals(ErrorBean.TYPE_SHOW)) {
            ToastUtil.showToast(errorBean.desc);
        }
        if (tag == noticelist_Tag) {
            mAdapter.loadMoreComplete();
            showErrorEmptyView();
        }
    }

    @Override
    public void setData(ErrorBean errorBean, int tag) {
        switch (tag) {
            case noticelist_Tag:
                SystemNoticesBean bean = (SystemNoticesBean) errorBean;
                if (bean.getData() != null && bean.getData().size() > 0) {
                    page++;
                    mAdapter.addData(bean.getData());
                    mAdapter.setEnableLoadMore(true);
                    mAdapter.loadMoreComplete();
                } else {
                    if (page == 1) {
                        showEmptyView();
                    }
                    mAdapter.setEnableLoadMore(false);
                    mAdapter.loadMoreEnd();
                }
                break;
        }
    }

    @Override
    public void progress(int precent, int tag) {

    }

    private void showEmptyView() {
        if (mdatas.size() == 0) {
            View emptyView = provideActivity().getLayoutInflater().inflate(R.layout.empty_view_normal, (ViewGroup) mBinding.recycleNotices.getParent(), false);
            mAdapter.setEmptyView(emptyView);
        }
    }

    private void showErrorEmptyView() {
        if (mdatas.size() == 0) {
            View emptyView = provideActivity().getLayoutInflater().inflate(R.layout.empty_view_error, (ViewGroup) mBinding.recycleNotices.getParent(), false);
            emptyView.findViewById(R.id.txt_empty_retry).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadMore();
                }
            });
            if (mAdapter != null)
                mAdapter.setEmptyView(emptyView);
        }
    }
}
