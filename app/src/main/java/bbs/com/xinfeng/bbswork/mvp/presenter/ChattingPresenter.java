package bbs.com.xinfeng.bbswork.mvp.presenter;

import java.io.File;
import java.util.List;

import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.model.ChattingModel;

/**
 * Created by dell on 2017/10/17.
 */

public class ChattingPresenter extends NetPresenter {
    private ChattingModel mModel;

    public ChattingPresenter(NetContract.INetView netView) {
        super(netView);
        netModel = mModel = new ChattingModel(this, netView.provideActivity());
    }


    public void publishTheme(String topicId, String content, List<String> picList, String terminal_id, int tag) {
        mModel.publishTheme(topicId, content, picList, terminal_id, tag);
    }

    public void getThemeList(String topicId, String theme_id, int direct, int label, int tag) {
        mModel.getThemeList(topicId, theme_id, direct, label, tag);
    }

    public void upLoadPic(File file, int tag) {
        mModel.uploadPic(file, tag);
    }

    public void upLoadPicOne(File file, String localpath, int tag) {
        mModel.uploadPicOne(file, localpath, tag);
    }

    public void uploadOneReal(File file, String localpath, long terminal_id, int tag) {
        mModel.uploadPicOneReal(file, localpath, terminal_id, tag);
    }

    public void likeOperate(int themeid, int hasLike, int tag) {
        mModel.likeOperate(themeid, hasLike, tag);
    }

    public void getTopicMembers(String topicId, int type, int last_thread_id, int tag) {
        mModel.getTopicMembers(topicId, type, last_thread_id, tag);

    }

    public void operateCollect(int act, int themeid, int tag) {
        mModel.operateCollection(act, themeid, tag);
    }

    public void readTheme(int id, int tag) {
        mModel.readTheme(id, tag);
    }

    public void readThemeAudio(int id, int tag) {
        mModel.readThemeAudio(id, tag);
    }
}
