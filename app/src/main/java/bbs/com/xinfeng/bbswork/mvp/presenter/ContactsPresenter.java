package bbs.com.xinfeng.bbswork.mvp.presenter;

import java.io.File;

import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.model.ContactsModel;
import bbs.com.xinfeng.bbswork.mvp.model.TopicModel;

/**
 * Created by dell on 2017/10/17.
 */

public class ContactsPresenter extends NetPresenter {
    private ContactsModel mModel;

    public ContactsPresenter(NetContract.INetView netView) {
        super(netView);
        netModel = mModel = new ContactsModel(this, netView.provideActivity());
    }


    public void register(String user, String password, int tag) {
        mModel.register(user, password, tag);
    }

    public void getContactList(String name, int page, int tag) {
        mModel.getContactList(name, page, tag);
    }
}
