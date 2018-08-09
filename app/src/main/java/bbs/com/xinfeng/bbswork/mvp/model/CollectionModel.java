package bbs.com.xinfeng.bbswork.mvp.model;

import android.app.Activity;
import android.text.TextUtils;
import android.util.ArrayMap;

import java.util.List;

import bbs.com.xinfeng.bbswork.domin.BaseBean;
import bbs.com.xinfeng.bbswork.domin.TestBean;
import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import io.reactivex.Flowable;

/**
 * Created by dell on 2017/10/20.
 */

public class CollectionModel extends NetModel {
    public CollectionModel(NetContract.OnDataLoadingListener dataLoadingListener, Activity activity) {
        super(dataLoadingListener, activity);
    }

    public void getCollectionList(int page, int type, String current, int tag) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("page", page + "");
        params.put("type", type + "");
        if (!TextUtils.isEmpty(current))
            params.put("current", current);
        packageData(retrofitService.getCollectionList(params), tag);
    }

    public void cancelCollection(List<String> deleteList, int tag) {
        if (deleteList != null && deleteList.size() > 0) {
            ArrayMap<String, String> params = new ArrayMap<>();
            StringBuffer colletions = new StringBuffer();
            for (String c : deleteList) {
                colletions.append(c);
                colletions.append(",");
            }
            if (colletions.length() > 0)
                colletions.deleteCharAt(colletions.length() - 1);
            params.put("act", "-1");
            params.put("thread_id", colletions.toString());
            packageData(retrofitService.collectOperate(params), tag);
        }

    }

    public void updataThemeInfo(String ids, int tag) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("thread_id", ids + "");
        packageData(retrofitService.getCollectionList(params), tag);
    }

    public void readThemeAudio(int threadId, int tag) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("thread_id", threadId + "");
        packageData(retrofitService.haveReadAudio(params), tag);

    }

    public void readTheme(int id, int tag) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("thread_id", id + "");
        packageData(retrofitService.haveRead(params), tag);

    }
}
