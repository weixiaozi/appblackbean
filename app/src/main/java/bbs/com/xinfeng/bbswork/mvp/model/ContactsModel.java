package bbs.com.xinfeng.bbswork.mvp.model;

import android.app.Activity;
import android.text.TextUtils;
import android.util.ArrayMap;

import java.io.File;
import java.util.Map;

import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;

import static bbs.com.xinfeng.bbswork.domin.UploadBean.File_pic;
import static bbs.com.xinfeng.bbswork.domin.UploadBean.Pic_topic;

/**
 * Created by dell on 2017/10/20.
 */

public class ContactsModel extends NetModel {
    public ContactsModel(NetContract.OnDataLoadingListener dataLoadingListener, Activity activity) {
        super(dataLoadingListener, activity);
    }


    public void creatTopic(String title, String introduce, String image, int tag) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("name", title);
        params.put("introduce", introduce);
        params.put("img_url", image);
        packageData(retrofitService.creatTopic(params), tag);
    }

    public void changeTopic(String id, String title, String introduce, String image, int tag) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("id", id);
        params.put("name", title);
        params.put("introduce", introduce);
        params.put("img_url", image);
        packageData(retrofitService.changeTopic(params), tag);
    }

    public void getTopicUsers(String id, int tag) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("id", id);
        packageData(retrofitService.getTopicUsers(params), tag);
    }

    public void joinTopic(String id, String channel, int tag) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("id", id);
        params.put("seo_channel", channel);
        packageData(retrofitService.joinTopicUsers(params), tag);
    }

    public void outTopic(String id, int tag) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("id", id);
        packageData(retrofitService.outTopicUsers(params), tag);
    }

    public void register(String user, String password, int tag) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("username", user);
        params.put("password", password);
        packageData(retrofitService.registerUser(params), tag);
    }

    public void uploadPic(File newFile, int uploadpicTag) {
        Map<String, String> params = new ArrayMap<>();
        params.put("type", File_pic);
        params.put("cate", Pic_topic);
        packageData(retrofitService.uploadHeadPic(packParamsToRequestBody(params, newFile, uploadpicTag)), uploadpicTag);
    }

    public void getContactList(String name, int page, int tag) {
        ArrayMap<String, String> params = new ArrayMap<>();
        if (!TextUtils.isEmpty(name))
            params.put("name", name);
        params.put("page", page + "");
        packageData(retrofitService.getContactlist(params), tag);
    }
}
