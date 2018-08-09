package bbs.com.xinfeng.bbswork.mvp.model;

import android.content.Context;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.util.Map;

import bbs.com.xinfeng.bbswork.base.App;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.rx_cache2.Reply;
import bbs.com.xinfeng.bbswork.base.IBaseModel;
import bbs.com.xinfeng.bbswork.base.ProgressListener;
import bbs.com.xinfeng.bbswork.base.http.CacheProvide;
import bbs.com.xinfeng.bbswork.base.http.RetrofitHelper;
import bbs.com.xinfeng.bbswork.base.http.RetrofitService;
import bbs.com.xinfeng.bbswork.base.http.UploadFileRequestBody;
import bbs.com.xinfeng.bbswork.domin.BaseBean;
import bbs.com.xinfeng.bbswork.domin.ErrorBean;
import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.utils.ACache;
import bbs.com.xinfeng.bbswork.utils.ArmsUtils;
import bbs.com.xinfeng.bbswork.utils.RxUtil;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static bbs.com.xinfeng.bbswork.base.http.ErrorCode.ERROR_CODE_NETWORK;
import static bbs.com.xinfeng.bbswork.base.http.ErrorCode.ERROR_DESC_NETWORK;
import static bbs.com.xinfeng.bbswork.domin.ErrorBean.TYPE_DEAL;
import static bbs.com.xinfeng.bbswork.domin.ErrorBean.TYPE_SHOW;

/**
 * Created by dell on 2017/10/18.
 */

public class NetModel implements IBaseModel {
    private ACache aCache;
    protected Context mContext;

    private Map<Integer, Disposable> mSubscriptionMap = new ArrayMap<>();
    public NetContract.OnDataLoadingListener dataLoadingListener;
    protected RetrofitService retrofitService;
    protected CacheProvide cacheProvide;
    protected Gson gson;

    public NetModel(NetContract.OnDataLoadingListener dataLoadingListener, Context activity) {
        mContext = activity;
        this.dataLoadingListener = dataLoadingListener;
        retrofitService = RetrofitHelper.getInstance().getRetrofitService();
        cacheProvide = RetrofitHelper.getInstance().getCacheProvide();
        gson = RetrofitHelper.getInstance().getGson();
        /*if (mContext != null)
            aCache = ACache.get(mContext);*/
    }

    @Override
    public void cancelNormal(int tag) {
        Disposable disposable = mSubscriptionMap.get(tag);
        if (disposable != null && !disposable.isDisposed())
            disposable.dispose();
    }

    @Override
    public void cancelAll() {
        for (Map.Entry<Integer, Disposable> entry : mSubscriptionMap.entrySet()) {
            if (!entry.getValue().isDisposed())
                entry.getValue().dispose();
        }
        mSubscriptionMap.clear();
    }

    /**
     * 设置缓存 ACache
     *
     * @param classifyInfo
     * @param tag
     */
    @Override
    public void packageData(Flowable<? extends BaseBean> classifyInfo, int tag, String cacheKey, int cacheTime) {

        if (cacheTime <= 0) {
            dataLoadingListener.startLoading(tag);
            Disposable disposable = classifyInfo.map(new Function<BaseBean, ErrorBean>() {
                @Override
                public ErrorBean apply(BaseBean baseBean) throws Exception {
                    if (baseBean.getCode() == BaseBean.SUCCESS_CODE)
                        aCache.put(ArmsUtils.encodeToMD5(cacheKey), gson.toJson(baseBean.getData()));
                    return baseBean.getData();
                }
            }).compose(RxUtil.fixScheduler()).subscribe(testBeanBaseBean -> {
                dataLoadingListener.onSuccess(testBeanBaseBean, tag, true);
            }, Throwable -> {
                String objString = aCache.getAsString(ArmsUtils.encodeToMD5(cacheKey));
                if (!TextUtils.isEmpty(objString)) {
                    dataLoadingListener.onSuccess(gson.fromJson(objString, ErrorBean.class), tag, false);
                }
                dataLoadingListener.onError(new ErrorBean(ERROR_CODE_NETWORK, ERROR_DESC_NETWORK, TYPE_SHOW), tag);
            });
            mSubscriptionMap.put(tag, disposable);

        } else {
            String objString = aCache.getAsString(ArmsUtils.encodeToMD5(cacheKey));
            if (TextUtils.isEmpty(objString)) {
                dataLoadingListener.startLoading(tag);
                Disposable disposable = classifyInfo.map(new Function<BaseBean, ErrorBean>() {
                    @Override
                    public ErrorBean apply(BaseBean baseBean) throws Exception {
                        if (baseBean.getCode() == BaseBean.SUCCESS_CODE)
                            aCache.put(ArmsUtils.encodeToMD5(cacheKey), gson.toJson(baseBean.getData()), cacheTime);
                        return baseBean.getData();
                    }
                }).compose(RxUtil.fixScheduler()).subscribe(testBeanBaseBean -> {
                    dataLoadingListener.onSuccess(testBeanBaseBean, tag, true);
                }, Throwable -> {
                    dataLoadingListener.onError(new ErrorBean(ERROR_CODE_NETWORK, ERROR_DESC_NETWORK, TYPE_SHOW), tag);
                });
                mSubscriptionMap.put(tag, disposable);
            } else {
                dataLoadingListener.onSuccess(gson.fromJson(objString, ErrorBean.class), tag, false);
            }
        }

    }

