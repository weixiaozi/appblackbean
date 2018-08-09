package bbs.com.xinfeng.bbswork.ui.adapter.decoration;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import bbs.com.xinfeng.bbswork.R;


/**
 * Created by dell on 2017/8/22.
 */

public class ChatPicItemDecoration extends RecyclerView.ItemDecoration {
    private int raw;
    private int topRaw;

    public ChatPicItemDecoration(Context context) {
        raw = context.getResources().getDimensionPixelSize(R.dimen.dp10);
        topRaw = context.getResources().getDimensionPixelSize(R.dimen.dp12);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildLayoutPosition(view);

        if (position != 3 && position != 7) {
            outRect.right = raw;
        }
        outRect.top = topRaw;
        if (position > 3) {
            outRect.bottom = topRaw;
        }

    }
}
