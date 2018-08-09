package bbs.com.xinfeng.bbswork.domin;

import java.io.Serializable;

/**
 * Created by dell on 2016/11/23 11:02.
 */

public class SetMesStatusBean extends ErrorBean implements Serializable {
    public static final String URL = "api/chat/setstatus";

    private int session_id;

    public int getSession_id() {
        return session_id;
    }

    public void setSession_id(int session_id) {
        this.session_id = session_id;
    }
}
