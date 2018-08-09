package bbs.com.xinfeng.bbswork.ui.service;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import bbs.com.xinfeng.bbswork.base.App;
import bbs.com.xinfeng.bbswork.module.GlideApp;
import bbs.com.xinfeng.bbswork.utils.RxUtil;

import static bbs.com.xinfeng.bbswork.base.Constant.GAME_PATH;

public class DownLoadPicIntentService extends IntentService {

    int i;
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public DownLoadPicIntentService() {
        super("downLoadPicIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        ArrayList<String> pics = intent.getStringArrayListExtra("pics");
        Flowable.fromArray(pics).compose(RxUtil.fixScheduler()).subscribe(new Consumer<ArrayList<String>>() {
            @Override
            public void accept(ArrayList<String> strings) throws Exception {
                for (String picUrl : strings) {
                    GlideApp.with(App.getInstance()).asBitmap().load(picUrl).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            try {
//                                savePresentCache(resource,picUrl.substring(picUrl.length()-8));
                                savePresentCache(resource,"number_"+(i++)+".png");
                            } catch (NoSuchAlgorithmException e) {

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });

    }

    private void savePresentCache(Bitmap bitmap, String filename) throws NoSuchAlgorithmException, FileNotFoundException {
        File imgFolder = new File(GAME_PATH);
        if (!imgFolder.exists()) {
            imgFolder.mkdirs();
        }
        File imgFile = new File(GAME_PATH, filename);
        FileOutputStream fos = new FileOutputStream(imgFile);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
    }
}
