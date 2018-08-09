package bbs.com.xinfeng.bbswork.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.base.BaseActivity;
import bbs.com.xinfeng.bbswork.databinding.ActivityMineInfoBinding;
import bbs.com.xinfeng.bbswork.databinding.ActivityModifyBinding;
import bbs.com.xinfeng.bbswork.domin.ErrorBean;
import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.presenter.ModifyInfoPresenter;
import bbs.com.xinfeng.bbswork.utils.ToastUtil;

public class MineInfoActivity extends BaseActivity<ActivityMineInfoBinding, ModifyInfoPresenter> implements NetContract.INetView {

    @Override
    protected ModifyInfoPresenter creatPresenter() {
        return new ModifyInfoPresenter(this);
    }

    @Override
    protected int initView(Bundle savedInstanceState) {
        return R.layout.activity_mine_info;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mBinding.basebar.barLeftClick.setVisibility(View.VISIBLE);
        mBinding.basebar.barTxtTitle.setText(R.string.mine_info);
        mBinding.basebar.barLeftClick.setOnClickListener(v -> finish());
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void showLoading(int tag) {
    }

    @Override
    public void hideLoading(int tag) {
        stopLoading();
    }

    @Override
    public void showError(ErrorBean errorBean, int tag) {
        if (errorBean.androidType.equals(ErrorBean.TYPE_SHOW)) {
            ToastUtil.showToast(errorBean.desc);
        }
    }

    @Override
    public void setData(ErrorBean errorBean, int tag) {

    }

    @Override
    public void progress(int precent, int tag) {

    }
}
