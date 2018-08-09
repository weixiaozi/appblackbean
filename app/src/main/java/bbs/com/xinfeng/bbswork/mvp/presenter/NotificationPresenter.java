package bbs.com.xinfeng.bbswork.mvp.presenter;

import java.io.File;

import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.model.NotificationModel;
import bbs.com.xinfeng.bbswork.mvp.model.TopicModel;

/**
 * Created by dell on 2017/10/17.
 */

public class NotificationPresenter extends NetPresenter {
    private NotificationModel mModel;

    public NotificationPresenter(NetContract.INetView netView) {
        super(netView);
        netModel = mModel = new NotificationModel(this, netView.provideActivity());
    }


    public void getNotifications(int page, int tag) {
        mModel.getNotification(page, tag);
    }

    public void getNewNotifications(int noticeId, int direct, int tag) {
        mModel.getNewNotification(noticeId, direct, tag);
    }

    public void operationNotification(int topic_id, int user_id, int log_id, String msg, int act, int tag) {
        mModel.operateNotice(topic_id, user_id, log_id, msg, act, tag);
    }

    public void inviteOperationNotification(int topic_id, int log_id, int act, String msg, int tag) {
        mModel.inviteNotice(topic_id, log_id, act, msg, tag);
    }

    public void clearNotification(int tag) {
        mModel.clearNotice(tag);
    }
}
