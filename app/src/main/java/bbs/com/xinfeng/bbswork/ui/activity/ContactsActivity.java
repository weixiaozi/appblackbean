package bbs.com.xinfeng.bbswork.ui.activity;

import android.content.Intent;
import android.os.Bundle;
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
import java.util.List;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.base.BaseActivity;
import bbs.com.xinfeng.bbswork.databinding.ActivityContactsBinding;
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

public class ContactsActivity extends BaseActivity<ActivityContactsBinding, ContactsPresenter> implements NetContract.INetView {
    private static final int contactlist_Tag = 300;
    private List<ContactListBean.DataBean> mdatas = new ArrayList<>();
    private ContactListAdapter mAdapter;
    private int page = 1;
    private int mt;
    private String c1;

    @Override
    protected ContactsPresenter creatPresenter() {
        return new ContactsPresenter(this);
    }

    @Override
    protected int initView(Bundle savedInstanceState) {
        return R.layout.activity_contacts;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mBinding.basebar.barTxtTitle.setText(R.string.mes_contacts);
        mBinding.basebar.barLeftClick.setVisibility(View.VISIBLE);
        mBinding.basebar.barLeftClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mt = getIntent().getIntExtra("forwardingmt", 0);
        c1 = getIntent().getStringExtra("forwardingc1");
        initAdapter();
//        addHeader();
        mPresenter.getContactList(null, page, contactlist_Tag);
    }

    private void initAdapter() {
        mAdapter = new ContactListAdapter(mdatas);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        });
        mBinding.recycleContacts.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recycleContacts.setAdapter(mAdapter);
        mBinding.recycleContacts.addOnItemTouchListener(new OnItemClickListener() {
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
                if (mt != 0)
                    finish();
            }
        });
    }

    private void loadMore() {
        mPresenter.getContactList(null, page, contactlist_Tag);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void showLoading(int tag) {
        if (tag == contactlist_Tag && mdatas.size() == 0)
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
        if (tag == contactlist_Tag) {
            mAdapter.loadMoreComplete();
            showErrorEmptyView();
        }
    }

    @Override
    public void setData(ErrorBean errorBean, int tag) {
        switch (tag) {
            case contactlist_Tag:
                if (errorBean instanceof ContactListBean) {
                    if (page == 1)
                        addHeader();
                    ContactListBean bean = (ContactListBean) errorBean;
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

    private void addHeader() {
        View headView = getLayoutInflater().inflate(R.layout.topic_item_head, (ViewGroup) mBinding.recycleContacts.getParent(), false);
        ((TextView) headView.findViewById(R.id.txt_search_content)).setText("搜索联系人");
        headView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(provideActivity(), SearchContactActivity.class);
                if (mt != 0) {
                    intent.putExtra("forwardingmt", mt);
                    intent.putExtra("forwardingc1", c1);
                }
                startActivity(intent);
            }
        });
        mAdapter.addHeaderView(headView);
    }

    private void showEmptyView() {
        if (mdatas.size() == 0) {
            mAdapter.removeAllHeaderView();
            View emptyView = provideActivity().getLayoutInflater().inflate(R.layout.empty_view_normal, (ViewGroup) mBinding.recycleContacts.getParent(), false);
            mAdapter.setEmptyView(emptyView);
        }
    }

    private void showErrorEmptyView() {
        if (mdatas.size() == 0) {
            mAdapter.removeAllHeaderView();
            View emptyView = provideActivity().getLayoutInflater().inflate(R.layout.empty_view_error, (ViewGroup) mBinding.recycleContacts.getParent(), false);
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
    public void onEventMainThread(CloseContactsBus bus) {
        if (bus.isClose) {
            finish();
        }
    }

    @Override
    protected boolean isBindEventBus() {
        return true;
    }
}
