package bbs.com.xinfeng.bbswork.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.base.BaseActivity;
import bbs.com.xinfeng.bbswork.databinding.ActivityClickMeBinding;
import bbs.com.xinfeng.bbswork.databinding.ActivityReplyMeBinding;
import bbs.com.xinfeng.bbswork.domin.ClickMeListBean;
import bbs.com.xinfeng.bbswork.domin.DeleteThemeBus;
import bbs.com.xinfeng.bbswork.domin.ErrorBean;
import bbs.com.xinfeng.bbswork.domin.ReplyMeListBean;
import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.presenter.MyReplyPresenter;
import bbs.com.xinfeng.bbswork.ui.adapter.ClickMeAdapter;
import bbs.com.xinfeng.bbswork.ui.adapter.ReplyMeAdapter;
import bbs.com.xinfeng.bbswork.utils.ToastUtil;
import bbs.com.xinfeng.bbswork.widget.popwindow.PointPoppubWindow;

public class ClickMeActivity extends BaseActivity<ActivityClickMeBinding, MyReplyPresenter> implements NetContract.INetView {
    private static final int replymeList_Tag = 300;
    private List<ClickMeListBean.DataBean> mDatas = new ArrayList<>();
    private ClickMeAdapter mAdapter;
    private int pageid;
    private PointPoppubWindow pointPop;

    @Override
    protected MyReplyPresenter creatPresenter() {
        return new MyReplyPresenter(this);
    }

    @Override
    protected int initView(Bundle savedInstanceState) {
        return R.layout.activity_click_me;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mBinding.basebar.barTxtTitle.setText(R.string.mes_click_me);
        mBinding.basebar.barLeftClick.setVisibility(View.VISIBLE);
        mBinding.basebar.barLeftClick.setOnClickListener(v -> finish());
        initAdapter();
        loadMore();
    }

    private void initAdapter() {
        mAdapter = new ClickMeAdapter(mDatas);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        });
        mBinding.recycleClickme.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recycleClickme.setAdapter(mAdapter);
        mAdapter.setmListen(new ClickMeAdapter.onItemOperateListen() {
            @Override
            public void operateItem(int position) {
                ClickMeListBean.DataBean dataBean = mDatas.get(position);
                if (dataBean.getTopic().getIsjoin() == 1) {
                    if (dataBean.getThread().getStatus() == 1) {
                        if (dataBean.getReply_status() == 1) {
                            Intent intent = new Intent(provideActivity(), ThemeDetailActivity.class);
                            intent.putExtra("topicid", dataBean.getTopic_id());
                            intent.putExtra("themeid", dataBean.getThread_id());
                            if (dataBean.getTypeX() == 3)
                                intent.putExtra("replyid", dataBean.getReply_pid());
                            startActivity(intent);
                        } else {
                            ToastUtil.showToast("该评论已删除");
                        }
                    } else {
                        ToastUtil.showToast("该观点已删除");
                    }
                } else {
                    if (pointPop == null) {
                        pointPop = new PointPoppubWindow(provideActivity());
                        pointPop.setPointInfo("您已经不在此话题中，无法查看此观点，是否现在申请加入此话题？",
                                "", false, "取消", "现在申请");
                    }
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
                            Intent intent = new Intent(provideActivity(), TopicDeatilOtherActivity.class);
                            intent.putExtra("topicid", dataBean.getTopic().getId() + "");
                            startActivity(intent);
                        }
                    });
                    pointPop.show();
                }

            }

            @Override
            public void goUserInfo(int position) {
                Intent intent = new Intent(provideActivity(), UserInfoActivity.class);
                intent.putExtra("userid", mDatas.get(position).getAtUsers().getId());
                startActivity(intent);
            }
        });
    }

    private void loadMore() {
        mPresenter.getClickme(pageid, replymeList_Tag);
    }


    @Override
    protected void initEvent() {

    }

    @Override
    public void showLoading(int tag) {
        if (tag == replymeList_Tag && mDatas.size() == 0)
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
        if (tag == replymeList_Tag)
            showErrorEmptyView();
    }

    @Override
    public void setData(ErrorBean errorBean, int tag) {
        switch (tag) {
            case replymeList_Tag:
                if (errorBean instanceof ClickMeListBean) {
                    ClickMeListBean bean = (ClickMeListBean) errorBean;
                    if (bean.getData() != null && bean.getData().size() > 0) {
                        pageid = bean.getData().get(bean.getData().size() - 1).getPageId();
                        mAdapter.addData(bean.getData());
                        mAdapter.setEnableLoadMore(true);
                        mAdapter.loadMoreComplete();
                    } else {
                        if (pageid == 0) {
                            showEmptyView();
                        }
                        mAdapter.setEnableLoadMore(false);
                        mAdapter.loadMoreEnd();
                    }

                }
                break;
        }
    }

    @Override
    public void progress(int precent, int tag) {

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(DeleteThemeBus e) {
        for (ClickMeListBean.DataBean bean : mDatas) {
            if (bean.getThread_id() == e.themeId) {
                bean.getThread().setStatus(0);
            }
        }

    }

    @Override
    protected boolean isBindEventBus() {
        return true;
    }

    private void showEmptyView() {
        if (mDatas.size() == 0) {
            View emptyView = provideActivity().getLayoutInflater().inflate(R.layout.empty_view_normal, (ViewGroup) mBinding.recycleClickme.getParent(), false);
            mAdapter.setEmptyView(emptyView);
        }
    }

    private void showErrorEmptyView() {
        if (mDatas.size() == 0) {
            View emptyView = provideActivity().getLayoutInflater().inflate(R.layout.empty_view_error, (ViewGroup) mBinding.recycleClickme.getParent(), false);
            emptyView.findViewById(R.id.txt_empty_retry).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadMore();
                }
            });
            mAdapter.setEmptyView(emptyView);
        }
    }
}
