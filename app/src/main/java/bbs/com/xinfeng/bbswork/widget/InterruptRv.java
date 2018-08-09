package bbs.com.xinfeng.bbswork.widget;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by 苏杭 on 2016/11/24 18:15.
 */

public class InterruptRv extends RecyclerView {
    public InterruptRv(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private Activity mActivity;

    public void setActivity(Activity activity) {
        mActivity = activity;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            if (mOnRecyclerViewTouchDownListen != null) {
                mOnRecyclerViewTouchDownListen.onTouchDown();
            }
        }
        return super.dispatchTouchEvent(e);
    }

    public interface OnRecyclerViewTouchDownListen {
        void onTouchDown();
    }

    OnRecyclerViewTouchDownListen mOnRecyclerViewTouchDownListen = null;

    public void setOnRecyclerViewTouchDownListen(OnRecyclerViewTouchDownListen e) {
        mOnRecyclerViewTouchDownListen = e;
    }
}
