package bbs.com.xinfeng.bbswork.domin;

import java.util.List;

/**
 * Created by dell on 2016/11/23 17:46.
 */
//用户详情
public class UserDetailBean extends ErrorBean {
    public static final String URL = "api/user/detail";


    /**
     * user : {"name":"双面人","introduce":"双面、人双面、人","portrait_thumb":"http://provider.test.6rooms.net/upload/bbs_portrait/20180502/20001/100_100/20180502141335_M3AEeoGwFjw52GUtohGo.png","follows":3,"fans":2,"isme":0,"isfollow":1}
     * topics : [{"id":3,"name":"人文环境讨论组","img_url_thumb":"http://provider.test.6rooms.net/upload/user/20180413/20002/200_200/20180413040256_H85UAjWbp5X1MyII6jto.png","joined_at":"2018-04-13 15:05:34","thread_num":4,"post_num":5,"like_num":1,"isjoin":0},{"id":2,"name":"二次元女主角的修养","img_url_thumb":"http://provider.test.6rooms.net/upload/user/20180413/20004/200_200/20180413040154_MYGChAh75Wan2cOvZYqZ.png","joined_at":"2018-04-13 14:05:23","thread_num":1,"post_num":0,"like_num":1,"isjoin":1},{"id":1,"name":"废萌是必要的！","img_url_thumb":"http://provider.test.6rooms.net/upload/user/20180413/20004/200_200/20180413035935_QgzJeBggOvxRgC1l3V4R.png","joined_at":"2018-04-13 14:05:10","thread_num":1,"post_num":0,"like_num":0,"isjoin":1},{"id":12,"name":"区块链","img_url_thumb":"http://provider.test.6rooms.net/upload/user/20180413/20001/200_200/20180413052657_6EhLzSto6beLwqdSWdZD.jpg","joined_at":"2018-04-13 13:27:28","thread_num":6,"post_num":1,"like_num":1,"isjoin":0},{"id":8,"name":"种树公益话题","img_url_thumb":"http://provider.test.6rooms.net/upload/user/20180413/20009/200_200/20180413051654_jrkykVVRdy7S9aCIKYNW.png","joined_at":"2018-04-13 14:26:40","thread_num":1,"post_num":0,"like_num":1,"isjoin":0}]
     */

    private UserBean user;
    private List<TopicsBean> topics;

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public List<TopicsBean> getTopics() {
        return topics;
    }

    public void setTopics(List<TopicsBean> topics) {
        this.topics = topics;
    }

    public static class UserBean {
        /**
         * name : 双面人
         * introduce : 双面、人双面、人
         * portrait_thumb : http://provider.test.6rooms.net/upload/bbs_portrait/20180502/20001/100_100/20180502141335_M3AEeoGwFjw52GUtohGo.png
         * follows : 3
         * fans : 2
         * isme : 0
         * isfollow : 1
         */

        private String name;
        private String introduce;
        private String portrait;
        private String portrait_thumb;
        private int follows;
        private int fans;
        private int isme;
        private int isfollow;
        private int isblock;
        private int session_id;

        public int getSession_id() {
            return session_id;
        }

        public void setSession_id(int session_id) {
            this.session_id = session_id;
        }

        public String getPortrait() {
            return portrait;
        }

        public void setPortrait(String portrait) {
            this.portrait = portrait;
        }

        public int getIsblock() {
            return isblock;
        }

        public void setIsblock(int isblock) {
            this.isblock = isblock;
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

        public String getPortrait_thumb() {
            return portrait_thumb;
        }

        public void setPortrait_thumb(String portrait_thumb) {
            this.portrait_thumb = portrait_thumb;
        }

        public int getFollows() {
            return follows;
        }

        public void setFollows(int follows) {
            this.follows = follows;
        }

        public int getFans() {
            return fans;
        }

        public void setFans(int fans) {
            this.fans = fans;
        }

        public int getIsme() {
            return isme;
        }

        public void setIsme(int isme) {
            this.isme = isme;
        }

        public int getIsfollow() {
            return isfollow;
        }

        public void setIsfollow(int isfollow) {
            this.isfollow = isfollow;
        }
    }

    public static class TopicsBean {
        /**
         * id : 3
         * name : 人文环境讨论组
         * img_url_thumb : http://provider.test.6rooms.net/upload/user/20180413/20002/200_200/20180413040256_H85UAjWbp5X1MyII6jto.png
         * joined_at : 2018-04-13 15:05:34
         * thread_num : 4
         * post_num : 5
         * like_num : 1
         * isjoin : 0
         */

        private int id;
        private String name;
        private String img_url_thumb;
        private String time_label;
        private int thread_num;
        private int post_num;
        private int like_num;
        private int isjoin;

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

        public String getImg_url_thumb() {
            return img_url_thumb;
        }

        public void setImg_url_thumb(String img_url_thumb) {
            this.img_url_thumb = img_url_thumb;
        }

        public String getTime_label() {
            return time_label;
        }

        public void setTime_label(String time_label) {
            this.time_label = time_label;
        }

        public int getThread_num() {
            return thread_num;
        }

        public void setThread_num(int thread_num) {
            this.thread_num = thread_num;
        }

        public int getPost_num() {
            return post_num;
        }

        public void setPost_num(int post_num) {
            this.post_num = post_num;
        }

        public int getLike_num() {
            return like_num;
        }

        public void setLike_num(int like_num) {
            this.like_num = like_num;
        }

        public int getIsjoin() {
            return isjoin;
        }

        public void setIsjoin(int isjoin) {
            this.isjoin = isjoin;
        }
    }
}

