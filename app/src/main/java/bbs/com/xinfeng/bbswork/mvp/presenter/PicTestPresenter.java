package bbs.com.xinfeng.bbswork.mvp.presenter;

import java.io.File;

import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.model.PicTestModel;

/**
 * Created by dell on 2017/11/29.
 */

public class PicTestPresenter extends NetPresenter {
    private PicTestModel mModel;

    public PicTestPresenter(NetContract.INetView netView) {
        super(netView);
        netModel = mModel = new PicTestModel(this, netView.provideActivity());
    }

    public void uploadPic(File newFile, int uploadpicTag) {
        mModel.uploadPic(newFile,uploadpicTag);
    }
}
