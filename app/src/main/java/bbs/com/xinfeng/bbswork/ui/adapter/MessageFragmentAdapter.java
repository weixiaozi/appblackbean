package bbs.com.xinfeng.bbswork.ui.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.domin.MessageFragmentInnerBean;
import bbs.com.xinfeng.bbswork.domin.TopListInnerBean;
import bbs.com.xinfeng.bbswork.module.GlideApp;
import bbs.com.xinfeng.bbswork.utils.ArmsUtils;
import bbs.com.xinfeng.bbswork.utils.FormatUtils;
import bbs.com.xinfeng.bbswork.utils.ScreenUtils;
import bbs.com.xinfeng.bbswork.utils.SpanUtil;

/**
 * Created by dell on 2018/4/10.
 */

public class MessageFragmentAdapter extends BaseQuickAdapter<MessageFragmentInnerBean, BaseViewHolder> {

    public MessageFragmentAdapter(@Nullable List<MessageFragmentInnerBean> data) {
        super(R.layout.item_message_fragment, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, MessageFragmentInnerBean item) {
        GlideApp.with(mContext).load(item.getImg()).override(150).placeholder(R.drawable.icon_head_default).error(R.drawable.icon_head_default).into((ImageView) helper.getView(R.id.iv_head));
        helper.setText(R.id.txt_friend_name, item.getLabel());
        helper.setText(R.id.txt_last_content, SpanUtil.getExpressionString(mContext, item.getLast_msg(), false, helper.getView(R.id.txt_last_content)));
        helper.setText(R.id.txt_last_time, FormatUtils.dataToUseData(item.getLast_at()));
        if (item.getN() == 0) {
            helper.getView(R.id.txt_operate_mes).setVisibility(View.INVISIBLE);
        } else {
            helper.getView(R.id.txt_operate_mes).setVisibility(View.VISIBLE);
            helper.setText(R.id.txt_operate_mes, item.getN() < 100 ? item.getN() + "" : "99+");
        }
        helper.getView(R.id.llayout_parent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListen != null)
                    mListen.onClickItem(helper.getLayoutPosition() - 1);
            }
        });
        helper.getView(R.id.llayout_parent).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mListen != null)
                    mListen.onLongClickItem(helper.getLayoutPosition() - 1);
                return true;
            }
        });
    }

    public interface onItemOperateListen {
        void onClickItem(int position);

        void onLongClickItem(int postion);
    }

    private onItemOperateListen mListen;

    public void setmListen(onItemOperateListen mListen) {
        this.mListen = mListen;
    }

}
