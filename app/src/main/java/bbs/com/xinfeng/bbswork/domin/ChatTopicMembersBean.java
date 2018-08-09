package bbs.com.xinfeng.bbswork.domin;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dell on 2016/11/23 11:02.
 */

public class ChatTopicMembersBean extends ErrorBean implements Serializable {
    public static final String URL = "api/topic/member";

    /**
     * count : {"all":15,"new":0,"online":1}
     * list : [{"user_id":20005,"join_at":"2018-04-13 13:15:37","is_new":0,"online":0,"user_portrait_thumb":"http://provider.test.6rooms.net/upload/user/20180413/10066/100_100/20180413052242_3yn6rPaUvlfNRLXIWtsS.png"},{"user_id":20012,"join_at":"2018-04-13 14:04:16","is_new":0,"online":0,"user_portrait_thumb":"http://provider.test.6rooms.net/upload/user/20180418/20012/100_100/20180418092133_8fSULPxfrdfw24XrtVCu.png"},{"user_id":20014,"join_at":"2018-04-16 14:44:32","is_new":0,"online":0,"user_portrait_thumb":"http://provider.test.6rooms.net/upload/user/20180413/20014/100_100/20180413061018_ay9OGWVlHJVhLdCrHdHE.png"},{"user_id":20004,"join_at":"2018-04-17 18:17:22","is_new":0,"online":1,"user_portrait_thumb":"http://provider.test.6rooms.net/upload/user/20180417/20004/100_100/20180417091227_06GmeBfFQTMQjJIboQsx.png"},{"user_id":20024,"join_at":"2018-04-16 09:44:11","is_new":0,"online":0,"user_portrait_thumb":"http://provider.test.6rooms.net/upload/user/20180413/20024/100_100/20180413074738_NAEFWvWJhgTlZy9b1A8G.png"},{"user_id":20021,"join_at":"2018-04-13 16:37:25","is_new":0,"online":0,"user_portrait_thumb":"http://provider.test.6rooms.net/upload/user/20180413/20021/100_100/20180413083739_ls9CXgjdVUVuymv3cLC9.png"},{"user_id":20030,"join_at":"2018-04-13 16:42:14","is_new":0,"online":0,"user_portrait_thumb":"http://provider.test.6rooms.net/upload/user/20180413/20030/100_100/20180413084142_xX0v12nhPsHVYnaQVaUh.png"},{"user_id":20002,"join_at":"2018-04-24 15:27:34","is_new":0,"online":0,"user_portrait_thumb":"http://provider.test.6rooms.net/upload/user/20180413/20002/100_100/20180413035414_qdsPq3N3khmh462g56KK.png"}]
     */
    private int noread;

    public int getNoread() {
        return noread;
    }

    public void setNoread(int noread) {
        this.noread = noread;
    }

    private CountBean count;
    private List<ListBean> list;

    public CountBean getCount() {
        return count;
    }

    public void setCount(CountBean count) {
        this.count = count;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class CountBean {
        /**
         * all : 15
         * new : 0
         * online : 1
         */

        private int all;
        @SerializedName("new")
        private int newX;
        private int online;

        public int getAll() {
            return all;
        }

        public void setAll(int all) {
            this.all = all;
        }

        public int getNewX() {
            return newX;
        }

        public void setNewX(int newX) {
            this.newX = newX;
        }

        public int getOnline() {
            return online;
        }

        public void setOnline(int online) {
            this.online = online;
        }
    }

    public static class ListBean {
        /**
         * user_id : 20005
         * join_at : 2018-04-13 13:15:37
         * is_new : 0
         * online : 0
         * user_portrait_thumb : http://provider.test.6rooms.net/upload/user/20180413/10066/100_100/20180413052242_3yn6rPaUvlfNRLXIWtsS.png
         */

        private int user_id;
        private String join_at;
        private int is_new;
        private int online;
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

        public String getJoin_at() {
            return join_at;
        }

        public void setJoin_at(String join_at) {
            this.join_at = join_at;
        }

        public int getIs_new() {
            return is_new;
        }

        public void setIs_new(int is_new) {
            this.is_new = is_new;
        }

        public int getOnline() {
            return online;
        }

        public void setOnline(int online) {
            this.online = online;
        }

        public String getUser_portrait_thumb() {
            return user_portrait_thumb;
        }

        public void setUser_portrait_thumb(String user_portrait_thumb) {
            this.user_portrait_thumb = user_portrait_thumb;
        }
    }
}
