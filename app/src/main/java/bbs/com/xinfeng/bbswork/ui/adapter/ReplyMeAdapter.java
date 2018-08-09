package bbs.com.xinfeng.bbswork.ui.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.domin.ReplyMeListBean;
import bbs.com.xinfeng.bbswork.module.GlideApp;
import bbs.com.xinfeng.bbswork.utils.FormatUtils;
import bbs.com.xinfeng.bbswork.utils.SpanUtil;
import bbs.com.xinfeng.bbswork.widget.CircleImageView;

/**
 * Created by dell on 2018/4/10.
 */

public class ReplyMeAdapter extends BaseQuickAdapter<ReplyMeListBean.DataBean, BaseViewHolder> {

    public ReplyMeAdapter(@Nullable List<ReplyMeListBean.DataBean> data) {
        super(R.layout.item_replyme, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ReplyMeListBean.DataBean item) {
        GlideApp.with(mContext).load(item.getReplyUser().getPortrait_thumb()).override(48).placeholder(R.drawable.icon_head_default).error(R.drawable.icon_head_default).into((CircleImageView) helper.getView(R.id.iv_head));

        helper.setText(R.id.txt_name, item.getReplyUser().getName());
        helper.setText(R.id.txt_time, item.getReply().getCreated_at2());
        helper.setText(R.id.txt_reply_content, SpanUtil.getExpressionString(mContext, item.getReply().getContent(), true, helper.getView(R.id.txt_reply_content)));
        helper.getView(R.id.txt_reply_content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListen != null)
                    mListen.operateItem(helper.getLayoutPosition());
            }
        });
        helper.setText(R.id.txt_replyme_source, SpanUtil.getExpressionString(mContext, "回复我的：" + item.getMy().getContent(), true, helper.getView(R.id.txt_replyme_source)));
        helper.getView(R.id.txt_replyme_source).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListen != null)
                    mListen.operateItem(helper.getLayoutPosition());
            }
        });
        helper.setText(R.id.txt_reply_topicname, item.getTopic().getName());
        helper.getView(R.id.llayout_collect_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListen != null)
                    mListen.operateItem(helper.getLayoutPosition());
            }
        });
        helper.getView(R.id.iv_head).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListen != null)
                    mListen.goUserInfo(helper.getLayoutPosition());
            }
        });

    }


    public interface onItemOperateListen {
        void operateItem(int position);

        void goUserInfo(int position);
    }

    private onItemOperateListen mListen;

    public void setmListen(onItemOperateListen mListen) {
        this.mListen = mListen;
    }
}
