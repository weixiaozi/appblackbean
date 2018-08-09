package bbs.com.xinfeng.bbswork.mvp.presenter;

import java.io.File;

import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.model.PrivateChatModel;

/**
 * Created by dell on 2017/10/24.
 */

public class PrivateChatPresenter extends NetPresenter {
    PrivateChatModel mModel;

    public PrivateChatPresenter(NetContract.INetView netView) {
        super(netView);
        netModel = mModel = new PrivateChatModel(this, netView.provideActivity());
    }

    public void upgradeCheck(int tag) {
        if (mModel != null)
            mModel.upgradeCheck(tag);
    }

    public boolean addChat(int userid, long time) {
        if (mModel != null)
            return mModel.addChat(userid, time + "");
        return false;
    }

    @Override
    public void destory() {
        if (mModel != null)
            mModel.destory();
        super.destory();
    }

    public boolean sendRealMes(int chatId, int type, String content, long time, boolean hasRunSend) {
        if (mModel != null)
            return mModel.sendRealMes(chatId, type, content, time, hasRunSend);
        return false;
    }


    public void uploadOneReal(File file, String localpath, long terminal_id, int tag) {
        if (mModel != null)
            mModel.uploadPicOneReal(file, localpath, terminal_id, tag);
    }

    public boolean getChatList(int chatId, String ci, String type, long s) {
        if (mModel != null)
            return mModel.getChatList(chatId, ci, type, s);
        return false;
    }

    public boolean operateMes(int a, int si, String ci) {
        if (mModel != null)
            return mModel.operateMes(a, si, ci);
        return false;
    }

    public void getUserinfo(int userid, int chatid, int tag) {
        if (mModel != null)
            mModel.getUserInfo(userid, chatid, tag);
    }

    public void actBlack(int userid, int act, int tag) {
        if (mModel != null)
            mModel.actBlack(userid, act, tag);
    }

    public void setStatus(int sessionId, int status, int tag) {
        if (mModel != null)
            mModel.setStatus(sessionId, status, tag);
    }
}
