package bbs.com.xinfeng.bbswork.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.base.App;
import bbs.com.xinfeng.bbswork.base.BaseActivity;
import bbs.com.xinfeng.bbswork.base.Constant;
import bbs.com.xinfeng.bbswork.databinding.ActivityPrivateChatBinding;
import bbs.com.xinfeng.bbswork.domin.AddChatBean;
import bbs.com.xinfeng.bbswork.domin.BackPrivateChatBus;
import bbs.com.xinfeng.bbswork.domin.ErrorBean;
import bbs.com.xinfeng.bbswork.domin.JoinPrivateChatBus;
import bbs.com.xinfeng.bbswork.domin.MesInfoChangeBean;
import bbs.com.xinfeng.bbswork.domin.PrivateBackRunBus;
import bbs.com.xinfeng.bbswork.domin.PrivateChatItemBean;
import bbs.com.xinfeng.bbswork.domin.PrivateChatItemBean_;
import bbs.com.xinfeng.bbswork.domin.PrivateChatListBean;
import bbs.com.xinfeng.bbswork.domin.PrivateMesChangedBean;
import bbs.com.xinfeng.bbswork.domin.PrivateUserDetailBean;
import bbs.com.xinfeng.bbswork.domin.SendPrivateMesBean;
import bbs.com.xinfeng.bbswork.domin.SendToServerIsSuccessBean;
import bbs.com.xinfeng.bbswork.domin.SocketStatusBus;
import bbs.com.xinfeng.bbswork.domin.UploadBean;
import bbs.com.xinfeng.bbswork.domin.VideoInfoPackage;
import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.model.PrivateChatModel;
import bbs.com.xinfeng.bbswork.mvp.presenter.PrivateChatPresenter;
import bbs.com.xinfeng.bbswork.ui.adapter.PrivateChatAdapter;
import bbs.com.xinfeng.bbswork.ui.fragment.MessageFragment;
import bbs.com.xinfeng.bbswork.utils.ImageUtil;
import bbs.com.xinfeng.bbswork.utils.LogUtil;
import bbs.com.xinfeng.bbswork.utils.RxUtil;
import bbs.com.xinfeng.bbswork.utils.SharedPrefUtil;
import bbs.com.xinfeng.bbswork.utils.ToastUtil;
import bbs.com.xinfeng.bbswork.utils.emoji.EmojiUtil;
import bbs.com.xinfeng.bbswork.videoupload.TXUGCPublish;
import bbs.com.xinfeng.bbswork.videoupload.TXUGCPublishTypeDef;
import bbs.com.xinfeng.bbswork.widget.ChattingRootView;
import bbs.com.xinfeng.bbswork.widget.imageShow.ShowImagesDialog;
import bbs.com.xinfeng.bbswork.widget.popwindow.ChatRepublishPoppubWindow;
import bbs.com.xinfeng.bbswork.widget.popwindow.MessageFragmentPoppubWindow;
import bbs.com.xinfeng.bbswork.widget.popwindow.PointPoppubWindow;
import bbs.com.xinfeng.bbswork.widget.popwindow.PrivateChatPoppubWindow;
import bbs.com.xinfeng.bbswork.widget.popwindow.QPopuWindow;
import bbs.com.xinfeng.bbswork.widget.recordvoice.RecordVoiceButton;
import io.objectbox.Box;
import io.objectbox.query.Query;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

import static bbs.com.xinfeng.bbswork.base.Constant.head_key;
import static bbs.com.xinfeng.bbswork.base.Constant.userid_key;
import static bbs.com.xinfeng.bbswork.domin.PrivateChatItemBean.LOADING;
import static bbs.com.xinfeng.bbswork.mvp.model.PrivateChatModel.SOCKET_MES_TAG;

import com.donkingliang.imageselector.utils.ImageSelector;

public class PrivateChatActivity extends BaseActivity<ActivityPrivateChatBinding, PrivateChatPresenter> implements NetContract.INetView, View.OnClickListener, TXUGCPublishTypeDef.ITXVideoPublishListener {
    private final int boxMaxNum = 20;
    private final int UserinfoAttach_Tag = 301;
    private static final int ACTBLACK_IN_TAG = 302;
    private static final int ACTBLACK_OUT_TAG = 303;
    private static final int ClearContent_TAG = 304;
    private final int uploadpicone_realSend_TAG = 501;
    private final String INDEX = "index";
    private final String NEXT = "next";
    private final String PREV = "prev";
    private String nextCi = "";//防止重复向下拉取
    private int socketStatus;
    private int chatId;
    private int userid;

    private List<PrivateChatItemBean> mData = Collections.synchronizedList(new ArrayList<>());
    private List<PrivateChatItemBean> resendDatas = new ArrayList<>();
    private PrivateChatAdapter mAdapter;
    private Handler mHandler = new Handler();
    private LinearLayoutManager linearLayoutManager;
    private TXUGCPublish mVideoPublish;
    private int authorId;
    private int unreadDown;
    private int unreadUp;
    private ChatRepublishPoppubWindow republishPop;

    private boolean isIndex;//是否加载过首屏
    private Box<PrivateChatItemBean> box;
    private Query<PrivateChatItemBean> queryBuild;

    private MessageFragmentPoppubWindow mesPop;
    private PrivateChatPoppubWindow funcPop;
    public static boolean isPrivateChatting;
    private int forwardmt;
    private String forwardc1;
    private ShowImagesDialog imagesDialog;
    private ArrayList<String> src = new ArrayList<>();
    private ArrayList<String> thumb = new ArrayList<>();
    private int isblock;
    private PointPoppubWindow pointPop;
    private double firstItemMs;
    private int rawX;
    private int rawY;
    private Disposable chatIdDispose;

    @Override
    protected void onStart() {
        super.onStart();
        isPrivateChatting = true;
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
        if (tag == PrivateChatModel.SOCKET_MES_TAG) {
            if (errorBean instanceof SendPrivateMesBean) {
                if (chatId == ((SendPrivateMesBean) errorBean).getData().getSi()) {
                    if (((SendPrivateMesBean) errorBean).getCode() == 160006) {

                        for (int i = mData.size() - 1; i >= 0; i--) {
                            if (TextUtils.equals(mData.get(i).getTime() + "", ((SendPrivateMesBean) errorBean).getS())) {
                                mData.get(i).setSelfStatus(2);
//                                mData.get(i).setHasRunSend(false);
                                mData.get(i).setBackRunning(false);
                                mData.get(i).setTm(((SendPrivateMesBean) errorBean).getMessage());
                                mAdapter.notifyItemChanged(i);
                                if (linearLayoutManager.getItemCount() - 1 == linearLayoutManager.findLastVisibleItemPosition())
                                    mBinding.recycleChatting.smoothScrollToPosition(mAdapter.getItemCount() - 1);

                                break;
                            }
                        }

                        for (PrivateChatItemBean info : mData) {
                            if (info.getSelfStatus() == 1) {
                                realPublish(info);
                                break;
                            }
                        }
                    } else if (((SendPrivateMesBean) errorBean).getCode() == 160007) {
                        boolean isFind = false;
                        for (int i = mData.size() - 1; i >= 0; i--) {
                            if (mData.get(i).getSelfStatus() == 1 && ((SendPrivateMesBean) errorBean).getS().equals(mData.get(i).getTime() + "")) {
                                isFind = true;
                                mData.remove(i);
                                mAdapter.notifyItemRemoved(i);
                                break;
                            }
                        }
                        if (!isFind) {
                            for (PrivateChatItemBean errorData : resendDatas) {
                                if (errorData.getSelfStatus() == 1 && ((SendPrivateMesBean) errorBean).getS().equals(errorData.getTime() + "")) {
                                    resendDatas.remove(errorData);
                                    break;
                                }
                            }
                        }
                        continueSend();
                    } else {
                        for (int i = mData.size() - 1; i >= 0; i--) {
                            if (TextUtils.equals(mData.get(i).getTime() + "", ((SendPrivateMesBean) errorBean).getS())) {
                                mData.get(i).setSelfStatus(2);
//                                mData.get(i).setHasRunSend(false);
                                mData.get(i).setBackRunning(false);
                                mAdapter.notifyItemChanged(i);
                                break;
                            }
                        }
                    }

                }
            } else if (errorBean instanceof PrivateChatListBean) {
                mAdapter.removeLoading();
                mBinding.chatrootview.loadFinish();
            }
        } else if (tag == uploadpicone_realSend_TAG) {
            for (int i = mData.size() - 1; i >= 0; i--) {
                if (mData.get(i).getSelfStatus() == 1 && mData.get(i).getMt() == 2) {
                    mData.get(i).setSelfStatus(2);
//                    mData.get(i).setHasRunSend(false);
                    mData.get(i).setBackRunning(false);
                    mAdapter.notifyItemChanged(i);
                }
            }
        } else if (tag == ACTBLACK_IN_TAG) {
            if (TextUtils.equals(errorBean.androidcode, "150000")) {
                isblock = 1;
            }
        } else if (tag == ACTBLACK_OUT_TAG) {
            if (TextUtils.equals(errorBean.androidcode, "150001")) {
                isblock = 0;
            }
        }
    }

