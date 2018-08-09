package bbs.com.xinfeng.bbswork.domin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2018/3/20.
 */
//我的观点
public class MyThemeListBean extends ErrorBean {
    public static final String URL = "api/thread/my";


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 500
         * content :
         * like : 0
         * collections : 0
         * comments : 0
         * created_at : 2018-05-03 14:49:25
         * last_at : null
         * last_reply_userid : null
         * topic_id : 31
         * replyUsers : []
         * src : ["http://provider.test.6rooms.net/upload/bbs_thread/20180503/20004/src/20180503144925_Ofseq9CRFQVkTppvgwae.png?width=750&height=750"]
         * thumb : ["http://provider.test.6rooms.net/upload/bbs_thread/20180503/20004/300_300/20180503144925_Ofseq9CRFQVkTppvgwae.png"]
         * topic : {"id":31,"name":"99999999","img_url":"http://provider.test.6rooms.net/upload/user/20180417/20004/src/20180417064143_bYlXG26M8ULBBEKX6UWW.jpg","isjoin":1,"img_url_thumb":"http://provider.test.6rooms.net/upload/user/20180417/20004/200_200/20180417064143_bYlXG26M8ULBBEKX6UWW.jpg"}
         */

        private int id;
        private String content;
        private String likecn;
        private int collections;
        private String commentscn;
        private String created_at2;
        private Object last_at;
        private Object last_reply_userid;
        private int topic_id;
        private TopicBean topic;
        private List<?> replyUsers;
        private List<AttachThemeBean> attch;
        private List<String> src = new ArrayList<>();
        private List<String> thumb = new ArrayList<>();
        private int isAnimator;
        private int audioread;
        private int videoType;//1：图片2：语音3:视频
        private String videoPath;
        private String coverPath;
        private int duration;

        public List<AttachThemeBean> getAttch() {
            return attch;
        }

        public void setAttch(List<AttachThemeBean> attch) {
            this.attch = attch;
        }

        public int isAnimator() {
            return isAnimator;
        }

        public void setAnimator(int animator) {
            isAnimator = animator;
        }

        public int getAudioread() {
            return audioread;
        }

        public void setAudioread(int audioread) {
            this.audioread = audioread;
        }

        public int getVideoType() {
            return videoType;
        }

        public void setVideoType(int videoType) {
            this.videoType = videoType;
        }

        public String getVideoPath() {
            return videoPath;
        }

        public void setVideoPath(String videoPath) {
            this.videoPath = videoPath;
        }

        public String getCoverPath() {
            return coverPath;
        }

        public void setCoverPath(String coverPath) {
            this.coverPath = coverPath;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
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


        public int getCollections() {
            return collections;
        }

        public void setCollections(int collections) {
            this.collections = collections;
        }

        public String getLikecn() {
            return likecn;
        }

        public void setLikecn(String likecn) {
            this.likecn = likecn;
        }

        public String getCommentscn() {
            return commentscn;
        }

        public void setCommentscn(String commentscn) {
            this.commentscn = commentscn;
        }

        public String getCreated_at2() {
            return created_at2;
        }

        public void setCreated_at2(String created_at2) {
            this.created_at2 = created_at2;
        }

        public Object getLast_at() {
            return last_at;
        }

        public void setLast_at(Object last_at) {
            this.last_at = last_at;
        }

        public Object getLast_reply_userid() {
            return last_reply_userid;
        }

        public void setLast_reply_userid(Object last_reply_userid) {
            this.last_reply_userid = last_reply_userid;
        }

        public int getTopic_id() {
            return topic_id;
        }

        public void setTopic_id(int topic_id) {
            this.topic_id = topic_id;
        }

        public TopicBean getTopic() {
            return topic;
        }

        public void setTopic(TopicBean topic) {
            this.topic = topic;
        }

        public List<?> getReplyUsers() {
            return replyUsers;
        }

        public void setReplyUsers(List<?> replyUsers) {
            this.replyUsers = replyUsers;
        }

        public List<String> getSrc() {
            return src;
        }

        public void setSrc(List<String> src) {
            this.src = src;
        }

        public List<String> getThumb() {
            return thumb;
        }

        public void setThumb(List<String> thumb) {
            this.thumb = thumb;
        }

        public static class TopicBean {
            /**
             * id : 31
             * name : 99999999
             * img_url : http://provider.test.6rooms.net/upload/user/20180417/20004/src/20180417064143_bYlXG26M8ULBBEKX6UWW.jpg
             * isjoin : 1
             * img_url_thumb : http://provider.test.6rooms.net/upload/user/20180417/20004/200_200/20180417064143_bYlXG26M8ULBBEKX6UWW.jpg
             */

            private int id;
            private String name;
            private String img_url;
            private int isjoin;
            private String img_url_thumb;

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

            public int getIsjoin() {
                return isjoin;
            }

            public void setIsjoin(int isjoin) {
                this.isjoin = isjoin;
            }

            public String getImg_url_thumb() {
                return img_url_thumb;
            }

            public void setImg_url_thumb(String img_url_thumb) {
                this.img_url_thumb = img_url_thumb;
            }
        }
    }
}
