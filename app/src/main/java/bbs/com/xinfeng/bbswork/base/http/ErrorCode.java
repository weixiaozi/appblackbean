package bbs.com.xinfeng.bbswork.base.http;

/**
 * Created by dell on 2017/10/20.
 */

public class ErrorCode {
    /**
     * 错误码
     */
    //网络访问异常
    public static String ERROR_CODE_NETWORK = "-8000";
    public static String ERROR_DESC_NETWORK = "网络访问出错";

    //下载过程中出现IO异常
    public static String ERROR_CODE_DOWNLOAD_IO = "-8001";
    public static String ERROR_DESC_DOWNLOAD_IO = "下载中网络异常";
    public static String ERROR_CODE_DOWNLOAD_ILLEGAL = "-8002";
    public static String ERROR_DESC_DOWNLOAD_ILLEGAL = "url不正确";
}
