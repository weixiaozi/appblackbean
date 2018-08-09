package bbs.com.xinfeng.bbswork.domin;

/**
 * Created by dell on 2016/11/23 17:46.
 */
//注册
public class RegisterBean extends ErrorBean {
    public static final String URL = "api/user/reg";


    /**
     * user : {"id":40145,"name":"","phone":"13720089001","portrait":"","status":0,"online":0,"created_at":"2018-04-03 10:31:19","updated_at":"2018-04-08 09:47:03"}
     * profile : true
     */

    private UserBean user;


    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public static class UserBean {
        /**
         * id : 40145
         * name :
         * phone : 13720089001
         * portrait :
         * status : 0
         * online : 0
         * created_at : 2018-04-03 10:31:19
         * updated_at : 2018-04-08 09:47:03
         */

        private int id;
        private String name;
        private String phone;
        private String portrait;
        private int status;
        private int online;
        private String created_at;
        private String updated_at;
        private boolean profile;

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

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPortrait() {
            return portrait;
        }

        public void setPortrait(String portrait) {
            this.portrait = portrait;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getOnline() {
            return online;
        }

        public void setOnline(int online) {
            this.online = online;
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

        public boolean isProfile() {
            return profile;
        }

        public void setProfile(boolean profile) {
            this.profile = profile;
        }

    }
}

