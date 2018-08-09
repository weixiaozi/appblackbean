package bbs.com.xinfeng.bbswork.base;

import android.os.Environment;

/**
 * Created by dell on 2017/10/18.
 */

public class Constant {
    public static final String STORAGE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/blackbean";
    public static final String GAME_PATH = STORAGE_PATH + "/gamepic";
    //    public static final String BASEURL_OFFICE = "http://bbs.prev.6rooms.net/";
    public static final String BASEURL_OFFICE = "https://bbs.xiu123.cn/";
    public static final String BASEURL_TEST = "http://bbs.proxy.6rooms.net/";
    public static String BASEURL = "http://bbs.proxy.6rooms.net/";
    public static final String baseurl_key = "baseurl";
    public static final String AUTHORITY = "bbs.com.xinfeng.bbs.fileprovider";
    public static String hpUuid;//设备uid

    public static final String client_id = "3";
    public static final String client_secret = "sqCeFO68xR7iAukaIpqx9vQCDI9ITMVyDK5j6vdT";

    public static final String isLogin = "islogin";
    public static final String isPush = "isPush";
    public static final String token_key = "token";
    public static final String refreshtoken_key = "refreshtoken";
    public static final String token_timeout_key = "tokentimeout";
    public static final String profile_key = "profile";
    public static final String userid_key = "userid";
    public static final String name_key = "username";
    public static final String head_key = "userhead";
    public static final String phone_key = "userphone";
    public static final String current_key = "notifycurrent";
    public static final String mes_current_key = "mescurrent";
    public static final String lastVersion_key = "lastversion";
    public static final String collect_current_key = "collectcurrent";
    public static String token;
    public static String user_agent;


    public static final String firstchat_key = "firstchat";
    //videosecret
    public static String videoSecret;

    public static final int VideoMaxDuration = 15;

    public static final String ugcLicenceUrl = "http://license.vod2.myqcloud.com/license/v1/142f564982ea4b7bf5b6bdd5ee5de6cd/TXUgcSDK.licence";
    public static final String ugcKey = "71ca59802c5973b41fc6bc7c7e7de0f8";

}
