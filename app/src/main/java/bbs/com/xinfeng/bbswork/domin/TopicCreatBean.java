package bbs.com.xinfeng.bbswork.domin;

/**
 * Created by dell on 2018/3/20.
 */
//创建话题
public class TopicCreatBean extends ErrorBean {
    public static final String URL = "api/topic/create";


    /**
     * topic_id : 145
     */

    private int topic_id;

    public int getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(int topic_id) {
        this.topic_id = topic_id;
    }
}
