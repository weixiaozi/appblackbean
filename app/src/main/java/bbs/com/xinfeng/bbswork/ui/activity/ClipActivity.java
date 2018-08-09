package bbs.com.xinfeng.bbswork.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.base.BaseActivity;
import bbs.com.xinfeng.bbswork.databinding.ActivityClipBinding;
import bbs.com.xinfeng.bbswork.domin.ErrorBean;
import bbs.com.xinfeng.bbswork.mvp.presenter.NetPresenter;
import bbs.com.xinfeng.bbswork.utils.ArmsUtils;

public class ClipActivity extends BaseActivity<ActivityClipBinding, NetPresenter> {

    public static final int CUT_REQUEST = 111;
    public static final String CLIP = "clip";
    public static final String PATH = "path";
    public String clipName = "clip.png";

    @Override
    public void showLoading(int tag) {

    }

    @Override
    public void hideLoading(int tag) {

    }

    @Override
    public void showError(ErrorBean errorBean, int tag) {

    }

    @Override
    protected NetPresenter creatPresenter() {
        return null;
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected int initView(Bundle savedInstanceState) {
        return R.layout.activity_clip;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        new RxPermissions(this).request(Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(aBoolean -> {

        });
        String path = getIntent().getStringExtra(PATH);
        final Bitmap bitmap = BitmapFactory.decodeFile(path);
        mBinding.zcivClip.setIsCanResize(false);

        mBinding.zcivClip.setSize(ArmsUtils.dip2px(provideActivity(), 150));
        mBinding.zcivClip.setImageBitmap(bitmap);
        mBinding.btnOk.setOnClickListener(v -> {
            File file = new File(getExternalCacheDir(), clipName);
            try {
                FileOutputStream fos = new FileOutputStream(file);
                Bitmap clipBitmap = mBinding.zcivClip.getClipBitmap();
                clipBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent();
            intent.putExtra(CLIP, file.getAbsolutePath());
            setResult(RESULT_OK, intent);
            finish();
        });
    }
}
