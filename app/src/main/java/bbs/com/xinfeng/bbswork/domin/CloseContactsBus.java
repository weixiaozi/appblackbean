package bbs.com.xinfeng.bbswork.domin;

/**
 * Created by dell on 2018/4/2.
 */
//关闭联系人列表
public class CloseContactsBus {

    public boolean isClose;

    public CloseContactsBus(boolean isClose) {
        this.isClose = isClose;
    }
}
