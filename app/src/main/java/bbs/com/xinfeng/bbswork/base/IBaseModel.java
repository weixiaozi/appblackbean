package bbs.com.xinfeng.bbswork.base;

import io.reactivex.Flowable;
import bbs.com.xinfeng.bbswork.domin.BaseBean;

/**
 * Created by dell on 2017/10/17.
 */

public interface IBaseModel {
    /**
     * 取消某个请求或者停止上传
     *
     * @param tag 该请求的标记
     */
    void cancelNormal(int tag);

    void cancelAll();

    /**
     * 设置缓存
     *
     * @param classifyInfo
     * @param tag
     * @param cacheKey:建议使用接口名称，如包含分页，拼接page值
     * @param cacheTime
     */
    void packageData(Flowable<? extends BaseBean> classifyInfo, int tag, String cacheKey, int cacheTime);

    /**
     * 不设置缓存
     *
     * @param classifyInfo
     * @param tag
     */
    void packageData(Flowable<? extends BaseBean> classifyInfo, int tag);

}
