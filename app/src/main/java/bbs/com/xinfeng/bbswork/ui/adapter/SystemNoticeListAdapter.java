package bbs.com.xinfeng.bbswork.ui.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.domin.SystemNoticesBean;
import bbs.com.xinfeng.bbswork.domin.TopicListBean;
import bbs.com.xinfeng.bbswork.module.GlideApp;
import bbs.com.xinfeng.bbswork.utils.FormatUtils;
import bbs.com.xinfeng.bbswork.utils.SpanUtil;
import bbs.com.xinfeng.bbswork.widget.CircleImageView;

/**
 * Created by dell on 2018/4/10.
 */

public class SystemNoticeListAdapter extends BaseQuickAdapter<SystemNoticesBean.DataBean, BaseViewHolder> {
    public SystemNoticeListAdapter(@Nullable List<SystemNoticesBean.DataBean> data) {
        super(R.layout.item_system_notices, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SystemNoticesBean.DataBean item) {
        helper.setText(R.id.txt_notice_title, item.getTitle());
        helper.setText(R.id.txt_notice_content, SpanUtil.getExpressionString(mContext, item.getContent(),
                true, helper.getView(R.id.txt_notice_content)));
        helper.setText(R.id.txt_notice_time, item.getCreated_at2());
    }
}
