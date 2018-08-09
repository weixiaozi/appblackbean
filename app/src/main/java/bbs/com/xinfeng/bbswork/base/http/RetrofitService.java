package bbs.com.xinfeng.bbswork.base.http;

import java.util.Map;

import bbs.com.xinfeng.bbswork.domin.BlackActionBean;
import bbs.com.xinfeng.bbswork.domin.ChatTopicMembersBean;
import bbs.com.xinfeng.bbswork.domin.ClickMeListBean;
import bbs.com.xinfeng.bbswork.domin.CollectOperateBean;
import bbs.com.xinfeng.bbswork.domin.CollectionListBean;
import bbs.com.xinfeng.bbswork.domin.ContactListBean;
import bbs.com.xinfeng.bbswork.domin.FeedbackBean;
import bbs.com.xinfeng.bbswork.domin.FollowActionBean;
import bbs.com.xinfeng.bbswork.domin.GetCodeBean;
import bbs.com.xinfeng.bbswork.domin.GetPushSettingBean;
import bbs.com.xinfeng.bbswork.domin.HaveReadBean;
import bbs.com.xinfeng.bbswork.domin.InviteInAppBean;
import bbs.com.xinfeng.bbswork.domin.LikeActionBean;
import bbs.com.xinfeng.bbswork.domin.MemberAllBean;
import bbs.com.xinfeng.bbswork.domin.ModifyUserinfoBean;
import bbs.com.xinfeng.bbswork.domin.MyBlackListBean;
import bbs.com.xinfeng.bbswork.domin.MyFansListBean;
import bbs.com.xinfeng.bbswork.domin.MyReplyListBean;
import bbs.com.xinfeng.bbswork.domin.MyThemeListBean;
import bbs.com.xinfeng.bbswork.domin.NotificationBean;
import bbs.com.xinfeng.bbswork.domin.OperateTopicNotificationBean;
import bbs.com.xinfeng.bbswork.domin.PrivateUserDetailBean;
import bbs.com.xinfeng.bbswork.domin.PublishReplyBean;
import bbs.com.xinfeng.bbswork.domin.PublishThemeBean;
import bbs.com.xinfeng.bbswork.domin.RemoveUserBean;
import bbs.com.xinfeng.bbswork.domin.ReplyMeListBean;
import bbs.com.xinfeng.bbswork.domin.ReportToWebBean;
import bbs.com.xinfeng.bbswork.domin.SetMesStatusBean;
import bbs.com.xinfeng.bbswork.domin.SetPushIdBean;
import bbs.com.xinfeng.bbswork.domin.SetPushSettingBean;
import bbs.com.xinfeng.bbswork.domin.SystemNoticesBean;
import bbs.com.xinfeng.bbswork.domin.ThemeDetailReplyBean;
import bbs.com.xinfeng.bbswork.domin.ThemeListBean;
import bbs.com.xinfeng.bbswork.domin.TopicApplyBean;
import bbs.com.xinfeng.bbswork.domin.TopicChangeBean;
import bbs.com.xinfeng.bbswork.domin.TopicCreatBean;
import bbs.com.xinfeng.bbswork.domin.JoinTopicBean;
import bbs.com.xinfeng.bbswork.domin.NoticeChangeBean;
import bbs.com.xinfeng.bbswork.domin.OutTopicBean;
import bbs.com.xinfeng.bbswork.domin.RegisterBean;
import bbs.com.xinfeng.bbswork.domin.SocketAddressBean;
import bbs.com.xinfeng.bbswork.domin.TokenBean;
import bbs.com.xinfeng.bbswork.domin.TopicDetailBean;
import bbs.com.xinfeng.bbswork.domin.TopicListBean;
import bbs.com.xinfeng.bbswork.domin.TopicUserListBean;
import bbs.com.xinfeng.bbswork.domin.TopicUsersBean;
import bbs.com.xinfeng.bbswork.domin.UpgradeBean;
import bbs.com.xinfeng.bbswork.domin.UserContentBean;
import bbs.com.xinfeng.bbswork.domin.UserDetailBean;
import bbs.com.xinfeng.bbswork.domin.UserInfoBean;
import bbs.com.xinfeng.bbswork.domin.VideoSecretBean;
import io.reactivex.Flowable;
import bbs.com.xinfeng.bbswork.domin.BaseBean;
import bbs.com.xinfeng.bbswork.domin.CollectionBean;
import bbs.com.xinfeng.bbswork.domin.UploadBean;
import bbs.com.xinfeng.bbswork.domin.TestBean;
import okhttp3.RequestBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

