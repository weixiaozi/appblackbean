package bbs.com.xinfeng.bbswork.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.base.BaseActivity;
import bbs.com.xinfeng.bbswork.databinding.ActivityMembersDisplayBinding;
import bbs.com.xinfeng.bbswork.domin.ErrorBean;
import bbs.com.xinfeng.bbswork.domin.MemberAllBean;
import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.presenter.MemberListPresenter;
import bbs.com.xinfeng.bbswork.ui.adapter.MembersDisplayAdapter;
import bbs.com.xinfeng.bbswork.utils.ToastUtil;

public class MembersDisplayActivity extends BaseActivity<ActivityMembersDisplayBinding, MemberListPresenter> implements NetContract.INetView {
    private static final int Members_Tag = 301;
    private int topoicId;
    private List<MemberAllBean.DataBean> mdatas = new ArrayList<>();
    private MembersDisplayAdapter mAdapter;

    @Override
    protected MemberListPresenter creatPresenter() {
        return new MemberListPresenter(this);
    }

    @Override
    protected int initView(Bundle savedInstanceState) {
        return R.layout.activity_members_display;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        topoicId = getIntent().getIntExtra("topicid", 0);
        mBinding.basebar.barLeftClick.setVisibility(View.VISIBLE);
        mBinding.basebar.barTxtTitle.setText(R.string.members_all);
        mBinding.basebar.barLeftClick.setOnClickListener(v -> finish());
        initAdapter();

        mPresenter.getMembersOut(topoicId, Members_Tag);
    }

    private void initAdapter() {
        mAdapter = new MembersDisplayAdapter(mdatas);
        mAdapter.setEnableLoadMore(false);
        mBinding.recycleMember.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recycleMember.setAdapter(mAdapter);
        mBinding.recycleMember.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(provideActivity(), UserInfoActivity.class);
                intent.putExtra("userid", mdatas.get(position).getUser_id());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void showLoading(int tag) {
        if (tag == Members_Tag && mdatas.size() == 0)
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
        if (tag == Members_Tag)
            showErrorEmptyView();
    }

    @Override
    public void setData(ErrorBean errorBean, int tag) {
        switch (tag) {
            case Members_Tag:
                MemberAllBean bean = (MemberAllBean) errorBean;
                mAdapter.addData(bean.getData());
                showEmptyView();
                break;
        }
    }

    @Override
    public void progress(int precent, int tag) {

    }

    private void showEmptyView() {
        if (mdatas.size() == 0) {
            View emptyView = provideActivity().getLayoutInflater().inflate(R.layout.empty_view_normal, (ViewGroup) mBinding.recycleMember.getParent(), false);
            mAdapter.setEmptyView(emptyView);
        }
    }

    private void showErrorEmptyView() {
        if (mdatas.size() == 0) {
            View emptyView = provideActivity().getLayoutInflater().inflate(R.layout.empty_view_error, (ViewGroup) mBinding.recycleMember.getParent(), false);
            emptyView.findViewById(R.id.txt_empty_retry).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.getMembersOut(topoicId, Members_Tag);
                }
            });
            mAdapter.setEmptyView(emptyView);
        }
    }
}
