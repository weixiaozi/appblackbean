package bbs.com.xinfeng.bbswork.domin;

import java.util.List;

/**
 * Created by dell on 2018/3/20.
 */
//黑名单list
public class MyBlackListBean extends ErrorBean {
    public static final String URL = "api/user/blocklist";


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * user_id : 20005
         * followed_at : 2018-05-15 13:03:48
         * username : 孙悟空
         * isonline : 0
         * isblock : 1
         * portrait : http://provider.test.6rooms.net/upload/user/20180413/10066/src/20180413052242_3yn6rPaUvlfNRLXIWtsS.png
         * portrait_thumb : http://provider.test.6rooms.net/upload/user/20180413/10066/100_100/20180413052242_3yn6rPaUvlfNRLXIWtsS.png
         */

        private int user_id;
        private String followed_at;
        private String username;
        private int isonline;
        private int isblock;
        private String portrait;
        private String portrait_thumb;

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getFollowed_at() {
            return followed_at;
        }

        public void setFollowed_at(String followed_at) {
            this.followed_at = followed_at;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public int getIsonline() {
            return isonline;
        }

        public void setIsonline(int isonline) {
            this.isonline = isonline;
        }

        public int getIsblock() {
            return isblock;
        }

        public void setIsblock(int isblock) {
            this.isblock = isblock;
        }

        public String getPortrait() {
            return portrait;
        }

        public void setPortrait(String portrait) {
            this.portrait = portrait;
        }

        public String getPortrait_thumb() {
            return portrait_thumb;
        }

        public void setPortrait_thumb(String portrait_thumb) {
            this.portrait_thumb = portrait_thumb;
        }
    }
}
