package bbs.com.xinfeng.bbswork.utils;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.lang.ref.WeakReference;

import bbs.com.xinfeng.bbswork.base.App;
import bbs.com.xinfeng.bbswork.module.GlideApp;
import bbs.com.xinfeng.bbswork.module.GlideRequest;
import bbs.com.xinfeng.bbswork.module.GlideRequests;

/**
 * Created by dell on 2017/10/26.
 */

public class ImageLoader {
    private GlideRequests glideRequests;
    private String url;
    private int loadDrawable;
    private int errorDrawable;
    private Transformation mTransformation;
    private WeakReference<ImageView> mReference;
    private DiskCacheStrategy mCacheStrategy = DiskCacheStrategy.NONE;
    private int mWidth, mHeight;

    public ImageLoader() {
        glideRequests = GlideApp.with(App.mApp);
    }

    public ImageLoader setUrl(String url) {
        this.url = url;
        return this;
    }

    public ImageLoader setLoadDrawable(int drawableId) {
        this.loadDrawable = drawableId;
        return this;
    }

    public ImageLoader setErrorDrawable(int drawableId) {
        this.errorDrawable = drawableId;
        return this;
    }

    public ImageLoader setmTransformation(Transformation mTransformation) {
        this.mTransformation = mTransformation;
        return this;
    }

    public ImageLoader setmCacheStrategy(DiskCacheStrategy mCacheStrategy) {
        this.mCacheStrategy = mCacheStrategy;
        return this;
    }

    public ImageLoader override(int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
        return this;
    }

    public ImageLoader into(ImageView imageView) {
        this.mReference = new WeakReference<ImageView>(imageView);
        return this;
    }


    public void build() {
        GlideRequest<Drawable> load = glideRequests.load(url);
        load.diskCacheStrategy(mCacheStrategy);
        if (mWidth != 0 || mHeight != 0)
            load.override(mWidth, mHeight);
        if (errorDrawable != 0) {
            load.fallback(errorDrawable);
            load.error(errorDrawable);
        }
        if (loadDrawable != 0)
            load.placeholder(loadDrawable);
        if (mTransformation != null)
            load.transforms(mTransformation);
        load.transition(DrawableTransitionOptions.withCrossFade());
        if (mReference != null && mReference.get() != null)
            load.into(mReference.get());
        if (mOnLoadDrawableListener != null) {
            load.into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                    mOnLoadDrawableListener.onLoadDrawable(resource);
                }
            });
        }
    }

    public interface OnLoadDrawableListener {
        void onLoadDrawable(Drawable drawable);
    }

    private OnLoadDrawableListener mOnLoadDrawableListener;

    public ImageLoader into(OnLoadDrawableListener onLoadDrawableListener) {
        this.mOnLoadDrawableListener = onLoadDrawableListener;
        return this;
    }

}
