package bbs.com.xinfeng.bbswork.mvp.presenter;

import java.io.File;

import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.model.ModifyInfoModel;

/**
 * Created by dell on 2017/10/17.
 */

public class ModifyInfoPresenter extends NetPresenter {
    private ModifyInfoModel mModel;

    public ModifyInfoPresenter(NetContract.INetView netView) {
        super(netView);
        netModel = mModel = new ModifyInfoModel(this, netView.provideActivity());
    }


    public void getUserInfo(int tag) {
        mModel.getUserInfo(tag);
    }

    public void modifyInfo(String name, String portrait, String introduce, int tag) {
        mModel.modifyInfo(name, portrait, introduce, tag);
    }

    public void uploadPic(File newFile, int uploadpicTag) {
        mModel.uploadPic(newFile, uploadpicTag);
    }

    public void outLogin(int tag) {
        mModel.outLogin(tag);
    }
}
