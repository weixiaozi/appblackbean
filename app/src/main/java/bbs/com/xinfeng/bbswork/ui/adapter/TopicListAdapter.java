package bbs.com.xinfeng.bbswork.ui.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.domin.TopicListBean;
import bbs.com.xinfeng.bbswork.module.GlideApp;
import bbs.com.xinfeng.bbswork.utils.FormatUtils;
import bbs.com.xinfeng.bbswork.widget.CircleImageView;

/**
 * Created by dell on 2018/4/10.
 */

public class TopicListAdapter extends BaseQuickAdapter<TopicListBean.DataBean, BaseViewHolder> {
    public TopicListAdapter(@Nullable List<TopicListBean.DataBean> data) {
        super(R.layout.item_topiclist, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TopicListBean.DataBean item) {
        GlideApp.with(mContext).load(item.getImg_url_thumb()).override(100).placeholder(R.drawable.icon_topic_default).error(R.drawable.icon_topic_default).into((CircleImageView) helper.getView(R.id.iv_topic_page));
        helper.setText(R.id.txt_topic_name, item.getName());
        helper.setText(R.id.txt_topic_author, item.getCreate_username() + " " + item.getTime_label());
        helper.setText(R.id.txt_topic_members, item.getFans_number() + "成员    " + item.getThread_number() + "观点");
        if (item.getIsjoin() == 0) {
            helper.getView(R.id.iv_topic_joined).setVisibility(View.GONE);
            helper.getView(R.id.txt_topic_status).setVisibility(View.GONE);
        } else if (item.getIsjoin() == 1) {
            helper.getView(R.id.iv_topic_joined).setVisibility(View.VISIBLE);
            helper.getView(R.id.txt_topic_status).setVisibility(View.VISIBLE);
            helper.setText(R.id.txt_topic_status, "已加入");
        } else if (item.getIsjoin() == 2) {
            helper.getView(R.id.iv_topic_joined).setVisibility(View.GONE);
            helper.getView(R.id.txt_topic_status).setVisibility(View.VISIBLE);
            helper.setText(R.id.txt_topic_status, "申请中");
        } else if (item.getIsjoin() == 6) {
            helper.getView(R.id.iv_topic_joined).setVisibility(View.GONE);
            helper.getView(R.id.txt_topic_status).setVisibility(View.VISIBLE);
            helper.setText(R.id.txt_topic_status, "被拒绝");
        }
    }
}
