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

public class TopicDetailModel extends NetModel {
    public TopicDetailModel(NetContract.OnDataLoadingListener dataLoadingListener, Activity activity) {
        super(dataLoadingListener, activity);
    }


    public void getTopicDetail(String topicid, int tag) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("id", topicid);
        packageData(retrofitService.topicDetails(params), tag);
    }

    public void outTopic(String id, int tag) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("id", id);
        packageData(retrofitService.outTopicUsers(params), tag);
    }

    public void readTheme(int id, int tag) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("thread_id", id + "");
        packageData(retrofitService.haveRead(params), tag);

    }

    public void readThemeAudio(int id, int tag) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("thread_id", id + "");
        packageData(retrofitService.haveReadAudio(params), tag);

    }
}
