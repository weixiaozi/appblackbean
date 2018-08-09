package bbs.com.xinfeng.bbswork.domin;

/**
 * Created by dell on 2018/6/7.
 */

public class VideoSelectBus {
    public boolean isFinish;
    public String videoPath;
    public int duration;

    public VideoSelectBus(boolean isFinish, String videoPath) {
        this.isFinish = isFinish;
        this.videoPath = videoPath;
    }

    public VideoSelectBus(boolean isFinish, String videoPath, int duration) {
        this.isFinish = isFinish;
        this.videoPath = videoPath;
        this.duration = duration;
    }
}
