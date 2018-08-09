package bbs.com.xinfeng.bbswork.mvp.presenter;

import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.model.MythemeModel;

/**
 * Created by dell on 2017/10/17.
 */

public class MyThemePresenter extends NetPresenter {
    private MythemeModel mModel;

    public MyThemePresenter(NetContract.INetView netView) {
        super(netView);
        netModel = mModel = new MythemeModel(this, netView.provideActivity());
    }


    public void getMyThemeList(int threadid, int tag) {
        mModel.getThemeList(threadid, tag);
    }

    public void upDataThemeInfo(String threadid, int tag) {
        mModel.upDataThemeInfo(threadid, tag);
    }

    public void readThemeAudio(int id, int tag) {
        mModel.readThemeAudio(id, tag);
    }
}
