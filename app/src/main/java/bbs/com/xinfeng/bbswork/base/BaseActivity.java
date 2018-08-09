package bbs.com.xinfeng.bbswork.base;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.zip.Inflater;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.utils.InputLeakUtil;
import bbs.com.xinfeng.bbswork.utils.ScreenUtils;
import bbs.com.xinfeng.bbswork.widget.popwindow.LoadingPoppubWindow;
import io.reactivex.disposables.CompositeDisposable;


/**
 * Created by dell on 2017/10/17.
 */

public abstract class BaseActivity<D extends ViewDataBinding, T extends IBasePresenter> extends AppCompatActivity implements IBaseView {
    protected final String TAG = this.getClass().getSimpleName();

    protected T mPresenter;

    protected D mBinding;
    protected CompositeDisposable mDisposables = new CompositeDisposable();

    private View loadRoot;
    private ImageView loadImage;
    private AnimationDrawable animationDrawable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            int layoutResID = initView(savedInstanceState);
            if (layoutResID != 0) {//如果initView返回0,框架则不会调用setContentView(),当然也不会 Bind ButterKnife
                mBinding = DataBindingUtil.setContentView(this, layoutResID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!isSelfimmerseUI())
            immerseUI(getResources().getColor(R.color.line_low_color));
        mPresenter = creatPresenter();
        if (isBindEventBus())
            EventBus.getDefault().register(this);
        addLoading();
        initData(savedInstanceState);
        initEvent();
    }

    private void addLoading() {
        ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
        View mRootView = LayoutInflater.from(provideActivity()).inflate(R.layout.loading_popupwindow, null);
        loadRoot = mRootView.findViewById(R.id.flayout_loading_root);
        loadImage = (ImageView) mRootView.findViewById(R.id.iv_loading);
        decorView.addView(mRootView);
    }

    protected void startLoading() {
        loadRoot.setVisibility(View.VISIBLE);
        if (animationDrawable == null)
            animationDrawable = (AnimationDrawable) loadImage.getDrawable();
        animationDrawable.start();
    }

    protected void stopLoading() {
        loadRoot.setVisibility(View.GONE);
        if (animationDrawable != null)
            animationDrawable.stop();
    }

    protected abstract T creatPresenter();

    protected boolean isBindEventBus() {
        return false;
    }

    protected boolean isSelfimmerseUI() {
        return false;
    }

    protected boolean adapterFitSystemWindows() {
        return false;
    }


    protected abstract int initView(Bundle savedInstanceState);

    protected abstract void initData(Bundle savedInstanceState);

    protected abstract void initEvent();

    @Override
    public Activity provideActivity() {
        return this;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(Integer i) {

    }

    public void immerseUI(int color) {
//        ViewGroup rootView = getWindow().getDecorView().findViewById(android.R.id.content);
//        rootView.setPadding(0, ScreenUtils.getStatusBarHeight(this), 0, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0 以上直接设置状态栏颜色
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(color);
        } else {
            if (!adapterFitSystemWindows()) {
                ViewGroup rootView = getWindow().getDecorView().findViewById(android.R.id.content);
                rootView.setPadding(0, ScreenUtils.getStatusBarHeight(this), 0, 0);
                //根布局添加占位状态栏
                ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
                View statusBarView = new View(this);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ScreenUtils.getStatusBarHeight(this));
                statusBarView.setBackgroundColor(color);
                decorView.addView(statusBarView, lp);
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposables.dispose();
        if (mPresenter != null)
            mPresenter.destory();
        this.mPresenter = null;
        if (isBindEventBus())
            EventBus.getDefault().unregister(this);
        if (mBinding != null)
            mBinding.unbind();
        InputLeakUtil.fixInputMethodManager(this);
    }
}
