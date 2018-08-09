package bbs.com.xinfeng.bbswork.domin;

/**
 * Created by dell on 2018/5/23.
 */

public class ReplyPostsBean {

    /**
     * id : 345
     * content : 徐下次V型从V型从v
     * content_type : 10
     * author_userid : 20009
     * status : 1
     * thread_id : 1994
     * topic_id : 3
     * type : 2
     * reply_pid : 0
     * last_reply_postid :
     * created_at : 2018-05-23 15:46:08
     * updated_at : 2018-05-23 15:46:08
     * author_user_name : 奥拓_拉夫司机
     * author_user_portrait_thumb : http://provider.proxy.6rooms.net/upload/bbs_portrait/20180523/20009/100_100/20180523113754_7XAL7EzKyBjWgzQSfiLh.jpg
     */

    private int id;
    private String content;
    private int content_type;
    private int author_userid;
    private int status;
    private int thread_id;
    private int topic_id;
    private int type;
    private int reply_pid;
    private String last_reply_postid;
    private String created_at;
    private String updated_at;
    private String author_username="";
    private String author_user_portrait_thumb;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getContent_type() {
        return content_type;
    }

    public void setContent_type(int content_type) {
        this.content_type = content_type;
    }

    public int getAuthor_userid() {
        return author_userid;
    }

    public void setAuthor_userid(int author_userid) {
        this.author_userid = author_userid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getThread_id() {
        return thread_id;
    }

    public void setThread_id(int thread_id) {
        this.thread_id = thread_id;
    }

    public int getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(int topic_id) {
        this.topic_id = topic_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getReply_pid() {
        return reply_pid;
    }

    public void setReply_pid(int reply_pid) {
        this.reply_pid = reply_pid;
    }

    public String getLast_reply_postid() {
        return last_reply_postid;
    }

    public void setLast_reply_postid(String last_reply_postid) {
        this.last_reply_postid = last_reply_postid;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getAuthor_user_name() {
        return author_username;
    }

    public void setAuthor_user_name(String author_user_name) {
        this.author_username = author_user_name;
    }

    public String getAuthor_user_portrait_thumb() {
        return author_user_portrait_thumb;
    }

    public void setAuthor_user_portrait_thumb(String author_user_portrait_thumb) {
        this.author_user_portrait_thumb = author_user_portrait_thumb;
    }
}
