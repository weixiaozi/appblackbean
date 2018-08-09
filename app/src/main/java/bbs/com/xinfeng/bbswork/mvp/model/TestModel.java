package bbs.com.xinfeng.bbswork.mvp.model;

import android.app.Activity;
import android.util.ArrayMap;

import io.reactivex.Flowable;
import bbs.com.xinfeng.bbswork.domin.BaseBean;
import bbs.com.xinfeng.bbswork.domin.TestBean;
import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;

/**
 * Created by dell on 2017/10/20.
 */

public class TestModel extends NetModel {
    public TestModel(NetContract.OnDataLoadingListener dataLoadingListener, Activity activity) {
        super(dataLoadingListener, activity);
    }

    public void getBphp(int tag) {
        ArrayMap<String, String> params = new ArrayMap<>();
        Flowable<BaseBean<TestBean>> classifyInfo = retrofitService.getClassifyInfo(params);

        packageData(classifyInfo, tag);
//        Flowable<Reply<BaseBean<TestBean>>> classifyInfo1 = cacheProvide.getClassifyInfo(classifyInfo, new EvictProvider(false));
//        packageDataWithCache(classifyInfo1, tag);

    }

}
