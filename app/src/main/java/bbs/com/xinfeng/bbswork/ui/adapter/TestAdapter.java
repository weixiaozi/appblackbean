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
import bbs.com.xinfeng.bbswork.databinding.ItemTest1Binding;
import bbs.com.xinfeng.bbswork.utils.ImageLoader;

/**
 * Created by dell on 2017/10/27.
 */

public class TestAdapter extends BaseRvAdapter<ItemTest1Binding> {
    private List<String> datas;

    public TestAdapter(Context mContext, List datas) {
        super(mContext);
        this.datas = datas;
    }

    @Override
    protected MyViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        return bind(R.layout.item_test1, parent);
    }

    @Override
    protected void onBindHolder(MyViewHolder holder, int position) {
        new ImageLoader().setUrl(datas.get(position)).setmCacheStrategy(DiskCacheStrategy.ALL).into(holder.mBinding.itemIv).build();
        holder.mBinding.itemIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(App.mApp, holder.getLayoutPosition() + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }
}
