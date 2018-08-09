package bbs.com.xinfeng.bbswork.ui.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.base.App;
import bbs.com.xinfeng.bbswork.base.BaseActivity;
import bbs.com.xinfeng.bbswork.base.BaseFragment;
import bbs.com.xinfeng.bbswork.base.Constant;
import bbs.com.xinfeng.bbswork.databinding.ActivityMainBinding;
import bbs.com.xinfeng.bbswork.domin.BackPrivateChatBus;
import bbs.com.xinfeng.bbswork.domin.ErrorBean;
import bbs.com.xinfeng.bbswork.domin.JoinPrivateChatBus;
import bbs.com.xinfeng.bbswork.domin.LoginBus;
import bbs.com.xinfeng.bbswork.domin.NavigatorBus;
import bbs.com.xinfeng.bbswork.domin.NoticeChangeBean;
import bbs.com.xinfeng.bbswork.domin.PrivateBackRunBus;
import bbs.com.xinfeng.bbswork.domin.PrivateChatItemBean;
import bbs.com.xinfeng.bbswork.domin.PrivateChatItemBean_;
import bbs.com.xinfeng.bbswork.domin.Progress;
import bbs.com.xinfeng.bbswork.domin.SendPrivateMesBean;
import bbs.com.xinfeng.bbswork.domin.SendToServerIsSuccessBean;
import bbs.com.xinfeng.bbswork.domin.SocketStatusBus;
import bbs.com.xinfeng.bbswork.domin.UpgradeBean;
import bbs.com.xinfeng.bbswork.domin.UploadBean;
import bbs.com.xinfeng.bbswork.domin.VideoInfoPackage;
import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.model.MainModel;
import bbs.com.xinfeng.bbswork.mvp.presenter.MainPresenter;
import bbs.com.xinfeng.bbswork.ui.adapter.MainFragmentAdapter;
import bbs.com.xinfeng.bbswork.ui.fragment.MessageFragment;
import bbs.com.xinfeng.bbswork.ui.fragment.HomeFragment;
import bbs.com.xinfeng.bbswork.ui.fragment.CollectionFragment;
import bbs.com.xinfeng.bbswork.ui.fragment.MineFragment;
import bbs.com.xinfeng.bbswork.utils.ImageUtil;
import bbs.com.xinfeng.bbswork.utils.LogUtil;
import bbs.com.xinfeng.bbswork.utils.RxUtil;
import bbs.com.xinfeng.bbswork.utils.SharedPrefUtil;
import bbs.com.xinfeng.bbswork.utils.SystemUtil;
import bbs.com.xinfeng.bbswork.utils.ToastUtil;
import bbs.com.xinfeng.bbswork.utils.UpgradeUtil;
import bbs.com.xinfeng.bbswork.utils.shortvideo.choose.FileUtils;
import bbs.com.xinfeng.bbswork.utils.shortvideo.choose.TCConstants;
import bbs.com.xinfeng.bbswork.videoupload.TXUGCPublish;
import bbs.com.xinfeng.bbswork.videoupload.TXUGCPublishTypeDef;
import cn.jpush.android.api.JPushInterface;
import io.objectbox.Box;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import static android.view.KeyEvent.KEYCODE_BACK;
import static bbs.com.xinfeng.bbswork.mvp.model.MainModel.SOCKET_MES_TAG;
import static bbs.com.xinfeng.bbswork.utils.UpgradeUtil.where_main;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainPresenter> implements NetContract.INetView, TXUGCPublishTypeDef.ITXVideoPublishListener {
    private final int upgradeCheck_tag = 1;
    private final int upgradeCheck_socket_tag = 2;
    private final int uploadpicone_realSend_TAG = 801;
    private static final int handler_TIMEOUT = 100;
    private boolean canClose = false;
    private UpgradeUtil upgradeUtil;
    public static boolean isStrart;
    private MyReceiver mReceiver;
    public static LinkedHashMap<String, PrivateChatItemBean> waitSendDatas = new LinkedHashMap<>();
    public static HashMap<String, String> picMap = new HashMap();
    public static HashMap<String, VideoInfoPackage> audioMap = new HashMap();
    private int socketStatus;
    private TXUGCPublish mVideoPublish;
    private static MainActivity mMainActivity;
    private Box<PrivateChatItemBean> box;
    public static HashMap<Integer, Integer> openPrivateChats = new HashMap<>();//打开过的私聊界面
    private int authorId;
    //    private MyHandler mHandler = new MyHandler(this);
    public static int currentPostion;

    public static MainActivity getInstance() {
        return mMainActivity;
    }

    public void putWaitData(PrivateChatItemBean data) {
        waitSendDatas.put(data.getTime() + "", data);
        if (waitSendDatas.size() <= 1) {
            sendNext();
        }
    }

    private void realPublish(PrivateChatItemBean readyInfo) {
        int type = readyInfo.getMt();
        if (type == 1) {//文本
            if (socketStatus == 2) {
                String replace = readyInfo.getC1().toString()
                        .replace("\\", "\\\\")
                        .replace("\n", "\\n")
                        .replace("\b", "\\b")
                        .replace("\f", "\\f")
                        .replace("\r", "\\r")
                        .replace("\t", "\\t");
                mPresenter.sendRealMes(readyInfo.getChatId(), 1, replace, readyInfo.getTime(), readyInfo.isHasRunSend());
//                mHandler.sendEmptyMessageDelayed(handler_TIMEOUT, 10000);
            } else {
                updataLocalDatas(readyInfo.getChatId(), readyInfo.getTime(), 0, null);
                waitSendDatas.remove(readyInfo.getTime() + "");
                sendNext();
            }
        } else if (type == 2) {//图片
            if (updataPicMapReal(readyInfo.getC1(), readyInfo.getTime(), uploadpicone_realSend_TAG)) {
                readyInfo.setWebPic(picMap.get(readyInfo.getC1()));
                if (socketStatus == 2) {
                    mPresenter.sendRealMes(readyInfo.getChatId(), 2, readyInfo.getWebPic(), readyInfo.getTime(), readyInfo.isHasRunSend());
//                    mHandler.sendEmptyMessageDelayed(handler_TIMEOUT, 10000);
                } else {
                    updataLocalDatas(readyInfo.getChatId(), readyInfo.getTime(), 0, null);
                    waitSendDatas.remove(readyInfo.getTime() + "");
                    sendNext();
                }
            }
        } else if (type == 3) {//语音
            if (updataAudioMap(2, readyInfo.getC1(), null, readyInfo.getDuration(), readyInfo.getTime() + "")) {
                readyInfo.setWebPic(audioMap.get(readyInfo.getC1()).webUrl);
                if (socketStatus == 2) {
                    mPresenter.sendRealMes(readyInfo.getChatId(), 3, readyInfo.getWebPic(), readyInfo.getTime(), readyInfo.isHasRunSend());
//                    mHandler.sendEmptyMessageDelayed(handler_TIMEOUT, 10000);
                } else {
                    updataLocalDatas(readyInfo.getChatId(), readyInfo.getTime(), 0, null);
                    waitSendDatas.remove(readyInfo.getTime() + "");
                    sendNext();
                }
            }
        } else if (type == 4) {//视频

        }
    }

    //选择后上传图片
    private boolean updataPicMapReal(String upList, long terminal_id, int tag) {
        boolean isFinish = true;
        String web = MainActivity.picMap.get(upList);
        if (TextUtils.isEmpty(web)) {
            isFinish = false;
            uploadPicOneReal(upList, terminal_id, tag);
        }
        return isFinish;
    }

    private void uploadPicOneReal(String localpath, long terminal_id, int tag) {
        mDisposables.add(Flowable.just(localpath).map(new Function<String, File>() {
            @Override
            public File apply(String path) throws Exception {
                File newFile = new File(getExternalCacheDir(), "thememain" + ".jpg");
                if (ImageUtil.compressBmpToFile(provideActivity(), path, newFile))
                    return newFile;
                else
                    return new File(path);
            }
        }).compose(RxUtil.fixScheduler()).subscribe(new Consumer<File>() {
            @Override
            public void accept(File file) throws Exception {
                startUploadOneReal(file, localpath, terminal_id, tag);
            }
        }));
    }

    private void startUploadOneReal(File file, String localpath, long terminal_id, int tag) {
        mPresenter.uploadOneReal(file, localpath, terminal_id, tag);
    }


    //上传音视频
    private boolean updataAudioMap(int videoType, String videoPath, String coverPath, int duration, String time) {
        boolean isFinish = true;
        VideoInfoPackage web = MainActivity.audioMap.get(videoPath);
        if (web == null) {
            web = new VideoInfoPackage(videoType, videoPath, coverPath, duration);
            MainActivity.audioMap.put(videoPath, web);
        }
        if (TextUtils.isEmpty(web.webUrl)) {
            isFinish = false;
            uploadVideoOne(videoType, videoPath, coverPath, duration, time);
        }
        return isFinish;
    }

    private void uploadVideoOne(int videoType, String videoPath, String coverPath, int duration, String time) {
        if (mVideoPublish == null) {
            mVideoPublish = new TXUGCPublish(provideActivity().getApplicationContext(), "blackbean");
            mVideoPublish.setListener(this);
        }
        TXUGCPublishTypeDef.TXPublishParam param = new TXUGCPublishTypeDef.TXPublishParam();
        // signature计算规则可参考 https://www.qcloud.com/document/product/266/9221
        param.signature = Constant.videoSecret;
        param.videoPath = videoPath;
        param.coverPath = coverPath;
        param.duration = duration;
        param.key = time;
        param.type = videoType;
        int publishCode = mVideoPublish.publishVideo(param);
        if (publishCode == 1012) {
            TXUGCPublishTypeDef.TXPublishResult result = new TXUGCPublishTypeDef.TXPublishResult();
            result.retCode = publishCode;
            result.localPath = videoPath;
            result.duration = duration;
            result.key = time;
            onPublishComplete(result);
        }
        LogUtil.i("publisherror", "发布失败，错误码：" + publishCode);
    }

    @Override
    public void showLoading(int tag) {

    }

    @Override
    public void hideLoading(int tag) {

    }

    @Override
    public void showError(ErrorBean errorBean, int tag) {
        if (errorBean.androidType.equals(ErrorBean.TYPE_SHOW)) {
            ToastUtil.showToast(errorBean.desc);
        }
        if (tag == MainModel.SOCKET_MES_TAG) {
            if (errorBean instanceof SendPrivateMesBean) {
                if (((SendPrivateMesBean) errorBean).getCode() == 160006) {//拉黑
                    PrivateChatItemBean itemBean = waitSendDatas.get(((SendPrivateMesBean) errorBean).getS());
                    if (itemBean != null) {
                        updataLocalDatas(itemBean.getChatId(), itemBean.getTime(), 0, ((SendPrivateMesBean) errorBean).getMessage());
                        waitSendDatas.remove(((SendPrivateMesBean) errorBean).getS());
                    }
                    sendNext();
                } else if (((SendPrivateMesBean) errorBean).getCode() == 160007) {//发送重复
                    PrivateChatItemBean itemBean = waitSendDatas.get(((SendPrivateMesBean) errorBean).getS());
                    if (itemBean != null) {
                        updataLocalDatas(itemBean.getChatId(), itemBean.getTime(), 1, ((SendPrivateMesBean) errorBean).getData().getId());
                        waitSendDatas.remove(((SendPrivateMesBean) errorBean).getS());
                    }
                    sendNext();
                } else {
                    PrivateChatItemBean itemBean = waitSendDatas.get(((SendPrivateMesBean) errorBean).getS());
                    if (itemBean != null) {
                        updataLocalDatas(itemBean.getChatId(), itemBean.getTime(), 0, null);
                        waitSendDatas.remove(((SendPrivateMesBean) errorBean).getS());
                    }
                    sendNext();
                }
            }
        } else if (tag == uploadpicone_realSend_TAG) {
            PrivateChatItemBean itemBean = waitSendDatas.get(errorBean.key);
            if (itemBean != null) {
                updataLocalDatas(itemBean.getChatId(), itemBean.getTime(), 0, null);
                waitSendDatas.remove(errorBean.key);
            }
            sendNext();
        }
    }

    @Override
    public void setData(ErrorBean errorBean, int tag) {
        switch (tag) {
            case upgradeCheck_tag:
                UpgradeBean upgradeBean = (UpgradeBean) errorBean;
                if (Integer.parseInt(upgradeBean.getVersion()) > SystemUtil.getAppVersion()) {
                    if (upgradeBean.isUpgrade()) {
                        startActivity(new Intent(provideActivity(), MainActivity.class));
                        if (upgradeUtil == null)
                            upgradeUtil = new UpgradeUtil(provideActivity());
                        upgradeUtil.checkUpgrade(upgradeBean, where_main);
                    }
                }

                break;
            case upgradeCheck_socket_tag:
                UpgradeBean upBean = (UpgradeBean) errorBean;
                if (Integer.parseInt(upBean.getVersion()) > SystemUtil.getAppVersion()) {
                    if (upBean.isUpgrade())
                        startActivity(new Intent(provideActivity(), MainActivity.class));
                    if (upgradeUtil == null)
                        upgradeUtil = new UpgradeUtil(provideActivity());
                    upgradeUtil.checkUpgrade(upBean, where_main);
                }

                break;
            case uploadpicone_realSend_TAG:
                if (errorBean instanceof UploadBean) {
                    UploadBean bean = (UploadBean) errorBean;
                    MainActivity.picMap.put(errorBean.localPath, bean.getSrc());
                    if (waitSendDatas.get(errorBean.key) != null) {
                        if (updataPicMapReal(waitSendDatas.get(errorBean.key).getC1(), Long.parseLong(errorBean.key), uploadpicone_realSend_TAG))
                            realPublish(waitSendDatas.get(errorBean.key));
                        break;
                    }
                }
                break;
            case SOCKET_MES_TAG:
                runOnUiThread(() -> {
                    if (errorBean instanceof SendToServerIsSuccessBean) {
                        SendToServerIsSuccessBean bean = (SendToServerIsSuccessBean) errorBean;
                        if (bean.getA() == 4) {
                            PrivateChatItemBean itemBean = waitSendDatas.get(bean.getKey());
                            if (itemBean != null) {
                                if (bean.getStatus() == 1) {
                                    itemBean.setHasRunSend(true);
                                    updataLocalDatas(itemBean.getChatId(), itemBean.getTime(), 2, null);
                                } else {
                                    updataLocalDatas(itemBean.getChatId(), itemBean.getTime(), 0, "");
                                    waitSendDatas.remove(itemBean.getTime() + "");
                                    sendNext();
                                }
                            }
                        }

                    }
                });

                if (errorBean instanceof SendPrivateMesBean) {//发送消息成功
                    SendPrivateMesBean bean = (SendPrivateMesBean) errorBean;
                    PrivateChatItemBean itemBean = waitSendDatas.get(bean.getS());
                    if (itemBean != null) {
                        updataLocalDatas(itemBean.getChatId(), itemBean.getTime(), 1, bean.getData().getId());
                        waitSendDatas.remove(bean.getS());
                        sendNext();
                    }
                }
                break;
        }
    }

    @Override
    public void progress(int precent, int tag) {

    }

    @Override
    protected MainPresenter creatPresenter() {
        return new MainPresenter(this);
    }

    @Override
    protected void initEvent() {
        mBinding.rbtnMain1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.viewpagerMain.setCurrentItem(0, false);
                updataButtonCheck(0);
            }
        });
        mBinding.rbtnMain2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.viewpagerMain.setCurrentItem(1, false);
                updataButtonCheck(1);
            }
        });
        mBinding.rbtnMain3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.viewpagerMain.setCurrentItem(2, false);
                updataButtonCheck(2);
            }
        });
        mBinding.rbtnMain4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.viewpagerMain.setCurrentItem(3, false);
                updataButtonCheck(3);
            }
        });

    }

    public int getCurrentPostion() {
        return currentPostion;
    }

    private void updataButtonCheck(int postion) {
        currentPostion = postion;
        mBinding.rbtnMain1.setSelected(false);
        mBinding.rbtnMain2.setSelected(false);
        mBinding.rbtnMain3.setSelected(false);
        mBinding.rbtnMain4.setSelected(false);
        if (postion == 0) {
            mBinding.rbtnMain1.setSelected(true);
        } else if (postion == 1) {
            mBinding.rbtnMain2.setSelected(true);
        } else if (postion == 2) {
            mBinding.rbtnMain3.setSelected(true);
            mBinding.ivHomeNew3.setVisibility(View.GONE);
        } else if (postion == 3) {
            mBinding.rbtnMain4.setSelected(true);
        }
    }

    @Override
    protected int initView(Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        this.mMainActivity = this;
        isStrart = true;
        authorId = SharedPrefUtil.getInt(Constant.userid_key, 0);
        initReceiver();
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        if (audioManager.isSpeakerphoneOn()) {
            App.getInstance().mIsEarPhoneOn = false;
            audioManager.setSpeakerphoneOn(true);
            audioManager.setMode(AudioManager.MODE_NORMAL);
        } else {
            App.getInstance().mIsEarPhoneOn = true;
            audioManager.setSpeakerphoneOn(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            } else {
                audioManager.setMode(AudioManager.MODE_IN_CALL);
            }
        }
        List<BaseFragment> fragments = new ArrayList<>();
        HomeFragment homeFragment = new HomeFragment();
        MessageFragment messageFragment = new MessageFragment();
        CollectionFragment collectionFragment = new CollectionFragment();
        MineFragment mineFragment = new MineFragment();

        fragments.add(homeFragment);
        fragments.add(messageFragment);
        fragments.add(collectionFragment);
        fragments.add(mineFragment);
        mBinding.viewpagerMain.setAdapter(new MainFragmentAdapter(getSupportFragmentManager(), fragments));
        mBinding.viewpagerMain.setCurrentItem(0);
        currentPostion = 0;
        mBinding.rbtnMain1.setSelected(true);
        mBinding.viewpagerMain.setOffscreenPageLimit(4);
        mBinding.viewpagerMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updataButtonCheck(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        new RxPermissions(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(aBoolean -> {
            if (aBoolean) {
                mPresenter.upgradeCheck(upgradeCheck_tag);
            }
        });

        App.getInstance().lazyConnectSocket();
        dealNotify();
//        copyLicenceToSdcard();
        socketStatus = App.getInstance().socketStatus;

    }

    private void dealNotify() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            if (!TextUtils.isEmpty(extras)) {
                try {
                    JSONObject extrasJson = new JSONObject(extras);
                    int mType = extrasJson.optInt("type");
                    switch (mType) {
                        case 101:
                        case 104:
                        case 111:
                        case 112:
                        case 113:
                        case 201:
                        case 211:
                        case 212:
                            Intent mIntent = new Intent(provideActivity(), NotificationActivity.class);
                            mIntent.putExtra("jpushtype", mType);
                            startActivity(mIntent);
                            break;
                        case 301:
                        case 311:
                        case 313:
                        case 321:
                            Intent intent = new Intent(provideActivity(), ThemeDetailActivity.class);
                            intent.putExtra("topicid", extrasJson.optInt("topic_id"));
                            intent.putExtra("themeid", extrasJson.optInt("thread_id"));
                            intent.putExtra("jpushtype", mType);
                            startActivity(intent);
                            break;
                        case 312://二级回复
                            Intent intent3 = new Intent(provideActivity(), ThemeDetailActivity.class);
                            intent3.putExtra("topicid", extrasJson.optInt("topic_id"));
                            intent3.putExtra("themeid", extrasJson.optInt("thread_id"));
                            intent3.putExtra("replyid", extrasJson.optInt("post_id"));
                            intent3.putExtra("jpushtype", mType);
                            startActivity(intent3);
                            break;
                        case 401:
                            Intent intent1 = new Intent(provideActivity(), SystemNotifyActivity.class);
                            intent1.putExtra("noticetype", 1);
                            intent1.putExtra("jpushtype", mType);
                            startActivity(intent1);
                            break;
                        case 402:
                            Intent intent2 = new Intent(provideActivity(), SystemNotifyActivity.class);
                            intent2.putExtra("noticetype", 2);
                            intent2.putExtra("jpushtype", mType);
                            startActivity(intent2);
                            break;
                        case 501:
                            MessageFragment.ResumeChatId = 0;
                            EventBus.getDefault().post(new BackPrivateChatBus(0));
                            Intent intent4 = new Intent(provideActivity(), PrivateChatActivity.class);
                            intent4.putExtra("userid", extrasJson.optInt("user_id"));
                            intent4.putExtra("username", extrasJson.optString("user_name"));
                            intent4.putExtra("chatid", extrasJson.optInt("chat_id"));
                            startActivity(intent4);
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KEYCODE_BACK)) {
            if (!canClose) {
                ToastUtil.showToast("再按一次退出应用");
                canClose = true;
                mDisposables.add(Flowable.timer(3, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        canClose = false;
                    }
                }));
                return true;
            }
            return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(LoginBus bus) {
        if (!bus.isLoginSuccess) {
            Intent intent = new Intent(provideActivity(), LoginActivity.class);
            intent.putExtra("from", "welcome");
            startActivity(intent);
            finish();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(Progress p) {
        if (upgradeUtil != null && p.where == where_main)
            upgradeUtil.updataProgress(p);
    }

    public static final int First_unread_news = 288;
    public static final int Second_unread_news = 289;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(NoticeChangeBean.DataBean e) {
        if (e.getTypeX() == 16 || e.getTypeX() == 19 || e.getTypeX() == 17 || e.getTypeX() == First_unread_news) {//16：话题新增 19：主题新增 17:话题修改
            mBinding.ivHomeNew1.setVisibility(View.VISIBLE);
        } else if ((e.getTypeX() >= 11 && e.getTypeX() <= 15) || e.getTypeX() == Second_unread_news) {
            mBinding.ivHomeNew2.setVisibility(View.VISIBLE);
        } else if (e.getTypeX() == 29) {//收藏消息变动自定义type
            if (mBinding.viewpagerMain.getCurrentItem() != 2)
                mBinding.ivHomeNew3.setVisibility(View.VISIBLE);
        } else if (e.getTypeX() == 28) {//版本检测
            new RxPermissions(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(aBoolean -> {
                if (aBoolean) {
                    mPresenter.upgradeCheck(upgradeCheck_socket_tag);
                }
            });
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventUnreadNew(NavigatorBus p) {
        switch (p.position) {
            case 0:
                mBinding.ivHomeNew1.setVisibility(View.GONE);
                break;
            case 1:
                mBinding.ivHomeNew2.setVisibility(View.GONE);
                break;
            case 2:
                mBinding.ivHomeNew3.setVisibility(View.GONE);
                break;
            case 3:
                mBinding.ivHomeNew4.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected boolean isBindEventBus() {
        return true;
    }

    @Override
    protected void onDestroy() {
        isStrart = false;
        App.getInstance().disConnectSocket();
        unregisterReceiver(mReceiver);
        if (mVideoPublish != null) {
            mVideoPublish.canclePublish();
            mVideoPublish.setListener(null);
        }
        waitSendDatas.clear();
        picMap.clear();
        audioMap.clear();
        openPrivateChats.clear();
//        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    private void copyLicenceToSdcard() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String sdcardFolder = getExternalFilesDir(null).getAbsolutePath();
                File sdcardLicenceFile = new File(sdcardFolder + File.separator + TCConstants.UGC_LICENCE_NAME);
                if (sdcardLicenceFile.exists()) {
                    return;
                }
                try {
                    FileUtils.copyFromAssetToSdcard(provideActivity(), TCConstants.UGC_LICENCE_NAME, sdcardFolder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // 监听耳机插入
    private void initReceiver() {
        mReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(mReceiver, filter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SocketStatusBus e) {
        socketStatus = e.status;
    }

    @Override
    public void onPublishProgress(long uploadBytes, long totalBytes) {

    }

    @Override
    public void onPublishComplete(TXUGCPublishTypeDef.TXPublishResult result) {
        PrivateChatItemBean itemBean = waitSendDatas.get(result.key);
        if (result.retCode == 0) {
            if (result.type == 2) {
                VideoInfoPackage videoInfoPackage = MainActivity.audioMap.get(result.localPath);
                if (videoInfoPackage != null) {
                    videoInfoPackage.webUrl = result.videoURL + ";" + result.duration;
                    MainActivity.audioMap.put(result.localPath, videoInfoPackage);
                }
            } else if (result.type == 3) {
                VideoInfoPackage videoInfoPackage = MainActivity.audioMap.get(result.localPath);
                if (videoInfoPackage != null) {
                    videoInfoPackage.webUrl = result.videoURL + ";" + result.coverURL + ";" + result.duration + ";" + result.width + ";" + result.height;
                    MainActivity.audioMap.put(result.localPath, videoInfoPackage);
                }
            }
            if (itemBean != null) {
                realPublish(itemBean);
            } else {
                waitSendDatas.remove(result.key);
                sendNext();
            }
        } else {
            if (itemBean != null)
                updataLocalDatas(itemBean.getChatId(), itemBean.getTime(), 0, null);
            waitSendDatas.remove(result.key);
            sendNext();
        }

        LogUtil.i("publishcomplete", result.retCode + " Msg:" + (result.retCode == 0 ? result.videoURL : result.descMsg));
    }

    //更新数据库
    private void updataLocalDatas(int chatId, long time, int status, String message) {//0:失败；1：成功2：写入完成
        if (box == null) {
            box = App.mApp.getmBoxStore().boxFor(PrivateChatItemBean.class);
        }
        PrivateChatItemBean item = box.query().equal(PrivateChatItemBean_.chatId, chatId).equal(PrivateChatItemBean_.time, time).build().findFirst();
        if (item != null) {
            if (status == 1) {
                PrivateChatItemBean currentItem = box.query().equal(PrivateChatItemBean_.uid_android, authorId).equal(PrivateChatItemBean_.chatId, chatId).equal(PrivateChatItemBean_.mt, 400100).build().findFirst();
                if (currentItem != null) {
                    currentItem.setId(message);
                    box.put(currentItem);
                }
                box.remove(item);
            } else if (status == 0) {
                EventBus.getDefault().post(new PrivateBackRunBus(0, chatId, time, message));
                item.setSelfStatus(2);
//                item.setHasRunSend(false);
                item.setBackRunning(false);
                if (!TextUtils.isEmpty(message))
                    item.setTm(message);
                box.put(item);
            } else if (status == 2) {
                item.setHasRunSend(true);
                box.put(item);
            }
        }
    }

    //加载下一个
    private void sendNext() {
//        mHandler.removeMessages(handler_TIMEOUT);
        Iterator<Map.Entry<String, PrivateChatItemBean>> iterator = waitSendDatas.entrySet().iterator();
        if (iterator.hasNext()) {
            Map.Entry<String, PrivateChatItemBean> next = iterator.next();
            realPublish(next.getValue());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(JoinPrivateChatBus bus) {
        if (bus.chatId != 0) {
            Iterator<Map.Entry<String, PrivateChatItemBean>> iterator = waitSendDatas.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, PrivateChatItemBean> next = iterator.next();
                if (next.getValue().getChatId() == bus.chatId) {
                    iterator.remove();
                }
            }
        }
    }

    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent data) {
            if (data != null) {
                //插入了耳机
                if (data.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
                    int state = data.getIntExtra("state", 0);
                    AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    int currVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

                    if (state == 0) {
                        App.getInstance().mIsEarPhoneOn = false;
                        audioManager.setSpeakerphoneOn(true);
                        audioManager.setMode(AudioManager.MODE_NORMAL);
//                        audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
//                                audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL),
//                                AudioManager.STREAM_VOICE_CALL);
                        Log.i(TAG, "set SpeakerphoneOn true!");
                    } else {
                        App.getInstance().mIsEarPhoneOn = true;
                        audioManager.setSpeakerphoneOn(false);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
                        } else {
                            audioManager.setMode(AudioManager.MODE_IN_CALL);
                        }
//                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currVolume,
//                                AudioManager.STREAM_VOICE_CALL);
                        Log.i(TAG, "set SpeakerphoneOn false!");
                    }
                }
            }
        }

    }

    public static class MyHandler extends Handler {
        private WeakReference<MainActivity> mActivity;

        public MyHandler(MainActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MainActivity context = mActivity.get();
            switch (msg.what) {
                case handler_TIMEOUT:
                    if (mActivity != null) {
                        Iterator<Map.Entry<String, PrivateChatItemBean>> iterator = context.waitSendDatas.entrySet().iterator();
                        if (iterator.hasNext()) {
                            PrivateChatItemBean value = iterator.next().getValue();
                            context.updataLocalDatas(value.getChatId(), value.getTime(), 0, "");
                            context.waitSendDatas.remove(value.getTime() + "");
                            context.sendNext();
                        }
                    }
                    break;
            }
        }
    }
}
