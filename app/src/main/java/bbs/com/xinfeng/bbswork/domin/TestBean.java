package bbs.com.xinfeng.bbswork.domin;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dell on 2017/10/18.
 */

public class TestBean extends ErrorBean implements Serializable {
    private static final long serialVersionUID = 7247714666080613254L;
    public static final String URL = "api/app/a.php";


    /**
     * c : 0
     * channelNameList : []
     * live : {"type":"2","wlist":["3700","1895","1915","2240","2305","2250","2235","1910","1930","13795","14570","11625","11645","11655","11835","11955","11970","12080","12140","12170","12375","16090","16305","16315","16475","17230","17425","17530","17625","17745","17760","17900","17915","17930","18110","18115","18210","18555","18750","19115","19495","25805","48625","48825","48180","53945","48150","48695","40705","48670","48605","48025","51340","53685","56510","3635","56760","49070","68112","58525","7975","48680","72751","68888"]}
     */

    private String c;
    private LiveBean live;
    private List<String> channelNameList;

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public LiveBean getLive() {
        return live;
    }

    public void setLive(LiveBean live) {
        this.live = live;
    }

    public List<?> getChannelNameList() {
        return channelNameList;
    }

    public void setChannelNameList(List<String> channelNameList) {
        this.channelNameList = channelNameList;
    }

    public static class LiveBean {
        /**
         * type : 2
         * wlist : ["3700","1895","1915","2240","2305","2250","2235","1910","1930","13795","14570","11625","11645","11655","11835","11955","11970","12080","12140","12170","12375","16090","16305","16315","16475","17230","17425","17530","17625","17745","17760","17900","17915","17930","18110","18115","18210","18555","18750","19115","19495","25805","48625","48825","48180","53945","48150","48695","40705","48670","48605","48025","51340","53685","56510","3635","56760","49070","68112","58525","7975","48680","72751","68888"]
         */

        private String type;
        private List<String> wlist;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<String> getWlist() {
            return wlist;
        }

        public void setWlist(List<String> wlist) {
            this.wlist = wlist;
        }
    }
}
