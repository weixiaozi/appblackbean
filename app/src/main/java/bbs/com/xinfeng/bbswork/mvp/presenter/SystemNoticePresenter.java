package bbs.com.xinfeng.bbswork.mvp.presenter;

import java.io.File;

import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.model.SystemNoticeModel;
import bbs.com.xinfeng.bbswork.mvp.model.TopicModel;

/**
 * Created by dell on 2017/10/17.
 */

public class SystemNoticePresenter extends NetPresenter {
    private SystemNoticeModel mModel;

    public SystemNoticePresenter(NetContract.INetView netView) {
        super(netView);
        netModel = mModel = new SystemNoticeModel(this, netView.provideActivity());
    }

    public void getNoticeList(int noticetype, int page, int tag) {
        mModel.getNoticeList(noticetype, page, tag);
    }
}
