package bbs.com.xinfeng.bbswork.ui.adapter.layoutmanager;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import bbs.com.xinfeng.bbswork.ui.adapter.ChatSelectPicAdapter;


/**
 * Created by dell on 2018/4/19.
 */

public class ChatPicsTouchHelperCallback extends ItemTouchHelper.Callback {
    private RecyclerView.Adapter mAdapter;
    private ItemTouchHelperAdapter mAdapterCallback;

    public ChatPicsTouchHelperCallback(RecyclerView.Adapter adapter, ItemTouchHelperAdapter adapterCallback) {
        mAdapterCallback = adapterCallback;
        mAdapter = adapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (viewHolder.getAdapterPosition() == mAdapter.getItemCount() - 1 && mAdapter.getItemCount() != ChatSelectPicAdapter.maxPics) {
            return 0;
        } else {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT; //允许上下左右的拖动
            return makeMovementFlags(dragFlags, 0);
        }
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        mAdapterCallback.onItemMove(viewHolder, target);
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;//长按启用拖拽
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false; //不启用拖拽删除
    }
}
