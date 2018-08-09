package bbs.com.xinfeng.bbswork.domin;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dell on 2016/11/23 17:46.
 */
//通知有变动
public class NoticeChangeBean extends ErrorBean {
    public static final String URL = "api/notice";


    /**
     * current : 121
     * data : [{"type":22,"ids":[[1,1,1],[1,1,1]]}]
     */

    private long current;
    private List<DataBean> data;

    public long getCurrent() {
        return current;
    }

    public void setCurrent(long current) {
        this.current = current;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * type : 22
         * ids : [[1,1,1],[1,1,1]]
         */

        @SerializedName("type")
        private int typeX;
        private int num;
        private List<List<Integer>> ids;

        public int getTypeX() {
            return typeX;
        }

        public void setTypeX(int typeX) {
            this.typeX = typeX;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public List<List<Integer>> getIds() {
            return ids;
        }

        public void setIds(List<List<Integer>> ids) {
            this.ids = ids;
        }
    }
}

