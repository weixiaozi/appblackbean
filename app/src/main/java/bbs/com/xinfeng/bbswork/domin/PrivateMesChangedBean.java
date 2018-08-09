package bbs.com.xinfeng.bbswork.domin;

/**
 * Created by dell on 2018/3/20.
 */
//消息变动
public class PrivateMesChangedBean extends BaseSocketBean {

    /**
     * a : 5
     * si : 76
     */

    private int a;
    private int si;
    private int t;

    public int getT() {
        return t;
    }

    public void setT(int t) {
        this.t = t;
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
}
