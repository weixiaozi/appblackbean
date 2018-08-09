package bbs.com.xinfeng.bbswork.domin;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.annotation.Transient;

/**
 * Created by dell on 2018/4/16.
 */

public class ThemeDetailReplyBean extends ErrorBean {
    public static final String URL = "api/post/gets";


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 242
         * content : 擦擦一次此言差矣
         * content_type : 10
         * author_userid : 20003
         * status : 1
         * thread_id : 2002
         * topic_id : 3
         * type : 2
         * reply_pid : 0
         * comments : 0
         * last_reply_postid :
         * created_at : 2018-05-23 20:02:48
         * updated_at : 2018-05-23 20:02:48
         * user : {"id":20003,"name":"中本聪","portrait_thumb":"http://provider.test.6rooms.net/upload/user/20180413/10066/100_100/20180413052004_iSaIANTieTv0mAD88SgM.png","isme":1}
         * attch : []
         * last_reply_post : []
         * likenum : 0
         * islike : 0
         */
        private boolean isSelf;
        private int selfStatus;//1:loading2:error;3finish
        private long terminal_id;
        private String replyId;
        private int level;

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public String getReplyId() {
            return replyId;
        }

        public void setReplyId(String replyId) {
            this.replyId = replyId;
        }

        public boolean isSelf() {
            return isSelf;
        }

        public void setSelf(boolean self) {
            isSelf = self;
        }

        public int getSelfStatus() {
            return selfStatus;
        }

        public void setSelfStatus(int selfStatus) {
            this.selfStatus = selfStatus;
        }

        public long getTerminal_id() {
            return terminal_id;
        }

        public void setTerminal_id(long terminal_id) {
            this.terminal_id = terminal_id;
        }

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
        private String commentscn;
        private String last_reply_postid;
        private String created_at2;
        private String updated_at;
        private UserBean user;
        private String likecn = "0";
        private int islike;
        private List<AttachThemeBean> attch;
        private List<ReplyPostsBean> last_reply_post;
        private int audioread;

        private int videoType;//1：图片2：语音3:视频
        private String videoPath;
        private String coverPath;
        private int duration;
        private List<String> src = new ArrayList<>();
        private List<String> thumb = new ArrayList<>();
        private List<String> webPhotos = new ArrayList<>();
        private int isAnimator;
        private String label;
        private boolean isSelfInLandlord;//只看楼主下，是否是自己发送的，加载下一页时过滤掉

        public boolean isSelfInLandlord() {
            return isSelfInLandlord;
        }

        public void setSelfInLandlord(boolean selfInLandlord) {
            isSelfInLandlord = selfInLandlord;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public List<String> getWebPhotos() {
            return webPhotos;
        }

        public void setWebPhotos(List<String> webPhotos) {
            this.webPhotos = webPhotos;
        }

        public int isAnimator() {
            return isAnimator;
        }

        public void setAnimator(int animator) {
            isAnimator = animator;
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

        public int getAudioread() {
            return audioread;
        }

        public void setAudioread(int audioread) {
            this.audioread = audioread;
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

        public String getCommentscn() {
            return commentscn;
        }

        public void setCommentscn(String commentscn) {
            this.commentscn = commentscn;
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

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public String getLikecn() {
            return likecn;
        }

        public void setLikecn(String likecn) {
            this.likecn = likecn;
        }

        public int getIslike() {
            return islike;
        }

        public void setIslike(int islike) {
            this.islike = islike;
        }

        public List<AttachThemeBean> getAttch() {
            return attch;
        }

        public void setAttch(List<AttachThemeBean> attch) {
            this.attch = attch;
        }

        public List<ReplyPostsBean> getLast_reply_post() {
            return last_reply_post;
        }

        public void setLast_reply_post(List<ReplyPostsBean> last_reply_post) {
            this.last_reply_post = last_reply_post;
        }

        public static class UserBean {
            /**
             * id : 20003
             * name : 中本聪
             * portrait_thumb : http://provider.test.6rooms.net/upload/user/20180413/10066/100_100/20180413052004_iSaIANTieTv0mAD88SgM.png
             * isme : 1
             */

            private int id;
            private String name;
            private String portrait_thumb;
            private int isme;

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

            public int getIsme() {
                return isme;
            }

            public void setIsme(int isme) {
                this.isme = isme;
            }
        }
    }
}
