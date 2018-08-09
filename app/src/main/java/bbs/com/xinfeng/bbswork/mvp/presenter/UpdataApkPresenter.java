package bbs.com.xinfeng.bbswork.mvp.presenter;

import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.model.UpdataApkModel;

/**
 * Created by dell on 2017/11/22.
 */

public class UpdataApkPresenter extends NetPresenter {
    private UpdataApkModel mModel;

    public UpdataApkPresenter(NetContract.INetView netView) {
        super(netView);
        netModel = mModel = new UpdataApkModel(this, netView.provideActivity());
    }

    public void downloadApk(String downUrl, String path, String name, int tag) {
        mModel.downloadApk(downUrl, path, name, tag);
    }
}
