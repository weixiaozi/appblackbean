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

public class SystemNoticeModel extends NetModel {
    public SystemNoticeModel(NetContract.OnDataLoadingListener dataLoadingListener, Activity activity) {
        super(dataLoadingListener, activity);
    }

    public void getNoticeList(int type, int page, int tag) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("type", type + "");
        params.put("page", page + "");
        packageData(retrofitService.getSystemNotices(params), tag);
    }
}
