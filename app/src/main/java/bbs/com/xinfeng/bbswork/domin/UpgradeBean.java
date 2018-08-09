package bbs.com.xinfeng.bbswork.domin;

/**
 * Created by dell on 2018/1/18.
 */

public class UpgradeBean extends ErrorBean {
    public static final String URL = "api/app/upgrade";


    /**
     * version : 1
     * upgrade : false
     * url : http://provider.test.6rooms.net/upload/user/20180419/14/src/20180419022323_DDHxcXW4v5HqHP7fYv9X.apk
     * channel : 8000
     */

    private String version;
    private String version_name;
    private boolean upgrade;
    private String url;
    private String channel;

    public String getVersion_name() {
        return version_name;
    }

    public void setVersion_name(String version_name) {
        this.version_name = version_name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isUpgrade() {
        return upgrade;
    }

    public void setUpgrade(boolean upgrade) {
        this.upgrade = upgrade;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
