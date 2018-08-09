package bbs.com.xinfeng.bbswork.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;


import org.greenrobot.eventbus.EventBus;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.base.BaseActivity;
import bbs.com.xinfeng.bbswork.databinding.ActivityNotifyDetailBinding;
import bbs.com.xinfeng.bbswork.databinding.EditpointPopupwindowBinding;
import bbs.com.xinfeng.bbswork.domin.BackTopicBus;
import bbs.com.xinfeng.bbswork.domin.ErrorBean;
import bbs.com.xinfeng.bbswork.domin.NotificationBean;
import bbs.com.xinfeng.bbswork.module.GlideApp;
import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.presenter.NotificationPresenter;
import bbs.com.xinfeng.bbswork.ui.fragment.HomeFragment;
import bbs.com.xinfeng.bbswork.utils.ToastUtil;
import bbs.com.xinfeng.bbswork.widget.popwindow.EditPointPoppubWindow;

public class NotifyDetailActivity extends BaseActivity<ActivityNotifyDetailBinding, NotificationPresenter> implements NetContract.INetView {
    private static final int Operate_refuse_Tag = 30;
    private static final int Operate_agree_Tag = 31;
    private static final int Invite_Agree_Tag = 32;
    private static final int Invite_Refuse_Tag = 33;

    private NotificationBean.DataBean noticeDetail;

    @Override
    protected NotificationPresenter creatPresenter() {
        return new NotificationPresenter(this);
    }

