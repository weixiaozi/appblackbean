package bbs.com.xinfeng.bbswork.utils.shareUtil;

import java.util.ArrayList;

/**
 * Created by dell on 2018/1/10.
 */

public class ShareData {
    public static final int QQ = 1;
    public static final int QQ_ZONE = 2;
    public static final int WX = 3;
    public static final int WX_CIRCLE = 4;
    public static final int SINA = 5;
    public static final int APPLICATION = 6;//应用内

    private int channel;
    private String title;
    private String summary;
    private String targtUrl;
    private String picUrl;
    private ArrayList<String> picUrls;//多张图片分享（qqzone）


    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTargtUrl() {
        return targtUrl;
    }

    public void setTargtUrl(String targtUrl) {
        this.targtUrl = targtUrl;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public ArrayList<String> getPicUrls() {
        return picUrls;
    }

    public void setPicUrls(ArrayList<String> picUrls) {
        this.picUrls = picUrls;
    }
}
