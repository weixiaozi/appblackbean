package bbs.com.xinfeng.bbswork.domin;

/**
 * Created by dell on 2016/11/23 17:46.
 */
//用户观点，回复，关注，粉丝等数量
public class UserContentBean extends ErrorBean {
    public static final String URL = "api/user/statist";


    /**
     * data : {"fans":0,"follows":0,"replies":1,"threads":3}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * fans : 0
         * follows : 0
         * replies : 1
         * threads : 3
         */

        private int fans;
        private int follows;
        private int replies;
        private int threads;
        private int blocks;

        public int getBlocks() {
            return blocks;
        }

        public void setBlocks(int blocks) {
            this.blocks = blocks;
        }

        public int getFans() {
            return fans;
        }

        public void setFans(int fans) {
            this.fans = fans;
        }

        public int getFollows() {
            return follows;
        }

        public void setFollows(int follows) {
            this.follows = follows;
        }

        public int getReplies() {
            return replies;
        }

        public void setReplies(int replies) {
            this.replies = replies;
        }

        public int getThreads() {
            return threads;
        }

        public void setThreads(int threads) {
            this.threads = threads;
        }
    }
}

