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

public class MemberslistAdapter extends BaseQuickAdapter<MemberAllBean.DataBean, BaseViewHolder> {
    private int action;
    private String topicName;

    public MemberslistAdapter(@Nullable List<MemberAllBean.DataBean> data, int action, String topicname) {
        super(R.layout.item_memberlist, data);
        this.action = action;
        this.topicName = topicName;
    }

    @Override
    protected void convert(BaseViewHolder helper, MemberAllBean.DataBean item) {
        GlideApp.with(mContext).load(item.getUser_portrait_thumb()).override(72).placeholder(R.drawable.icon_head_default).error(R.drawable.icon_head_default).into((CircleImageView) helper.getView(R.id.iv_head));
        helper.setText(R.id.txt_member_name, item.getUser_name());
        if (action == ACTION_IN) {
            if (item.getIsjoin() == 1) {
                ((ImageView) helper.getView(R.id.iv_member_select)).setImageResource(R.drawable.icon_select_white);
                helper.getView(R.id.txt_topic_status).setVisibility(View.VISIBLE);
            } else {
                helper.getView(R.id.txt_topic_status).setVisibility(View.GONE);
                if (item.getSelected() == 1) {
                    ((ImageView) helper.getView(R.id.iv_member_select)).setImageResource(R.drawable.icon_select_black);
                } else {
                    ((ImageView) helper.getView(R.id.iv_member_select)).setImageResource(R.drawable.icon_unselect);
                }
            }
        } else {
            if (item.getIsauther() == 1 || item.getIsme() == 1) {
                ((ImageView) helper.getView(R.id.iv_member_select)).setImageResource(R.drawable.icon_select_white);
            } else {
                if (item.getSelected() == 1) {
                    ((ImageView) helper.getView(R.id.iv_member_select)).setImageResource(R.drawable.icon_select_black);
                } else {
                    ((ImageView) helper.getView(R.id.iv_member_select)).setImageResource(R.drawable.icon_unselect);
                }
            }
        }

    }

    public interface onItemOperateListen {
        void operateItem(int position);
    }

    private onItemOperateListen mListen;

    public void setmListen(onItemOperateListen mListen) {
        this.mListen = mListen;
    }
}
