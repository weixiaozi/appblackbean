package bbs.com.xinfeng.bbswork.mvp.presenter;

import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.model.MessageModel;

/**
 * Created by dell on 2017/10/17.
 */

public class MessagePresenter extends NetPresenter {
    private MessageModel mModel;

    public MessagePresenter(NetContract.INetView netView) {
        super(netView);
        netModel = mModel = new MessageModel(this, netView.provideActivity());
    }

    public void sychronizeList(String gi, String ci, long s) {
        mModel.sychronizeList(gi, ci, s);
    }

    @Override
    public void destory() {
        mModel.destory();
        super.destory();
    }

    public void setStatus(int sessionId, int status, int tag) {
        mModel.setStatus(sessionId,status,tag);
    }
}
