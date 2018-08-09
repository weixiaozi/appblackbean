package bbs.com.xinfeng.bbswork.domin;

/**
 * Created by dell on 2018/4/16.
 */

public class GetPushSettingBean extends ErrorBean {
    public static final String URL = "api/push/get";


    /**
     * message : 1
     * post : 1
     */

    private int message;
    private int post;

    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }

    public int getPost() {
        return post;
    }

    public void setPost(int post) {
        this.post = post;
    }
}
