package bbs.com.xinfeng.bbswork.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.base.BaseActivity;
import bbs.com.xinfeng.bbswork.base.Constant;
import bbs.com.xinfeng.bbswork.databinding.ActivityUserInfoBinding;
import bbs.com.xinfeng.bbswork.databinding.ItemUserinfoHeadBinding;
import bbs.com.xinfeng.bbswork.domin.BackPrivateChatBus;
import bbs.com.xinfeng.bbswork.domin.ErrorBean;
import bbs.com.xinfeng.bbswork.domin.OutBlacksBus;
import bbs.com.xinfeng.bbswork.domin.UserDetailBean;
import bbs.com.xinfeng.bbswork.module.GlideApp;
import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.presenter.UserInfoPresenter;
import bbs.com.xinfeng.bbswork.ui.adapter.UserinfoDetailAdapter;
import bbs.com.xinfeng.bbswork.ui.fragment.HomeFragment;
import bbs.com.xinfeng.bbswork.ui.fragment.MessageFragment;
import bbs.com.xinfeng.bbswork.utils.ArmsUtils;
import bbs.com.xinfeng.bbswork.utils.LogUtil;
import bbs.com.xinfeng.bbswork.utils.ScreenUtils;
import bbs.com.xinfeng.bbswork.utils.ToastUtil;
import bbs.com.xinfeng.bbswork.widget.imageShow.ShowImagesDialog;
import bbs.com.xinfeng.bbswork.widget.popwindow.PointPoppubWindow;
import bbs.com.xinfeng.bbswork.widget.popwindow.TopicDetailPoppubWindow;
import me.iwf.photopicker.PhotoPreview;

public class UserInfoActivity extends BaseActivity<ActivityUserInfoBinding, UserInfoPresenter> implements NetContract.INetView {
    private static final int Userinfolist_Tag = 300;
    private static final int FollowAction_Tag = 301;
    private static final int ACTBLACK_IN_TAG = 302;
    private static final int ACTBLACK_OUT_TAG = 303;
    private List<UserDetailBean.TopicsBean> mdatas = new ArrayList<>();
    private UserinfoDetailAdapter mAdapter;
    private int page = 1;
    private int userid;
    private boolean isAddHead;
    private TopicDetailPoppubWindow pop;
    private int isblock = -1;
    private PointPoppubWindow pointPop;
    private int baseHeight;
    private UserDetailBean bean;
    private ItemUserinfoHeadBinding headBinding;
    private ShowImagesDialog imagesDialog;

    @Override
    protected UserInfoPresenter creatPresenter() {
        return new UserInfoPresenter(this);
    }

    @Override
    protected int initView(Bundle savedInstanceState) {
        return R.layout.activity_user_info;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        immerseUI(getResources().getColor(R.color.black));
        baseHeight = ArmsUtils.dip2px(provideActivity(), 167);
        userid = getIntent().getIntExtra("userid", 0);
        pop = new TopicDetailPoppubWindow(provideActivity());
        initAdapter();
        mPresenter.getUserinfoList(userid, page, Userinfolist_Tag);
    }

