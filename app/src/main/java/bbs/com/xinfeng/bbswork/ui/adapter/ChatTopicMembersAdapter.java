package bbs.com.xinfeng.bbswork.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.base.BaseRvAdapter;
import bbs.com.xinfeng.bbswork.databinding.ItemTopicMemberHeadBinding;
import bbs.com.xinfeng.bbswork.domin.ChatTopicMembersBean;
import bbs.com.xinfeng.bbswork.module.GlideApp;

/**
 * Created by dell on 2017/10/27.
 */

public class ChatTopicMembersAdapter extends BaseRvAdapter<ItemTopicMemberHeadBinding> {
    private List<ChatTopicMembersBean.ListBean> datas;

    public ChatTopicMembersAdapter(Context mContext, List datas) {
        super(mContext);
        this.datas = datas;
    }

    @Override
    protected MyViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        return bind(R.layout.item_topic_member_head, parent);
    }

    @Override
    protected void onBindHolder(MyViewHolder holder, int position) {
        ChatTopicMembersBean.ListBean info = datas.get(position);
        if (info.getDrawableId() == 0) {
            GlideApp.with(mContext).load(info.getUser_portrait_thumb()).override(60).placeholder(R.drawable.icon_head_default).error(R.drawable.icon_head_default).into(holder.mBinding.ivHead);
        } else {
            holder.mBinding.ivHead.setImageResource(info.getDrawableId());
        }
        if (info.getIs_new() == 1) {
            holder.mBinding.ivLable.setVisibility(View.VISIBLE);
            holder.mBinding.ivMemberOnline.setVisibility(View.INVISIBLE);
        } else if (info.getOnline() == 1) {
            holder.mBinding.ivLable.setVisibility(View.INVISIBLE);
            holder.mBinding.ivMemberOnline.setVisibility(View.VISIBLE);
        } else {
            holder.mBinding.ivLable.setVisibility(View.INVISIBLE);
            holder.mBinding.ivMemberOnline.setVisibility(View.INVISIBLE);
        }
        holder.mBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListen != null)
                    mListen.onclick(holder.getLayoutPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        int size = datas.size();
        if (size > 9) {
            size = 9;
        }
        return size;
    }

    public interface onItemClickListen {
        void onclick(int position);
    }

    private onItemClickListen mListen;

    public void setOnItemClickListen(onItemClickListen mListen) {
        this.mListen = mListen;
    }
}
