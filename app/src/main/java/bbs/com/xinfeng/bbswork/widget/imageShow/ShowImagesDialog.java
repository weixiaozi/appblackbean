package bbs.com.xinfeng.bbswork.widget.imageShow;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.module.GlideApp;
import me.iwf.photopicker.adapter.PhotoPagerAdapter;
//import uk.co.senab.photoview.PhotoView;
//import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Administrator on 2017/5/3.
 * 嵌套了viewpager的图片浏览
 */

public class ShowImagesDialog extends Dialog {

    private View mView;
    private Context mContext;
    private ShowImagesViewPager mViewPager;
    private TextView mIndexText;
    private List<String> mImgUrls = new ArrayList<>();
    private List<String> mImgThumUrls = new ArrayList<>();
    private PhotoPagerAdapter mAdapter;
    private int currentPosition;

    public ShowImagesDialog(@NonNull Context context) {
        super(context, R.style.transparentBgDialog);
        this.mContext = context;
        initView();
        initData();
    }

    private void initView() {
        mView = View.inflate(mContext, R.layout.dialog_images_brower, null);
        mViewPager = (ShowImagesViewPager) mView.findViewById(R.id.vp_images);
        mIndexText = (TextView) mView.findViewById(R.id.tv_image_index);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mView);
        Window window = getWindow();
        /*WindowManager.LayoutParams wl = window.getAttributes();

        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        wl.x = 0;
        wl.y = 0;
        wl.height = metrics.heightPixels;
        wl.width = metrics.widthPixels;
        wl.gravity = Gravity.CENTER;
        window.setAttributes(wl);*/

        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.FILL_PARENT;
        lp.height = WindowManager.LayoutParams.FILL_PARENT;
        window.setAttributes(lp);
    }

    public void setPicList(List<String> imgUrls, List<String> imgThumUrls, int position) {
        mImgUrls.clear();
        mImgUrls.addAll(imgUrls);
        mImgThumUrls.clear();
        mImgThumUrls.addAll(imgThumUrls);
        currentPosition = position;
        notifyPic();
    }

    private void notifyPic() {
        mIndexText.setText(currentPosition + 1 + "/" + mImgUrls.size());
        mAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(currentPosition, false);
    }

    private void initData() {
        mAdapter = new PhotoPagerAdapter(mContext,GlideApp.with(mContext), mImgUrls, mImgThumUrls);
        mViewPager.setAdapter(mAdapter);
        mAdapter.setmListen(new PhotoPagerAdapter.onPicClickListen() {
            @Override
            public void closePic() {
                dismiss();
                return;
            }
        });
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mIndexText.setText(position + 1 + "/" + mImgUrls.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void setHasSave(boolean hasSave) {
        mAdapter.setHasSave(hasSave);
    }
}
