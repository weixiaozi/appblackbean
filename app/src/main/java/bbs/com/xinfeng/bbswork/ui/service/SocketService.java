package bbs.com.xinfeng.bbswork.ui.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import bbs.com.xinfeng.bbswork.base.App;
import bbs.com.xinfeng.bbswork.base.Constant;
import bbs.com.xinfeng.bbswork.domin.SocketStatusBus;
import bbs.com.xinfeng.bbswork.utils.LogUtil;
import bbs.com.xinfeng.bbswork.utils.SocketUtil;
import bbs.com.xinfeng.bbswork.utils.SystemUtil;

/**
 * 由于移动设备的网络的复杂性，经常会出现网络断开，如果没有心跳包的检测，
 * 客户端只会在需要发送数据的时候才知道自己已经断线，会延误，甚至丢失服务器发送过来的数据。
 */

public class SocketService extends Service {
    public static final int GRAY_SERVICE_ID = 1001;
    private static final String TAG = "socker_helper";
    public static final int HEART_TAG = 100;
    public static final int NORMALMESSAGE_TAG = 101;
    public static final int auth_TAG = 102;
    public static final int ERR_NET = 200;
    public static final int ERR_ADDR = 201;
    public static final int ERR_TIMEOUT = 202;
    private int bufferSize = 1024;
    private ExecutorService threadPool = Executors.newCachedThreadPool();
    private boolean isSuccess;
    private OnSocketCallBack mCallBack;
    private final IBinder mBinder = new BackServiceBinder();
    //服务器游标（主键）
    private int currentMes;
    /**
     * 心跳频率
     */
    public static int HEART_BEAT_RATE = 5 * 1000;
    /**
     * 服务器ip地址
     */
    public static String HOST = "t1.srv.6rooms.net";
    /**
     * 服务器端口号
     */
    public static int PORT = 32768;
    /**
     * 读线程
     */
    private ReadThread mReadThread;

    private Socket mSocket;

    // For heart Beat
    private MyHandler myHandler = new MyHandler(this);
    private BufferedWriter out;

    /**
     * 心跳任务，不断重复调用自己
     */

    public static class MyHandler extends Handler {
        private WeakReference<SocketService> mService;

        public MyHandler(SocketService service) {
            mService = new WeakReference<>(service);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            SocketService refrence = mService.get();
            if (refrence != null) {
                switch (msg.what) {
                    case HEART_TAG:
                        if (System.currentTimeMillis() - refrence.sendTime >= HEART_BEAT_RATE) {
                            refrence.sendMsg("{" + "\"a\":\"2\"" + "}", null, 2, 0);//就发送一个\r\n过去 如果发送失败，就重新初始化一个socket
                        }
                        refrence.myHandler.sendEmptyMessageDelayed(HEART_TAG, HEART_BEAT_RATE);

                        break;
                    case NORMALMESSAGE_TAG:
                        if (refrence.mCallBack != null)
                            refrence.mCallBack.messageCallback(msg.getData().getString("s"), msg.getData().getString("message"));
                        break;
                    case auth_TAG:
                        //认证接口
                        if (refrence != null) {
                            refrence.myHandler.removeMessages(auth_TAG);
                            refrence.myHandler.sendEmptyMessageDelayed(auth_TAG, 5000);
                            refrence.sendMsg(SocketUtil.formatAuthMessage(Constant.token, SystemUtil.getMAC(refrence)), null, 1, 0);
                            LogUtil.i("authtagsuccess");
                        }
                        break;
                }
            }
        }
    }

