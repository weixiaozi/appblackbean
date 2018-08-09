package bbs.com.xinfeng.bbswork.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;


import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.base.App;
import bbs.com.xinfeng.bbswork.base.BaseActivity;
import bbs.com.xinfeng.bbswork.base.Constant;
import bbs.com.xinfeng.bbswork.databinding.ActivityTopicDeatilOtherBinding;
import bbs.com.xinfeng.bbswork.databinding.ActivityTopicDeatilSelfBinding;
import bbs.com.xinfeng.bbswork.domin.AttachThemeBean;
import bbs.com.xinfeng.bbswork.domin.BackTopicBus;
import bbs.com.xinfeng.bbswork.domin.ErrorBean;
import bbs.com.xinfeng.bbswork.domin.MyThemeListBean;
import bbs.com.xinfeng.bbswork.domin.OutTopicBus;
import bbs.com.xinfeng.bbswork.domin.TopicDetailBean;
import bbs.com.xinfeng.bbswork.module.GlideApp;
import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.presenter.TopicDetailPresenter;
import bbs.com.xinfeng.bbswork.ui.adapter.MyThemeAdapter;
import bbs.com.xinfeng.bbswork.ui.adapter.ThemeListAdapter;
import bbs.com.xinfeng.bbswork.ui.adapter.TopicMermbersHeadWithNameAdapter;
import bbs.com.xinfeng.bbswork.ui.fragment.HomeFragment;
import bbs.com.xinfeng.bbswork.utils.ToastUtil;
import bbs.com.xinfeng.bbswork.utils.shareUtil.ShareData;
import bbs.com.xinfeng.bbswork.utils.shareUtil.ShareUtil;
import bbs.com.xinfeng.bbswork.widget.popwindow.SharePoppubWindow;
import bbs.com.xinfeng.bbswork.widget.popwindow.TopicDetailPoppubWindow;

import static bbs.com.xinfeng.bbswork.ui.activity.MembersListActivity.ACTION_IN;
import static bbs.com.xinfeng.bbswork.ui.activity.MembersListActivity.ACTION_OUT;
import static bbs.com.xinfeng.bbswork.widget.popwindow.ThemeDetailPoppubWindow.THEMEREPLY_TYPE;
import static bbs.com.xinfeng.bbswork.widget.popwindow.ThemeDetailPoppubWindow.THEME_TYPE;

public class TopicDeatilSelfActivity extends BaseActivity<ActivityTopicDeatilSelfBinding, TopicDetailPresenter> implements NetContract.INetView {

    private static final int topicdetail_Tag = 410;
    private static final int OutTopic_Tag = 411;
    private final int ReadTheme_TAG = 412;
    private SharePoppubWindow sharePoppubWindow;
    private TopicDetailBean bean;
    private ShareUtil shareUtil;
    private TopicDetailPoppubWindow pop;
    private ThemeListAdapter themeListAdapter;

    @Override
    protected TopicDetailPresenter creatPresenter() {
        return new TopicDetailPresenter(this);
    }

