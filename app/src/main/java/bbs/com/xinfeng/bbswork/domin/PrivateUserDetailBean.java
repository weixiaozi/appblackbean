package bbs.com.xinfeng.bbswork.domin;

/**
 * Created by dell on 2016/11/23 17:46.
 */
//私聊获取用户附属信息
public class PrivateUserDetailBean extends ErrorBean {
    public static final String URL = "api/chat/more";


    /**
     * session_id : 107
     * isblock : 1
     */

    private String session_id;
    private int isblock;

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public int getIsblock() {
        return isblock;
    }

    public void setIsblock(int isblock) {
        this.isblock = isblock;
    }
}

