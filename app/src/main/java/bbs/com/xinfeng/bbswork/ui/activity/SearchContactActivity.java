package bbs.com.xinfeng.bbswork.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
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

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.base.BaseActivity;
import bbs.com.xinfeng.bbswork.databinding.ActivitySearchContactBinding;
import bbs.com.xinfeng.bbswork.domin.BackPrivateChatBus;
import bbs.com.xinfeng.bbswork.domin.CloseContactsBus;
import bbs.com.xinfeng.bbswork.domin.ContactListBean;
import bbs.com.xinfeng.bbswork.domin.ErrorBean;
import bbs.com.xinfeng.bbswork.domin.TopicListBean;
import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.presenter.ContactsPresenter;
import bbs.com.xinfeng.bbswork.ui.adapter.ContactListAdapter;
import bbs.com.xinfeng.bbswork.ui.adapter.TopicListAdapter;
import bbs.com.xinfeng.bbswork.ui.fragment.MessageFragment;
import bbs.com.xinfeng.bbswork.utils.ToastUtil;

public class SearchContactActivity extends BaseActivity<ActivitySearchContactBinding, ContactsPresenter> implements NetContract.INetView {
    private static final int toplist_Tag = 300;
    private static final int toplist_more_Tag = 301;
    private List<ContactListBean.DataBean> mdatas = new ArrayList<>();
    private ContactListAdapter mAdapter;
    private int page = 1;
    private String searchContent;
    private int mt;
    private String c1;

    @Override
    protected ContactsPresenter creatPresenter() {
        return new ContactsPresenter(this);
    }

    @Override
    protected int initView(Bundle savedInstanceState) {
        return R.layout.activity_search_contact;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mt = getIntent().getIntExtra("forwardingmt", 0);
        c1 = getIntent().getStringExtra("forwardingc1");
        initAdapter();
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

    private void initAdapter() {
        mAdapter = new ContactListAdapter(mdatas);
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
                MessageFragment.ResumeChatId = 0;
                EventBus.getDefault().post(new BackPrivateChatBus(0));
                Intent intent = new Intent(provideActivity(), PrivateChatActivity.class);
                intent.putExtra("userid", mdatas.get(position).getUser_id());
                intent.putExtra("username", mdatas.get(position).getUsername());
                intent.putExtra("chatid", mdatas.get(position).getSession_id());
                if (mt != 0) {
                    intent.putExtra("forwardingmt", mt);
                    intent.putExtra("forwardingc1", c1);
                }
                startActivity(intent);
                if (mt != 0) {
                    EventBus.getDefault().post(new CloseContactsBus(true));
                    finish();
                }

            }
        });
    }

    private void loadMore() {
        mPresenter.getContactList(searchContent, page, toplist_more_Tag);
    }

    private void refresh() {
        page = 1;
        mAdapter.setEnableLoadMore(false);
        mPresenter.getContactList(searchContent, page, toplist_Tag);
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
                ContactListBean bean = (ContactListBean) errorBean;
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
                ContactListBean bean1 = (ContactListBean) errorBean;
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

    @Override
    public void progress(int precent, int tag) {

    }
}
