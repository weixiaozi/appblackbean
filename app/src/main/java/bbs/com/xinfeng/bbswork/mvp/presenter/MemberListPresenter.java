package bbs.com.xinfeng.bbswork.mvp.presenter;

import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.model.MemberListModel;
import bbs.com.xinfeng.bbswork.mvp.model.NotificationModel;

/**
 * Created by dell on 2017/10/17.
 */

public class MemberListPresenter extends NetPresenter {
    private MemberListModel mModel;

    public MemberListPresenter(NetContract.INetView netView) {
        super(netView);
        netModel = mModel = new MemberListModel(this, netView.provideActivity());
    }



    public void getMembersIn(int topoicId, int page, int tag) {
        mModel.getMembersIn(topoicId, page, tag);
    }

    public void getMembersOut(int topoicId, int tag) {
        mModel.getMembersOut(topoicId, tag);
    }

    public void inviteUsers(int topoicId, String ids, String channel, int tag) {
        mModel.inviteUser(topoicId, ids, channel, tag);
    }

    public void removeUsers(int topoicId, String ids, int tag) {
        mModel.removeUser(topoicId, ids, tag);
    }
}
