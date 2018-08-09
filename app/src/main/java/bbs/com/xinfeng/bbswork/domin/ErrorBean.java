package bbs.com.xinfeng.bbswork.domin;

/**
 * Created by dell on 2017/10/18.
 */

public class ErrorBean {
    public static String TYPE_DEAL = "1";
    public static String TYPE_SHOW = "2";

    public ErrorBean(String code, String desc, String type) {
        this.androidcode = code;
        this.desc = desc;
        this.androidType = type;
    }

    public ErrorBean(String androidcode, String desc, String type, String key) {
        this.androidcode = androidcode;
        this.desc = desc;
        this.androidType = type;
        this.key = key;
    }

    public ErrorBean(String androidcode, String desc, String type, String key, String localPath) {
        this.androidcode = androidcode;
        this.desc = desc;
        this.androidType = type;
        this.key = key;
        this.localPath = localPath;
    }

    public ErrorBean() {

    }

    public String androidcode;
    public String desc;
    public String androidType = TYPE_DEAL;
    public String key;
    public String localPath;

    @Override
    public String toString() {
        return "ErrorBean{" +
                "code='" + androidcode + '\'' +
                ", desc='" + desc + '\'' +
                ", type='" + androidType + '\'' +
                '}';
    }
}
