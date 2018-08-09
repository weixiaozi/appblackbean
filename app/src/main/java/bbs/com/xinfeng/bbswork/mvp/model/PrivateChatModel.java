package bbs.com.xinfeng.bbswork.mvp.model;

import android.content.Context;
import android.util.ArrayMap;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Map;

import bbs.com.xinfeng.bbswork.base.App;
import bbs.com.xinfeng.bbswork.domin.AddChatBean;
import bbs.com.xinfeng.bbswork.domin.BaseBean;
import bbs.com.xinfeng.bbswork.domin.MesInfoChangeBean;
import bbs.com.xinfeng.bbswork.domin.PrivateChatListBean;
import bbs.com.xinfeng.bbswork.domin.PrivateMesChangedBean;
import bbs.com.xinfeng.bbswork.domin.SendPrivateMesBean;
import bbs.com.xinfeng.bbswork.domin.SendToServerIsSuccessBean;
import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.utils.LogUtil;
import bbs.com.xinfeng.bbswork.utils.SocketUtil;
import bbs.com.xinfeng.bbswork.utils.SystemUtil;

import static bbs.com.xinfeng.bbswork.domin.ErrorBean.TYPE_DEAL;
import static bbs.com.xinfeng.bbswork.domin.ErrorBean.TYPE_SHOW;
import static bbs.com.xinfeng.bbswork.domin.UploadBean.File_pic;
import static bbs.com.xinfeng.bbswork.domin.UploadBean.Pic_chat;

/**
 * Created by dell on 2017/10/24.
 */

public class PrivateChatModel extends NetModel implements App.OnMessageCallBack {
    public static final int SOCKET_MES_TAG = 999999;

    public PrivateChatModel(NetContract.OnDataLoadingListener dataLoadingListener, Context activity) {
        super(dataLoadingListener, activity);
        App.getInstance().registerSocketListen(this);
    }

    public void upgradeCheck(int tag) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("channel", SystemUtil.getChannelCode(mContext));
        params.put("version", SystemUtil.getAppVersion() + "");

