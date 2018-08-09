package bbs.com.xinfeng.bbswork.mvp.presenter;

import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.model.LoginModel;

/**
 * Created by dell on 2017/10/17.
 */

public class LoginPresenter extends NetPresenter {
    private LoginModel mModel;

    public LoginPresenter(NetContract.INetView netView) {
        super(netView);
        netModel = mModel = new LoginModel(this, netView.provideActivity());
    }


    public void getCode(String tel, int tag) {
        mModel.getCode(tel, tag);
    }

    public void register(String tel, String code, int tag) {
        mModel.register(tel, code, tag);
    }
}
