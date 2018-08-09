package bbs.com.xinfeng.bbswork.domin;

import java.util.List;

/**
 * Created by dell on 2016/11/23 17:46.
 */
//获取话题的用户列表
public class TopicUsersBean extends ErrorBean {
    public static final String URL = "api/topic/user";


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * user_id : 10027
         * user_name : test023
         * user_portrait :
         */

        private int user_id;
        private String user_name;
        private String user_portrait;

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
    }
}

