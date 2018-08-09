package bbs.com.xinfeng.bbswork.ui.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.domin.MyReplyListBean;
import bbs.com.xinfeng.bbswork.utils.FormatUtils;
import bbs.com.xinfeng.bbswork.utils.SpanUtil;

/**
 * Created by dell on 2018/4/10.
 */

public class MyReplyAdapter extends BaseQuickAdapter<MyReplyListBean.DataBean, BaseViewHolder> {

    public MyReplyAdapter(@Nullable List<MyReplyListBean.DataBean> data) {
        super(R.layout.item_myreply, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyReplyListBean.DataBean item) {
        helper.setText(R.id.txt_time, item.getMy().getCreated_at2());
        helper.setText(R.id.txt_reply_content, SpanUtil.getExpressionString(mContext, item.getMy().getContent(),
                true, helper.getView(R.id.txt_reply_content)));
        helper.getView(R.id.txt_reply_content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListen != null)
                    mListen.operateItem(helper.getLayoutPosition());
            }
        });
        helper.setText(R.id.txt_theme_topicname, SpanUtil.getExpressionString(mContext, "原观点：" + item.getThread().getContent(),
                true, helper.getView(R.id.txt_theme_topicname)));
        helper.getView(R.id.txt_theme_topicname).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListen != null)
                    mListen.operateItem(helper.getLayoutPosition());
            }
        });

        helper.getView(R.id.llayout_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListen != null)
                    mListen.operateItem(helper.getLayoutPosition());
            }
        });
    }


    public interface onItemOperateListen {
        void operateItem(int position);

    }

    private onItemOperateListen mListen;

    public void setmListen(onItemOperateListen mListen) {
        this.mListen = mListen;
    }
}
