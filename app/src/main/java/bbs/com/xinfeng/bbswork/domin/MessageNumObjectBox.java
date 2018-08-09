package bbs.com.xinfeng.bbswork.domin;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by dell on 2018/4/16.
 */
@Entity
public class MessageNumObjectBox {
    @Id
    private long id;

    private int replyNum;
    private int clikNum;
    private int notifyNum;
    private int operateNum;
    private int systemNotifyNum;
    private int user_id;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(int replyNum) {
        this.replyNum = replyNum;
    }

    public int getClikNum() {
        return clikNum;
    }

    public void setClikNum(int clikNum) {
        this.clikNum = clikNum;
    }

    public int getNotifyNum() {
        return notifyNum;
    }

    public void setNotifyNum(int notifyNum) {
        this.notifyNum = notifyNum;
    }

    public int getOperateNum() {
        return operateNum;
    }

    public void setOperateNum(int operateNum) {
        this.operateNum = operateNum;
    }

    public int getSystemNotifyNum() {
        return systemNotifyNum;
    }

    public void setSystemNotifyNum(int systemNotifyNum) {
        this.systemNotifyNum = systemNotifyNum;
    }
}
