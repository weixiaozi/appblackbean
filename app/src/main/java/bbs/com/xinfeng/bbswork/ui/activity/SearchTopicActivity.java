package bbs.com.xinfeng.bbswork.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.base.BaseActivity;
import bbs.com.xinfeng.bbswork.base.Constant;
import bbs.com.xinfeng.bbswork.databinding.ActivitySearchTopicBinding;
import bbs.com.xinfeng.bbswork.domin.ErrorBean;
import bbs.com.xinfeng.bbswork.domin.NotificationBean;
import bbs.com.xinfeng.bbswork.domin.TopicListBean;
import bbs.com.xinfeng.bbswork.domin.TopicOperateBus;
import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.presenter.TopicPresenter;
import bbs.com.xinfeng.bbswork.ui.adapter.TopicListAdapter;
import bbs.com.xinfeng.bbswork.utils.SharedPrefUtil;
import bbs.com.xinfeng.bbswork.utils.ToastUtil;

public class SearchTopicActivity extends BaseActivity<ActivitySearchTopicBinding, TopicPresenter> implements NetContract.INetView {
    private static final int toplist_Tag = 300;
    private static final int toplist_more_Tag = 301;
    private List<TopicListBean.DataBean> mdatas = new ArrayList<>();
    private TopicListAdapter mAdapter;
    private int page = 1;
    private String searchContent;

    @Override
    protected TopicPresenter creatPresenter() {
        return new TopicPresenter(this);
    }

    @Override
    protected void initEvent() {
        mBinding.ivSearchClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.editSearchTopic.setText("");
            }
        });
        mBinding.txtSearchCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mBinding.editSearchTopic.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_SEARCH)) {
                    if (!TextUtils.isEmpty(mBinding.editSearchTopic.getText().toString().trim())) {
                        searchContent = mBinding.editSearchTopic.getText().toString().trim();
                        refresh();
                    } else {
                        ToastUtil.showToast("请输入要搜索的内容");
                    }
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected int initView(Bundle savedInstanceState) {
        return R.layout.activity_search_topic;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        initAdapter();
    }

    private void initAdapter() {
        mAdapter = new TopicListAdapter(mdatas);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        });
        mBinding.recycleTopiclist.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recycleTopiclist.setAdapter(mAdapter);
        mBinding.recycleTopiclist.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (mdatas.get(position).getIsjoin() == 1) {
                    Intent intent = new Intent(provideActivity(), TopicDeatilSelfActivity.class);
                    intent.putExtra("topicid", mdatas.get(position).getId() + "");
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(provideActivity(), TopicDeatilOtherActivity.class);
                    intent.putExtra("topicid", mdatas.get(position).getId() + "");
                    startActivity(intent);
                }
            }
        });
    }

    private void loadMore() {
        mPresenter.getTopicList(searchContent, page, toplist_more_Tag);
    }

    private void refresh() {
        page = 1;
        mAdapter.setEnableLoadMore(false);
        mPresenter.getTopicList(searchContent, page, toplist_Tag);
    }

    @Override
    public void showLoading(int tag) {
        if (tag == toplist_Tag && mdatas.size() == 0)
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
        if (tag == toplist_Tag)
            showErrorEmptyView();
    }

    @Override
    public void setData(ErrorBean errorBean, int tag) {
        switch (tag) {
            case toplist_Tag:
                TopicListBean bean = (TopicListBean) errorBean;
                mdatas.clear();
                if (bean.getData() != null && bean.getData().size() > 0) {
                    page++;
                    mdatas.addAll(bean.getData());
                    mAdapter.setNewData(mdatas);
                    mAdapter.setEnableLoadMore(true);
                    mAdapter.loadMoreComplete();
                } else {
                    showEmptyView();
                    mAdapter.setNewData(mdatas);
                    mAdapter.setEnableLoadMore(false);
                    mAdapter.loadMoreEnd();
                }

                break;
            case toplist_more_Tag:
                TopicListBean bean1 = (TopicListBean) errorBean;
                if (bean1.getData() != null && bean1.getData().size() > 0) {
                    page++;
                    mAdapter.addData(bean1.getData());
                    mAdapter.setEnableLoadMore(true);
                    mAdapter.loadMoreComplete();
                } else {
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
            View emptyView = provideActivity().getLayoutInflater().inflate(R.layout.empty_view_normal, (ViewGroup) mBinding.recycleTopiclist.getParent(), false);
            mAdapter.setEmptyView(emptyView);
        }
    }

    private void showErrorEmptyView() {
        if (mdatas.size() == 0) {
            View emptyView = provideActivity().getLayoutInflater().inflate(R.layout.empty_view_error, (ViewGroup) mBinding.recycleTopiclist.getParent(), false);
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
    public void event(TopicOperateBus e) {
        for (int i = 0; i < mdatas.size(); i++) {
            if (mdatas.get(i).getId() == e.topicId) {
                if (e.status == 2) {
                    mdatas.get(i).setIsjoin(2);
                } else if (e.status == 0) {
                    mdatas.get(i).setIsjoin(0);
                } else if (e.status == 1) {
                    mdatas.get(i).setIsjoin(1);
                }

                mAdapter.refreshNotifyItemChanged(i);
                break;
            }
        }
    }

    @Override
    protected boolean isBindEventBus() {
        return true;
    }
}
