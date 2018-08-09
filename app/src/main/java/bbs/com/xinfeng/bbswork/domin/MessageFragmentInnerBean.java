package bbs.com.xinfeng.bbswork.domin;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by dell on 2018/6/21.
 */
@Entity
public class MessageFragmentInnerBean {


    /**
     * id : 11
     * type : 1
     * label : t98765
     * img : http://provider.proxy.6rooms.net/upload/bbs_portrait/20180512/20128/100_100/20180512143558_z93pdtvaI03joYqmIMuN.png
     * last_at : 2018-05-24 15:35:27
     * last_msg : 1527147327.2279
     * current : 133iv
     * n : 4
     */
    @Id
    private long boxid;
    private int id;
    private int type;
    private String label;
    private String img;
    private String last_at;
    private String last_msg = "";
    private String current;
    private int n;
    private int tid;
    private int last_uid;
    private int uid_android;

    public int getUid_android() {
        return uid_android;
    }

    public void setUid_android(int uid_android) {
        this.uid_android = uid_android;
    }

    public int getLast_uid() {
        return last_uid;
    }

    public void setLast_uid(int last_uid) {
        this.last_uid = last_uid;
    }

    public long getBoxid() {
        return boxid;
    }

    public void setBoxid(long boxid) {
        this.boxid = boxid;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getLast_at() {
        return last_at;
    }

    public void setLast_at(String last_at) {
        this.last_at = last_at;
    }

    public String getLast_msg() {
        return last_msg;
    }

    public void setLast_msg(String last_msg) {
        this.last_msg = last_msg;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }
}
