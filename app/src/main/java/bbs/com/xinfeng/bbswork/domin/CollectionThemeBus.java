package bbs.com.xinfeng.bbswork.domin;

/**
 * Created by dell on 2018/4/2.
 */
//取消收藏
public class CollectionThemeBus {

    public int themeId;
    public boolean isCollect;

    public CollectionThemeBus(int themeId, boolean isCollect) {
        this.themeId = themeId;
        this.isCollect = isCollect;
    }
}
