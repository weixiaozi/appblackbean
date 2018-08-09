package bbs.com.xinfeng.bbswork.mvp.presenter;

import java.io.File;

import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.model.TopicModel;
import bbs.com.xinfeng.bbswork.mvp.model.UserInfoModel;

/**
 * Created by dell on 2017/10/17.
 */

public class UserInfoPresenter extends NetPresenter {
    private UserInfoModel mModel;

    public UserInfoPresenter(NetContract.INetView netView) {
        super(netView);
        netModel = mModel = new UserInfoModel(this, netView.provideActivity());
    }


    public void followAction(int type, int userid, int tag) {
        mModel.followAction(type, userid, tag);
    }

    public void getUserinfoList(int userid, int page, int tag) {
        mModel.getUserInfoList(userid, page, tag);
    }

    public void actBlack(int userid, int act, int tag) {
        mModel.actBlack(userid,act,tag);
    }
}
