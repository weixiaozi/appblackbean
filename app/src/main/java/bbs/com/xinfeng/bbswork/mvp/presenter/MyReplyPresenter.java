package bbs.com.xinfeng.bbswork.mvp.presenter;

import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.model.MyReplyModel;
import bbs.com.xinfeng.bbswork.mvp.model.MythemeModel;

/**
 * Created by dell on 2017/10/17.
 */

public class MyReplyPresenter extends NetPresenter {
    private MyReplyModel mModel;

    public MyReplyPresenter(NetContract.INetView netView) {
        super(netView);
        netModel = mModel = new MyReplyModel(this, netView.provideActivity());
    }


    public void getMyReplyList(int pageid, int tag) {
        mModel.getMyreply(pageid, tag);
    }

    public void getReplyMe(int pageid, int tag) {
        mModel.getReplyMe(pageid, tag);
    }

    public void getClickme(int pageid, int tag) {
        mModel.getClickme(pageid, tag);
    }
}
