package bbs.com.xinfeng.bbswork.ui.adapter.layoutmanager;

import android.support.v7.widget.RecyclerView;

/**
 * Created by dell on 2018/4/19.
 */

public interface ItemTouchHelperAdapter {
    //数据交换
    void onItemMove(RecyclerView.ViewHolder source, RecyclerView.ViewHolder target);
}