    private long sendTime = 0L;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT < 18) {
            startForeground(GRAY_SERVICE_ID, new Notification());//API < 18 ，此方法能有效隐藏Notification上的图标
        } else {
            startForeground(GRAY_SERVICE_ID, new Notification());
            Intent innerIntent = new Intent(this, ProtectService.class);
            startService(innerIntent);
        }

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }

    public class BackServiceBinder extends Binder {
        public SocketService getService() {
            return SocketService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        new InitSocketThread().start();
    }


    public boolean sendMsg(final String msg, String key, int a, int chatId) {
        if (null == mSocket) {
            return false;
        }
        if (!mSocket.isClosed() && !mSocket.isOutputShutdown()) {
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        /*OutputStream os = mSocket.getOutputStream();
                        String message = msg + "\r\n";
                        os.write(message.getBytes());
                        os.flush();*/
                        if (out == null)
                            out = new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream()));
                        out.write(msg + "\r\n");
                        out.flush();

                        isSuccess = true;
                        if (mCallBack != null)
                            mCallBack.sendToServeCallback(key, 1, a, chatId);
                    } catch (IOException e) {
                        isSuccess = false;
                        if (mCallBack != null)
                            mCallBack.sendToServeCallback(key, 0, a, chatId);
                        disConnect();
                        if (mCallBack != null)
                            mCallBack.errorCallback(ERR_NET);
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        isSuccess = false;
                        if (mCallBack != null)
                            mCallBack.sendToServeCallback(key, 0, a, chatId);
                        disConnect();
                        if (mCallBack != null)
                            mCallBack.errorCallback(ERR_NET);
                        e.printStackTrace();
                    }
                }
            });
            sendTime = System.currentTimeMillis();//每次发送成数据，就改一下最后成功发送的时间，节省心跳间隔时间
        } else {
            return false;
        }
        return isSuccess;
    }

    private void initSocket() {//初始化Socket
        try {
            disConnect();
            mSocket = new Socket(HOST, PORT);
            mReadThread = new ReadThread(SocketService.this);
            mReadThread.start();
            myHandler.sendEmptyMessageDelayed(HEART_TAG, HEART_BEAT_RATE);
        } catch (UnknownHostException e) {
            disConnect();
            if (mCallBack != null)
                mCallBack.errorCallback(ERR_ADDR);
            e.printStackTrace();
        } catch (IllegalThreadStateException e) {
            disConnect();
            if (mCallBack != null)
                mCallBack.errorCallback(ERR_ADDR);
            e.printStackTrace();
        } catch (IOException e) {
            disConnect();
            if (mCallBack != null)
                mCallBack.errorCallback(ERR_NET);
            e.printStackTrace();
        }
    }

    public void connet() {
        new InitSocketThread().start();
    }

    private void disConnect() {
        if (mReadThread != null) {
            mReadThread.release();
        } else
            releaseLastSocket();
    }

    public void cutSocket() {
        if (mReadThread != null) {
            mReadThread.release();
        }
    }

    /**
     * 心跳机制判断出socket已经断开后，就销毁连接方便重新创建连接
     *
     * @param
     */
    private synchronized void releaseLastSocket() {
        myHandler.removeCallbacksAndMessages(null);
        try {
            if (null != mSocket) {
                if (!mSocket.isClosed()) {
                    mSocket.close();
                }
                mSocket = null;
            }
            if (out != null) {
                out.close();
                out = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class InitSocketThread extends Thread {
        @Override
        public void run() {
            super.run();
            initSocket();
        }
    }

    // Thread to read content from Socket
    class ReadThread extends Thread {
        private WeakReference<SocketService> mWeakService;
        private boolean isStart = true;
        private InputStream is;

        public ReadThread(SocketService socket) {
            mWeakService = new WeakReference<>(socket);
        }

        public void release() {
            isStart = false;
            releaseLastSocket();
        }

        @Override
        public void run() {
            super.run();
            SocketService service = mWeakService.get();
            if (null != service) {
                try {
                    is = service.mSocket.getInputStream();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1000000);
                    StringBuffer message = new StringBuffer();
                    int totalLength = 0;
                    int length;
                    byte[] buffer = new byte[bufferSize];
                    while (service != null && service.mSocket != null && !service.mSocket.isClosed() && !service.mSocket.isInputShutdown()
                            && isStart) {
                        length = is.read(buffer);
                        if (length != -1) {
                            byteBuffer.put(buffer.clone(), 0, length);
                            totalLength += length;
                            message = message.append(new String(Arrays.copyOf(buffer, length), "utf-8"));
                            if (message.toString().endsWith("\r\n")) {
                                if (totalLength > bufferSize) {
                                    message.delete(0, message.length());
                                    message = message.append(new String(Arrays.copyOf(byteBuffer.array(), totalLength), "utf-8"));
                                }
                                totalLength = 0;
                                byteBuffer.clear();
                                for (String msg : message.toString().split("\r\n")) {
                                    dealMsg(msg);
                                }

                                message.delete(0, message.length());
                            }
                        } else {
                            totalLength = 0;
                            byteBuffer.clear();
                        }


                    }
                    LogUtil.i(TAG, "readthread shutup");
                    release();
                    if (mCallBack != null)
                        mCallBack.errorCallback(ERR_TIMEOUT);
                } catch (Exception e) {
                    disConnect();
                    if (mCallBack != null)
                        mCallBack.errorCallback(ERR_NET);
                    e.printStackTrace();
                } finally {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        private void dealMsg(String message) {
            if (!TextUtils.isEmpty(message)) {
//                            String message = new String(Arrays.copyOf(buffer,
//                                    length), "utf-8").trim();
                Log.e(TAG, message);
                try {
                    JSONObject jsonObject = new JSONObject(message);
                    int a = jsonObject.getInt("a");
                    switch (a) {
                        case 1://认证返回
                            LogUtil.i("socketlinktimes", "认证成功");
                            myHandler.removeMessages(auth_TAG);
                            App.getInstance().socketStatus = 2;
                            EventBus.getDefault().post(new SocketStatusBus(2));
                            break;
                        case 2://心跳
                            if (mCallBack != null)
                                mCallBack.heartCallback(message);
                            break;
                        case 3://推送
                            int current = jsonObject.getInt("current");
                            if (currentMes != current) {
                                currentMes = current;
                                if (mCallBack != null)
                                    mCallBack.messageChangeCallback(currentMes);
                            }
                            break;
                        case 4://发送内容成功
                        case 5://接受文本消息
                        case 8://同步消息
                        case 9://拉取消息
                        case 10://添加会话
                        case 11://撤回消息
                        case 12://删除消息
                        case 13://语音已播
                        case 15://消息更新
                            Message obtain = Message.obtain();
                            obtain.what = NORMALMESSAGE_TAG;
//                            obtain.obj = message;
                            Bundle bundle = new Bundle();
                            bundle.putString("message", message);
                            try {
                                bundle.putString("s", jsonObject.getString("s"));
                            } catch (JSONException e) {

                            }
                            obtain.setData(bundle);
                            myHandler.sendMessage(obtain);
                            break;
                        case 6://建立连接成功
                            LogUtil.i("socketlinktimes", "建立连接成功");
                            myHandler.removeMessages(auth_TAG);
                            myHandler.sendEmptyMessage(auth_TAG);
                            App.getInstance().socketStatus = 1;
                            EventBus.getDefault().post(new SocketStatusBus(1));
                            break;
                        case 7://服务端主动关闭链接
                            App.getInstance().socketStatus = 4;
                            EventBus.getDefault().post(new SocketStatusBus(4));
                            disConnect();
                            if (mCallBack != null)
                                mCallBack.errorCallback(ERR_NET);
                            break;
                    }
                } catch (Exception e) {
                    LogUtil.i("eeeeee", message);
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!threadPool.isShutdown())
            threadPool.shutdown();
        disConnect();
    }

    public interface OnSocketCallBack {
        void heartCallback(String heartContent);

        void messageCallback(String key, String content);

        void errorCallback(int errCode);

        void messageChangeCallback(int current);

        void sendToServeCallback(String key, int status, int a, int chatId);
    }

    public void setOnSocketCallBack(OnSocketCallBack mCallBack) {
        this.mCallBack = mCallBack;
    }


}