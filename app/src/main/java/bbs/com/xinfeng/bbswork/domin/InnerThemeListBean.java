package bbs.com.xinfeng.bbswork.domin;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Transient;

/**
 * Created by dell on 2018/4/8.
 */
@Entity
public class InnerThemeListBean {

    @Transient
    public static final int LOADING = -1;
    @Transient
    private int type;//-1:loading;0：other
    /**
     * id : 318
     * content : 再发一些表情😣😲😜👿😌😏😖
     * author_userid : 20002
     * status : 1
     * comments : 1
     * collections : 0
     * like : 0
     * displayorder : 0
     * topic_id : 3
     * terminal_id : 0
     * posttableid : 8
     * last_at : 2018-04-26 10:31:21
     * last_reply_userid : 20005
     * created_at : 2018-04-25 19:24:12
     * updated_at : 2018-04-26 10:31:21
     * src : ["http://provider.test.6rooms.net/upload/bbs_thread/20180425/20002/src/20180425112412_sScDED1mVlOC5rFfaaqY.png"]
     * thumb : ["http://provider.test.6rooms.net/upload/bbs_thread/20180425/20002/300_300/20180425112412_sScDED1mVlOC5rFfaaqY.png"]
     * hasLike : 0
     * hasCollect : 0
     * author_username : 我最美101
     * portrait_thumb : http://provider.test.6rooms.net/upload/user/20180413/20002/100_100/20180413035414_qdsPq3N3khmh462g56KK.png
     * replyUsers : [{"id":20005,"name":"孙悟空","portrait_thumb":"http://provider.test.6rooms.net/upload/user/20180413/10066/100_100/20180413052242_3yn6rPaUvlfNRLXIWtsS.png"}]
     */

    private boolean isSelf;
    private int selfStatus;//1:loading2:error;3finish
    @Transient
    private List<String> webPhotos = new ArrayList<>();

    public List<String> getWebPhotos() {
        return webPhotos;
    }

