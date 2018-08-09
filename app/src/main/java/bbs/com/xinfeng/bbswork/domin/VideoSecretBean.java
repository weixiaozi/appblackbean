package bbs.com.xinfeng.bbswork.domin;

/**
 * Created by dell on 2016/11/23 17:46.
 */
//用户信息
public class VideoSecretBean extends ErrorBean {
    public static final String URL = "api/video/s";


    /**
     * s : 8iT04lLQg1GAQl5xbBSM8EIRe1xzZWNyZXRJZD1BS0lEdXpEakRheUNUUVBQU1Q4Tkczb1lOSkRCem1YSk1waVgmY3VycmVudFRpbWVTdGFtcD0xNTI3NjY1Njk1JmV4cGlyZVRpbWU9MTUyNzc1MjA5NSZyYW5kb209MTMxNDY0NzgxMCZzb3VyY2VDb250ZXh0PSU1QjEwMDAwMyU1RA==
     * expire : 1527752095
     * path : b/t/100003
     */

    private String s;
    private long expire;
    private String path;

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