    /**
     * 不设置缓存
     *
     * @param classifyInfo
     * @param tag
     */
    @Override
    public void packageData(Flowable<? extends BaseBean> classifyInfo, int tag) {
        dataLoadingListener.startLoading(tag);
        Disposable disposable = classifyInfo.compose(RxUtil.fixScheduler()).subscribe(new Consumer<BaseBean>() {
            @Override
            public void accept(BaseBean baseBean) throws Exception {
                if (baseBean.getCode() == BaseBean.SUCCESS_CODE) {
                    dataLoadingListener.onSuccess(baseBean.getData(), tag, true);
                } else {
                    dataLoadingListener.onError(new ErrorBean(baseBean.getCode() + "", baseBean.getNotice() == null ? baseBean.getMessage() : baseBean.getNotice(), baseBean.getNotice() == null ? TYPE_DEAL : TYPE_SHOW), tag);
                }
            }
        }, Throwable -> {
            if (App.isDebug)
                dataLoadingListener.onError(new ErrorBean(ERROR_CODE_NETWORK, Throwable.toString(), TYPE_SHOW), tag);
            else
                dataLoadingListener.onError(new ErrorBean(ERROR_CODE_NETWORK, ERROR_DESC_NETWORK, TYPE_SHOW), tag);
        });

     /*   Disposable disposable = classifyInfo.map(new Function<BaseBean, ErrorBean>() {
            @Override
            public ErrorBean apply(BaseBean baseBean) throws Exception {
                return baseBean.getData();
            }
        }).compose(RxUtil.fixScheduler()).subscribe(testBeanBaseBean -> {
            dataLoadingListener.onSuccess(testBeanBaseBean, tag, true);
        }, Throwable -> {
            dataLoadingListener.onError(new ErrorBean(ERROR_CODE_NETWORK, ERROR_DESC_NETWORK, TYPE_SHOW), tag);
        });*/
        mSubscriptionMap.put(tag, disposable);

    }

    /**
     * 不设置缓存,带key
     *
     * @param classifyInfo
     * @param tag
     */
    public void packageData(Flowable<? extends BaseBean> classifyInfo, String key, int tag) {
        dataLoadingListener.startLoading(tag);
        Disposable disposable = classifyInfo.compose(RxUtil.fixScheduler()).subscribe(new Consumer<BaseBean>() {
            @Override
            public void accept(BaseBean baseBean) throws Exception {
                if (baseBean.getCode() == BaseBean.SUCCESS_CODE) {
                    ErrorBean data = baseBean.getData();
                    if (data == null) {
                        data = new ErrorBean();
                    }
                    data.key = key;
                    dataLoadingListener.onSuccess(data, tag, true);
                } else {
                    dataLoadingListener.onError(new ErrorBean(baseBean.getCode() + "", baseBean.getNotice() == null ? baseBean.getMessage() : baseBean.getNotice(), baseBean.getNotice() == null ? TYPE_DEAL : TYPE_SHOW, key), tag);
                }
            }
        }, Throwable -> {
            if (App.isDebug)
                dataLoadingListener.onError(new ErrorBean(ERROR_CODE_NETWORK, Throwable.toString(), TYPE_SHOW, key), tag);
            else
                dataLoadingListener.onError(new ErrorBean(ERROR_CODE_NETWORK, ERROR_DESC_NETWORK, TYPE_SHOW, key), tag);
        });

        mSubscriptionMap.put(tag, disposable);

    }

    /**
     * 上传单张图片
     *
     * @param classifyInfo
     * @param tag
     */
    public void packageDataUpload(Flowable<? extends BaseBean> classifyInfo, String path, int tag) {
        dataLoadingListener.startLoading(tag);
        Disposable disposable = classifyInfo.compose(RxUtil.fixScheduler()).subscribe(new Consumer<BaseBean>() {
            @Override
            public void accept(BaseBean baseBean) throws Exception {
                if (baseBean.getCode() == BaseBean.SUCCESS_CODE) {
                    ErrorBean data = baseBean.getData();
                    if (data == null) {
                        data = new ErrorBean();
                    }
                    data.key = path;
                    dataLoadingListener.onSuccess(data, tag, true);
                } else {
                    dataLoadingListener.onError(new ErrorBean(baseBean.getCode() + "", baseBean.getNotice() == null ? baseBean.getMessage() : baseBean.getNotice(), baseBean.getNotice() == null ? TYPE_DEAL : TYPE_SHOW, path), tag);
                }
            }
        }, Throwable -> {
            if (App.isDebug)
                dataLoadingListener.onError(new ErrorBean(ERROR_CODE_NETWORK, Throwable.toString(), TYPE_SHOW, path), tag);
            else
                dataLoadingListener.onError(new ErrorBean(ERROR_CODE_NETWORK, ERROR_DESC_NETWORK, TYPE_SHOW, path), tag);
        });

        mSubscriptionMap.put(tag, disposable);

    }

