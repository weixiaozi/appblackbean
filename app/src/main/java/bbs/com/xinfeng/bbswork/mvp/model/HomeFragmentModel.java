package bbs.com.xinfeng.bbswork.mvp.model;

import android.app.Activity;
import android.text.TextUtils;
import android.util.ArrayMap;

import bbs.com.xinfeng.bbswork.domin.BaseBean;
import bbs.com.xinfeng.bbswork.domin.TestBean;
import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import io.reactivex.Flowable;

/**
 * Created by dell on 2017/10/20.
 */

public class HomeFragmentModel extends NetModel {
    public HomeFragmentModel(NetContract.OnDataLoadingListener dataLoadingListener, Activity activity) {
        super(dataLoadingListener, activity);
    }


    public void getTopicList(String topicIds, int tag) {
        ArrayMap<String, String> params = new ArrayMap<>();
        if (!TextUtils.isEmpty(topicIds))
            params.put("id", topicIds);
        packageData(retrofitService.getTopicUserlist(params), tag);
    }
}
