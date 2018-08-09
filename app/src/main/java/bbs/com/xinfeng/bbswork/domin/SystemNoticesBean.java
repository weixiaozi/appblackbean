package bbs.com.xinfeng.bbswork.domin;

import java.util.List;

/**
 * Created by dell on 2018/4/16.
 */

public class SystemNoticesBean extends ErrorBean {
    public static final String URL = "api/system/message";

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 2
         * title : 需求
         * content : 运营需求
         * type : 2
         * status : 1
         * created_at : 2018-04-13 14:34:53
         * updated_at : 2018-04-13 14:34:53
         */

        private int id;
        private String title;
        private String content;
        private int type;
        private int status;
        private String created_at2;
        private String updated_at;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
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
    }
}