    /**
     * 上传单张图片
     *
     * @param classifyInfo
     * @param tag
     */
    public void packageDataUpload(Flowable<? extends BaseBean> classifyInfo, String key, String path, int tag) {
        dataLoadingListener.startLoading(tag);
        Disposable disposable = classifyInfo.compose(RxUtil.fixScheduler()).subscribe(new Consumer<BaseBean>() {
            @Override
            public void accept(BaseBean baseBean) throws Exception {
                if (baseBean.getCode() == BaseBean.SUCCESS_CODE) {
                    ErrorBean data = baseBean.getData();
                    if (data == null) {
                        data = new ErrorBean();
                    }
                    data.localPath = path;
                    data.key = key;
                    dataLoadingListener.onSuccess(data, tag, true);
                } else {
                    dataLoadingListener.onError(new ErrorBean(baseBean.getCode() + "", baseBean.getNotice() == null ? baseBean.getMessage() : baseBean.getNotice(), baseBean.getNotice() == null ? TYPE_DEAL : TYPE_SHOW, key, path), tag);
                }
            }
        }, Throwable -> {
            if (App.isDebug)
                dataLoadingListener.onError(new ErrorBean(ERROR_CODE_NETWORK, Throwable.toString(), TYPE_SHOW, key, path), tag);
            else
                dataLoadingListener.onError(new ErrorBean(ERROR_CODE_NETWORK, ERROR_DESC_NETWORK, TYPE_SHOW, key, path), tag);
        });

        mSubscriptionMap.put(tag, disposable);

    }


    /**
     * Rxcache 设置缓存
     *
     * @param classifyInfo
     * @param tag
     */
    public void packageDataWithCache(Flowable<? extends Reply<? extends BaseBean>> classifyInfo, int tag) {
        dataLoadingListener.startLoading(tag);
        Disposable disposable = classifyInfo.compose(RxUtil.fixScheduler()).subscribe(new Consumer<Reply<? extends BaseBean>>() {
            @Override
            public void accept(Reply<? extends BaseBean> reply) {
                BaseBean data = reply.getData();
                dataLoadingListener.onSuccess(data.getData(), tag, true);
            }
        }, throwable -> {
            dataLoadingListener.onError(new ErrorBean(ERROR_CODE_NETWORK, ERROR_DESC_NETWORK, TYPE_SHOW), tag);
        });


       /* Disposable disposable = classifyInfo.map(new Function<Reply<? extends BaseBean>, ErrorBean>() {
            @Override
            public ErrorBean apply(@NonNull Reply<? extends BaseBean> reply) throws Exception {
                return reply.getData().getContent();
            }
        }).compose(RxUtil.fixScheduler()).subscribe(testBeanBaseBean -> {
                    dataLoadingListener.onSuccess(testBeanBaseBean, tag, true);
                }, throwable -> {
                    dataLoadingListener.onError(new ErrorBean(ERROR_CODE_NETWORK, ERROR_DESC_NETWORK, TYPE_SHOW), tag);
                    Log.e("bbbbbbb", throwable.getMessage());
                    Logger.log(Logger.ERROR, ERROR_CODE_NETWORK, ERROR_DESC_NETWORK, throwable);
                });*/
        mSubscriptionMap.put(tag, disposable);
    }

    /**
     * 上传文件时包装request
     *
     * @param params
     * @param file
     * @param tag
     * @return
     */
    public Map<String, RequestBody> packParamsToRequestBody(Map<String, String> params, File file, int tag) {
        Map<String, RequestBody> requestBodyMap = new ArrayMap<>();
        UploadFileRequestBody fileRequestBody = new UploadFileRequestBody(file, MediaType.parse(RetrofitHelper.getInstance().getMimeType(file.getAbsolutePath())), new ProgressListener() {
            @Override
            public void onProgress(long currentBytes, long contentLength, boolean done) {
                dataLoadingListener.onProgress((int) (100f * currentBytes / contentLength), tag, done);
            }
        });
        requestBodyMap.put("file\"; filename=\"" + file.getName(), fileRequestBody);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            requestBodyMap.put(entry.getKey(), RequestBody.create(null, entry.getValue()));
        }
        return requestBodyMap;
    }
}
