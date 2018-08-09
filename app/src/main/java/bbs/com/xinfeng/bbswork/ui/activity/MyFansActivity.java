package bbs.com.xinfeng.bbswork.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.base.BaseActivity;
import bbs.com.xinfeng.bbswork.databinding.ActivityMyFansBinding;
import bbs.com.xinfeng.bbswork.domin.ErrorBean;
import bbs.com.xinfeng.bbswork.domin.MemberAllBean;
import bbs.com.xinfeng.bbswork.domin.MyFansListBean;
import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.presenter.MyFansPresenter;
import bbs.com.xinfeng.bbswork.ui.adapter.FansListAdapter;
import bbs.com.xinfeng.bbswork.ui.adapter.FollowListAdapter;
import bbs.com.xinfeng.bbswork.utils.ToastUtil;
import bbs.com.xinfeng.bbswork.widget.popwindow.PointPoppubWindow;

public class MyFansActivity extends BaseActivity<ActivityMyFansBinding, MyFansPresenter> implements NetContract.INetView {
    public static final int ACTION_FOLLOW = 1;
    public static final int ACTION_FANS = 2;

    private static final int fanslist_Tag = 300;
    private static final int followlist_Tag = 301;
    private static final int follow_act_tag = 302;
    private static final int cancelfollow_act_tag = 303;
    private List<MyFansListBean.DataBean> mdatas = new ArrayList<>();
    private int page = 1;
    private FansListAdapter mAdapter;
    private FollowListAdapter mFollowAdapter;
    private int action;
    private int selectPostion = -1;
    private PointPoppubWindow pointPop;

    @Override
    protected MyFansPresenter creatPresenter() {
        return new MyFansPresenter(this);
    }

    @Override
    protected int initView(Bundle savedInstanceState) {
        return R.layout.activity_my_fans;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        action = getIntent().getIntExtra("fansaction", 1);
        mBinding.basebar.barLeftClick.setVisibility(View.VISIBLE);
        mBinding.basebar.barLeftClick.setOnClickListener(v -> finish());
        if (action == ACTION_FANS) {
            mBinding.basebar.barTxtTitle.setText(R.string.mine_fans);
        } else {
            mBinding.basebar.barTxtTitle.setText(R.string.mine_follow);
        }
        initAdapter();
        loadMore();
    }

    private void initAdapter() {
        if (action == ACTION_FANS) {
            mAdapter = new FansListAdapter(mdatas);
            mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                @Override
                public void onLoadMoreRequested() {
                    loadMore();
                }
            });
            mAdapter.setmListen(new FansListAdapter.onItemOperateListen() {
                @Override
                public void operateItem(int position) {
                    MyFansListBean.DataBean bean = mdatas.get(position);
                    if (bean.getIsfollow() != 1) {
                        selectPostion = position;
                        followAction(1, bean.getUser_id(), follow_act_tag);
                    }
                }

                @Override
                public void goUserinfo(int position) {
                    Intent intent = new Intent(provideActivity(), UserInfoActivity.class);
                    intent.putExtra("userid", mdatas.get(position).getUser_id());
                    startActivity(intent);
                }
            });
            mBinding.recycleMyfans.setLayoutManager(new LinearLayoutManager(this));
            mBinding.recycleMyfans.setAdapter(mAdapter);
        } else {

            mFollowAdapter = new FollowListAdapter(mdatas);
            mFollowAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                @Override
                public void onLoadMoreRequested() {
                    loadMore();
                }
            });
            mFollowAdapter.setmListen(new FollowListAdapter.onItemOperateListen() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent intent = new Intent(provideActivity(), UserInfoActivity.class);
                    intent.putExtra("userid", mdatas.get(position).getUser_id());
                    startActivity(intent);
                }

                @Override
                public void onDeleteBtnCilck(View view, int position) {
                    if (pointPop == null)
                        pointPop = new PointPoppubWindow(provideActivity());
                    pointPop.setPointInfo("是否取消对此人的关注?", null, false, "取消", "确认");
                    pointPop.setDismissListener(new PointPoppubWindow.DismissListener() {
                        @Override
                        public void dismiss() {

                        }

                        @Override
                        public void clickOne() {

                        }

                        @Override
                        public void clickleft() {

                        }

                        @Override
                        public void clickRight() {
                            selectPostion = position;
                            followAction(-1, mdatas.get(position).getUser_id(), cancelfollow_act_tag);
                        }
                    });
                    pointPop.show();

                }
            });
            mBinding.recycleMyfans.setLayoutManager(new LinearLayoutManager(this));
            mBinding.recycleMyfans.setAdapter(mFollowAdapter);
        }

    }

    private void followAction(int type, int userid, int tag) {
        mPresenter.followAction(type, userid, tag);
    }

    private void loadMore() {
        if (action == ACTION_FANS) {
            getList(2, fanslist_Tag);
        } else {
            getList(1, followlist_Tag);
        }
    }

    private void getList(int type, int tag) {
        mPresenter.getFansList(page, type, tag);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void showLoading(int tag) {
        if ((tag == fanslist_Tag || tag == followlist_Tag) && mdatas.size() == 0)
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
        if ((tag == fanslist_Tag || tag == followlist_Tag))
            showErrorEmptyView();
    }

    @Override
    public void setData(ErrorBean errorBean, int tag) {
        switch (tag) {
            case fanslist_Tag:
                MyFansListBean bean = (MyFansListBean) errorBean;
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
            case followlist_Tag:
                MyFansListBean bean1 = (MyFansListBean) errorBean;
                if (bean1.getData() != null && bean1.getData().size() > 0) {
                    page++;
                    mFollowAdapter.addData(bean1.getData());
                    mFollowAdapter.setEnableLoadMore(true);
                    mFollowAdapter.loadMoreComplete();
                } else {
                    if (page == 1) {
                        showEmptyView();
                    }
                    mFollowAdapter.setEnableLoadMore(false);
                    mFollowAdapter.loadMoreEnd();
                }
                break;
            case follow_act_tag:
                if (selectPostion != -1) {
                    mdatas.get(selectPostion).setIsfollow(1);
                    mAdapter.notifyItemChanged(selectPostion);
                }
                break;
            case cancelfollow_act_tag:
                if (selectPostion != -1) {
                    mFollowAdapter.remove(selectPostion);
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
            View emptyView = provideActivity().getLayoutInflater().inflate(R.layout.empty_view_normal, (ViewGroup) mBinding.recycleMyfans.getParent(), false);
            if (mAdapter != null)
                mAdapter.setEmptyView(emptyView);
            if (mFollowAdapter != null)
                mFollowAdapter.setEmptyView(emptyView);
        }
    }

    private void showErrorEmptyView() {
        if (mdatas.size() == 0) {
            View emptyView = provideActivity().getLayoutInflater().inflate(R.layout.empty_view_error, (ViewGroup) mBinding.recycleMyfans.getParent(), false);
            emptyView.findViewById(R.id.txt_empty_retry).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadMore();
                }
            });
            if (mAdapter != null)
                mAdapter.setEmptyView(emptyView);
            if (mFollowAdapter != null)
                mFollowAdapter.setEmptyView(emptyView);
        }
    }
}
