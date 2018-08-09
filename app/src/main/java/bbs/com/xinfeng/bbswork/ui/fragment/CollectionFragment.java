package bbs.com.xinfeng.bbswork.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.base.App;
import bbs.com.xinfeng.bbswork.base.BaseFragment;
import bbs.com.xinfeng.bbswork.base.Constant;
import bbs.com.xinfeng.bbswork.databinding.FragmentCollectionBinding;
import bbs.com.xinfeng.bbswork.databinding.PointPopupwindowBinding;
import bbs.com.xinfeng.bbswork.domin.AttachThemeBean;
import bbs.com.xinfeng.bbswork.domin.CollectionInnerBean;
import bbs.com.xinfeng.bbswork.domin.CollectionInnerBean_;
import bbs.com.xinfeng.bbswork.domin.CollectionListBean;
import bbs.com.xinfeng.bbswork.domin.CollectionThemeBus;
import bbs.com.xinfeng.bbswork.domin.DeleteThemeBus;
import bbs.com.xinfeng.bbswork.domin.ErrorBean;
import bbs.com.xinfeng.bbswork.domin.InnerThemeListBean;
import bbs.com.xinfeng.bbswork.domin.MesReduceBus;
import bbs.com.xinfeng.bbswork.domin.MyThemeListBean;
import bbs.com.xinfeng.bbswork.domin.NavigatorBus;
import bbs.com.xinfeng.bbswork.domin.NoticeChangeBean;
import bbs.com.xinfeng.bbswork.domin.TopListInnerBean;
import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.presenter.CollectionPresenter;
import bbs.com.xinfeng.bbswork.ui.activity.ChattingActivity;
import bbs.com.xinfeng.bbswork.ui.activity.MainActivity;
import bbs.com.xinfeng.bbswork.ui.activity.ThemeDetailActivity;
import bbs.com.xinfeng.bbswork.ui.activity.TopicDeatilOtherActivity;
import bbs.com.xinfeng.bbswork.ui.activity.TopicOperateActivity;
import bbs.com.xinfeng.bbswork.ui.activity.UserInfoActivity;
import bbs.com.xinfeng.bbswork.ui.adapter.CollectionListAdapter;
import bbs.com.xinfeng.bbswork.utils.SharedPrefUtil;
import bbs.com.xinfeng.bbswork.utils.ToastUtil;
import bbs.com.xinfeng.bbswork.widget.popwindow.PointPoppubWindow;
import io.objectbox.Box;
import io.objectbox.query.Query;

import static bbs.com.xinfeng.bbswork.domin.InnerThemeListBean.LOADING;

/**
 * Created by dell on 2017/10/24.
 */

public class CollectionFragment extends BaseFragment<FragmentCollectionBinding, CollectionPresenter> implements NetContract.INetView {
    private final int CollectionList_TAG = 100;
    private final int CollectionList_more_TAG = 101;
    private final int cancelCollection_TAG = 102;
    private final int updata_TAG = 103;
    private final int ReadTheme_TAG = 104;
    private List<CollectionInnerBean> mDatas = new ArrayList<>();
    private CollectionListAdapter mAdapter;
    private int page = 1;
    private int managerStatus;//0:管理,1:删除
    private List<String> deleteList = new ArrayList<>();
    private PointPoppubWindow pointPop;
    private Box<CollectionInnerBean> box;
    private final int boxMaxNum = 20;
    private int userId;
    private Query<CollectionInnerBean> boxQuery;
    private String current;

    public void showLoading(int tag) {
        if (tag == CollectionList_TAG && mDatas.size() == 0)
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
        if (tag == CollectionList_TAG && page == 1) {
            showEmptyView();
            mBinding.swipeLayout.setRefreshing(false);
        }
    }

