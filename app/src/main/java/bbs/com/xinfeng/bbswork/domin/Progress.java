package bbs.com.xinfeng.bbswork.domin;

/**
 * Created by 苏杭 on 2017/2/27 17:41.
 */

public class Progress {
    public int progress;
    public boolean isError;
    public int where;

    public Progress(int progress, int where) {
        this.progress = progress;
        this.where = where;
    }
}
