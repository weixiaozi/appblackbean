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

public class TopicOperateModel extends NetModel {
    public TopicOperateModel(NetContract.OnDataLoadingListener dataLoadingListener, Activity activity) {
        super(dataLoadingListener, activity);
    }

    public void applyIntopic(String topicid, String info, int tag) {
        ArrayMap<String, String> params = new ArrayMap<>();
        if (!TextUtils.isEmpty(info))
            params.put("apply_msg", info);
        params.put("id", topicid);
        packageData(retrofitService.applyInTopic(params), tag);
    }
}
