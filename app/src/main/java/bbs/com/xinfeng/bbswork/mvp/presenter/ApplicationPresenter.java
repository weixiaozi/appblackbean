package bbs.com.xinfeng.bbswork.mvp.presenter;

import bbs.com.xinfeng.bbswork.base.Constant;
import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.model.ApplicationModel;
import bbs.com.xinfeng.bbswork.mvp.model.MainModel;

/**
 * Created by dell on 2017/10/24.
 */

public class ApplicationPresenter extends NetPresenter {
    private ApplicationModel mModel;

    public ApplicationPresenter(NetContract.INetView netView) {
        super(netView);
        netModel = mModel = new ApplicationModel(this, netView.provideActivity());
    }

    public void setPushId(int tag) {
        mModel.setPushid(tag);
    }

    public void getToken(String username, String password, int tag) {
        mModel.getToken(username, password, tag);
    }

    public void refreshToken(int tag) {
        mModel.refreshToken(tag);
    }

    public void getIpAddress(int tag) {
        mModel.getSocketAddress(tag);
    }

    public void notifyMessage(long current, int tag) {
        mModel.notifyMessage(current, tag);
    }

    public void getUserInfo(int tag) {
        mModel.getUserInfo(tag);
    }

    public void reportToWeb(int type, int tag) {
        mModel.reportToWeb(type, tag);
    }

    public void getVideoSecret(int videoSecret_tag) {
        mModel.getVideoSecret(videoSecret_tag);
    }
}
