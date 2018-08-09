package bbs.com.xinfeng.bbswork.domin;

/**
 * Created by dell on 2018/5/31.
 */
//发布的音视频准备信息
public class VideoInfoPackage {
    public int type;
    public String videoPath;
    public String coverPath;
    public int duration;
    public String webUrl;

    public VideoInfoPackage(int type, String videoPath, String coverPath, int duration) {
        this.type = type;
        this.videoPath = videoPath;
        this.coverPath = coverPath;
        this.duration = duration;
    }
}