    public void setWebPhotos(List<String> webPhotos) {
        this.webPhotos = webPhotos;
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

    @Id
    private Long themeid;

    private int id;
    private String content;
    private int author_userid;
    private int status;
    private String commentscn = "0";
    private int collections;
    private String likecn = "0";
    private int displayorder;
    private int topic_id;
    private long terminal_id;
    private int posttableid;
    private String last_at;
    private String last_reply_userid;
    private String created_at2;
    private String updated_at;
    private int hasLike;
    private int hasCollect;
    private String author_username;
    private String portrait_thumb;

    private int videoType;//1：图片2：语音3:视频4:网页视频
    private String videoPath;
    private String coverPath;
    private int duration;
    private float videoWidth;
    private float videoHeight;
    private int uid_android;
    private int ishot;
    private String label;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getIshot() {
        return ishot;
    }

    public void setIshot(int ishot) {
        this.ishot = ishot;
    }

    public int getUid_android() {
        return uid_android;
    }

    public void setUid_android(int uid_android) {
        this.uid_android = uid_android;
    }

    public float getVideoWidth() {
        return videoWidth;
    }

    public void setVideoWidth(float videoWidth) {
        this.videoWidth = videoWidth;
    }

    public float getVideoHeight() {
        return videoHeight;
    }

    public void setVideoHeight(float videoHeight) {
        this.videoHeight = videoHeight;
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

    @Transient
    private List<String> src = new ArrayList<>();
    @Transient
    private List<String> thumb = new ArrayList<>();
    @Transient
    private List<ReplyUsersBean> replyUsers;
    @Transient
    private List<ReplyPostsBean> replyPosts;
    @Transient
    private List<AttachThemeBean> attch;
    @Transient
    private int isAnimator;//0：默认1:加载中2：播放中
    private String srcBox;
    private String thumbBox;
    private int isread;
    private int audioread;
    private String readnumcn;

    public int getAudioread() {
        return audioread;
    }

    public void setAudioread(int audioread) {
        this.audioread = audioread;
    }

    public int isAnimator() {
        return isAnimator;
    }

    public void setAnimator(int animator) {
        isAnimator = animator;
    }

    public List<ReplyPostsBean> getReplyPosts() {
        return replyPosts;
    }

    public void setReplyPosts(List<ReplyPostsBean> replyPosts) {
        this.replyPosts = replyPosts;
    }

    public List<AttachThemeBean> getAttch() {
        return attch;
    }

    public void setAttch(List<AttachThemeBean> attch) {
        this.attch = attch;
    }

    public int getIsread() {
        return isread;
    }

    public void setIsread(int isread) {
        this.isread = isread;
    }

    public String getReadnumcn() {
        return readnumcn;
    }

    public void setReadnumcn(String readnumcn) {
        this.readnumcn = readnumcn;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public InnerThemeListBean(int type) {
        this.type = type;
    }

    public InnerThemeListBean() {
    }

    /**
     * id : 319
     * content : 我是一只小小鸟，一下子就飞很高，知道我是怎么做的的吗，
     * author_userid : 20002
     * status : 1
     * comments : 0
     * collections : 1
     * like : 0
     * displayorder : 0
     * topic_id : 3
     * terminal_id : 0
     * posttableid : 9
     * last_at : null
     * last_reply_userid : null
     * created_at : 2018-04-25 19:36:41
     * updated_at : 2018-04-26 10:03:24
     * src : ["http://provider.test.6rooms.net/upload/bbs_thread/20180425/20002/src/20180425113641_2UPZPl1IEx3dzTdBd3Kx.png"]
     * thumb : ["http://provider.test.6rooms.net/upload/bbs_thread/20180425/20002/300_300/20180425113641_2UPZPl1IEx3dzTdBd3Kx.png"]
     * hasLike : 0
     * hasCollect : 0
     * author_username : 我最美101
     * portrait_thumb : http://provider.test.6rooms.net/upload/user/20180413/20002/100_100/20180413035414_qdsPq3N3khmh462g56KK.png
     */


    public Long getThemeid() {
        return themeid;
    }

    public void setThemeid(Long themeid) {
        this.themeid = themeid;
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

    public String getCommentscn() {
        return commentscn;
    }

    public void setCommentscn(String commentscn) {
        this.commentscn = commentscn;
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

    public int getDisplayorder() {
        return displayorder;
    }

    public void setDisplayorder(int displayorder) {
        this.displayorder = displayorder;
    }

    public int getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(int topic_id) {
        this.topic_id = topic_id;
    }

    public long getTerminal_id() {
        return terminal_id;
    }

    public void setTerminal_id(long terminal_id) {
        this.terminal_id = terminal_id;
    }

    public int getPosttableid() {
        return posttableid;
    }

    public void setPosttableid(int posttableid) {
        this.posttableid = posttableid;
    }

    public String getLast_at() {
        return last_at;
    }

    public void setLast_at(String last_at) {
        this.last_at = last_at;
    }

    public String getLast_reply_userid() {
        return last_reply_userid;
    }

    public void setLast_reply_userid(String last_reply_userid) {
        this.last_reply_userid = last_reply_userid;
    }

    public String getCreated_at2() {
        return created_at2;
    }

    public void setCreated_at2(String created_at) {
        this.created_at2 = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public int getHasLike() {
        return hasLike;
    }

    public void setHasLike(int hasLike) {
        this.hasLike = hasLike;
    }

    public int getHasCollect() {
        return hasCollect;
    }

    public void setHasCollect(int hasCollect) {
        this.hasCollect = hasCollect;
    }

    public String getAuthor_username() {
        return author_username;
    }

    public void setAuthor_username(String author_username) {
        this.author_username = author_username;
    }

    public String getPortrait_thumb() {
        return portrait_thumb;
    }

    public void setPortrait_thumb(String portrait_thumb) {
        this.portrait_thumb = portrait_thumb;
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

    public List<ReplyUsersBean> getReplyUsers() {
        return replyUsers;
    }

    public void setReplyUsers(List<ReplyUsersBean> replyUsers) {
        this.replyUsers = replyUsers;
    }


}
