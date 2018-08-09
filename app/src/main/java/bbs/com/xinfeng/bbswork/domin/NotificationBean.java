package bbs.com.xinfeng.bbswork.domin;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dell on 2018/3/20.
 */
//通知
public class NotificationBean extends ErrorBean {
    public static final String URL = "api/notice/action";

    public static final int OPERATE_101 = 101;
    public static final int OPERATE_102 = 102;
    public static final int OPERATE_103 = 103;
    public static final int OPERATE_104 = 104;
    public static final int OPERATE_105 = 105;
    public static final int OPERATE_111 = 111;
    public static final int OPERATE_112 = 112;
    public static final int OPERATE_113 = 113;
    public static final int OPERATE_201 = 201;
    public static final int OPERATE_202 = 202;
    public static final int OPERATE_203 = 203;
    public static final int OPERATE_204 = 204;
    public static final int OPERATE_211 = 211;
    public static final int OPERATE_212 = 212;
    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public class DataBean implements Serializable {
        private static final long serialVersionUID = -6919461367497580385L;
        /**
         * log_id : 890
         * type : 101
         * created_at : 2018-05-17 10:02:41
         * user_id : 20004
         * user_portrait : http://provider.test.6rooms.net/upload/user/20180417/20004/src/20180417091227_06GmeBfFQTMQjJIboQsx.png
         * user_name : 96猫
         * topic_id : 87
         * topic_name : test111
         * msg_join : 滚滚滚滚滚滚
         * topic_introduce : 9988776655
         * topic_thread : 13
         * topic_fans : 3
         * topic_thumb : http://provider.proxy.6rooms.net/upload/bbs_topic/20180512/20128/200_200/20180512143633_ttxeVdgnDJXVFK5CsXvy.png
         */

        private int log_id;
        private int notice_id;
        @SerializedName("type")
        private int typeX;
        private String created_at2;
        private int user_id;
        private String user_portrait;
        private String user_name;
        private int topic_id;
        private String topic_name;
        private String msg_join;
        private String msg_deny;
        private String topic_introduce;
        private int topic_thread;
        private int topic_fans;
        private String topic_thumb;
        private int topic_status;

        public int getNotice_id() {
            return notice_id;
        }

        public void setNotice_id(int notice_id) {
            this.notice_id = notice_id;
        }

        public int getTopic_status() {
            return topic_status;
        }

        public void setTopic_status(int topic_status) {
            this.topic_status = topic_status;
        }

        public String getMsg_deny() {
            return msg_deny;
        }

        public void setMsg_deny(String msg_deny) {
            this.msg_deny = msg_deny;
        }

        public int getLog_id() {
            return log_id;
        }

        public void setLog_id(int log_id) {
            this.log_id = log_id;
        }

        public int getTypeX() {
            return typeX;
        }

        public void setTypeX(int typeX) {
            this.typeX = typeX;
        }

        public String getCreated_at2() {
            return created_at2;
        }

        public void setCreated_at2(String created_at2) {
            this.created_at2 = created_at2;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getUser_portrait() {
            return user_portrait;
        }

        public void setUser_portrait(String user_portrait) {
            this.user_portrait = user_portrait;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public int getTopic_id() {
            return topic_id;
        }

        public void setTopic_id(int topic_id) {
            this.topic_id = topic_id;
        }

        public String getTopic_name() {
            return topic_name;
        }

        public void setTopic_name(String topic_name) {
            this.topic_name = topic_name;
        }

        public String getMsg_join() {
            return msg_join;
        }

        public void setMsg_join(String msg_join) {
            this.msg_join = msg_join;
        }

        public String getTopic_introduce() {
            return topic_introduce;
        }

        public void setTopic_introduce(String topic_introduce) {
            this.topic_introduce = topic_introduce;
        }

        public int getTopic_thread() {
            return topic_thread;
        }

        public void setTopic_thread(int topic_thread) {
            this.topic_thread = topic_thread;
        }

        public int getTopic_fans() {
            return topic_fans;
        }

        public void setTopic_fans(int topic_fans) {
            this.topic_fans = topic_fans;
        }

        public String getTopic_thumb() {
            return topic_thumb;
        }

        public void setTopic_thumb(String topic_thumb) {
            this.topic_thumb = topic_thumb;
        }
    }

}
