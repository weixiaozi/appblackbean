package bbs.com.xinfeng.bbswork.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.base.BaseRvAdapter;
import bbs.com.xinfeng.bbswork.databinding.ItemMemberHeadWithNameBinding;
import bbs.com.xinfeng.bbswork.domin.TopicDetailBean;
import bbs.com.xinfeng.bbswork.module.GlideApp;

/**
 * Created by dell on 2017/10/27.
 */

public class TopicMermbersHeadWithNameAdapter extends BaseRvAdapter<ItemMemberHeadWithNameBinding> {
    private List<TopicDetailBean.FansBean> datas;

    public TopicMermbersHeadWithNameAdapter(Context mContext, List datas) {
        super(mContext);
        this.datas = datas;
    }

    @Override
    protected MyViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        return bind(R.layout.item_member_head_with_name, parent);
    }

    @Override
    protected void onBindHolder(MyViewHolder holder, int position) {
        if (datas.get(position).getDrawableId() == 0) {
            GlideApp.with(mContext).load(datas.get(position).getUser_portrait_thumb()).override(100).placeholder(R.drawable.icon_head_default).error(R.drawable.icon_head_default).into(holder.mBinding.ivHead);
            holder.mBinding.txtName.setTextColor(mContext.getResources().getColor(R.color.black));
            holder.mBinding.txtName.setText(datas.get(position).getUser_name());
        } else {
            holder.mBinding.ivHead.setImageResource(datas.get(position).getDrawableId());
            if (position == 0)
                holder.mBinding.txtName.setTextColor(mContext.getResources().getColor(R.color.register_code));
            else
                holder.mBinding.txtName.setTextColor(mContext.getResources().getColor(R.color.input_txt_lenght));
            holder.mBinding.txtName.setText(datas.get(position).getUser_name());
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
        if (size > 10) {
            size = 10;
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