    private void initAdapter() {
        mAdapter = new UserinfoDetailAdapter(mdatas);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        });
        mBinding.recycleUserinfolist.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recycleUserinfolist.setAdapter(mAdapter);
        mBinding.recycleUserinfolist.addOnItemTouchListener(new OnItemClickListener() {
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
        mBinding.recycleUserinfolist.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                float alpha = getScollYDistance() / (float) baseHeight;
                updataBar(alpha);
                mBinding.ivBarBg.setAlpha(alpha);
            }
        });
    }

    private void updataBar(float alpha) {
        if (alpha >= 1) {
            mBinding.barTxtTitle.setVisibility(View.VISIBLE);
            if (bean != null && bean.getUser().getIsme() != 1) {
                mBinding.txtBarFollow.setVisibility(View.VISIBLE);
                mBinding.ivBarMore.setVisibility(View.GONE);
            }
        } else {
            mBinding.barTxtTitle.setVisibility(View.INVISIBLE);
            if (bean != null && bean.getUser().getIsme() != 1) {
                mBinding.ivBarMore.setVisibility(View.VISIBLE);
                mBinding.txtBarFollow.setVisibility(View.GONE);
            }

        }

    }

    public int getScollYDistance() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) mBinding.recycleUserinfolist.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        int itemHeight = firstVisiableChildView.getHeight();
        return (position) * itemHeight - firstVisiableChildView.getTop();
    }

    private void addHeader(UserDetailBean bean) {
        this.bean = bean;
        View headView = getLayoutInflater().inflate(R.layout.item_userinfo_head, (ViewGroup) mBinding.recycleUserinfolist.getParent(), false);
        headBinding = DataBindingUtil.bind(headView);
        GlideApp.with(provideActivity()).load(bean.getUser().getPortrait_thumb()).override(100).placeholder(R.drawable.icon_head_default).error(R.drawable.icon_head_default).into(headBinding.ivUserhead);

        headBinding.txtUsername.setText(bean.getUser().getName());
        mBinding.barTxtTitle.setText(bean.getUser().getName());
        headBinding.txtUserintroduce.setText(bean.getUser().getIntroduce());
        headBinding.txtUserinfoFans.setText(bean.getUser().getFollows() + " 关注      " + bean.getUser().getFans() + " 粉丝");
        if (bean.getUser().getIsme() != 1) {
            if (bean.getUser().getIsfollow() == 1) {
                headBinding.buttonUserinfoFollow.setText("已关注");
                mBinding.txtBarFollow.setText("已关注");
            } else {
                headBinding.buttonUserinfoFollow.setText("关注");
                mBinding.txtBarFollow.setText("关注");
            }
            headBinding.buttonUserinfoFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    followAct();
                }
            });
            headBinding.buttonUserinfoChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MessageFragment.ResumeChatId = 0;
                    EventBus.getDefault().post(new BackPrivateChatBus(0));
                    Intent intent = new Intent(provideActivity(), PrivateChatActivity.class);
                    intent.putExtra("userid", userid);
                    intent.putExtra("username", bean.getUser().getName());
                    intent.putExtra("chatid", bean.getUser().getSession_id());
                    startActivity(intent);
                }
            });
        } else {
            headBinding.buttonUserinfoChat.setVisibility(View.GONE);
            headBinding.buttonUserinfoFollow.setVisibility(View.GONE);
        }
        headBinding.ivUserhead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> src = new ArrayList<>();
                ArrayList<String> thum = new ArrayList<>();
                src.add(bean.getUser().getPortrait());
                thum.add(bean.getUser().getPortrait_thumb());
                if (imagesDialog == null) {
                    imagesDialog = new ShowImagesDialog(provideActivity());
                    imagesDialog.setHasSave(true);
                }
                imagesDialog.setPicList(src, thum, 0);
                imagesDialog.show();
            }
        });
        mAdapter.addHeaderView(headView);
    }

    private void followAct() {
        mPresenter.followAction(bean.getUser().getIsfollow() == 1 ? -1 : 1, userid, FollowAction_Tag);
        bean.getUser().setIsfollow(bean.getUser().getIsfollow() == 1 ? 0 : 1);
        bean.getUser().setFans(bean.getUser().getFans() + (bean.getUser().getIsfollow() == 1 ? 1 : -1));
        headBinding.txtUserinfoFans.setText(bean.getUser().getFollows() + " 关注      " + bean.getUser().getFans() + " 粉丝");

        if (bean.getUser().getIsfollow() == 1) {
            headBinding.buttonUserinfoFollow.setText("已关注");
            mBinding.txtBarFollow.setText("已关注");
            ToastUtil.showToast("关注成功");
        } else {
            headBinding.buttonUserinfoFollow.setText("关注");
            mBinding.txtBarFollow.setText("关注");
            ToastUtil.showToast("取消关注成功");
        }
    }

    private void loadMore() {
        mPresenter.getUserinfoList(userid, page, Userinfolist_Tag);
    }

    @Override
    protected void initEvent() {
        mBinding.flayoutBarRoot.setOnClickListener(v -> {

        });
        mBinding.barLeftClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBinding.ivBarMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.setDismissListener(new TopicDetailPoppubWindow.DismissListener() {
                    @Override
                    public void dismiss() {

                    }

                    @Override
                    public void onOut() {
                        if (isblock == 1) {
                            mPresenter.actBlack(userid, -1, ACTBLACK_OUT_TAG);
                        } else {
                            if (pointPop == null)
                                pointPop = new PointPoppubWindow(provideActivity());
                            pointPop.setPointInfo("您是否确定将TA拉入黑名单", null, false, "取消", "确认");
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
                                    mPresenter.actBlack(userid, 1, ACTBLACK_IN_TAG);
                                }
                            });
                            pointPop.show();
                        }

                    }

                    @Override
                    public void onReport() {
                        Intent intent = new Intent(provideActivity(), WebviewActivity.class);
                        intent.putExtra("weburl", Constant.BASEURL + "/report?user=" + userid);
                        startActivity(intent);
                    }
                });
                pop.show();
            }
        });
        mBinding.txtBarFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followAct();
            }
        });
    }

    @Override
    public void showLoading(int tag) {
        if (tag == Userinfolist_Tag && mdatas.size() == 0)
            startLoading();
    }

    @Override
    public void hideLoading(int tag) {
        stopLoading();
    }

    @Override
    public void showError(ErrorBean errorBean, int tag) {
        if (errorBean.androidType.equals(ErrorBean.TYPE_SHOW))
            ToastUtil.showToast(errorBean.desc);
        if (tag == Userinfolist_Tag && !isAddHead)
            showErrorEmptyView();
        else if (tag == ACTBLACK_IN_TAG) {
            if (TextUtils.equals(errorBean.androidcode, "150000")) {
                isblock = 1;
                if (pop != null)
                    pop.setBtnTest("移除黑名单");
            }
        } else if (tag == ACTBLACK_OUT_TAG) {
            if (TextUtils.equals(errorBean.androidcode, "150001")) {
                isblock = 0;
                if (pop != null)
                    pop.setBtnTest("加入黑名单");
            }
        }
    }

    @Override
    public void setData(ErrorBean errorBean, int tag) {
        switch (tag) {
            case Userinfolist_Tag:
                if (errorBean instanceof UserDetailBean) {
                    UserDetailBean bean = (UserDetailBean) errorBean;
                    if (bean.getTopics() != null && bean.getTopics().size() > 0) {
                        page++;
                        mAdapter.addData(bean.getTopics());
                        mAdapter.setEnableLoadMore(true);
                        mAdapter.loadMoreComplete();
                    } else {
                        if (page == 1) {
//                            ToastUtil.showToast("没有话题，快来创建");
                        }
                        mAdapter.setEnableLoadMore(false);
                        mAdapter.loadMoreEnd();
                    }
                    if (!isAddHead) {
                        if (bean.getUser().getIsme() != 1) {
                            mBinding.ivBarMore.setVisibility(View.VISIBLE);
                            pop.showOutButton();
                            isblock = bean.getUser().getIsblock();
                            if (isblock == 1) {
                                pop.setBtnTest("移除黑名单");
                            } else {
                                pop.setBtnTest("加入黑名单");
                            }
                        } else {
                            mBinding.ivBarMore.setVisibility(View.GONE);
                        }
                        addHeader(bean);
                        isAddHead = true;
                    }
                }
                break;
            case ACTBLACK_OUT_TAG:
                isblock = 0;
                if (isblock == 1) {
                    pop.setBtnTest("移除黑名单");
                } else {
                    pop.setBtnTest("加入黑名单");
                }
                ToastUtil.showToast("移除黑名单成功");
                break;
            case ACTBLACK_IN_TAG:
                isblock = 1;
                if (isblock == 1) {
                    pop.setBtnTest("移除黑名单");
                } else {
                    pop.setBtnTest("加入黑名单");
                }
                ToastUtil.showToast("加入黑名单成功");
                break;
        }
    }

    @Override
    public void progress(int precent, int tag) {

    }

    @Override
    protected boolean isSelfimmerseUI() {
        return true;
    }


    private void showErrorEmptyView() {
        if (mdatas.size() == 0) {
            mAdapter.removeAllHeaderView();
            isAddHead = false;
            View emptyView = provideActivity().getLayoutInflater().inflate(R.layout.empty_view_error, (ViewGroup) mBinding.recycleUserinfolist.getParent(), false);
            emptyView.findViewById(R.id.txt_empty_retry).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.getUserinfoList(userid, page, Userinfolist_Tag);
                }
            });
            mAdapter.setEmptyView(emptyView);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isblock == 0)
            EventBus.getDefault().post(new OutBlacksBus(userid));
    }
}
