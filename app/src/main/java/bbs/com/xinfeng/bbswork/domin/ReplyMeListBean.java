package bbs.com.xinfeng.bbswork.domin;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dell on 2018/3/20.
 */
//回复我的
public class ReplyMeListBean extends ErrorBean {
    public static final String URL = "api/post/replyme";


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * pageId : 1300
         * my : {"content":"chjfjg","status":2}
         * reply : {"id":467,"content":"jfjg","content_type":10,"author_userid":20003,"status":1,"thread_id":2003,"topic_id":3,"type":3,"reply_pid":466,"reply_pid2":0,"comments":0,"last_reply_postid":"","created_at":"2018-05-25 11:23:32","updated_at":"2018-05-25 11:23:32"}
         * replyUser : {"id":20003,"name":"中本聪","portrait_thumb":"http://provider.test.6rooms.net/upload/user/20180413/10066/100_100/20180413052004_iSaIANTieTv0mAD88SgM.png"}
         * topic : {"name":"人文环境讨论组","id":3,"img_url":"http://provider.test.6rooms.net/upload/user/20180413/20002/src/20180413040256_H85UAjWbp5X1MyII6jto.png","img_url_thumb":"http://provider.test.6rooms.net/upload/user/20180413/20002/200_200/20180413040256_H85UAjWbp5X1MyII6jto.png","isjoin":1}
         * thread : {"status":1}
         */

        private int pageId;
        private MyBean my;
        private ReplyBean reply;
        private ReplyUserBean replyUser;
        private TopicBean topic;
        private ThreadBean thread;

        public int getPageId() {
            return pageId;
        }

        public void setPageId(int pageId) {
            this.pageId = pageId;
        }

        public MyBean getMy() {
            return my;
        }

        public void setMy(MyBean my) {
            this.my = my;
        }

        public ReplyBean getReply() {
            return reply;
        }

        public void setReply(ReplyBean reply) {
            this.reply = reply;
        }

        public ReplyUserBean getReplyUser() {
            return replyUser;
        }

        public void setReplyUser(ReplyUserBean replyUser) {
            this.replyUser = replyUser;
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

        public static class MyBean {
            /**
             * content : chjfjg
             * status : 2
             */

            private String content;
            private int status;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }
        }

        public static class ReplyBean {
            /**
             * id : 467
             * content : jfjg
             * content_type : 10
             * author_userid : 20003
             * status : 1
             * thread_id : 2003
             * topic_id : 3
             * type : 3
             * reply_pid : 466
             * reply_pid2 : 0
             * comments : 0
             * last_reply_postid :
             * created_at : 2018-05-25 11:23:32
             * updated_at : 2018-05-25 11:23:32
             */

            private int id;
            private String content;
            private int content_type;
            private int author_userid;
            private int status;
            private int thread_id;
            private int topic_id;
            @SerializedName("type")
            private int typeX;
            private int reply_pid;
            private int reply_pid2;
            private int comments;
            private String last_reply_postid;
            private String created_at2;
            private String updated_at;

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

            public int getReply_pid2() {
                return reply_pid2;
            }

            public void setReply_pid2(int reply_pid2) {
                this.reply_pid2 = reply_pid2;
            }

            public int getComments() {
                return comments;
            }

            public void setComments(int comments) {
                this.comments = comments;
            }

            public String getLast_reply_postid() {
                return last_reply_postid;
            }

            public void setLast_reply_postid(String last_reply_postid) {
                this.last_reply_postid = last_reply_postid;
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
        }

        public static class ReplyUserBean {
            /**
             * id : 20003
             * name : 中本聪
             * portrait_thumb : http://provider.test.6rooms.net/upload/user/20180413/10066/100_100/20180413052004_iSaIANTieTv0mAD88SgM.png
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
             * isjoin : 1
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
