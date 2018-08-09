package bbs.com.xinfeng.bbswork.mvp.model;

import android.app.Activity;
import android.text.TextUtils;
import android.util.ArrayMap;

import java.io.File;
import java.util.Map;

import bbs.com.xinfeng.bbswork.base.App;
import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.utils.ArmsUtils;
import bbs.com.xinfeng.bbswork.utils.SystemUtil;

import static bbs.com.xinfeng.bbswork.domin.UploadBean.File_pic;
import static bbs.com.xinfeng.bbswork.domin.UploadBean.Pic_portrait;
import static bbs.com.xinfeng.bbswork.domin.UploadBean.Pic_thread;

/**
 * Created by dell on 2017/10/20.
 */

public class ModifyInfoModel extends NetModel {
    public ModifyInfoModel(NetContract.OnDataLoadingListener dataLoadingListener, Activity activity) {
        super(dataLoadingListener, activity);
    }

    public void getUserInfo(int tag) {
        packageData(retrofitService.getUserinfo(new ArrayMap<>()), tag);
    }

    public void modifyInfo(String name, String portrait, String introduce, int tag) {
        Map<String, String> params = new ArrayMap<>();
        params.put("username", name);
        params.put("portrait", portrait);

        if (!TextUtils.isEmpty(introduce))
            params.put("introduce", introduce);
        packageData(retrofitService.modifyUserinfo(params), tag);
    }

    public void uploadPic(File newFile, int uploadpicTag) {
        Map<String, String> params = new ArrayMap<>();
        params.put("type", File_pic);
        params.put("cate", Pic_portrait);
        packageData(retrofitService.uploadHeadPic(packParamsToRequestBody(params, newFile, uploadpicTag)), uploadpicTag);
    }

    public void outLogin(int tag) {
        Map<String, String> params = new ArrayMap<>();
        params.put("pushid", ArmsUtils.encodeToMD5(SystemUtil.getMAC(App.getInstance())));
        packageData(retrofitService.outLogin(params), tag);
    }
}
