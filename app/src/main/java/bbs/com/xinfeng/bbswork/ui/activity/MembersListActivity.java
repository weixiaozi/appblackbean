package bbs.com.xinfeng.bbswork.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.base.BaseActivity;
import bbs.com.xinfeng.bbswork.databinding.ActivityMembersListBinding;
import bbs.com.xinfeng.bbswork.databinding.ActivityNotificationBinding;
import bbs.com.xinfeng.bbswork.domin.ErrorBean;
import bbs.com.xinfeng.bbswork.domin.MemberAllBean;
import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.presenter.MemberListPresenter;
import bbs.com.xinfeng.bbswork.ui.adapter.MemberslistAdapter;
import bbs.com.xinfeng.bbswork.utils.ToastUtil;

public class MembersListActivity extends BaseActivity<ActivityMembersListBinding, MemberListPresenter> implements NetContract.INetView {
    public static final int ACTION_IN = 1;
    public static final int ACTION_OUT = 2;

    private static final int MembersIn_Tag = 300;
    private static final int MembersOut_Tag = 301;
    private static final int Invite_Tag = 302;
    private static final int Deleteuser_Tag = 303;
    private List<MemberAllBean.DataBean> mdatas = new ArrayList<>();
    private int page = 1;
    private MemberslistAdapter mAdapter;
    private int action;
    private int topoicId;
    private List<String> selectedList = new ArrayList<>();

    @Override
    protected MemberListPresenter creatPresenter() {
        return new MemberListPresenter(this);
    }

