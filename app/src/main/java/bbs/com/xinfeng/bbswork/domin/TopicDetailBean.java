package bbs.com.xinfeng.bbswork.domin;


import java.util.ArrayList;
import java.util.List;

public class TopicDetailBean extends ErrorBean {
    public static final String URL = "api/topic/detail";


    /**
     * status : 1
     * isauthor : 0
     * topic : {"id":64,"name":"发撒大概v嘘嘘","introduce":"电脑的表示表示不作不死表示表示不是个事v过","img_url":"http://provider.test.6rooms.net/upload/user/20180408/40127/src/20180408070241_rDwXkKnNF3hgC6ZtVX6o.png","fans_number":5,"thread_number":0,"create_userid":10027,"status":1,"last_at":"2018-03-21 15:04:28","last_thread_id":0,"created_at":"2018-03-21 15:04:28","updated_at":"2018-04-10 18:20:48","create_user_name":"test027","create_user_portrait":"","create_user_portrait_thumb":"","img_url_thumb":"http://provider.test.6rooms.net/upload/user/20180408/40127/200_200/20180408070241_rDwXkKnNF3hgC6ZtVX6o.png"}
     * fans : [{"user_id":10070,"user_name":"test070","user_portrait":"","user_portrait_thumb":""},{"user_id":10005,"user_name":"test005","user_portrait":"","user_portrait_thumb":""},{"user_id":1,"user_name":"guesth5","user_portrait":"","user_portrait_thumb":""},{"user_id":10066,"user_name":"test066","user_portrait":"http://provider.test.6rooms.net/upload/user/20180402/10127/src/20180402064133_GFPqeweA8Ya06ae6fIgz.png","user_portrait_thumb":"http://provider.test.6rooms.net/upload/user/20180402/10127/100_100/20180402064133_GFPqeweA8Ya06ae6fIgz.png"},{"user_id":40147,"user_name":"kkjnk","user_portrait":"http://provider.test.6rooms.net/upload/user/20180410/40147/src/20180410093927_hSJeTrkNomyQDjundrip.png","user_portrait_thumb":"http://provider.test.6rooms.net/upload/user/20180410/40147/100_100/20180410093927_hSJeTrkNomyQDjundrip.png"}]
     * thread : [{"id":36,"content":"这是新的主题帖内容,详细内容111111","author_userid":10083,"comments":2,"collections":0,"like":1,"displayorder":0,"topic_id":64,"terminal_id":"","posttableid":6,"last_at":"","created_at":"2018-03-21 15:54:40","updated_at":"2018-04-09 13:26:20","author_user_name":"test083","author_user_portrait":"","author_user_portrait_thumb":"","img":{"src":["http://provider.test.6rooms.net/upload/user/20180408/40127/src/20180408085440_PemkVuLUMdXtFmBbWWtn.png","http://provider.test.6rooms.net/upload/user/20180408/40127/src/20180408085440_PemkVuLUMdXtFmBbWWtn.png"],"thumb":["http://provider.test.6rooms.net/upload/user/20180408/40127/300_300/20180408085440_PemkVuLUMdXtFmBbWWtn.png","http://provider.test.6rooms.net/upload/user/20180408/40127/300_300/20180408085440_PemkVuLUMdXtFmBbWWtn.png"]}}]
     */

    private int status;
    private int isauthor;
    private TopicBean topic;
    private List<FansBean> fans;
    private List<ThreadBean> thread;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIsauthor() {
        return isauthor;
    }

    public void setIsauthor(int isauthor) {
        this.isauthor = isauthor;
    }

    public TopicBean getTopic() {
        return topic;
    }

    public void setTopic(TopicBean topic) {
        this.topic = topic;
    }

    public List<FansBean> getFans() {
        return fans;
    }

    public void setFans(List<FansBean> fans) {
        this.fans = fans;
    }

    public List<ThreadBean> getThread() {
        return thread;
    }

    public void setThread(List<ThreadBean> thread) {
        this.thread = thread;
    }

    public static class TopicBean {
        /**
         * id : 64
         * name : 发撒大概v嘘嘘
         * introduce : 电脑的表示表示不作不死表示表示不是个事v过
         * img_url : http://provider.test.6rooms.net/upload/user/20180408/40127/src/20180408070241_rDwXkKnNF3hgC6ZtVX6o.png
         * fans_number : 5
         * thread_number : 0
         * create_userid : 10027
         * status : 1
         * last_at : 2018-03-21 15:04:28
         * last_thread_id : 0
         * created_at : 2018-03-21 15:04:28
         * updated_at : 2018-04-10 18:20:48
         * create_user_name : test027
         * create_user_portrait :
         * create_user_portrait_thumb :
         * img_url_thumb : http://provider.test.6rooms.net/upload/user/20180408/40127/200_200/20180408070241_rDwXkKnNF3hgC6ZtVX6o.png
         */

        private int id;
        private String name;
        private String introduce;
        private String img_url;
        private int fans_number;
        private int thread_number;
        private int create_userid;
        private int status;
        private String last_at;
        private int last_thread_id;
        private String created_at;
        private String time_label;
        private String updated_at;
        private String create_user_name;
        private String create_user_portrait;
        private String create_user_portrait_thumb;
        private String img_url_thumb;
        private String web_share;

        public String getWeb_share() {
            return web_share;
        }

