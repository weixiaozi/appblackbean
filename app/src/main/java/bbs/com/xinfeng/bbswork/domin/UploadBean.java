package bbs.com.xinfeng.bbswork.domin;

/**
 * Created by dell on 2016/11/23 17:46.
 */

public class UploadBean extends ErrorBean {
    public static final String URL = "api/file/upload";

    public static final String File_pic = "pic";
    public static final String File_video = "video";
    public static final String File_file = "file";
    public static final String Pic_portrait = "portrait";
    public static final String Pic_topic = "topic";
    public static final String Pic_thread = "thread";
    public static final String Pic_chat = "chat";

    /**
     * src : http://provider.test.6rooms.net/upload/user/20180319/1/src/eeWucmYDUmr12CYlNOocheIqo2g0DKE8EeXmd5gK.png
     */

    private String src;

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}

