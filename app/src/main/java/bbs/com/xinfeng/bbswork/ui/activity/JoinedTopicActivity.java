package bbs.com.xinfeng.bbswork.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.base.BaseActivity;
import bbs.com.xinfeng.bbswork.base.Constant;
import bbs.com.xinfeng.bbswork.databinding.ActivityJoinedTopicBinding;
import bbs.com.xinfeng.bbswork.domin.ErrorBean;
import bbs.com.xinfeng.bbswork.domin.TopicListBean;
import bbs.com.xinfeng.bbswork.domin.TopicOperateBus;
import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.presenter.TopicPresenter;
import bbs.com.xinfeng.bbswork.ui.adapter.TopicListAdapter;
import bbs.com.xinfeng.bbswork.utils.LogUtil;
import bbs.com.xinfeng.bbswork.utils.SharedPrefUtil;
import bbs.com.xinfeng.bbswork.utils.ToastUtil;

public class JoinedTopicActivity extends BaseActivity<ActivityJoinedTopicBinding, TopicPresenter> implements NetContract.INetView {
    private static final int toplist_Tag = 300;
    private List<TopicListBean.DataBean> mdatas = new ArrayList<>();
    private TopicListAdapter mAdapter;
    private int page = 1;

    @Override
    protected TopicPresenter creatPresenter() {
        return new TopicPresenter(this);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected int initView(Bundle savedInstanceState) {
        return R.layout.activity_joined_topic;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mBinding.basebar.barTxtTitle.setText(R.string.topic_title_join);
        mBinding.basebar.barLeftClick.setVisibility(View.VISIBLE);
        mBinding.basebar.barRightTxt.setText("创建");
        mBinding.basebar.barLeftClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBinding.basebar.barRightClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(provideActivity(), CreatTopicActivity.class));
            }
        });
        initAdapter();
//        addHeader();
        mPresenter.getTopicList(null, page, toplist_Tag);
    }

    private void addHeader() {
        View headView = getLayoutInflater().inflate(R.layout.topic_item_head, (ViewGroup) mBinding.recycleTopiclist.getParent(), false);
        headView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(provideActivity(), SearchTopicActivity.class));
            }
        });
        mAdapter.addHeaderView(headView);
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
        mPresenter.getTopicList(null, page, toplist_Tag);
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
        if (tag == toplist_Tag) {
            mAdapter.loadMoreComplete();
            showErrorEmptyView();
        }
    }

    @Override
    public void setData(ErrorBean errorBean, int tag) {
        switch (tag) {
            case toplist_Tag:
                if (errorBean instanceof TopicListBean) {
                    if (page == 1)
                        addHeader();
                    TopicListBean bean = (TopicListBean) errorBean;
                    if (bean.getData() != null && bean.getData().size() > 0) {
                        page++;
                        mAdapter.addData(bean.getData());
                        mAdapter.setEnableLoadMore(true);
                        mAdapter.loadMoreComplete();
                    } else {
                        if (page == 1) {
                        }
                        mAdapter.setEnableLoadMore(false);
                        mAdapter.loadMoreEnd();
                    }
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
            mAdapter.removeAllHeaderView();
            View emptyView = provideActivity().getLayoutInflater().inflate(R.layout.empty_view_normal, (ViewGroup) mBinding.recycleTopiclist.getParent(), false);
            mAdapter.setEmptyView(emptyView);
        }
    }

    private void showErrorEmptyView() {
        if (mdatas.size() == 0) {
            mAdapter.removeAllHeaderView();
            View emptyView = provideActivity().getLayoutInflater().inflate(R.layout.empty_view_error, (ViewGroup) mBinding.recycleTopiclist.getParent(), false);
            emptyView.findViewById(R.id.txt_empty_retry).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadMore();
                }
            });
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
