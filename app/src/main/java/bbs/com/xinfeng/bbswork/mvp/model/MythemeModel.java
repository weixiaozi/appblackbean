package bbs.com.xinfeng.bbswork.mvp.model;

import android.app.Activity;
import android.util.ArrayMap;

import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;

/**
 * Created by dell on 2017/10/20.
 */

public class MythemeModel extends NetModel {
    public MythemeModel(NetContract.OnDataLoadingListener dataLoadingListener, Activity activity) {
        super(dataLoadingListener, activity);
    }

    public void getThemeList(int thread_id, int tag) {
        ArrayMap<String, String> params = new ArrayMap<>();
        if (thread_id != 0) {
            params.put("thread_id", thread_id + "");
            params.put("direct", "-1");
        }
        packageData(retrofitService.getMyThemeList(params), tag);
    }

    public void upDataThemeInfo(String thread_id, int tag) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("thread_id", thread_id + "");
        params.put("direct", "0");
        packageData(retrofitService.getMyThemeList(params), tag);
    }

    public void readThemeAudio(int id, int tag) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("thread_id", id + "");
        packageData(retrofitService.haveReadAudio(params), tag);

    }
}
