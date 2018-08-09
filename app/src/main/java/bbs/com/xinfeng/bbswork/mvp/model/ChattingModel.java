package bbs.com.xinfeng.bbswork.mvp.model;

import android.app.Activity;
import android.text.TextUtils;
import android.util.ArrayMap;

import java.io.File;
import java.util.List;
import java.util.Map;

import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import io.reactivex.Flowable;

import static bbs.com.xinfeng.bbswork.domin.UploadBean.File_pic;
import static bbs.com.xinfeng.bbswork.domin.UploadBean.Pic_thread;

/**
 * Created by dell on 2017/10/20.
 */

public class ChattingModel extends NetModel {
    public ChattingModel(NetContract.OnDataLoadingListener dataLoadingListener, Activity activity) {
        super(dataLoadingListener, activity);
    }

    public void publishTheme(String topicId, String content, List<String> picList, String terminal_id, int tag) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("topic_id", topicId);
        if (!TextUtils.isEmpty(content))
            params.put("content", content);
        if (picList != null && picList.size() > 0) {
            StringBuffer pics = new StringBuffer();
            for (String pic : picList) {
                pics.append(pic);
                pics.append(",");
            }
            if (pics.length() > 0)
                pics.deleteCharAt(pics.length() - 1);
            params.put("file", pics.toString());
        }
        params.put("terminal_id", terminal_id);
        packageData(retrofitService.publishTheme(params), terminal_id, tag);
    }

    public void getThemeList(String topicId, String theme_id, int direct, int label, int tag) {
        cancelNormal(tag);
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("topic_id", topicId);
        if (!TextUtils.isEmpty(theme_id)) {
            params.put("thread_id", theme_id + "");
            params.put("direct", direct + "");
        }
        if (label != 0)
            params.put("label", label + "");
        packageData(retrofitService.getThemeList(params), tag);
    }

    public void uploadPic(File newFile, int uploadpicTag) {
        Map<String, String> params = new ArrayMap<>();
        params.put("type", File_pic);
        params.put("cate", Pic_thread);
        packageData(retrofitService.uploadHeadPic(packParamsToRequestBody(params, newFile, uploadpicTag)), uploadpicTag);
    }

    public void uploadPicOne(File newFile, String localpath, int uploadpicTag) {
        Map<String, String> params = new ArrayMap<>();
        params.put("type", File_pic);
        params.put("cate", Pic_thread);
        packageDataUpload(retrofitService.uploadHeadPic(packParamsToRequestBody(params, newFile, uploadpicTag)), localpath, uploadpicTag);
    }

    public void uploadPicOneReal(File newFile, String localpath, long terminal_id, int uploadpicTag) {
        Map<String, String> params = new ArrayMap<>();
        params.put("type", File_pic);
        params.put("cate", Pic_thread);
        packageDataUpload(retrofitService.uploadHeadPic(packParamsToRequestBody(params, newFile, uploadpicTag)), terminal_id + "", localpath, uploadpicTag);
    }

    public void likeOperate(int themeid, int hasLike, int tag) {
        Map<String, String> params = new ArrayMap<>();
        params.put("thread_id", themeid + "");
        params.put("act", (hasLike == 1 ? 2 : 1) + "");
        packageData(retrofitService.likeAction(params), tag);
    }

    public void getTopicMembers(String topicId, int type, int lastThreadId, int tag) {
        Map<String, String> params = new ArrayMap<>();
        params.put("id", topicId + "");
        params.put("type", type + "");
        params.put("last_thread_id", lastThreadId + "");
        packageData(retrofitService.chatTopicMember(params), tag);
    }

    public void operateCollection(int act, int themeid, int tag) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("act", act + "");
        params.put("thread_id", themeid + "");
        packageData(retrofitService.collectOperate(params), tag);

    }


    public void readTheme(int id, int tag) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("thread_id", id + "");
        packageData(retrofitService.haveRead(params), tag);

    }

    public void readThemeAudio(int id, int tag) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("thread_id", id + "");
        packageData(retrofitService.haveReadAudio(params), tag);

    }
}
