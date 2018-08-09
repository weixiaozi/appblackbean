package bbs.com.xinfeng.bbswork.domin;

import java.io.Serializable;

/**
 * Created by dell on 2017/10/19.
 */

public class BaseBean<T extends ErrorBean> implements Serializable {
    /*100000=>'操作成功',
      100001=>'操作失败',
      100002=>'参数验证失败',
      100003=>'未知错误',
      100004=>'没有访问权限',
      100005=>'刷新令牌失效',*/
    public static final int SUCCESS_CODE = 100000;
    public static final int REFRESHTOKEN_EXPIRED_CODE = 100005;
    public static final int TOKEN_EXPIRED_CODE = 100004;
    private int code;
    private String message;
    private T data;
    private String notice;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    @Override
    public String toString() {
        return "BaseBean{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
