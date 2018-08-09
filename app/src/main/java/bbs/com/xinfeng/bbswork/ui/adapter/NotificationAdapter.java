package bbs.com.xinfeng.bbswork.ui.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.domin.NotificationBean;
import bbs.com.xinfeng.bbswork.module.GlideApp;
import bbs.com.xinfeng.bbswork.utils.FormatUtils;
import bbs.com.xinfeng.bbswork.widget.CircleImageView;

/**
 * Created by dell on 2018/4/10.
 */

public class NotificationAdapter extends BaseQuickAdapter<NotificationBean.DataBean, BaseViewHolder> {
    public NotificationAdapter(@Nullable List<NotificationBean.DataBean> data) {
        super(R.layout.item_notification, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NotificationBean.DataBean item) {
        if (item.getTypeX() == NotificationBean.OPERATE_111 || item.getTypeX() == NotificationBean.OPERATE_112 || item.getTypeX() == NotificationBean.OPERATE_113) {
            GlideApp.with(mContext).load(item.getTopic_thumb()).override(100).placeholder(R.drawable.icon_topic_default).error(R.drawable.icon_topic_default).into((CircleImageView) helper.getView(R.id.iv_head));
            helper.setText(R.id.txt_apply_name, item.getTopic_name());
        } else {
            GlideApp.with(mContext).load(item.getUser_portrait()).override(100).placeholder(R.drawable.icon_head_default).error(R.drawable.icon_head_default).into((CircleImageView) helper.getView(R.id.iv_head));
            helper.setText(R.id.txt_apply_name, item.getUser_name());
        }

        helper.setText(R.id.txt_notice_time, item.getCreated_at2());
        helper.getView(R.id.llayout_parent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListen != null)
                    mListen.onItemClick(helper.getLayoutPosition());
            }
        });
        switch (item.getTypeX()) {
            case NotificationBean.OPERATE_101:
                if (TextUtils.isEmpty(item.getMsg_join())) {
                    helper.getView(R.id.txt_apply_reason).setVisibility(View.GONE);
                } else {
                    helper.getView(R.id.txt_apply_reason).setVisibility(View.VISIBLE);
                    helper.setText(R.id.txt_apply_reason, item.getMsg_join());
                }
                helper.setText(R.id.txt_notice_title, "申请加入 " + item.getTopic_name());
                helper.getView(R.id.txt_notify_status).setVisibility(View.GONE);
                helper.getView(R.id.button_notify_operate).setVisibility(View.VISIBLE);
                helper.setText(R.id.button_notify_operate, "同意");
                helper.getView(R.id.button_notify_operate).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListen != null)
                            mListen.operateItem(helper.getLayoutPosition());
                    }
                });
                break;
            case NotificationBean.OPERATE_102:
            case NotificationBean.OPERATE_103:
            case NotificationBean.OPERATE_105:
                if (TextUtils.isEmpty(item.getMsg_join())) {
                    helper.getView(R.id.txt_apply_reason).setVisibility(View.GONE);
                } else {
                    helper.getView(R.id.txt_apply_reason).setVisibility(View.VISIBLE);
                    helper.setText(R.id.txt_apply_reason, item.getMsg_join());
                }
                helper.setText(R.id.txt_notice_title, "申请加入 " + item.getTopic_name());
                helper.getView(R.id.button_notify_operate).setVisibility(View.GONE);
                helper.getView(R.id.txt_notify_status).setVisibility(View.VISIBLE);
                if (item.getTypeX() == NotificationBean.OPERATE_102)
                    helper.setText(R.id.txt_notify_status, "已同意");
                if (item.getTypeX() == NotificationBean.OPERATE_103)
                    helper.setText(R.id.txt_notify_status, "已拒绝");
                if (item.getTypeX() == NotificationBean.OPERATE_105)
                    helper.setText(R.id.txt_notify_status, "已失效");
                break;
            case NotificationBean.OPERATE_104:
            case NotificationBean.OPERATE_111:
            case NotificationBean.OPERATE_112:
            case NotificationBean.OPERATE_113:
            case NotificationBean.OPERATE_211:
            case NotificationBean.OPERATE_212:
                helper.getView(R.id.txt_apply_reason).setVisibility(View.GONE);
                helper.getView(R.id.button_notify_operate).setVisibility(View.GONE);
                helper.getView(R.id.txt_notify_status).setVisibility(View.GONE);
                if (item.getTypeX() == NotificationBean.OPERATE_104)
                    helper.setText(R.id.txt_notice_title, "退出了" + item.getTopic_name());
                if (item.getTypeX() == NotificationBean.OPERATE_111)
                    helper.setText(R.id.txt_notice_title, "已通过您" + item.getTopic_name() + "的申请");
                if (item.getTypeX() == NotificationBean.OPERATE_112)
                    helper.setText(R.id.txt_notice_title, "拒绝了您" + item.getTopic_name() + "的申请");
                if (item.getTypeX() == NotificationBean.OPERATE_113)
                    helper.setText(R.id.txt_notice_title, "将您移除了" + item.getTopic_name());
                if (item.getTypeX() == NotificationBean.OPERATE_211)
                    helper.setText(R.id.txt_notice_title, "接受了加入" + item.getTopic_name() + "的邀请");
                if (item.getTypeX() == NotificationBean.OPERATE_212)
                    helper.setText(R.id.txt_notice_title, "拒绝了加入" + item.getTopic_name() + "的邀请");
                break;
            case NotificationBean.OPERATE_201:
                helper.getView(R.id.txt_apply_reason).setVisibility(View.GONE);
                helper.setText(R.id.txt_notice_title, "邀您加入" + item.getTopic_name());
                helper.getView(R.id.txt_notify_status).setVisibility(View.GONE);
                helper.getView(R.id.button_notify_operate).setVisibility(View.VISIBLE);
                helper.setText(R.id.button_notify_operate, "加入");
                helper.getView(R.id.button_notify_operate).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListen != null)
                            mListen.operateItem(helper.getLayoutPosition());
                    }
                });
                break;
            case NotificationBean.OPERATE_202:
            case NotificationBean.OPERATE_203:
            case NotificationBean.OPERATE_204:
                helper.getView(R.id.txt_apply_reason).setVisibility(View.GONE);
                helper.setText(R.id.txt_notice_title, "邀您加入" + item.getTopic_name());
                helper.getView(R.id.button_notify_operate).setVisibility(View.GONE);
                helper.getView(R.id.txt_notify_status).setVisibility(View.VISIBLE);
                if (item.getTypeX() == NotificationBean.OPERATE_202)
                    helper.setText(R.id.txt_notify_status, "已同意");
                if (item.getTypeX() == NotificationBean.OPERATE_203)
                    helper.setText(R.id.txt_notify_status, "已拒绝");
                if (item.getTypeX() == NotificationBean.OPERATE_204)
                    helper.setText(R.id.txt_notify_status, "已失效");
                break;
        }

    }

    public interface onItemOperateListen {
        void operateItem(int position);

        void onItemClick(int position);
    }

    private onItemOperateListen mListen;

    public void setmListen(onItemOperateListen mListen) {
        this.mListen = mListen;
    }
}
