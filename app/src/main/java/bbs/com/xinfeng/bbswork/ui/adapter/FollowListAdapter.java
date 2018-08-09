package bbs.com.xinfeng.bbswork.ui.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.domin.MyFansListBean;
import bbs.com.xinfeng.bbswork.module.GlideApp;
import bbs.com.xinfeng.bbswork.widget.CircleImageView;

/**
 * Created by dell on 2018/4/10.
 */

public class FollowListAdapter extends BaseQuickAdapter<MyFansListBean.DataBean, BaseViewHolder> {

    public FollowListAdapter(@Nullable List<MyFansListBean.DataBean> data) {
        super(R.layout.item_followlist, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyFansListBean.DataBean item) {
        helper.getView(R.id.iv_head).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListen != null)
                    mListen.onItemClick(v, helper.getLayoutPosition());
            }
        });
        GlideApp.with(mContext).load(item.getPortrait_thumb()).override(72).placeholder(R.drawable.icon_head_default).error(R.drawable.icon_head_default).into((CircleImageView) helper.getView(R.id.iv_head));
        helper.setText(R.id.txt_member_name, item.getUsername());
        helper.getView(R.id.iv_member_online).setVisibility(item.getIsonline() == 1 ? View.VISIBLE : View.GONE);
        helper.getView(R.id.txt_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListen != null)
                    mListen.onDeleteBtnCilck(v, helper.getLayoutPosition());
            }
        });

    }


    public interface onItemOperateListen {
        void onItemClick(View view, int position);

        void onDeleteBtnCilck(View view, int position);
    }

    private onItemOperateListen mListen;

    public void setmListen(onItemOperateListen mListen) {
        this.mListen = mListen;
    }
}
