package bbs.com.xinfeng.bbswork.domin;

/**
 * Created by dell on 2017/11/22.
 */

public class DownLoadBean extends ErrorBean {
    private String path;

    public DownLoadBean(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
