package bbs.com.xinfeng.bbswork.mvp.presenter;

import java.util.List;

import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.model.CollectionModel;

/**
 * Created by dell on 2017/10/17.
 */

public class CollectionPresenter extends NetPresenter {
    private CollectionModel mModel;

    public CollectionPresenter(NetContract.INetView netView) {
        super(netView);
        netModel = mModel = new CollectionModel(this, netView.provideActivity());
    }


    public void getCollectionList(int page, int type, String current, int tag) {
        mModel.getCollectionList(page, type, current, tag);
    }

    public void cancelCollection(List<String> deleteList, int tag) {
        mModel.cancelCollection(deleteList, tag);
    }

    public void updataThemeInfo(String ids, int tag) {
        mModel.updataThemeInfo(ids, tag);
    }

    public void readThemeAudio(int threadId, int tag) {
        mModel.readThemeAudio(threadId, tag);
    }

    public void readTheme(int id, int tag) {
        mModel.readTheme(id, tag);
    }
}
