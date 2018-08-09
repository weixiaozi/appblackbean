package bbs.com.xinfeng.bbswork.domin;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by dell on 2018/3/30.
 */
@Entity
public class TopListInnerBean {

    @Id
    private Long id;
    private int unreadNum = 0;//自定义，未读消息
    /**
     * topic_id : 6
     * last_at : 2018-04-17 15:04:17
     * topic_name : 西游记
     * topic_img_thumb : http://provider.test.6rooms.net/upload/user/20180413/20005/200_200/20180413051526_yi4uYZwPc2dDCEeXzhqF.png
     * last_content : 佛教路途用英语知我者谓我心忧知我者谓我心
     */

    private int topic_id;
    private String last_at;
    private String topic_name;
    private String topic_img;
    private String last_content;
    private int fans_number;
    private int comments;
    private int isauthor;
    private int uid_android;

    public int getUid_android() {
        return uid_android;
    }

    public void setUid_android(int uid_android) {
        this.uid_android = uid_android;
    }

    public int getIsauthor() {
        return isauthor;
    }

    public void setIsauthor(int isauthor) {
        this.isauthor = isauthor;
    }

    public String getTopic_img() {
        return topic_img;
    }

    public void setTopic_img(String topic_img) {
        this.topic_img = topic_img;
    }

    public int getFans_number() {
        return fans_number;
    }

    public void setFans_number(int fans_number) {
        this.fans_number = fans_number;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getUnreadNum() {
        return unreadNum;
    }

    public void setUnreadNum(int unreadNum) {
        this.unreadNum = unreadNum;
    }


    public int getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(int topic_id) {
        this.topic_id = topic_id;
    }

    public String getLast_at() {
        return last_at;
    }

    public void setLast_at(String last_at) {
        this.last_at = last_at;
    }

    public String getTopic_name() {
        return topic_name;
    }

    public void setTopic_name(String topic_name) {
        this.topic_name = topic_name;
    }

    public String getLast_content() {
        return last_content;
    }

    public void setLast_content(String last_content) {
        this.last_content = last_content;
    }
}
