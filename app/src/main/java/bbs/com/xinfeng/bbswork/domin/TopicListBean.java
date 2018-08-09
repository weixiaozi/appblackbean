package bbs.com.xinfeng.bbswork.domin;

import java.util.List;

/**
 * Created by dell on 2016/11/23 17:46.
 */
//获取所有话题列表
public class TopicListBean extends ErrorBean {
    public static final String URL = "api/topic/list";


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1
         * name : 莱特币
         * introduce : 本圈正式更改名字,由比特币修改为莱特币
         * img_url : http://provider.test.6rooms.net/upload/user/20180408/40127/src/20180408070241_rDwXkKnNF3hgC6ZtVX6o.png
         * fans_number : 1
         * thread_number : 6
         * create_userid : 1
         * last_at : 2018-04-09 15:38:16
         * last_thread_id : 89
         * created_at : 2018-03-14 17:54:36
         * updated_at : 2018-04-09 15:38:16
         * img_url_thumb : http://provider.test.6rooms.net/upload/user/20180408/40127/200_200/20180408070241_rDwXkKnNF3hgC6ZtVX6o.png
         * isjoin : 0
         * create_username : guesth5
         */

        private int id;
        private String name;
        private String introduce;
        private String img_url;
        private int fans_number;
        private int thread_number;
        private int create_userid;
        private String last_at;
        private int last_thread_id;
        private String time_label;
        private String updated_at;
        private String img_url_thumb;
        private int isjoin;
        private String create_username;

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

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public String getImg_url_thumb() {
            return img_url_thumb;
        }

        public void setImg_url_thumb(String img_url_thumb) {
            this.img_url_thumb = img_url_thumb;
        }

        public int getIsjoin() {
            return isjoin;
        }

        public void setIsjoin(int isjoin) {
            this.isjoin = isjoin;
        }

        public String getCreate_username() {
            return create_username;
        }

        public void setCreate_username(String create_username) {
            this.create_username = create_username;
        }
    }
}

