package bbs.com.xinfeng.bbswork.base.http;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictProvider;
import io.rx_cache2.LifeCache;
import io.rx_cache2.Reply;
import bbs.com.xinfeng.bbswork.domin.BaseBean;
import bbs.com.xinfeng.bbswork.domin.CollectionBean;
import bbs.com.xinfeng.bbswork.domin.TestBean;

/**
 * Created by dell on 2017/10/20.
 */
public interface CacheProvide {
    @LifeCache(duration = 3, timeUnit = TimeUnit.MINUTES)
    Flowable<Reply<BaseBean<TestBean>>> getClassifyInfo(Flowable<BaseBean<TestBean>> test, EvictProvider evictProvider);

    @LifeCache(duration = 100, timeUnit = TimeUnit.MINUTES)
    Flowable<Reply<BaseBean<CollectionBean>>> getCollectionInfo(Flowable<BaseBean<CollectionBean>> test, DynamicKey idLastUserQueried, EvictProvider evictProvider);
}
