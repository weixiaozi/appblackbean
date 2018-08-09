package bbs.com.xinfeng.bbswork.base;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.utils.InputLeakUtil;

/**
 * Created by dell on 2017/10/24.
 */

public abstract class BaseFragment<T extends ViewDataBinding, P extends IBasePresenter> extends Fragment implements IBaseView {
    protected final String TAG = this.getClass().getSimpleName();

    protected P mPresenter;
    protected T mBinding;

    private View loadRoot;
    private ImageView loadImage;
    private AnimationDrawable animationDrawable;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isBindEventBus())
            EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        int layoutId = initView(savedInstanceState);
        if (layoutId != 0) {
            //绑定到butterknife
            View view = inflater.inflate(layoutId, container, false);
            mBinding = DataBindingUtil.bind(view);
            return view;
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = creatPresenter();
        addLoading();
        initData(savedInstanceState);
        initEvent();
    }

    private void addLoading() {
        ViewGroup decorView = (ViewGroup) mBinding.getRoot();
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

    protected abstract P creatPresenter();

    protected abstract void initEvent();

    protected boolean isBindEventBus() {
        return false;
    }

    @Override
    public Activity provideActivity() {
        return getActivity();
    }

    protected abstract int initView(Bundle savedInstanceState);

    protected abstract void initData(Bundle savedInstanceState);

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.destory();
        this.mPresenter = null;
        if (isBindEventBus())
            EventBus.getDefault().unregister(this);
        if (mBinding != null)
            mBinding.unbind();
        InputLeakUtil.fixInputMethodManager(getActivity());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(Integer i) {

    }
}
