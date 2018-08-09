package bbs.com.xinfeng.bbswork.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by dell on 2018/4/28.
 */

public class UnInterceptRecycleview extends RecyclerView {
    public UnInterceptRecycleview(Context context) {
        super(context);
    }

    public UnInterceptRecycleview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return false;
    }
}
