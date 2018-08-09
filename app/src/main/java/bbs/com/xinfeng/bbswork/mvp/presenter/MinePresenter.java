package bbs.com.xinfeng.bbswork.mvp.presenter;

import java.io.File;

import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.model.MineModel;
import bbs.com.xinfeng.bbswork.mvp.model.TopicModel;

/**
 * Created by dell on 2017/10/17.
 */

public class MinePresenter extends NetPresenter {
    private MineModel mModel;

    public MinePresenter(NetContract.INetView netView) {
        super(netView);
        netModel = mModel = new MineModel(this, netView.provideActivity());
    }

    public void getUserInfo(int tag) {
        mModel.getUserInfo(tag);
    }

    public void getUserContent(int tag) {
        mModel.getUserContent(tag);
    }
}
