package bbs.com.xinfeng.bbswork.domin;

/**
 * Created by dell on 2018/5/30.
 */

public class MesReduceBus {
    public int type;
    public int themeId;
    public MesReduceBus(int type) {
        this.type = type;
    }

    public MesReduceBus(int type, int themeId) {
        this.type = type;
        this.themeId = themeId;
    }
}
