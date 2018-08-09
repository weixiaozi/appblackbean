package bbs.com.xinfeng.bbswork.utils;

import android.os.Build;

import bbs.com.xinfeng.bbswork.base.App;

import static bbs.com.xinfeng.bbswork.utils.SystemUtil.getAppVersion;

/**
 * Created by dell on 2018/3/7.
 */

public class SocketUtil {
    //认证
    public static String formatAuthMessage(String token, String macid) {
        return "{\"a\":\"1\",\"authorization\":\"" + token + "\",\"macid\":\"" + macid + "\",\"os\":\"" + SystemUtil.getSystemVersion() + "\",\"phone\":\"" + Build.MODEL + "\",\"app\":\"" + getAppVersion() + "\",\"token\":\"" + ArmsUtils.encodeToMD5(SystemUtil.getMAC(App.getInstance())) + "\",\"terminal\":4}";
    }

    //添加会话
    public static String addChat(int userId, String time) {
        return "{\"a\":10,\"targetid\":" + userId + ",\"type\":" + 1 + ",\"s\":\"" + time + "\"}";
    }

    //发送消息
    public static String sendChat(int chatId, int type, String content, long time) {
        return "{\"a\":4,\"si\":" + chatId + ",\"mt\":" + type + ",\"co\":\"" + content + "\",\"s\":\"" + time + "\"}";
    }

    //发送消息(重发)
    public static String sendChat(int chatId, int type, String content, long time, int st) {
        return "{\"a\":4,\"si\":" + chatId + ",\"mt\":" + type + ",\"co\":\"" + content + "\",\"st\":" + st + ",\"s\":\"" + time + "\"}";
    }

    //拉取消息
    public static String getChatList(int chatId, String ci, String type, long s) {
        return "{\"a\":9,\"si\":" + chatId + ",\"ci\":\"" + ci + "\",\"type\":\"" + type + "\",\"s\":\"" + s + "\"}";
    }

    //同步消息
    public static String synchronizeMesList(String gi, String ci, long s) {
        return "{\"a\":8,\"ci\":\"" + ci + "\",\"gi\":\"" + gi + "\",\"s\":\"" + s + "\"}";
    }

    //a=11撤销；a=12删除；a=13语音已播
    public static String operateMes(int a, int si, String ci, String s) {
        return "{\"a\":" + a + ",\"si\":\"" + si + "\",\"ci\":\"" + ci + "\",\"s\":\"" + s + "\"}";
    }

}