    @Override
    protected void initEvent() {
        mBinding.flayoutAllmembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bean != null) {
                    Intent intent = new Intent(provideActivity(), MembersDisplayActivity.class);
                    intent.putExtra("topicid", bean.getTopic().getId());
                    startActivity(intent);
                }
            }
        });
        mBinding.txtTopicMoreThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bean != null) {
                    if (getIntent().getBooleanExtra("fromchat", false) && HomeFragment.resumeTopicid == bean.getTopic().getId()) {
                        finish();
                    } else {
                        EventBus.getDefault().post(new BackTopicBus(bean.getTopic().getId()));
                        HomeFragment.resumeTopicid = bean.getTopic().getId();
                        Intent intent = new Intent(provideActivity(), ChattingActivity.class);
                        intent.putExtra("topoicname", bean.getTopic().getName());
                        intent.putExtra("topoicpic", bean.getTopic().getImg_url_thumb());
                        startActivity(intent);
                    }
                }
            }
        });
    }

    @Override
    protected int initView(Bundle savedInstanceState) {
        return R.layout.activity_topic_deatil_self;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mBinding.basebar.barLeftClick.setVisibility(View.VISIBLE);
        mBinding.basebar.barTxtTitle.setText(R.string.topic_title_detail);
        mBinding.basebar.barLeftClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        showRightBtn();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getTopicDetail(getIntent().getStringExtra("topicid"), topicdetail_Tag);
    }

    @Override
    public void showLoading(int tag) {
        if (tag == topicdetail_Tag && bean == null)
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
    }

    @Override
    public void setData(ErrorBean errorBean, int tag) {
        switch (tag) {
            case topicdetail_Tag:
                if (errorBean instanceof TopicDetailBean) {
                    bean = (TopicDetailBean) errorBean;
                    dealDatasForDB(bean.getThread());
                    fillData(bean);
                }
                break;
            case OutTopic_Tag:
                ToastUtil.showToast("话题退出成功");
                EventBus.getDefault().post(new OutTopicBus(true));
                finish();
                break;
        }
    }

    private void fillData(TopicDetailBean bean) {
        mBinding.llayoutRoot.setVisibility(View.VISIBLE);
        GlideApp.with(App.mApp).load(bean.getTopic().getImg_url_thumb()).placeholder(R.drawable.icon_topic_default).error(R.drawable.icon_topic_default).into(mBinding.ivTopicPage);
        mBinding.txtTopicName.setText(bean.getTopic().getName());
        mBinding.txtTDetailIntroduce.setText(bean.getTopic().getIntroduce());
        mBinding.txtMemberLength.setText(bean.getTopic().getFans_number() + " 成员");
        TopicDetailBean.FansBean fansIn = new TopicDetailBean.FansBean();
        fansIn.setUser_name("邀请加入");
        fansIn.setDrawableId(R.drawable.icon_in_member);
        bean.getFans().add(0, fansIn);
        if (bean.getIsauthor() == 1) {
            if (bean.getFans().size() <= 2) {
                if (pop == null) {
                    pop = new TopicDetailPoppubWindow(provideActivity());
                }
                pop.showOutButton();
            }
            TopicDetailBean.FansBean fansout = new TopicDetailBean.FansBean();
            fansout.setUser_name("移除");
            fansout.setDrawableId(R.drawable.icon_out_member);
            bean.getFans().add(1, fansout);
            mBinding.ivInfoNext.setVisibility(View.VISIBLE);
            mBinding.ivIntroduceNext.setVisibility(View.VISIBLE);
            mBinding.llayoutTopicInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(provideActivity(), CreatTopicActivity.class);
                    intent.putExtra("topoicid", bean.getTopic().getId());
                    intent.putExtra("topoicname", bean.getTopic().getName());
                    intent.putExtra("topoicintroduce", bean.getTopic().getIntroduce());
                    intent.putExtra("topoicpic", bean.getTopic().getImg_url());
                    startActivity(intent);
                }
            });
            mBinding.llayoutTopoicIntroduce.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(provideActivity(), CreatTopicActivity.class);
                    intent.putExtra("topoicid", bean.getTopic().getId());
                    intent.putExtra("topoicname", bean.getTopic().getName());
                    intent.putExtra("topoicintroduce", bean.getTopic().getIntroduce());
                    intent.putExtra("topoicpic", bean.getTopic().getImg_url());
                    startActivity(intent);
                }
            });
        } else {
            if (pop == null) {
                pop = new TopicDetailPoppubWindow(provideActivity());
            }
            pop.showOutButton();
        }
        mBinding.recycleMemberHead.setLayoutManager(new GridLayoutManager(provideActivity(), 5) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mBinding.recycleMemberHead.setNestedScrollingEnabled(false);
        TopicMermbersHeadWithNameAdapter membersAdapter = new TopicMermbersHeadWithNameAdapter(provideActivity(), bean.getFans());
        mBinding.recycleMemberHead.setAdapter(membersAdapter);
        membersAdapter.setOnItemClickListen(new TopicMermbersHeadWithNameAdapter.onItemClickListen() {
            @Override
            public void onclick(int position) {
                if (position == 0) {
                    if (sharePoppubWindow == null) {
                        sharePoppubWindow = new SharePoppubWindow(provideActivity());
                        sharePoppubWindow.setDismissListener(new SharePoppubWindow.DismissListener() {
                            @Override
                            public void dismiss() {

                            }

                            @Override
                            public void shareChannel(int channelId) {
                                switch (channelId) {
                                    case ShareData.WX:
                                        ShareData shareData = new ShareData();
                                        shareData.setTitle(bean.getTopic().getName());
                                        shareData.setSummary(bean.getTopic().getIntroduce());
                                        ArrayList<String> pics = new ArrayList<>();
                                        pics.add(bean.getTopic().getImg_url_thumb());
                                        shareData.setPicUrl(bean.getTopic().getImg_url_thumb());
                                        shareData.setPicUrls(pics);
                                        shareData.setTargtUrl(Constant.BASEURL + bean.getTopic().getWeb_share());
                                        shareData.setChannel(ShareData.WX);
                                        if (shareUtil == null)
                                            shareUtil = new ShareUtil(provideActivity());
                                        shareUtil.startShare(shareData);
                                        break;
                                    case ShareData.APPLICATION:
                                        Intent intent = new Intent(provideActivity(), MembersListActivity.class);
                                        intent.putExtra("action", ACTION_IN);
                                        intent.putExtra("topicid", bean.getTopic().getId());
                                        startActivity(intent);
                                        break;
                                }
                            }
                        });
                    }
                    if (!sharePoppubWindow.isShowing())
                        sharePoppubWindow.show();

                } else {
                    if (bean.getFans().get(position).getDrawableId() != 0) {
                        Intent intent = new Intent(provideActivity(), MembersListActivity.class);
                        intent.putExtra("action", ACTION_OUT);
                        intent.putExtra("topicid", bean.getTopic().getId());
                        intent.putExtra("topicname", bean.getTopic().getName());
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(provideActivity(), UserInfoActivity.class);
                        intent.putExtra("userid", bean.getFans().get(position).getUser_id());
                        startActivity(intent);
                    }
                }
            }
        });
        if (bean.getThread().size() > 0) {
            mBinding.llayoutTopicThemesRoot.setVisibility(View.VISIBLE);
            mBinding.recycleTopicThemes.setLayoutManager(new LinearLayoutManager(provideActivity(), LinearLayoutManager.VERTICAL, false) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });
            mBinding.recycleTopicThemes.setNestedScrollingEnabled(false);
            themeListAdapter = new ThemeListAdapter(provideActivity(), bean.getThread());
            mBinding.recycleTopicThemes.setAdapter(themeListAdapter);
            themeListAdapter.setmListen(new ThemeListAdapter.onItemOperateListen() {
                @Override
                public void operateItem(int position) {
                    Intent intent = new Intent(provideActivity(), ThemeDetailActivity.class);
                    intent.putExtra("topicid", bean.getThread().get(position).getTopic_id());
                    intent.putExtra("themeid", bean.getThread().get(position).getId());
                    startActivity(intent);
                }

                @Override
                public void clickPic(int position) {
                    if (bean.getThread().get(position).getIsread() != 1) {
                        mPresenter.readTheme(bean.getThread().get(position).getId(), ReadTheme_TAG);
                        bean.getThread().get(position).setIsread(1);
                    }
                }

                @Override
                public void clickAudio(int position) {
                    if (bean.getThread().get(position).getAudioread() != 1) {
                        mPresenter.readThemeAudio(bean.getThread().get(position).getId(), ReadTheme_TAG);
                    }
                }

            });
        } else {
            mBinding.llayoutTopicThemesRoot.setVisibility(View.GONE);
        }


    }

    private void dealDatasForDB(List<TopicDetailBean.ThreadBean> datas) {
        for (TopicDetailBean.ThreadBean data : datas) {
            if (data.getAttch() != null && data.getAttch().size() > 0) {
                for (AttachThemeBean attach : data.getAttch()) {
                    if (attach.getMime() == 1) {
                        data.setVideoType(attach.getMime());
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
            }
        }
    }

    private void showRightBtn() {
        mBinding.basebar.barRightPic.setImageResource(R.drawable.icon_more);
        mBinding.basebar.barRightClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pop == null) {
                    pop = new TopicDetailPoppubWindow(provideActivity());
                }
                pop.setDismissListener(new TopicDetailPoppubWindow.DismissListener() {
                    @Override
                    public void dismiss() {

                    }

                    @Override
                    public void onOut() {
                        mPresenter.outTopic(getIntent().getStringExtra("topicid"), OutTopic_Tag);
                    }

                    @Override
                    public void onReport() {
                        Intent intent = new Intent(provideActivity(), WebviewActivity.class);
                        intent.putExtra("weburl", Constant.BASEURL + "/report?topic=" + getIntent().getStringExtra("topicid"));
                        startActivity(intent);
                    }
                });

                pop.show();
            }
        });
    }

    @Override
    public void progress(int precent, int tag) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (themeListAdapter != null)
            themeListAdapter.stopMediaPlayer();
    }

    @Override
    protected void onDestroy() {
        if (themeListAdapter != null)
            themeListAdapter.releaseMediaPlayer();
        super.onDestroy();
    }
}
