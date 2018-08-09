package bbs.com.xinfeng.bbswork.widget.popwindow;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.utils.shareUtil.ShareData;

/**
 * Created by xiaodong on 16/11/22.
 */

public class SharePoppubWindow implements View.OnClickListener {
    private final LinearLayout llayoutRoot;
    private Activity mContext;
    private PopupWindow sharepopupWindow;
    public boolean isDismiss = true;
    private final View mShareview;
    private ValueAnimator mAnimator;
    private ValueAnimator va;

    public SharePoppubWindow(Activity context) {
        this.mContext = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        mShareview = inflater.inflate(R.layout.share_popupwindow, null);
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mShareview.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);
        } else {
            mShareview.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }
        FrameLayout flayout = (FrameLayout) mShareview.findViewById(R.id.flayout);
        flayout.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                dismiss();
            }
            return false;
        });
        llayoutRoot = (LinearLayout) mShareview.findViewById(R.id.llayout_root);
        LinearLayout llayoutQQ = (LinearLayout) mShareview.findViewById(R.id.llayout_qq);
        LinearLayout llayout_qq_space = (LinearLayout) mShareview.findViewById(R.id.llayout_qq_space);
        LinearLayout llayout_sina_weibo = (LinearLayout) mShareview.findViewById(R.id.llayout_sina_weibo);
        LinearLayout llayout_wechat = (LinearLayout) mShareview.findViewById(R.id.llayout_wechat);
        LinearLayout llayout_weixin = (LinearLayout) mShareview.findViewById(R.id.llayout_weixin);
        LinearLayout llayout_app = (LinearLayout) mShareview.findViewById(R.id.llayout_app);
        sharepopupWindow = new PopupWindow(mShareview, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        sharepopupWindow.setOutsideTouchable(true);
        sharepopupWindow.setFocusable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        sharepopupWindow.setBackgroundDrawable(new BitmapDrawable());
//        sharepopupWindow.setAnimationStyle(R.style.pop_ver_anim_gift);
        sharepopupWindow.setOnDismissListener(() -> {
            isDismiss = true;
            if (mListener != null) {
                mListener.dismiss();
            }
        });
        llayoutQQ.setOnClickListener(this);
        llayout_qq_space.setOnClickListener(this);
        llayout_sina_weibo.setOnClickListener(this);
        llayout_wechat.setOnClickListener(this);
        llayout_weixin.setOnClickListener(this);
        llayout_app.setOnClickListener(this);
    }


    public interface DismissListener {
        void dismiss();

        void shareChannel(int channelId);
    }

    private DismissListener mListener;

    public void setDismissListener(DismissListener listener) {
        mListener = listener;
    }

    public boolean isShowing() {
        if (sharepopupWindow == null) {
            return false;
        } else {
            return sharepopupWindow.isShowing();
        }
    }


    public void dismiss() {
        if (sharepopupWindow != null) {
            closeAnim();
        }
    }

    public void show() {
        //背景置暗
        showAnim();
        sharepopupWindow.showAtLocation(llayoutRoot, Gravity.BOTTOM, 0, 0);
    }

    private void showAnim() {
        if (mAnimator == null) {
            mAnimator = ValueAnimator.ofInt(mContext.getResources().getDisplayMetrics().heightPixels, 0);
            mAnimator.addUpdateListener(animation -> {
                int value = (int) animation.getAnimatedValue();
                llayoutRoot.setTranslationY(value);
            });
            mAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);

                }
            });
        }
        mAnimator.setDuration(300).start();
    }

    private void closeAnim() {
        if (va == null) {
            va = ValueAnimator.ofInt(0, mContext.getResources().getDisplayMetrics().heightPixels);
            va.addUpdateListener(animation -> {
                int value = (int) animation.getAnimatedValue();
                llayoutRoot.setTranslationY(value);
            });
            va.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    sharepopupWindow.dismiss();
                }
            });
        }
        va.setDuration(300).start();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //qq好友
            case R.id.llayout_qq:
                if (mListener != null)
                    mListener.shareChannel(ShareData.QQ);
                break;
            //qq空间
            case R.id.llayout_qq_space:
                if (mListener != null)
                    mListener.shareChannel(ShareData.QQ_ZONE);
                break;
            //新浪
            case R.id.llayout_sina_weibo:
                if (mListener != null)
                    mListener.shareChannel(ShareData.SINA);

                break;
            //微信好友
            case R.id.llayout_wechat:
                if (mListener != null)
                    mListener.shareChannel(ShareData.WX);
                break;
            //微信朋友圈
            case R.id.llayout_weixin:
                if (mListener != null)
                    mListener.shareChannel(ShareData.WX_CIRCLE);
                break;
            //微信朋友圈
            case R.id.llayout_app:
                if (mListener != null)
                    mListener.shareChannel(ShareData.APPLICATION);
                break;
        }
        sharepopupWindow.dismiss();
    }


}
