package bbs.com.xinfeng.bbswork.domin;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Transient;

/**
 * Created by dell on 2018/5/4.
 */
@Entity
public class CollectionInnerBean {
    /**
     * thread_id : 57
     * followed_at : 2018-04-17 10:38:19
     * thread_content : 今天去哪里玩呀？????????
     * thread_comments : 0
     * thread_collections : 1
     * thread_like : 0
     * thread_last_at :
     * thread_img : {"src":[],"thumb":[]}
     * author_userid : 20006
     * author_username : 小树叶🍂
     * author_userportrait : http://provider.test.6rooms.net/upload/user/20180413/20006/src/20180413040658_hHoxad6tFg6uXSDJImaM.png
     * author_userportrait_thumb : http://provider.test.6rooms.net/upload/user/20180413/20006/100_100/20180413040658_hHoxad6tFg6uXSDJImaM.png
     * topic_id : 3
     * topic_name : 人文环境讨论组
     * topic_introduce : 大家要加群，一起讨论北京的旅游景点.
     * topic_img_url : http://provider.test.6rooms.net/upload/user/20180413/20002/src/20180413040256_H85UAjWbp5X1MyII6jto.png
     * topic_img_url_thumb : http://provider.test.6rooms.net/upload/user/20180413/20002/200_200/20180413040256_H85UAjWbp5X1MyII6jto.png
     */
    @Id
    private long id;
    @Transient
    private boolean isSelect;//自定义，是否选择
    @Transient
    private int news;//自定义，是否有新消息

    private int thread_id;
    private String followed_at2;
    private String thread_content;
    private String thread_commentscn;
    private int thread_collections;
    private String thread_likecn;
    private String thread_last_at;
    private int n;
    @Transient
    private ThreadImgBean thread_img;
    private String srcBox;
    private String thumbBox;
    private int author_userid;
    private String author_username;
    private String author_userportrait;
    private String author_userportrait_thumb;
    private int topic_id;
    private String topic_name;
    private String topic_introduce;
    private String topic_img_url;
    private String topic_img_url_thumb;
    private int isjoin;
    private int thread_status;
    private int isread;

    @Transient
    private List<AttachThemeBean> attch;
    @Transient
    private List<String> src = new ArrayList<>();
    @Transient
    private List<String> thumb = new ArrayList<>();
    @Transient
    private int isAnimator;
    private int audioread;
    private int videoType;//1：图片2：语音3:视频
    private String videoPath;
    private String coverPath;
    private int duration;
    private int uid_android;

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int getUid_android() {
        return uid_android;
    }

    public void setUid_android(int uid_android) {
        this.uid_android = uid_android;
    }

    public int getIsread() {
        return isread;
    }

    public void setIsread(int isread) {
        this.isread = isread;
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

    public List<AttachThemeBean> getAttch() {
        return attch;
    }

    public void setAttch(List<AttachThemeBean> attch) {
        this.attch = attch;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSrcBox() {
        return srcBox;
    }

    public void setSrcBox(String srcBox) {
        this.srcBox = srcBox;
    }

    public String getThumbBox() {
        return thumbBox;
    }

    public void setThumbBox(String thumbBox) {
        this.thumbBox = thumbBox;
    }

    public int getThread_status() {
        return thread_status;
    }

    public void setThread_status(int thread_status) {
        this.thread_status = thread_status;
    }

    public int getIsjoin() {
        return isjoin;
    }

    public void setIsjoin(int isjoin) {
        this.isjoin = isjoin;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getNews() {
        return news;
    }

    public void setNews(int news) {
        this.news = news;
    }

    public int getThread_id() {
        return thread_id;
    }

    public void setThread_id(int thread_id) {
        this.thread_id = thread_id;
    }

    public String getFollowed_at2() {
        return followed_at2;
    }

    public void setFollowed_at2(String followed_at2) {
        this.followed_at2 = followed_at2;
    }

    public String getThread_content() {
        return thread_content;
    }

    public void setThread_content(String thread_content) {
        this.thread_content = thread_content;
    }

    public String getThread_commentscn() {
        return thread_commentscn;
    }

    public void setThread_commentscn(String thread_commentscn) {
        this.thread_commentscn = thread_commentscn;
    }

    public String getThread_likecn() {
        return thread_likecn;
    }

    public void setThread_likecn(String thread_likecn) {
        this.thread_likecn = thread_likecn;
    }

    public int getThread_collections() {
        return thread_collections;
    }

    public void setThread_collections(int thread_collections) {
        this.thread_collections = thread_collections;
    }


    public String getThread_last_at() {
        return thread_last_at;
    }

    public void setThread_last_at(String thread_last_at) {
        this.thread_last_at = thread_last_at;
    }

    public ThreadImgBean getThread_img() {
        return thread_img;
    }

    public void setThread_img(ThreadImgBean thread_img) {
        this.thread_img = thread_img;
    }

    public int getAuthor_userid() {
        return author_userid;
    }

    public void setAuthor_userid(int author_userid) {
        this.author_userid = author_userid;
    }

    public String getAuthor_username() {
        return author_username;
    }

    public void setAuthor_username(String author_username) {
        this.author_username = author_username;
    }

    public String getAuthor_userportrait() {
        return author_userportrait;
    }

    public void setAuthor_userportrait(String author_userportrait) {
        this.author_userportrait = author_userportrait;
    }

    public String getAuthor_userportrait_thumb() {
        return author_userportrait_thumb;
    }

    public void setAuthor_userportrait_thumb(String author_userportrait_thumb) {
        this.author_userportrait_thumb = author_userportrait_thumb;
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

    public String getTopic_introduce() {
        return topic_introduce;
    }

    public void setTopic_introduce(String topic_introduce) {
        this.topic_introduce = topic_introduce;
    }

    public String getTopic_img_url() {
        return topic_img_url;
    }

    public void setTopic_img_url(String topic_img_url) {
        this.topic_img_url = topic_img_url;
    }

    public String getTopic_img_url_thumb() {
        return topic_img_url_thumb;
    }

    public void setTopic_img_url_thumb(String topic_img_url_thumb) {
        this.topic_img_url_thumb = topic_img_url_thumb;
    }

    public static class ThreadImgBean {
        private List<String> src;
        private List<String> thumb;

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
    }
}