        public void setWeb_share(String web_share) {
            this.web_share = web_share;
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

        public String getIntroduce() {
            return introduce;
        }

        public void setIntroduce(String introduce) {
            this.introduce = introduce;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public int getFans_number() {
            return fans_number;
        }

        public void setFans_number(int fans_number) {
            this.fans_number = fans_number;
        }

        public int getThread_number() {
            return thread_number;
        }

        public void setThread_number(int thread_number) {
            this.thread_number = thread_number;
        }

        public int getCreate_userid() {
            return create_userid;
        }

        public void setCreate_userid(int create_userid) {
            this.create_userid = create_userid;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getLast_at() {
            return last_at;
        }

        public void setLast_at(String last_at) {
            this.last_at = last_at;
        }

        public int getLast_thread_id() {
            return last_thread_id;
        }

        public void setLast_thread_id(int last_thread_id) {
            this.last_thread_id = last_thread_id;
        }

        public String getTime_label() {
            return time_label;
        }

        public void setTime_label(String time_label) {
            this.time_label = time_label;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public String getCreate_user_name() {
            return create_user_name;
        }

        public void setCreate_user_name(String create_user_name) {
            this.create_user_name = create_user_name;
        }

        public String getCreate_user_portrait() {
            return create_user_portrait;
        }

        public void setCreate_user_portrait(String create_user_portrait) {
            this.create_user_portrait = create_user_portrait;
        }

        public String getCreate_user_portrait_thumb() {
            return create_user_portrait_thumb;
        }

        public void setCreate_user_portrait_thumb(String create_user_portrait_thumb) {
            this.create_user_portrait_thumb = create_user_portrait_thumb;
        }

        public String getImg_url_thumb() {
            return img_url_thumb;
        }

        public void setImg_url_thumb(String img_url_thumb) {
            this.img_url_thumb = img_url_thumb;
        }
    }

    public static class FansBean {
        /**
         * user_id : 10070
         * user_name : test070
         * user_portrait :
         * user_portrait_thumb :
         */

        private int user_id;
        private String user_name;
        private String user_portrait;
        private String user_portrait_thumb;

        private int drawableId;//自定义，加载本地资源

        public int getDrawableId() {
            return drawableId;
        }

        public void setDrawableId(int drawableId) {
            this.drawableId = drawableId;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getUser_portrait() {
            return user_portrait;
        }

        public void setUser_portrait(String user_portrait) {
            this.user_portrait = user_portrait;
        }

        public String getUser_portrait_thumb() {
            return user_portrait_thumb;
        }

        public void setUser_portrait_thumb(String user_portrait_thumb) {
            this.user_portrait_thumb = user_portrait_thumb;
        }
    }

    public static class ThreadBean {
        /**
         * id : 36
         * content : 这是新的主题帖内容,详细内容111111
         * author_userid : 10083
         * comments : 2
         * collections : 0
         * like : 1
         * displayorder : 0
         * topic_id : 64
         * terminal_id :
         * posttableid : 6
         * last_at :
         * created_at : 2018-03-21 15:54:40
         * updated_at : 2018-04-09 13:26:20
         * author_user_name : test083
         * author_user_portrait :
         * author_user_portrait_thumb :
         * img : {"src":["http://provider.test.6rooms.net/upload/user/20180408/40127/src/20180408085440_PemkVuLUMdXtFmBbWWtn.png","http://provider.test.6rooms.net/upload/user/20180408/40127/src/20180408085440_PemkVuLUMdXtFmBbWWtn.png"],"thumb":["http://provider.test.6rooms.net/upload/user/20180408/40127/300_300/20180408085440_PemkVuLUMdXtFmBbWWtn.png","http://provider.test.6rooms.net/upload/user/20180408/40127/300_300/20180408085440_PemkVuLUMdXtFmBbWWtn.png"]}
         */

        private int id;
        private String content;
        private int author_userid;
        private String commentscn;
        private int collections;
        private String likecn;
        private int displayorder;
        private int topic_id;
        private String terminal_id;
        private int posttableid;
        private String last_at;
        private String created_at2;
        private String updated_at;
        private String author_user_name;
        private String author_user_portrait;
        private String author_user_portrait_thumb;
        private ImgBean img;
        private int isread;

        private List<AttachThemeBean> attch;
        private List<String> src = new ArrayList<>();
        private List<String> thumb = new ArrayList<>();
        private int isAnimator;
        private int audioread;
        private int videoType;//1：图片2：语音3:视频
        private String videoPath;
        private String coverPath;
        private int duration;

        public int getIsread() {
            return isread;
        }

        public void setIsread(int isread) {
            this.isread = isread;
        }

        public List<AttachThemeBean> getAttch() {
            return attch;
        }

        public void setAttch(List<AttachThemeBean> attch) {
            this.attch = attch;
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

        public int getAuthor_userid() {
            return author_userid;
        }

        public void setAuthor_userid(int author_userid) {
            this.author_userid = author_userid;
        }

        public String getComments() {
            return commentscn;
        }

        public void setComments(String comments) {
            this.commentscn = comments;
        }

        public int getCollections() {
            return collections;
        }

        public void setCollections(int collections) {
            this.collections = collections;
        }

        public String getLike() {
            return likecn;
        }

        public void setLike(String like) {
            this.likecn = like;
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

        public String getTerminal_id() {
            return terminal_id;
        }

        public void setTerminal_id(String terminal_id) {
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

        public String getAuthor_user_name() {
            return author_user_name;
        }

        public void setAuthor_user_name(String author_user_name) {
            this.author_user_name = author_user_name;
        }

        public String getAuthor_user_portrait() {
            return author_user_portrait;
        }

        public void setAuthor_user_portrait(String author_user_portrait) {
            this.author_user_portrait = author_user_portrait;
        }

        public String getAuthor_user_portrait_thumb() {
            return author_user_portrait_thumb;
        }

        public void setAuthor_user_portrait_thumb(String author_user_portrait_thumb) {
            this.author_user_portrait_thumb = author_user_portrait_thumb;
        }

        public ImgBean getImg() {
            return img;
        }

        public void setImg(ImgBean img) {
            this.img = img;
        }

        public static class ImgBean {
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
}

