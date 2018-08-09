package bbs.com.xinfeng.bbswork.mvp.model;

import android.app.Activity;
import android.util.ArrayMap;

import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;

/**
 * Created by dell on 2017/10/20.
 */

public class MyReplyModel extends NetModel {
    public MyReplyModel(NetContract.OnDataLoadingListener dataLoadingListener, Activity activity) {
        super(dataLoadingListener, activity);
    }


    public void getMyreply(int pageid, int tag) {
        ArrayMap<String, String> params = new ArrayMap<>();
        if (pageid != 0) {
            params.put("id", pageid + "");
            params.put("direct", "-1");
        }
        packageData(retrofitService.getMyreplyList(params), tag);
    }

    public void getReplyMe(int pageid, int tag) {
        ArrayMap<String, String> params = new ArrayMap<>();
        if (pageid != 0) {
            params.put("id", pageid + "");
            params.put("direct", "-1");
        }
        packageData(retrofitService.getReplyMeList(params), tag);
    }

    public void getClickme(int pageid, int tag) {
        ArrayMap<String, String> params = new ArrayMap<>();
        if (pageid != 0) {
            params.put("id", pageid + "");
            params.put("direct", "-1");
        }
        packageData(retrofitService.getClickMeList(params), tag);
    }
}
