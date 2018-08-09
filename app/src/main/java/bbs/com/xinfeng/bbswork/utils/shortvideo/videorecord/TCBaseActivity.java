package bbs.com.xinfeng.bbswork.utils.shortvideo.videorecord;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import bbs.com.xinfeng.bbswork.utils.shortvideo.choose.TCConstants;
import bbs.com.xinfeng.bbswork.utils.shortvideo.choose.TCUtils;


/**
 * base activity to handle relogin info
 * Created by Administrator on 2016/9/20
 */
public class TCBaseActivity extends Activity {

    private static final String TAG = TCBaseActivity.class.getSimpleName();

    //错误消息弹窗

    //被踢下线广播监听
    private LocalBroadcastManager mLocalBroadcatManager;
    private BroadcastReceiver mExitBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mLocalBroadcatManager.unregisterReceiver(mExitBroadcastReceiver);
    }


}
