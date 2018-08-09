package bbs.com.xinfeng.bbswork.mvp.presenter;

import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.model.TestModel;
import bbs.com.xinfeng.bbswork.mvp.model.WebviewModel;

/**
 * Created by dell on 2017/10/17.
 */

public class WebviewPresenter extends NetPresenter {
    private WebviewModel mModel;

    public WebviewPresenter(NetContract.INetView netView) {
        super(netView);
        netModel = mModel = new WebviewModel(this, netView.provideActivity());
    }


    public void getBphp(int tag) {
        mModel.getBphp(tag);
    }

}
