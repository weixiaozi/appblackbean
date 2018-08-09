package bbs.com.xinfeng.bbswork.mvp.model;

import android.app.Activity;
import android.text.TextUtils;
import android.util.ArrayMap;

import java.io.File;
import java.util.List;
import java.util.Map;

import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;

import static bbs.com.xinfeng.bbswork.domin.UploadBean.File_pic;
import static bbs.com.xinfeng.bbswork.domin.UploadBean.Pic_thread;

/**
 * Created by dell on 2017/10/20.
 */

public class ThemeDetailModel extends NetModel {
    public ThemeDetailModel(NetContract.OnDataLoadingListener dataLoadingListener, Activity activity) {
        super(dataLoadingListener, activity);
    }

    public void getThemeDetail(int topicid, int themeid, int tag) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("topic_id", topicid + "");
        params.put("thread_id", themeid + "");
        params.put("direct", "0");
        params.put("read", "1");
        packageData(retrofitService.getThemeList(params), tag);
    }

    public void likeOperate(int themeid, int hasLike, int postId, int tag) {
        Map<String, String> params = new ArrayMap<>();
        params.put("thread_id", themeid + "");
        params.put("act", (hasLike == 1 ? 2 : 1) + "");
        if (postId > 0)
            params.put("post_id", postId + "");
        packageData(retrofitService.likeAction(params), tag);
    }

    public void operateCollection(int act, int themeid, int tag) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("act", act + "");
        params.put("thread_id", themeid + "");
        packageData(retrofitService.collectOperate(params), tag);

    }

    public void getReplyList(int themeid, int pid, String direct, int label, String key, int tag) {
        cancelNormal(tag);
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("thread_id", themeid + "");
        if (pid != 0) {
            params.put("pid", pid + "");
            params.put("direct", direct);
        }
        if (label != 0)
            params.put("label", label + "");
        packageData(retrofitService.getThemeReplyList(params), key, tag);
    }

    public void getReplyTwoList(int themeid, int reply_pid, int pid, String key, int tag) {
        cancelNormal(tag);
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("thread_id", themeid + "");
        params.put("reply_pid", reply_pid + "");
        if (pid != 0) {
            params.put("pid", pid + "");
            params.put("direct", "1");
        }
        packageData(retrofitService.getThemeReplyList(params), key, tag);
    }

    public void getReplyDetail(int themeid, int pid, int tag) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("thread_id", themeid + "");
        params.put("pid", pid + "");
        params.put("direct", "0");
        packageData(retrofitService.getThemeReplyList(params), tag);
    }

    public void publishReply(int themeid, String pid, String content, List<String> picList, long terminal_id, int tag) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("thread_id", themeid + "");
        if (!TextUtils.isEmpty(pid)) {
            params.put("pid", pid + "");
        }
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
        packageData(retrofitService.publishReply(params), terminal_id + "", tag);
    }

    public void deleteRepley(int themeid, int replyid, int tag) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("thread_id", themeid + "");
        params.put("post_id", replyid + "");
        packageData(retrofitService.deleteReply(params), tag);
    }

    public void deleteTheme(int themeid, int tag) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("id", themeid + "");
        packageData(retrofitService.deleteTheme(params), tag);
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

    public void readThemeAudio(int id, int postid, int tag) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("thread_id", id + "");
        if (postid != 0)
            params.put("post_id", postid + "");
        packageData(retrofitService.haveReadAudio(params), tag);

    }
}
