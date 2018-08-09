package me.iwf.photopicker.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.R;
import me.iwf.photopicker.utils.AndroidLifecycleUtils;
import me.iwf.photopicker.utils.ScreenUtils;

/**
 * Created by donglua on 15/6/21.
 */
public class PhotoPagerAdapter extends PagerAdapter {
    private boolean hasSave;
    private List<String> paths = new ArrayList<>();
    private List<String> thumPaths;
    private RequestManager mGlide;
    private RotateAnimation ra = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    private int width;
    private int height;

    public void setHasSave(boolean hasSave) {
        this.hasSave = hasSave;
    }

    private Context mContext;

    public PhotoPagerAdapter(Context mContext, RequestManager glide, List<String> paths, List<String> tPaths) {
        this.mContext = mContext;
        width = ScreenUtils.getScreenWidth(mContext);
        height = ScreenUtils.getScreenHeight(mContext);

        this.paths = paths;
        if (tPaths != null) {
            this.thumPaths = tPaths;
        }
        this.mGlide = glide;
        ra.setInterpolator(new LinearInterpolator());
        ra.setFillAfter(true);
        ra.setRepeatCount(Integer.MAX_VALUE);
        ra.setDuration((long) (1000));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final Context context = container.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.__picker_picker_item_pager, container, false);

        final SubsamplingScaleImageView imageView = (SubsamplingScaleImageView) itemView.findViewById(R.id.iv_pager);
        final ImageView imageThumView = (ImageView) itemView.findViewById(R.id.iv_thum_pager);
        final ImageView imageLoading = (ImageView) itemView.findViewById(R.id.iv_loading);
        final LinearLayout llayout_save = (LinearLayout) itemView.findViewById(R.id.llayout_save);
        final TextView txt_pop_save = (TextView) itemView.findViewById(R.id.txt_pop_save);
        final TextView txt_pop_cancel = (TextView) itemView.findViewById(R.id.txt_pop_cancel);
//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        final String path = paths.get(position);
        String thumPath = null;
        if (thumPaths != null && thumPaths.size() > 0) {
            thumPath = thumPaths.get(position);
        }


        boolean canLoadImage = AndroidLifecycleUtils.canLoadImage(context);

