package bbs.com.xinfeng.bbswork.ui.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
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

public class AtMembersAdapter extends BaseQuickAdapter<MemberAllBean.DataBean, BaseViewHolder> {
    private String searchContent;

    public AtMembersAdapter(@Nullable List<MemberAllBean.DataBean> data) {
        super(R.layout.item_atmembers, data);
    }

    public void setSearchContent(String searchContent) {
        this.searchContent = searchContent;
    }

    @Override
    protected void convert(BaseViewHolder helper, MemberAllBean.DataBean item) {
        if (!TextUtils.isEmpty(searchContent)) {
            if (item.getUser_name().contains(searchContent)) {

                helper.getView(R.id.llayout_atmember_root).setVisibility(View.VISIBLE);
            } else {
                helper.getView(R.id.llayout_atmember_root).setVisibility(View.GONE);
            }
        } else {
            helper.getView(R.id.llayout_atmember_root).setVisibility(View.VISIBLE);
        }
        GlideApp.with(mContext).load(item.getUser_portrait_thumb()).override(72).placeholder(R.drawable.icon_head_default).error(R.drawable.icon_head_default).into((CircleImageView) helper.getView(R.id.iv_head));
        helper.setText(R.id.txt_member_name, item.getUser_name());
        if (item.getSelected() == 1) {
            ((ImageView) helper.getView(R.id.iv_member_select)).setImageResource(R.drawable.icon_select_black);
        } else {
            ((ImageView) helper.getView(R.id.iv_member_select)).setImageResource(R.drawable.icon_unselect);
        }
        helper.getView(R.id.iv_member_select).setOnClickListener(new View.OnClickListener() {
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
