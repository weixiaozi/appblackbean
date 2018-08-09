package bbs.com.xinfeng.bbswork.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import bbs.com.xinfeng.bbswork.utils.LogUtil;


/**
 * Created by dell on 2018/3/22.
 */

public class ChattingRootView extends LinearLayout {
    private RecyclerView mRecycleview;
    private boolean isTop;
    private float lastY;
    private int loadingHeight;
    private Context mContext;
    private LinearLayoutManager layoutManager;
    private boolean isLoading;
    private boolean secondTouch;//预防加载中时滑动
    private int sumSlide;
    private NotifyAdapterLoading adapterLoading;
    private boolean isFilled;

    public ChattingRootView(Context context) {
        super(context);
        init(context);
    }

    public ChattingRootView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ChattingRootView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        loadingHeight = dip2px(50);
        this.post(new Runnable() {
            @Override
            public void run() {
                mRecycleview = (RecyclerView) getChildAt(0);
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
                        }
                    }
                });
            }
        });

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (mListen != null)
                mListen.hideViews();
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isLoading) {
            secondTouch = true;
            return true;
        } else {
            secondTouch = false;
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = ev.getY();
                sumSlide = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                if (isTop && (ev.getY() - lastY) > 0) {
//                    isAddRecycleviewLoading(true);
                    if (adapterLoading != null)
                        adapterLoading.addLoading();
                    else {
                        new RuntimeException("need set adapter");
                    }
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isLoading) {
            return true;
        }
        if (secondTouch) {
            return true;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float moveY = event.getY();
                if (sumSlide < -loadingHeight) {
                    sumSlide = -loadingHeight;
                } else if ((sumSlide + lastY - moveY) > 0) {
                    mRecycleview.scrollBy(0, -sumSlide);
                    sumSlide = 0;
                } else {
                    mRecycleview.scrollBy(0, (int) (lastY - moveY));
                    sumSlide += (int) (lastY - moveY);
                }

                lastY = moveY;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (sumSlide < 0) {
                    layoutManager.scrollToPositionWithOffset(0, 0);
                    if (mListen != null) {
                        isLoading = true;
                        mListen.startLoading();
                    }
                } else {
                    isLoading = false;
                    if (adapterLoading != null)
                        adapterLoading.removeLoading();
                    else {
                        new RuntimeException("need set adapter");
                    }
                    layoutManager.scrollToPosition(0);
                    isTop = true;
                }
                sumSlide = 0;
                break;

        }
        return true;
    }

    private int dip2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public interface OnLoadingListening {
        void startLoading();

        void hideViews();
    }

    private OnLoadingListening mListen;

    public void setOnLoadingLisening(OnLoadingListening mListen) {
        this.mListen = mListen;
    }

    public interface NotifyAdapterLoading {
        void addLoading();

        void removeLoading();
    }

    public void setAdatperLoading(NotifyAdapterLoading adatperLoading) {
        this.adapterLoading = adatperLoading;
    }

    public void loadFinish() {
        isLoading = false;
    }

    public void loadStart() {
        isLoading = true;
        adapterLoading.addLoading();
    }

    public void reset() {
        if (mRecycleview != null) {
            if (!mRecycleview.canScrollVertically(1))
                isTop = false;
        }
    }
}
