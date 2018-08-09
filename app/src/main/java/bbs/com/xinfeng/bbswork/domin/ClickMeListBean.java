package bbs.com.xinfeng.bbswork.domin;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dell on 2018/3/20.
 */
//@我的
public class ClickMeListBean extends ErrorBean {
    public static final String URL = "api/post/atme";


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 47
         * content : #s我最美101,20002#e#s双面、人,20001#e#s中本聪,20003#e#s3664,20023#e#s96猫,20004#e#s小树叶🍂,20006#e#s孙悟空,20005#e#s奥拉夫脱司机,20017#e#s离殇,20045#e#s先看看,20013#e
         * author_userid : 20004
         * status : 1
         * thread_id : 392
         * topic_id : 3
         * type : 1
         * reply_pid : 0
         * created_at : 2018-04-28 10:28:39
         * updated_at : 2018-04-28 10:28:39
         * atUsers : {"id":20004,"name":"96猫","portrait_thumb":"http://provider.test.6rooms.net/upload/user/20180417/20004/100_100/20180417091227_06GmeBfFQTMQjJIboQsx.png"}
         * topic : {"name":"人文环境讨论组","id":3,"img_url":"http://provider.test.6rooms.net/upload/user/20180413/20002/src/20180413040256_H85UAjWbp5X1MyII6jto.png","img_url_thumb":"http://provider.test.6rooms.net/upload/user/20180413/20002/200_200/20180413040256_H85UAjWbp5X1MyII6jto.png","isjoin":0}
         * thread : {"status":1}
         * pageId : 100
         */

        private int id;
        private String content;
        private int author_userid;
        private int status;
        private int thread_id;
        private int topic_id;
        @SerializedName("type")
        private int typeX;
        private int reply_pid;
        private String created_at2;
        private String updated_at;
        private AtUsersBean atUsers;
        private TopicBean topic;
        private ThreadBean thread;
        private int pageId;
        private int reply_status;

        public int getReply_status() {
            return reply_status;
        }

        public void setReply_status(int reply_status) {
            this.reply_status = reply_status;
        }

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

        public int getTypeX() {
            return typeX;
        }

        public void setTypeX(int typeX) {
            this.typeX = typeX;
        }

        public int getReply_pid() {
            return reply_pid;
        }

        public void setReply_pid(int reply_pid) {
            this.reply_pid = reply_pid;
        }

        public String getCreated_at2() {
            return created_at2;
        }

        public void setCreated_at2(String created_at2) {
            this.created_at2 = created_at2;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public AtUsersBean getAtUsers() {
            return atUsers;
        }

        public void setAtUsers(AtUsersBean atUsers) {
            this.atUsers = atUsers;
        }

        public TopicBean getTopic() {
            return topic;
        }

        public void setTopic(TopicBean topic) {
            this.topic = topic;
        }

        public ThreadBean getThread() {
            return thread;
        }

        public void setThread(ThreadBean thread) {
            this.thread = thread;
        }

        public int getPageId() {
            return pageId;
        }

        public void setPageId(int pageId) {
            this.pageId = pageId;
        }

        public static class AtUsersBean {
            /**
             * id : 20004
             * name : 96猫
             * portrait_thumb : http://provider.test.6rooms.net/upload/user/20180417/20004/100_100/20180417091227_06GmeBfFQTMQjJIboQsx.png
             */

            private int id;
            private String name;
            private String portrait_thumb;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPortrait_thumb() {
                return portrait_thumb;
            }

            public void setPortrait_thumb(String portrait_thumb) {
                this.portrait_thumb = portrait_thumb;
            }
        }

        public static class TopicBean {
            /**
             * name : 人文环境讨论组
             * id : 3
             * img_url : http://provider.test.6rooms.net/upload/user/20180413/20002/src/20180413040256_H85UAjWbp5X1MyII6jto.png
             * img_url_thumb : http://provider.test.6rooms.net/upload/user/20180413/20002/200_200/20180413040256_H85UAjWbp5X1MyII6jto.png
             * isjoin : 0
             */

            private String name;
            private int id;
            private String img_url;
            private String img_url_thumb;
            private int isjoin;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getImg_url() {
                return img_url;
            }

            public void setImg_url(String img_url) {
                this.img_url = img_url;
            }

            public String getImg_url_thumb() {
                return img_url_thumb;
            }

            public void setImg_url_thumb(String img_url_thumb) {
                this.img_url_thumb = img_url_thumb;
            }

            public int getIsjoin() {
                return isjoin;
            }

            public void setIsjoin(int isjoin) {
                this.isjoin = isjoin;
            }
        }

        public static class ThreadBean {
            /**
             * status : 1
             */

            private int status;

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }
        }
    }
}
