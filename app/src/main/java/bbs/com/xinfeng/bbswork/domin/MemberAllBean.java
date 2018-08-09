package bbs.com.xinfeng.bbswork.domin;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dell on 2018/3/20.
 */
//用户相关的所有用户
public class MemberAllBean extends ErrorBean {
    public static final String URL = "api/topic/invitelist";
    public static final String URL1 = "api/topic/user";


    /**
     * data : [{"user_id":40145,"user_name":"尼桑君","user_portrait":"http://imgsrc.baidu.com/forum/w=580/sign=6ade238c064f78f0800b9afb49300a83/6cbb60d0f703918f31df8012543d269758eec47c.jpg","user_portrait_thumb":"","isjoin":1,"topic_name":"hhh巴巴爸爸/电话号还好还好哈就/尼桑君first"},{"user_id":10179,"user_name":"北国君","user_portrait":"http://provider.test.6rooms.net/upload/user/20180411/10179/src/20180411061212_mGioKT5orpJUHmYxT5Pe.jpg","user_portrait_thumb":"http://provider.test.6rooms.net/upload/user/20180411/10179/100_100/20180411061212_mGioKT5orpJUHmYxT5Pe.jpg","isjoin":0,"topic_name":"hhh巴巴爸爸/电话号还好还好哈就/尼桑君first"}]
     * current_page : 1
     * per_page : 10
     * total : 2
     */

    private int online_number;
    private int fans_number;


    private String current_page;
    private int per_page;
    private int total;
    private List<DataBean> data;

    public int getOnline_number() {
        return online_number;
    }

    public void setOnline_number(int online_number) {
        this.online_number = online_number;
    }

    public int getFans_number() {
        return fans_number;
    }

    public void setFans_number(int fans_number) {
        this.fans_number = fans_number;
    }

    public String getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(String current_page) {
        this.current_page = current_page;
    }

    public int getPer_page() {
        return per_page;
    }

    public void setPer_page(int per_page) {
        this.per_page = per_page;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * user_id : 40145
         * user_name : 尼桑君
         * user_portrait : http://imgsrc.baidu.com/forum/w=580/sign=6ade238c064f78f0800b9afb49300a83/6cbb60d0f703918f31df8012543d269758eec47c.jpg
         * user_portrait_thumb :
         * isjoin : 1
         * topic_name : hhh巴巴爸爸/电话号还好还好哈就/尼桑君first
         */

        private int selected;//自定义 0：未选中；1：选中

        private int user_id;
        private String user_name;
        private String user_portrait;
        private String user_portrait_thumb;
        private int isjoin;
        private String topic_name;

        private int online;
        private int isme;
        private int isauthor;

        public int getOnline() {
            return online;
        }

        public void setOnline(int online) {
            this.online = online;
        }

        public int getIsme() {
            return isme;
        }

        public void setIsme(int isme) {
            this.isme = isme;
        }

        public int getIsauther() {
            return isauthor;
        }

        public void setIsauther(int isauthor) {
            this.isauthor = isauthor;
        }

        public int getSelected() {
            return selected;
        }

        public void setSelected(int selected) {
            this.selected = selected;
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

        public int getIsjoin() {
            return isjoin;
        }

        public void setIsjoin(int isjoin) {
            this.isjoin = isjoin;
        }

        public String getTopic_name() {
            return topic_name;
        }

        public void setTopic_name(String topic_name) {
            this.topic_name = topic_name;
        }
    }
}
