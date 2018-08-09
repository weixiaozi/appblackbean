package bbs.com.xinfeng.bbswork.domin;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dell on 2018/3/20.
 */
//消息页面
public class MessageFragmentBean extends BaseSocketBean {


    /**
     * a : 8
     * s : 1527056007.6802
     * data : {"type":1,"global":"22:133ie","list":[{"id":9,"type":1,"label":"96猫","img":"http://provider.test.6rooms.net/upload/user/20180417/20004/100_100/20180417091227_06GmeBfFQTMQjJIboQsx.png","last_at":"2018-05-24 13:48:14","last_msg":"1527140894.3831","current":"133ie"},{"id":11,"type":1,"label":"t98765","img":"http://provider.proxy.6rooms.net/upload/bbs_portrait/20180512/20128/100_100/20180512143558_z93pdtvaI03joYqmIMuN.png","last_at":"2018-05-24 15:35:27","last_msg":"1527147327.2279","current":"133iv","n":4}]}
     */

    private int a;
    private String s;
    private DataBean data;

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

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
        /**
         * type : 1
         * global : 22:133ie
         * list : [{"id":9,"type":1,"label":"96猫","img":"http://provider.test.6rooms.net/upload/user/20180417/20004/100_100/20180417091227_06GmeBfFQTMQjJIboQsx.png","last_at":"2018-05-24 13:48:14","last_msg":"1527140894.3831","current":"133ie"},{"id":11,"type":1,"label":"t98765","img":"http://provider.proxy.6rooms.net/upload/bbs_portrait/20180512/20128/100_100/20180512143558_z93pdtvaI03joYqmIMuN.png","last_at":"2018-05-24 15:35:27","last_msg":"1527147327.2279","current":"133iv","n":4}]
         */

        @SerializedName("type")
        private int typeX;
        private String global;
        private List<MessageFragmentInnerBean> list;

        public int getTypeX() {
            return typeX;
        }

        public void setTypeX(int typeX) {
            this.typeX = typeX;
        }

        public String getGlobal() {
            return global;
        }

        public void setGlobal(String global) {
            this.global = global;
        }

        public List<MessageFragmentInnerBean> getList() {
            return list;
        }

        public void setList(List<MessageFragmentInnerBean> list) {
            this.list = list;
        }

    }
}
