package bbs.com.xinfeng.bbswork.ui.adapter.decoration;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import bbs.com.xinfeng.bbswork.R;

/**
 * Created by dell on 2017/8/22.
 */

public class ItemLineDecoration extends RecyclerView.ItemDecoration {
    private int raw;

    public ItemLineDecoration(Context context, int bottom) {
        raw = context.getResources().getDimensionPixelSize(bottom);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildLayoutPosition(view);
        if (position == parent.getAdapter().getItemCount() - 1) {
            outRect.bottom = raw;
        } else {
            outRect.bottom = 0;
        }


    }
}
