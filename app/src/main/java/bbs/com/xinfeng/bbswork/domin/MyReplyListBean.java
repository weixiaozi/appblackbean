package bbs.com.xinfeng.bbswork.domin;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dell on 2018/3/20.
 */
//我的回复
public class MyReplyListBean extends ErrorBean {
    public static final String URL = "api/post/myreply";


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * pageId : 367
         * thread : {"content":"[图片]","status":1}
         * topic : {"isjoin":1,"id":34,"name":"海珠格格","img_url":"http://provider.test.6rooms.net/upload/user/20180418/20017/src/20180418060853_MUFLiD7TyoE3yuGXlD5p.png","img_url_thumb":"http://provider.test.6rooms.net/upload/user/20180418/20017/200_200/20180418060853_MUFLiD7TyoE3yuGXlD5p.png"}
         * my : {"id":81,"content":"回复奥拓_拉夫司机: 参加吹风机","author_userid":20004,"status":1,"thread_id":468,"topic_id":34,"type":2,"reply_pid":71,"created_at":"2018-05-03 14:37:12","updated_at":"2018-05-03 14:37:12"}
         */

        private int pageId;
        private ThreadBean thread;
        private TopicBean topic;
        private MyBean my;

        public int getPageId() {
            return pageId;
        }

        public void setPageId(int pageId) {
            this.pageId = pageId;
        }

        public ThreadBean getThread() {
            return thread;
        }

        public void setThread(ThreadBean thread) {
            this.thread = thread;
        }

        public TopicBean getTopic() {
            return topic;
        }

        public void setTopic(TopicBean topic) {
            this.topic = topic;
        }

        public MyBean getMy() {
            return my;
        }

        public void setMy(MyBean my) {
            this.my = my;
        }

        public static class ThreadBean {
            /**
             * content : [图片]
             * status : 1
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

        public static class TopicBean {
            /**
             * isjoin : 1
             * id : 34
             * name : 海珠格格
             * img_url : http://provider.test.6rooms.net/upload/user/20180418/20017/src/20180418060853_MUFLiD7TyoE3yuGXlD5p.png
             * img_url_thumb : http://provider.test.6rooms.net/upload/user/20180418/20017/200_200/20180418060853_MUFLiD7TyoE3yuGXlD5p.png
             */

            private int isjoin;
            private int id;
            private String name;
            private String img_url;
            private String img_url_thumb;

            public int getIsjoin() {
                return isjoin;
            }

            public void setIsjoin(int isjoin) {
                this.isjoin = isjoin;
            }

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
        }

        public static class MyBean {
            /**
             * id : 81
             * content : 回复奥拓_拉夫司机: 参加吹风机
             * author_userid : 20004
             * status : 1
             * thread_id : 468
             * topic_id : 34
             * type : 2
             * reply_pid : 71
             * created_at : 2018-05-03 14:37:12
             * updated_at : 2018-05-03 14:37:12
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
        }
    }
}
