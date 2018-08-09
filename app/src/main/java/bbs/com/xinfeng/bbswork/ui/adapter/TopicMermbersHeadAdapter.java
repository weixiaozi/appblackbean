package bbs.com.xinfeng.bbswork.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.base.App;
import bbs.com.xinfeng.bbswork.base.BaseRvAdapter;
import bbs.com.xinfeng.bbswork.databinding.ItemMemberHeadBinding;
import bbs.com.xinfeng.bbswork.databinding.ItemTest1Binding;
import bbs.com.xinfeng.bbswork.domin.TopicDetailBean;
import bbs.com.xinfeng.bbswork.module.GlideApp;
import bbs.com.xinfeng.bbswork.utils.ImageLoader;
import bbs.com.xinfeng.bbswork.widget.CircleImageView;

/**
 * Created by dell on 2017/10/27.
 */

public class TopicMermbersHeadAdapter extends BaseRvAdapter<ItemMemberHeadBinding> {
    private List<TopicDetailBean.FansBean> datas;

    public TopicMermbersHeadAdapter(Context mContext, List datas) {
        super(mContext);
        this.datas = datas;
    }

    @Override
    protected MyViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = bind(R.layout.item_member_head, parent);
        holder.mBinding.ivHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListen != null)
                    mListen.onclick(holder.getLayoutPosition());
            }
        });
        return holder;
    }

    @Override
    protected void onBindHolder(MyViewHolder holder, int position) {
        GlideApp.with(mContext).load(datas.get(position).getUser_portrait_thumb()).override(72).placeholder(R.drawable.icon_head_default).error(R.drawable.icon_head_default).into(holder.mBinding.ivHead);
    }

    @Override
    public int getItemCount() {
        int size = datas.size();
        if (size > 5) {
            size = 5;
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
