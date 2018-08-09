package bbs.com.xinfeng.bbswork.domin;

/**
 * Created by dell on 2018/3/20.
 */
//添加会话
public class AddChatBean extends BaseSocketBean {

    /**
     * a : 10
     * code : 100000
     * message : 操作成功
     * data : {"id":21}
     */

    private String a;
    private DataBean data;

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }


    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 21
         */

        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
