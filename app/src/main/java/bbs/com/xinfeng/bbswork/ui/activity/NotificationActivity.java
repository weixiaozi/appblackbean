package bbs.com.xinfeng.bbswork.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.base.BaseActivity;
import bbs.com.xinfeng.bbswork.databinding.ActivityNotificationBinding;
import bbs.com.xinfeng.bbswork.domin.ErrorBean;
import bbs.com.xinfeng.bbswork.domin.MesReduceBus;
import bbs.com.xinfeng.bbswork.domin.NoticeChangeBean;
import bbs.com.xinfeng.bbswork.domin.NotificationBean;
import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.presenter.NotificationPresenter;
import bbs.com.xinfeng.bbswork.ui.adapter.NotificationAdapter;
import bbs.com.xinfeng.bbswork.utils.LogUtil;
import bbs.com.xinfeng.bbswork.utils.ToastUtil;

public class NotificationActivity extends BaseActivity<ActivityNotificationBinding, NotificationPresenter> implements NetContract.INetView {
    private static final int notifications_Tag = 300;
    private static final int notifications_more_Tag = 301;
    private static final int notifications_Topmore_Tag = 305;
    private static final int notifications_Updata_Tag = 306;
    private static final int Apply_operate_Tag = 302;
    private static final int Invite_operate_Tag = 303;
    private static final int ClearNotice_Tag = 304;
    private List<NotificationBean.DataBean> mdatas = new ArrayList<>();
    private int page = 1;
    private NotificationAdapter mAdapter;
    private int operatePosition;
    private int selectId;

