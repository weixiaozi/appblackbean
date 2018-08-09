package bbs.com.xinfeng.bbswork.mvp.model;

import android.app.Activity;
import android.util.ArrayMap;

import org.json.JSONException;
import org.json.JSONObject;

import bbs.com.xinfeng.bbswork.base.App;
import bbs.com.xinfeng.bbswork.domin.BaseBean;
import bbs.com.xinfeng.bbswork.domin.MessageFragmentBean;
import bbs.com.xinfeng.bbswork.domin.PrivateMesChangedBean;
import bbs.com.xinfeng.bbswork.domin.SendPrivateMesBean;
import bbs.com.xinfeng.bbswork.domin.SendToServerIsSuccessBean;
import bbs.com.xinfeng.bbswork.domin.TestBean;
import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.utils.SocketUtil;
import io.reactivex.Flowable;

import static bbs.com.xinfeng.bbswork.domin.ErrorBean.TYPE_DEAL;
import static bbs.com.xinfeng.bbswork.domin.ErrorBean.TYPE_SHOW;

/**
 * Created by dell on 2017/10/20.
 */

public class MessageModel extends NetModel implements App.OnMessageCallBack {
    public static final int SOCKET_MES_TAG = 999999;

    public MessageModel(NetContract.OnDataLoadingListener dataLoadingListener, Activity activity) {
        super(dataLoadingListener, activity);
        App.getInstance().registerSocketListen(this);
    }


    public void sychronizeList(String gi, String ci, long s) {
        App.getInstance().sendmes(SocketUtil.synchronizeMesList(gi, ci, s), s + "", 8, 0);
    }

    @Override
    public void messageCallback(String content) {
        try {
            JSONObject jsonObject = new JSONObject(content);
            int a = jsonObject.getInt("a");
            switch (a) {
                case 5:
                    PrivateMesChangedBean mesChangedBean = gson.fromJson(content, PrivateMesChangedBean.class);
                    dataLoadingListener.onSuccess(mesChangedBean, SOCKET_MES_TAG, true);
                    break;
                case 8:
                    MessageFragmentBean mesListBean = gson.fromJson(content, MessageFragmentBean.class);
                    if (mesListBean.getCode() == BaseBean.SUCCESS_CODE) {
                        dataLoadingListener.onSuccess(mesListBean, SOCKET_MES_TAG, true);
                    } else {
                        mesListBean.androidcode = mesListBean.getCode() + "";
                        mesListBean.desc = mesListBean.getNotice() == null ? mesListBean.getMessage() : mesListBean.getNotice();
                        mesListBean.androidType = mesListBean.getNotice() == null ? TYPE_DEAL : TYPE_SHOW;
                        dataLoadingListener.onError(mesListBean, SOCKET_MES_TAG);
                    }
                    break;
            }
        } catch (JSONException e) {
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

    public void setStatus(int sessionId, int status, int tag) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("session_id", sessionId + "");
        params.put("status", status + "");
        packageData(retrofitService.setMesListStatus(params), tag);
    }
}
