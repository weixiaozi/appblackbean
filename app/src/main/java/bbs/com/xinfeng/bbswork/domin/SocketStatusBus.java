package bbs.com.xinfeng.bbswork.domin;

/**
 * Created by dell on 2018/6/22.
 */

public class SocketStatusBus {
    public int status;////-1:socket服务没有启动；0：服务启动；1：建立连接2：认证成功3：连接断开4:服务器主动断开连接

    public SocketStatusBus(int status) {
        this.status = status;
    }
}
