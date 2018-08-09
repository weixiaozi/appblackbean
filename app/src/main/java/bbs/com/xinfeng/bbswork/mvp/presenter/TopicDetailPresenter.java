package bbs.com.xinfeng.bbswork.mvp.presenter;


import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.model.TopicDetailModel;

/**
 * Created by dell on 2017/10/17.
 */

public class TopicDetailPresenter extends NetPresenter {
    private TopicDetailModel mModel;

    public TopicDetailPresenter(NetContract.INetView netView) {
        super(netView);
        netModel = mModel = new TopicDetailModel(this, netView.provideActivity());
    }


    public void getTopicDetail(String topicid, int tag) {
        mModel.getTopicDetail(topicid, tag);
    }

    public void outTopic(String id, int tag) {
        mModel.outTopic(id, tag);
    }

    public void readTheme(int id, int tag) {
        mModel.readTheme(id, tag);
    }

    public void readThemeAudio(int id, int tag) {
        mModel.readThemeAudio(id, tag);
    }
}
