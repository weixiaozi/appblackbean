package bbs.com.xinfeng.bbswork.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.base.BaseActivity;
import bbs.com.xinfeng.bbswork.databinding.ActivityMyBlacksBinding;
import bbs.com.xinfeng.bbswork.databinding.ActivityMyFansBinding;
import bbs.com.xinfeng.bbswork.domin.ErrorBean;
import bbs.com.xinfeng.bbswork.domin.MyBlackListBean;
import bbs.com.xinfeng.bbswork.domin.MyFansListBean;
import bbs.com.xinfeng.bbswork.domin.OutBlacksBus;
import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.presenter.MyFansPresenter;
import bbs.com.xinfeng.bbswork.ui.adapter.BlackListAdapter;
import bbs.com.xinfeng.bbswork.ui.adapter.FansListAdapter;
import bbs.com.xinfeng.bbswork.ui.adapter.FollowListAdapter;
import bbs.com.xinfeng.bbswork.utils.ToastUtil;

public class MyBlacksActivity extends BaseActivity<ActivityMyBlacksBinding, MyFansPresenter> implements NetContract.INetView {
    private static final int Blacklist_TAG = 30;
    private static final int ACTBLACK_OUT_TAG = 31;
    private List<MyBlackListBean.DataBean> mdatas = new ArrayList<>();
    private BlackListAdapter mAdapter;

    private int page = 1;
    private int selectPostion = -1;

    @Override
    protected MyFansPresenter creatPresenter() {
        return new MyFansPresenter(this);
    }

    @Override
    protected int initView(Bundle savedInstanceState) {
        return R.layout.activity_my_blacks;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mBinding.basebar.barTxtTitle.setText(R.string.mine_blacks);
        mBinding.basebar.barLeftClick.setVisibility(View.VISIBLE);
        mBinding.basebar.barLeftClick.setOnClickListener(v -> finish());

        initAdapter();
        loadMore();
    }

    private void initAdapter() {
        mAdapter = new BlackListAdapter(mdatas);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        });
        mAdapter.setmListen(new BlackListAdapter.onItemOperateListen() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(provideActivity(), UserInfoActivity.class);
                intent.putExtra("userid", mdatas.get(position).getUser_id());
                startActivity(intent);
            }

            @Override
            public void onDeleteBtnCilck(View view, int position) {
                selectPostion = position;
                mPresenter.actBlack(mdatas.get(position).getUser_id(), -1, ACTBLACK_OUT_TAG);
            }
        });
        mBinding.recycleMyblack.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recycleMyblack.setAdapter(mAdapter);

    }

    private void loadMore() {
        mPresenter.getBlackList(page, Blacklist_TAG);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void showLoading(int tag) {
        if (tag == Blacklist_TAG && mdatas.size() == 0)
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
        if (tag == Blacklist_TAG)
            showErrorEmptyView();
    }

    @Override
    public void setData(ErrorBean errorBean, int tag) {
        switch (tag) {
            case Blacklist_TAG:
                MyBlackListBean bean1 = (MyBlackListBean) errorBean;
                if (bean1.getData() != null && bean1.getData().size() > 0) {
                    page++;
                    mAdapter.addData(bean1.getData());
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
            case ACTBLACK_OUT_TAG:
                if (selectPostion != -1) {
                    mAdapter.remove(selectPostion);
                    showEmptyView();
                }
                break;
        }
    }

    @Override
    public void progress(int precent, int tag) {

    }

    private void showEmptyView() {
        if (mdatas.size() == 0) {
            View emptyView = provideActivity().getLayoutInflater().inflate(R.layout.empty_view_normal, (ViewGroup) mBinding.recycleMyblack.getParent(), false);
            if (mAdapter != null)
                mAdapter.setEmptyView(emptyView);
        }
    }

    private void showErrorEmptyView() {
        if (mdatas.size() == 0) {
            View emptyView = provideActivity().getLayoutInflater().inflate(R.layout.empty_view_error, (ViewGroup) mBinding.recycleMyblack.getParent(), false);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(OutBlacksBus outBlacksBus) {
        for (int i = 0; i < mdatas.size(); i++) {
            if (mdatas.get(i).getUser_id() == outBlacksBus.userid) {
                mAdapter.remove(i);
                showEmptyView();
                break;
            }
        }
    }

    @Override
    protected boolean isBindEventBus() {
        return true;
    }
}