    @Override
    protected int initView(Bundle savedInstanceState) {
        return R.layout.activity_members_list;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        action = getIntent().getIntExtra("action", 0);
        topoicId = getIntent().getIntExtra("topicid", 0);

        mBinding.basebar.barLeftClick.setVisibility(View.VISIBLE);
        mBinding.basebar.barRightClick.setVisibility(View.VISIBLE);
        if (action == ACTION_IN) {
            mBinding.basebar.barTxtTitle.setText(R.string.member_in);
            mBinding.basebar.barRightTxt.setText("发出邀请");
        } else if (action == ACTION_OUT) {
            mBinding.basebar.barTxtTitle.setText(R.string.member_out);
            mBinding.basebar.barRightTxt.setText("删除");
        }

        mBinding.basebar.barLeftClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBinding.basebar.barRightClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (action == ACTION_IN) {
                    if (selectedList.size() == 0) {
                        ToastUtil.showToast("请选择邀请对象");
                    } else {
                        StringBuffer ids = new StringBuffer();
                        for (String id : selectedList) {
                            ids.append(id);
                            ids.append(",");
                        }
                        if (ids.length() > 0)
                            ids.deleteCharAt(ids.length() - 1);
                        mPresenter.inviteUsers(topoicId, ids.toString(), "1", Invite_Tag);
                    }
                } else if (action == ACTION_OUT) {
                    if (selectedList.size() == 0) {
                        ToastUtil.showToast("请选择要删除的成员");
                    } else {
                        StringBuffer ids = new StringBuffer();
                        for (String id : selectedList) {
                            ids.append(id);
                            ids.append(",");
                        }
                        if (ids.length() > 0)
                            ids.deleteCharAt(ids.length() - 1);
                        mPresenter.removeUsers(topoicId, ids.toString(), Deleteuser_Tag);
                    }
                }
            }
        });
        initAdapter();
        if (action == ACTION_IN) {
//            addHead();
            mPresenter.getMembersIn(topoicId, page, MembersIn_Tag);
        } else if (action == ACTION_OUT) {
            mPresenter.getMembersOut(topoicId, MembersOut_Tag);
        }

    }

    private void addHead() {
        View headView = getLayoutInflater().inflate(R.layout.item_member_addhead, (ViewGroup) mBinding.recycleMember.getParent(), false);
        mAdapter.addHeaderView(headView);
    }

    private void initAdapter() {
        mAdapter = new MemberslistAdapter(mdatas, action, getIntent().getStringExtra("topicname"));
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        });
        mBinding.recycleMember.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recycleMember.setAdapter(mAdapter);
        mBinding.recycleMember.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (action == ACTION_IN) {
                    if (mdatas.get(position).getIsjoin() != 1) {
                        mdatas.get(position).setSelected(mdatas.get(position).getSelected() == 1 ? 0 : 1);
                        if (mdatas.get(position).getSelected() == 1) {
                            selectedList.add(mdatas.get(position).getUser_id() + "");
                        } else {
                            selectedList.remove(mdatas.get(position).getUser_id() + "");
                        }
                        mAdapter.notifyItemChanged(position);
                    }
                } else {
                    if (mdatas.get(position).getIsme() != 1 && mdatas.get(position).getIsauther() != 1) {
                        mdatas.get(position).setSelected(mdatas.get(position).getSelected() == 1 ? 0 : 1);
                        if (mdatas.get(position).getSelected() == 1) {
                            selectedList.add(mdatas.get(position).getUser_id() + "");
                        } else {
                            selectedList.remove(mdatas.get(position).getUser_id() + "");
                        }
                        mAdapter.notifyItemChanged(position);
                    }
                }
            }
        });
    }

    private void loadMore() {
        if (action == ACTION_IN) {
            mPresenter.getMembersIn(topoicId, page, MembersIn_Tag);
        } else if (action == ACTION_OUT) {

        }
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void showLoading(int tag) {
        if ((tag == MembersIn_Tag || tag == MembersOut_Tag) && mdatas.size() == 0)
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
        if (tag == MembersIn_Tag || tag == MembersOut_Tag)
            showErrorEmptyView();
    }

    @Override
    public void setData(ErrorBean errorBean, int tag) {
        switch (tag) {
            case MembersIn_Tag:
                if (errorBean instanceof MemberAllBean) {
                    MemberAllBean bean = (MemberAllBean) errorBean;
                    /*if (page == 1)
                        addHead();*/
                    if (bean.getData() != null && bean.getData().size() > 0) {
                        page++;
                        mAdapter.addData(bean.getData());
                        mAdapter.setEnableLoadMore(true);
                        mAdapter.loadMoreComplete();
                    } else {
                        if (page == 1) {
                            showEmptyView();
                        }
                        mAdapter.setEnableLoadMore(false);
                        mAdapter.loadMoreEnd();
                    }
                }
                break;
            case MembersOut_Tag:
                if (errorBean instanceof MemberAllBean) {
                    MemberAllBean bean = (MemberAllBean) errorBean;
                    if (bean.getData() != null && bean.getData().size() > 0) {
                        mdatas.clear();
                        for (MemberAllBean.DataBean userData : bean.getData()) {
                            if (userData.getIsauther() == 1) {
                                bean.getData().remove(userData);
                                break;
                            }
                        }
                        mdatas.addAll(bean.getData());
                        mAdapter.setNewData(mdatas);
                        mAdapter.setEnableLoadMore(false);

                    } else {
                        mAdapter.loadMoreEnd();
                    }
                    if (mdatas.size() == 0)
                        mBinding.basebar.barRightClick.setVisibility(View.INVISIBLE);
                    showEmptyView();
                }
                break;
            case Invite_Tag:
                ToastUtil.showToast("邀请发送成功");
                finish();
                break;
            case Deleteuser_Tag:
                selectedList.clear();
                mPresenter.getMembersOut(topoicId, MembersOut_Tag);
                break;
        }
    }

    @Override
    public void progress(int precent, int tag) {

    }

    private void showEmptyView() {
        if (mdatas.size() == 0) {
            mAdapter.removeAllHeaderView();
            View emptyView = provideActivity().getLayoutInflater().inflate(R.layout.empty_view_normal, (ViewGroup) mBinding.recycleMember.getParent(), false);
            mAdapter.setEmptyView(emptyView);
        }
    }

    private void showErrorEmptyView() {
        if (mdatas.size() == 0) {
            mAdapter.removeAllHeaderView();
            View emptyView = provideActivity().getLayoutInflater().inflate(R.layout.empty_view_error, (ViewGroup) mBinding.recycleMember.getParent(), false);
            emptyView.findViewById(R.id.txt_empty_retry).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (action == ACTION_IN) {
                        mPresenter.getMembersIn(topoicId, page, MembersIn_Tag);
                    } else if (action == ACTION_OUT) {
                        mPresenter.getMembersOut(topoicId, MembersOut_Tag);
                    }
                }
            });
            mAdapter.setEmptyView(emptyView);
        }
    }
}
