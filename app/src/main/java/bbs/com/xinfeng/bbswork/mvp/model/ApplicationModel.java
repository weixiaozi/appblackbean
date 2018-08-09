package bbs.com.xinfeng.bbswork.mvp.model;

import android.content.Context;
import android.os.Build;
import android.util.ArrayMap;

import java.util.Map;

import bbs.com.xinfeng.bbswork.base.App;
import bbs.com.xinfeng.bbswork.base.Constant;
import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.utils.ArmsUtils;
import bbs.com.xinfeng.bbswork.utils.LogUtil;
import bbs.com.xinfeng.bbswork.utils.SharedPrefUtil;
import bbs.com.xinfeng.bbswork.utils.SystemUtil;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

import static bbs.com.xinfeng.bbswork.base.Constant.client_id;
import static bbs.com.xinfeng.bbswork.base.Constant.client_secret;

/**
 * Created by dell on 2017/10/24.
 */

public class ApplicationModel extends NetModel {
    Map<String, String> paramsToReport = new ArrayMap<>();

    public ApplicationModel(NetContract.OnDataLoadingListener dataLoadingListener, Context activity) {
        super(dataLoadingListener, activity);
        paramsToReport.put("macid", SystemUtil.getMAC(App.getInstance()));
        paramsToReport.put("os", SystemUtil.getSystemVersion());
        paramsToReport.put("phone", Build.MODEL);
        paramsToReport.put("app", SystemUtil.getAppVersion() + "");
        paramsToReport.put("terminal", "4");
    }

    public void setPushid(int tag) {
        Map<String, String> params = new ArrayMap<>();
        params.put("pushid", ArmsUtils.encodeToMD5(SystemUtil.getMAC(App.getInstance())));
        params.put("macid", SystemUtil.getMAC(App.getInstance()));
        params.put("os", SystemUtil.getSystemVersion());
        params.put("phone", Build.MODEL);
        params.put("app", SystemUtil.getAppVersion() + "");
        params.put("terminal", "4");
        packageData(retrofitService.setPushid(params), tag);
    }

    public void getToken(String username, String password, int tag) {
        Map<String, String> params = new ArrayMap<>();
        params.put("grant_type", "password");
        params.put("client_id", client_id);
        params.put("client_secret", client_secret);
        params.put("username", username);
        params.put("password", password);
        params.put("pushid", ArmsUtils.encodeToMD5(SystemUtil.getMAC(App.mApp)));

        packageData(retrofitService.getToken(params), tag);
    }

    public void refreshToken(int tag) {
        Map<String, String> params = new ArrayMap<>();
        params.put("grant_type", "refresh_token");
        params.put("client_id", client_id);
        params.put("client_secret", client_secret);
        params.put("refresh_token", SharedPrefUtil.getString(Constant.refreshtoken_key, ""));
        packageData(retrofitService.getToken(params), tag);
    }

    public void getSocketAddress(int tag) {
        Map<String, String> params = new ArrayMap<>();
        packageData(retrofitService.getSocketAddress(params), tag);
    }

    public void notifyMessage(long current, int tag) {
        cancelNormal(tag);
        Map<String, String> params = new ArrayMap<>();
        if (current != 0)
            params.put("current", current + "");
        packageData(retrofitService.getNoticeChange(params), tag);
    }

    public void getUserInfo(int tag) {
        Map<String, String> params = new ArrayMap<>();
        packageData(retrofitService.getUserinfo(params), tag);
    }

    public void reportToWeb(int type, int tag) {
        paramsToReport.put("type", type + "");

        packageData(retrofitService.reprotToWeb(paramsToReport), tag);
    }

    public void getVideoSecret(int tag) {
        Map<String, String> params = new ArrayMap<>();
        packageData(retrofitService.getVideoSecret(params), tag);
    }
}
