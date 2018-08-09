package bbs.com.xinfeng.bbswork.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;


import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.base.App;
import bbs.com.xinfeng.bbswork.base.BaseActivity;
import bbs.com.xinfeng.bbswork.base.Constant;
import bbs.com.xinfeng.bbswork.databinding.ActivityModifyBinding;
import bbs.com.xinfeng.bbswork.domin.ErrorBean;
import bbs.com.xinfeng.bbswork.domin.ModifyInfoBus;
import bbs.com.xinfeng.bbswork.domin.UploadBean;
import bbs.com.xinfeng.bbswork.domin.UserInfoBean;
import bbs.com.xinfeng.bbswork.domin.UserInfoBus;
import bbs.com.xinfeng.bbswork.module.GlideApp;
import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.presenter.ModifyInfoPresenter;
import bbs.com.xinfeng.bbswork.utils.ImageUtil;
import bbs.com.xinfeng.bbswork.utils.LogUtil;
import bbs.com.xinfeng.bbswork.utils.SharedPrefUtil;
import bbs.com.xinfeng.bbswork.utils.ToastUtil;
import me.iwf.photopicker.PhotoPicker;

import static bbs.com.xinfeng.bbswork.ui.activity.ClipActivity.CUT_REQUEST;

public class ModifyActivity extends BaseActivity<ActivityModifyBinding, ModifyInfoPresenter> implements NetContract.INetView {

    public static final int userinfo_TAG = 600;
    public static final int modifyuserinfo_TAG = 601;
    private static final int UPLOADPIC_TAG = 602;
    private static final int outLogin_Tag = 603;
    private int userId;
    private String name;
    private String head;
    RotateAnimation ra = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

    @Override
    public void showLoading(int tag) {

    }

    @Override
    public void hideLoading(int tag) {

    }

    @Override
    public void showError(ErrorBean errorBean, int tag) {
        if (errorBean.androidType.equals(ErrorBean.TYPE_SHOW)) {
            ToastUtil.showToast(errorBean.desc);
        }
        if (tag == UPLOADPIC_TAG) {
            mBinding.ivLoading.clearAnimation();
            mBinding.ivLoading.setVisibility(View.GONE);
            if (TextUtils.isEmpty(head)) {
                mBinding.ivHead.setVisibility(View.VISIBLE);
                mBinding.ivHead.setImageResource(R.drawable.icon_head_error);
            }
            ToastUtil.showToast("上传失败，请重新上传");
        }
    }

