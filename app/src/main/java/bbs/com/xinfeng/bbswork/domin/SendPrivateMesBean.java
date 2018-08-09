package bbs.com.xinfeng.bbswork.domin;

/**
 * Created by dell on 2018/3/20.
 */
//发送消息【下行】
public class SendPrivateMesBean extends BaseSocketBean {

    /**
     * a : 4
     * s : 1529394613531
     * code : 100000
     * message : 操作成功
     * data : {"current":"12pnz"}
     */

    private String a;
    private String s;
    private DataBean data;

    public String getA() {
        return a;
    }

    public void setA(String a) {
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
         * current : 12pnz
         */

        private String id;
        private int si;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getSi() {
            return si;
        }

        public void setSi(int si) {
            this.si = si;
        }
    }
}
