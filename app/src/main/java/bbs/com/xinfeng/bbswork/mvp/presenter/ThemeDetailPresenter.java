package bbs.com.xinfeng.bbswork.mvp.presenter;

import java.io.File;
import java.util.List;

import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.model.ThemeDetailModel;

/**
 * Created by dell on 2017/10/17.
 */

public class ThemeDetailPresenter extends NetPresenter {
    private ThemeDetailModel mModel;

    public ThemeDetailPresenter(NetContract.INetView netView) {
        super(netView);
        netModel = mModel = new ThemeDetailModel(this, netView.provideActivity());
    }


    public void getThemeDetail(int topicid, int themeid, int tag) {
        mModel.getThemeDetail(topicid, themeid, tag);

    }

    public void likeOperate(int themeid, int hasLike, int postId, int tag) {
        mModel.likeOperate(themeid, hasLike, postId, tag);
    }

    public void operateCollect(int act, int themeid, int tag) {
        mModel.operateCollection(act, themeid, tag);
    }

    public void getReplyList(int themeid, int pid, String direct, int label, String key, int tag) {
        mModel.getReplyList(themeid, pid, direct, label, key, tag);

    }

    public void getReplyTwoList(int themeid, int reply_pid, int pid, String key, int tag) {
        mModel.getReplyTwoList(themeid, reply_pid, pid, key, tag);

    }

    public void getReplyDetail(int themeid, int pid, int tag) {
        mModel.getReplyDetail(themeid, pid, tag);

    }

    public void publishReply(int themeid, String pid, String content, List<String> picList, long terminal_id, int tag) {
        mModel.publishReply(themeid, pid, content, picList, terminal_id, tag);
    }

    public void deleteReply(int themeid, int replyid, int tag) {
        mModel.deleteRepley(themeid, replyid, tag);
    }

    public void deleteTheme(int themeid, int deleteTheme_tag) {
        mModel.deleteTheme(themeid, deleteTheme_tag);
    }

    public void upLoadPicOne(File file, String localpath, int tag) {
        mModel.uploadPicOne(file, localpath, tag);
    }

    public void readThemeAudio(int id, int postid, int tag) {
        mModel.readThemeAudio(id, postid, tag);
    }

    public void uploadOneReal(File file, String localpath, long terminal_id, int tag) {
        mModel.uploadPicOneReal(file, localpath, terminal_id, tag);
    }
}
