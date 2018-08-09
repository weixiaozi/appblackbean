package bbs.com.xinfeng.bbswork.mvp.presenter;

import java.io.File;

import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.model.SettingModel;

/**
 * Created by dell on 2017/11/29.
 */

public class SettingPresenter extends NetPresenter {
    private SettingModel mModel;

    public SettingPresenter(NetContract.INetView netView) {
        super(netView);
        netModel = mModel = new SettingModel(this, netView.provideActivity());
    }

    public void getPushSetting(int tag) {
        mModel.getPushSetting(tag);
    }

    public void setPushSetting(int type, int tag) {
        mModel.setPushSetting(type, tag);
    }

    public void pushFeedback(int type, String content, int tag) {
        mModel.pushFeedback(type, content, tag);
    }

    public void outLogin(int tag) {
        mModel.outLogin(tag);
    }

    public void upgradeCheck(int tag) {
        mModel.upgradeCheck(tag);
    }
}
