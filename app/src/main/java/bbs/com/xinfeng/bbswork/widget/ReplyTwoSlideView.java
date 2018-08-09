package bbs.com.xinfeng.bbswork.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;

import bbs.com.xinfeng.bbswork.utils.LogUtil;
import bbs.com.xinfeng.bbswork.utils.ScreenUtils;


/**
 * Created by dell on 2018/3/22.
 * 二级回复滑动关闭
 */

public class ReplyTwoSlideView extends LinearLayout {
    private RecyclerView mRecycleview;
    private boolean isTop;
    private float lastY;
    private float downY;
    private int height;
    private Context mContext;
    private LinearLayoutManager layoutManager;
    private boolean isFilled;
    private VelocityTracker mVelocityTracker;
    private int mFling;

    public ReplyTwoSlideView(Context context) {
        super(context);
        init(context);
    }

    public ReplyTwoSlideView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ReplyTwoSlideView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        height = ScreenUtils.getHasVirtualKeyHeight(mContext);
        mFling = ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity() * 15;
    }

    public void attachRecycleview(RecyclerView recyclerView, OnLoadingListening mListen) {
        this.mListen = mListen;
        mRecycleview = recyclerView;
        this.post(new Runnable() {
            @Override
            public void run() {
                layoutManager = (LinearLayoutManager) mRecycleview.getLayoutManager();
                mRecycleview.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        if (!isFilled) {
                            View view = layoutManager.findViewByPosition(layoutManager.findLastVisibleItemPosition());
                            if (view != null)
                                isFilled = view.getBottom() >= recyclerView.getBottom();
                        }
                        if (isFilled) {
                            if (!recyclerView.canScrollVertically(-1)) {
                                isTop = true;
                            } else {
                                isTop = false;
                            }
                        } else {
                            isTop = true;
                        }
                    }
                });
            }
        });
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = ev.getRawY();
                lastY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (isTop && (ev.getY() - lastY) > 0) {

                    /*if (adapterLoading != null)
                        adapterLoading.addLoading();
                    else {
                        new RuntimeException("need set adapter");
                    }*/
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        addVelocityTrackerEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float moveY = event.getRawY();
                LogUtil.i("slideview", moveY + "__" + (moveY - downY));
                if (moveY - downY >= 0 && moveY - downY <= height)
                    setTranslationY(moveY - downY);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mListen != null) {
                    if ((event.getRawY() - downY) <= height / 4) {
//                        LogUtil.i("cccccccc", getTouchVelocityY() + "___" + mFling);
                        if (getTouchVelocityY() > mFling) {
                            mListen.onSlide(event.getRawY() - downY, false);
                        } else {
                            mListen.onSlide(event.getRawY() - downY, true);
                        }
                    } else {
                        mListen.onSlide(event.getRawY() - downY, false);
                    }
                }
                downY = 0;
                lastY = 0;
                break;

        }
        return true;
    }

    private int dip2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public interface OnLoadingListening {

        void onSlide(float currentY, boolean isUp);
    }

    private OnLoadingListening mListen;

    public void setOnLoadingLisening(OnLoadingListening mListen) {
        this.mListen = mListen;
    }

    private void addVelocityTrackerEvent(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }

        mVelocityTracker.addMovement(event);
    }

    // 获得纵向的手速
    private int getTouchVelocityY() {
        if (mVelocityTracker == null)
            return 0;
        mVelocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) mVelocityTracker.getYVelocity();
        return velocity;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mVelocityTracker != null) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }
}
