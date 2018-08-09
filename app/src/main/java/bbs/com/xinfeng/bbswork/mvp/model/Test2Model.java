package bbs.com.xinfeng.bbswork.mvp.model;

import android.content.Context;
import android.util.ArrayMap;

import java.util.Map;

import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;

/**
 * Created by dell on 2017/11/23.
 */

public class Test2Model extends NetModel {
    public Test2Model(NetContract.OnDataLoadingListener dataLoadingListener, Context activity) {
        super(dataLoadingListener, activity);
    }

    public void getCollectData(String size, String page, int tag) {
        Map<String, String> params = new ArrayMap<>();
        params.put("uid", "1895");
        params.put("encpass", "9db06bcff9248837f86d1a6bcf41c9e7");
        params.put("size", size);
        params.put("page", page);
        packageData(retrofitService.getCollectionInfo(params), tag);
//        Flowable<Reply<BaseBean<CollectionBean>>> collectionInfo = cacheProvide.getCollectionInfo(retrofitService.getCollectionInfo(params),new DynamicKey(page), new EvictProvider(false));
//        packageDataWithCache(collectionInfo, tag);
    }
}
