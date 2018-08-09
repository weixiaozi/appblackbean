package bbs.com.xinfeng.bbswork.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.base.App;
import bbs.com.xinfeng.bbswork.base.BaseFragment;
import bbs.com.xinfeng.bbswork.base.Constant;
import bbs.com.xinfeng.bbswork.databinding.FragmentHomeBinding;
import bbs.com.xinfeng.bbswork.domin.ErrorBean;
import bbs.com.xinfeng.bbswork.domin.NavigatorBus;
import bbs.com.xinfeng.bbswork.domin.NoticeChangeBean;
import bbs.com.xinfeng.bbswork.domin.TopListInnerBean;
import bbs.com.xinfeng.bbswork.domin.TopListInnerBean_;
import bbs.com.xinfeng.bbswork.domin.TopicUserListBean;
import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.presenter.HomeFragmentPresenter;
import bbs.com.xinfeng.bbswork.ui.activity.ChattingActivity;
import bbs.com.xinfeng.bbswork.ui.activity.JoinedTopicActivity;
import bbs.com.xinfeng.bbswork.ui.adapter.HomeTopicListAdapter;
import bbs.com.xinfeng.bbswork.ui.adapter.decoration.ItemLineDecoration;
import bbs.com.xinfeng.bbswork.utils.LogUtil;
import bbs.com.xinfeng.bbswork.utils.SharedPrefUtil;
import bbs.com.xinfeng.bbswork.utils.ToastUtil;
import io.objectbox.Box;
import io.objectbox.query.Query;

import static bbs.com.xinfeng.bbswork.ui.activity.MainActivity.First_unread_news;

/**
 * Created by dell on 2017/10/24.
 */

public class HomeFragment extends BaseFragment<FragmentHomeBinding, HomeFragmentPresenter> implements NetContract.INetView {
    private final int TOPICLIST_TAG = 210;
    private final int TOPICLIST_UPDATA_News_TAG = 211;
    private final int TOPICLIST_UPDATA_TAG = 212;
    private Box<TopListInnerBean> box;
    private List<TopListInnerBean> mDatas = Collections.synchronizedList(new ArrayList<>());
    private List<TopListInnerBean> mOldDatas = new ArrayList<>();
    private HomeTopicListAdapter mAdapter;
    private View emptyView;
    public static int resumeTopicid;//进入某个话题页时，不更新该话题消息
    private int userId;
    private Query<TopListInnerBean> boxQuery;

    public void showLoading(int tag) {
        if (tag == TOPICLIST_TAG && mDatas.size() == 0)
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
        if (tag == TOPICLIST_TAG) {
            showEmptyView();
            mBinding.swipeLayout.setRefreshing(false);
        }
    }

    @Override
    public void setData(ErrorBean errorBean, int tag) {
        switch (tag) {
            case TOPICLIST_TAG:
                TopicUserListBean bean = (TopicUserListBean) errorBean;
                mDatas.clear();
                mAdapter.notifyDataSetChanged();
                mDatas.addAll(bean.getData());
                for (TopListInnerBean oldTopic : mOldDatas) {
                    if (oldTopic.getUnreadNum() > 0) {
                        for (TopListInnerBean newTopic : mDatas) {
                            if (newTopic.getTopic_id() == oldTopic.getTopic_id()) {
                                newTopic.setUnreadNum(oldTopic.getUnreadNum());
                                break;
                            }
                        }
                    }
                }
                mOldDatas.clear();
                mOldDatas.addAll(mDatas);
                mAdapter.setNewData(mDatas);
                App.getInstance().getNotify();
                showEmptyView();

                mBinding.swipeLayout.setRefreshing(false);
                break;
            case TOPICLIST_UPDATA_News_TAG:
                TopicUserListBean bean1 = (TopicUserListBean) errorBean;
                if (bean1.getData().size() > 0) {
                    for (TopListInnerBean info : bean1.getData()) {
                        int unread = 0;
                        for (TopListInnerBean newData : mDatas) {
                            if (info.getTopic_id() == newData.getTopic_id()) {
                                unread = newData.getUnreadNum();
                                mDatas.remove(newData);
                                break;
                            }
                        }
                        if (info.getTopic_id() != resumeTopicid) {
                            info.setUnreadNum(unread + 1);
                        } else {
                            info.setUnreadNum(0);
                        }
                    }
                    mDatas.addAll(0, bean1.getData());
                    mOldDatas.clear();
                    mOldDatas.addAll(mDatas);
                    mAdapter.notifyDataSetChanged();
                }
                break;
            case TOPICLIST_UPDATA_TAG:
                TopicUserListBean bean2 = (TopicUserListBean) errorBean;
                if (bean2.getData().size() > 0) {
                    for (TopListInnerBean info : bean2.getData()) {
                        for (TopListInnerBean newData : mDatas) {
                            if (info.getTopic_id() == newData.getTopic_id()) {
                                newData.setTopic_img(info.getTopic_img());
                                newData.setTopic_name(info.getTopic_name());
                                break;
                            }
                        }
                    }
                    mOldDatas.clear();
                    mOldDatas.addAll(mDatas);
                    mAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    private void showEmptyView() {
        if (mDatas.size() == 0) {
            if (emptyView == null) {
                emptyView = provideActivity().getLayoutInflater().inflate(R.layout.empty_view_homefragment, (ViewGroup) mBinding.recycleTopiclist.getParent(), false);
            }
            ((TextView) emptyView.findViewById(R.id.txt_no_title)).setText("欢迎 " + SharedPrefUtil.getString(Constant.name_key, "") + "！");
            mAdapter.setEmptyView(emptyView);
        }
    }

    @Override
    public void progress(int precent, int tag) {

    }

    @Override
    protected HomeFragmentPresenter creatPresenter() {
        return new HomeFragmentPresenter(this);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected int initView(Bundle savedInstanceState) {
        return R.layout.fragment_home;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        userId = SharedPrefUtil.getInt(Constant.userid_key, 0);
        mBinding.basebar.barIvTitle.setImageResource(R.drawable.icon_logo_navigator);
        mBinding.basebar.barRightPic.setImageResource(R.drawable.icon_join_topic);
        mBinding.basebar.barRightClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(provideActivity(), JoinedTopicActivity.class));
            }
        });

        if (box == null) {
            box = App.mApp.getmBoxStore().boxFor(TopListInnerBean.class);
            boxQuery = box.query().equal(TopListInnerBean_.uid_android, userId).build();
        }
        List<TopListInnerBean> allData = boxQuery.find();
        if (allData != null && allData.size() > 0) {
            mOldDatas.addAll(allData);
        }
        mDatas.addAll(mOldDatas);
        initAdapter();
        refresh();
        updataNavigatorUnreadNew(true);
    }

