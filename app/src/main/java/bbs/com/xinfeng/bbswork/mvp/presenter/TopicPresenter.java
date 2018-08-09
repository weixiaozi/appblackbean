package bbs.com.xinfeng.bbswork.mvp.presenter;

import java.io.File;

import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.model.TestModel;
import bbs.com.xinfeng.bbswork.mvp.model.TopicModel;

/**
 * Created by dell on 2017/10/17.
 */

public class TopicPresenter extends NetPresenter {
    private TopicModel mModel;

    public TopicPresenter(NetContract.INetView netView) {
        super(netView);
        netModel = mModel = new TopicModel(this, netView.provideActivity());
    }


    public void creatTopic(String title, String introduce, String image, int tag) {
        mModel.creatTopic(title, introduce, image, tag);
    }

    public void changeTopic(String id, String title, String introduce, String image, int tag) {
        mModel.changeTopic(id, title, introduce, image, tag);
    }

    public void getTopicUsers(String id, int tag) {
        mModel.getTopicUsers(id, tag);
    }

    public void joinTopic(String id, String channel, int tag) {
        mModel.joinTopic(id, channel, tag);
    }

    public void outTopic(String id, int tag) {
        mModel.outTopic(id, tag);
    }

    public void register(String user, String password, int tag) {
        mModel.register(user, password, tag);
    }

    public void uploadPic(File newFile, int uploadpicTag) {
        mModel.uploadPic(newFile, uploadpicTag);
    }

    public void getTopicList(String name, int page, int tag) {
        mModel.getTopicList(name, page, tag);
    }
}
