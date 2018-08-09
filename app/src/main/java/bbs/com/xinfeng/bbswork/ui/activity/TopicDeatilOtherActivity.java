package bbs.com.xinfeng.bbswork.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.base.App;
import bbs.com.xinfeng.bbswork.base.BaseActivity;
import bbs.com.xinfeng.bbswork.databinding.ActivityCreatTopicBinding;
import bbs.com.xinfeng.bbswork.databinding.ActivityTopicDeatilOtherBinding;
import bbs.com.xinfeng.bbswork.domin.ErrorBean;
import bbs.com.xinfeng.bbswork.domin.TopicDetailBean;
import bbs.com.xinfeng.bbswork.domin.TopicOperateBus;
import bbs.com.xinfeng.bbswork.module.GlideApp;
import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.presenter.TopicDetailPresenter;
import bbs.com.xinfeng.bbswork.mvp.presenter.TopicPresenter;
import bbs.com.xinfeng.bbswork.ui.adapter.TopicMermbersHeadAdapter;
import bbs.com.xinfeng.bbswork.utils.ToastUtil;

public class TopicDeatilOtherActivity extends BaseActivity<ActivityTopicDeatilOtherBinding, TopicDetailPresenter> implements NetContract.INetView {
    private static final int topicdetail_Tag = 410;
    private TopicDetailBean bean;

    @Override
    protected TopicDetailPresenter creatPresenter() {
        return new TopicDetailPresenter(this);
    }

    @Override
    protected void initEvent() {
        mBinding.txtMemberLength.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bean != null) {
                    Intent intent = new Intent(provideActivity(), MembersDisplayActivity.class);
                    intent.putExtra("topicid", bean.getTopic().getId());
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected int initView(Bundle savedInstanceState) {
        return R.layout.activity_topic_deatil_other;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mBinding.basebar.barLeftClick.setVisibility(View.VISIBLE);
        mBinding.basebar.barTxtTitle.setText(R.string.topic_title_detail);
        mBinding.basebar.barLeftClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getTopicDetail(getIntent().getStringExtra("topicid"), topicdetail_Tag);
    }

    @Override
    public void showLoading(int tag) {
        if (tag == topicdetail_Tag && bean == null)
            startLoading();
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
        switch (tag) {
            case topicdetail_Tag:
                if (errorBean instanceof TopicDetailBean) {
                    bean = (TopicDetailBean) errorBean;
                    fillData(bean);
                }
                break;
        }
    }

    private void fillData(TopicDetailBean bean) {
        mBinding.llayoutRoot.setVisibility(View.VISIBLE);
        GlideApp.with(App.mApp).load(bean.getTopic().getImg_url_thumb()).override(160).placeholder(R.drawable.icon_topic_default).error(R.drawable.icon_topic_default).into(mBinding.ivTDetailPage);
        mBinding.txtTDetailName.setText(bean.getTopic().getName());
        GlideApp.with(App.mApp).load(bean.getTopic().getCreate_user_portrait_thumb()).override(48).placeholder(R.drawable.icon_head_default).error(R.drawable.icon_head_default).into(mBinding.ivHead);
        mBinding.txtTDetailAuthorname.setText(bean.getTopic().getCreate_user_name());
        mBinding.txtTDetailProperty.setText(bean.getTopic().getTime_label() + "   " + bean.getTopic().getThread_number() + " 观点");
        if (bean.getFans() != null && bean.getFans().size() > 0) {
            mBinding.recycleMemberHead.setLayoutManager(new LinearLayoutManager(provideActivity(), LinearLayoutManager.HORIZONTAL, false));
            TopicMermbersHeadAdapter headAdapter = new TopicMermbersHeadAdapter(provideActivity(), bean.getFans());
            mBinding.recycleMemberHead.setAdapter(headAdapter);
            headAdapter.setOnItemClickListen(new TopicMermbersHeadAdapter.onItemClickListen() {
                @Override
                public void onclick(int position) {
                    Intent intent = new Intent(provideActivity(), UserInfoActivity.class);
                    intent.putExtra("userid", bean.getFans().get(position).getUser_id());
                    startActivity(intent);
                }
            });
        }
        mBinding.txtMemberLength.setText(bean.getTopic().getFans_number() + " 成员");
        mBinding.txtTDetailIntroduce.setText(bean.getTopic().getIntroduce());
        if (bean.getStatus() == 0) {
            mBinding.buttonSend.setClickable(true);
            mBinding.buttonSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(provideActivity(), TopicOperateActivity.class);
                    intent.putExtra("topicid", bean.getTopic().getId() + "");
                    intent.putExtra("topicpic", bean.getTopic().getImg_url_thumb() + "");
                    intent.putExtra("topicname", bean.getTopic().getName() + "");
                    startActivity(intent);
                }
            });
        } else if (bean.getStatus() == 1) {
            mBinding.buttonSend.setText("已加入");
            mBinding.buttonSend.setClickable(false);
        } else if (bean.getStatus() == 2) {
            mBinding.buttonSend.setText("审核中");
            mBinding.buttonSend.setClickable(false);
        }
    }

    @Override
    public void progress(int precent, int tag) {

    }

    @Override
    protected void onDestroy() {
        if (bean != null)
            EventBus.getDefault().post(new TopicOperateBus(bean.getTopic().getId(), bean.getStatus()));
        super.onDestroy();
    }
}
