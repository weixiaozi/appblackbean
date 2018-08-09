package bbs.com.xinfeng.bbswork.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.base.App;
import bbs.com.xinfeng.bbswork.base.BaseActivity;
import bbs.com.xinfeng.bbswork.base.Constant;
import bbs.com.xinfeng.bbswork.databinding.ActivitySettingBinding;
import bbs.com.xinfeng.bbswork.domin.ErrorBean;
import bbs.com.xinfeng.bbswork.domin.GetPushSettingBean;
import bbs.com.xinfeng.bbswork.domin.LoginBus;
import bbs.com.xinfeng.bbswork.domin.Progress;
import bbs.com.xinfeng.bbswork.domin.UpgradeBean;
import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.presenter.SettingPresenter;
import bbs.com.xinfeng.bbswork.utils.SharedPrefUtil;
import bbs.com.xinfeng.bbswork.utils.SystemUtil;
import bbs.com.xinfeng.bbswork.utils.ToastUtil;
import bbs.com.xinfeng.bbswork.utils.UpgradeUtil;
import bbs.com.xinfeng.bbswork.widget.CheckSwitchButton;

import static bbs.com.xinfeng.bbswork.base.Constant.BASEURL;
import static bbs.com.xinfeng.bbswork.base.Constant.BASEURL_OFFICE;
import static bbs.com.xinfeng.bbswork.base.Constant.BASEURL_TEST;
import static bbs.com.xinfeng.bbswork.utils.UpgradeUtil.where_setting;

public class SettingActivity extends BaseActivity<ActivitySettingBinding, SettingPresenter> implements NetContract.INetView {
    private static final int pushsetting_Tag = 300;
    private static final int setnewspush_Tag = 301;
    private static final int setportspush_Tag = 302;
    private static final int outLogin_Tag = 303;
    private static final int upgradeCheck_tag = 304;
    private UpgradeUtil upgradeUtil;

    @Override
    protected SettingPresenter creatPresenter() {
        return new SettingPresenter(this);
    }

    @Override
    protected int initView(Bundle savedInstanceState) {
        return R.layout.activity_setting;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mBinding.basebar.barLeftClick.setVisibility(View.VISIBLE);
        mBinding.basebar.barLeftClick.setOnClickListener(v -> finish());
        mBinding.basebar.barTxtTitle.setText(R.string.mine_setting);
        mBinding.txtSettingPhone.setText(getIntent().getStringExtra("phone"));
        mBinding.txtSettingVersion.setText("(" + SystemUtil.getVersionName(provideActivity()) + ")");

        mPresenter.getPushSetting(pushsetting_Tag);
        mPresenter.upgradeCheck(upgradeCheck_tag);
    }

    @Override
    protected void initEvent() {
        mBinding.llayoutSettingFeedback.setOnClickListener(v -> {
            startActivity(new Intent(provideActivity(), FeedbackActivity.class));
        });
        mBinding.txtSetttingOutlogin.setOnClickListener(v -> {
            mPresenter.outLogin(outLogin_Tag);
        });
        mBinding.llayoutSettingClear.setOnClickListener(v -> {
            App.getInstance().clearCache();
            ToastUtil.showToast("清除成功");
        });

        if (App.isDebug) {
            mBinding.txtTestRoot.setVisibility(View.VISIBLE);
            mBinding.txtTestRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBinding.llayoutTest.setVisibility(View.VISIBLE);
                    mBinding.edtTestReal.setText(BASEURL);
                }
            });
            mBinding.txtTestFormal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBinding.edtTestReal.setText(BASEURL_OFFICE);
                }
            });
            mBinding.txtTestNoFormal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBinding.edtTestReal.setText(BASEURL_TEST);
                }
            });
            mBinding.btnTestCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBinding.llayoutTest.setVisibility(View.GONE);
                }
            });
            mBinding.btnTestSure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(mBinding.edtTestReal.getText().toString().trim())) {
                        SharedPrefUtil.putString(Constant.baseurl_key, mBinding.edtTestReal.getText().toString().trim());
                        mPresenter.outLogin(outLogin_Tag);
                        ToastUtil.showToast("请杀死应用后重启");
                    }
                    mBinding.llayoutTest.setVisibility(View.GONE);
                }
            });
        }

    }

    private void setPushSetting(int type, int tag) {
        mPresenter.setPushSetting(type, tag);
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
            ToastUtil.showToast(errorBean.desc);
        }
        if (tag == setnewspush_Tag) {
            mBinding.switchSettingNewmes.setOnCheckedChangeListener(null);
            mBinding.switchSettingNewmes.setChecked(!mBinding.switchSettingNewmes.isChecked());
            mBinding.switchSettingNewmes.setOnCheckedChangeListener(new CheckSwitchButton.OnCheckedChangeListener() {
                @Override
                public void isOpen(CheckSwitchButton checkSwitchButton, boolean mChecked) {
                    setPushSetting(1, setnewspush_Tag);
                }
            });
        }
        if (tag == setportspush_Tag) {
            mBinding.switchSettingNewtheme.setOnCheckedChangeListener(null);
            mBinding.switchSettingNewtheme.setChecked(!mBinding.switchSettingNewtheme.isChecked());
            mBinding.switchSettingNewtheme.setOnCheckedChangeListener(new CheckSwitchButton.OnCheckedChangeListener() {
                @Override
                public void isOpen(CheckSwitchButton checkSwitchButton, boolean mChecked) {
                    setPushSetting(2, setportspush_Tag);
                }
            });
        }

    }

    @Override
    public void setData(ErrorBean errorBean, int tag) {
        switch (tag) {
            case pushsetting_Tag:
                GetPushSettingBean bean = (GetPushSettingBean) errorBean;
                mBinding.switchSettingNewmes.setChecked(bean.getMessage() == 1);
                mBinding.switchSettingNewtheme.setChecked(bean.getPost() == 1);

                mBinding.switchSettingNewmes.setOnCheckedChangeListener(new CheckSwitchButton.OnCheckedChangeListener() {
                    @Override
                    public void isOpen(CheckSwitchButton checkSwitchButton, boolean mChecked) {
                        setPushSetting(1, setnewspush_Tag);
                    }
                });
                mBinding.switchSettingNewtheme.setOnCheckedChangeListener(new CheckSwitchButton.OnCheckedChangeListener() {
                    @Override
                    public void isOpen(CheckSwitchButton checkSwitchButton, boolean mChecked) {
                        setPushSetting(2, setportspush_Tag);
                    }
                });
                break;
            case outLogin_Tag:
                App.getInstance().outLogin();
                finish();
                EventBus.getDefault().post(new LoginBus(false));
                break;
            case upgradeCheck_tag:
                UpgradeBean upgradeBean = (UpgradeBean) errorBean;
                if (Integer.parseInt(upgradeBean.getVersion()) > SystemUtil.getAppVersion()) {
                    mBinding.ivSettingUpdate.setVisibility(View.VISIBLE);
                    mBinding.llayoutSettingUpdate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (upgradeUtil == null)
                                upgradeUtil = new UpgradeUtil(provideActivity());
                            upgradeUtil.checkUpgrade(upgradeBean, where_setting);
                        }
                    });
                }

                break;
        }
    }

    @Override
    public void progress(int precent, int tag) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(Progress p) {
        if (upgradeUtil != null && p.where == where_setting)
            upgradeUtil.updataProgress(p);
    }

    @Override
    protected boolean isBindEventBus() {
        return true;
    }
}