/**
 * Created by dell on 2017/10/18.
 */

public interface RetrofitService {

    @POST(TestBean.URL)
    @FormUrlEncoded
    Flowable<BaseBean<TestBean>> getClassifyInfo(@FieldMap Map<String, String> params);

    @POST(UpgradeBean.URL)
    @FormUrlEncoded
    Flowable<BaseBean<UpgradeBean>> getUpGradeInfo(@FieldMap Map<String, String> params);

    @POST(CollectionBean.URL)
    @FormUrlEncoded
    Flowable<BaseBean<CollectionBean>> getCollectionInfo(@FieldMap Map<String, String> params);

    @POST(UploadBean.URL)
    @Multipart
    Flowable<BaseBean<UploadBean>> uploadHeadPic(@PartMap Map<String, RequestBody> params);

    @POST(SetPushIdBean.URL)
    @FormUrlEncoded
    Flowable<BaseBean<SetPushIdBean>> setPushid(@FieldMap Map<String, String> params);

    @POST(TokenBean.URL)
    @FormUrlEncoded
    Flowable<BaseBean<TokenBean>> getToken(@FieldMap Map<String, String> params);

    @POST(SocketAddressBean.URL)
    @FormUrlEncoded
    Flowable<BaseBean<SocketAddressBean>> getSocketAddress(@FieldMap Map<String, String> params);

    @POST(TopicCreatBean.URL)
    @FormUrlEncoded
    Flowable<BaseBean<TopicCreatBean>> creatTopic(@FieldMap Map<String, String> params);

    @POST(TopicChangeBean.URL)
    @FormUrlEncoded
    Flowable<BaseBean<TopicChangeBean>> changeTopic(@FieldMap Map<String, String> params);

    @POST(TopicUserListBean.URL)
    @FormUrlEncoded
    Flowable<BaseBean<TopicUserListBean>> getTopicUserlist(@FieldMap Map<String, String> params);

    @POST(TopicListBean.URL)
    @FormUrlEncoded
    Flowable<BaseBean<TopicListBean>> getTopiclist(@FieldMap Map<String, String> params);

    @POST(ContactListBean.URL)
    @FormUrlEncoded
    Flowable<BaseBean<ContactListBean>> getContactlist(@FieldMap Map<String, String> params);

    @POST(UserDetailBean.URL)
    @FormUrlEncoded
    Flowable<BaseBean<UserDetailBean>> getUserInfoList(@FieldMap Map<String, String> params);

    @POST(PrivateUserDetailBean.URL)
    @FormUrlEncoded
    Flowable<BaseBean<PrivateUserDetailBean>> getPrivateAttachInfo(@FieldMap Map<String, String> params);

    @POST(NoticeChangeBean.URL)
    @FormUrlEncoded
    Flowable<BaseBean<NoticeChangeBean>> getNoticeChange(@FieldMap Map<String, String> params);

    @POST(TopicUsersBean.URL)
    @FormUrlEncoded
    Flowable<BaseBean<TopicUsersBean>> getTopicUsers(@FieldMap Map<String, String> params);

    @POST(TopicDetailBean.URL)
    @FormUrlEncoded
    Flowable<BaseBean<TopicDetailBean>> topicDetails(@FieldMap Map<String, String> params);

    @POST(JoinTopicBean.URL)
    @FormUrlEncoded
    Flowable<BaseBean<JoinTopicBean>> joinTopicUsers(@FieldMap Map<String, String> params);