    @Override
    protected NotificationPresenter creatPresenter() {
        return new NotificationPresenter(this);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected int initView(Bundle savedInstanceState) {
        return R.layout.activity_notification;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mBinding.basebar.barTxtTitle.setText(R.string.notification);
        mBinding.basebar.barLeftClick.setVisibility(View.VISIBLE);

        mBinding.basebar.barLeftClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initAdapter();
        setOnRefresh();
        refresh();
        EventBus.getDefault().post(new MesReduceBus(getIntent().getIntExtra("jpushtype", 0)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (selectId != 0) {
            mPresenter.getNewNotifications(selectId, 0, notifications_Updata_Tag);
            selectId = 0;
        }
    }

    private void setOnRefresh() {
        mBinding.swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    private void refresh() {
        mAdapter.setEnableLoadMore(false);
        mPresenter.getNewNotifications(0, 1, notifications_Tag);
    }

    private void initAdapter() {
        mAdapter = new NotificationAdapter(mdatas);
        mAdapter.setmListen(new NotificationAdapter.onItemOperateListen() {
            @Override
            public void operateItem(int position) {
                operatePosition = position;
                NotificationBean.DataBean dataBean = mdatas.get(position);
                if (dataBean.getTypeX() == NotificationBean.OPERATE_101) {
                    mPresenter.operationNotification(dataBean.getTopic_id(), dataBean.getUser_id(), dataBean.getLog_id(), null, 5, Apply_operate_Tag);
                } else if (dataBean.getTypeX() == NotificationBean.OPERATE_201) {
                    mPresenter.inviteOperationNotification(dataBean.getTopic_id(), dataBean.getLog_id(), 1, null, Invite_operate_Tag);
                }
            }

            @Override
            public void onItemClick(int position) {
                if (mdatas.get(position).getTypeX() != NotificationBean.OPERATE_104 && mdatas.get(position).getTypeX() != NotificationBean.OPERATE_113) {
                    selectId = mdatas.get(position).getNotice_id();
                    Intent intent = new Intent(provideActivity(), NotifyDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("noticedetail", mdatas.get(position));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        });
        mBinding.recycleNotification.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recycleNotification.setAdapter(mAdapter);

    }

    private void loadMore() {
        if (mdatas.size() > 0) {
            mPresenter.getNewNotifications(mdatas.get(mdatas.size() - 1).getNotice_id(), -1, notifications_more_Tag);
        }
    }

    @Override
    public void showLoading(int tag) {
        if (tag == notifications_Tag && mdatas.size() == 0)
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
        if (tag == notifications_Tag && page == 1) {
            mBinding.swipeLayout.setRefreshing(false);
            showErrorEmptyView();
        }
    }

    @Override
    public void setData(ErrorBean errorBean, int tag) {
        switch (tag) {
            case notifications_Tag:
                NotificationBean bean = (NotificationBean) errorBean;
                mBinding.swipeLayout.setRefreshing(false);
                mdatas.clear();
                if (bean.getData() != null && bean.getData().size() > 0) {
                    page++;
                    mdatas.addAll(bean.getData());
                    mAdapter.setNewData(mdatas);
                    mAdapter.setEnableLoadMore(true);
                    mAdapter.loadMoreComplete();
                } else {
                    mAdapter.setNewData(mdatas);
                    mAdapter.setEnableLoadMore(false);
                    mAdapter.loadMoreEnd();
                }
                showEmptyView();
                break;
            case notifications_more_Tag:
                NotificationBean bean1 = (NotificationBean) errorBean;
                if (bean1.getData() != null && bean1.getData().size() > 0) {
                    mAdapter.addData(bean1.getData());
                    mAdapter.setEnableLoadMore(true);
                    mAdapter.loadMoreComplete();
                } else {
                    mAdapter.setEnableLoadMore(false);
                    mAdapter.loadMoreEnd();
                }
                break;
            case notifications_Topmore_Tag:
                NotificationBean bean2 = (NotificationBean) errorBean;
                if (bean2.getData() != null && bean2.getData().size() > 0) {
                    mAdapter.addData(0, bean2.getData());
                    showEmptyView();
                }

                break;
            case notifications_Updata_Tag:
                NotificationBean bean3 = (NotificationBean) errorBean;
                if (bean3.getData() != null && bean3.getData().size() > 0) {
                    for (NotificationBean.DataBean newData : bean3.getData()) {
                        for (int i = 0; i < mdatas.size(); i++) {
                            if (newData.getLog_id() == mdatas.get(i).getLog_id()) {
                                mAdapter.setData(i, newData);
                                break;
                            }
                        }
                    }
                }

                break;
            case Apply_operate_Tag:
                mdatas.get(operatePosition).setTypeX(NotificationBean.OPERATE_102);
                mAdapter.notifyItemChanged(operatePosition);
                break;
            case Invite_operate_Tag:
                mdatas.get(operatePosition).setTypeX(NotificationBean.OPERATE_202);
                mAdapter.notifyItemChanged(operatePosition);
                break;
            case ClearNotice_Tag:
                ToastUtil.showToast("通知已清空");
                mdatas.clear();
                mAdapter.notifyDataSetChanged();
                showEmptyView();
                break;
        }
    }

    @Override
    public void progress(int precent, int tag) {

    }

    private void showEmptyView() {
        if (mdatas.size() == 0) {
            View emptyView = provideActivity().getLayoutInflater().inflate(R.layout.empty_view_normal, (ViewGroup) mBinding.recycleNotification.getParent(), false);
            mAdapter.setEmptyView(emptyView);
            mBinding.basebar.barRightClick.setVisibility(View.INVISIBLE);
        } else {
            mBinding.basebar.barRightClick.setVisibility(View.VISIBLE);
            mBinding.basebar.barRightClick.setVisibility(View.VISIBLE);
            mBinding.basebar.barRightTxt.setText("清空");
            mBinding.basebar.barRightClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.clearNotification(ClearNotice_Tag);
                }
            });
        }
    }

    private void showErrorEmptyView() {
        if (mdatas.size() == 0) {
            View emptyView = provideActivity().getLayoutInflater().inflate(R.layout.empty_view_error, (ViewGroup) mBinding.recycleNotification.getParent(), false);
            emptyView.findViewById(R.id.txt_empty_retry).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    refresh();
                }
            });
            if (mAdapter != null)
                mAdapter.setEmptyView(emptyView);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(NoticeChangeBean.DataBean e) {
        if (e.getTypeX() == 13 && e.getNum() > 0) {
            mPresenter.getNewNotifications(mdatas.size() == 0 ? 0 : mdatas.get(0).getNotice_id(), 1, notifications_Topmore_Tag);
        }

    }

    @Override
    protected boolean isBindEventBus() {
        return true;
    }

}
