package bbs.com.xinfeng.bbswork.mvp.presenter;

import java.io.File;

import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.model.MainModel;

/**
 * Created by dell on 2017/10/24.
 */

public class MainPresenter extends NetPresenter {
    MainModel mModel;

    public MainPresenter(NetContract.INetView netView) {
        super(netView);
        netModel = mModel = new MainModel(this, netView.provideActivity());
    }

    public void upgradeCheck(int tag) {
        mModel.upgradeCheck(tag);
    }

    public boolean sendRealMes(int chatId, int type, String content, long time, boolean hasRunSend) {
        return mModel.sendRealMes(chatId, type, content, time, hasRunSend);
    }

    public void uploadOneReal(File file, String localpath, long terminal_id, int tag) {
        mModel.uploadPicOneReal(file, localpath, terminal_id, tag);
    }

    @Override
    public void destory() {
        mModel.destory();
        super.destory();
    }
}
