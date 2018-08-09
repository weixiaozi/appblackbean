package bbs.com.xinfeng.bbswork.mvp.presenter;

import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.model.MyFansModel;
import bbs.com.xinfeng.bbswork.mvp.model.MythemeModel;

/**
 * Created by dell on 2017/10/17.
 */

public class MyFansPresenter extends NetPresenter {
    private MyFansModel mModel;

    public MyFansPresenter(NetContract.INetView netView) {
        super(netView);
        netModel = mModel = new MyFansModel(this, netView.provideActivity());
    }

    public void getFansList(int page, int type, int tag) {
        mModel.getFansList(page, type, tag);
    }

    public void followAction(int type, int userid, int tag) {
        mModel.followAction(type, userid, tag);
    }

    public void actBlack(int user_id, int act, int tag) {
        mModel.actBlack(user_id, act, tag);
    }

    public void getBlackList(int page, int tag) {
        mModel.getBlackList(page, tag);
    }
}
