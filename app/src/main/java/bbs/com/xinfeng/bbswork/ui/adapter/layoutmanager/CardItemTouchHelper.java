package bbs.com.xinfeng.bbswork.ui.adapter.layoutmanager;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import java.util.List;

/**
 * Created by dell on 2017/10/31.
 */

public class CardItemTouchHelper<T> extends ItemTouchHelper.Callback {
    private List<T> mDatas;
    private RecyclerView.Adapter adapter;

    public CardItemTouchHelper(RecyclerView.Adapter adapter, List<T> list) {
        this.adapter = adapter;
        this.mDatas = list;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int swipFlags = 0;
        if (recyclerView.getLayoutManager() instanceof CardLayouManager) {
            swipFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        }
        return makeMovementFlags(0, swipFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        viewHolder.itemView.setOnTouchListener(null);
        int from = viewHolder.getLayoutPosition();
        mDatas.remove(from);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        View itemView = viewHolder.itemView;
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            float radio = dX / getThreshold(recyclerView, viewHolder);
            if (radio > 1)
                radio = 1;
            if (radio < -1)
                radio = -1;
            itemView.setRotation(radio * 15);
            int childCount = recyclerView.getChildCount();
            if (childCount > CardLayouManager.DEFAULT_SHOW_ITEM) {
                for (int position = 1; position < childCount - 1; position++) {
                    int index = childCount - position - 1;
                    View view = recyclerView.getChildAt(position);
                    // 和之前 onLayoutChildren 是一个意思，不过是做相反的动画
                    view.setScaleX(1 - index * 0.1f + Math.abs(radio) * 0.1f);
                    view.setScaleY(1 - index * 0.1f + Math.abs(radio) * 0.1f);
                    view.setTranslationY((index - Math.abs(radio)) * itemView.getMeasuredHeight() / CardLayouManager.DEFAULT_TRANSLATE_Y);
                }

            } else {
                // 当数据源个数小于或等于最大显示数时
                for (int position = 0; position < childCount - 1; position++) {
                    int index = childCount - position - 1;
                    View view = recyclerView.getChildAt(position);
                    view.setScaleX(1 - index * 0.1f + Math.abs(radio) * 0.1f);
                    view.setScaleY(1 - index * 0.1f + Math.abs(radio) * 0.1f);
                    view.setTranslationY((index - Math.abs(radio)) * itemView.getMeasuredHeight() / CardLayouManager.DEFAULT_TRANSLATE_Y);
                }
            }
        }
    }

    private float getThreshold(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
//        Log.i("mmmmm", getSwipeThreshold(viewHolder) + "");
        return recyclerView.getWidth() * getSwipeThreshold(viewHolder);
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return super.isItemViewSwipeEnabled();
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setRotation(0f);
    }
}
