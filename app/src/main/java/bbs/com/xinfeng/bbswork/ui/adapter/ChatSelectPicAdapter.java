package bbs.com.xinfeng.bbswork.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.io.File;
import java.util.Collections;
import java.util.List;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.base.BaseRvAdapter;
import bbs.com.xinfeng.bbswork.databinding.ItemChatSelectpicBinding;
import bbs.com.xinfeng.bbswork.module.GlideApp;
import bbs.com.xinfeng.bbswork.ui.activity.ChattingActivity;
import bbs.com.xinfeng.bbswork.ui.adapter.layoutmanager.ItemTouchHelperAdapter;
import bbs.com.xinfeng.bbswork.utils.ArmsUtils;
import bbs.com.xinfeng.bbswork.utils.LogUtil;
import bbs.com.xinfeng.bbswork.utils.ScreenUtils;

/**
 * Created by dell on 2018/4/19.
 */

public class ChatSelectPicAdapter extends BaseRvAdapter<ItemChatSelectpicBinding> implements ItemTouchHelperAdapter {
    public static final int maxPics = 9;
    private List<String> mDatas;
    private int height;

    public ChatSelectPicAdapter(Context mContext, List data) {
        super(mContext);
        mDatas = data;
        height = (ScreenUtils.getScreenWidth(mContext) - ArmsUtils.dip2px(mContext, 60)) / 4;
    }

    @Override
    protected MyViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = bind(R.layout.item_chat_selectpic, parent);
        holder.mBinding.fllayoutRoot.setLayoutParams(new LinearLayout.LayoutParams(height, height));
        return holder;
    }

    @Override
    protected void onBindHolder(MyViewHolder holder, int position) {
        String url = mDatas.get(position);
        if (url.equals(ChattingActivity.addButton)) {
            holder.mBinding.ivChatDel.setVisibility(View.GONE);
            holder.mBinding.ivChatPic.setImageResource(R.drawable.icon_chat_addpic);
        } else {
            holder.mBinding.ivChatDel.setVisibility(View.VISIBLE);
            GlideApp.with(mContext).load(Uri.fromFile(new File(url))).override(height).into(holder.mBinding.ivChatPic);
        }
        holder.mBinding.ivChatPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListen != null) {
                    LogUtil.i("ccsssww", getItemCount() + "____" + holder.getLayoutPosition());
                    if (url.equals(ChattingActivity.addButton)) {
                        mListen.onItemaddClick(holder.getLayoutPosition());
                    } else {
                        mListen.onItempicClick(holder.getLayoutPosition());
                    }
                }
            }
        });
        holder.mBinding.ivChatDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListen != null)
                    mListen.onItemdelClick(holder.getLayoutPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public void onItemMove(RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        if (target.getAdapterPosition() == getItemCount() - 1 && getItemCount() != maxPics) {
        } else {
            Collections.swap(mDatas, source.getAdapterPosition(), target.getAdapterPosition());
            notifyItemMoved(source.getAdapterPosition(), target.getAdapterPosition());

        }
    }

    public interface onItemOperateListen {

        void onItempicClick(int position);

        void onItemdelClick(int position);

        void onItemaddClick(int position);
    }

    private onItemOperateListen mListen;

    public void setmListen(onItemOperateListen mListen) {
        this.mListen = mListen;
    }
}
