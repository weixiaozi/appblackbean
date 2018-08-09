package bbs.com.xinfeng.bbswork.ui.service;

import android.app.Activity;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.lang.reflect.Field;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.base.Constant;
import bbs.com.xinfeng.bbswork.domin.DownLoadBean;
import bbs.com.xinfeng.bbswork.domin.ErrorBean;
import bbs.com.xinfeng.bbswork.domin.Progress;
import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.presenter.UpdataApkPresenter;
import bbs.com.xinfeng.bbswork.utils.LogUtil;
import bbs.com.xinfeng.bbswork.utils.NotificationUtils;
import bbs.com.xinfeng.bbswork.utils.ToastUtil;

import static bbs.com.xinfeng.bbswork.base.Constant.AUTHORITY;


/**
 * Created by dell on 2017/11/22.
 */

public class UpdateApkService extends IntentService implements NetContract.INetView {
    public static final int DOWNAPK_TAG = 1;
    private boolean isFocusUpdate;
    private UpdataApkPresenter updataApkPresenter;
    private int where;
    private NotificationUtils notificationUtils;

    public UpdateApkService() {
        super("updateApkService");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(Integer i) {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificationUtils = new NotificationUtils(getApplicationContext());
        EventBus.getDefault().register(this);
        updataApkPresenter = new UpdataApkPresenter(this);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String downUrl = intent.getStringExtra("downurl");
        isFocusUpdate = intent.getBooleanExtra("isFocusUpdate", false);
        where = intent.getIntExtra("where", 0);

        showLoading(DOWNAPK_TAG);
        updataApkPresenter.downloadApk(downUrl, Constant.STORAGE_PATH, "blackbean.apk", DOWNAPK_TAG);
    }

    @Override
    public void showLoading(int tag) {
        if (!isFocusUpdate) {
            showNotification();
        }
    }

    @Override
    public void hideLoading(int tag) {

    }

    @Override
    public void showError(ErrorBean errorBean, int tag) {
        LogUtil.i("aaaaa" + errorBean.desc);
        Progress p = new Progress(0, where);
        p.isError = true;
        EventBus.getDefault().post(p);
    }

    @Override
    public Activity provideActivity() {
        return null;
    }

    @Override
    public void setData(ErrorBean errorBean, int tag) {
        switch (tag) {
            case DOWNAPK_TAG:
                if (errorBean instanceof DownLoadBean) {
                    DownLoadBean downLoadBean = (DownLoadBean) errorBean;

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), AUTHORITY, new File(downLoadBean.getPath()));
                        intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                        //兼容8.0
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            boolean hasInstallPermission = getApplicationContext().getPackageManager().canRequestPackageInstalls();
                            if (!hasInstallPermission) {
                                ToastUtil.showToast("请允许安装未知来源");
                                startInstallPermissionSettingActivity();
                                return;
                            }
                        }
                    } else {
                        intent.setDataAndType(Uri.fromFile(new File(downLoadBean.getPath())), "application/vnd.android.package-archive");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    }
                    if (getApplicationContext().getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
                        getApplicationContext().startActivity(intent);
                    }

/*
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (Build.VERSION.SDK_INT >= 24) {
                        Uri uriForFile = FileProvider.getUriForFile(getApplicationContext(), AUTHORITY, new File(downLoadBean.getPath()));
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.setDataAndType(uriForFile, "application/vnd.android.package-archive");
                    } else {
                        intent.setDataAndType(Uri.fromFile(new File(downLoadBean.getPath())), "application/vnd.android.package-archive");
                    }
                    getApplication().startActivity(intent);*/
                }
                break;
        }
    }

    /**
     * 跳转到设置-允许安装未知来源-页面
     */
    private void startInstallPermissionSettingActivity() {
        //注意这个是8.0新API
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
    }

    @Override
    public void progress(int precent, int tag) {
        if (!isFocusUpdate) {
            if (notificationUtils != null)
                notificationUtils.sendNotification("升级中..." + precent + "%", 0);
            if (precent == 100)
                EventBus.getDefault().post(new Progress(precent, where));
        } else {
            EventBus.getDefault().post(new Progress(precent, where));
        }
    }

    private void showNotification() {
        if (notificationUtils != null)
            notificationUtils.sendNotification("升级中...0%", 0);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        updataApkPresenter.destory();
        notificationUtils.cancel();
        EventBus.getDefault().unregister(this);
    }
}
