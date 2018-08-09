package bbs.com.xinfeng.bbswork.domin;

import java.util.List;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Transient;

/**
 * Created by dell on 2018/6/19.
 */
@Entity
public class PrivateChatItemBean {
    @Id
    private Long privateChatid;
    @Transient
    public static final int LOADING = -1;
    private int type;//-1:loading;0：other;1:self
    @Transient
    private int isAnimator;//0：默认1:加载中2：播放中
    private boolean isSelf;
    private int selfStatus;//1:loading2:error;3finish
    private double lastms;//发送失败的记录上一条的ms
    private boolean hasRunSend;//是否执行过socket的发送命令
    private boolean isBackRunning;//是否交给了后台执行
    private int uid_android;
    @Transient
    private String webPic;
    private String img;
    private long time;
    private int duration;
    private int chatId;
    private String ud;
    private String ut;

    public int getUid_android() {
        return uid_android;
    }

    public void setUid_android(int uid_android) {
        this.uid_android = uid_android;
    }

    public String getUd() {
        return ud;
    }

    public void setUd(String ud) {
        this.ud = ud;
    }

    public String getUt() {
        return ut;
    }

    public void setUt(String ut) {
        this.ut = ut;
    }

    public boolean isBackRunning() {
        return isBackRunning;
    }

    public void setBackRunning(boolean backRunning) {
        isBackRunning = backRunning;
    }

    public boolean isHasRunSend() {
        return hasRunSend;
    }

    public void setHasRunSend(boolean hasRunSend) {
        this.hasRunSend = hasRunSend;
    }

    public double getLastms() {
        return lastms;
    }

    public void setLastms(double lastms) {
        this.lastms = lastms;
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public Long getPrivateChatid() {
        return privateChatid;
    }

    public void setPrivateChatid(Long privateChatid) {
        this.privateChatid = privateChatid;
    }

    public PrivateChatItemBean(int type) {
        this.type = type;
    }

    public PrivateChatItemBean() {
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setIsAnimator(int isAnimator) {
        this.isAnimator = isAnimator;
    }

    public boolean isSelf() {
        return isSelf;
    }

    public void setSelf(boolean self) {
        isSelf = self;
    }

    public int getSelfStatus() {
        return selfStatus;
    }

    public void setSelfStatus(int selfStatus) {
        this.selfStatus = selfStatus;
    }

    public String getWebPic() {
        return webPic;
    }

    public void setWebPic(String webPic) {
        this.webPic = webPic;
    }

    public int isAnimator() {
        return isAnimator;
    }

    public void setAnimator(int isAnimator) {
        this.isAnimator = isAnimator;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private String id;
    private int mt;
    private int su;
    private String c1;
    private String c2;
    private double ms;
    private String tm;
    private int s1;
    private String s;

    public double getMs() {
        return ms;
    }

    public void setMs(double ms) {
        this.ms = ms;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public String getTm() {
        return tm;
    }

    public void setTm(String tm) {
        this.tm = tm;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMt() {
        return mt;
    }

    public void setMt(int mt) {
        this.mt = mt;
    }

    public int getSu() {
        return su;
    }

    public void setSu(int su) {
        this.su = su;
    }

    public String getC1() {
        return c1;
    }

    public void setC1(String c1) {
        this.c1 = c1;
    }

    public String getC2() {
        return c2;
    }

    public void setC2(String c2) {
        this.c2 = c2;
    }

    public int getS1() {
        return s1;
    }

    public void setS1(int s1) {
        this.s1 = s1;
    }

    @Override
    public String toString() {
        return "PrivateChatItemBean{" +
                "privateChatid=" + privateChatid +
                "time=" + time +
                ", isSelf=" + isSelf +
                ", selfStatus=" + selfStatus +
                ", lastms=" + lastms +
                ", hasRunSend=" + hasRunSend +
                ", chatId=" + chatId +
                ", id='" + id + '\'' +
                ", mt=" + mt +
                ", c1='" + c1 + '\'' +
                '}';
    }
}
