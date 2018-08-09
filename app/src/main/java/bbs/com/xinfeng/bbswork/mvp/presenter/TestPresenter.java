package bbs.com.xinfeng.bbswork.mvp.presenter;

import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.model.TestModel;

/**
 * Created by dell on 2017/10/17.
 */

public class TestPresenter extends NetPresenter {
    private TestModel mModel;

    public TestPresenter(NetContract.INetView netView) {
        super(netView);
        netModel = mModel = new TestModel(this, netView.provideActivity());
    }


    public void getBphp(int tag) {
        mModel.getBphp(tag);
    }

}
