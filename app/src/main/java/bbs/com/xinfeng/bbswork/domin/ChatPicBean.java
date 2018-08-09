package bbs.com.xinfeng.bbswork.domin;

/**
 * Created by dell on 2018/4/19.
 */

public class ChatPicBean {
    private String localPath;
    private String backupLocalPath;
    private String webUrl;

    public ChatPicBean(String localPath) {
        this.localPath = localPath;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getBackupLocalPath() {
        return backupLocalPath;
    }

    public void setBackupLocalPath(String backupLocalPath) {
        this.backupLocalPath = backupLocalPath;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }
}
