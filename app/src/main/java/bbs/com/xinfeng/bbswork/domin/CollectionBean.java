package bbs.com.xinfeng.bbswork.domin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 */

public class CollectionBean extends ErrorBean implements Serializable {
    public static final String URL = "api/user/info/getCollectedVideoList.php";
    public static final String METHOD = "getCollectionInfo";
    private String total = "0";
    private String page = "1";
    private String pageTotal = "0";

    private List<ListEntity> list = new ArrayList<>();


    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(String pageTotal) {
        this.pageTotal = pageTotal;
    }

    public List<ListEntity> getList() {
        return list;
    }

    public void setList(List<ListEntity> list) {
        this.list = list;
    }

    public static class ListEntity {

        @Override
        public String toString() {
            return "ListEntity{" +
                    "videoID='" + videoID + '\'' +
                    ", gameName='" + gameName + '\'' +
                    ", viewCount='" + viewCount + '\'' +
                    ", uid='" + uid + '\'' +
                    ", nick='" + nick + '\'' +
                    ", title='" + title + '\'' +
                    ", orientation='" + orientation + '\'' +
                    ", poster='" + poster + '\'' +
                    ", ispic='" + ispic + '\'' +
                    ", collectCount='" + collectCount + '\'' +
                    ", videoUrl='" + videoUrl + '\'' +
                    ", commentCount='" + commentCount + '\'' +
                    ", upCount='" + upCount + '\'' +
                    ", isChecked=" + isChecked +
                    '}';
        }

        private String videoID = "";
        private String gameName = "";
        private String viewCount = "0";
        private String uid = "";
        private String nick = "";
        private String title = "";
        private String orientation = "";
        private String poster = "";
        private String ispic = "";
        private String collectCount = "0";
        private String videoUrl = "";
        private String commentCount = "0";
        private String upCount = "0";
        private boolean isChecked;

        public String getUpCount() {
            return upCount;
        }

        public void setUpCount(String upCount) {
            this.upCount = upCount;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public String getVideoID() {
            return videoID;
        }

        public void setVideoID(String videoID) {
            this.videoID = videoID;
        }

        public String getGameName() {
            return gameName;
        }

        public void setGameName(String gameName) {
            this.gameName = gameName;
        }

        public String getViewCount() {
            return viewCount;
        }

        public void setViewCount(String viewCount) {
            this.viewCount = viewCount;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getNick() {
            return nick;
        }

        public void setNick(String nick) {
            this.nick = nick;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getOrientation() {
            return orientation;
        }

        public void setOrientation(String orientation) {
            this.orientation = orientation;
        }

        public String getPoster() {
            return poster;
        }

        public void setPoster(String poster) {
            this.poster = poster;
        }

        public String getIspic() {
            return ispic;
        }

        public void setIspic(String ispic) {
            this.ispic = ispic;
        }

        public String getCollectCount() {
            return collectCount;
        }

        public void setCollectCount(String collectCount) {
            this.collectCount = collectCount;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        public String getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(String commentCount) {
            this.commentCount = commentCount;
        }
    }

    @Override
    public String toString() {
        return "CollectionBean{" +
                "total='" + total + '\'' +
                ", page='" + page + '\'' +
                ", pageTotal='" + pageTotal + '\'' +
                ", list=" + list +
                '}';
    }
}