    @Override
    protected int initView(Bundle savedInstanceState) {
        return R.layout.activity_notify_detail;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mBinding.basebar.barLeftClick.setVisibility(View.VISIBLE);
        mBinding.basebar.barTxtTitle.setText(R.string.notification_detatil);
        mBinding.basebar.barLeftClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        noticeDetail = (NotificationBean.DataBean) getIntent().getSerializableExtra("noticedetail");
        int type = noticeDetail.getTypeX();
        if (type == NotificationBean.OPERATE_111 || type == NotificationBean.OPERATE_112 || type == NotificationBean.OPERATE_113) {
            GlideApp.with(provideActivity()).load(noticeDetail.getTopic_thumb()).placeholder(R.drawable.icon_topic_default).error(R.drawable.icon_topic_default).into(mBinding.ivHead);
            mBinding.txtName.setText(noticeDetail.getTopic_name());

        } else {
            GlideApp.with(provideActivity()).load(noticeDetail.getUser_portrait()).placeholder(R.drawable.icon_head_default).error(R.drawable.icon_head_default).into(mBinding.ivHead);
            mBinding.txtName.setText(noticeDetail.getUser_name());
            mBinding.ivHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(provideActivity(), UserInfoActivity.class);
                    intent.putExtra("userid", noticeDetail.getUser_id());
                    startActivity(intent);
                }
            });
        }
        switch (type) {
            case NotificationBean.OPERATE_101:
                mBinding.llayoutTypeOne.setVisibility(View.VISIBLE);
                mBinding.llayoutOneOperate.setVisibility(View.VISIBLE);
                mBinding.txtResult.setText("申请加入  " + noticeDetail.getTopic_name());
                mBinding.txtOneApplyReason.setText(TextUtils.isEmpty(noticeDetail.getMsg_join()) ? "无" : noticeDetail.getMsg_join());
                mBinding.buttonOneNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditPointPoppubWindow editPointPoppubWindow = new EditPointPoppubWindow(provideActivity());
                        editPointPoppubWindow.setDismissListener(new EditPointPoppubWindow.DismissListener() {
                            @Override
                            public void dismiss() {

                            }

                            @Override
                            public void clickleft() {

                            }

                            @Override
                            public void clickRight(String content) {
                                operateNotice(noticeDetail.getTopic_id(), noticeDetail.getUser_id(), noticeDetail.getLog_id(), content, 4, Operate_refuse_Tag);
                            }
                        });
                        editPointPoppubWindow.show();

                    }
                });
                mBinding.buttonOneYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        operateNotice(noticeDetail.getTopic_id(), noticeDetail.getUser_id(), noticeDetail.getLog_id(), null, 5, Operate_agree_Tag);

                    }
                });
                break;
            case NotificationBean.OPERATE_102:
                mBinding.llayoutTypeOne.setVisibility(View.VISIBLE);
                mBinding.txtResult.setText("申请加入  " + noticeDetail.getTopic_name());
                mBinding.txtOneApplyReason.setText(TextUtils.isEmpty(noticeDetail.getMsg_join()) ? "无" : noticeDetail.getMsg_join());
                mBinding.txtNotifyStatus.setText("已同意");
                break;

            case NotificationBean.OPERATE_103:
                mBinding.llayoutTypeOne.setVisibility(View.VISIBLE);
                mBinding.txtResult.setText("申请加入  " + noticeDetail.getTopic_name());
                mBinding.txtOneApplyReason.setText(TextUtils.isEmpty(noticeDetail.getMsg_join()) ? "无" : noticeDetail.getMsg_join());
                mBinding.txtNotifyStatus.setText("已拒绝");
                mBinding.llayoutOneRefuse.setVisibility(View.VISIBLE);
                mBinding.txtOneRefuseReason.setText(TextUtils.isEmpty(noticeDetail.getMsg_deny()) ? "无" : noticeDetail.getMsg_deny());

                break;
            case NotificationBean.OPERATE_105:
                mBinding.llayoutTypeOne.setVisibility(View.VISIBLE);
                mBinding.txtResult.setText("申请加入  " + noticeDetail.getTopic_name());
                mBinding.txtOneApplyReason.setText(TextUtils.isEmpty(noticeDetail.getMsg_join()) ? "无" : noticeDetail.getMsg_join());
                mBinding.txtNotifyStatus.setText("已失效");

                break;
            case NotificationBean.OPERATE_111:
                mBinding.llayoutTypeTwo.setVisibility(View.VISIBLE);
                mBinding.txtResult.setText("已通过 您加入" + noticeDetail.getTopic_name());
                mBinding.buttonTwo.setText("去话题");
                mBinding.buttonTwo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (noticeDetail.getTopic_status() == 1) {
                            EventBus.getDefault().post(new BackTopicBus(noticeDetail.getTopic_id()));
                            HomeFragment.resumeTopicid = noticeDetail.getTopic_id();
                            Intent intent = new Intent(provideActivity(), ChattingActivity.class);
                            intent.putExtra("topoicname", noticeDetail.getTopic_name());
                            intent.putExtra("topoicpic", noticeDetail.getTopic_thumb());
                            startActivity(intent);
                        } else {
                            ToastUtil.showToast("已不在话题中");
                        }
                    }
                });
                break;
            case NotificationBean.OPERATE_112:
                mBinding.llayoutTypeTwo.setVisibility(View.VISIBLE);
                mBinding.txtResult.setText("拒绝 您加入" + noticeDetail.getTopic_name());
                mBinding.llayoutTwoRefuse.setVisibility(View.VISIBLE);
                mBinding.txtTwoRefuseReason.setText(TextUtils.isEmpty(noticeDetail.getMsg_deny()) ? "无" : noticeDetail.getMsg_deny());
                mBinding.buttonTwo.setText("重新申请");
                mBinding.buttonTwo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(provideActivity(), TopicOperateActivity.class);
                        intent.putExtra("topicid", noticeDetail.getTopic_id() + "");
                        intent.putExtra("topicname", noticeDetail.getTopic_name());
                        intent.putExtra("topicpic", noticeDetail.getTopic_thumb());
                        startActivity(intent);
                    }
                });
                break;
            case NotificationBean.OPERATE_202:
            case NotificationBean.OPERATE_203:
            case NotificationBean.OPERATE_204:
                if (type == NotificationBean.OPERATE_202) {
                    mBinding.txtNotifyStatus.setText("已同意");
                } else if (type == NotificationBean.OPERATE_203) {
                    mBinding.txtNotifyStatus.setText("已拒绝");
                } else if (type == NotificationBean.OPERATE_204) {
                    mBinding.txtNotifyStatus.setText("已失效");
                }
                mBinding.txtResult.setText("邀请您加入");
                mBinding.llayoutTypeThree.setVisibility(View.VISIBLE);
                GlideApp.with(provideActivity()).load(noticeDetail.getTopic_thumb()).override(100).placeholder(R.drawable.icon_topic_default).error(R.drawable.icon_topic_default).into(mBinding.ivThreeTopicPage);
                mBinding.txtThreeTopicName.setText(noticeDetail.getTopic_name());
                mBinding.txtThreeTopicInfo.setText(noticeDetail.getTopic_fans() + " 成员  " + noticeDetail.getTopic_thread() + " 观点");
                if (!TextUtils.isEmpty(noticeDetail.getTopic_introduce())) {
                    mBinding.txtThreeTopicIntroduce.setText(noticeDetail.getTopic_introduce());
                } else {
                    mBinding.txtThreeTopicIntroduce.setVisibility(View.GONE);
                }
                break;
            case NotificationBean.OPERATE_211:
            case NotificationBean.OPERATE_212:
                if (type == NotificationBean.OPERATE_211) {
                    mBinding.txtResult.setText("同意您的邀请");
                } else if (type == NotificationBean.OPERATE_212) {
                    mBinding.txtResult.setText("拒绝您的邀请");
                }
                mBinding.llayoutTypeThree.setVisibility(View.VISIBLE);
                GlideApp.with(provideActivity()).load(noticeDetail.getTopic_thumb()).override(100).placeholder(R.drawable.icon_topic_default).error(R.drawable.icon_topic_default).into(mBinding.ivThreeTopicPage);
                mBinding.txtThreeTopicName.setText(noticeDetail.getTopic_name());
                mBinding.txtThreeTopicInfo.setText(noticeDetail.getTopic_fans() + " 成员  " + noticeDetail.getTopic_thread() + " 观点");
                if (!TextUtils.isEmpty(noticeDetail.getTopic_introduce())) {
                    mBinding.txtThreeTopicIntroduce.setText(noticeDetail.getTopic_introduce());
                } else {
                    mBinding.txtThreeTopicIntroduce.setVisibility(View.GONE);
                }

                break;

            case NotificationBean.OPERATE_201:
                mBinding.txtResult.setText("邀请您加入");
                mBinding.llayoutTypeThree.setVisibility(View.VISIBLE);
                GlideApp.with(provideActivity()).load(noticeDetail.getTopic_thumb()).override(100).placeholder(R.drawable.icon_topic_default).error(R.drawable.icon_topic_default).into(mBinding.ivThreeTopicPage);
                mBinding.txtThreeTopicName.setText(noticeDetail.getTopic_name());
                mBinding.txtThreeTopicInfo.setText(noticeDetail.getTopic_fans() + " 成员  " + noticeDetail.getTopic_thread() + " 观点");
                if (!TextUtils.isEmpty(noticeDetail.getTopic_introduce())) {
                    mBinding.txtThreeTopicIntroduce.setText(noticeDetail.getTopic_introduce());
                } else {
                    mBinding.txtThreeTopicIntroduce.setVisibility(View.GONE);
                }
                mBinding.llayoutThreeOperate.setVisibility(View.VISIBLE);
                mBinding.buttonThreeNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditPointPoppubWindow editPointPoppubWindow = new EditPointPoppubWindow(provideActivity());
                        editPointPoppubWindow.setDismissListener(new EditPointPoppubWindow.DismissListener() {
                            @Override
                            public void dismiss() {

                            }

                            @Override
                            public void clickleft() {

                            }

                            @Override
                            public void clickRight(String content) {
                                mPresenter.inviteOperationNotification(noticeDetail.getTopic_id(), noticeDetail.getLog_id(), 2, content, Invite_Refuse_Tag);
                            }
                        });
                        editPointPoppubWindow.show();

                    }

                });
                mBinding.buttonThreeYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.inviteOperationNotification(noticeDetail.getTopic_id(), noticeDetail.getLog_id(), 1, null, Invite_Agree_Tag);

                    }
                });
                break;
        }

    }


    @Override
    protected void initEvent() {

    }

    private void operateNotice(int topic_id, int user_id, int log_id, String msg, int act, int tag) {
        mPresenter.operationNotification(topic_id, user_id, log_id, msg, act, tag);
    }

    @Override
    public void showLoading(int tag) {

    }

    @Override
    public void hideLoading(int tag) {

    }

    @Override
    public void showError(ErrorBean errorBean, int tag) {

    }

    @Override
    public void setData(ErrorBean errorBean, int tag) {
        switch (tag) {
            case Operate_refuse_Tag:
                finish();
                break;
            case Operate_agree_Tag:
                finish();
                break;
            case Invite_Agree_Tag:
                finish();
                break;
            case Invite_Refuse_Tag:
                finish();
                break;
        }
    }

    @Override
    public void progress(int precent, int tag) {

    }
}
