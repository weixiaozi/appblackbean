package bbs.com.xinfeng.bbswork.domin;


/**
 * Created by dell on 2018/4/16.
 */

public class PublishReplyBean extends ErrorBean {
    public static final String URL = "api/post/reply";
    public static final String URL1 = "api/post/del";


    /**
     * thread_id : 4273
     * post_id : 623
     */

    private int thread_id;
    private int post_id;

    public int getThread_id() {
        return thread_id;
    }

    public void setThread_id(int thread_id) {
        this.thread_id = thread_id;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }
}
