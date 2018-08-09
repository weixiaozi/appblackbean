package bbs.com.xinfeng.bbswork.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.ArrayList;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.base.BaseActivity;
import bbs.com.xinfeng.bbswork.databinding.ActivityCreatTopicBinding;
import bbs.com.xinfeng.bbswork.databinding.ActivityLoginBinding;
import bbs.com.xinfeng.bbswork.domin.ErrorBean;
import bbs.com.xinfeng.bbswork.domin.TopicCreatBean;
import bbs.com.xinfeng.bbswork.domin.UploadBean;
import bbs.com.xinfeng.bbswork.module.GlideApp;
import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.presenter.TopicPresenter;
import bbs.com.xinfeng.bbswork.utils.ImageUtil;
import bbs.com.xinfeng.bbswork.utils.ToastUtil;
import me.iwf.photopicker.PhotoPicker;

import static bbs.com.xinfeng.bbswork.ui.activity.ClipActivity.CUT_REQUEST;

public class CreatTopicActivity extends BaseActivity<ActivityCreatTopicBinding, TopicPresenter> implements NetContract.INetView {
    private static final int UPLOADPIC_TAG = 500;
    private static final int CREATTOPIC_TAG = 501;
    private static final int change_TAG = 502;
    private String pagePic;
    private int topoicid;
    private String topoicname;
    private String topoicIntroduce;
    RotateAnimation ra = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);


    @Override
    public void showLoading(int tag) {
    }

    @Override
    public void hideLoading(int tag) {
    }

    @Override
    public void showError(ErrorBean errorBean, int tag) {
        if (errorBean.androidType.equals(ErrorBean.TYPE_SHOW))
            ToastUtil.showToast(errorBean.desc);
        if (tag == UPLOADPIC_TAG) {
            mBinding.ivLoading.clearAnimation();
            mBinding.ivLoading.setVisibility(View.GONE);
            if (TextUtils.isEmpty(pagePic)) {
                mBinding.ivPage.setVisibility(View.VISIBLE);
                mBinding.ivPage.setImageResource(R.drawable.icon_topic_error);
            }
            ToastUtil.showToast("上传失败，请重新上传");
        }
    }

    @Override
    public void setData(ErrorBean errorBean, int tag) {
        switch (tag) {
            case UPLOADPIC_TAG:
                if (errorBean instanceof UploadBean) {
                    UploadBean bean = (UploadBean) errorBean;
                    pagePic = bean.getSrc();
                    GlideApp.with(provideActivity()).load(bean.getSrc()).error(R.drawable.icon_topic_page).placeholder(R.drawable.icon_topic_page).listener(new RequestListener<Drawable>() {
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
                            mBinding.ivPage.setVisibility(View.VISIBLE);
                            mBinding.ivPage.setImageDrawable(resource);
                        }
                    });
                }
                break;
            case CREATTOPIC_TAG:
                if (errorBean instanceof TopicCreatBean) {
                    TopicCreatBean bean = (TopicCreatBean) errorBean;
                    Intent intent = new Intent(provideActivity(), TopicDeatilSelfActivity.class);
                    intent.putExtra("topicid", bean.getTopic_id() + "");
                    startActivity(intent);
                    finish();
                }
                break;
            case change_TAG:
                finish();
                break;
        }
    }

    @Override
    public void progress(int precent, int tag) {

    }

    @Override
    protected TopicPresenter creatPresenter() {
        return new TopicPresenter(this);
    }

    @Override
    protected void initEvent() {
        mBinding.ivPage.setOnClickListener(v -> new RxPermissions(provideActivity()).request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(aBoolean -> {
            if (aBoolean) {
                PhotoPicker.builder()
                        .setPhotoCount(1)
                        .setShowCamera(true)
                        .setPreviewEnabled(true)
                        .start(provideActivity(), PhotoPicker.REQUEST_CODE);
            } else {
                ToastUtil.showToast("请开启存储权限");
            }
        }));

        mBinding.buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(pagePic)) {
                    ToastUtil.showToast("请设置封面");
                } else if (mBinding.editTopicName.getText().toString().length() < 2) {
                    ToastUtil.showToast("话题名称太短了~");
                } /*else if (mBinding.editTopicIntroduce.getText().toString().length() < 10) {
                    ToastUtil.showToast("简介太短了~");
                } */ else {
                    if (topoicid == 0)
                        mPresenter.creatTopic(mBinding.editTopicName.getText().toString(), mBinding.editTopicIntroduce.getText().toString(), pagePic, CREATTOPIC_TAG);
                    else
                        mPresenter.changeTopic(topoicid + "", mBinding.editTopicName.getText().toString(), mBinding.editTopicIntroduce.getText().toString(), pagePic, change_TAG);
                }
            }
        });

    }

    @Override
    protected int initView(Bundle savedInstanceState) {
        return R.layout.activity_creat_topic;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        topoicid = getIntent().getIntExtra("topoicid", 0);
        topoicname = getIntent().getStringExtra("topoicname");
        topoicIntroduce = getIntent().getStringExtra("topoicintroduce");
        pagePic = getIntent().getStringExtra("topoicpic");


        ra.setInterpolator(new LinearInterpolator());
        ra.setFillAfter(true);
        ra.setRepeatCount(Integer.MAX_VALUE);
        ra.setDuration((long) (1000));

        mBinding.basebar.barLeftClick.setVisibility(View.VISIBLE);
        if (topoicid == 0) {
            mBinding.basebar.barTxtTitle.setText(R.string.topic_title_creat);
            mBinding.buttonSend.setText("创建");
        } else {
            mBinding.basebar.barTxtTitle.setText(R.string.topic_title_change);
            GlideApp.with(provideActivity()).load(pagePic).error(R.drawable.icon_topic_page).placeholder(R.drawable.icon_topic_page).into(mBinding.ivPage);

            mBinding.buttonSend.setText("修改");
            mBinding.editTopicName.setText(topoicname);
            mBinding.editTopicName.setSelection(topoicname.length());
            mBinding.txtTopicnameLength.setText(topoicname.length() + "/12");

            mBinding.editTopicIntroduce.setText(topoicIntroduce);
            mBinding.editTopicIntroduce.setSelection(topoicIntroduce.length());
            mBinding.txtTopicintroduceLength.setText(topoicIntroduce.length() + "/100");
//            mBinding.editTopicName.setFocusable(false);
//            mBinding.editTopicName.setClickable(false);
        }
        mBinding.basebar.barLeftClick.setOnClickListener(v -> finish());
        mBinding.editTopicName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() <= 12) {
                    mBinding.txtTopicnameLength.setText(s.length() + "/12");
                } else {
                    s.delete(12, s.length());
                }
            }
        });
        mBinding.editTopicIntroduce.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() <= 100) {
                    mBinding.txtTopicintroduceLength.setText(s.length() + "/100");
                } else {
                    s.delete(100, s.length());
                }
            }
        });
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
                if (TextUtils.isEmpty(pagePic))
                    mBinding.ivPage.setVisibility(View.INVISIBLE);
                mBinding.ivLoading.setVisibility(View.VISIBLE);
                mBinding.ivLoading.startAnimation(ra);
                if (isFinish)
                    mPresenter.uploadPic(newFile, UPLOADPIC_TAG);
                else
                    mPresenter.uploadPic(new File(clipPath), UPLOADPIC_TAG);

            }
        }
    }

    @Override
    protected void onDestroy() {
        ra.cancel();
        super.onDestroy();
    }
}