    @Override
    public void setData(ErrorBean errorBean, int tag) {
        switch (tag) {
            case SOCKET_MES_TAG:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (errorBean instanceof SendToServerIsSuccessBean) {
                            SendToServerIsSuccessBean bean = (SendToServerIsSuccessBean) errorBean;
                            if (bean.getChatId() == chatId) {
                                switch (bean.getA()) {
                                    case 4:
                                        if (bean.getStatus() == 1) {
                                            for (int i = mData.size() - 1; i >= 0; i--) {
                                                if (TextUtils.equals(bean.getKey(), mData.get(i).getTime() + "")) {
                                                    mData.get(i).setHasRunSend(true);
                                                    break;
                                                }
                                            }
                                        } else {
                                            for (int i = mData.size() - 1; i >= 0; i--) {
                                                if (TextUtils.equals(mData.get(i).getTime() + "", bean.getKey())) {
                                                    mData.get(i).setSelfStatus(2);
                                                    mData.get(i).setBackRunning(false);
                                                    mAdapter.notifyItemChanged(i);
                                                    break;
                                                }
                                            }
                                        }
                                        break;
                                    case 9:
                                        if (bean.getStatus() != 1) {
                                            mAdapter.removeLoading();
                                            mBinding.chatrootview.loadFinish();
                                        }
                                        break;
                                }

                            } else if (bean.getChatId() == -1 && chatId == 0) {
                                if (bean.getA() == 10) {
                                    if (bean.getStatus() != 1) {
                                        chatIdDispose = Flowable.timer(3, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
                                            @Override
                                            public void accept(Long aLong) throws Exception {
                                                mPresenter.addChat(userid, System.currentTimeMillis());
                                                chatIdDispose.dispose();
                                            }
                                        });
                                        mDisposables.add(chatIdDispose);
                                    }
                                }
                            }
                        }
                    }
                });

                if (errorBean instanceof MesInfoChangeBean) {
                    MesInfoChangeBean bean = (MesInfoChangeBean) errorBean;
                    if (bean.getSi() == chatId) {
                        String[] split = bean.getData().split(",");
                        for (String changeId : split) {
                            for (int j = 0; j < mData.size(); j++) {
                                if (TextUtils.equals(changeId, mData.get(j).getId())) {
                                    switch (bean.getTypeX()) {
                                        //1已读2已播3删除4撤回
                                        case 1:
                                            mData.get(j).setS1(2);
                                            mAdapter.notifyItemChanged(j);
                                            break;
                                        case 2:
                                            mData.get(j).setS1(2);
                                            mAdapter.notifyItemChanged(j);
                                            break;
                                        case 3:
                                            mData.remove(j);
                                            mAdapter.notifyItemRemoved(j);
                                            resetChatView();
                                            break;
                                        case 4:
                                            mData.get(j).setMt(54);
                                            mData.get(j).setC1(bean.getC());
                                            mAdapter.notifyItemChanged(j);
                                            break;
                                        case 5:
                                            mData.get(j).setMt(5);
                                            mData.get(j).setC1(bean.getC());
                                            mData.get(j).setUt(bean.getUt());
                                            mData.get(j).setUd(bean.getUd());
                                            mAdapter.notifyItemChanged(j);
                                            if (linearLayoutManager.getItemCount() - 1 == linearLayoutManager.findLastVisibleItemPosition())
                                                mBinding.recycleChatting.smoothScrollToPosition(mAdapter.getItemCount() - 1);

                                            break;
                                    }
                                    break;
                                }
                            }
                        }
                    }
                } else if (errorBean instanceof AddChatBean) {//添加会话
                    AddChatBean addChatBean = (AddChatBean) errorBean;
                    chatId = addChatBean.getData().getId();
                    MessageFragment.ResumeChatId = chatId;
                    initBox();
                    mAdapter.notifyDataSetChanged();
                    /*mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mBinding.recycleChatting.scrollToPosition(mAdapter.getItemCount() - 1);
                        }
                    }, 50);*/
                    getIndexChatList();
                } else if (errorBean instanceof SendPrivateMesBean) {//发送消息成功
                    SendPrivateMesBean bean = (SendPrivateMesBean) errorBean;
                    if (chatId == bean.getData().getSi()) {
                        boolean isFind = false;
                        for (int i = mData.size() - 1; i >= 0; i--) {
                            if (mData.get(i).getSelfStatus() == 1 && bean.getS().equals(mData.get(i).getTime() + "")) {
                                isFind = true;
                                mData.get(i).setSelfStatus(3);
                                mData.get(i).setId(bean.getData().getId());
                                mAdapter.notifyItemChanged(i);
                                break;
                            }
                        }
                        if (!isFind) {
                            for (PrivateChatItemBean errorData : resendDatas) {
                                if (errorData.getSelfStatus() == 1 && bean.getS().equals(errorData.getTime() + "")) {
                                    resendDatas.remove(errorData);
                                    break;
                                }
                            }
                        }
                        continueSend();
                    }
                } else if (errorBean instanceof PrivateMesChangedBean) {//消息变动
                    PrivateMesChangedBean bean = (PrivateMesChangedBean) errorBean;
                    if (bean.getSi() == chatId) {
                        getNextChatList();
                    }
                } else if (errorBean instanceof PrivateChatListBean) {//拉取消息消息
                    PrivateChatListBean bean = (PrivateChatListBean) errorBean;
                    dealDates(bean);
                    if (TextUtils.equals(bean.getData().getType(), INDEX)) {
                        mData.clear();
                        dealErrorDataToList(bean.getData().getList(), resendDatas);
                        if (bean.getData().getList() != null && bean.getData().getList().size() > 0) {
                            nextCi = bean.getData().getCurrent();
                            firstItemMs = bean.getData().getList().get(bean.getData().getList().size() - 1).getMs();
                            /*if (bean.getData().getList().get(0).getMt() == 51) {
                                bean.getData().getList().remove(0);
                            }*/
                            mData.addAll(bean.getData().getList());
//                            mData.addAll(mData.size(), resendDatas);
                            mAdapter.notifyDataSetChanged();
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mBinding.recycleChatting.scrollToPosition(mAdapter.getItemCount() - 1);
                                }
                            }, 50);
                        } else {
//                            mData.addAll(resendDatas);
                            mAdapter.notifyDataSetChanged();
                            mAdapter.removeLoading();
                            mBinding.chatrootview.loadFinish();
                        }
                        unreadUp = bean.getData().getN();

                        if (forwardmt == 1) {
                            realPublish(readyPublish(1, forwardc1, 0));
                        } else if (forwardmt == 2) {
                            MainActivity.picMap.put(forwardc1, forwardc1);
                            ArrayList<String> pics = new ArrayList<>();
                            pics.add(forwardc1);
                            realPublish(readyPublishPics(2, pics));
                        }

                        updataUnreadUpView();
