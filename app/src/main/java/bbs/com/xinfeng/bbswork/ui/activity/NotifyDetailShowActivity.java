package bbs.com.xinfeng.bbswork.ui.activity;

import android.os.Bundle;
import android.view.View;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.base.BaseActivity;
import bbs.com.xinfeng.bbswork.databinding.ActivityNotifyDetailShowBinding;
import bbs.com.xinfeng.bbswork.domin.ErrorBean;
import bbs.com.xinfeng.bbswork.domin.NotificationBean;
import bbs.com.xinfeng.bbswork.module.GlideApp;
import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.presenter.NotificationPresenter;

public class NotifyDetailShowActivity extends BaseActivity<ActivityNotifyDetailShowBinding, NotificationPresenter> implements NetContract.INetView {
    private NotificationBean.DataBean noticeDetail;
    private static final int Invite_Agree_Tag = 301;
    private static final int Invite_Refuse_Tag = 302;

    @Override
    protected NotificationPresenter creatPresenter() {
        return new NotificationPresenter(this);
    }

    @Override
    protected int initView(Bundle savedInstanceState) {
        return R.layout.activity_notify_detail_show;
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
        GlideApp.with(provideActivity()).load(noticeDetail.getUser_portrait()).override(74).placeholder(R.drawable.icon_head_default).error(R.drawable.icon_head_default).into(mBinding.ivHead);
        GlideApp.with(provideActivity()).load(noticeDetail.getTopic_thumb()).override(100).placeholder(R.drawable.icon_topic_default).error(R.drawable.icon_topic_default).into(mBinding.ivTopicPage);
        mBinding.txtApplyName.setText(noticeDetail.getUser_name() + "邀请您加入");
        mBinding.txtTopicName.setText(noticeDetail.getTopic_name());
        mBinding.txtTopicInfo.setText(noticeDetail.getTopic_fans() + " 成员  " + noticeDetail.getTopic_thread() + " 观点");
        mBinding.txtTopicIntroduce.setText(noticeDetail.getTopic_introduce());
        int type = noticeDetail.getTypeX();
        if (type == NotificationBean.OPERATE_201) {
            mBinding.txtNotifyStatus.setVisibility(View.GONE);
            mBinding.llayoutOperate.setVisibility(View.VISIBLE);
            mBinding.buttonNotifyNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.inviteOperationNotification(noticeDetail.getTopic_id(), noticeDetail.getLog_id(), 2, null, Invite_Refuse_Tag);

                }
            });
            mBinding.buttonNotifyYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.inviteOperationNotification(noticeDetail.getTopic_id(), noticeDetail.getLog_id(), 1, null, Invite_Agree_Tag);
                }
            });
        } else if (type == NotificationBean.OPERATE_202) {
            mBinding.llayoutOperate.setVisibility(View.GONE);
            mBinding.txtNotifyStatus.setVisibility(View.VISIBLE);
            mBinding.txtNotifyStatus.setText("已同意");
        } else if (type == NotificationBean.OPERATE_203) {
            mBinding.llayoutOperate.setVisibility(View.GONE);
            mBinding.txtNotifyStatus.setVisibility(View.VISIBLE);
            mBinding.txtNotifyStatus.setText("已拒绝");
        } else if (type == NotificationBean.OPERATE_204) {
            mBinding.llayoutOperate.setVisibility(View.GONE);
            mBinding.txtNotifyStatus.setVisibility(View.VISIBLE);
            mBinding.txtNotifyStatus.setText("已失效");
        }
    }

    @Override
    protected void initEvent() {

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
            case Invite_Agree_Tag:
                mBinding.llayoutOperate.setVisibility(View.GONE);
                mBinding.txtNotifyStatus.setVisibility(View.VISIBLE);
                mBinding.txtNotifyStatus.setText("已同意");
                break;
            case Invite_Refuse_Tag:
                mBinding.llayoutOperate.setVisibility(View.GONE);
                mBinding.txtNotifyStatus.setVisibility(View.VISIBLE);
                mBinding.txtNotifyStatus.setText("已拒绝");
                break;
        }
    }

    @Override
    public void progress(int precent, int tag) {

    }
}