    @Override
    public void setData(ErrorBean errorBean, int tag) {
        switch (tag) {
            case userinfo_TAG:
                UserInfoBean userInfoBean = (UserInfoBean) errorBean;
                if (!TextUtils.isEmpty(userInfoBean.getName())) {
                    mBinding.editName.setText(userInfoBean.getName());
                    mBinding.editName.setSelection(userInfoBean.getName().length());
                }
                if (!TextUtils.isEmpty(userInfoBean.getPortrait()))
                    GlideApp.with(App.mApp).load(userInfoBean.getPortrait()).placeholder(R.drawable.icon_head_default).error(R.drawable.icon_head_default).into(mBinding.ivHead);
                if ((!TextUtils.isEmpty(userInfoBean.getIntroduce()))) {
                    mBinding.editIntroduce.setText(userInfoBean.getIntroduce());
                    mBinding.editIntroduce.setSelection(userInfoBean.getIntroduce().length());
                }
                userId = userInfoBean.getId();
                name = userInfoBean.getName();
                head = userInfoBean.getPortrait();
                LogUtil.i("csssd", userInfoBean.getPhone());
                break;
            case UPLOADPIC_TAG:
                if (errorBean instanceof UploadBean) {
                    UploadBean bean = (UploadBean) errorBean;
                    head = bean.getSrc();
                    GlideApp.with(provideActivity()).load(bean.getSrc()).error(R.drawable.icon_head_default).placeholder(R.drawable.icon_head_default).listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            mBinding.ivLoading.clearAnimation();
                            mBinding.ivLoading.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    }).into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                            mBinding.ivLoading.clearAnimation();
                            mBinding.ivLoading.setVisibility(View.GONE);
                            mBinding.ivHead.setVisibility(View.VISIBLE);
                            mBinding.ivHead.setImageDrawable(resource);
                        }
                    });
                }
                break;
            case modifyuserinfo_TAG:
                SharedPrefUtil.putBoolean(Constant.profile_key, false);
                SharedPrefUtil.putInt(Constant.userid_key, userId);
                SharedPrefUtil.putString(Constant.name_key, name);
                SharedPrefUtil.putString(Constant.head_key, head);
                if ("welcome".equals(getIntent().getStringExtra("from"))) {
                    startActivity(new Intent(provideActivity(), MainActivity.class));
                } else {
                    EventBus.getDefault().post(new ModifyInfoBus());
                }
                finish();
                break;
            case outLogin_Tag:
                Intent intent = new Intent(provideActivity(), LoginActivity.class);
                if ("welcome".equals(getIntent().getStringExtra("from"))) {
                    intent.putExtra("from", "welcome");
                }
                App.mApp.outLogin();
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public void progress(int precent, int tag) {

    }

    @Override
    protected ModifyInfoPresenter creatPresenter() {
        return new ModifyInfoPresenter(this);
    }

    @Override
    protected void initEvent() {
        mBinding.editName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    hideKeyboard();
                    return true;
                }
                return false;
            }
        });
        mBinding.editName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 10) {
                    s.delete(10, s.length());
                } else {
                    mBinding.txtNameLength.setText(s.length() + "/10");
                }
            }
        });
        mBinding.editIntroduce.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 15) {
                    s.delete(15, s.length());
                } else {
                    mBinding.txtIntroduceLength.setText(s.length() + "/15");
                }
            }
        });

        mBinding.basebar.barLeftClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getIntent().getBooleanExtra("frommine", false)) {
                    finish();
                } else
                    mPresenter.outLogin(outLogin_Tag);

            }
        });
        mBinding.buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = mBinding.editName.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    ToastUtil.showToast("请填写昵称");
                } else if (TextUtils.isEmpty(head)) {
                    ToastUtil.showToast("请上传头像");
                } else {
                    mPresenter.modifyInfo(name, head, mBinding.editIntroduce.getText().toString(), modifyuserinfo_TAG);
                }

            }
        });
        mBinding.ivHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RxPermissions(provideActivity()).request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(aBoolean -> {
                    if (aBoolean) {
                        PhotoPicker.builder()
                                .setPhotoCount(1)
                                .setShowCamera(true)
                                .setPreviewEnabled(true)
                                .start(provideActivity(), PhotoPicker.REQUEST_CODE);
                    } else {
                        ToastUtil.showToast("请开启存储权限");
                    }
                });
            }
        });
    }

    @Override
    protected int initView(Bundle savedInstanceState) {
        return R.layout.activity_modify;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mBinding.basebar.barLeftClick.setVisibility(View.VISIBLE);
        mBinding.basebar.barTxtTitle.setText(R.string.bar_modify_info);
        ra.setInterpolator(new LinearInterpolator());
        ra.setFillAfter(true);
        ra.setRepeatCount(Integer.MAX_VALUE);
        ra.setDuration((long) (1000));
        mPresenter.getUserInfo(userinfo_TAG);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(UserInfoBus bus) {
        if (!bus.isProfile) {
            if ("welcome".equals(getIntent().getStringExtra("from"))) {
                startActivity(new Intent(provideActivity(), MainActivity.class));
            }
            finish();
        }
    }

    @Override
    protected boolean isBindEventBus() {
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PhotoPicker.REQUEST_CODE && data != null) {
                ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                Intent intent = new Intent(provideActivity(), ClipActivity.class);
                intent.putExtra(ClipActivity.PATH, photos.get(0));
                startActivityForResult(intent, CUT_REQUEST);
            } else if (requestCode == CUT_REQUEST) {
                String clipPath = data.getStringExtra(ClipActivity.CLIP);
                File newFile = new File(getExternalCacheDir(), "report.jpg");
                boolean isFinish = ImageUtil.compressBmpToFile(this, clipPath, newFile);
                if (TextUtils.isEmpty(head))
                    mBinding.ivHead.setVisibility(View.INVISIBLE);
                mBinding.ivLoading.setVisibility(View.VISIBLE);
                mBinding.ivLoading.startAnimation(ra);
                if (isFinish)
                    mPresenter.uploadPic(newFile, UPLOADPIC_TAG);
                else
                    mPresenter.uploadPic(new File(clipPath), UPLOADPIC_TAG);
            }
        }
    }

    /**
     * 隐藏软键盘
     */
    protected void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    protected void onDestroy() {
        ra.cancel();
        super.onDestroy();
    }
}