        packageData(retrofitService.getUpGradeInfo(params), tag);
    }

    public void uploadPicOneReal(File newFile, String localpath, long terminal_id, int uploadpicTag) {
        Map<String, String> params = new ArrayMap<>();
        params.put("type", File_pic);
        params.put("cate", Pic_chat);
        packageDataUpload(retrofitService.uploadHeadPic(packParamsToRequestBody(params, newFile, uploadpicTag)), terminal_id + "", localpath, uploadpicTag);
    }


    @Override
    public void messageCallback(String content) {
        try {
            JSONObject jsonObject = new JSONObject(content);
            int a = jsonObject.getInt("a");
            switch (a) {
                case 10:
                    AddChatBean addChatBean = gson.fromJson(content, AddChatBean.class);
                    if (addChatBean.getCode() == BaseBean.SUCCESS_CODE) {
                        dataLoadingListener.onSuccess(addChatBean, SOCKET_MES_TAG, true);
                    } else {
                        addChatBean.androidcode = addChatBean.getCode() + "";
                        addChatBean.desc = addChatBean.getNotice() == null ? addChatBean.getMessage() : addChatBean.getNotice();
                        addChatBean.androidType = addChatBean.getNotice() == null ? TYPE_DEAL : TYPE_SHOW;
                        dataLoadingListener.onError(addChatBean, SOCKET_MES_TAG);
                    }
                    break;
                case 4:
                    SendPrivateMesBean sendPrivateMesBean = gson.fromJson(content, SendPrivateMesBean.class);
                    if (sendPrivateMesBean.getCode() == BaseBean.SUCCESS_CODE) {
                        dataLoadingListener.onSuccess(sendPrivateMesBean, SOCKET_MES_TAG, true);
                    } else {
                        sendPrivateMesBean.androidcode = sendPrivateMesBean.getCode() + "";
                        sendPrivateMesBean.desc = sendPrivateMesBean.getNotice() == null ? sendPrivateMesBean.getMessage() : sendPrivateMesBean.getNotice();
                        sendPrivateMesBean.androidType = sendPrivateMesBean.getNotice() == null ? TYPE_DEAL : TYPE_SHOW;
                        dataLoadingListener.onError(sendPrivateMesBean, SOCKET_MES_TAG);
                    }
                    break;
                case 9:
                    PrivateChatListBean chatListBean = gson.fromJson(content, PrivateChatListBean.class);
                    if (chatListBean.getCode() == BaseBean.SUCCESS_CODE) {
                        dataLoadingListener.onSuccess(chatListBean, SOCKET_MES_TAG, true);
                    } else {
                        chatListBean.androidcode = chatListBean.getCode() + "";
                        chatListBean.desc = chatListBean.getNotice() == null ? chatListBean.getMessage() : chatListBean.getNotice();
                        chatListBean.androidType = chatListBean.getNotice() == null ? TYPE_DEAL : TYPE_SHOW;
                        dataLoadingListener.onError(chatListBean, SOCKET_MES_TAG);
                    }
                    break;
                case 5:
                    PrivateMesChangedBean mesChangedBean = gson.fromJson(content, PrivateMesChangedBean.class);
                    dataLoadingListener.onSuccess(mesChangedBean, SOCKET_MES_TAG, true);
                    break;
                case 15:
                    MesInfoChangeBean mesInfoChangedBean = gson.fromJson(content, MesInfoChangeBean.class);
                    dataLoadingListener.onSuccess(mesInfoChangedBean, SOCKET_MES_TAG, true);
                    break;
            }
            LogUtil.i("privatemessageocketprivate:", content);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void errorCallback(int errCode) {
        LogUtil.i("privateerrorsocket:", errCode + "___");
    }

    @Override
    public void sendToServeCallback(String key, int status, int a, int chatId) {
        dataLoadingListener.onSuccess(new SendToServerIsSuccessBean(key, status, a, chatId), SOCKET_MES_TAG, true);
    }


    public void destory() {
        App.getInstance().unregisterSocketListen(this);
    }

    public boolean sendRealMes(int chatId, int type, String content, long time, boolean hasRunSend) {
        if (hasRunSend) {
            LogUtil.i("sendrealmessageprivate", SocketUtil.sendChat(chatId, type, content, time, 2));
            return App.getInstance().sendmes(SocketUtil.sendChat(chatId, type, content, time, 2), time + "", 4, chatId);
        } else {
            LogUtil.i("sendrealmessageprivate", SocketUtil.sendChat(chatId, type, content, time));
            return App.getInstance().sendmes(SocketUtil.sendChat(chatId, type, content, time), time + "", 4, chatId);
        }
    }

    public boolean addChat(int userid, String s) {
        return App.getInstance().sendmes(SocketUtil.addChat(userid, s), s, 10, -1);
    }

    public boolean getChatList(int chatId, String ci, String type, long s) {
        return App.getInstance().sendmes(SocketUtil.getChatList(chatId, ci, type, s), s + "", 9, chatId);
    }

    public boolean operateMes(int a, int si, String ci) {
        String s = System.currentTimeMillis() + "";
        return App.getInstance().sendmes(SocketUtil.operateMes(a, si, ci, s), s, a, si);
    }

    public void getUserInfo(int userid, int chatid, int tag) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("user_id", userid + "");
        params.put("session_id", chatid + "");
        packageData(retrofitService.getPrivateAttachInfo(params), tag);
    }

    public void actBlack(int userid, int act, int tag) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("target_id", userid + "");
        params.put("act", act + "");
        packageData(retrofitService.blackAction(params), tag);
    }

    public void setStatus(int sessionId, int status, int tag) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("session_id", sessionId + "");
        params.put("status", status + "");
        packageData(retrofitService.setMesListStatus(params), tag);
    }
}
