package bbs.com.xinfeng.bbswork.domin;

import java.util.List;

/**
 * Created by dell on 2016/11/23 17:46.
 */
//获取联系人列表
public class ContactListBean extends ErrorBean {
    public static final String URL = "api/chat/followlist";


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * user_id : 10001
         * followed_at : 2018-04-17 13:45:53
         * username : test001
         * isonline : 0
         * isfollow : 1
         * portrait : http://provider.test.6rooms.net/upload/user/20180413/10066/src/20180413040443_iDeMao19Sj88HTDRNPtO.png
         * portrait_thumb : http://provider.test.6rooms.net/upload/user/20180413/10066/100_100/20180413040443_iDeMao19Sj88HTDRNPtO.png
         */

        private int user_id;
        private int session_id;
        private String followed_at;
        private String username;
        private int isonline;
        private int isfollow;
        private String portrait;
        private String portrait_thumb;

        public int getSession_id() {
            return session_id;
        }

        public void setSession_id(int session_id) {
            this.session_id = session_id;
        }

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

        public int getIsfollow() {
            return isfollow;
        }

        public void setIsfollow(int isfollow) {
            this.isfollow = isfollow;
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

