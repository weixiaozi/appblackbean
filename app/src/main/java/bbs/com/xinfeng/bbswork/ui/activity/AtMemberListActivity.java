package bbs.com.xinfeng.bbswork.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.base.BaseActivity;
import bbs.com.xinfeng.bbswork.databinding.ActivityAtMemberListBinding;
import bbs.com.xinfeng.bbswork.databinding.ActivityMembersListBinding;
import bbs.com.xinfeng.bbswork.domin.AtMemberInfo;
import bbs.com.xinfeng.bbswork.domin.ErrorBean;
import bbs.com.xinfeng.bbswork.domin.MemberAllBean;
import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.presenter.MemberListPresenter;
import bbs.com.xinfeng.bbswork.ui.adapter.AtMembersAdapter;
import bbs.com.xinfeng.bbswork.ui.adapter.MemberslistAdapter;
import bbs.com.xinfeng.bbswork.utils.ToastUtil;

public class AtMemberListActivity extends BaseActivity<ActivityAtMemberListBinding, MemberListPresenter> implements NetContract.INetView {
    private static final int Atmembers_Tag = 200;
    private List<MemberAllBean.DataBean> mdatas = new ArrayList<>();
    private List<MemberAllBean.DataBean> selectedList = new ArrayList<>();
    private AtMembersAdapter mAdapter;

    @Override
    protected MemberListPresenter creatPresenter() {
        return new MemberListPresenter(this);
    }

    @Override
    protected int initView(Bundle savedInstanceState) {
        return R.layout.activity_at_member_list;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mBinding.basebar.barLeftClick.setVisibility(View.VISIBLE);
        mBinding.basebar.barTxtTitle.setText(R.string.atmembers);
        mBinding.basebar.barRightTxt.setText(R.string.finish);
        mBinding.basebar.barLeftClick.setOnClickListener(v -> {
            hideKeyboard();
            finish();
        });
        mBinding.basebar.barRightClick.setOnClickListener(v -> {
            hideKeyboard();
            Intent intent = new Intent();
            intent.putExtra("atmemberlist", (Serializable) selectedList);
            setResult(RESULT_OK, intent);
            finish();
        });
        initAdapter();
//        addHeader();
        mPresenter.getMembersOut(Integer.parseInt(getIntent().getStringExtra("topicid")), Atmembers_Tag);
    }

    private void initAdapter() {
        mAdapter = new AtMembersAdapter(mdatas);
      /*  mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        });*/
        mBinding.recycleAtmembers.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recycleAtmembers.setAdapter(mAdapter);
        mBinding.recycleAtmembers.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                mdatas.get(position).setSelected(mdatas.get(position).getSelected() == 1 ? 0 : 1);
                if (mdatas.get(position).getSelected() == 1) {
                    selectedList.add(mdatas.get(position));
                } else {
                    selectedList.remove(mdatas.get(position));
                }
                mAdapter.notifyItemChanged(position + 1);//加头方式更新要加一
            }
        });
    }

    private void addHeader() {
        View headView = getLayoutInflater().inflate(R.layout.atmembers_item_head, (ViewGroup) mBinding.recycleAtmembers.getParent(), false);
        EditText editText = (EditText) headView.findViewById(R.id.edit_atmembers_search);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_SEARCH)) {
                    updataSearchDatas(editText.getText().toString().trim());
                    return true;
                }
                return false;
            }
        });

        mAdapter.addHeaderView(headView);
    }

    private void updataSearchDatas(String searchContent) {
        mAdapter.setSearchContent(searchContent);
        mAdapter.notifyDataSetChanged();

    }

    private void loadMore() {
//        mPresenter.getMembersIn(topoicId, page, MembersIn_Tag);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void showLoading(int tag) {
        if (tag == Atmembers_Tag && mdatas.size() == 0)
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
        if (tag == Atmembers_Tag)
            showErrorEmptyView();
    }

    @Override
    public void setData(ErrorBean errorBean, int tag) {
        switch (tag) {
            case Atmembers_Tag:
                MemberAllBean bean = (MemberAllBean) errorBean;
                mAdapter.addData(bean.getData());
                addHeader();
                showEmptyView();
                break;
        }
    }

    @Override
    public void progress(int precent, int tag) {

    }


    /**
     * 隐藏软键盘
     */
    protected void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void showEmptyView() {
        if (mdatas.size() == 0) {
            mAdapter.removeAllHeaderView();
            View emptyView = provideActivity().getLayoutInflater().inflate(R.layout.empty_view_normal, (ViewGroup) mBinding.recycleAtmembers.getParent(), false);
            mAdapter.setEmptyView(emptyView);
        }
    }

    private void showErrorEmptyView() {
        if (mdatas.size() == 0) {
            mAdapter.removeAllHeaderView();
            View emptyView = provideActivity().getLayoutInflater().inflate(R.layout.empty_view_error, (ViewGroup) mBinding.recycleAtmembers.getParent(), false);
            emptyView.findViewById(R.id.txt_empty_retry).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.getMembersOut(Integer.parseInt(getIntent().getStringExtra("topicid")), Atmembers_Tag);
                }
            });
            mAdapter.setEmptyView(emptyView);
        }
    }
}
