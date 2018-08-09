package bbs.com.xinfeng.bbswork.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.TimeUnit;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.base.App;
import bbs.com.xinfeng.bbswork.base.BaseActivity;
import bbs.com.xinfeng.bbswork.base.Constant;
import bbs.com.xinfeng.bbswork.databinding.ActivityLoginBinding;
import bbs.com.xinfeng.bbswork.domin.ErrorBean;
import bbs.com.xinfeng.bbswork.domin.GetCodeBean;
import bbs.com.xinfeng.bbswork.domin.LoginBus;
import bbs.com.xinfeng.bbswork.domin.NoticeChangeBean;
import bbs.com.xinfeng.bbswork.domin.RegisterBean;
import bbs.com.xinfeng.bbswork.domin.TokenBean;
import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.presenter.LoginPresenter;
import bbs.com.xinfeng.bbswork.utils.LogUtil;
import bbs.com.xinfeng.bbswork.utils.RxUtil;
import bbs.com.xinfeng.bbswork.utils.SharedPrefUtil;
import bbs.com.xinfeng.bbswork.utils.ToastUtil;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class LoginActivity extends BaseActivity<ActivityLoginBinding, LoginPresenter> implements NetContract.INetView {
    private final int getcode_TAG = 300;
    private final int register_TAG = 301;
    private String tel;
    private String code;
    private int allTime;
    private RegisterBean registerBean;
    private Disposable disposable;

    @Override
    protected LoginPresenter creatPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected void initEvent() {
        mBinding.editTel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 11) {
                    s.delete(11, s.length());
                }
            }
        });
        mBinding.txtCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tel = mBinding.editTel.getText().toString().trim();
                if (TextUtils.isEmpty(tel)) {
                    showError(new ErrorBean("-20", "请输入正确的手机号码", ErrorBean.TYPE_SHOW), -20);
                } else {
                    mBinding.txtCode.setClickable(false);
                    mPresenter.getCode(tel, getcode_TAG);
                }
            }
        });
        mBinding.buttonLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                tel = mBinding.editTel.getText().toString().trim();
                if (TextUtils.isEmpty(tel)) {
                    showError(new ErrorBean("-20", "请输入手机号获取验证码", ErrorBean.TYPE_SHOW), -20);
                } else if (TextUtils.isEmpty(mBinding.editCode.getText().toString().trim())) {
                    showError(new ErrorBean("-20", "请输入验证码", ErrorBean.TYPE_SHOW), -20);
                } else {
                    code = mBinding.editCode.getText().toString().trim();
                    mPresenter.register(tel, code, register_TAG);
                }
            }
        });
        mBinding.txtProtoal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(provideActivity(), WebviewActivity.class);
                intent.putExtra("weburl", Constant.BASEURL + "/agreement");
                startActivity(intent);
            }
        });
    }

    @Override
    protected int initView(Bundle savedInstanceState) {
        return R.layout.activity_login;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mBinding.basebar.barTxtTitle.setText(R.string.bar_register);
        if (!TextUtils.isEmpty(SharedPrefUtil.getString(Constant.phone_key, ""))) {
            mBinding.editTel.setText(SharedPrefUtil.getString(Constant.phone_key, ""));
            mBinding.editTel.setSelection(mBinding.editTel.getText().toString().length());
        }
    }

    @Override
    public void showLoading(int tag) {

    }

    @Override
    public void hideLoading(int tag) {

    }

    @Override
    public void showError(ErrorBean errorBean, int tag) {
        if (errorBean.androidType.equals(ErrorBean.TYPE_SHOW)) {
            mBinding.txtError.setText(errorBean.desc);
        }
        if (tag == getcode_TAG)
            mBinding.txtCode.setClickable(true);
    }

    @Override
    public void setData(ErrorBean errorBean, int tag) {
        switch (tag) {
            case getcode_TAG:
                GetCodeBean bean = (GetCodeBean) errorBean;
                if (App.isDebug) {
                    if (bean != null && bean.getCode() != 0)
                        mBinding.editCode.setText(bean.getCode() + "");
                } else {
                    if (bean != null && bean.getCode() != 0)
                        mBinding.editCode.setText(bean.getCode() + "");
                }
                startTimer(60);
                break;
            case register_TAG:
                registerBean = (RegisterBean) errorBean;
                SharedPrefUtil.putBoolean(Constant.profile_key, registerBean.getUser().isProfile());
                SharedPrefUtil.putInt(Constant.userid_key, registerBean.getUser().getId());
                SharedPrefUtil.putString(Constant.name_key, registerBean.getUser().getName());
                SharedPrefUtil.putString(Constant.head_key, registerBean.getUser().getPortrait());
                SharedPrefUtil.putString(Constant.phone_key, registerBean.getUser().getPhone());
                App.mApp.getToken(tel, code);
                break;
        }
    }

    private void startTimer(int time) {
        allTime = time;
        disposable = Flowable.interval(0, 1, TimeUnit.SECONDS).compose(RxUtil.fixScheduler()).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                mBinding.txtCode.setTextColor(provideActivity().getResources().getColor(R.color.line_color));
                mBinding.txtCode.setText(allTime + "s");
                if (allTime == 0) {
                    mBinding.txtCode.setTextColor(provideActivity().getResources().getColor(R.color.register_code));
                    mBinding.txtCode.setText(R.string.login_getcode);
                    mBinding.txtCode.setClickable(true);
                    disposable.dispose();
                }
                allTime--;
            }
        });
        mDisposables.add(disposable);
    }

    @Override
    public void progress(int precent, int tag) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(LoginBus bus) {
        if (bus.isLoginSuccess) {
            if (registerBean.getUser().isProfile()) {
                Intent intent = new Intent(provideActivity(), ModifyActivity.class);
                intent.putExtra("from", "welcome");
                startActivity(intent);
            } else {
                if ("welcome".equals(getIntent().getStringExtra("from"))) {
                    startActivity(new Intent(provideActivity(), MainActivity.class));
                }
            }

            finish();
        }
    }

    @Override
    protected boolean isBindEventBus() {
        return true;
    }
}
