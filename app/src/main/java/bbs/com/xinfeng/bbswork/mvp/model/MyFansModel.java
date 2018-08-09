package bbs.com.xinfeng.bbswork.mvp.model;

import android.app.Activity;
import android.util.ArrayMap;

import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;

/**
 * Created by dell on 2017/10/20.
 */

public class MyFansModel extends NetModel {
    public MyFansModel(NetContract.OnDataLoadingListener dataLoadingListener, Activity activity) {
        super(dataLoadingListener, activity);
    }

    public void getFansList(int page, int type, int tag) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("page", page + "");
        params.put("type", type + "");
        packageData(retrofitService.getFansList(params), tag);
    }

    public void followAction(int type, int userid, int tag) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("act", type + "");
        params.put("target_id", userid + "");
        packageData(retrofitService.followAction(params), tag);
    }

    public void actBlack(int user_id, int act, int tag) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("target_id", user_id + "");
        params.put("act", act + "");
        packageData(retrofitService.blackAction(params), tag);
    }

    public void getBlackList(int page, int tag) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("page", page + "");
        packageData(retrofitService.getBlackList(params), tag);
    }
}
