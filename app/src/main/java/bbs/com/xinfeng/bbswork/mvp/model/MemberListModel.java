package bbs.com.xinfeng.bbswork.mvp.model;

import android.app.Activity;
import android.util.ArrayMap;

import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;

/**
 * Created by dell on 2017/10/20.
 */

public class MemberListModel extends NetModel {
    public MemberListModel(NetContract.OnDataLoadingListener dataLoadingListener, Activity activity) {
        super(dataLoadingListener, activity);
    }


    public void getMembersIn(int topoicId, int page, int tag) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("id", topoicId + "");
        params.put("page", page + "");
        packageData(retrofitService.getAllMemberAboutUser(params), tag);
    }

    public void getMembersOut(int topoicId, int tag) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("id", topoicId + "");
        packageData(retrofitService.getTopicMembers(params), tag);
    }

    public void inviteUser(int topoicId, String ids, String channel, int tag) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("id", topoicId + "");
        params.put("user_id", ids);
        params.put("seo_channel", channel);
        packageData(retrofitService.inviteUsers(params), tag);
    }

    public void removeUser(int topoicId, String ids, int tag) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("id", topoicId + "");
        params.put("user_id", ids);
        packageData(retrofitService.removeUsers(params), tag);
    }
}
