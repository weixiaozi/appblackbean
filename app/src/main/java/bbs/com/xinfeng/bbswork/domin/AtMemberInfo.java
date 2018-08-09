package bbs.com.xinfeng.bbswork.domin;

/**
 * Created by dell on 2018/4/20.
 */

public class AtMemberInfo {
    private String name;
    private int uid;

    public AtMemberInfo(String name, int uid) {
        this.name = name;
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