    @POST(InviteInAppBean.URL)
    @FormUrlEncoded
    Flowable<BaseBean<InviteInAppBean>> inviteUsers(@FieldMap Map<String, String> params);

    @POST(RemoveUserBean.URL)
    @FormUrlEncoded
    Flowable<BaseBean<RemoveUserBean>> removeUsers(@FieldMap Map<String, String> params);

    @POST(OutTopicBean.URL)
    @FormUrlEncoded
    Flowable<BaseBean<OutTopicBean>> outTopicUsers(@FieldMap Map<String, String> params);

    @POST(TopicApplyBean.URL)
    @FormUrlEncoded
    Flowable<BaseBean<TopicApplyBean>> applyInTopic(@FieldMap Map<String, String> params);

    @POST(GetCodeBean.URL)
    @FormUrlEncoded
    Flowable<BaseBean<GetCodeBean>> getCode(@FieldMap Map<String, String> params);

    @POST(RegisterBean.URL)
    @FormUrlEncoded
    Flowable<BaseBean<RegisterBean>> registerUser(@FieldMap Map<String, String> params);

    @POST(ModifyUserinfoBean.URL)
    @FormUrlEncoded
    Flowable<BaseBean<ModifyUserinfoBean>> modifyUserinfo(@FieldMap Map<String, String> params);

    @POST(UserInfoBean.URL)
    @FormUrlEncoded
    Flowable<BaseBean<UserInfoBean>> getUserinfo(@FieldMap Map<String, String> params);

    @POST(VideoSecretBean.URL)
    @FormUrlEncoded
    Flowable<BaseBean<VideoSecretBean>> getVideoSecret(@FieldMap Map<String, String> params);

    @POST(UserContentBean.URL)
    @FormUrlEncoded
    Flowable<BaseBean<UserContentBean>> getUserContent(@FieldMap Map<String, String> params);

    @POST(ThemeListBean.URL)
    @FormUrlEncoded
    Flowable<BaseBean<ThemeListBean>> getThemeList(@FieldMap Map<String, String> params);

    @POST(PublishThemeBean.URL)
    @FormUrlEncoded
    Flowable<BaseBean<PublishThemeBean>> publishTheme(@FieldMap Map<String, String> params);

    @POST(PublishThemeBean.URL1)
    @FormUrlEncoded
    Flowable<BaseBean<PublishThemeBean>> deleteTheme(@FieldMap Map<String, String> params);

    @POST(NotificationBean.URL)
    @FormUrlEncoded
    Flowable<BaseBean<NotificationBean>> getNotifications(@FieldMap Map<String, String> params);

    @POST(OperateTopicNotificationBean.URL)
    @FormUrlEncoded
    Flowable<BaseBean<OperateTopicNotificationBean>> operateNotice(@FieldMap Map<String, String> params);

    @POST(OperateTopicNotificationBean.URL1)
    @FormUrlEncoded
    Flowable<BaseBean<OperateTopicNotificationBean>> clearNotice(@FieldMap Map<String, String> params);

    @POST(OperateTopicNotificationBean.URL2)
    @FormUrlEncoded
    Flowable<BaseBean<OperateTopicNotificationBean>> inviteOperateNotice(@FieldMap Map<String, String> params);

    @POST(MemberAllBean.URL)
    @FormUrlEncoded
    Flowable<BaseBean<MemberAllBean>> getAllMemberAboutUser(@FieldMap Map<String, String> params);

    @POST(MemberAllBean.URL1)
    @FormUrlEncoded
    Flowable<BaseBean<MemberAllBean>> getTopicMembers(@FieldMap Map<String, String> params);

    @POST(SystemNoticesBean.URL)
    @FormUrlEncoded
    Flowable<BaseBean<SystemNoticesBean>> getSystemNotices(@FieldMap Map<String, String> params);

    @POST(GetPushSettingBean.URL)
    @FormUrlEncoded
    Flowable<BaseBean<GetPushSettingBean>> getPushSetting(@FieldMap Map<String, String> params);

    @POST(SetPushSettingBean.URL)
    @FormUrlEncoded
    Flowable<BaseBean<SetPushSettingBean>> setPushSetting(@FieldMap Map<String, String> params);

