package bbs.com.xinfeng.bbswork.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
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
import bbs.com.xinfeng.bbswork.databinding.ActivityMyThemeBinding;
import bbs.com.xinfeng.bbswork.domin.AttachThemeBean;
import bbs.com.xinfeng.bbswork.domin.ClickMeListBean;
import bbs.com.xinfeng.bbswork.domin.CollectionInnerBean;
import bbs.com.xinfeng.bbswork.domin.DeleteThemeBus;
import bbs.com.xinfeng.bbswork.domin.ErrorBean;
import bbs.com.xinfeng.bbswork.domin.MyThemeListBean;
import bbs.com.xinfeng.bbswork.domin.NoticeChangeBean;
import bbs.com.xinfeng.bbswork.domin.OutTopicBus;
import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.presenter.MyThemePresenter;
import bbs.com.xinfeng.bbswork.ui.adapter.MyThemeAdapter;
import bbs.com.xinfeng.bbswork.ui.fragment.CollectionFragment;
import bbs.com.xinfeng.bbswork.utils.ToastUtil;
import bbs.com.xinfeng.bbswork.widget.popwindow.PointPoppubWindow;


public class MyThemeActivity extends BaseActivity<ActivityMyThemeBinding, MyThemePresenter> implements NetContract.INetView {
    private static final int themeList_Tag = 300;
    private static final int updata_Tag = 301;
    private static final int ReadTheme_TAG = 302;
    private List<MyThemeListBean.DataBean> mDatas = new ArrayList<>();
    private MyThemeAdapter mAdapter;
    private int threadId;
    private PointPoppubWindow pointPop;

    @Override
    protected MyThemePresenter creatPresenter() {
        return new MyThemePresenter(this);
    }

    @Override
    protected int initView(Bundle savedInstanceState) {
        return R.layout.activity_my_theme;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mBinding.basebar.barTxtTitle.setText(R.string.mine_theme);
        mBinding.basebar.barLeftClick.setVisibility(View.VISIBLE);
        mBinding.basebar.barLeftClick.setOnClickListener(v -> finish());
        initAdapter();
        loadMore();
    }


