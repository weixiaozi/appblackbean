package bbs.com.xinfeng.bbswork.widget.popwindow;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
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

/**
 * Created by dell on 16/11/22.
 */

public class PrivateChatPoppubWindow implements View.OnClickListener {
    private final LinearLayout llayoutRoot;
    private final TextView txt_pop_first;
    private final TextView txt_pop_second;
    private final TextView txt_pop_third;
    private final TextView txt_pop_fouth;

    private Activity mContext;
    private PopupWindow sharepopupWindow;
    public boolean isDismiss = true;
    private final View mShareview;
    private ValueAnimator mAnimator;
    private ValueAnimator va;
    private boolean hasCollect;

    public PrivateChatPoppubWindow(Activity context) {
        this.mContext = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        mShareview = inflater.inflate(R.layout.private_chat_popupwindow, null);
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

        txt_pop_first = (TextView) mShareview.findViewById(R.id.txt_pop_first);
        txt_pop_second = (TextView) mShareview.findViewById(R.id.txt_pop_second);
        txt_pop_third = (TextView) mShareview.findViewById(R.id.txt_pop_third);
        txt_pop_fouth = (TextView) mShareview.findViewById(R.id.txt_pop_fouth);
        TextView txtCancel = (TextView) mShareview.findViewById(R.id.txt_pop_cancel);
        sharepopupWindow = new PopupWindow(mShareview, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        sharepopupWindow.setOutsideTouchable(true);
        sharepopupWindow.setFocusable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        sharepopupWindow.setBackgroundDrawable(new BitmapDrawable());
//        sharepopupWindow.setAnimationStyle(R.style.pop_ver_anim_gift);
        sharepopupWindow.setOnDismissListener(() -> {
            isDismiss = true;
            /*if (mListener != null) {
                mListener.dismiss();
            }*/
        });
        txt_pop_first.setOnClickListener(this);
        txt_pop_second.setOnClickListener(this);
        txt_pop_third.setOnClickListener(this);
        txt_pop_fouth.setOnClickListener(this);
        txtCancel.setOnClickListener(this);
    }


    public interface DismissListener {
        void onFirst();

        void onSecond();

        void onThird();

        void onFourth();
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

    public void setBtnContent(String fcontent, String scontent, String tcontent, String fourcontent) {
        txt_pop_first.setVisibility(TextUtils.isEmpty(fcontent) ? View.GONE : View.VISIBLE);
        txt_pop_first.setText(fcontent);
        txt_pop_second.setVisibility(TextUtils.isEmpty(scontent) ? View.GONE : View.VISIBLE);
        txt_pop_second.setText(scontent);
        txt_pop_third.setVisibility(TextUtils.isEmpty(tcontent) ? View.GONE : View.VISIBLE);
        txt_pop_third.setText(tcontent);
        txt_pop_fouth.setVisibility(TextUtils.isEmpty(fourcontent) ? View.GONE : View.VISIBLE);
        txt_pop_fouth.setText(fourcontent);
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
            case R.id.txt_pop_first:
                if (mListener != null)
                    mListener.onFirst();
                break;
            case R.id.txt_pop_second:
                if (mListener != null)
                    mListener.onSecond();
                break;
            case R.id.txt_pop_third:
                if (mListener != null)
                    mListener.onThird();
                break;
            case R.id.txt_pop_fouth:
                if (mListener != null)
                    mListener.onFourth();
                break;
            case R.id.txt_pop_cancel:
                break;
        }
        sharepopupWindow.dismiss();
    }

}
