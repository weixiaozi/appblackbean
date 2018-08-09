package bbs.com.xinfeng.bbswork.mvp.model;

import android.content.Context;
import android.util.ArrayMap;

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
import io.reactivex.Flowable;

import static bbs.com.xinfeng.bbswork.domin.ErrorBean.TYPE_DEAL;
import static bbs.com.xinfeng.bbswork.domin.ErrorBean.TYPE_SHOW;
import static bbs.com.xinfeng.bbswork.domin.UploadBean.File_pic;
import static bbs.com.xinfeng.bbswork.domin.UploadBean.Pic_chat;

/**
 * Created by dell on 2017/10/24.
 */

public class MainModel extends NetModel implements App.OnMessageCallBack {
    public static final int SOCKET_MES_TAG = 999999;

    public MainModel(NetContract.OnDataLoadingListener dataLoadingListener, Context activity) {
        super(dataLoadingListener, activity);
        App.getInstance().registerSocketListen(this);
    }

    public void upgradeCheck(int tag) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("channel", SystemUtil.getChannelCode(mContext));
        params.put("version", SystemUtil.getAppVersion() + "");

        packageData(retrofitService.getUpGradeInfo(params), tag);
    }

    public boolean sendRealMes(int chatId, int type, String content, long time, boolean hasRunSend) {
        if (hasRunSend) {
            LogUtil.i("sendrealmessagemain", SocketUtil.sendChat(chatId, type, content, time, 2));
            return App.getInstance().sendmes(SocketUtil.sendChat(chatId, type, content, time, 2), time + "", 4, chatId);
        } else {
            LogUtil.i("sendrealmessagemain", SocketUtil.sendChat(chatId, type, content, time));
            return App.getInstance().sendmes(SocketUtil.sendChat(chatId, type, content, time), time + "", 4, chatId);
        }
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
            }
            LogUtil.i("privatemessageocketmain:", content);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void errorCallback(int errCode) {

    }

    @Override
    public void sendToServeCallback(String key, int status, int a, int chatId) {
        dataLoadingListener.onSuccess(new SendToServerIsSuccessBean(key, status, a, chatId), SOCKET_MES_TAG, true);
    }


    public void destory() {
        App.getInstance().unregisterSocketListen(this);
    }
}