    @Override
    public void setData(ErrorBean errorBean, int tag) {
        switch (tag) {
            case CollectionList_TAG:
                deleteList.clear();
                if (managerStatus == 1) {
                    deleteList.clear();
                }
                CollectionListBean bean = (CollectionListBean) errorBean;
                if (!TextUtils.isEmpty(bean.getCurrent())) {
                    current = bean.getCurrent();
                    SharedPrefUtil.putString(Constant.collect_current_key, current);
                }
                mBinding.swipeLayout.setRefreshing(false);
                mDatas.clear();
                mAdapter.notifyDataSetChanged();
                if (bean.getData() != null && bean.getData().size() > 0) {
                    dealDatasForDB(bean.getData());
                    page++;
                    mDatas.addAll(bean.getData());
                    mAdapter.setNewData(mDatas);
                    mAdapter.setEnableLoadMore(true);
                    mAdapter.loadMoreComplete();
                } else {
                    mAdapter.setNewData(mDatas);
                    mAdapter.setEnableLoadMore(false);
                    mAdapter.loadMoreEnd();
                }
//                updataNavigatorUnreadNew();
                showEmptyView();
                break;
            case CollectionList_more_TAG:
                CollectionListBean bean1 = (CollectionListBean) errorBean;
                if (bean1.getData() != null && bean1.getData().size() > 0) {
                    dealDatasForDB(bean1.getData());
                    page++;
                    mAdapter.addData(bean1.getData());
                    mAdapter.setEnableLoadMore(true);
                    mAdapter.loadMoreComplete();
                } else {
                    mAdapter.setEnableLoadMore(false);
                    mAdapter.loadMoreEnd();
                }
                break;
            case cancelCollection_TAG:
                refresh();
                break;
            case updata_TAG:
                if (errorBean instanceof CollectionListBean) {
                    CollectionListBean bean2 = (CollectionListBean) errorBean;
                    if (bean2.getData() != null && bean2.getData().size() > 0) {
                        dealDatasForDB(bean2.getData());
                        for (CollectionInnerBean newData : bean2.getData()) {
                            for (int i = 0; i < mDatas.size(); i++) {
                                if (newData.getThread_id() == mDatas.get(i).getThread_id()) {
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

    private void dealDatasForDB(List<CollectionInnerBean> datas) {
        StringBuffer srcBuffer = new StringBuffer();
        StringBuffer thumBuffer = new StringBuffer();
        for (CollectionInnerBean data : datas) {
            data.getSrc().clear();
            data.getThumb().clear();
            srcBuffer.delete(0, srcBuffer.length());
            thumBuffer.delete(0, thumBuffer.length());
            if (data.getAttch() != null && data.getAttch().size() > 0) {
                for (AttachThemeBean attach : data.getAttch()) {
                    if (attach.getMime() == 1) {
                        data.setVideoType(attach.getMime());
                        srcBuffer.append(attach.getUrl()).append(",");
                        thumBuffer.append(attach.getUrl2()).append(",");
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
                data.setSrcBox(srcBuffer.toString());
                data.setThumbBox(thumBuffer.toString());
            }
        }
    }

    @Override
    public void progress(int precent, int tag) {

    }

    @Override
    protected CollectionPresenter creatPresenter() {
        return new CollectionPresenter(this);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected int initView(Bundle savedInstanceState) {
        return R.layout.fragment_collection;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        userId = SharedPrefUtil.getInt(Constant.userid_key, 0);
        current = SharedPrefUtil.getString(Constant.collect_current_key, "");
        mBinding.basebar.barTxtTitle.setText(R.string.collection_title);
        mBinding.basebar.barRightTxt.setText(R.string.manager);
        mBinding.basebar.barLeftClick.setVisibility(View.VISIBLE);
        mBinding.basebar.barLeftPic.setVisibility(View.INVISIBLE);
        mBinding.basebar.barLeftClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.basebar.barRightTxt.setText(R.string.manager);
                mBinding.basebar.barLeftTxt.setText("");
                mBinding.basebar.barLeftClick.setClickable(false);
                deleteList.clear();
                managerStatus = 0;
                mAdapter.setManager(false);
                for (CollectionInnerBean info : mDatas) {
                    info.setSelect(false);
                }
                mAdapter.notifyDataSetChanged();
            }
        });
        mBinding.basebar.barRightClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (managerStatus == 0) {
                    mBinding.basebar.barRightTxt.setText(R.string.delete);
                    mBinding.basebar.barLeftTxt.setText(R.string.cancel);
                    mBinding.basebar.barLeftClick.setClickable(true);
                    deleteList.clear();
                    managerStatus = 1;
                    mAdapter.setManager(true);
                    mAdapter.notifyDataSetChanged();
                } else if (managerStatus == 1) {
                    if (deleteList.size() > 0) {
                        mBinding.basebar.barRightTxt.setText(R.string.manager);
                        mBinding.basebar.barLeftTxt.setText("");
                        mBinding.basebar.barLeftClick.setClickable(false);
                        managerStatus = 0;
                        deleteCollections();
                        deleteList.clear();
                        mAdapter.setManager(false);
                        mAdapter.notifyDataSetChanged();

                    } else {
                        ToastUtil.showToast("请选择需要删除的观点");
                    }
                }
            }
        });
        initBox();
        initAdapter();
        setOnRefresh();
        refresh();
    }

    private void initBox() {
        if (box == null) {
            box = App.mApp.getmBoxStore().boxFor(CollectionInnerBean.class);
            boxQuery = box.query().equal(CollectionInnerBean_.uid_android, userId).build();
        }
        List<CollectionInnerBean> boxList = boxQuery.find();
        for (CollectionInnerBean boxBean : boxList) {
            if (!TextUtils.isEmpty(boxBean.getSrcBox())) {
                String[] split = boxBean.getSrcBox().split(",");
                CollectionInnerBean.ThreadImgBean threadImgBean = new CollectionInnerBean.ThreadImgBean();
                threadImgBean.setSrc(new ArrayList<>(Arrays.asList(split)));
                boxBean.setThread_img(threadImgBean);
            }
            if (!TextUtils.isEmpty(boxBean.getThumbBox())) {
                String[] split = boxBean.getThumbBox().split(",");
                CollectionInnerBean.ThreadImgBean threadImgBean = new CollectionInnerBean.ThreadImgBean();
                threadImgBean.setThumb(new ArrayList<>(Arrays.asList(split)));
                boxBean.setThread_img(threadImgBean);
            }
            mDatas.add(boxBean);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mBinding.swipeLayout.setRefreshing(false);
        boxQuery.remove();
        List<CollectionInnerBean> boxData = new ArrayList<>();
        int currentNum = 0;
        for (CollectionInnerBean data : mDatas) {
            if (currentNum < boxMaxNum) {
                data.setUid_android(userId);
                boxData.add(data);
                currentNum++;
            } else {
                break;
            }
        }
        box.put(boxData);
    }

    private void deleteCollections() {
        mPresenter.cancelCollection(deleteList, cancelCollection_TAG);
    }

    private void initAdapter() {
        mAdapter = new CollectionListAdapter(mDatas);

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        });
        mAdapter.setmListen(new CollectionListAdapter.onItemOperateListen() {
            @Override
            public void operateItem(int position) {
                CollectionInnerBean dataBean = mDatas.get(position);
                if (managerStatus == 0) {
                    if (dataBean.getNews() > 0) {
                        mDatas.get(position).setN(0);
                        mAdapter.notifyItemChanged(position);
                    }
                    if (dataBean.getIsjoin() == 1) {
                        if (dataBean.getThread_status() == 1) {
                            Intent intent = new Intent(provideActivity(), ThemeDetailActivity.class);
                            intent.putExtra("topicid", dataBean.getTopic_id());
                            intent.putExtra("themeid", dataBean.getThread_id());
                            startActivity(intent);
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
                                intent.putExtra("topicid", dataBean.getTopic_id() + "");
                                startActivity(intent);
                            }
                        });
                        pointPop.show();
                    }

                } else {
                    if (dataBean.isSelect()) {
                        deleteList.remove(dataBean.getThread_id() + "");
                    } else {
                        deleteList.add(dataBean.getThread_id() + "");
                    }
                    dataBean.setSelect(!dataBean.isSelect());
                    mAdapter.notifyItemChanged(position);

                }
            }

            @Override
            public void goUserInfo(int position) {
                Intent intent = new Intent(provideActivity(), UserInfoActivity.class);
                intent.putExtra("userid", mDatas.get(position).getAuthor_userid());
                startActivity(intent);
            }

            @Override
            public void clickPic(int position) {
                if (mDatas.get(position).getIsread() != 1) {
                    mPresenter.readTheme(mDatas.get(position).getThread_id(), ReadTheme_TAG);
                    mDatas.get(position).setIsread(1);
                }
            }

            @Override
            public void clickAudio(int position) {
                if (mDatas.get(position).getAudioread() != 1) {
                    mPresenter.readThemeAudio(mDatas.get(position).getThread_id(), ReadTheme_TAG);
                }
            }
        });
        mBinding.recycleCollection.setLayoutManager(new LinearLayoutManager(provideActivity()));
        mBinding.recycleCollection.setAdapter(mAdapter);

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
        page = 1;
        mAdapter.setEnableLoadMore(false);
        mPresenter.getCollectionList(page, 1, current, CollectionList_TAG);
    }

    private void refreshAuto() {
        page = 1;
        mAdapter.setEnableLoadMore(false);
        mPresenter.getCollectionList(page, 0, current, CollectionList_TAG);
    }

    private void loadMore() {
        mPresenter.getCollectionList(page, 0, current, CollectionList_more_TAG);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(NoticeChangeBean.DataBean e) {
        /*if (e.getTypeX() == 22) {
            if (e.getIds().size() > 0) {
                for (List<Integer> ids : e.getIds()) {
                    if (ids.size() >= 2 && mDatas.size() > 0) {
                        for (int i = 0; i < mDatas.size(); i++) {
                            if (mDatas.get(i).getThread_id() == ids.get(1)) {
                                mDatas.get(i).setNews(mDatas.get(i).getNews() + 1);
                                mAdapter.notifyItemChanged(i);
                                e.setTypeX(22222);//收藏消息变动自定义type
                                EventBus.getDefault().post(e);
                                break;
                            }
                        }

                    }

                }

            }
        }*/
        if (e.getTypeX() == 29) {
            if (((MainActivity) getActivity()).getCurrentPostion() != 2) {
                refresh();
            }
        }

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
        mPresenter.updataThemeInfo(ids, updata_TAG);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(DeleteThemeBus e) {
        for (int i = 0; i < mDatas.size(); i++) {
            if (mDatas.get(i).getThread_id() == e.themeId) {
                mAdapter.remove(i);
                break;
            }
        }
        showEmptyView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MesReduceBus e) {
        if (e.type > 0 && e.themeId != 0) {
            for (int i = 0; i < mDatas.size(); i++) {
                if (mDatas.get(i).getThread_id() == e.themeId) {
                    mDatas.get(i).setN(0);
                    mAdapter.notifyItemChanged(i);
//                    updataNavigatorUnreadNew();
                    break;
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CollectionThemeBus e) {
        if (e.isCollect) {
            refreshAuto();
        } else {
            for (int i = 0; i < mDatas.size(); i++) {
                if (mDatas.get(i).getThread_id() == e.themeId) {
                    mAdapter.remove(i);
                    break;
                }
            }
            showEmptyView();
        }
    }

    @Override
    protected boolean isBindEventBus() {
        return true;
    }

    private void showEmptyView() {
        if (mDatas.size() == 0) {
            View emptyView = provideActivity().getLayoutInflater().inflate(R.layout.empty_view_collectionfragment, (ViewGroup) mBinding.recycleCollection.getParent(), false);
            mAdapter.setEmptyView(emptyView);
            mBinding.basebar.barRightClick.setVisibility(View.INVISIBLE);
            mBinding.basebar.barLeftClick.setVisibility(View.INVISIBLE);

        } else {
            mBinding.basebar.barLeftClick.setVisibility(View.VISIBLE);
            mBinding.basebar.barRightClick.setVisibility(View.VISIBLE);
        }
    }

    private void updataNavigatorUnreadNew() {
        boolean hasUnread = false;
        for (CollectionInnerBean data : mDatas) {
            if (data.getNews() > 0) {
                hasUnread = true;
                break;
            }
        }
        if (!hasUnread)
            EventBus.getDefault().post(new NavigatorBus(2));
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            if (mAdapter != null)
                mAdapter.stopMediaPlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        updataNavigatorUnreadNew();
    }

    @Override
    public void onDestroy() {

        try {
            if (mAdapter != null)
                mAdapter.releaseMediaPlayer();
        } catch (Exception e) {

        }
        super.onDestroy();
    }


}
