package bbs.com.xinfeng.bbswork.base;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.rtmp.TXLiveBase;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.ugc.TXUGCBase;


import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import bbs.com.xinfeng.bbswork.BuildConfig;
import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.domin.BaseBean;
import bbs.com.xinfeng.bbswork.domin.CollectionInnerBean;
import bbs.com.xinfeng.bbswork.domin.ErrorBean;
import bbs.com.xinfeng.bbswork.domin.InnerThemeListBean;
import bbs.com.xinfeng.bbswork.domin.LoginBus;
import bbs.com.xinfeng.bbswork.domin.MessageFragmentInnerBean;
import bbs.com.xinfeng.bbswork.domin.MessageNumObjectBox;
import bbs.com.xinfeng.bbswork.domin.MyObjectBox;
import bbs.com.xinfeng.bbswork.domin.NoticeChangeBean;
import bbs.com.xinfeng.bbswork.domin.PrivateChatItemBean;
import bbs.com.xinfeng.bbswork.domin.SocketAddressBean;
import bbs.com.xinfeng.bbswork.domin.SocketStatusBus;
import bbs.com.xinfeng.bbswork.domin.SocketTimeoutInfo;
import bbs.com.xinfeng.bbswork.domin.TokenBean;
import bbs.com.xinfeng.bbswork.domin.TopListInnerBean;
import bbs.com.xinfeng.bbswork.domin.UserInfoBean;
import bbs.com.xinfeng.bbswork.domin.UserInfoBus;
import bbs.com.xinfeng.bbswork.domin.VideoSecretBean;
import bbs.com.xinfeng.bbswork.function.CrashHandler;
import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.presenter.ApplicationPresenter;
import bbs.com.xinfeng.bbswork.ui.service.SocketService;
import bbs.com.xinfeng.bbswork.utils.ArmsUtils;
import bbs.com.xinfeng.bbswork.utils.LogUtil;
import bbs.com.xinfeng.bbswork.utils.SharedPrefUtil;
import bbs.com.xinfeng.bbswork.utils.SystemUtil;
import bbs.com.xinfeng.bbswork.utils.ToastUtil;
import cn.jiguang.analytics.android.api.JAnalyticsInterface;
import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import io.objectbox.BoxStore;
import io.objectbox.android.AndroidObjectBrowser;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static bbs.com.xinfeng.bbswork.base.Constant.isLogin;
import static bbs.com.xinfeng.bbswork.base.Constant.isPush;
import static bbs.com.xinfeng.bbswork.base.Constant.ugcKey;
import static bbs.com.xinfeng.bbswork.base.Constant.ugcLicenceUrl;


/**
 * Created by dell on 2017/10/17.
 */

public class App extends Application implements NetContract.INetView {
    public final int GETTOKEN_TAG = 800;//手机号初次请求和刷新令牌
    public final int SOCKETADDRESS_TAG = 801;
    public final int notifymessage_TAG = 802;
    public final int userinfo_TAG = 803;
    public final int pushid_TAG = 804;
    public final int report_TAG = 805;
    public final int videoSecret_TAG = 806;

    private boolean fromPhoneToken;
    //网络变化状态0：wifi 1：移动网络 2：没网
    public static int netStatus = -1;

    public static App mApp;
    public static boolean isDebug = true;
    private ApplicationPresenter mPresenter;
    public Handler mHandler = new MyHandler(this);
    private boolean isFirst = true;
    private boolean isDestory;

    private volatile long mCurrent;

    long advanceTime = 60 * 60 * 1000;
    private Disposable tokenDisposable;
    private Disposable addressDisposable;
    private Disposable userinfoDisposable;
    private Disposable pushidDisposable;
    private Disposable videoSecretDisposable;
    private SocketService service;
    private BoxStore mBoxStore;
    private SocketAddressBean bean;
    private boolean isSelfConnet;//是否是不需要在activity中启动socketservice
    private SocketServiceConnection serviceConnection;
    public static int appCount;

    public static App getInstance() {
        return mApp;
    }

    private static List<OnMessageCallBack> mCallList = new ArrayList<>();

    private int retry_count;
    private int retry_time;
    private int retry_time_max;