    private void initAdapter() {
        mAdapter = new HomeTopicListAdapter(mDatas);
        mBinding.recycleTopiclist.setLayoutManager(new GridLayoutManager(provideActivity(), 2, LinearLayoutManager.VERTICAL, false));
        ;
        mBinding.recycleTopiclist.setAdapter(mAdapter);
        mBinding.recycleTopiclist.addItemDecoration(new ItemLineDecoration(provideActivity(), R.dimen.dp18));
        mBinding.swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        mBinding.recycleTopiclist.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                resumeTopicid = mDatas.get(position).getTopic_id();
                mDatas.get(position).setUnreadNum(0);
                mOldDatas.get(position).setUnreadNum(0);
                mAdapter.notifyItemChanged(position);
                Intent intent = new Intent(provideActivity(), ChattingActivity.class);
                intent.putExtra("topoicname", mDatas.get(position).getTopic_name());
                intent.putExtra("topoicpic", mDatas.get(position).getTopic_img());
                startActivity(intent);
            }
        });
    }

    private void updataNavigatorUnreadNew(boolean isOncreat) {
        boolean hasUnread = false;
        for (TopListInnerBean data : mDatas) {
            if (data.getUnreadNum() > 0) {
                hasUnread = true;
                break;
            }
        }
        if (!hasUnread)
            EventBus.getDefault().post(new NavigatorBus(0));
        if (isOncreat && hasUnread) {
            NoticeChangeBean.DataBean bean = new NoticeChangeBean.DataBean();
            bean.setTypeX(First_unread_news);
            EventBus.getDefault().post(bean);

        }
    }

    private void refresh() {
        mPresenter.getTopicList("", TOPICLIST_TAG);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(NoticeChangeBean.DataBean e) {
        if (e.getTypeX() == 16 || e.getTypeX() == 19) {//16：话题新增 19：主题新增
            StringBuffer ids = new StringBuffer();
            for (List<Integer> id : e.getIds()) {
                ids.append(id.get(0));
                ids.append(",");
            }
            if (ids.length() > 0)
                ids.deleteCharAt(ids.length() - 1);
            mPresenter.getTopicList(ids.toString(), TOPICLIST_UPDATA_News_TAG);
        } else if (e.getTypeX() == 17) {//话题修改
            StringBuffer ids = new StringBuffer();
            for (List<Integer> id : e.getIds()) {
                ids.append(id.get(0));
                ids.append(",");
            }
            if (ids.length() > 0)
                ids.deleteCharAt(ids.length() - 1);
            mPresenter.getTopicList(ids.toString(), TOPICLIST_UPDATA_TAG);
        } else if (e.getTypeX() == 18) {//话题删除
            for (List<Integer> id : e.getIds()) {
                for (int i = 0; i < mDatas.size(); i++) {
                    if (mDatas.get(i).getTopic_id() == id.get(0)) {
                        mDatas.remove(i);
                        if (mAdapter != null)
                            mAdapter.notifyItemRemoved(i);
                        break;
                    }
                }
            }
            showEmptyView();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mBinding.swipeLayout.setRefreshing(false);
        boxQuery.remove();
        for (TopListInnerBean data : mDatas) {
            data.setUid_android(userId);
        }
        box.put(mDatas);
    }


    @Override
    protected boolean isBindEventBus() {
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        updataNavigatorUnreadNew(false);
    }
}
