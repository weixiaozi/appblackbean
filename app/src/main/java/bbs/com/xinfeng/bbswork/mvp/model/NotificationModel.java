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

public class NotificationModel extends NetModel {
    public NotificationModel(NetContract.OnDataLoadingListener dataLoadingListener, Activity activity) {
        super(dataLoadingListener, activity);
    }

    public void getNotification(int page, int tag) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("page", page + "");
        packageData(retrofitService.getNotifications(params), tag);
    }

    public void getNewNotification(int noticeId, int direct, int tag) {
        cancelNormal(tag);
        ArrayMap<String, String> params = new ArrayMap<>();
        if (noticeId != 0) {
            params.put("notice_id", noticeId + "");
        }
        params.put("direct", direct + "");
        packageData(retrofitService.getNotifications(params), tag);
    }

    public void operateNotice(int topic_id, int user_id, int log_id, String msg, int act, int tag) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("id", topic_id + "");
        params.put("user_id", user_id + "");
        params.put("act", act + "");
        params.put("id", topic_id + "");
        params.put("log_id", log_id + "");
        if (!TextUtils.isEmpty(msg))
            params.put("check_msg", msg);
        packageData(retrofitService.operateNotice(params), tag);
    }

    public void clearNotice(int tag) {
        ArrayMap<String, String> params = new ArrayMap<>();
        packageData(retrofitService.clearNotice(params), tag);
    }

    public void inviteNotice(int topic_id, int log_id, int act, String msg, int tag) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("id", topic_id + "");
        params.put("type", act + "");
        params.put("log_id", log_id + "");
        if (!TextUtils.isEmpty(msg))
            params.put("check_msg", msg);
        packageData(retrofitService.inviteOperateNotice(params), tag);
    }
}
