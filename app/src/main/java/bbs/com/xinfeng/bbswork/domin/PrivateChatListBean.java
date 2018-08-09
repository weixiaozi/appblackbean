package bbs.com.xinfeng.bbswork.domin;

import java.util.List;

/**
 * Created by dell on 2018/6/19.
 */

public class PrivateChatListBean extends BaseSocketBean {

    private String s;
    /**
     * code : 100000
     * message : 操作成功
     * data : {"list":[{"id":"12pnz","mt":1,"su":100001,"c1":"1529393491.936659","c2":"","at":"2018-06-19 15:31:32","s1":2,"s2":1},{"id":"12po0","mt":1,"su":100001,"c1":"1529393492.301561","c2":"","at":"2018-06-19 15:31:32","s1":2,"s2":1},{"id":"12po1","mt":1,"su":100002,"c1":"1529393492.338312","c2":"","at":"2018-06-19 15:31:32","s1":2,"s2":1},{"id":"12po2","mt":1,"su":100002,"c1":"1529393492.388584","c2":"","at":"2018-06-19 15:31:32","s1":2,"s2":1},{"id":"12po3","mt":1,"su":100001,"c1":"1529393492.438944","c2":"","at":"2018-06-19 15:31:32","s1":2,"s2":1},{"id":"12po4","mt":1,"su":100001,"c1":"1529393492.471259","c2":"","at":"2018-06-19 15:31:32","s1":2,"s2":1},{"id":"12po5","mt":1,"su":100002,"c1":"1529393492.505088","c2":"","at":"2018-06-19 15:31:32","s1":2,"s2":1},{"id":"12po6","mt":1,"su":100001,"c1":"1529393492.537913","c2":"","at":"2018-06-19 15:31:32","s1":2,"s2":1},{"id":"12po7","mt":1,"su":100001,"c1":"1529393492.571266","c2":"","at":"2018-06-19 15:31:32","s1":2,"s2":1},{"id":"ar4nq","mt":1,"su":100002,"c1":"1529393492.604887","c2":"","at":"2018-06-19 15:31:32","s1":2,"s2":1}],"user":[{"id":100001,"label":"张三","img":"http://res.proxy.6rooms.net/0/2/75b969e51d1f26150c2976e3tafdde74dc771150a5.png"},{"id":100002,"label":"李四","img":"http://res.proxy.6rooms.net/1/24/a9882a353bb33ifd3aeb8945723ac5bf36974107ef.png"}]}
     */

    private DataBean data;

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private int n;
        private String current;
        private String type;
        private String expire;
        private List<PrivateChatItemBean> list;
        private List<UserBean> user;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getN() {
            return n;
        }

        public void setN(int n) {
            this.n = n;
        }

        public String getCurrent() {
            return current;
        }

        public void setCurrent(String current) {
            this.current = current;
        }

        public String getExpire() {
            return expire;
        }

        public void setExpire(String expire) {
            this.expire = expire;
        }

        public List<PrivateChatItemBean> getList() {
            return list;
        }

        public void setList(List<PrivateChatItemBean> list) {
            this.list = list;
        }

        public List<UserBean> getUser() {
            return user;
        }

        public void setUser(List<UserBean> user) {
            this.user = user;
        }

        public static class UserBean {
            /**
             * id : 100001
             * label : 张三
             * img : http://res.proxy.6rooms.net/0/2/75b969e51d1f26150c2976e3tafdde74dc771150a5.png
             */

            private int id;
            private String label;
            private String img;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getLabel() {
                return label;
            }

            public void setLabel(String label) {
                this.label = label;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }
        }
    }
}
