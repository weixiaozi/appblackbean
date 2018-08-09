package bbs.com.xinfeng.bbswork.domin;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dell on 2018/3/20.
 */
//获取验证码
public class GetCodeBean extends ErrorBean {
    public static final String URL = "api/user/sms";


    /**
     * code : 191298
     */

    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int codeX) {
        this.code = codeX;
    }
}
