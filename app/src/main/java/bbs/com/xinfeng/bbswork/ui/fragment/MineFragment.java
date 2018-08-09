package bbs.com.xinfeng.bbswork.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;


import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.base.App;
import bbs.com.xinfeng.bbswork.base.BaseFragment;
import bbs.com.xinfeng.bbswork.databinding.FragmentMineBinding;
import bbs.com.xinfeng.bbswork.domin.ErrorBean;
import bbs.com.xinfeng.bbswork.domin.ModifyInfoBus;
import bbs.com.xinfeng.bbswork.domin.NoticeChangeBean;
import bbs.com.xinfeng.bbswork.domin.UserContentBean;
import bbs.com.xinfeng.bbswork.domin.UserInfoBean;
import bbs.com.xinfeng.bbswork.module.GlideApp;
import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.presenter.MinePresenter;
import bbs.com.xinfeng.bbswork.ui.activity.ModifyActivity;
import bbs.com.xinfeng.bbswork.ui.activity.MyBlacksActivity;
import bbs.com.xinfeng.bbswork.ui.activity.MyFansActivity;
import bbs.com.xinfeng.bbswork.ui.activity.MyReplyActivity;
import bbs.com.xinfeng.bbswork.ui.activity.MyThemeActivity;
import bbs.com.xinfeng.bbswork.ui.activity.SettingActivity;
import bbs.com.xinfeng.bbswork.ui.activity.UserInfoActivity;
import bbs.com.xinfeng.bbswork.utils.LogUtil;
import bbs.com.xinfeng.bbswork.utils.ToastUtil;

import static bbs.com.xinfeng.bbswork.ui.activity.MyFansActivity.ACTION_FANS;
import static bbs.com.xinfeng.bbswork.ui.activity.MyFansActivity.ACTION_FOLLOW;

/**
 * Created by dell on 2017/10/24.
 */

public class MineFragment extends BaseFragment<FragmentMineBinding, MinePresenter> implements NetContract.INetView {
    private static final int userinfo_TAG = 109;
    private static final int usercontent_TAG = 110;
    private UserInfoBean userInfoBean;

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
            case userinfo_TAG:
                userInfoBean = (UserInfoBean) errorBean;
                GlideApp.with(provideActivity()).load(userInfoBean.getPortrait_thumb()).override(140).placeholder(R.drawable.icon_head_default).error(R.drawable.icon_head_default).into(mBinding.ivMineHead);
                mBinding.txtMineName.setText(userInfoBean.getName());
                mBinding.txtMineIntroduce.setText(TextUtils.isEmpty(userInfoBean.getIntroduce()) ? "一句话介绍下自己吧" : userInfoBean.getIntroduce());
                break;
            case usercontent_TAG:
                UserContentBean bean = (UserContentBean) errorBean;
                notifyNums(bean.getData());
                break;

        }
    }

    private void notifyNums(UserContentBean.DataBean bean) {
        mBinding.txtMineThemeNum.setVisibility(bean.getThreads() == 0 ? View.GONE : View.VISIBLE);
        mBinding.txtMineReplyNum.setVisibility(bean.getReplies() == 0 ? View.GONE : View.VISIBLE);
        mBinding.txtMineFollowNum.setVisibility(bean.getFollows() == 0 ? View.GONE : View.VISIBLE);
        mBinding.txtMineFansNum.setVisibility(bean.getFans() == 0 ? View.GONE : View.VISIBLE);
        mBinding.txtMineBlacksNum.setVisibility(bean.getBlocks() == 0 ? View.GONE : View.VISIBLE);

        mBinding.txtMineThemeNum.setText(bean.getThreads() + "");
        mBinding.txtMineReplyNum.setText(bean.getReplies() + "");
        mBinding.txtMineFollowNum.setText(bean.getFollows() + "");
        mBinding.txtMineFansNum.setText(bean.getFans() + "");
        mBinding.txtMineBlacksNum.setText(bean.getBlocks() + "");
    }

    @Override
    public void progress(int precent, int tag) {

    }

    @Override
    protected MinePresenter creatPresenter() {
        return new MinePresenter(this);
    }

    @Override
    protected void initEvent() {
        mBinding.llayoutMineSelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(provideActivity(), ModifyActivity.class);
                intent.putExtra("frommine", true);
                startActivity(intent);
            }
        });
        mBinding.llayoutMineTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(provideActivity(), MyThemeActivity.class));
            }
        });
        mBinding.llayoutMineReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(provideActivity(), MyReplyActivity.class));
            }
        });
        mBinding.llayoutMineFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(provideActivity(), MyFansActivity.class);
                intent.putExtra("fansaction", ACTION_FOLLOW);
                startActivity(intent);
            }
        });
        mBinding.llayoutMineFans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(provideActivity(), MyFansActivity.class);
                intent.putExtra("fansaction", ACTION_FANS);
                startActivity(intent);
            }
        });
        mBinding.llayoutMineBlacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(provideActivity(), MyBlacksActivity.class);
                startActivity(intent);
            }
        });
        mBinding.llayoutSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userInfoBean != null) {
                    Intent intent = new Intent(provideActivity(), SettingActivity.class);
                    intent.putExtra("phone", userInfoBean.getPhone());
                    startActivity(intent);

                }
            }
        });
    }

    @Override
    protected int initView(Bundle savedInstanceState) {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mBinding.basebar.barTxtTitle.setText(R.string.mine_title);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (userInfoBean == null)
            mPresenter.getUserInfo(userinfo_TAG);
        mPresenter.getUserContent(usercontent_TAG);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ModifyInfoBus e) {
        mPresenter.getUserInfo(userinfo_TAG);
    }

    @Override
    protected boolean isBindEventBus() {
        return true;
    }
}
