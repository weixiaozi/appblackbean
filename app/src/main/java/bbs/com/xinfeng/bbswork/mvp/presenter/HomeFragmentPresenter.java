package bbs.com.xinfeng.bbswork.mvp.presenter;

import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.model.HomeFragmentModel;
import bbs.com.xinfeng.bbswork.mvp.model.TestModel;

/**
 * Created by dell on 2017/10/17.
 */

public class HomeFragmentPresenter extends NetPresenter {
    private HomeFragmentModel mModel;

    public HomeFragmentPresenter(NetContract.INetView netView) {
        super(netView);
        netModel = mModel = new HomeFragmentModel(this, netView.provideActivity());
    }


    public void getTopicList(String topicIds, int tag) {
        mModel.getTopicList(topicIds,tag);
    }
}
