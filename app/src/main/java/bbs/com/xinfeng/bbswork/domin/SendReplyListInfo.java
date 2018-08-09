package bbs.com.xinfeng.bbswork.domin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2018/6/9.
 */

public class SendReplyListInfo {
    private int level;//1:一级回复2：二级回复
    private int type;//1:图片2：语音
    private String content;
    private List<String> thumb = new ArrayList<>();
    private List<String> web = new ArrayList<>();
    private int duration;
    private String replyId;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getReplyId() {
        return replyId;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getThumb() {
        return thumb;
    }

    public void setThumb(List<String> thumb) {
        this.thumb = thumb;
    }

    public List<String> getWeb() {
        return web;
    }

    public void setWeb(List<String> web) {
        this.web = web;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
