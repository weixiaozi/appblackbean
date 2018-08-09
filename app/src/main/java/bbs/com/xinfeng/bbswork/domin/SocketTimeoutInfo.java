package bbs.com.xinfeng.bbswork.domin;

/**
 * Created by dell on 2018/7/18.
 */

public class SocketTimeoutInfo {
    public int a;
    public Long happenTime;
    public int chatId;

    public SocketTimeoutInfo(int a, Long happenTime) {
        this.a = a;
        this.happenTime = happenTime;
    }

    public SocketTimeoutInfo(int a, Long happenTime, int chatId) {
        this.a = a;
        this.happenTime = happenTime;
        this.chatId = chatId;
    }
}
