package bbs.com.xinfeng.bbswork.domin;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dell on 2018/3/20.
 */
//处理话题通知
public class OperateTopicNotificationBean extends ErrorBean {
    public static final String URL = "api/topic/verify";//同意或拒绝申请加入话题
    public static final String URL1 = "api/notice/actiondel";//清空通知
    public static final String URL2 = "api/topic/inviteconfirm";//邀请话题 同意或拒绝

}
