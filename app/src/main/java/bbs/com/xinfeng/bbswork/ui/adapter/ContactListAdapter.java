package bbs.com.xinfeng.bbswork.ui.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.domin.ContactListBean;
import bbs.com.xinfeng.bbswork.domin.TopicListBean;
import bbs.com.xinfeng.bbswork.module.GlideApp;
import bbs.com.xinfeng.bbswork.utils.FormatUtils;
import bbs.com.xinfeng.bbswork.widget.CircleImageView;

/**
 * Created by dell on 2018/4/10.
 */

public class ContactListAdapter extends BaseQuickAdapter<ContactListBean.DataBean, BaseViewHolder> {
    public ContactListAdapter(@Nullable List<ContactListBean.DataBean> data) {
        super(R.layout.item_contact, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ContactListBean.DataBean item) {
        GlideApp.with(mContext).load(item.getPortrait_thumb()).override(118).placeholder(R.drawable.icon_head_default).error(R.drawable.icon_head_default).into((CircleImageView) helper.getView(R.id.iv_head));
        helper.setText(R.id.txt_member_name, item.getUsername());

    }
}