//                        continueSend();

                    } else if (TextUtils.equals(bean.getData().getType(), NEXT)) {
                        nextCi = bean.getData().getCurrent();
                        if (bean.getData().getList() != null && bean.getData().getList().size() > 0) {
                            firstItemMs = bean.getData().getList().get(bean.getData().getList().size() - 1).getMs();
                            int j = mData.size() - bean.getData().getList().size() - 1;
                            int offset = 0;
                            loop1:
                            for (PrivateChatItemBean newData : bean.getData().getList()) {
                                if (mData.size() > 0) {
                                    for (int i = mData.size() - 1 - offset; i >= mData.size() - j - offset; i--) {
                                        if (i >= 0) {
                                            if (!mData.get(i).isSelf() && TextUtils.equals(mData.get(i).getId(), newData.getId())) {
                                                continue loop1;
                                            }
                                        }
                                    }

                                }

                                if (newData.getSu() == authorId) {
                                    boolean isReplace = false;
                                    for (int i = mData.size() - 1; i >= 0; i--) {
                                        if (TextUtils.equals(newData.getS(), mData.get(i).getTime() + "")) {
                                            if (i == mData.size() - 1) {
                                                isReplace = true;
                                                newData.setTime(mData.get(i).getTime());
                                                mData.set(i, newData);
                                                mAdapter.notifyItemChanged(i);
                                                if (!TextUtils.isEmpty(newData.getTm()) && linearLayoutManager.getItemCount() - 1 == linearLayoutManager.findLastVisibleItemPosition())
                                                    mBinding.recycleChatting.smoothScrollToPosition(mAdapter.getItemCount() - 1);
                                            } else {
                                                newData.setTime(mData.get(i).getTime());
                                                mData.remove(i);
                                                mAdapter.notifyItemRemoved(i);
                                            }
                                            break;
                                        }
                                    }
                                    if (!isReplace) {
                                        mData.add(newData);
                                        mAdapter.notifyItemInserted(mData.size() - 1);
                                        /*if (linearLayoutManager.getItemCount() - 1 == linearLayoutManager.findLastVisibleItemPosition()) {
                                            mData.add(newData);
                                            mAdapter.notifyItemInserted(mData.size() - 1);
                                        } else {
                                            mData.add(newData);
                                            mAdapter.notifyItemInserted(mData.size() - 1);
                                        }*/
                                    }

                                } else {
                                   /* if (mData.size() > 0) {
                                        int j = mData.size() - bean.getData().getList().size() - 1;
                                        for (int i = mData.size() - 1; i >= j; i--) {
                                            if (i >= 0) {
                                                if (mData.get(i).isSelf())
                                                    j--;
                                                if (TextUtils.equals(mData.get(i).getId(), newData.getId())) {
                                                    continue loop1;
                                                }
                                            }
                                        }

                                    }*/
                                    if (linearLayoutManager.getItemCount() - 1 == linearLayoutManager.findLastVisibleItemPosition()) {
                                        mData.add(newData);
                                        mAdapter.notifyItemInserted(mData.size() - 1);
                                        mBinding.recycleChatting.scrollToPosition(mAdapter.getItemCount() - 1);
                                    } else {
                                        mData.add(newData);
                                        mAdapter.notifyItemInserted(mData.size() - 1);
                                        unreadDown++;
                                        mBinding.txtChatUnreadDown.setText(unreadDown + "个新消息");
                                        mBinding.flayoutChatUnreadDown.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                        }
                    } else if (TextUtils.equals(bean.getData().getType(), PREV)) {
                        dealErrorDataToList(bean.getData().getList(), resendDatas);
                        /*if (bean.getData().getList() != null && bean.getData().getList().size() > 0) {
                            if (bean.getData().getList().get(0).getMt() == 51) {
                                bean.getData().getList().remove(0);
                            }
                        }*/
                        if (bean.getData().getList() != null && bean.getData().getList().size() > 0) {
                            mAdapter.removeLoading();
                            /*if (bean.getData().getList().get(0).getMt() == 51) {
                                bean.getData().getList().remove(0);
                            }*/
                            mData.addAll(0, bean.getData().getList());
                            mAdapter.notifyItemRangeInserted(0, bean.getData().getList().size());
                            mBinding.chatrootview.loadFinish();
                            unreadUp = unreadUp - bean.getData().getList().size();
                        } else {
                            mAdapter.removeLoading();
                            mBinding.chatrootview.loadFinish();
                            unreadUp = 0;
                            ToastUtil.showToast("没有更多了");
                        }
                        updataUnreadUpView();
                    }
                    showEmptyView();
                }
                break;
            case uploadpicone_realSend_TAG:
                if (errorBean instanceof UploadBean) {
                    UploadBean bean = (UploadBean) errorBean;
                    MainActivity.picMap.put(errorBean.localPath, bean.getSrc());
                    for (int i = mData.size() - 1; i >= 0; i--) {
                        if (mData.get(i).getSelfStatus() == 1 && errorBean.key.equals(mData.get(i).getTime() + "")) {
                            if (updataPicMapReal(mData.get(i).getC1(), Long.parseLong(errorBean.key), uploadpicone_realSend_TAG))
                                realPublish(mData.get(i));
                            break;
                        }
                    }
//                    if (updataPicMap(readyInfo.getThumb(), uploadpicone_realSend_TAG))
//                        realPublishWithPics(readyInfo);
                }
                break;
            case UserinfoAttach_Tag:
                if (errorBean instanceof PrivateUserDetailBean) {
                    PrivateUserDetailBean bean = (PrivateUserDetailBean) errorBean;
                    isblock = bean.getIsblock();
                    showRightButtonView();
                }
                break;
            case ACTBLACK_OUT_TAG:
                isblock = 0;
                ToastUtil.showToast("移除黑名单成功");
                break;
            case ACTBLACK_IN_TAG:
                isblock = 1;
                ToastUtil.showToast("加入黑名单成功");
                break;
            case ClearContent_TAG:
                EventBus.getDefault().post(new JoinPrivateChatBus(chatId));
                resendDatas.clear();
                mData.clear();
                mAdapter.notifyDataSetChanged();
                showEmptyView();
                resetChatView();

                break;
        }
    }

    private void continueSend() {
        boolean hasSend = false;
        for (PrivateChatItemBean info : resendDatas) {
            if (info.getSelfStatus() == 1 && !info.isBackRunning() && !info.isHasRunSend()) {
                LogUtil.i("sendrealmessagcontinueerror", info.toString());
                hasSend = true;
                realPublish(info);
                break;
            }
        }
        if (!hasSend) {
            for (PrivateChatItemBean info : mData) {
                if (info.getSelfStatus() == 1 && !info.isBackRunning() && !info.isHasRunSend()) {
                    LogUtil.i("sendrealmessagcontinuedata", info.toString());
                    realPublish(info);
                    break;
                }
            }
        }
    }

    private void resetChatView() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mBinding.chatrootview.reset();
            }
        }, 50);
    }

    private void showRightButtonView() {
        hideKeyboard();
        if (mesPop == null) {
            mesPop = new MessageFragmentPoppubWindow(provideActivity());
        }
        mesPop.setFirstBtn("清空");
        mesPop.setSecondBtn("举报");
        mesPop.setThirdBtn(isblock == 1 ? "移除黑名单" : "加入黑名单");
        mesPop.setDismissListener(new MessageFragmentPoppubWindow.DismissListener() {
            @Override
            public void dismiss() {

            }

            @Override
            public void onDel() {
                if (chatId != 0)
                    mPresenter.setStatus(chatId, 4, ClearContent_TAG);

                /*boolean isGet = false;
                for (int i = mData.size() - 1; i >= 0; i--) {
                    if (mData.get(i).isSelf()) {
                        mData.remove(i);
                        mAdapter.notifyItemRemoved(i);
                    } else {
                        if (!isGet) {
                            nextCi = mData.get(i).getId();
                            isGet = true;
                        }
                    }
                }
                if (chatId != 0) {
                    ArrayList<String> delIds = new ArrayList<>();
                    StringBuffer stringBuffer = new StringBuffer();
                    int count = 0;
                    for (PrivateChatItemBean info : mData) {
                        if (!TextUtils.isEmpty(info.getId())) {
                            count++;
                            stringBuffer.append(info.getId());
                            stringBuffer.append(",");
                            if (count >= 100) {
                                delIds.add(stringBuffer.deleteCharAt(stringBuffer.length() - 1).toString());
                                stringBuffer.delete(0, stringBuffer.length());
                                count = 0;
                            }

                        }
                    }
                    if (stringBuffer.length() > 0) {
                        delIds.add(stringBuffer.deleteCharAt(stringBuffer.length() - 1).toString());
                    }
                    for (String ids : delIds) {
                        mPresenter.operateMes(12, chatId, ids);
                    }
                }*/
            }

            @Override
            public void onReport() {
                Intent intent = new Intent(provideActivity(), WebviewActivity.class);
                intent.putExtra("weburl", Constant.BASEURL + "/report?user=" + userid);
                startActivity(intent);
            }

            @Override
            public void onBlacklist() {
                if (isblock == 1) {
                    mPresenter.actBlack(userid, -1, ACTBLACK_OUT_TAG);
                } else {
                    if (pointPop == null)
                        pointPop = new PointPoppubWindow(provideActivity());
                    pointPop.setPointInfo("您是否确定将TA拉入黑名单", null, false, "取消", "确认");
                    pointPop.setDismissListener(new PointPoppubWindow.DismissListener() {
                        @Override
                        public void dismiss() {

                        }

                        @Override
                        public void clickOne() {

                        }

                        @Override
                        public void clickleft() {

                        }

                        @Override
                        public void clickRight() {
                            mPresenter.actBlack(userid, 1, ACTBLACK_IN_TAG);
                        }
                    });
                    pointPop.show();
                }

            }
        });

        mesPop.show();
    }

    private void updataUnreadUpView() {
        if (unreadUp > 0) {
            mBinding.flayoutChatUnreadUp.setVisibility(View.VISIBLE);
            mBinding.txtChatUnreadUp.setText(unreadUp + " 未读消息");
            mBinding.flayoutChatUnreadUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBinding.chatrootview.loadStart();
                    mBinding.recycleChatting.scrollToPosition(0);
                    getPrevChatList();

                }
            });
        } else {
            mBinding.flayoutChatUnreadUp.setVisibility(View.GONE);
        }
    }

    private void dealDates(PrivateChatListBean bean) {
        for (PrivateChatItemBean info : bean.getData().getList()) {
            for (PrivateChatListBean.DataBean.UserBean userInfo : bean.getData().getUser()) {
                if (info.getSu() == userInfo.getId()) {
                    info.setImg(userInfo.getImg());
                    break;
                }
            }
            if (info.getMt() == 3) {
                try {
                    info.setDuration(Integer.parseInt(Uri.parse(info.getC1()).getQueryParameter("t")));
                } catch (Exception e) {
                }
            }
        }
//        Collections.reverse(bean.getData().getList());
    }

    @Override
    public void progress(int precent, int tag) {

    }

    @Override
    protected PrivateChatPresenter creatPresenter() {
        return new PrivateChatPresenter(this);
    }

    @Override
    protected int initView(Bundle savedInstanceState) {
        return R.layout.activity_private_chat;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        userid = getIntent().getIntExtra("userid", 0);
        forwardmt = getIntent().getIntExtra("forwardingmt", 0);
        forwardc1 = getIntent().getStringExtra("forwardingc1");
        mBinding.basebar.barLeftClick.setVisibility(View.VISIBLE);
        mBinding.basebar.barTxtTitle.setText(getIntent().getStringExtra("username"));
        mBinding.basebar.barLeftClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
               /* if (hasUnSendMes()) {
                    closePoint();
                } else {
                    finish();
                }*/
            }
        });
        mBinding.basebar.barRightPic.setImageResource(R.drawable.icon_more);
        mBinding.basebar.barRightClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mesPop == null) {
                    mPresenter.getUserinfo(userid, chatId, UserinfoAttach_Tag);
                } else {
                    showRightButtonView();
                }
            }
        });
        authorId = SharedPrefUtil.getInt(Constant.userid_key, 0);
        EmojiUtil emojiUtil = new EmojiUtil();
        emojiUtil.setSelfDrawable(getApplicationContext(), mBinding.vpEmoji, mBinding.llDot, mBinding.editContent, 0);
        chatId = getIntent().getIntExtra("chatid", 0);
        MessageFragment.ResumeChatId = chatId;

        initBox();
        initAdapterChat();
        initRecord();
        socketStatus = App.getInstance().socketStatus;
        updateSocketView();
        if (socketStatus != 2) {
            isIndex = true;
            if (chatId != 0) {
                getIndexChatList();
            } else {
                mPresenter.addChat(userid, System.currentTimeMillis());
            }
        }

    }

    private void initBox() {
        if (box == null) {
            box = App.mApp.getmBoxStore().boxFor(PrivateChatItemBean.class);
        }
        if (chatId != 0 && queryBuild == null) {
            queryBuild = box.query().equal(PrivateChatItemBean_.uid_android, authorId).equal(PrivateChatItemBean_.chatId, chatId).build();
        }
        if (queryBuild != null) {
            List<PrivateChatItemBean> boxList = queryBuild.find();
            boolean isOpened = MainActivity.openPrivateChats.containsKey(chatId);
            for (PrivateChatItemBean boxBean : boxList) {
                LogUtil.i("sendrealmessagbox", boxBean.toString());
                if (!isOpened) {
                    if (boxBean.getSelfStatus() == 1) {
                        boxBean.setSelfStatus(2);
//                        boxBean.setHasRunSend(false);
                        boxBean.setBackRunning(false);
                    }
                }
                if (TextUtils.isEmpty(boxBean.getId()) && (boxBean.getSelfStatus() == 1 || boxBean.getSelfStatus() == 2)) {
                    if (boxBean.getLastms() != 0)
                        resendDatas.add(boxBean);
                } else if (boxBean.getSelfStatus() == 3) {
                } else {
                    if (boxBean.getMt() == 400100) {
                        nextCi = boxBean.getId();
                        firstItemMs = boxBean.getLastms();
                    } else {
                        mData.add(boxBean);
                    }
                }

            }

            if (TextUtils.isEmpty(nextCi)) {
                for (int i = mData.size() - 1; i >= 0; i--) {
                    if (!mData.get(i).isSelf() && !TextUtils.isEmpty(mData.get(i).getId())) {
                        nextCi = mData.get(i).getId();
                        firstItemMs = mData.get(i).getMs();
                        break;
                    }
                }
            }
            MainActivity.openPrivateChats.put(chatId, chatId);
            showEmptyView();
        }
    }

    private void dealErrorDataToList(List<PrivateChatItemBean> mData, List<PrivateChatItemBean> resendData) {
        if (resendDatas.size() > 0) {
            if (mData.size() > 1) {
                double startMs = mData.get(0).getMs();
                double errorStartMs = resendDatas.get(0).getLastms();
                double errorEndMs = resendDatas.get(resendDatas.size() - 1).getLastms();
                if (errorStartMs >= startMs || errorEndMs >= startMs) {
                    List<PrivateChatItemBean> removeData = new ArrayList<>();
                    for (PrivateChatItemBean errorData : resendDatas) {
                        for (int i = mData.size() - 1; i >= 0; i--) {
                            if (errorData.getLastms() >= (mData.get(i).getLastms() != 0 ? mData.get(i).getLastms() : mData.get(i).getMs())) {
                                mData.add(i + 1, errorData);
                                removeData.add(errorData);
                                break;
                            }
                        }
                    }
                    resendDatas.removeAll(removeData);
                }
            } else {
                mData.addAll(resendDatas);
                resendDatas.clear();
            }
        }
    }

    private synchronized void getIndexChatList() {
       /* String ci = "";
        for (int i = mData.size() - 1; i >= 0; i--) {
            if (!mData.get(i).isSelf()) {
                ci = mData.get(i).getId();
                break;
            }
        }
        getChatList(chatId, ci, INDEX, System.currentTimeMillis());*/
        getChatList(chatId, nextCi, INDEX, System.currentTimeMillis());
    }

    private synchronized void getPrevChatList() {
        if (socketStatus == 2) {
            String ci = "";
            for (PrivateChatItemBean info : mData) {
                if (info.getType() != LOADING && !info.isSelf()) {
                    ci = info.getId();
                    break;
                }
            }
            if (!TextUtils.isEmpty(ci))
                getChatList(chatId, ci, PREV, System.currentTimeMillis());
            else {
                mAdapter.removeLoading();
                mBinding.chatrootview.loadFinish();
                ToastUtil.showToast("没有更多了");
            }

        } else {
            mAdapter.removeLoading();
            mBinding.chatrootview.loadFinish();
            ToastUtil.showToast("请求失败");
        }
    }

    private synchronized void getNextChatList() {
        /*for (int i = mData.size() - 1; i >= 0; i--) {
            if (!mData.get(i).isSelf() && !TextUtils.isEmpty(mData.get(i).getId())) {
                nextCi = mData.get(i).getId();
                getChatList(chatId, nextCi, NEXT, System.currentTimeMillis());
                return;
            }
        }*/
        if (!TextUtils.isEmpty(nextCi))
            getChatList(chatId, nextCi, NEXT, System.currentTimeMillis());
    }

    private void getChatList(int chatId, String ci, String type, long s) {
        mPresenter.getChatList(chatId, ci, type, s);
    }

    private void initRecord() {
        if (mVideoPublish == null) {
            mVideoPublish = new TXUGCPublish(provideActivity().getApplicationContext(), "blackbean");
            mVideoPublish.setListener(this);
        }
        mBinding.buttonRecord.setRecordListener(new RecordVoiceButton.OnRecordVoiceListener() {
            @Override
            public void onRecordFinished(int duration, String path) {
                realPublish(readyPublish(3, path, duration));
            }

            @Override
            public void onStartRecord() {
                mAdapter.stopMediaPlayer();
            }
        });
        mBinding.buttonRecord.setFilePath(Constant.STORAGE_PATH + "/voice");
        mAdapter.initMediaPlayer();
    }

    private void initAdapterChat() {
        mAdapter = new PrivateChatAdapter(provideActivity(), mData);
        mBinding.recycleChatting.setAdapter(mAdapter);
        mBinding.recycleChatting.setItemAnimator(null);
        mBinding.chatrootview.setAdatperLoading(mAdapter);
        linearLayoutManager = new LinearLayoutManager(provideActivity(), LinearLayoutManager.VERTICAL, false);
        mBinding.recycleChatting.setLayoutManager(linearLayoutManager);
//        mBinding.recycleChatting.addItemDecoration(new ItemLineDecoration(provideActivity(), R.dimen.dp12));
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mBinding.recycleChatting.scrollToPosition(mAdapter.getItemCount() - 1);
            }
        }, 50);
        mBinding.chatrootview.setOnLoadingLisening(new ChattingRootView.OnLoadingListening() {
            @Override
            public void startLoading() {
                if (mPresenter != null)
                    getPrevChatList();
            }

            @Override
            public void hideViews() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hideKeyboard();
                        mBinding.editContent.clearFocus();
                        mBinding.flayoutRecord.setVisibility(View.GONE);
                        mBinding.llayoutTxt.setVisibility(View.VISIBLE);
                        mBinding.flayoutChatPicRoot.setVisibility(View.GONE);
                        mBinding.llayoutChatEmojiRoot.setVisibility(View.GONE);
                        mBinding.ivChatEmoji.setSelected(false);
                        updataSendButton();
                    }
                }, 50);

            }
        });

        mBinding.recycleChatting.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1)) {
                    unreadDown = 0;
                    mBinding.flayoutChatUnreadDown.setVisibility(View.GONE);
                }
            }
        });
        mAdapter.setOnItemOperateListen(new PrivateChatAdapter.onItemOperateListen() {
            @Override
            public void clickHead(int position) {
                if (isPreventGo()) {
                    Intent intent = new Intent(provideActivity(), UserInfoActivity.class);
                    intent.putExtra("userid", mData.get(position).getSu());
                    startActivity(intent);
                }
            }

            @Override
            public void longClick(View v, int position, String content) {
                PrivateChatItemBean info = mData.get(position);
                if (chatId == 0 || TextUtils.isEmpty(info.getId())) {
                    return;
                }
                ArrayList<String> itemTxtS = new ArrayList<>();
                ArrayList<Integer> itemDrawableS = new ArrayList<>();
                ArrayList<String> itemClickS = new ArrayList<>();
                if (info.getMt() == 1) {
                    itemTxtS.add("复制");
                    itemDrawableS.add(R.drawable.icon_private_copy);
                    itemClickS.add("copy");
                } else if (info.getMt() == 5) {
                    itemTxtS.add("复制链接");
                    itemDrawableS.add(R.drawable.icon_private_copy);
                    itemClickS.add("copy");
                }

                if ((!info.isSelf() && (info.getMt() == 1 || info.getMt() == 2 || info.getMt() == 5))) {
                    itemTxtS.add("转发");
                    itemDrawableS.add(R.drawable.icon_private_forward);
                    itemClickS.add("forward");
                }
                itemTxtS.add("删除");
                itemDrawableS.add(R.drawable.icon_private_delete);
                itemClickS.add("delete");
                if (info.getSu() == authorId && (System.currentTimeMillis() / 1000 - info.getMs() <= 115)) {
                    itemTxtS.add("撤回");
                    itemDrawableS.add(R.drawable.icon_private_retract);
                    itemClickS.add("retract");
                }
                QPopuWindow.getInstance(provideActivity()).builder
                        .bindView(v, position)
                        .setPopupItemList(itemTxtS.toArray(new String[itemTxtS.size()]))
                        .setTextDrawableRes(itemDrawableS.toArray(new Integer[itemDrawableS.size()]))
                        .setPointers(rawX, rawY)
                        .setDividerVisibility(false)
                        .setTextSize(11)
                        .setOnPopuListItemClickListener(new QPopuWindow.OnPopuListItemClickListener() {
                            @Override
                            public void onPopuListItemClick(View anchorView, int anchorViewPosition, int position) {
                                String clickItem = itemClickS.get(position);
                                if (TextUtils.equals(clickItem, "copy")) {
                                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                    ClipData mClipData = ClipData.newPlainText("Label", content);
                                    cm.setPrimaryClip(mClipData);
                                    ToastUtil.showToast("已复制");

                                } else if (TextUtils.equals(clickItem, "forward")) {
                                    Intent intent = new Intent(provideActivity(), ContactsActivity.class);
                                    intent.putExtra("forwardingmt", info.getMt() == 5 ? 1 : info.getMt());
                                    intent.putExtra("forwardingc1", info.getMt() == 1 ? content : info.getC1());
                                    startActivity(intent);

                                } else if (TextUtils.equals(clickItem, "delete")) {
                                    for (int i = mData.size() - 1; i >= 0; i--) {
                                        if (!mData.get(i).isSelf()) {
                                            nextCi = mData.get(i).getId();
                                            break;
                                        }
                                    }
                                    mPresenter.operateMes(12, chatId, info.getId());

                                } else if (TextUtils.equals(clickItem, "retract")) {
                                    if (System.currentTimeMillis() / 1000 - info.getMs() <= 115)
                                        mPresenter.operateMes(11, chatId, info.getId());
                                    else
                                        ToastUtil.showToast("超出撤回时间");
                                }

                            }
                        }).show();


/*                if (funcPop == null) {
                    funcPop = new PrivateChatPoppubWindow(provideActivity());
                }
                funcPop.setBtnContent(info.getMt() == 1 ? "复制" : "", (!info.isSelf() && (info.getMt() == 1 || info.getMt() == 2)) ? "转发" : "", "删除",
                        (info.getSu() == authorId && (System.currentTimeMillis() / 1000 - info.getMs() <= 115) ? "撤回" : ""));
                funcPop.setDismissListener(new PrivateChatPoppubWindow.DismissListener() {
                    @Override
                    public void onFirst() {
                        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData mClipData = ClipData.newPlainText("Label", info.getC1());
                        cm.setPrimaryClip(mClipData);
                        ToastUtil.showToast("已复制");
                    }

                    @Override
                    public void onSecond() {
                        Intent intent = new Intent(provideActivity(), ContactsActivity.class);
                        intent.putExtra("forwardingmt", info.getMt());
                        intent.putExtra("forwardingc1", info.getC1());
                        startActivity(intent);
                    }

                    @Override
                    public void onThird() {
                        for (int i = mData.size() - 1; i >= 0; i--) {
                            if (!mData.get(i).isSelf()) {
                                nextCi = mData.get(i).getId();
                                break;
                            }
                        }
                        mPresenter.operateMes(12, chatId, info.getId());
                    }

                    @Override
                    public void onFourth() {
                        if (System.currentTimeMillis() / 1000 - info.getMs() <= 115)
                            mPresenter.operateMes(11, chatId, info.getId());
                        else
                            ToastUtil.showToast("超出撤回时间");
                    }

                });
                if (!TextUtils.isEmpty(info.getId()))
                    funcPop.show();*/
            }

            @Override
            public void onClick(int position) {
            }

            @Override
            public void sendEerror(int position) {
                if (republishPop == null) {
                    republishPop = new ChatRepublishPoppubWindow(provideActivity());
                }
                republishPop.setDismissListener(new ChatRepublishPoppubWindow.DismissListener() {
                    @Override
                    public void dismiss() {

                    }

                    @Override
                    public void onDelete() {
                        mData.remove(position);
                        mAdapter.notifyItemRemoved(position);
                        resetChatView();
                    }

                    @Override
                    public void onRepublish() {
                        mData.get(position).setSelfStatus(1);
                        mAdapter.notifyItemChanged(position);
                        realPublish(mData.get(position));
                    }
                });
                republishPop.show();
            }

            @Override
            public void clickAudio(int position) {
                if (mData.get(position).getS1() != 2) {
                    mPresenter.operateMes(13, chatId, mData.get(position).getId());
                }
            }

            @Override
            public void clickPic(int position) {
                if (isPreventGo()) {
                    if (imagesDialog == null) {
                        imagesDialog = new ShowImagesDialog(provideActivity());
                        imagesDialog.setHasSave(true);
                    }
                    int showPosition = 0;
                    int count = 0;
                    src.clear();
                    thumb.clear();
                    for (int i = 0; i < mData.size(); i++) {
                        if (!mData.get(i).isSelf() && mData.get(i).getMt() == 2) {
                            count++;
                            src.add(mData.get(i).getC1());
                            thumb.add(mData.get(i).getC2());
                            if (i == position)
                                showPosition = count - 1;
                        }
                    }

                    imagesDialog.setPicList(src, thumb, showPosition);
                    imagesDialog.show();
                }
            }
        });
        showEmptyView();
    }


    @Override
    protected void initEvent() {
        mBinding.tvPchatPic.setOnClickListener(this);
        mBinding.ivChatRecord.setOnClickListener(this);
        mBinding.ivChatKeyboard.setOnClickListener(this);

        mBinding.ivEmojiDelete.setOnClickListener(this);
        mBinding.flayoutChatEmoji.setOnClickListener(this);
        mBinding.flayoutChatSend.setOnClickListener(this);
        mBinding.ivChatRecord.setOnClickListener(this);
        mBinding.flayoutChatUnreadDown.setOnClickListener(this);

        mBinding.editContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    clickEditView();
                }
                return false;
            }
        });

        mBinding.editContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

               /* if (before == 0 && s.toString().length() > 0 && s.toString().substring(s.length() - 1).equals("@")) {
                    Intent intent = new Intent(provideActivity(), AtMemberListActivity.class);
                    intent.putExtra("topicid", topicId);
                    startActivityForResult(intent, AT_request);
                }*/
            }

            @Override
            public void afterTextChanged(Editable s) {
                updataSendButton();

            }
        });
    }

    //更新发送按钮状态
    private void updataSendButton() {
        if (mBinding.editContent.getEditableText().toString().trim().length() > 0) {
            mBinding.ivChatSend.setBackground(getResources().getDrawable(R.drawable.icon_chat_send_select));
        } else {
            mBinding.ivChatSend.setBackground(getResources().getDrawable(R.drawable.selector_pchat_more));
            mBinding.ivChatSend.setSelected(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.flayout_chat_emoji:
                showOrHideEmoji();
                break;
            case R.id.flayout_chat_send:
                if (mBinding.editContent.getEditableText().toString().trim().length() > 0) {
                    if (mBinding.editContent.getEditableText().toString().trim().length() <= 1000)
                        realPublish(readyPublish(1, mBinding.editContent.getEditableText().toString(), 0));
                    else
                        ToastUtil.showToast("发送消息内容超长，请分条发送。");
                } else {
                    showOrHideMore();
                }
                break;
            case R.id.iv_emoji_delete:
                int keyCode = KeyEvent.KEYCODE_DEL;
                KeyEvent keyEventDown = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
                KeyEvent keyEventUp = new KeyEvent(KeyEvent.ACTION_UP, keyCode);
                mBinding.editContent.onKeyDown(keyCode, keyEventDown);
                mBinding.editContent.onKeyUp(keyCode, keyEventUp);
                break;
            case R.id.tv_pchat_pic:
                ImageSelector.builder()
                        .useCamera(true) // 设置是否使用拍照
                        .setSingle(false)  //设置是否单选
                        .setMaxSelectCount(9) // 图片的最大选择数量，小于等于0时，不限数量。
                        .start(this, PhotoPicker.REQUEST_CODE); // 打开相册

                /*PhotoPicker.builder()
                        .setPhotoCount(9)
                        .setShowCamera(true)
                        .setPreviewEnabled(true)
                        .setShowSendButton(true)
                        .start(provideActivity(), PhotoPicker.REQUEST_CODE);*/
                break;
            case R.id.iv_chat_record:
                new RxPermissions(this).request(Manifest.permission.RECORD_AUDIO).subscribe(aBoolean -> {
                    if (aBoolean) {
                        hideKeyboard();
                        mBinding.editContent.clearFocus();
                        mBinding.llayoutTxt.setVisibility(View.GONE);
                        mBinding.flayoutRecord.setVisibility(View.VISIBLE);
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mBinding.recycleChatting.scrollToPosition(mAdapter.getItemCount() - 1);
                            }
                        }, 50);
                    } else {
                        ToastUtil.showToast("请开启录音权限");
                    }
                });

                break;
            case R.id.iv_chat_keyboard:
                mBinding.flayoutRecord.setVisibility(View.GONE);
                mBinding.llayoutTxt.setVisibility(View.VISIBLE);
                break;
            case R.id.flayout_chat_unread_down:
                unreadDown = 0;
                mBinding.recycleChatting.scrollToPosition(mAdapter.getItemCount() - 1);
                mBinding.flayoutChatUnreadDown.setVisibility(View.GONE);
                break;
        }
    }

    private void realPublish(PrivateChatItemBean readyInfo) {
        int type = readyInfo.getMt();
        if (type == 1) {//文本
            if (socketStatus != 2 || chatId == 0) {
                updateToErrorStatus();
            } else {
                if (mPresenter != null) {
                    String replace = readyInfo.getC1().toString()
                            .replace("\\", "\\\\")
                            .replace("\n", "\\n")
                            .replace("\b", "\\b")
                            .replace("\f", "\\f")
                            .replace("\r", "\\r")
                            .replace("\t", "\\t");
                    mPresenter.sendRealMes(chatId, 1, replace, readyInfo.getTime(), readyInfo.isHasRunSend());
//                    readyInfo.setHasRunSend(true);
                }
            }
        } else if (type == 2) {//图片
            if (updataPicMapReal(readyInfo.getC1(), readyInfo.getTime(), uploadpicone_realSend_TAG)) {
                readyInfo.setWebPic(MainActivity.picMap.get(readyInfo.getC1()));
//                readyInfo.setC1(MainActivity.picMap.get(readyInfo.getC1()));
                if (socketStatus != 2 || chatId == 0) {
                    updateToErrorStatus();
                } else {
                    if (mPresenter != null) {
                        mPresenter.sendRealMes(chatId, 2, readyInfo.getWebPic(), readyInfo.getTime(), readyInfo.isHasRunSend());
//                        readyInfo.setHasRunSend(true);
                    }
                }
            }
        } else if (type == 3) {//语音
            if (updataAudioMap(2, readyInfo.getC1(), null, readyInfo.getDuration())) {
                readyInfo.setWebPic(MainActivity.audioMap.get(readyInfo.getC1()).webUrl);
                if (socketStatus != 2 || chatId == 0) {
                    updateToErrorStatus();
                } else {
                    if (mPresenter != null) {
                        mPresenter.sendRealMes(chatId, 3, readyInfo.getWebPic(), readyInfo.getTime(), readyInfo.isHasRunSend());
//                        readyInfo.setHasRunSend(true);
                    }
                }
            }
        } else if (type == 4) {//视频

        }
    }

    private void updateToErrorStatus() {
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).getSelfStatus() == 1) {
                mData.get(i).setSelfStatus(2);
//                mData.get(i).setHasRunSend(false);
                mData.get(i).setBackRunning(false);
                mAdapter.notifyItemChanged(i);
            }
        }
    }

    private PrivateChatItemBean readyPublish(int type, String content, int duration) {
        PrivateChatItemBean bean = new PrivateChatItemBean(1);
        bean.setSelf(true);
        bean.setChatId(chatId);
        bean.setSelfStatus(1);
        bean.setSu(SharedPrefUtil.getInt(userid_key, 0));
        bean.setImg(SharedPrefUtil.getString(head_key, ""));
        bean.setC1(content);
        bean.setC2(content);
        bean.setMt(type);
        bean.setTime(System.currentTimeMillis());
        bean.setMs((System.currentTimeMillis()) / 1000);
        bean.setLastms(firstItemMs);
        bean.setDuration(duration);
        mData.add(bean);
        mAdapter.notifyItemInserted(mData.size() - 1);
        showEmptyView();
        mBinding.recycleChatting.scrollToPosition(mAdapter.getItemCount() - 1);
        mBinding.editContent.setText("");
        return bean;
    }

    private PrivateChatItemBean readyPublishPics(int type, List<String> pics) {
        List<PrivateChatItemBean> picDates = new ArrayList<>();
        for (int i = 0; i < pics.size(); i++) {
            PrivateChatItemBean bean = new PrivateChatItemBean(1);
            bean.setSelf(true);
            bean.setChatId(chatId);
            bean.setSelfStatus(1);
            bean.setSu(SharedPrefUtil.getInt(userid_key, 0));
            bean.setImg(SharedPrefUtil.getString(head_key, ""));
            bean.setMt(type);
            bean.setC1(pics.get(i));
            bean.setC2(pics.get(i));
            bean.setMs((System.currentTimeMillis() + i) / 1000);
            bean.setTime(System.currentTimeMillis() + i);
            bean.setLastms(firstItemMs);
            picDates.add(bean);
        }
        mData.addAll(picDates);
        mAdapter.notifyItemRangeInserted(mData.size() - picDates.size(), picDates.size());
        showEmptyView();
        mBinding.recycleChatting.scrollToPosition(mAdapter.getItemCount() - 1);
        mBinding.editContent.setText("");
        return picDates.get(0);
    }

    private void clickEditView() {
        mBinding.flayoutChatPicRoot.setVisibility(View.GONE);
        mBinding.ivChatSend.setSelected(false);
        mBinding.llayoutChatEmojiRoot.setVisibility(View.GONE);
        mBinding.ivChatEmoji.setSelected(false);
        showKeyboard(mBinding.editContent);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mBinding.recycleChatting.scrollToPosition(mAdapter.getItemCount() - 1);
            }
        }, 200);

    }

    private void showOrHideMore() {
        if (mBinding.flayoutChatPicRoot.getVisibility() == View.VISIBLE) {
            mBinding.ivChatSend.setSelected(false);
            mBinding.flayoutChatPicRoot.setVisibility(View.GONE);
        } else {
            hideKeyboard();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBinding.ivChatSend.setSelected(true);
                    mBinding.llayoutChatEmojiRoot.setVisibility(View.GONE);
                    mBinding.flayoutChatPicRoot.setVisibility(View.VISIBLE);
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mBinding.recycleChatting.scrollToPosition(mAdapter.getItemCount() - 1);
                        }
                    }, 50);
                }
            }, 50);

        }
    }

    private void showOrHideEmoji() {
        if (mBinding.ivChatEmoji.isSelected()) {
            mBinding.ivChatEmoji.setSelected(false);
            mBinding.flayoutChatPicRoot.setVisibility(View.GONE);
            mBinding.llayoutChatEmojiRoot.setVisibility(View.GONE);
            mBinding.ivChatSend.setSelected(false);
            showKeyboard(mBinding.editContent);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBinding.recycleChatting.scrollToPosition(mAdapter.getItemCount() - 1);
                }
            }, 100);
        } else {
            hideKeyboard();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBinding.ivChatEmoji.setSelected(true);
                    mBinding.flayoutChatPicRoot.setVisibility(View.GONE);
                    mBinding.ivChatSend.setSelected(false);
                    mBinding.llayoutChatEmojiRoot.setVisibility(View.VISIBLE);
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mBinding.recycleChatting.scrollToPosition(mAdapter.getItemCount() - 1);
                        }
                    }, 50);
                }
            }, 50);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_Normal_CODE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    ArrayList<String> picList = data.getStringArrayListExtra(ImageSelector.SELECT_RESULT);
                    if (picList != null && picList.size() > 0) {
                        realPublish(readyPublishPics(2, picList));
                    }
                }
            }
        }
    }

    //上传音视频
    private boolean updataAudioMap(int videoType, String videoPath, String coverPath, int duration) {
        boolean isFinish = true;
        VideoInfoPackage web = MainActivity.audioMap.get(videoPath);
        if (web == null) {
            web = new VideoInfoPackage(videoType, videoPath, coverPath, duration);
            MainActivity.audioMap.put(videoPath, web);
        }
        if (TextUtils.isEmpty(web.webUrl)) {
            isFinish = false;
            uploadVideoOne(videoType, videoPath, coverPath, duration);
        }
        return isFinish;
    }

    private void uploadVideoOne(int videoType, String videoPath, String coverPath, int duration) {
        TXUGCPublishTypeDef.TXPublishParam param = new TXUGCPublishTypeDef.TXPublishParam();
        // signature计算规则可参考 https://www.qcloud.com/document/product/266/9221
        param.signature = Constant.videoSecret;
        param.videoPath = videoPath;
        param.coverPath = coverPath;
        param.duration = duration;
        param.type = videoType;
        int publishCode = mVideoPublish.publishVideo(param);
        if (publishCode == 1012) {
            TXUGCPublishTypeDef.TXPublishResult result = new TXUGCPublishTypeDef.TXPublishResult();
            result.retCode = publishCode;
            result.localPath = videoPath;
            result.duration = duration;
            onPublishComplete(result);
        }
        LogUtil.i("publisherror", "发布失败，错误码：" + publishCode);
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
                File newFile = new File(getExternalCacheDir(), "theme" + ".jpg");
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
        if (mPresenter != null)
            mPresenter.uploadOneReal(file, localpath, terminal_id, tag);
    }

    /**
     * 隐藏软键盘
     */
    protected void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    protected void showKeyboard(EditText et) {
        et.requestFocus();
        ((InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    protected void onStop() {
        mAdapter.stopMediaPlayer();
        super.onStop();
        isPrivateChatting = false;
    }

    @Override
    protected void onDestroy() {
        MessageFragment.ResumeChatId = 0;
        App.getInstance().reportToWeb(7);
        /*if (mPresenter != null)
            mPresenter.destory();
        mPresenter = null;*/

        /*for (PrivateChatItemBean waitData : resendDatas) {
            if (waitData.getSelfStatus() == 1 && !waitData.isHasRunSend()) {
                MainActivity.getInstance().putWaitData(waitData);
            }
        }*/
        for (PrivateChatItemBean waitData : mData) {
            if (waitData.getChatId() != 0 && waitData.getSelfStatus() == 1) {
                waitData.setBackRunning(true);
                MainActivity.getInstance().putWaitData(waitData);
            }
        }
        updateLocalBox();
        if (mVideoPublish != null) {
            mVideoPublish.canclePublish();
            mVideoPublish.setListener(null);
            mVideoPublish = null;
        }
        mAdapter.releaseMediaPlayer();
        mBinding.buttonRecord.releaseRecorder();
        super.onDestroy();
    }

    private void updateLocalBox() {
        if (chatId != 0 && queryBuild != null) {
            queryBuild.remove();
            List<PrivateChatItemBean> boxData = new ArrayList<>();
            int currentNum = 0;
            int currentErrorNum = 0;
            for (int i = mData.size() - 1; i >= 0; i--) {
                PrivateChatItemBean innerThemeListBean = mData.get(i);
                if (innerThemeListBean.getType() != LOADING) {
                    innerThemeListBean.setUid_android(authorId);
                    if (innerThemeListBean.getSelfStatus() == 1 || innerThemeListBean.getSelfStatus() == 2) {
                        if (currentErrorNum < boxMaxNum) {
                            innerThemeListBean.setChatId(chatId);
                            boxData.add(innerThemeListBean);
                            currentErrorNum++;
                        }
                    } else {
                        if (currentNum < boxMaxNum) {
                            innerThemeListBean.setChatId(chatId);
                            boxData.add(innerThemeListBean);
                            currentNum++;
                        }
                    }

                    if ((currentNum + currentErrorNum) >= 40)
                        break;

                }
            }
            if (currentErrorNum < 20 && resendDatas.size() > 0) {
                for (int i = resendDatas.size() - 1; i >= 0; i--) {
                    if (currentErrorNum < boxMaxNum) {
                        resendDatas.get(i).setUid_android(authorId);
                        boxData.add(resendDatas.get(i));
                        currentErrorNum++;
                    } else {
                        break;
                    }
                }
            }

            Collections.reverse(boxData);
            PrivateChatItemBean currentItem = new PrivateChatItemBean();
            currentItem.setMt(400100);
            currentItem.setUid_android(authorId);
            currentItem.setChatId(chatId);
            currentItem.setId(nextCi);
            currentItem.setLastms(firstItemMs);
            boxData.add(currentItem);
            box.put(boxData);
        }
    }

    @Override
    public void onPublishProgress(long uploadBytes, long totalBytes) {

    }

    @Override
    public void onPublishComplete(TXUGCPublishTypeDef.TXPublishResult result) {
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
        } else {
//            if (result.retCode != TVCConstants.ERR_CLIENT_BUSY && result.retCode != TVCConstants.ERR_UGC_PUBLISHING)
            for (int i = mData.size() - 1; i >= 0; i--) {
                if (mData.get(i).getSelfStatus() == 1 && result.localPath.equals(mData.get(i).getC1())) {
                    mData.get(i).setSelfStatus(2);
//                    mData.get(i).setHasRunSend(false);
                    mData.get(i).setBackRunning(false);
                    mAdapter.notifyItemChanged(i);
                    break;
                }
            }
        }

        for (int i = mData.size() - 1; i >= 0; i--) {
            if (mData.get(i).getSelfStatus() == 1 && result.localPath.equals(mData.get(i).getC1())) {
                realPublish(mData.get(i));
                break;
            }
        }

        LogUtil.i("publishcomplete", result.retCode + " Msg:" + (result.retCode == 0 ? result.videoURL : result.descMsg));
    }

    private void showEmptyView() {
        if (mData.size() > 0) {
            for (PrivateChatItemBean data : mData) {
                if (data.getMt() == 1 || data.getMt() == 2 || data.getMt() == 3
                        || data.getMt() == 53 || data.getMt() == 54) {
                    mBinding.llayoutEmpty.setVisibility(View.GONE);
                    return;
                }
            }
            mBinding.llayoutEmpty.setVisibility(View.VISIBLE);
        } else {
            mBinding.llayoutEmpty.setVisibility(View.VISIBLE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(PrivateBackRunBus e) {
        if (e.chatId == chatId) {
            if (e.type == 0) {
                boolean isFind = false;
                for (int i = mData.size() - 1; i >= 0; i--) {
                    if (mData.get(i).getTime() == e.time) {
                        isFind = true;
                        mData.get(i).setSelfStatus(2);
//                        mData.get(i).setHasRunSend(false);
                        mData.get(i).setBackRunning(false);
                        if (!TextUtils.isEmpty(e.id))
                            mData.get(i).setTm(e.id);
                        mAdapter.notifyItemChanged(i);
                        if (!TextUtils.isEmpty(e.id)) {
                            if (linearLayoutManager.getItemCount() - 1 == linearLayoutManager.findLastVisibleItemPosition())
                                mBinding.recycleChatting.smoothScrollToPosition(mAdapter.getItemCount() - 1);
                        }
                        break;
                    }
                }

                if (!isFind) {
                    for (PrivateChatItemBean errorBean : resendDatas) {
                        if (errorBean.getTime() == e.time) {
                            errorBean.setSelfStatus(2);
//                            errorBean.setHasRunSend(false);
                            errorBean.setBackRunning(false);
                            if (!TextUtils.isEmpty(e.id))
                                errorBean.setTm(e.id);
                            break;
                        }
                    }
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BackPrivateChatBus e) {
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SocketStatusBus e) {
        socketStatus = e.status;
        updateSocketView();
    }

    private void updateSocketView() {
        if (socketStatus == 2) {
            mBinding.txtChatSocketview.setVisibility(View.GONE);
            if (!isIndex) {
                isIndex = true;
                if (chatId != 0) {
                    getIndexChatList();
                } else {
                    mPresenter.addChat(userid, System.currentTimeMillis());
                }
            } else {
                getNextChatList();
            }
        } else {
            mBinding.txtChatSocketview.setText("连接中...");
            mBinding.txtChatSocketview.setVisibility(View.VISIBLE);
            for (int i = 0; i < mData.size(); i++) {
                if (mData.get(i).getSelfStatus() == 1) {
                    mData.get(i).setSelfStatus(2);
//                    mData.get(i).setHasRunSend(false);
                    mData.get(i).setBackRunning(false);
                    mAdapter.notifyItemChanged(i);
                }
            }

        }
        mAdapter.removeLoading();
        mBinding.chatrootview.loadFinish();
    }

    @Override
    protected boolean isBindEventBus() {
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (chatId != 0 && queryBuild != null) {
            queryBuild.remove();
        }
    }

    @Override
    protected boolean adapterFitSystemWindows() {
        return true;
    }

    private boolean isPreventGo() {
        return !isSoftShowing() && mBinding.flayoutChatPicRoot.getVisibility() == View.GONE && mBinding.llayoutChatEmojiRoot.getVisibility() == View.GONE && mBinding.flayoutRecord.getVisibility() == View.GONE;
    }

    private boolean isSoftShowing() {
        //获取当前屏幕内容的高度
        int screenHeight = getWindow().getDecorView().getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return screenHeight - rect.bottom - getSoftButtonsBarHeight() >= 128;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private int getSoftButtonsBarHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        //这个方法获取可能不是真实屏幕的高度
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        //获取当前屏幕的真实高度
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        } else {
            return 0;
        }
    }

/*    @Override
    public void onBackPressed() {
        if (hasUnSendMes()) {
            closePoint();
            return;
        }
        super.onBackPressed();
    }*/

    private void closePoint() {
        if (pointPop == null)
            pointPop = new PointPoppubWindow(provideActivity());
        pointPop.setPointInfo("现在离开对话，您正在发送的消息将失败，您确定要离开吗？", null, false, "取消", "确认");
        pointPop.setDismissListener(new PointPoppubWindow.DismissListener() {
            @Override
            public void dismiss() {

            }

            @Override
            public void clickOne() {

            }

            @Override
            public void clickleft() {

            }

            @Override
            public void clickRight() {
                finish();
            }
        });
        pointPop.show();
    }

    private boolean hasUnSendMes() {
        for (PrivateChatItemBean data : mData) {
            if (data.getSelfStatus() == 1) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        rawX = (int) ev.getRawX();
        rawY = (int) ev.getRawY();
        return super.dispatchTouchEvent(ev);
    }
}
