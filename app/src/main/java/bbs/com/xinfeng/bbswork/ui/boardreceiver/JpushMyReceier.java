package bbs.com.xinfeng.bbswork.ui.boardreceiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import bbs.com.xinfeng.bbswork.base.App;
import bbs.com.xinfeng.bbswork.domin.BackPrivateChatBus;
import bbs.com.xinfeng.bbswork.ui.activity.MainActivity;
import bbs.com.xinfeng.bbswork.ui.activity.NotificationActivity;
import bbs.com.xinfeng.bbswork.ui.activity.PrivateChatActivity;
import bbs.com.xinfeng.bbswork.ui.activity.SystemNotifyActivity;
import bbs.com.xinfeng.bbswork.ui.activity.ThemeDetailActivity;
import bbs.com.xinfeng.bbswork.ui.activity.WelcomeActivity;
import bbs.com.xinfeng.bbswork.ui.fragment.MessageFragment;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.data.JPushLocalNotification;

import static bbs.com.xinfeng.bbswork.base.App.appCount;
import static bbs.com.xinfeng.bbswork.ui.activity.ChattingActivity.isChatting;
import static bbs.com.xinfeng.bbswork.ui.activity.MainActivity.currentPostion;
import static bbs.com.xinfeng.bbswork.ui.activity.MainActivity.isStrart;
import static bbs.com.xinfeng.bbswork.ui.activity.PrivateChatActivity.isPrivateChatting;

/**
 * Created by dell on 2018/3/1.
 */

public class JpushMyReceier extends BroadcastReceiver {
    final String TAG = "MyReceiver";
    private static int notifyId = 112111;
    NotificationManager nm;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (null == nm) {
            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        Bundle bundle = intent.getExtras();
        Log.d(TAG, "onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String title = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "JPush用户注册成功" + title);
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "接受到推送下来的自定义消息");
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            if (appCount == 1) {//在前台有震动
                try {
                    JSONObject extrasJson = new JSONObject(extras);
                    int mType = extrasJson.optInt("type");
                    if (mType == 313 && currentPostion == 2) {

                    } else if (!((isChatting && mType == 301) || (isPrivateChatting && mType == 501))) {//除了在发观点页面且有type=301以外都震动
                        Vibrator vibrator = (Vibrator) App.getInstance().getSystemService(App.getInstance().VIBRATOR_SERVICE);
                        vibrator.vibrate(400);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else if (appCount <= 0) {//在后台有通知
                try {
                    JSONObject extrasJson = new JSONObject(extras);
                    int mType = extrasJson.optInt("type");
                    switch (mType) {
                        case 100:
                        case 101:
                        case 104:
                        case 111:
                        case 112:
                        case 113:
                        case 201:
                        case 211:
                        case 212:
                        case 301:
                        case 311:
                        case 313:
                        case 321:
                        case 312:
                        case 401:
                        case 402:
                        case 501:
                            String title = bundle.getString(JPushInterface.EXTRA_TITLE);
                            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);

                            JPushLocalNotification ln = new JPushLocalNotification();
                            ln.setBuilderId(2);
                            ln.setTitle(title);
                            ln.setContent(message);
                            notifyId++;
                            ln.setNotificationId(notifyId);
                            ln.setExtras(extras);
                            JPushInterface.addLocalNotification(App.getInstance(), ln);
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "接受到推送下来的通知");

            receivingNotification(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "用户点击打开了通知");
            receivingNotification(context, bundle);
            openNotification(context, bundle);

        } else {
            Log.d(TAG, "Unhandled intent - " + intent.getAction());
        }
    }

    private void receivingNotification(Context context, Bundle bundle) {
        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        Log.d(TAG, " title : " + title);
        String message = bundle.getString(JPushInterface.EXTRA_ALERT);
        Log.d(TAG, "message : " + message);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Log.d(TAG, "extras : " + extras);
    }

    private void openNotification(Context context, Bundle bundle) {
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        int mType;
        try {
            JSONObject extrasJson = new JSONObject(extras);
            mType = extrasJson.optInt("type");
            if (isStrart) {
                switch (mType) {
                    case 100:
                        Intent mIntentMain = new Intent(context, MainActivity.class);
                        mIntentMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(mIntentMain);
                        break;
                    case 101:
                    case 104:
                    case 111:
                    case 112:
                    case 113:
                    case 201:
                    case 211:
                    case 212:
                        Intent mIntent = new Intent(context, NotificationActivity.class);
                        mIntent.putExtra("jpushtype", mType);
                        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(mIntent);
                        break;
                    case 301:
                    case 311:
                    case 313:
                    case 321:
                        Intent intent = new Intent(context, ThemeDetailActivity.class);
                        intent.putExtra("topicid", extrasJson.optInt("topic_id"));
                        intent.putExtra("themeid", extrasJson.optInt("thread_id"));
                        intent.putExtra("jpushtype", mType);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        break;
                    case 312://二级回复
                        Intent intent3 = new Intent(context, ThemeDetailActivity.class);
                        intent3.putExtra("topicid", extrasJson.optInt("topic_id"));
                        intent3.putExtra("themeid", extrasJson.optInt("thread_id"));
                        intent3.putExtra("replyid", extrasJson.optInt("post_id"));
                        intent3.putExtra("jpushtype", mType);
                        intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent3);
                        break;
                    case 401:
                        Intent intent1 = new Intent(context, SystemNotifyActivity.class);
                        intent1.putExtra("noticetype", 1);
                        intent1.putExtra("jpushtype", mType);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent1);
                        break;
                    case 402:
                        Intent intent4 = new Intent(context, SystemNotifyActivity.class);
                        intent4.putExtra("noticetype", 2);
                        intent4.putExtra("jpushtype", mType);
                        intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent4);
                        break;
                    case 501:
                        MessageFragment.ResumeChatId = 0;
                        EventBus.getDefault().post(new BackPrivateChatBus(0));
                        Intent intent2 = new Intent(context, PrivateChatActivity.class);
                        intent2.putExtra("userid", extrasJson.optInt("user_id"));
                        intent2.putExtra("username", extrasJson.optString("user_name"));
                        intent2.putExtra("chatid", extrasJson.optInt("chat_id"));
                        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent2);
                        break;
                }
            } else {
                Intent mIntent = new Intent(context, WelcomeActivity.class);
                mIntent.putExtras(bundle);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(mIntent);
            }
        } catch (Exception e) {
            Log.w(TAG, "Unexpected: extras is not a valid json", e);
            return;
        }

         /*   if (TYPE_THIS.equals(myValue)) {
                Intent mIntent = new Intent(context, ThisActivity.class);
                mIntent.putExtras(bundle);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(mIntent);
            } else if (TYPE_ANOTHER.equals(myValue)){
                Intent mIntent = new Intent(context, AnotherActivity.class);
                mIntent.putExtras(bundle);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(mIntent);
            }*/
    }

    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
//                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
//                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }
}
