package bbs.com.xinfeng.bbswork.widget.popwindow;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.databinding.PointPopupwindowBinding;

/**
 * Created by dell on 16/11/22.
 */

public class LoadingPoppubWindow {

    private final View mRootView;
    private final ImageView loadImage;
    private Activity mContext;
    private PopupWindow sharepopupWindow;
    private AnimationDrawable animationDrawable;


    public LoadingPoppubWindow(Activity context) {
        this.mContext = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        mRootView = inflater.inflate(R.layout.loading_popupwindow, null);
        loadImage = (ImageView) mRootView.findViewById(R.id.iv_loading);

        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mRootView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);
        } else {
            mRootView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }
        sharepopupWindow = new PopupWindow(mRootView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        sharepopupWindow.setOutsideTouchable(true);
        sharepopupWindow.setFocusable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        sharepopupWindow.setBackgroundDrawable(new BitmapDrawable());
//        sharepopupWindow.setAnimationStyle(R.style.pop_ver_anim_gift);
        sharepopupWindow.setOnDismissListener(() -> {
            if (animationDrawable != null)
                animationDrawable.stop();
            if (mListener != null) {
                mListener.dismiss();
            }
        });
    }


    public interface DismissListener {
        void dismiss();
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
            sharepopupWindow.dismiss();
        }
    }

    public void show(View view) {
        showAnim();
        sharepopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    private void showAnim() {
        if (animationDrawable == null)
            animationDrawable = (AnimationDrawable) loadImage.getDrawable();
        animationDrawable.start();
    }

    private void closeAnim() {

    }

}
