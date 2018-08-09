package bbs.com.xinfeng.bbswork.domin;

import java.util.List;

/**
 * Created by dell on 2016/11/23 17:46.
 */
//获取自己相关话题列表
public class TopicUserListBean extends ErrorBean {
    public static final String URL = "api/topic/get";


    private List<TopListInnerBean> data;

    public List<TopListInnerBean> getData() {
        return data;
    }

    public void setData(List<TopListInnerBean> data) {
        this.data = data;
    }


}

