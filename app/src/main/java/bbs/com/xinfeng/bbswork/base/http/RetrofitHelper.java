package bbs.com.xinfeng.bbswork.base.http;

import android.content.Context;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import com.google.gson.Gson;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import bbs.com.xinfeng.bbswork.base.Constant;
import bbs.com.xinfeng.bbswork.base.http.interceptor.HeadInterceptor;
import bbs.com.xinfeng.bbswork.utils.SharedPrefUtil;
import io.rx_cache2.internal.RxCache;
import io.victoralbertos.jolyglot.GsonSpeaker;
import bbs.com.xinfeng.bbswork.base.App;
import bbs.com.xinfeng.bbswork.utils.DataHelper;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static bbs.com.xinfeng.bbswork.base.App.isDebug;
import static bbs.com.xinfeng.bbswork.base.Constant.BASEURL;
import static bbs.com.xinfeng.bbswork.base.Constant.BASEURL_OFFICE;
import static bbs.com.xinfeng.bbswork.base.Constant.BASEURL_TEST;

/**
 * Created by dell on 2017/10/20.
 */

public class RetrofitHelper {

    private OkHttpClient okHttpClient;
    private Retrofit retrofit;
    private RetrofitService retrofitService;
    private CacheProvide cacheProvide;
    private Gson gson;
    private static volatile RetrofitHelper singleton;

    public static RetrofitHelper getInstance() {
        if (singleton == null) {
            synchronized (RetrofitHelper.class) {
                if (singleton == null) {
                    singleton = new RetrofitHelper();
                }
            }
        }
        return singleton;
    }

   /* static class InstanceHolder {
        private static RetrofitHelper instance = new RetrofitHelper();
    }*/

    public RetrofitHelper() {
        if (isDebug) {
            if (!TextUtils.isEmpty(SharedPrefUtil.getString(Constant.baseurl_key, ""))) {
                BASEURL = SharedPrefUtil.getString(Constant.baseurl_key, "");
            } else {
                BASEURL = BASEURL_TEST;
            }
        } else {
            BASEURL = BASEURL_OFFICE;
        }

        initOkhttp();
        initRetrofit();
        initCache();
        initGson();
    }

    private void initGson() {
        gson = new Gson();
    }

    private CacheProvide initCache() {
        File cacheDirectory = new File(DataHelper.getCacheFile(App.mApp), "RxCache");
        return cacheProvide = new RxCache.Builder().setMaxMBPersistenceCache(100).persistence(DataHelper.makeDirs(cacheDirectory), new GsonSpeaker()).using(CacheProvide.class);
    }

    private RetrofitService initRetrofit() {
        retrofit = new Retrofit.Builder().baseUrl(BASEURL).client(okHttpClient).addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
        return retrofitService = retrofit.create(RetrofitService.class);
    }

    private void initOkhttp() {
        okHttpClient = new OkHttpClient.Builder().readTimeout(10, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS).connectTimeout(10, TimeUnit.SECONDS)
               /* //证书验证
                .certificatePinner(new CertificatePinner.Builder().add("", "").build())
                //代理
                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("", 8888)))
                //https安全验证
                .sslSocketFactory(getSSLSocketFactory(App.getInstance(), new int[]{R.raw.myssl}))*/
                .addInterceptor(new HeadInterceptor())
                .retryOnConnectionFailure(true).build();
    }

    public RetrofitService getRetrofitService() {
        if (retrofitService == null)
            initRetrofit();
        return retrofitService;
    }

    public CacheProvide getCacheProvide() {
        if (cacheProvide == null)
            initCache();
        return cacheProvide;
    }

    public Gson getGson() {
        if (gson == null)
            initGson();
        return gson;
    }

    protected static SSLSocketFactory getSSLSocketFactory(Context context, int[] certificates) {

        if (context == null) {
            throw new NullPointerException("context == null");
        }

        CertificateFactory certificateFactory;
        try {
            certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);

            for (int i = 0; i < certificates.length; i++) {
                InputStream certificate = context.getResources().openRawResource(certificates[i]);
                keyStore.setCertificateEntry(String.valueOf(i), certificateFactory.generateCertificate(certificate));

                if (certificate != null) {
                    certificate.close();
                }
            }
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getMimeType(String path) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(path);
        if (extension != null) {
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getMimeTypeFromExtension(extension);
        }
        return type;
    }
}
