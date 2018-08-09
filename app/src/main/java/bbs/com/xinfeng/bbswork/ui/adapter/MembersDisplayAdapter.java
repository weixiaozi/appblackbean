package bbs.com.xinfeng.bbswork.ui.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.domin.MemberAllBean;
import bbs.com.xinfeng.bbswork.module.GlideApp;
import bbs.com.xinfeng.bbswork.widget.CircleImageView;

import static bbs.com.xinfeng.bbswork.ui.activity.MembersListActivity.ACTION_IN;

/**
 * Created by dell on 2018/4/10.
 */

public class MembersDisplayAdapter extends BaseQuickAdapter<MemberAllBean.DataBean, BaseViewHolder> {

    public MembersDisplayAdapter(@Nullable List<MemberAllBean.DataBean> data) {
        super(R.layout.item_memberdisplay, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MemberAllBean.DataBean item) {
        GlideApp.with(mContext).load(item.getUser_portrait_thumb()).override(72).placeholder(R.drawable.icon_head_default).error(R.drawable.icon_head_default).into((CircleImageView) helper.getView(R.id.iv_head));
        helper.setText(R.id.txt_member_name, item.getUser_name());
        helper.getView(R.id.iv_member_online).setVisibility(item.getOnline() == 1 ? View.VISIBLE : View.GONE);
        helper.getView(R.id.txt_isauthor).setVisibility(item.getIsauther() == 1 ? View.VISIBLE : View.GONE);
    }

}
