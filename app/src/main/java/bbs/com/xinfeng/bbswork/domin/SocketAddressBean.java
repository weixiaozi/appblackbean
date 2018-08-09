package bbs.com.xinfeng.bbswork.domin;

/**
 * Created by dell on 2016/11/23 17:46.
 */

public class SocketAddressBean extends ErrorBean {
    public static final String URL = "api/tcpconnect";


    /**
     * host : 192.168.80.18
     * port : 31101
     * time : 10
     * retry_count : 5
     * retry_time : 3
     * retry_time_max : 55
     */

    private String host;
    private String port;
    private int time;
    private int retry_count;
    private int retry_time;
    private int retry_time_max;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getRetry_count() {
        return retry_count;
    }

    public void setRetry_count(int retry_count) {
        this.retry_count = retry_count;
    }

    public int getRetry_time() {
        return retry_time;
    }

    public void setRetry_time(int retry_time) {
        this.retry_time = retry_time;
    }

    public int getRetry_time_max() {
        return retry_time_max;
    }

    public void setRetry_time_max(int retry_time_max) {
        this.retry_time_max = retry_time_max;
    }
}

