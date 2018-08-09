package bbs.com.xinfeng.bbswork.mvp.model;

import android.app.Activity;
import android.text.TextUtils;
import android.util.ArrayMap;

import java.io.File;
import java.util.Map;

import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;

import static bbs.com.xinfeng.bbswork.domin.UploadBean.File_pic;
import static bbs.com.xinfeng.bbswork.domin.UploadBean.Pic_topic;

/**
 * Created by dell on 2017/10/20.
 */

public class MineModel extends NetModel {
    public MineModel(NetContract.OnDataLoadingListener dataLoadingListener, Activity activity) {
        super(dataLoadingListener, activity);
    }

    public void getUserInfo(int tag) {
        packageData(retrofitService.getUserinfo(new ArrayMap<>()), tag);
    }

    public void getUserContent(int tag) {
        packageData(retrofitService.getUserContent(new ArrayMap<>()), tag);
    }
}
