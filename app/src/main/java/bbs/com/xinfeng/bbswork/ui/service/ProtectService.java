package bbs.com.xinfeng.bbswork.ui.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import bbs.com.xinfeng.bbswork.utils.LogUtil;

import static bbs.com.xinfeng.bbswork.ui.service.SocketService.GRAY_SERVICE_ID;

/**
 * Created by dell on 2018/3/13.
 */

public class ProtectService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(GRAY_SERVICE_ID, new Notification());
        stopForeground(true);
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
