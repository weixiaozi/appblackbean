package bbs.com.xinfeng.bbswork.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.List;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.base.BaseRvAdapter;
import bbs.com.xinfeng.bbswork.databinding.ItemChatpicBinding;
import bbs.com.xinfeng.bbswork.module.GlideApp;
import bbs.com.xinfeng.bbswork.utils.ArmsUtils;

/**
 * Created by dell on 2018/4/10.
 */

public class ChatPicsAdapter extends BaseRvAdapter<ItemChatpicBinding> {
    List<String> mdatas;
    private int itemCount;
    private int picWidht;

    public ChatPicsAdapter(Context mContext, List<String> datas) {
        super(mContext);
        mdatas = datas;
    }

    @Override
    protected MyViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = bind(R.layout.item_chatpic, parent);
        if (picWidht != 0) {
            holder.mBinding.ivChatPic.getLayoutParams().width = picWidht;
            holder.mBinding.ivChatPic.getLayoutParams().height = picWidht;
        }
        holder.mBinding.ivChatPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListen != null)
                    mListen.operateItem(holder.getLayoutPosition(), v);
            }
        });
        return holder;
    }

    public void setMdatas(List<String> mdatas) {
        this.mdatas = mdatas;
    }

    @Override
    protected void onBindHolder(MyViewHolder holder, int position) {
        if (!mdatas.get(position).startsWith("http"))
            GlideApp.with(mContext).load(Uri.fromFile(new File(mdatas.get(position)))).centerCrop().override(picWidht == 0 ? 240 : picWidht).placeholder(R.drawable.icon_theme_default).error(R.drawable.icon_theme_default).into(holder.mBinding.ivChatPic);
        else
            GlideApp.with(mContext).load(mdatas.get(position)).override(picWidht == 0 ? 240 : picWidht).centerCrop().placeholder(R.drawable.icon_theme_default).error(R.drawable.icon_theme_default).into(holder.mBinding.ivChatPic);
        if (picWidht != 0 && position == 2 && mdatas.size() > 3) {
            holder.mBinding.txtChatPicNum.setVisibility(View.VISIBLE);
            holder.mBinding.txtChatPicNum.setText("+" + (mdatas.size() - 3));
        } else {
            holder.mBinding.txtChatPicNum.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return itemCount == 0 ? mdatas.size() : (mdatas.size() < itemCount ? mdatas.size() : 3);
    }


    public interface onItemOperateListen {
        void operateItem(int position, View v);
    }

    private onItemOperateListen mListen;

    public void setmListen(onItemOperateListen mListen) {
        this.mListen = mListen;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public int getPicWidht() {
        return picWidht;
    }

    public void setPicWidht(int picWidht) {
        this.picWidht = ArmsUtils.dip2px(mContext, picWidht);
    }
}
