package bbs.com.xinfeng.bbswork.domin;

/**
 * Created by dell on 2018/3/20.
 */
//socket向服务器发送是否成功
public class SendToServerIsSuccessBean extends BaseSocketBean {
    private String key;
    private int status;//status:0:写入失败；1;写入成功；2：连接超时
    private int a;//socket发送请求类型
    private int chatId;

    public SendToServerIsSuccessBean(String key, int status, int a, int chatId) {
        this.key = key;
        this.status = status;
        this.a = a;
        this.chatId = chatId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }
}
