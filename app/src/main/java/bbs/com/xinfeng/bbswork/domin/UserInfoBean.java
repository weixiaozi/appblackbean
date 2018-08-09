package bbs.com.xinfeng.bbswork.domin;

/**
 * Created by dell on 2016/11/23 17:46.
 */
//用户信息
public class UserInfoBean extends ErrorBean {
    public static final String URL = "api/user";


    /**
     * id : 40145
     * name :
     * phone : 13720089001
     * portrait :
     * status : 0
     * online : 0
     * created_at : 2018-04-03 10:31:19
     * updated_at : 2018-04-08 09:49:34
     * expired_at : 0000-00-00 00:00:00
     * portrait_thumb :
     * profile : true
     */

    private int id;
    private String name;
    private String phone;
    private String portrait;
    private int status;
    private int online;
    private String created_at;
    private String updated_at;
    private String expired_at;
    private String portrait_thumb;
    private String introduce;
    private boolean profile;

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getExpired_at() {
        return expired_at;
    }

    public void setExpired_at(String expired_at) {
        this.expired_at = expired_at;
    }

    public String getPortrait_thumb() {
        return portrait_thumb;
    }

    public void setPortrait_thumb(String portrait_thumb) {
        this.portrait_thumb = portrait_thumb;
    }

    public boolean isProfile() {
        return profile;
    }

    public void setProfile(boolean profile) {
        this.profile = profile;
    }
}

