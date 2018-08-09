package bbs.com.xinfeng.bbswork.domin;

import java.util.List;

/**
 * Created by dell on 2018/3/20.
 */
//收藏列表
public class CollectionListBean extends ErrorBean {
    public static final String URL = "api/user/favorlist";

    private String current;
    private List<CollectionInnerBean> data;

    public List<CollectionInnerBean> getData() {
        return data;
    }

    public void setData(List<CollectionInnerBean> data) {
        this.data = data;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }
}
