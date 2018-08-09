package bbs.com.xinfeng.bbswork.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.RadioGroup;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.base.BaseActivity;
import bbs.com.xinfeng.bbswork.databinding.ActivityFeedbackBinding;
import bbs.com.xinfeng.bbswork.databinding.ActivitySettingBinding;
import bbs.com.xinfeng.bbswork.domin.ErrorBean;
import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.presenter.SettingPresenter;
import bbs.com.xinfeng.bbswork.utils.ToastUtil;

public class FeedbackActivity extends BaseActivity<ActivityFeedbackBinding, SettingPresenter> implements NetContract.INetView {
    private static final int feedback_TAG = 500;
    private int selectpostion = 1;

    @Override
    protected SettingPresenter creatPresenter() {
        return new SettingPresenter(this);
    }

    @Override
    protected int initView(Bundle savedInstanceState) {
        return R.layout.activity_feedback;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mBinding.basebar.barTxtTitle.setText(R.string.setting_feedback);
        mBinding.basebar.barLeftClick.setVisibility(View.VISIBLE);
        mBinding.basebar.barLeftClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initEvent() {
        mBinding.rbtnFeedback1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectpostion = 1;
                updataButtonCheck(selectpostion);
            }
        });
        mBinding.rbtnFeedback2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectpostion = 2;
                updataButtonCheck(selectpostion);
            }
        });
        mBinding.rbtnFeedback3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectpostion = 3;
                updataButtonCheck(selectpostion);
            }
        });
        mBinding.rbtnFeedback4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectpostion = 4;
                updataButtonCheck(selectpostion);
            }
        });

        mBinding.editFeedbackContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() <= 300) {
                    mBinding.txtTopicintroduceLength.setText(s.length() + "/300");
                } else {
                    s.delete(300, s.length());
                }
            }
        });

        mBinding.buttonFeedbackSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mBinding.editFeedbackContent.getText().toString().trim())) {
                    mPresenter.pushFeedback(selectpostion, mBinding.editFeedbackContent.getText().toString().trim(), feedback_TAG);
                } else {
                    ToastUtil.showToast("请输入反馈内容");
                }
            }
        });
    }

    private void updataButtonCheck(int postion) {
        mBinding.rbtnFeedback1.setChecked(false);
        mBinding.rbtnFeedback2.setChecked(false);
        mBinding.rbtnFeedback3.setChecked(false);
        mBinding.rbtnFeedback4.setChecked(false);
        if (postion == 1) {
            mBinding.rbtnFeedback1.setChecked(true);
        } else if (postion == 2) {
            mBinding.rbtnFeedback2.setChecked(true);
        } else if (postion == 3) {
            mBinding.rbtnFeedback3.setChecked(true);
        } else if (postion == 4) {
            mBinding.rbtnFeedback4.setChecked(true);
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
            ToastUtil.showToast(errorBean.desc);
        }
    }

    @Override
    public void setData(ErrorBean errorBean, int tag) {
        if (tag == feedback_TAG) {
            ToastUtil.showToast("感谢您的宝贵意见，我们会更加努力！");
            finish();
        }
    }

    @Override
    public void progress(int precent, int tag) {

    }
}
