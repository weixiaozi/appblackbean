package bbs.com.xinfeng.bbswork.domin;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dell on 2018/3/20.
 */
//消息更新：删除，撤销，已读
public class MesInfoChangeBean extends BaseSocketBean {

    /**
     * a : 15
     * si : 101
     * type : 1
     * data : 2zjcy3
     */

    private int a;
    private int si;
    private int type;
    private String data;
    private String c;
    private String ut;
    private String ud;

    public String getUt() {
        return ut;
    }

    public void setUt(String ut) {
        this.ut = ut;
    }

    public String getUd() {
        return ud;
    }

    public void setUd(String ud) {
        this.ud = ud;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public int getSi() {
        return si;
    }

    public void setSi(int si) {
        this.si = si;
    }

    public int getTypeX() {
        return type;
    }

    public void setTypeX(int typeX) {
        this.type = typeX;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
