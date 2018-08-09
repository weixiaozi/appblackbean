package bbs.com.xinfeng.bbswork.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.base.BaseActivity;
import bbs.com.xinfeng.bbswork.databinding.ActivityCreatTopicBinding;
import bbs.com.xinfeng.bbswork.databinding.ActivityTopicOperateBinding;
import bbs.com.xinfeng.bbswork.domin.ErrorBean;
import bbs.com.xinfeng.bbswork.module.GlideApp;
import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.presenter.TopicOperatePresenter;
import bbs.com.xinfeng.bbswork.mvp.presenter.TopicPresenter;
import bbs.com.xinfeng.bbswork.utils.ToastUtil;

public class TopicOperateActivity extends BaseActivity<ActivityTopicOperateBinding, TopicOperatePresenter> implements NetContract.INetView {
    private final static int topicapply_tag = 200;

    @Override
    protected TopicOperatePresenter creatPresenter() {
        return new TopicOperatePresenter(this);
    }

    @Override
    protected void initEvent() {
        mBinding.buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.applyInTopic(getIntent().getStringExtra("topicid"), mBinding.editOperateInfo.getText().toString().trim(), topicapply_tag);
            }
        });
    }

    @Override
    protected int initView(Bundle savedInstanceState) {
        return R.layout.activity_topic_operate;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mBinding.basebar.barLeftClick.setVisibility(View.VISIBLE);
        mBinding.basebar.barTxtTitle.setText(R.string.topic_title_apply);
        mBinding.basebar.barLeftClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        GlideApp.with(provideActivity()).load(getIntent().getStringExtra("topicpic")).override(72).placeholder(R.drawable.icon_topic_default).error(R.drawable.icon_topic_default).into(mBinding.ivTopicPage);
        mBinding.txtTopicName.setText(getIntent().getStringExtra("topicname"));
        mBinding.editOperateInfo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() <= 20) {
                    mBinding.txtOperateLenght.setText(s.length() + "/20");
                } else {
                    s.delete(20, s.length());
                }
            }
        });
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
    }

    @Override
    public void setData(ErrorBean errorBean, int tag) {
        switch (tag) {
            case topicapply_tag:
                ToastUtil.showToast("申请成功");
                finish();
                break;
        }
    }

    @Override
    public void progress(int precent, int tag) {

    }
}
