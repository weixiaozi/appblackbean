package bbs.com.xinfeng.bbswork.mvp.presenter;

import java.io.File;

import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.model.TopicModel;
import bbs.com.xinfeng.bbswork.mvp.model.TopicOperateModel;

/**
 * Created by dell on 2017/10/17.
 */

public class TopicOperatePresenter extends NetPresenter {
    private TopicOperateModel mModel;

    public TopicOperatePresenter(NetContract.INetView netView) {
        super(netView);
        netModel = mModel = new TopicOperateModel(this, netView.provideActivity());
    }

    public void applyInTopic(String topicid, String info, int tag) {
        mModel.applyIntopic(topicid, info, tag);

    }
}
