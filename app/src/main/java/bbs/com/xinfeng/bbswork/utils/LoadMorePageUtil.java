package bbs.com.xinfeng.bbswork.utils;

/**
 * Created by dell on 2017/11/24.
 */

public class LoadMorePageUtil {
    public static final int NO_DATA = -1;//刷新没有数据
    public static final int NO_MORE_DATA = -2;//加载更多没有数据
    public static final int SIZE = 8;//默认请求数量

    private int pageTotal;
    private int singleSize = SIZE;
    private int currentPage = 1;

    public LoadMorePageUtil(int singleSize, int pageTotal) {
        this.singleSize = singleSize;
        this.pageTotal = pageTotal;
    }

    public LoadMorePageUtil(int singleSize) {
        this.singleSize = singleSize;
    }

    public LoadMorePageUtil() {
    }

    public String getRefreshPage() {
        currentPage = 1;
        return currentPage + "";
    }

    public int getLoadMorePage() {
        if (pageTotal <= 0)
            return NO_DATA;
        if (currentPage < pageTotal)
            return ++currentPage;
        return NO_MORE_DATA;
    }

    public String getSingleSize() {
        return singleSize + "";
    }

    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setSingleSize(int singleSize) {
        this.singleSize = singleSize;
    }
}
