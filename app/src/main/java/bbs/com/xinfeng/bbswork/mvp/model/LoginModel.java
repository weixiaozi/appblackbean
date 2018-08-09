package bbs.com.xinfeng.bbswork.mvp.model;

import android.app.Activity;
import android.text.TextUtils;
import android.util.ArrayMap;

import java.util.Map;

import bbs.com.xinfeng.bbswork.base.App;
import bbs.com.xinfeng.bbswork.domin.BaseBean;
import bbs.com.xinfeng.bbswork.domin.TestBean;
import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.utils.ArmsUtils;
import bbs.com.xinfeng.bbswork.utils.SystemUtil;
import io.reactivex.Flowable;

import static bbs.com.xinfeng.bbswork.base.Constant.client_id;
import static bbs.com.xinfeng.bbswork.base.Constant.client_secret;

/**
 * Created by dell on 2017/10/20.
 */

public class LoginModel extends NetModel {
    public LoginModel(NetContract.OnDataLoadingListener dataLoadingListener, Activity activity) {
        super(dataLoadingListener, activity);
    }

    public void getCode(String tel, int tag) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("phone", tel);
        params.put("act", "reg");
        packageData(retrofitService.getCode(params), tag);
    }

    public void register(String tel, String code, int tag) {
        Map<String, String> params = new ArrayMap<>();
        params.put("username", tel);
        params.put("password", code);
        packageData(retrofitService.registerUser(params), tag);
    }
}
