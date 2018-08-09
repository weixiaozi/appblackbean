package bbs.com.xinfeng.bbswork.mvp.model;

import android.content.Context;
import android.util.ArrayMap;

import java.io.File;
import java.util.Map;

import bbs.com.xinfeng.bbswork.base.App;
import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.utils.ArmsUtils;
import bbs.com.xinfeng.bbswork.utils.SystemUtil;

import static bbs.com.xinfeng.bbswork.domin.UploadBean.File_pic;
import static bbs.com.xinfeng.bbswork.domin.UploadBean.Pic_thread;

/**
 * Created by dell on 2017/11/29.
 */

public class SettingModel extends NetModel {
    public SettingModel(NetContract.OnDataLoadingListener dataLoadingListener, Context activity) {
        super(dataLoadingListener, activity);
    }

    public void getPushSetting(int tag) {
        Map<String, String> params = new ArrayMap<>();
        params.put("pushid", ArmsUtils.encodeToMD5(SystemUtil.getMAC(App.getInstance())));
        packageData(retrofitService.getPushSetting(params), tag);
    }

    public void setPushSetting(int type, int tag) {
        Map<String, String> params = new ArrayMap<>();
        params.put("type", type + "");
        params.put("pushid", ArmsUtils.encodeToMD5(SystemUtil.getMAC(App.getInstance())));
        packageData(retrofitService.setPushSetting(params), tag);
    }

    public void pushFeedback(int type, String content, int tag) {
        Map<String, String> params = new ArrayMap<>();
        params.put("type", "1");
        params.put("subtype", type + "");
        params.put("content", content);
        packageData(retrofitService.pushFeedback(params), tag);
    }

    public void outLogin(int tag) {
        Map<String, String> params = new ArrayMap<>();
        params.put("pushid", ArmsUtils.encodeToMD5(SystemUtil.getMAC(App.getInstance())));
        packageData(retrofitService.outLogin(params), tag);
    }

    public void upgradeCheck(int tag) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("channel", SystemUtil.getChannelCode(mContext));
        params.put("version", SystemUtil.getAppVersion() + "");

        packageData(retrofitService.getUpGradeInfo(params), tag);
    }
}
