package bbs.com.xinfeng.bbswork.widget.popwindow;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
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
import bbs.com.xinfeng.bbswork.databinding.PointPopupwindowBinding;

/**
 * Created by dell on 16/11/22.
 */

public class PointPoppubWindow implements View.OnClickListener {

    private final PointPopupwindowBinding binding;
    private Activity mContext;
    private PopupWindow sharepopupWindow;
    public boolean isDismiss = true;
    private ValueAnimator mAnimator;
    private ValueAnimator va;


    public PointPoppubWindow(Activity context) {
        this.mContext = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        View mRootView = inflater.inflate(R.layout.point_popupwindow, null);
        binding = DataBindingUtil.bind(mRootView);
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
            isDismiss = true;
            if (mListener != null) {
                mListener.dismiss();
            }
        });
        binding.txtPointButtonOne.setOnClickListener(this);
        binding.txtButtonLeft.setOnClickListener(this);
        binding.txtButtonRight.setOnClickListener(this);
    }

    public void setPointInfo(String title, String content, boolean isOne, String firstButton, String secondButton) {
        binding.txtPointTitle.setText(title);
        binding.txtPointContent.setVisibility(TextUtils.isEmpty(content) ? View.GONE : View.VISIBLE);
        binding.txtPointContent.setText(content);
        if (isOne) {
            binding.txtPointButtonOne.setVisibility(View.VISIBLE);
            binding.llayoutPointButton.setVisibility(View.GONE);
            binding.txtPointButtonOne.setText(firstButton);
        } else {
            binding.txtPointButtonOne.setVisibility(View.GONE);
            binding.llayoutPointButton.setVisibility(View.VISIBLE);
            binding.txtButtonLeft.setText(firstButton);
            binding.txtButtonRight.setText(secondButton);
        }
    }

    public interface DismissListener {
        void dismiss();

        void clickOne();

        void clickleft();

        void clickRight();
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
//        showAnim();
        sharepopupWindow.showAtLocation(binding.getRoot(), Gravity.BOTTOM, 0, 0);
    }

    private void showAnim() {
        if (mAnimator == null) {
            mAnimator = ValueAnimator.ofInt(mContext.getResources().getDisplayMetrics().heightPixels, 0);
            mAnimator.addUpdateListener(animation -> {
                int value = (int) animation.getAnimatedValue();
                binding.llayoutRoot.setTranslationY(value);
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
                binding.llayoutRoot.setTranslationY(value);
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
            case R.id.txt_point_button_one:
                if (mListener != null)
                    mListener.clickOne();
                break;
            case R.id.txt_button_left:
                if (mListener != null)
                    mListener.clickleft();
                break;
            case R.id.txt_button_right:
                if (mListener != null)
                    mListener.clickRight();
                break;
        }
        sharepopupWindow.dismiss();
    }

}