    private int retryCountLocal;

    public boolean mIsEarPhoneOn;
    public int socketStatus = -1;//-1:socket服务没有启动；0：服务启动；1：建立连接2：认证成功3：连接断开4:服务器主动断开连接

    public HashMap<String, SocketTimeoutInfo> socketTimeoutMap = new HashMap<>();
    private ExecutorService threadPool = Executors.newCachedThreadPool();

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.mApp = this;
        TXUGCBase.getInstance().setLicence(mApp, ugcLicenceUrl, ugcKey);
        if (isDebug) {
//            LeakCanary.install(this);
            CrashHandler.getInstance().init(this);
            TXLiveBase.setConsoleEnabled(true);
            TXLiveBase.setLogLevel(TXLiveConstants.LOG_LEVEL_DEBUG);
        }
        mPresenter = new ApplicationPresenter(this);
        initJPush();
        getAndSetToken();
        NetChangeReceiver mReceiver = new NetChangeReceiver();
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver, mFilter);
        if (SystemUtil.isNetworkAvailable()) {
            netStatus = 0;
        } else {
            netStatus = 2;
        }
        reportToWeb(1);
        if (SystemUtil.getAppVersion() > SharedPrefUtil.getInt(Constant.lastVersion_key, 0)) {
            reportToWeb(5);
            SharedPrefUtil.putInt(Constant.lastVersion_key, SystemUtil.getAppVersion());
        }
        initObjectBox();
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                appCount++;
                if (appCount == 1) {//回到前台
                    reportToWeb(3);
                    resetReTry();
                }
                LogUtil.i("applifecyclestart", appCount + "");
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                appCount--;
                LogUtil.i("applifecyclestop", appCount + "");
                if (appCount <= 0)
                    reportToWeb(2);
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    public void reportToWeb(int type) {
        mPresenter.reportToWeb(type, report_TAG);
    }

    //推送不能与本地绑定服务同时在application的oncreat中启动，推送的进程会死
    private void initJPush() {
        JAnalyticsInterface.init(this);
        JAnalyticsInterface.setDebugMode(isDebug);
        JPushInterface.init(this);
        JPushInterface.setDebugMode(isDebug);    // 设置开启日志,发布时请关闭日志
        JPushInterface.setAlias(this, 1, ArmsUtils.encodeToMD5(SystemUtil.getMAC(this)));
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//悬挂式Notification，5.0后显示
            builder.statusBarDrawable = R.drawable.icon_notification_logo;
        } else {
            builder.statusBarDrawable = R.mipmap.ic_launcher;
        }

        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL
                | Notification.FLAG_SHOW_LIGHTS;  //设置为自动消失和呼吸灯闪烁
        builder.notificationDefaults = Notification.DEFAULT_SOUND
                | Notification.DEFAULT_VIBRATE
                | Notification.DEFAULT_LIGHTS;  // 设置为铃声、震动、呼吸灯闪烁都要
        JPushInterface.setPushNotificationBuilder(2, builder);
    }

    private void initObjectBox() {
        mBoxStore = MyObjectBox.builder().androidContext(mApp).build();
        if (BuildConfig.DEBUG) {
            new AndroidObjectBrowser(mBoxStore).start(this);
        }
    }

    public void getAndSetToken() {
        if (SharedPrefUtil.getString(Constant.token_key, null) == null) {
            SharedPrefUtil.putBoolean(isLogin, false);
        } else {
            long refreshTime = SharedPrefUtil.getLong(Constant.token_timeout_key, 0);
            if (refreshTime - System.currentTimeMillis() < advanceTime) {
                refreshToken();
            } else {
                setToken(SharedPrefUtil.getString(Constant.token_key, null));
                updataUserInfo();
                mHandler.removeMessages(100);
                mHandler.sendEmptyMessageAtTime(100, refreshTime - advanceTime - 1000);
            }
        }

    }

    private void getIpAddress() {
        if (!isDestory)
            mPresenter.getIpAddress(SOCKETADDRESS_TAG);
    }

    private void refreshToken() {
        SharedPrefUtil.putBoolean(Constant.isLogin, false);
        Constant.token = "";
        mPresenter.refreshToken(GETTOKEN_TAG);
    }

    /**
     * 手机号获取token
     */
    public void getToken(String username, String password) {
        fromPhoneToken = true;
        mPresenter.getToken(username, password, GETTOKEN_TAG);
    }


    @Override
    public void showLoading(int tag) {

    }

    @Override
    public void hideLoading(int tag) {

    }

    public boolean sendmes(String mes, String key, int a, int chatId) {//chatId:-1:添加会话；0：不需要chatId
        LogUtil.i("privatemessageocketsend__", mes);
        putSocketSend(key, a, chatId);
        if (service != null) {
            boolean isSuccess = service.sendMsg(mes, key, a, chatId);
            return isSuccess;
        } else {
            return false;
        }
    }

    public void putSocketSend(String key, int a, int chatId) {
        socketTimeoutMap.put(key, new SocketTimeoutInfo(a, System.currentTimeMillis(), chatId));
        if (socketTimeoutMap.size() <= 1) {
            startSocketTimeoutCheck();
        }
    }

    private void startSocketTimeoutCheck() {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                while (!socketTimeoutMap.isEmpty()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        Iterator<Map.Entry<String, SocketTimeoutInfo>> iterator = socketTimeoutMap.entrySet().iterator();
                        Long currentTime = System.currentTimeMillis();
                        while (iterator.hasNext()) {
                            Map.Entry<String, SocketTimeoutInfo> next = iterator.next();
                            SocketTimeoutInfo value = next.getValue();
                            LogUtil.i("sockettimeoutcheck:", next.getKey() + "___" + value.happenTime);
                            if ((currentTime - value.happenTime) >= 10000) {
                                if (mCallList.size() > 0) {
                                    for (OnMessageCallBack callBack : mCallList) {
                                        callBack.sendToServeCallback(next.getKey(), 2, value.a, value.chatId);
                                    }
                                }
                                iterator.remove();
                            }
                        }
                    } catch (Exception e) {
                    }

                }
            }
        });
    }

    @Override
    public void showError(ErrorBean errorBean, int tag) {
        LogUtil.i("application999:", errorBean.desc + "___" + errorBean.androidcode + "___" + tag);
        if (errorBean.androidcode.equals(BaseBean.TOKEN_EXPIRED_CODE + "")) {
            refreshToken();
            return;
        }
        switch (tag) {
            case GETTOKEN_TAG:
                if (errorBean.androidcode.equals(BaseBean.REFRESHTOKEN_EXPIRED_CODE + "")) {
                    outLogin();
                    EventBus.getDefault().post(new LoginBus(false));
                    ToastUtil.showToast("请重新登录");
                } else {
                    tokenDisposable = Flowable.timer(3, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            tokenDisposable.dispose();
                            getAndSetToken();
                        }
                    });
                }
                break;
            case SOCKETADDRESS_TAG:
                addressDisposable = Flowable.timer(3, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        addressDisposable.dispose();
                        getIpAddress();
                    }
                });
                break;
            case userinfo_TAG:
                userinfoDisposable = Flowable.timer(3, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        userinfoDisposable.dispose();
                        updataUserInfo();
                    }
                });
                break;
            case notifymessage_TAG:
                break;
            case pushid_TAG:
                pushidDisposable = Flowable.timer(3, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        pushidDisposable.dispose();
                        setPushId();
                    }
                });
                break;
            case videoSecret_TAG:
                videoSecretDisposable = Flowable.timer(3, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        videoSecretDisposable.dispose();
                        getVideoSecret();
                    }
                });
                break;
        }
    }

    @Override
    public Activity provideActivity() {
        return null;
    }

    @Override
    public void setData(ErrorBean errorBean, int tag) {
        switch (tag) {
            case GETTOKEN_TAG:
                if (errorBean instanceof TokenBean) {
                    if (tokenDisposable != null && !tokenDisposable.isDisposed())
                        tokenDisposable.dispose();
                    TokenBean bean = (TokenBean) errorBean;

                    LogUtil.i("application999:", bean.getAccess_token());
                    setToken(bean.getToken_type() + " " + bean.getAccess_token());
                    SharedPrefUtil.putLong(Constant.token_timeout_key, System.currentTimeMillis() + (bean.getExpires_in() * 1000));
                    SharedPrefUtil.putString(Constant.token_key, bean.getToken_type() + " " + bean.getAccess_token());
                    SharedPrefUtil.putString(Constant.refreshtoken_key, bean.getRefresh_token());

                    mHandler.removeMessages(100);
                    mHandler.sendEmptyMessageAtTime(100, System.currentTimeMillis() + (bean.getExpires_in() * 1000) - advanceTime - 1000);
                    /*mHandler.postAtTime(new Runnable() {
                        @Override
                        public void run() {
                            refreshToken();
                        }
                    }, System.currentTimeMillis() + (bean.getExpires_in() * 1000) - advanceTime - 1000);*/
                    EventBus.getDefault().post(new LoginBus(true));
                    updataUserInfo();

                }
                break;
            case SOCKETADDRESS_TAG:
                if (errorBean instanceof SocketAddressBean) {
                    if (addressDisposable != null && !addressDisposable.isDisposed())
                        addressDisposable.dispose();
                    bean = (SocketAddressBean) errorBean;
                    if (retry_count == 0) {
                        retry_count = bean.getRetry_count();
                        retry_time = bean.getRetry_time();
                        retry_time_max = bean.getRetry_time_max();
                    }
                    LogUtil.i("socketlinktimes", "获取ip地址成功");
                    if (isSelfConnet)
                        connectSocket(bean.getHost(), bean.getPort(), bean.getTime());
                    LogUtil.i("mmmmmmdd", bean.getHost() + "___" + bean.getPort());
                }
                break;
            case notifymessage_TAG:
                if (errorBean instanceof NoticeChangeBean) {
                    NoticeChangeBean bean = (NoticeChangeBean) errorBean;
                    mCurrent = bean.getCurrent();
                    SharedPrefUtil.putLong(Constant.current_key, mCurrent);
                    List<NoticeChangeBean.DataBean> data = bean.getData();
                    LogUtil.i("mmmmmccnoticechanged", data.size() + "__");
                    if (data != null)
                        for (NoticeChangeBean.DataBean notify : data) {
                            EventBus.getDefault().post(notify);
                        }
                }
                break;
            case userinfo_TAG:
                if (errorBean instanceof UserInfoBean) {
                    if (userinfoDisposable != null && !userinfoDisposable.isDisposed())
                        userinfoDisposable.dispose();
                    UserInfoBean bean = (UserInfoBean) errorBean;
                    SharedPrefUtil.putBoolean(Constant.profile_key, bean.isProfile());
                    SharedPrefUtil.putInt(Constant.userid_key, bean.getId());
                    SharedPrefUtil.putString(Constant.name_key, bean.getName());
                    SharedPrefUtil.putString(Constant.head_key, bean.getPortrait());
                    SharedPrefUtil.putString(Constant.phone_key, bean.getPhone());
                    EventBus.getDefault().post(new UserInfoBus(bean.isProfile()));
                }
                break;
            case pushid_TAG:
                SharedPrefUtil.putBoolean(isPush, true);
                if (pushidDisposable != null && !pushidDisposable.isDisposed())
                    pushidDisposable.dispose();
                break;
            case videoSecret_TAG:
                if (videoSecretDisposable != null && !videoSecretDisposable.isDisposed())
                    videoSecretDisposable.dispose();
                VideoSecretBean bean = (VideoSecretBean) errorBean;
                Constant.videoSecret = bean.getS();
                mHandler.removeMessages(101);
                mHandler.sendEmptyMessageAtTime(101, bean.getExpire() * 1000 - 60000);
                break;
        }
    }

    private void updataUserInfo() {
        if (!fromPhoneToken && SharedPrefUtil.getBoolean(Constant.profile_key, true)) {
            mPresenter.getUserInfo(userinfo_TAG);
        }
    }

    public SocketAddressBean getBean() {
        return bean;
    }

    public void connectSocket(String host, String port, int time) {
        if (socketStatus == 1 || socketStatus == 2)
            return;
        SocketService.HOST = host;
        SocketService.PORT = Integer.parseInt(port);
        SocketService.HEART_BEAT_RATE = time * 1000;
        if (service == null) {
            Intent socketIntent = new Intent(this, SocketService.class);
            serviceConnection = new SocketServiceConnection();
            bindService(socketIntent, serviceConnection, BIND_AUTO_CREATE);
            startService(socketIntent);
        } else {
            service.connet();
        }
    }

    public void disConnectSocket() {
        isDestory = true;
        if (service != null) {
            service.cutSocket();
        }
        socketStatus = 3;
        EventBus.getDefault().post(new SocketStatusBus(3));
    }

    public void lazyConnectSocket() {
        isDestory = false;
        isSelfConnet = true;
        if (bean != null) {
            connectSocket(bean.getHost(), bean.getPort(), bean.getTime());
        }
    }

    @Override
    public void progress(int precent, int tag) {

    }

    private void setToken(String token) {
        Constant.token = token;
        SharedPrefUtil.putBoolean(isLogin, true);
        setPushId();
        getVideoSecret();
        if (isFirst) {
            getIpAddress();
            mCurrent = SharedPrefUtil.getLong(Constant.current_key, 0);
            getNotify();
            isFirst = false;
        }
    }

    private void setPushId() {
        if (!SharedPrefUtil.getBoolean(isPush, false))
            mPresenter.setPushId(pushid_TAG);
    }

    public void outLogin() {
        if (tokenDisposable != null && !tokenDisposable.isDisposed())
            tokenDisposable.dispose();
        if (addressDisposable != null && !addressDisposable.isDisposed())
            addressDisposable.dispose();
        if (userinfoDisposable != null && !userinfoDisposable.isDisposed())
            userinfoDisposable.dispose();
        if (pushidDisposable != null && !pushidDisposable.isDisposed())
            pushidDisposable.dispose();
        if (videoSecretDisposable != null && !videoSecretDisposable.isDisposed())
            videoSecretDisposable.dispose();

        Constant.token = "";
        SharedPrefUtil.remove(mApp, Constant.isLogin);
        SharedPrefUtil.remove(mApp, Constant.token_key);
        SharedPrefUtil.remove(mApp, Constant.refreshtoken_key);
        SharedPrefUtil.remove(mApp, Constant.token_timeout_key);
        SharedPrefUtil.remove(mApp, Constant.profile_key);
        SharedPrefUtil.remove(mApp, Constant.userid_key);
        SharedPrefUtil.remove(mApp, Constant.name_key);
        SharedPrefUtil.remove(mApp, Constant.head_key);
        SharedPrefUtil.remove(mApp, Constant.isPush);
        SharedPrefUtil.remove(mApp, Constant.current_key);
        SharedPrefUtil.remove(mApp, Constant.mes_current_key);
        SharedPrefUtil.remove(mApp, Constant.collect_current_key);
        /*if (mBoxStore != null) {
            mBoxStore.boxFor(TopListInnerBean.class).removeAll();
            mBoxStore.boxFor(MessageNumObjectBox.class).removeAll();
            mBoxStore.boxFor(CollectionInnerBean.class).removeAll();
            mBoxStore.boxFor(InnerThemeListBean.class).removeAll();
            mBoxStore.boxFor(MessageFragmentInnerBean.class).removeAll();
            mBoxStore.boxFor(PrivateChatItemBean.class).removeAll();
        }*/
        JPushInterface.clearLocalNotifications(this);
        disConnectSocket();
    }

    public void clearCache() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(mApp).clearDiskCache();
                if (mBoxStore != null) {
                    mBoxStore.boxFor(InnerThemeListBean.class).removeAll();
                    mBoxStore.boxFor(TopListInnerBean.class).removeAll();
                    mBoxStore.boxFor(MessageNumObjectBox.class).removeAll();
                    mBoxStore.boxFor(CollectionInnerBean.class).removeAll();
                    mBoxStore.boxFor(MessageFragmentInnerBean.class).removeAll();
                    mBoxStore.boxFor(PrivateChatItemBean.class).removeAll();
                }
            }
        }).start();
    }

    public synchronized void getNotify() {
        mPresenter.notifyMessage(mCurrent, notifymessage_TAG);
    }

    public void getVideoSecret() {
        mPresenter.getVideoSecret(videoSecret_TAG);
    }

    public class SocketServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            SocketService.BackServiceBinder binder = (SocketService.BackServiceBinder) iBinder;
            socketStatus = 0;
            EventBus.getDefault().post(new SocketStatusBus(0));
            service = binder.getService();
            service.setOnSocketCallBack(new SocketService.OnSocketCallBack() {
                @Override
                public void heartCallback(String heartContent) {
                    retryCountLocal = 0;
                }

                @Override
                public void messageCallback(String key, String content) {
                    if (!TextUtils.isEmpty(key))
                        socketTimeoutMap.remove(key);
                    if (mCallList.size() > 0) {
                        for (OnMessageCallBack callBack : mCallList) {
                            callBack.messageCallback(content);
                        }
                    }
                }

                @Override
                public void errorCallback(int errCode) {
                    LogUtil.i("kkkkkkkerror", errCode + "__");
                    socketStatus = 3;
                    EventBus.getDefault().post(new SocketStatusBus(3));

                    retryLinkSocket(retry_time);

                }

                @Override
                public void messageChangeCallback(int current) {
                    LogUtil.i("appkkk", mCurrent + "_");
                    if (mCurrent == 0) {
                        mCurrent = current - 1;
                    }
                    getNotify();
                }

                @Override
                public void sendToServeCallback(String key, int status, int a, int chatId) {
                    if (status == 0)
                        socketTimeoutMap.remove(key);
                    if (mCallList.size() > 0) {
                        for (OnMessageCallBack callBack : mCallList) {
                            callBack.sendToServeCallback(key, status, a, chatId);
                        }
                    }
                }
            });
            service.connet();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtil.i("cccccccccservice dead");
            service = null;
            socketStatus = -1;
            EventBus.getDefault().post(new SocketStatusBus(-1));
            connectSocket(SocketService.HOST, SocketService.PORT + "", SocketService.HEART_BEAT_RATE);
        }

    }

    private void retryLinkSocket(int time) {
        LogUtil.i("socketlinktimes", "触发连接方法");
        if (addressDisposable != null)
            addressDisposable.dispose();
        addressDisposable = Flowable.timer(retryCountLocal <= retry_count ? time : retry_time_max, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                retryCountLocal++;
                LogUtil.i("socketlinktimes", "获取ip地址方法");
                getIpAddress();
                addressDisposable.dispose();
            }
        });
    }

    class NetChangeReceiver extends BroadcastReceiver {
        private ConnectivityManager mConnectivityManager;
        private NetworkInfo netInfo;

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                netInfo = mConnectivityManager.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isAvailable()) {
                    //网络连接
                    String name = netInfo.getTypeName();
                    if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {//WiFi网络
                        netStatus = 0;


                    } else if (netInfo.getType() == ConnectivityManager.TYPE_ETHERNET) {///有线网络


                    } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {//移动网络
                        netStatus = 1;
                    }
                    resetReTry();
                } else {
                    netStatus = 2;
                }
                LogUtil.i("networkstatus:", netStatus + "");
            }
        }
    }

    public static class MyHandler extends Handler {
        private WeakReference<App> mapp;

        public MyHandler(App application) {
            mapp = new WeakReference<>(application);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 100://token
                    if (mapp.get() != null)
                        mapp.get().refreshToken();
                    break;
                case 101://上传视频的secret
                    if (mapp.get() != null)
                        mapp.get().getVideoSecret();
                    break;
            }
        }
    }

    public void resetReTry() {
        if (socketStatus == 3 || socketStatus == 4) {
            retryCountLocal = 0;
            retryLinkSocket(0);
        }
    }

    public BoxStore getmBoxStore() {
        return mBoxStore;
    }

    public interface OnMessageCallBack {
//        void heartCallback(String heartContent);

        void messageCallback(String content);

        void errorCallback(int errCode);

        void sendToServeCallback(String key, int status, int a, int chatId);//status:0:写入失败；1;写入成功；2：连接超时

    }


    public void registerSocketListen(OnMessageCallBack callBack) {
        mCallList.add(callBack);
    }

    public void unregisterSocketListen(OnMessageCallBack callBack) {
        mCallList.remove(callBack);
    }
}
