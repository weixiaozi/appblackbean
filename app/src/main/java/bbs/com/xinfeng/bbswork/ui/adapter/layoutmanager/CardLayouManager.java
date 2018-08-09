package bbs.com.xinfeng.bbswork.ui.adapter.layoutmanager;

import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by dell on 2017/10/31.
 */

public class CardLayouManager extends RecyclerView.LayoutManager {
    static final int DEFAULT_TRANSLATE_Y = 14;
    static int DEFAULT_SHOW_ITEM = 3;
    private RecyclerView mRecyclerView;
    private ItemTouchHelper mItemTouchHelper;
    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            RecyclerView.ViewHolder childViewHolder = mRecyclerView.getChildViewHolder(v);

            if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                mItemTouchHelper.startSwipe(childViewHolder);
            }
            return false;
        }
    };

    public CardLayouManager(RecyclerView mRecyclerView, ItemTouchHelper mItemTouchHelper) {
        this.mRecyclerView = mRecyclerView;
        this.mItemTouchHelper = mItemTouchHelper;
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        removeAllViews();
        detachAndScrapAttachedViews(recycler);

        int itemCount = getItemCount();

        if (itemCount >= DEFAULT_SHOW_ITEM) {
            for (int position = DEFAULT_SHOW_ITEM; position >= 0; position--) {
                View view = recycler.getViewForPosition(position);
                addView(view);
                measureChildWithMargins(view, 0, 0);
                int widthSpace = getWidth() - getDecoratedMeasuredWidth(view);
                int heightSpace = getHeight() - getDecoratedMeasuredHeight(view);

                layoutDecoratedWithMargins(view, widthSpace / 2, heightSpace / 2, getWidth() - widthSpace / 2, getHeight() - heightSpace / 2);

                if (position == DEFAULT_SHOW_ITEM) {
                    view.setScaleX(1 - (position - 1) * 0.1f);
                    view.setScaleY(1 - (position - 1) * 0.1f);
//                    Log.i("nnnnnn", widthSpace + "___" + heightSpace + "_____" + (position - 1) * view.getMeasuredHeight() / DEFAULT_TRANSLATE_Y);

                    view.setTranslationY((position - 1) * view.getMeasuredHeight() / DEFAULT_TRANSLATE_Y);
                } else if (position > 0) {
                    view.setScaleX(1 - position * 0.1f);
                    view.setScaleY(1 - position * 0.1f);
//                    Log.i("nnnnnn", "___" + position * view.getMeasuredHeight() / DEFAULT_TRANSLATE_Y);
                    view.setTranslationY(position * view.getMeasuredHeight() / DEFAULT_TRANSLATE_Y);
                } else {
                    view.setOnTouchListener(mOnTouchListener);
                }
            }
        } else {
            for (int position = itemCount - 1; position >= 0; position--) {
                View view = recycler.getViewForPosition(position);
                addView(view);
                measureChildWithMargins(view, 0, 0);
                int widthSpace = getWidth() - getDecoratedMeasuredWidth(view);
                int heightSpace = getHeight() - getDecoratedMeasuredHeight(view);

                layoutDecoratedWithMargins(view, widthSpace / 2, heightSpace / 2, getWidth() - widthSpace / 2, getHeight() - heightSpace / 2);
                if (position > 0) {
                    view.setScaleX(1 - position * 0.1f);
                    view.setScaleY(1 - position * 0.1f);
//                    Log.i("nnnnnn", "___" + position * view.getMeasuredHeight() / DEFAULT_TRANSLATE_Y);
                    view.setTranslationY(position * view.getMeasuredHeight() / DEFAULT_TRANSLATE_Y);
                } else {
                    view.setOnTouchListener(mOnTouchListener);
                }
            }
        }
    }

}