    @POST(FeedbackBean.URL)
    @FormUrlEncoded
    Flowable<BaseBean<FeedbackBean>> pushFeedback(@FieldMap Map<String, String> params);

    @POST(FeedbackBean.URL1)
    @FormUrlEncoded
    Flowable<BaseBean<FeedbackBean>> outLogin(@FieldMap Map<String, String> params);

    @POST(CollectionListBean.URL)
    @FormUrlEncoded
    Flowable<BaseBean<CollectionListBean>> getCollectionList(@FieldMap Map<String, String> params);

    @POST(CollectOperateBean.URL)
    @FormUrlEncoded
    Flowable<BaseBean<CollectOperateBean>> collectOperate(@FieldMap Map<String, String> params);

    @POST(MyThemeListBean.URL)
    @FormUrlEncoded
    Flowable<BaseBean<MyThemeListBean>> getMyThemeList(@FieldMap Map<String, String> params);

    @POST(ThemeDetailReplyBean.URL)
    @FormUrlEncoded
    Flowable<BaseBean<ThemeDetailReplyBean>> getThemeReplyList(@FieldMap Map<String, String> params);

    @POST(PublishReplyBean.URL)
    @FormUrlEncoded
    Flowable<BaseBean<PublishReplyBean>> publishReply(@FieldMap Map<String, String> params);

    @POST(PublishReplyBean.URL1)
    @FormUrlEncoded
    Flowable<BaseBean<PublishReplyBean>> deleteReply(@FieldMap Map<String, String> params);

    @POST(MyFansListBean.URL)
    @FormUrlEncoded
    Flowable<BaseBean<MyFansListBean>> getFansList(@FieldMap Map<String, String> params);

    @POST(MyBlackListBean.URL)
    @FormUrlEncoded
    Flowable<BaseBean<MyBlackListBean>> getBlackList(@FieldMap Map<String, String> params);

    @POST(MyReplyListBean.URL)
    @FormUrlEncoded
    Flowable<BaseBean<MyReplyListBean>> getMyreplyList(@FieldMap Map<String, String> params);

    @POST(ReplyMeListBean.URL)
    @FormUrlEncoded
    Flowable<BaseBean<ReplyMeListBean>> getReplyMeList(@FieldMap Map<String, String> params);

    @POST(ClickMeListBean.URL)
    @FormUrlEncoded
    Flowable<BaseBean<ClickMeListBean>> getClickMeList(@FieldMap Map<String, String> params);

    @POST(FollowActionBean.URL)
    @FormUrlEncoded
    Flowable<BaseBean<FollowActionBean>> followAction(@FieldMap Map<String, String> params);

    @POST(BlackActionBean.URL)
    @FormUrlEncoded
    Flowable<BaseBean<BlackActionBean>> blackAction(@FieldMap Map<String, String> params);

    @POST(HaveReadBean.URL)
    @FormUrlEncoded
    Flowable<BaseBean<HaveReadBean>> haveRead(@FieldMap Map<String, String> params);

    @POST(HaveReadBean.URL1)
    @FormUrlEncoded
    Flowable<BaseBean<HaveReadBean>> haveReadAudio(@FieldMap Map<String, String> params);

    @POST(LikeActionBean.URL)
    @FormUrlEncoded
    Flowable<BaseBean<LikeActionBean>> likeAction(@FieldMap Map<String, String> params);

    @POST(ChatTopicMembersBean.URL)
    @FormUrlEncoded
    Flowable<BaseBean<ChatTopicMembersBean>> chatTopicMember(@FieldMap Map<String, String> params);

    @POST(ReportToWebBean.URL)
    @FormUrlEncoded
    Flowable<BaseBean<ReportToWebBean>> reprotToWeb(@FieldMap Map<String, String> params);

    @POST(SetMesStatusBean.URL)
    @FormUrlEncoded
    Flowable<BaseBean<SetMesStatusBean>> setMesListStatus(@FieldMap Map<String, String> params);
}
