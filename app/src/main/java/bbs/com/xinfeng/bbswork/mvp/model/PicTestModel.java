package bbs.com.xinfeng.bbswork.mvp.model;

import android.content.Context;
import android.util.ArrayMap;

import java.io.File;
import java.util.Map;

import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;

import static bbs.com.xinfeng.bbswork.domin.UploadBean.File_pic;
import static bbs.com.xinfeng.bbswork.domin.UploadBean.Pic_thread;

/**
 * Created by dell on 2017/11/29.
 */

public class PicTestModel extends NetModel {
    public PicTestModel(NetContract.OnDataLoadingListener dataLoadingListener, Context activity) {
        super(dataLoadingListener, activity);
    }

    public void uploadPic(File newFile, int uploadpicTag) {
        Map<String, String> params = new ArrayMap<>();
        params.put("type", File_pic);
        params.put("cate", Pic_thread);
        packageData(retrofitService.uploadHeadPic(packParamsToRequestBody(params, newFile, uploadpicTag)), uploadpicTag);
    }

}