        if (canLoadImage) {
            imageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CUSTOM);
            final RequestOptions options = new RequestOptions();
            options.dontAnimate()
                    .dontTransform();
            final Uri uri;
            Uri thumUri = null;
            if (path.startsWith("http")) {
                uri = Uri.parse(path);
                if (!TextUtils.isEmpty(thumPath))
                    thumUri = Uri.parse(thumPath);

                if (thumUri != null) {
                    imageThumView.setVisibility(View.VISIBLE);
                    imageLoading.setVisibility(View.VISIBLE);
                    imageLoading.startAnimation(ra);

                    mGlide.setDefaultRequestOptions(options).load(thumUri)
                            .into(imageThumView);

                }

                mGlide.setDefaultRequestOptions(options).download(uri).listener(new RequestListener<File>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
                        imageLoading.clearAnimation();
                        imageLoading.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {

                        return false;
                    }
                }).into(new SimpleTarget<File>() {
                    @Override
                    public void onResourceReady(File resource, Transition<? super File> transition) {
                        float initImageScale = getInitImageScale(resource.getAbsolutePath());
                        imageView.setImage(ImageSource.uri(resource.getAbsolutePath()),
                                new ImageViewState(initImageScale, new PointF(0, 0), 0));
                        imageView.setMinScale(initImageScale * 0.6f);
                        imageView.setDoubleTapZoomScale(initImageScale);
                        imageView.setMaxScale(initImageScale * 1.4f);//最大显示比例
                        imageView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                imageLoading.clearAnimation();
                                imageLoading.setVisibility(View.GONE);
                                imageThumView.setVisibility(View.GONE);
                            }
                        }, 140);
                        imageView.setVisibility(View.VISIBLE);
                    }
                });

            } else {
                imageView.setVisibility(View.VISIBLE);
                File file = new File(path);
                uri = Uri.fromFile(file);
                float initImageScale = getInitImageScale(file.getAbsolutePath());
                imageView.setImage(ImageSource.uri(path),
                        new ImageViewState(initImageScale, new PointF(0, 0), 0));
                imageView.setMinScale(initImageScale * 0.6f);
                imageView.setDoubleTapZoomScale(initImageScale);
                imageView.setMaxScale(initImageScale * 1.4f);//最大显示比例
            }

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (context instanceof Activity) {
                        if (mListen != null) {
                            mListen.closePic();
                        } else {
                            if (!((Activity) context).isFinishing()) {
                                ((Activity) context).onBackPressed();
                            }
                        }
                    }
                }
            });
            imageThumView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (context instanceof Activity) {
                        if (mListen != null) {
                            mListen.closePic();
                        } else {
                            if (!((Activity) context).isFinishing()) {
                                ((Activity) context).onBackPressed();
                            }
                        }
                    }
                }
            });
            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (hasSave) {
                        llayout_save.setVisibility(View.VISIBLE);
                    }
                    return false;
                }
            });
            llayout_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    llayout_save.setVisibility(View.GONE);
                }
            });
            txt_pop_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    llayout_save.setVisibility(View.GONE);
                }
            });
            txt_pop_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mGlide.load(uri).listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            Toast.makeText(context, "保存失败", Toast.LENGTH_SHORT).show();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                            .into(new SimpleTarget<Drawable>() {
                                @Override
                                public void onResourceReady(final Drawable resource, Transition<? super Drawable> transition) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            saveBitmap(drawableToBitmap(resource), System.currentTimeMillis() / 1000 + "", Bitmap.CompressFormat.JPEG);
                                        }
                                    }).start();
                                    Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();
                                }
                            });
                    llayout_save.setVisibility(View.GONE);
                }

            });
        }


        container.addView(itemView);

        return itemView;
    }

    /**
     * 计算出图片初次显示需要放大倍数
     *
     * @param imagePath 图片的绝对路径
     */
    public float getInitImageScale(String imagePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);
        // 拿到图片的宽和高
        int dw = options.outWidth;
        int dh = options.outHeight;
        options.inJustDecodeBounds = false;
        float scale = 1.0f;
        //图片宽度大于屏幕，但高度小于屏幕，则缩小图片至填满屏幕宽
        if (dw > width && dh <= height) {
            scale = width * 1.0f / dw;
        }
        //图片宽度小于屏幕，但高度大于屏幕，则放大图片至填满屏幕宽
        if (dw <= width && dh > height) {
            scale = width * 1.0f / dw;
        }
        //图片高度和宽度都小于屏幕，则放大图片至填满屏幕宽
        if (dw < width && dh < height) {
            scale = width * 1.0f / dw;
        }
        //图片高度和宽度都大于屏幕，则缩小图片至填满屏幕宽
        if (dw > width && dh > height) {
            scale = width * 1.0f / dw;
        }
        return scale;
    }

    /**
     * 将Drawable转化为Bitmap
     *
     * @param drawable
     * @return
     */
    public Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable == null)
            return null;
        return ((BitmapDrawable) drawable).getBitmap();
    }

    /**
     * 将Bitmap以指定格式保存到指定路径
     *
     * @param bitmap
     * @param
     */
    public void saveBitmap(Bitmap bitmap, String name, Bitmap.CompressFormat format) {
        // 创建一个位于SD卡上的文件
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/blackbean/blackbean");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, name + ".jpg");
        try {
            // 打开指定文件输出流
            FileOutputStream out = new FileOutputStream(file);
            // 将位图输出到指定文件
            bitmap.compress(format, 100,
                    out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 其次把文件插入到系统图库
       /* try {
            MediaStore.Images.Media.insertImage(mContext.getContentResolver(), file.getAbsolutePath(), name + ".jpg", null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/
        // 最后通知图库更新
//        mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + Environment.getExternalStorageDirectory().getAbsolutePath() + "/blackbean/blackbean")));
        mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
    }

    @Override
    public int getCount() {
        return paths.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mGlide.clear((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void destory() {
        ra.cancel();
    }

    public interface onPicClickListen {
        void closePic();

    }

    private onPicClickListen mListen;

    public void setmListen(onPicClickListen mListen) {
        this.mListen = mListen;
    }
}
