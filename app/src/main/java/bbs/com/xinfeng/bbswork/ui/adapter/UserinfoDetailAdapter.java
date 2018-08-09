package bbs.com.xinfeng.bbswork.ui.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.domin.TopicListBean;
import bbs.com.xinfeng.bbswork.domin.UserDetailBean;
import bbs.com.xinfeng.bbswork.module.GlideApp;
import bbs.com.xinfeng.bbswork.utils.FormatUtils;
import bbs.com.xinfeng.bbswork.widget.CircleImageView;

/**
 * Created by dell on 2018/4/10.
 */

public class UserinfoDetailAdapter extends BaseQuickAdapter<UserDetailBean.TopicsBean, BaseViewHolder> {
    public UserinfoDetailAdapter(@Nullable List<UserDetailBean.TopicsBean> data) {
        super(R.layout.item_userinfo, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserDetailBean.TopicsBean item) {
        GlideApp.with(mContext).load(item.getImg_url_thumb()).override(100).placeholder(R.drawable.icon_topic_default).error(R.drawable.icon_topic_default).into((CircleImageView) helper.getView(R.id.iv_topic_page));
        helper.setText(R.id.txt_topic_name, item.getName());
        helper.setText(R.id.txt_topic_join, FormatUtils.dataToUseData(item.getTime_label()));
        helper.setText(R.id.txt_publistheme_num, item.getThread_num() + "");
        helper.setText(R.id.txt_replytheme_num, item.getPost_num() + "");
        helper.setText(R.id.txt_zantheme_num, item.getLike_num() + "");
    }
}