    private void initAdapter() {
        mAdapter = new MyThemeAdapter(mDatas, provideActivity());
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        });
        mBinding.recycleMytheme.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recycleMytheme.setAdapter(mAdapter);
        mAdapter.setmListen(new MyThemeAdapter.onItemOperateListen() {
            @Override
            public void operateItem(int position) {
                MyThemeListBean.DataBean dataBean = mDatas.get(position);
                if (dataBean.getTopic().getIsjoin() == 1) {
                    Intent intent = new Intent(provideActivity(), ThemeDetailActivity.class);
                    intent.putExtra("topicid", dataBean.getTopic_id());
                    intent.putExtra("themeid", dataBean.getId());
                    startActivity(intent);
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
            public void clickAudio(int position) {
                if (mDatas.get(position).getAudioread() != 1) {
                    mPresenter.readThemeAudio(mDatas.get(position).getId(), ReadTheme_TAG);
                }
            }
        });
    }

    private void loadMore() {
        mPresenter.getMyThemeList(threadId, themeList_Tag);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void showLoading(int tag) {
        if (tag == themeList_Tag && mDatas.size() == 0)
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
        if (tag == themeList_Tag)
            showErrorEmptyView();
    }

    @Override
    public void setData(ErrorBean errorBean, int tag) {
        switch (tag) {
            case themeList_Tag:
                if (errorBean instanceof MyThemeListBean) {
                    MyThemeListBean bean = (MyThemeListBean) errorBean;
                    if (bean.getData() != null && bean.getData().size() > 0) {
                        dealDatasForDB(bean.getData());
                        threadId = bean.getData().get(bean.getData().size() - 1).getId();
                        mAdapter.addData(bean.getData());
                        mAdapter.setEnableLoadMore(true);
                        mAdapter.loadMoreComplete();
                    } else {
                        if (threadId == 0) {
                            showEmptyView();
                        }
                        mAdapter.setEnableLoadMore(false);
                        mAdapter.loadMoreEnd();
                    }
                }
                break;
            case updata_Tag:
                if (errorBean instanceof MyThemeListBean) {
                    MyThemeListBean bean = (MyThemeListBean) errorBean;
                    if (bean.getData() != null && bean.getData().size() > 0) {
                        dealDatasForDB(bean.getData());
                        for (MyThemeListBean.DataBean newData : bean.getData()) {
                            for (int i = 0; i < mDatas.size(); i++) {
                                if (newData.getId() == mDatas.get(i).getId()) {
                                    mDatas.set(i, newData);
                                    mAdapter.notifyItemChanged(i);
                                    break;
                                }
                            }
                        }
                    }
                }
                break;
        }
    }

    private void dealDatasForDB(List<MyThemeListBean.DataBean> datas) {
        for (MyThemeListBean.DataBean data : datas) {
            if (data.getAttch() != null && data.getAttch().size() > 0) {
                for (AttachThemeBean attach : data.getAttch()) {
                    if (attach.getMime() == 1) {
                        data.setVideoType(attach.getMime());
                        data.getSrc().add(attach.getUrl());
                        data.getThumb().add(attach.getUrl2());
                    } else if (attach.getMime() == 2) {
                        data.setVideoType(attach.getMime());
                        data.setVideoPath(attach.getUrl());
                        data.setDuration(Integer.parseInt(Uri.parse(attach.getUrl()).getQueryParameter("t")));
                    } else if (attach.getMime() == 3) {
                        data.setVideoType(attach.getMime());
                        data.setVideoPath(attach.getUrl());
                        data.setCoverPath(attach.getUrl2());
                        data.setDuration(Integer.parseInt(Uri.parse(attach.getUrl()).getQueryParameter("t")));
                    } else if (attach.getMime() == 4) {
                        data.setVideoType(attach.getMime());
                        data.setVideoPath(attach.getUrl2());
                        data.setCoverPath(attach.getUrl());
                    }
                }
            }
        }
    }

    @Override
    public void progress(int precent, int tag) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(NoticeChangeBean.DataBean e) {
        if (e.getTypeX() == 26 || e.getTypeX() == 22 || e.getTypeX() == 24) {//扩展信息变更，回复信息变更
            StringBuffer ids = new StringBuffer();
            for (List<Integer> id : e.getIds()) {
                if (id.size() >= 2) {
                    ids.append(id.get(1));
                    ids.append(",");
                }
            }
            if (ids.length() > 0) {
                ids.deleteCharAt(ids.length() - 1);
                notifyTheme(ids.toString());
            }
        }
    }

    private void notifyTheme(String ids) {
        mPresenter.upDataThemeInfo(ids, updata_Tag);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(DeleteThemeBus e) {
        for (int i = 0; i < mDatas.size(); i++) {
            if (mDatas.get(i).getId() == e.themeId) {
                mAdapter.remove(i);
                break;
            }
        }
    }

    @Override
    protected boolean isBindEventBus() {
        return true;
    }

    private void showEmptyView() {
        if (mDatas.size() == 0) {
            View emptyView = provideActivity().getLayoutInflater().inflate(R.layout.empty_view_normal, (ViewGroup) mBinding.recycleMytheme.getParent(), false);
            mAdapter.setEmptyView(emptyView);
        }
    }

    private void showErrorEmptyView() {
        if (mDatas.size() == 0) {
            View emptyView = provideActivity().getLayoutInflater().inflate(R.layout.empty_view_error, (ViewGroup) mBinding.recycleMytheme.getParent(), false);
            emptyView.findViewById(R.id.txt_empty_retry).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadMore();
                }
            });
            mAdapter.setEmptyView(emptyView);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAdapter != null)
            mAdapter.stopMediaPlayer();
    }

    @Override
    protected void onDestroy() {
        mAdapter.releaseMediaPlayer();
        super.onDestroy();
    }
}
