package bbs.com.xinfeng.bbswork.utils.shareUtil;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
/*import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;*/
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
/*import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;*/

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;


import bbs.com.xinfeng.bbswork.base.App;
import bbs.com.xinfeng.bbswork.module.GlideApp;
import bbs.com.xinfeng.bbswork.utils.ToastUtil;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

//import static com.tencent.connect.share.QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT;

/**
 * Created by dell on 2018/1/10.
 */

public class ShareUtil /*implements IUiListener*/ {
    public static final String QQAPP_ID = "1106596169";
    public static final String WEIXIN_APP_ID = "wxbd6e25264ef423e2";
    public static final String SINA_APPKEY = "3302806124";
    private Activity mContext;
    //    private Tencent mTencent;
    private IWXAPI api;
//    private WbShareHandler shareHandler;

    public ShareUtil(Activity context) {
        this.mContext = context;
    }

    public void startShare(ShareData shareData) {
        if (shareData != null) {
            switch (shareData.getChannel()) {
                case ShareData.QQ:
                    shareToQQ(shareData);
                    break;
                case ShareData.QQ_ZONE:
                    shareToQQ_Zone(shareData);
                    break;
                case ShareData.WX:
                case ShareData.WX_CIRCLE:
                    shareToWx(shareData);
                    break;
                case ShareData.SINA:
                    shareToSina(shareData);
                    break;

            }
        }
    }

    private void shareToSina(ShareData shareData) {
        /*if (SystemUtil.isInstalled(mContext, "com.sina.weibo")) {
            if (shareHandler == null) {
                shareHandler = new WbShareHandler(mContext);
                shareHandler.registerApp();
            }
            Flowable.create((FlowableOnSubscribe<Boolean>) e -> e.onNext(true), BackpressureStrategy.BUFFER).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Boolean>() {
                @Override
                public void accept(Boolean aBoolean) throws Exception {
                    GlideApp.with(App.mApp).asBitmap().load(shareData.getPicUrl()).into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
                            weiboMessage.textObject = getTextObj(shareData.getTitle() + "\n" + shareData.getSummary() + shareData.getTargtUrl());
                            weiboMessage.imageObject = getImageObj(resource);
                            shareHandler.shareMessage(weiboMessage, false);
                        }
                    });

                }
            });

        } else {
            ToastUtil.showToast("请安装新浪客户端");
        }
*/
    }

    /**
     * 微信和朋友圈
     *
     * @param shareData
     */
    private void shareToWx(ShareData shareData) {
        if (api == null) {
            api = WXAPIFactory.createWXAPI(mContext, WEIXIN_APP_ID, true);
            api.registerApp(WEIXIN_APP_ID);
        }

        if (api.isWXAppInstalled() && api.isWXAppSupportAPI()) {
            WXWebpageObject webpage = new WXWebpageObject();
            webpage.webpageUrl = shareData.getTargtUrl();
            WXMediaMessage mediaMessage = new WXMediaMessage(webpage);
            mediaMessage.title = shareData.getTitle();
            mediaMessage.description = shareData.getSummary();
            Flowable.create((FlowableOnSubscribe<Boolean>) e -> e.onNext(true), BackpressureStrategy.BUFFER).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Boolean>() {
                @Override
                public void accept(Boolean aBoolean) throws Exception {
                    GlideApp.with(App.mApp).asBitmap().load(shareData.getPicUrl()).into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            mediaMessage.setThumbImage(compressImage(resource));
                            SendMessageToWX.Req req = new SendMessageToWX.Req();
                            req.transaction = buildTransaction("webpage");
                            req.message = mediaMessage;
                            req.scene = shareData.getChannel() == ShareData.WX ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
                            api.sendReq(req);
                        }
                    });

                }
            });

        } else {
            ToastUtil.showToast("请将微信安装或升级到最新版");
        }
    }

    private void shareToQQ_Zone(ShareData shareData) {
        /*if (mTencent == null)
            mTencent = Tencent.createInstance(QQAPP_ID, mContext.getApplicationContext());
        Bundle bundle = new Bundle();
        bundle.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        bundle.putString(QzoneShare.SHARE_TO_QQ_TITLE, shareData.getTitle());//必填
        bundle.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, shareData.getSummary());//选填
        bundle.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, shareData.getTargtUrl());//必填
        bundle.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, shareData.getPicUrls());
        bundle.putInt(QzoneShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        mTencent.shareToQzone(mContext, bundle, this);*/
    }

    private void shareToQQ(ShareData shareData) {
        /*if (mTencent == null)
            mTencent = Tencent.createInstance(QQAPP_ID, mContext.getApplicationContext());
        Bundle bundle = new Bundle();
        bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        bundle.putString(QQShare.SHARE_TO_QQ_TITLE, shareData.getTitle());
        bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, shareData.getPicUrl());
        bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareData.getTargtUrl());
        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, shareData.getSummary());
        mTencent.shareToQQ(mContext, bundle, this);*/
    }

    /*@Override
    public void onComplete(Object o) {
        LogUtil.i("qqshaore", "complete");
    }

    @Override
    public void onError(UiError uiError) {
        LogUtil.i("qqshaore", "error" + uiError.errorMessage + "__" + uiError.errorDetail);
    }

    @Override
    public void onCancel() {
        LogUtil.i("qqshaore", "cancel");
    }

    public Tencent getmTencent() {
        return mTencent;
    }*/

    public Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 32) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    public String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    /**
     * 创建文本消息对象。
     *
     * @return 文本消息对象。
     */
    /*private static TextObject getTextObj(String titles) {
        TextObject textObject = new TextObject();
        textObject.text = titles;
        return textObject;
    }*/

    /**
     * 创建图片消息对象。
     *
     * @return 图片消息对象。
     */
    /*private static ImageObject getImageObj(Bitmap mBitmap) {
        ImageObject imageObject = new ImageObject();
        //设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        imageObject.setImageObject(mBitmap);
        return imageObject;
    }

    public WbShareHandler getShareHandler() {
        return shareHandler;
    }*/
}
