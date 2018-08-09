package bbs.com.xinfeng.bbswork.domin;

/**
 * Created by dell on 2018/7/16.
 */

public class PrivateBackRunBus {
    public int type;//0：fail；1：success
    public int chatId;
    public long time;
    public String id;

    public PrivateBackRunBus(int type, int chatId, long time, String id) {
        this.type = type;
        this.chatId = chatId;
        this.time = time;
        this.id = id;
    }
}
