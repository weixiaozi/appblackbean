package bbs.com.xinfeng.bbswork.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.base.App;
import bbs.com.xinfeng.bbswork.base.BaseActivity;
import bbs.com.xinfeng.bbswork.base.Constant;
import bbs.com.xinfeng.bbswork.databinding.ActivityChattingBinding;
import bbs.com.xinfeng.bbswork.domin.AttachThemeBean;
import bbs.com.xinfeng.bbswork.domin.BackTopicBus;
import bbs.com.xinfeng.bbswork.domin.ChatTopicMembersBean;
import bbs.com.xinfeng.bbswork.domin.CollectionThemeBus;
import bbs.com.xinfeng.bbswork.domin.ErrorBean;
import bbs.com.xinfeng.bbswork.domin.InnerThemeListBean;
import bbs.com.xinfeng.bbswork.domin.InnerThemeListBean_;
import bbs.com.xinfeng.bbswork.domin.MemberAllBean;
import bbs.com.xinfeng.bbswork.domin.NoticeChangeBean;
import bbs.com.xinfeng.bbswork.domin.OutTopicBus;
import bbs.com.xinfeng.bbswork.domin.ThemeListBean;
import bbs.com.xinfeng.bbswork.domin.UploadBean;
import bbs.com.xinfeng.bbswork.domin.VideoInfoPackage;
import bbs.com.xinfeng.bbswork.domin.VideoSelectBus;
import bbs.com.xinfeng.bbswork.module.GlideApp;
import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.presenter.ChattingPresenter;
import bbs.com.xinfeng.bbswork.ui.adapter.ChatSelectPicAdapter;
import bbs.com.xinfeng.bbswork.ui.adapter.ChatTopicMembersAdapter;
import bbs.com.xinfeng.bbswork.ui.adapter.ChattingAdapter;
import bbs.com.xinfeng.bbswork.ui.adapter.layoutmanager.ChatPicsTouchHelperCallback;
import bbs.com.xinfeng.bbswork.ui.fragment.HomeFragment;
import bbs.com.xinfeng.bbswork.utils.FormatUtils;
import bbs.com.xinfeng.bbswork.utils.ImageUtil;
import bbs.com.xinfeng.bbswork.utils.LogUtil;
import bbs.com.xinfeng.bbswork.utils.RxUtil;
import bbs.com.xinfeng.bbswork.utils.SharedPrefUtil;
import bbs.com.xinfeng.bbswork.utils.ToastUtil;
import bbs.com.xinfeng.bbswork.utils.emoji.EmojiUtil;
import bbs.com.xinfeng.bbswork.utils.shortvideo.choose.TCVideoChooseActivity;
import bbs.com.xinfeng.bbswork.utils.shortvideo.videoplay.NewVodPlayerActivity;
import bbs.com.xinfeng.bbswork.videoupload.TXUGCPublish;
import bbs.com.xinfeng.bbswork.videoupload.TXUGCPublishTypeDef;
import bbs.com.xinfeng.bbswork.widget.ChattingRootView;
import bbs.com.xinfeng.bbswork.widget.popwindow.ChatOperatePoppubWindow;
import bbs.com.xinfeng.bbswork.widget.popwindow.ChatRepublishPoppubWindow;
import bbs.com.xinfeng.bbswork.widget.recordvoice.RecordVoiceButton;
import io.objectbox.Box;
import io.objectbox.query.Query;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
//import linwg.ImageBrowser;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

import static bbs.com.xinfeng.bbswork.base.Constant.head_key;
import static bbs.com.xinfeng.bbswork.base.Constant.name_key;
import static bbs.com.xinfeng.bbswork.base.Constant.userid_key;
import static bbs.com.xinfeng.bbswork.domin.InnerThemeListBean.LOADING;

public class ChattingActivity extends BaseActivity<ActivityChattingBinding, ChattingPresenter> implements NetContract.INetView, View.OnClickListener, TXUGCPublishTypeDef.ITXVideoPublishListener {
    private final int boxMaxNum = 20;
    private final int uploadpicone_TAG = 500;
    private final int uploadpicone_realSend_TAG = 501;
    private final int themeList_TAG = 310;
    private final int themeListmore_TAG = 311;
    private final int themeAdd_TAG = 312;
    private final int publishTheme_txt_TAG = 313;
    private final int Like_TAG = 314;
    private final int Members_TAG = 315;
    private final int Collection_TAG = 316;
    private final int ThemeNotify_TAG = 317;
    private final int ReadTheme_TAG = 318;
    private final int publishTheme_pic_TAG = 319;
    private final int publishTheme_audio_TAG = 320;
    private final int themeList_HOT_TAG = 321;
    private int AT_request = 10;
    public static final String addButton = "addbutton";

    private ArrayList<String> selectPics = new ArrayList<>();
    private ArrayList<String> selectVideos = new ArrayList<>();
    private int videoDuration;
    private ArrayList<String> backupSelectPics = new ArrayList<>();//备份，防止选择图片时，减掉图片后返回图片减少
    private HashMap<String, String> picMap = new HashMap();
    private HashMap<String, VideoInfoPackage> audioMap = new HashMap();
    private ChatSelectPicAdapter picAdapter;
    private Handler mHandler = new Handler();
    private String topicId;

    private ChattingAdapter chattingAdapter;
    private int themeid;
    private ChatTopicMembersBean membersBean;

    private List<InnerThemeListBean> mData = Collections.synchronizedList(new ArrayList<>());
    private List<InnerThemeListBean> resendDatas = new ArrayList<>();

    private Box<InnerThemeListBean> box;
    private Query<InnerThemeListBean> queryBuild;

    private ChatTopicMembersAdapter membersAdapter;
    private ChatOperatePoppubWindow chatOperatePoppubWindow;
    //    private InnerThemeListBean readyInfo;
    private int sendInPostion = -1;//当前发送的是消息是哪条
    private LinearLayoutManager linearLayoutManager;
    private int unreadDown;
    private int unreadUp;
    private int selectPosition = -1;
    private int authorId;

    private int last_thread_id;//用于查看未读观点
    private boolean isGetUnreadUpNum;
    private boolean isFirstloadData;
    private int firstLoadSize;

    public static boolean isChatting;

    private boolean isOtherClose;//当需要重新启动该页面的关闭操作，不将Homefragment.topicid置0
    private ChatRepublishPoppubWindow republishPop;

    private TXUGCPublish mVideoPublish = null;

    private int label;
    private int lastThemeId;

    @Override
    protected int initView(Bundle savedInstanceState) {
        return R.layout.activity_chatting;
    }

    @Override
    protected ChattingPresenter creatPresenter() {
        return new ChattingPresenter(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        isChatting = true;
    }

    @Override
    protected void onStop() {
        chattingAdapter.stopMediaPlayer();
        super.onStop();
        isChatting = false;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        topicId = HomeFragment.resumeTopicid + "";
        mBinding.barTxtTitle.setText(getIntent().getStringExtra("topoicname"));
        authorId = SharedPrefUtil.getInt(Constant.userid_key, 0);
        GlideApp.with(provideActivity()).load(getIntent().getStringExtra("topoicpic")).override(60).placeholder(R.drawable.icon_topic_default).error(R.drawable.icon_topic_default).into(mBinding.barRightPic);
        initBox();
        initAdapterChats();
        initAdapterPics();
        initRecord();
        refresh();

        mPresenter.getTopicMembers(topicId, 1, last_thread_id, Members_TAG);
        EmojiUtil emojiUtil = new EmojiUtil();
        emojiUtil.setSelfDrawable(getApplicationContext(), mBinding.vpEmoji, mBinding.llDot, mBinding.editContent, 0);
        if (SharedPrefUtil.getBoolean(Constant.firstchat_key, true)) {
            mBinding.ivChatFirstSplash.setVisibility(View.VISIBLE);
        }
    }

    private void initRecord() {
        if (mVideoPublish == null) {
            mVideoPublish = new TXUGCPublish(provideActivity().getApplicationContext(), "blackbean");
            mVideoPublish.setListener(ChattingActivity.this);
        }
        mBinding.buttonRecord.setRecordListener(new RecordVoiceButton.OnRecordVoiceListener() {
            @Override
            public void onRecordFinished(int duration, String path) {
//                readyInfo = readyPublishVideo(2, path, null, duration);
                realPublish(readyPublishVideo(2, path, null, duration, ""));
            }

            @Override
            public void onStartRecord() {
                chattingAdapter.stopMediaPlayer();
            }
        });
        mBinding.buttonRecord.setFilePath(Constant.STORAGE_PATH + "/voice");
        chattingAdapter.initMediaPlayer();
    }

    @Override
    public void onPublishProgress(long uploadBytes, long totalBytes) {
        LogUtil.i("publishprogress", "__" + (int) (100 * uploadBytes / totalBytes));
    }

    @Override
    public void onPublishComplete(TXUGCPublishTypeDef.TXPublishResult result) {
        if (result.retCode == 0) {
            if (result.type == 2) {
                VideoInfoPackage videoInfoPackage = audioMap.get(result.localPath);
                if (videoInfoPackage != null) {
                    videoInfoPackage.webUrl = "s;" + result.videoURL + ";" + result.duration;
                    audioMap.put(result.localPath, videoInfoPackage);
                }
            } else if (result.type == 3) {
                VideoInfoPackage videoInfoPackage = audioMap.get(result.localPath);
                if (videoInfoPackage != null) {
                    videoInfoPackage.webUrl = "v;" + result.videoURL + ";" + result.coverURL + ";" + result.duration + ";" + result.width + ";" + result.height;
                    audioMap.put(result.localPath, videoInfoPackage);
                }
            }
        } else {
//            if (result.retCode != TVCConstants.ERR_CLIENT_BUSY && result.retCode != TVCConstants.ERR_UGC_PUBLISHING)
            for (int i = mData.size() - 1; i >= 0; i--) {
                if (mData.get(i).getSelfStatus() == 1 && result.localPath.equals(mData.get(i).getVideoPath())) {
                    mData.get(i).setSelfStatus(2);
                    chattingAdapter.notifyItemChanged(i);
                    break;
                }
            }
        }

        for (int i = mData.size() - 1; i >= 0; i--) {
            if (mData.get(i).getSelfStatus() == 1 && result.localPath.equals(mData.get(i).getVideoPath())) {
                realPublish(mData.get(i));
                break;
            }
        }

        LogUtil.i("publishcomplete", result.retCode + " Msg:" + (result.retCode == 0 ? result.videoURL : result.descMsg));
    }

    private void initAdapterChats() {
        chattingAdapter = new ChattingAdapter(provideActivity(), mData, mBinding.recycleChatting);
        chattingAdapter.setOnItemOperateListen(new ChattingAdapter.onItemOperateListen() {
            @Override
            public void zanOperate(int position) {
                mPresenter.likeOperate(mData.get(position).getId(), mData.get(position).getHasLike(), Like_TAG);
                if (mData.get(position).getHasLike() == 1) {
                    mData.get(position).setHasLike(0);
                    try {
                        mData.get(position).setLikecn("" + (Integer.parseInt(mData.get(position).getLikecn()) - 1));
                    } catch (Exception e) {
                    }

                    ToastUtil.showToast("取消点赞");
                } else {
                    mData.get(position).setHasLike(1);
                    try {
                        mData.get(position).setLikecn("" + (Integer.parseInt(mData.get(position).getLikecn()) + 1));
                    } catch (Exception e) {
                    }
                    ToastUtil.showToast("点赞成功");
                }
                chattingAdapter.notifyItemChanged(position);
            }

            @Override
            public void collectOperate(int position) {
                selectPosition = position;
                mPresenter.operateCollect(mData.get(position).getHasCollect() == 1 ? -1 : 1, mData.get(position).getId(), Collection_TAG);
            }

            @Override
            public void commentOperate(int position) {
                Intent intent = new Intent(provideActivity(), ThemeDetailActivity.class);
                intent.putExtra("topicid", mData.get(position).getTopic_id());
                intent.putExtra("themeid", mData.get(position).getId());
                intent.putExtra("fromchat", true);
                intent.putExtra("showkeyboard", true);
                startActivity(intent);
            }

            @Override
            public void clickHead(int position) {
                if (!isSoftShowing() && mBinding.flayoutChatPicRoot.getVisibility() == View.GONE && mBinding.llayoutChatEmojiRoot.getVisibility() == View.GONE && mBinding.flayoutRecord.getVisibility() == View.GONE) {
                    Intent intent = new Intent(provideActivity(), UserInfoActivity.class);
                    intent.putExtra("userid", mData.get(position).getAuthor_userid());
                    startActivity(intent);
                }
            }

            @Override
            public void longClick(int position) {
                if (chatOperatePoppubWindow == null) {
                    chatOperatePoppubWindow = new ChatOperatePoppubWindow(provideActivity());
                }
                chatOperatePoppubWindow.setHasCollect(mData.get(position).getHasCollect() == 1);
                chatOperatePoppubWindow.setDismissListener(new ChatOperatePoppubWindow.DismissListener() {
                    @Override
                    public void dismiss() {

                    }

                    @Override
                    public void onReply() {
                        Intent intent = new Intent(provideActivity(), ThemeDetailActivity.class);
                        intent.putExtra("topicid", mData.get(position).getTopic_id());
                        intent.putExtra("themeid", mData.get(position).getId());
                        intent.putExtra("fromchat", true);
                        intent.putExtra("showkeyboard", true);
                        startActivity(intent);
                    }

                    @Override
                    public void onAt() {
                        mBinding.editContent.addAtSpan("@", mData.get(position).getAuthor_username(), mData.get(position).getAuthor_userid(), false);
                        mBinding.editContent.getText().insert(mBinding.editContent.getText().length(), " ");
                    }

                    @Override
                    public void onCollect() {
                        selectPosition = position;
                        mPresenter.operateCollect(mData.get(position).getHasCollect() == 1 ? -1 : 1, mData.get(position).getId(), Collection_TAG);
                    }

                    @Override
                    public void onReport() {
                        Intent intent = new Intent(provideActivity(), WebviewActivity.class);
                        intent.putExtra("weburl", Constant.BASEURL + "/report?thread=" + mData.get(position).getId());
                        startActivity(intent);
                    }
                });
                chatOperatePoppubWindow.show();
            }

            @Override
            public void onClick(int position) {
                if (!isSoftShowing() && mBinding.flayoutChatPicRoot.getVisibility() == View.GONE && mBinding.llayoutChatEmojiRoot.getVisibility() == View.GONE && mBinding.flayoutRecord.getVisibility() == View.GONE) {
                    Intent intent = new Intent(provideActivity(), ThemeDetailActivity.class);
                    intent.putExtra("topicid", mData.get(position).getTopic_id());
                    intent.putExtra("fromchat", true);
                    intent.putExtra("themeid", mData.get(position).getId());
                    startActivity(intent);
                }
            }

            @Override
            public void sendEerror(int position) {
                if (republishPop == null) {
                    republishPop = new ChatRepublishPoppubWindow(provideActivity());
                }
                republishPop.setDismissListener(new ChatRepublishPoppubWindow.DismissListener() {
                    @Override
                    public void dismiss() {

                    }

                    @Override
                    public void onDelete() {
                        mData.remove(position);
                        chattingAdapter.notifyItemRemoved(position);
                    }

                    @Override
                    public void onRepublish() {
                        mData.get(position).setSelfStatus(1);
                        chattingAdapter.notifyItemChanged(position);
//                            readyInfo = mData.get(position);
                        realPublish(mData.get(position));

                            /*sendInPostion = position;
                            if (sendInPostion < mData.size()) {
                                mData.get(sendInPostion).setSelfStatus(1);
                                chattingAdapter.notifyItemChanged(sendInPostion);
                                readyInfo = mData.get(sendInPostion);
                                realPublish(readyInfo);
                            }*/
                    }
                });
                republishPop.show();
            }

            @Override
            public void clickPic(int position) {
                if (mData.get(position).getIsread() != 1) {
                    mPresenter.readTheme(mData.get(position).getId(), ReadTheme_TAG);
                    mData.get(position).setIsread(1);
                }
            }

            @Override
            public void clickAudio(int position) {
                if (mData.get(position).getAudioread() != 1) {
                    mPresenter.readThemeAudio(mData.get(position).getId(), ReadTheme_TAG);
                }
            }

            @Override
            public void clickVideo(int position) {
                if (mData.get(position).getVideoType() == 3) {
                    Intent intent = new Intent(provideActivity(), NewVodPlayerActivity.class);
                    intent.putExtra("frompath", 4);
                    intent.putExtra("videopath", mData.get(position).getVideoPath());
                    intent.putExtra("coverpath", mData.get(position).getCoverPath());
                    startActivity(intent);
                } else if (mData.get(position).getVideoType() == 4) {
                    Intent intent = new Intent(provideActivity(), WebviewActivity.class);
                    intent.putExtra("weburl", mData.get(position).getVideoPath());
                    intent.putExtra("head", true);
                    startActivity(intent);
                }
            }
        });

        mBinding.recycleChatting.setAdapter(chattingAdapter);
        mBinding.recycleChatting.setItemAnimator(null);
        mBinding.chatrootview.setAdatperLoading(chattingAdapter);
        linearLayoutManager = new LinearLayoutManager(provideActivity(), LinearLayoutManager.VERTICAL, false);
        mBinding.recycleChatting.setLayoutManager(linearLayoutManager);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mBinding.recycleChatting.scrollToPosition(chattingAdapter.getItemCount() - 1);
            }
        }, 50);

        mBinding.recycleChatting.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1)) {
                    unreadDown = 0;
                    mBinding.flayoutChatUnreadDown.setVisibility(View.GONE);
                }
            }
        });

        mBinding.chatrootview.setOnLoadingLisening(new ChattingRootView.OnLoadingListening() {
            @Override
            public void startLoading() {
                if (mPresenter != null)
                    loadMore();

            }

            @Override
            public void hideViews() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hideKeyboard();
                        mBinding.editContent.clearFocus();
                        mBinding.llayoutAttachFunction.setVisibility(View.GONE);
                        mBinding.flayoutRecord.setVisibility(View.GONE);
                        mBinding.llayoutTxt.setVisibility(View.VISIBLE);
                        mBinding.flayoutChatPicRoot.setVisibility(View.GONE);
                        mBinding.ivChatAdd.setSelected(false);
                        mBinding.flayoutChatVideoRoot.setVisibility(View.GONE);
                        mBinding.ivChatAddVideo.setSelected(false);
                        mBinding.llayoutChatEmojiRoot.setVisibility(View.GONE);
                        mBinding.ivChatEmoji.setSelected(false);
                    }
                }, 50);

            }
        });

    }

    private void loadMore() {
        for (InnerThemeListBean info : mData) {
            if (info.getType() != LOADING && !info.isSelf()) {
                themeid = info.getId();
                break;
            }
        }
        mPresenter.getThemeList(topicId, themeid + "", -1, label, themeListmore_TAG);
    }

    private void refresh() {
        mPresenter.getThemeList(topicId, null, 0, 0, themeList_TAG);
    }

    private void addNewTheme(String themeIds) {
        int themeIdNext = 0;
        for (int i = mData.size() - 1; i >= 0; i--) {
            if (!mData.get(i).isSelf() && mData.get(i).getId() != 0) {
                themeIdNext = mData.get(i).getId();
                break;
            }
        }
        if (label == 1 && lastThemeId > themeIdNext)
            themeIdNext = lastThemeId;
        mPresenter.getThemeList(topicId, themeIdNext + "", 1, 0, themeAdd_TAG);

    }

    private void initAdapterPics() {
        selectPics.add(addButton);
        picAdapter = new ChatSelectPicAdapter(provideActivity(), selectPics);
        picAdapter.setmListen(new ChatSelectPicAdapter.onItemOperateListen() {
            @Override
            public void onItempicClick(int position) {
                previewPics(position);
            }

            @Override
            public void onItemdelClick(int position) {
                selectPics.remove(position);
                if (addButton.equals(selectPics.get(selectPics.size() - 1))) {
                    picAdapter.notifyItemRemoved(position);
                } else {
                    selectPics.add(addButton);
                    picAdapter.notifyDataSetChanged();
                }
                updataPicsView();
            }

            @Override
            public void onItemaddClick(int position) {
                selectPicsFromPhotos();
            }
        });
        mBinding.recycleChatmenuPicShow.setLayoutManager(new GridLayoutManager(provideActivity(), 4));
        mBinding.recycleChatmenuPicShow.setAdapter(picAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(new ChatPicsTouchHelperCallback(picAdapter, picAdapter));
        touchHelper.attachToRecyclerView(mBinding.recycleChatmenuPicShow);
    }

    private void initBox() {
        if (box == null) {
            box = App.mApp.getmBoxStore().boxFor(InnerThemeListBean.class);
            queryBuild = box.query().equal(InnerThemeListBean_.uid_android, authorId).equal(InnerThemeListBean_.topic_id, Integer.parseInt(topicId)).build();
        }
        List<InnerThemeListBean> boxList = queryBuild.find();
        for (InnerThemeListBean boxBean : boxList) {
            if (boxBean.getSelfStatus() == 1)
                boxBean.setSelfStatus(2);
            if (!TextUtils.isEmpty(boxBean.getSrcBox())) {
                String[] split = boxBean.getSrcBox().split(",");
                boxBean.setSrc(new ArrayList<>(Arrays.asList(split)));
            }
            if (!TextUtils.isEmpty(boxBean.getThumbBox())) {
                String[] split = boxBean.getThumbBox().split(",");
                boxBean.setThumb(new ArrayList<>(Arrays.asList(split)));
            }
            if (boxBean.getId() == 0 && boxBean.getSelfStatus() == 2)
                resendDatas.add(boxBean);
            if (boxBean.getId() != 0)
                last_thread_id = boxBean.getId();
            mData.add(boxBean);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        RecordVoiceButton.mIsPressed = false;
        queryBuild.remove();
        List<InnerThemeListBean> boxData = new ArrayList<>();
        List<InnerThemeListBean> errorData = new ArrayList<>();
        int currentNum = 0;
        for (int i = mData.size() - 1; i >= 0; i--) {
            InnerThemeListBean innerThemeListBean = mData.get(i);
            if (innerThemeListBean.getType() != LOADING)
                if (currentNum < boxMaxNum) {
                    if (innerThemeListBean.getSelfStatus() == 1 || innerThemeListBean.getSelfStatus() == 2) {
                        errorData.add(innerThemeListBean);
                    } else {
                        boxData.add(innerThemeListBean);
                    }
                    currentNum++;
                } else {
                    break;
                }
        }
        Collections.reverse(boxData);
        Collections.reverse(errorData);
        boxData.addAll(boxData.size(), errorData);
        for (InnerThemeListBean boxBean : boxData) {
            boxBean.setUid_android(authorId);
//            if (boxBean.getSelfStatus() == 1)
//                boxBean.setSelfStatus(2);
            if (TextUtils.isEmpty(boxBean.getSrcBox()) && boxBean.getSrc() != null && boxBean.getSrc().size() > 0) {
                StringBuffer stringBuffer = new StringBuffer();
                for (String src : boxBean.getSrc()) {
                    stringBuffer.append(src).append(",");
                }
                boxBean.setSrcBox(stringBuffer.toString());
            }
            if (TextUtils.isEmpty(boxBean.getThumbBox()) && boxBean.getThumb() != null && boxBean.getThumb().size() > 0) {
                StringBuffer stringBuffer = new StringBuffer();
                for (String thumb : boxBean.getThumb()) {
                    stringBuffer.append(thumb).append(",");
                }
                boxBean.setThumbBox(stringBuffer.toString());
            }
        }
        box.put(boxData);
    }

    @Override
    protected void initEvent() {
        mBinding.ivHotTheme.setOnClickListener(this);

        mBinding.ivChatmenuVideoEmptyadd.setOnClickListener(this);
        mBinding.ivChatVideopathDel.setOnClickListener(this);
        mBinding.ivChatVideopathPreview.setOnClickListener(this);


        mBinding.llayoutTopicmemberInfo.setOnClickListener(this);
        mBinding.llayoutChatMembers.setOnClickListener(this);
        mBinding.barRightClick.setOnClickListener(this);


        mBinding.barLeftClick.setOnClickListener(this);
        mBinding.ivChatEmoji.setOnClickListener(this);
        mBinding.ivChatClick.setOnClickListener(this);
        mBinding.flayoutChatVideo.setOnClickListener(this);
        mBinding.flayoutChatSend.setOnClickListener(this);
        mBinding.flayoutChatUnreadDown.setOnClickListener(this);
        mBinding.ivChatFirstSplash.setOnClickListener(this);

        mBinding.ivEmojiDelete.setOnClickListener(this);

        mBinding.ivChatAdd.setOnClickListener(this);
        mBinding.ivChatmenuPicEmptyadd.setOnClickListener(this);

        mBinding.ivChatRecord.setOnClickListener(this);
        mBinding.ivChatKeyboard.setOnClickListener(this);

        mBinding.editContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    clickEditView();
                }
                return false;
            }
        });

        mBinding.editContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (before == 0 && s.toString().length() > 0 && s.toString().substring(s.length() - 1).equals("@")) {
                    Intent intent = new Intent(provideActivity(), AtMemberListActivity.class);
                    intent.putExtra("topicid", topicId);
                    startActivityForResult(intent, AT_request);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                updataSendButton();

            }
        });
    }

    //点击发送按钮
    private void sendPublishButton() {
        if (!TextUtils.isEmpty(mBinding.editContent.getWebString().trim()) || selectPics.size() > 1 || selectVideos.size() > 0) {
//            readyInfo = readyPublish(mBinding.editContent.getWebString().trim());
            if (selectVideos.size() > 0) {
                realPublish(readyPublishVideo(3, selectVideos.get(0), selectVideos.get(0), videoDuration, mBinding.editContent.getWebString().trim()));
            } else {
                realPublish(readyPublish(mBinding.editContent.getWebString().trim()));
            }


          /*  if (selectPics.size() > 1 && updataPicMap()) {
                selectPics.remove(addButton);
                webPhotos.clear();
                for (String key : selectPics) {
                    webPhotos.add(picMap.get(key));
                }
                publishTheme(topicId, mBinding.editContent.getWebString().trim(), webPhotos, publishTheme_txt_TAG);
            } else {
                publishTheme(topicId, mBinding.editContent.getWebString().trim(), null, publishTheme_txt_TAG);
            }*/
        } else {
            ToastUtil.showToast("请输入观点");
        }
    }

    private void realPublish(InnerThemeListBean info) {
        if (info.getThumb() != null && info.getThumb().size() > 0) {//图片+文本
            if (updataPicMapReal(info.getThumb(), info.getTerminal_id(), uploadpicone_realSend_TAG)) {
                realPublishWithPics(info);
            }
        } else if (info.getVideoType() == 3) {//视频
            if (updataAudioMap(3, info.getVideoPath(), info.getCoverPath(), info.getDuration())) {
                realPublishWithAudio(info);
            }
        } else if (info.getVideoType() == 2) {//语音
            if (updataAudioMap(2, info.getVideoPath(), info.getCoverPath(), info.getDuration())) {
                realPublishWithAudio(info);
            }
        } else {
            publishTheme(topicId, info.getContent(), null, info.getTerminal_id() + "", publishTheme_txt_TAG);
        }
    }

    private void realPublishWithPics(InnerThemeListBean info) {
        info.getWebPhotos().clear();
        for (String key : info.getThumb()) {
            info.getWebPhotos().add(picMap.get(key));
        }
        publishTheme(topicId, info.getContent(), info.getWebPhotos(), info.getTerminal_id() + "", publishTheme_pic_TAG);
    }

    private void realPublishWithAudio(InnerThemeListBean info) {
        info.getWebPhotos().clear();
        info.getWebPhotos().add(audioMap.get(info.getVideoPath()).webUrl);
        publishTheme(topicId, info.getContent(), info.getWebPhotos(), info.getTerminal_id() + "", publishTheme_audio_TAG);
    }

    //预发布图片文字
    private InnerThemeListBean readyPublish(String content) {
        InnerThemeListBean contentBean = new InnerThemeListBean(0);
        contentBean.setSelf(true);
        contentBean.setSelfStatus(1);
        contentBean.setTopic_id(Integer.parseInt(topicId));
        contentBean.setAuthor_userid(SharedPrefUtil.getInt(userid_key, 0));
        contentBean.setAuthor_username(SharedPrefUtil.getString(name_key, ""));
        contentBean.setPortrait_thumb(SharedPrefUtil.getString(head_key, ""));
        contentBean.setCreated_at2(FormatUtils.stampToDate("" + System.currentTimeMillis() / 1000));
        contentBean.setContent(content);
        contentBean.setTerminal_id(System.currentTimeMillis());
        contentBean.setThumb(new ArrayList<>());
        contentBean.setWebPhotos(new ArrayList<>());

        if (selectPics.size() > 1) {
            contentBean.getThumb().addAll(selectPics);
            contentBean.getThumb().remove(addButton);
            contentBean.setVideoType(1);
        }
        mData.add(contentBean);
        chattingAdapter.notifyItemInserted(mData.size() - 1);
        mBinding.recycleChatting.scrollToPosition(chattingAdapter.getItemCount() - 1);
//        sendInPostion = mData.size() - 1;
//        webPhotos.clear();
        selectPics.clear();
        selectPics.add(addButton);
        hideKeyAndEmoji();
        picAdapter.notifyDataSetChanged();
        showEmptyView();
        updataPicsView();
        return contentBean;
    }

    //预发布语音和视频
    private InnerThemeListBean readyPublishVideo(int type, String videoPath, String coverPath, int duration, String content) {
        InnerThemeListBean contentBean = new InnerThemeListBean(0);
        contentBean.setSelf(true);
        contentBean.setSelfStatus(1);
        contentBean.setContent(content);
        contentBean.setTopic_id(Integer.parseInt(topicId));
        contentBean.setAuthor_userid(SharedPrefUtil.getInt(userid_key, 0));
        contentBean.setAuthor_username(SharedPrefUtil.getString(name_key, ""));
        contentBean.setPortrait_thumb(SharedPrefUtil.getString(head_key, ""));
        contentBean.setCreated_at2(FormatUtils.stampToDate("" + System.currentTimeMillis() / 1000));
        contentBean.setTerminal_id(System.currentTimeMillis());

        contentBean.setVideoType(type);
        contentBean.setVideoPath(videoPath);
        contentBean.setCoverPath(coverPath);
        contentBean.setDuration(duration);
        if (type == 3) {
            android.media.MediaMetadataRetriever mmr = new android.media.MediaMetadataRetriever();
            try {
                mmr.setDataSource(videoPath);
                contentBean.setVideoWidth(Float.parseFloat(mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)));
                contentBean.setVideoHeight(Float.parseFloat(mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)));
            } catch (Exception ex) {
            } finally {
                mmr.release();
            }

        }
        mData.add(contentBean);
        chattingAdapter.notifyItemInserted(mData.size() - 1);
        mBinding.recycleChatting.scrollToPosition(chattingAdapter.getItemCount() - 1);
//        sendInPostion = mData.size() - 1;

        picAdapter.notifyDataSetChanged();
        if (type == 3) {
            hideKeyAndEmoji();
            selectVideos.clear();
            updataVideoView();
        }

        showEmptyView();
        return contentBean;
    }


    private void startUploadOne(File file, String localpath, int tag) {
        mPresenter.upLoadPicOne(file, localpath, tag);
    }

    private void startUploadOneReal(File file, String localpath, long terminal_id, int tag) {
        mPresenter.uploadOneReal(file, localpath, terminal_id, tag);
    }


    private void hideKeyAndEmoji() {
        mBinding.editContent.setText("");
    }

    private void publishTheme(String topicId, String content, List<String> picList, String terminal_id, int tag) {
        mPresenter.publishTheme(topicId, content, picList, terminal_id, tag);
    }

    @Override
    protected boolean isBindEventBus() {
        return true;
    }


    @Override
    public void showLoading(int tag) {
        if ((tag == themeList_TAG || tag == themeList_HOT_TAG) && mData.size() == 0)
            startLoading();
    }

    @Override
    public void hideLoading(int tag) {
        stopLoading();
    }

    @Override
    public void setData(ErrorBean errorBean, int tag) {
        switch (tag) {
            case ReadTheme_TAG:
                break;
            case themeList_TAG:
                if (errorBean instanceof ThemeListBean) {
                    label = 0;
                    mBinding.ivHotTheme.setSelected(false);
                    restartGetErrorDatas();
                    mData.clear();
                    chattingAdapter.notifyDataSetChanged();
                    ThemeListBean bean = (ThemeListBean) errorBean;
                    if (bean.getData() != null && bean.getData().size() > 0) {
                        dealDatasForDB(bean.getData());

                        chattingAdapter.removeLoading();
                        Collections.reverse(bean.getData());
                        mData.addAll(0, bean.getData());
                        mData.addAll(mData.size(), resendDatas);
                        resendDatas.clear();
//                        chattingAdapter.notifyItemRangeInserted(0, bean.getData().size() - 1);
                        chattingAdapter.notifyDataSetChanged();
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mBinding.recycleChatting.scrollToPosition(chattingAdapter.getItemCount() - 1);
                            }
                        }, 50);

                        mBinding.chatrootview.loadFinish();
                        firstLoadSize = bean.getData().size();
                        isFirstloadData = true;
                        firstShowUnreadUpView();

                    } else {
                        mData.addAll(resendDatas);
                        resendDatas.clear();
                        chattingAdapter.notifyDataSetChanged();
                        chattingAdapter.removeLoading();
                        mBinding.chatrootview.loadFinish();
                    }
                    showEmptyView();
                }
                break;
            case themeList_HOT_TAG:
                if (errorBean instanceof ThemeListBean) {
                    label = 1;
                    mBinding.ivHotTheme.setSelected(true);
                    restartGetErrorDatas();
                    mData.clear();
                    chattingAdapter.notifyDataSetChanged();
                    ThemeListBean bean = (ThemeListBean) errorBean;
                    if (bean.getData() != null && bean.getData().size() > 0) {
                        dealDatasForDB(bean.getData());

                        chattingAdapter.removeLoading();
                        Collections.reverse(bean.getData());
                        mData.addAll(0, bean.getData());
                        mData.addAll(mData.size(), resendDatas);
//                        chattingAdapter.notifyItemRangeInserted(0, bean.getData().size() - 1);
                        chattingAdapter.notifyDataSetChanged();
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mBinding.recycleChatting.scrollToPosition(chattingAdapter.getItemCount() - 1);
                            }
                        }, 50);

                        mBinding.chatrootview.loadFinish();
                        firstLoadSize = bean.getData().size();
                        isFirstloadData = true;
                        firstShowUnreadUpView();

                    } else {
                        mData.addAll(resendDatas);
                        chattingAdapter.notifyDataSetChanged();
                        chattingAdapter.removeLoading();
                        mBinding.chatrootview.loadFinish();
                    }
                    showEmptyView();
                }
                break;
            case themeListmore_TAG:
                if (errorBean instanceof ThemeListBean) {
                    ThemeListBean bean = (ThemeListBean) errorBean;
                    if (bean.getData() != null && bean.getData().size() > 0) {
                        dealDatasForDB(bean.getData());
                        Collections.reverse(bean.getData());
                        chattingAdapter.removeLoading();
                        mData.addAll(0, bean.getData());
                        chattingAdapter.notifyItemRangeInserted(0, bean.getData().size());
                        mBinding.chatrootview.loadFinish();
                        unreadUp = unreadUp - bean.getData().size();
                    } else {
                        chattingAdapter.removeLoading();
                        mBinding.chatrootview.loadFinish();
                        unreadUp = 0;
                        ToastUtil.showToast("没有更多了");
                    }
                    updataUnreadUpView();
                }
                break;
            case themeAdd_TAG:
                if (errorBean instanceof ThemeListBean) {
                    ThemeListBean bean = (ThemeListBean) errorBean;
                    if (bean.getData() != null && bean.getData().size() > 0) {
                        dealDatasForDB(bean.getData());
                        Collections.reverse(bean.getData());
                        int j = mData.size() - bean.getData().size() - 1;
                        int offset = 0;
                        loop1:
                        for (InnerThemeListBean newData : bean.getData()) {
                            if (mData.size() > 0) {
                                for (int i = mData.size() - 1 - offset; i >= mData.size() - j - offset; i--) {
                                    if (i >= 0) {
                                        if (!mData.get(i).isSelf() && mData.get(i).getId() == newData.getId()) {
                                            continue loop1;
                                        }
                                    }
                                }
                                offset++;
                            }

                            if (newData.getAuthor_userid() == authorId) {
                                boolean isReplace = false;
                                for (int i = mData.size() - 1; i >= 0; i--) {
                                    if (newData.getTerminal_id() == mData.get(i).getTerminal_id()) {
                                       /* isReplace = true;
                                        mData.set(i, newData);
                                        chattingAdapter.notifyItemChanged(i);
                                        break;*/


                                        if (i == mData.size() - 1) {
                                            isReplace = true;
                                            mData.set(i, newData);
                                            chattingAdapter.notifyItemChanged(i);
                                        } else {
                                            mData.remove(i);
                                            chattingAdapter.notifyItemRemoved(i);
                                        }
                                        break;
                                    }
                                }
                                if (!isReplace) {
                                    mData.add(newData);
                                    chattingAdapter.notifyItemInserted(mData.size() - 1);
                                   /* if (linearLayoutManager.getItemCount() - 1 == linearLayoutManager.findLastVisibleItemPosition()) {
                                        mData.add(newData);
                                        chattingAdapter.notifyItemInserted(mData.size() - 1);
                                        mBinding.recycleChatting.scrollToPosition(chattingAdapter.getItemCount() - 1);
                                    } else {
                                        mData.add(newData);
                                        chattingAdapter.notifyItemInserted(mData.size() - 1);
                                    }*/
                                }

                            } else {
                                if (linearLayoutManager.getItemCount() - 1 == linearLayoutManager.findLastVisibleItemPosition()) {
                                    mData.add(newData);
                                    chattingAdapter.notifyItemInserted(mData.size() - 1);
                                    mBinding.recycleChatting.scrollToPosition(chattingAdapter.getItemCount() - 1);
                                } else {
                                    mData.add(newData);
                                    chattingAdapter.notifyItemInserted(mData.size() - 1);
                                    unreadDown++;
                                    mBinding.txtChatUnreadDown.setText(unreadDown + "个新观点");
                                    mBinding.flayoutChatUnreadDown.setVisibility(View.VISIBLE);
                                }
                            }

                        }

                    }
                }
                showEmptyView();
                break;
            case ThemeNotify_TAG:
                if (errorBean instanceof ThemeListBean) {
                    ThemeListBean bean = (ThemeListBean) errorBean;
                    if (bean.getData() != null && bean.getData().size() > 0) {
                        dealDatasForDB(bean.getData());
                        for (InnerThemeListBean newData : bean.getData()) {
                            for (int i = 0; i < mData.size(); i++) {
                                if (mData.get(i).getId() == newData.getId()) {
                                    mData.set(i, newData);
                                    chattingAdapter.notifyItemChanged(i);
                                    break;
                                }
                            }
                        }
                    }
                }
                break;
            case publishTheme_txt_TAG:
                for (int i = mData.size() - 1; i >= 0; i--) {
                    if (mData.get(i).getSelfStatus() == 1 && errorBean.key.equals(mData.get(i).getTerminal_id() + "")) {
                        mData.get(i).setSelfStatus(3);
                        chattingAdapter.notifyItemChanged(i);
                        break;
                    }
                }
                break;
            case publishTheme_pic_TAG:
                for (int i = mData.size() - 1; i >= 0; i--) {
                    if (mData.get(i).getSelfStatus() == 1 && errorBean.key.equals(mData.get(i).getTerminal_id() + "")) {
                        mData.get(i).setSelfStatus(3);
                        chattingAdapter.notifyItemChanged(i);
                        break;
                    }
                }
                for (InnerThemeListBean info : mData) {
                    if (info.getSelfStatus() == 1 && info.getThumb().size() > 0) {
                        realPublish(info);
                        break;
                    }
                }
                break;
            case publishTheme_audio_TAG:
                for (int i = mData.size() - 1; i >= 0; i--) {
                    if (mData.get(i).getSelfStatus() == 1 && errorBean.key.equals(mData.get(i).getTerminal_id() + "")) {
                        mData.get(i).setSelfStatus(3);
                        chattingAdapter.notifyItemChanged(i);
                        break;
                    }
                }
                for (InnerThemeListBean info : mData) {
                    if (info.getSelfStatus() == 1 && (info.getVideoType() == 2 || info.getVideoType() == 3)) {
                        realPublish(info);
                        break;
                    }
                }
                break;
            case uploadpicone_TAG:
                if (errorBean instanceof UploadBean) {
                    UploadBean bean = (UploadBean) errorBean;
                    picMap.put(errorBean.key, bean.getSrc());
                    updataPicMap(selectPics, uploadpicone_TAG);
                }
                break;
            case uploadpicone_realSend_TAG:
                if (errorBean instanceof UploadBean) {
                    UploadBean bean = (UploadBean) errorBean;
                    picMap.put(errorBean.localPath, bean.getSrc());
                    for (int i = mData.size() - 1; i >= 0; i--) {
                        if (mData.get(i).getSelfStatus() == 1 && errorBean.key.equals(mData.get(i).getTerminal_id() + "")) {
                            if (updataPicMapReal(mData.get(i).getThumb(), Long.parseLong(errorBean.key), uploadpicone_realSend_TAG))
                                realPublishWithPics(mData.get(i));
                            break;
                        }
                    }
//                    if (updataPicMap(readyInfo.getThumb(), uploadpicone_realSend_TAG))
//                        realPublishWithPics(readyInfo);
                }
                break;
            case Members_TAG:
                if (errorBean instanceof ChatTopicMembersBean) {
                    membersBean = (ChatTopicMembersBean) errorBean;
                    if (!isGetUnreadUpNum) {
                        isGetUnreadUpNum = true;
                        if (membersBean.getNoread() != 0) {
                            unreadUp = membersBean.getNoread();
                            firstShowUnreadUpView();
                        }

                    }

                    if (membersBean.getCount() != null) {
                        mBinding.llayoutTopicmemberInfo.setVisibility(View.VISIBLE);
                        mBinding.barTxtPersonnum.setText(membersBean.getCount().getNewX() + " 名新成员 " + membersBean.getCount().getOnline() + " 人在线");
                    }
                    if (membersBean.getList() != null) {
                        ChatTopicMembersBean.ListBean listBean = new ChatTopicMembersBean.ListBean();
                        listBean.setDrawableId(R.drawable.icon_members_more);
                        if (membersBean.getList().size() > 8) {
                            membersBean.getList().add(8, listBean);
                        } else {
                            membersBean.getList().add(listBean);
                        }
                        membersAdapter = new ChatTopicMembersAdapter(provideActivity(), membersBean.getList());
                        membersAdapter.setOnItemClickListen(new ChatTopicMembersAdapter.onItemClickListen() {
                            @Override
                            public void onclick(int position) {
                                if (membersBean.getList().get(position).getDrawableId() != 0) {
                                    Intent intent = new Intent(provideActivity(), MembersDisplayActivity.class);
                                    intent.putExtra("topicid", Integer.parseInt(topicId));
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(provideActivity(), UserInfoActivity.class);
                                    intent.putExtra("userid", membersBean.getList().get(position).getUser_id());
                                    startActivity(intent);
                                }
                            }
                        });
                        mBinding.recycleChatMember.setLayoutManager(new LinearLayoutManager(provideActivity(), LinearLayoutManager.HORIZONTAL, false));
                        mBinding.recycleChatMember.setAdapter(membersAdapter);
                    }

                }
                break;
            case Collection_TAG:
                if (mData.get(selectPosition).getHasCollect() == 1) {
                    mData.get(selectPosition).setHasCollect(0);
                    ToastUtil.showToast("取消收藏");
                    EventBus.getDefault().post(new CollectionThemeBus(mData.get(selectPosition).getId(), false));
                } else {
                    mData.get(selectPosition).setHasCollect(1);
                    ToastUtil.showToast("收藏成功");
                    EventBus.getDefault().post(new CollectionThemeBus(mData.get(selectPosition).getId(), true));
                }
                chattingAdapter.notifyItemChanged(selectPosition);
                break;
        }
    }

    private void restartGetErrorDatas() {
        if (resendDatas.size() == 0) {
            for (InnerThemeListBean data : mData) {
                if (data.getId() == 0 && data.getSelfStatus() == 2) {
                    resendDatas.add(data);
                }
            }
        }
        for (int i = mData.size() - 1; i > 0; i--) {
            if (mData.get(i).getId() != 0) {
                lastThemeId = mData.get(i).getId();
                break;
            }
        }
    }

    private void dealDatasForDB(List<InnerThemeListBean> datas) {
        StringBuffer srcBuffer = new StringBuffer();
        StringBuffer thumBuffer = new StringBuffer();
        for (InnerThemeListBean data : datas) {
            data.getSrc().clear();
            data.getThumb().clear();
            srcBuffer.delete(0, srcBuffer.length());
            thumBuffer.delete(0, thumBuffer.length());
            if (data.getAttch() != null && data.getAttch().size() > 0) {
                for (AttachThemeBean attach : data.getAttch()) {
                    if (attach.getMime() == 1) {
                        data.setVideoType(attach.getMime());
                        srcBuffer.append(attach.getUrl()).append(",");
                        thumBuffer.append(attach.getUrl2()).append(",");
                        data.getSrc().add(attach.getUrl());
                        data.getThumb().add(attach.getUrl2());
                    } else if (attach.getMime() == 2) {
                        data.setVideoType(attach.getMime());
                        data.setVideoPath(attach.getUrl());
                        data.setDuration(Integer.parseInt(Uri.parse(attach.getUrl()).getQueryParameter("t")));
                    } else if (attach.getMime() == 3) {
                        data.setVideoType(attach.getMime());
                        data.setVideoPath(attach.getUrl());
                        data.setCoverPath(attach.getUrl2());
                        data.setDuration(Integer.parseInt(Uri.parse(attach.getUrl()).getQueryParameter("t")));

                    } else if (attach.getMime() == 4) {
                        data.setVideoType(attach.getMime());
                        data.setVideoPath(attach.getUrl2());
                        data.setCoverPath(attach.getUrl());
                    }
                }
                data.setSrcBox(srcBuffer.toString());
                data.setThumbBox(thumBuffer.toString());
            }
        }
    }

    private void firstShowUnreadUpView() {
        if (isFirstloadData && isGetUnreadUpNum) {
            if (last_thread_id != 0 && unreadUp != 0) {
                for (int i = 0; i < mData.size(); i++) {
                    if (mData.get(i).getId() == last_thread_id) {
                        unreadUp = 0;
                        break;
                    }
                }
                unreadUp = unreadUp - firstLoadSize;
                updataUnreadUpView();

            }
        }
    }

    @Override
    public void showError(ErrorBean errorBean, int tag) {
        if (tag != themeAdd_TAG && tag != ThemeNotify_TAG)
            if (errorBean.androidType.equals(ErrorBean.TYPE_SHOW))
                ToastUtil.showToast(errorBean.desc);
        switch (tag) {
            case themeList_TAG:
                showEmptyView();
                break;
            case themeList_HOT_TAG:
                showEmptyView();
                break;
            case uploadpicone_realSend_TAG:
                for (int i = mData.size() - 1; i >= 0; i--) {
                    if (mData.get(i).getSelfStatus() == 1 && mData.get(i).getThumb() != null && mData.get(i).getThumb().size() > 0) {
                        mData.get(i).setSelfStatus(2);
                        chattingAdapter.notifyItemChanged(i);
                    }
                }
                break;
            case publishTheme_txt_TAG:
                for (int i = mData.size() - 1; i >= 0; i--) {
                    if (mData.get(i).getSelfStatus() == 1 && errorBean.key.equals(mData.get(i).getTerminal_id() + "")) {
                        mData.get(i).setSelfStatus(2);
                        chattingAdapter.notifyItemChanged(i);
                        break;
                    }
                }
                break;
            case publishTheme_pic_TAG:
                for (int i = mData.size() - 1; i >= 0; i--) {
                    if (mData.get(i).getSelfStatus() == 1 && errorBean.key.equals(mData.get(i).getTerminal_id() + "")) {
                        mData.get(i).setSelfStatus(2);
                        chattingAdapter.notifyItemChanged(i);
                        break;
                    }
                }
                for (InnerThemeListBean info : mData) {
                    if (info.getSelfStatus() == 1 && info.getThumb().size() > 0) {
                        realPublish(info);
                        break;
                    }
                }
                break;
            case publishTheme_audio_TAG:
                for (int i = mData.size() - 1; i >= 0; i--) {
                    if (mData.get(i).getSelfStatus() == 1 && errorBean.key.equals(mData.get(i).getTerminal_id() + "")) {
                        mData.get(i).setSelfStatus(2);
                        chattingAdapter.notifyItemChanged(i);
                        break;
                    }
                }
                for (InnerThemeListBean info : mData) {
                    if (info.getSelfStatus() == 1 && (info.getVideoType() == 2 || info.getVideoType() == 3)) {
                        realPublish(info);
                        break;
                    }
                }
                break;
            case themeListmore_TAG:
                chattingAdapter.removeLoading();
                mBinding.chatrootview.loadFinish();
                break;
        }
    }

    @Override
    public void progress(int precent, int tag) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_Normal_CODE) {
            selectPics.clear();
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    selectPics.addAll(data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS));
                }
            } else {
                selectPics.addAll(backupSelectPics);
            }
            if (selectPics.size() < ChatSelectPicAdapter.maxPics) {
                selectPics.add(addButton);
            }
            picAdapter.notifyDataSetChanged();
            updataPicsView();
            updataPicMap(selectPics, uploadpicone_TAG);

        } else if (requestCode == AT_request) {
            if (resultCode == RESULT_OK) {
                List<MemberAllBean.DataBean> list = (List<MemberAllBean.DataBean>) data.getSerializableExtra("atmemberlist");
                if (list.size() > 0) {
                    mBinding.editContent.getText().delete(mBinding.editContent.getText().length() - 1, mBinding.editContent.getText().length());
                    for (MemberAllBean.DataBean bean : list) {
                        mBinding.editContent.addAtSpan("@", bean.getUser_name(), bean.getUser_id(), false);
                        mBinding.editContent.getText().insert(mBinding.editContent.getText().length(), " ");
                    }
                }
            }
        }
    }

    private void updataUnreadUpView() {
        if (unreadUp > 0) {
            mBinding.flayoutChatUnreadUp.setVisibility(View.VISIBLE);
            mBinding.txtChatUnreadUp.setText(unreadUp + " 未读观点");
            mBinding.flayoutChatUnreadUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBinding.chatrootview.loadStart();
                    mBinding.recycleChatting.scrollToPosition(0);
                    loadMore();

                }
            });
        } else {
            mBinding.flayoutChatUnreadUp.setVisibility(View.GONE);
        }
    }

    //选择后上传图片
    private boolean updataPicMapReal(List<String> upList, long terminal_id, int tag) {
        boolean isFinish = true;
        for (String local : upList) {
            if (!addButton.equals(local)) {
                String web = picMap.get(local);
                if (TextUtils.isEmpty(web)) {
                    isFinish = false;
                    uploadPicOneReal(local, terminal_id, tag);
                    break;
                }
            }
        }
        return isFinish;
    }

    //选择后上传图片
    private boolean updataPicMap(List<String> upList, int tag) {
        boolean isFinish = true;
        for (String local : upList) {
            if (!addButton.equals(local)) {
                String web = picMap.get(local);
                if (TextUtils.isEmpty(web)) {
                    isFinish = false;
                    uploadPicOne(local, tag);
                    break;
                }
            }
        }
        return isFinish;
    }

    //上传音视频
    private boolean updataAudioMap(int videoType, String videoPath, String coverPath, int duration) {
        boolean isFinish = true;
        VideoInfoPackage web = audioMap.get(videoPath);
        if (web == null) {
            web = new VideoInfoPackage(videoType, videoPath, coverPath, duration);
            audioMap.put(videoPath, web);
        }
        if (TextUtils.isEmpty(web.webUrl)) {
            isFinish = false;
            uploadVideoOne(videoType, videoPath, coverPath, duration);
        }
        return isFinish;
    }

    private void uploadVideoOne(int videoType, String videoPath, String coverPath, int duration) {
        TXUGCPublishTypeDef.TXPublishParam param = new TXUGCPublishTypeDef.TXPublishParam();
        // signature计算规则可参考 https://www.qcloud.com/document/product/266/9221
        param.signature = Constant.videoSecret;
        param.videoPath = videoPath;
        param.coverPath = coverPath;
        param.duration = duration;
        param.type = videoType;
        int publishCode = mVideoPublish.publishVideo(param);
        if (publishCode == 1012) {
            TXUGCPublishTypeDef.TXPublishResult result = new TXUGCPublishTypeDef.TXPublishResult();
            result.retCode = publishCode;
            result.localPath = videoPath;
            result.duration = duration;
            onPublishComplete(result);
        }
        LogUtil.i("publisherror", "发布失败，错误码：" + publishCode);
    }

    private void uploadPicOne(String localpath, int tag) {
        mDisposables.add(Flowable.just(localpath).map(new Function<String, File>() {
            @Override
            public File apply(String path) throws Exception {
                File newFile = new File(getExternalCacheDir(), "theme" + ".jpg");
                if (ImageUtil.compressBmpToFile(provideActivity(), path, newFile))
                    return newFile;
                else
                    return new File(path);
            }
        }).compose(RxUtil.fixScheduler()).subscribe(new Consumer<File>() {
            @Override
            public void accept(File file) throws Exception {
                startUploadOne(file, localpath, tag);
            }
        }));
    }

    private void uploadPicOneReal(String localpath, long terminal_id, int tag) {
        mDisposables.add(Flowable.just(localpath).map(new Function<String, File>() {
            @Override
            public File apply(String path) throws Exception {
                File newFile = new File(getExternalCacheDir(), "theme" + ".jpg");
                if (ImageUtil.compressBmpToFile(provideActivity(), path, newFile))
                    return newFile;
                else
                    return new File(path);
            }
        }).compose(RxUtil.fixScheduler()).subscribe(new Consumer<File>() {
            @Override
            public void accept(File file) throws Exception {
                startUploadOneReal(file, localpath, terminal_id, tag);
            }
        }));
    }

    private void updataPicsView() {
        if (selectPics.size() > 1) {
            mBinding.ivChatmenuPicEmptyadd.setVisibility(View.GONE);
            mBinding.llayoutChatmenuPicShow.setVisibility(View.VISIBLE);
        } else {
            mBinding.ivChatmenuPicEmptyadd.setVisibility(View.VISIBLE);
            mBinding.llayoutChatmenuPicShow.setVisibility(View.GONE);
        }
        if (addButton.equals(selectPics.get(selectPics.size() - 1))) {
            mBinding.txtChatmenuPicPoint.setText("长按图片拖动可以调整排序 (" + (selectPics.size() - 1) + "/" + ChatSelectPicAdapter.maxPics + ")");
            mBinding.txtChatPicnum.setVisibility(selectPics.size() > 1 ? View.VISIBLE : View.GONE);
            mBinding.txtChatPicnum.setText((selectPics.size() - 1) + "");
        } else {
            mBinding.txtChatmenuPicPoint.setText("长按图片拖动可以调整排序 (" + selectPics.size() + "/" + ChatSelectPicAdapter.maxPics + ")");
            mBinding.txtChatPicnum.setVisibility(View.VISIBLE);
            mBinding.txtChatPicnum.setText(selectPics.size() + "");
        }
        updataSendButton();
    }

    private void updataVideoView() {
        if (selectVideos.size() > 0) {
            mBinding.ivChatmenuVideoEmptyadd.setVisibility(View.GONE);
            mBinding.fllayoutVideopath.setVisibility(View.VISIBLE);
        } else {
            mBinding.ivChatmenuVideoEmptyadd.setVisibility(View.VISIBLE);
            mBinding.fllayoutVideopath.setVisibility(View.GONE);
        }
        mBinding.txtChatVideonum.setVisibility(selectVideos.size() > 0 ? View.VISIBLE : View.GONE);
        mBinding.txtChatVideonum.setText(selectVideos.size() + "");

        updataSendButton();
    }

    //更新发送按钮状态
    private void updataSendButton() {
        if (mBinding.editContent.getEditableText().toString().length() > 0 || selectPics.size() > 1 || selectVideos.size() > 0) {
            mBinding.ivChatSend.setSelected(true);
            if (selectPics.size() <= 1 && selectVideos.size() == 0) {
                mBinding.ivChatAdd.setAlpha(1.0f);
                mBinding.ivChatAddVideo.setAlpha(1.0f);
            } else {
                mBinding.ivChatAdd.setAlpha(selectVideos.size() > 0 ? 0.35f : 1.0f);
                mBinding.ivChatAddVideo.setAlpha(selectPics.size() > 1 ? 0.35f : 1.0f);
            }
        } else {
            mBinding.ivChatSend.setSelected(false);
            mBinding.ivChatAdd.setAlpha(1.0f);
            mBinding.ivChatAddVideo.setAlpha(1.0f);
        }
    }

    @Override
    protected void onDestroy() {
        if (!isOtherClose)
            HomeFragment.resumeTopicid = 0;
        if (mVideoPublish != null) {
            mVideoPublish.canclePublish();
            mVideoPublish.setListener(null);
        }
        chattingAdapter.releaseMediaPlayer();
        mBinding.buttonRecord.releaseRecorder();
        picMap.clear();
        audioMap.clear();
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_hot_theme:
                if (label == 1) {
                    refresh();
                } else {
                    refreshHot();
                }
                break;

            case R.id.iv_chat_click:
                mBinding.editContent.getText().insert(mBinding.editContent.getText().length(), "@");
                break;
            case R.id.iv_chat_first_splash:
                hideFirstSplash();
                break;
            case R.id.flayout_chat_unread_down:
                unreadDown = 0;
                mBinding.recycleChatting.scrollToPosition(chattingAdapter.getItemCount() - 1);
                mBinding.flayoutChatUnreadDown.setVisibility(View.GONE);
                break;
            case R.id.llayout_topicmember_info:
                if (mBinding.llayoutChatMembers.getVisibility() == View.VISIBLE) {
                    mBinding.llayoutChatMembers.setVisibility(View.GONE);
                    mBinding.barIvPeopleArrow.setSelected(false);
                } else {
                    mBinding.barIvPeopleArrow.setSelected(true);
                    if (membersAdapter == null) {
                        mPresenter.getTopicMembers(topicId, 3, last_thread_id, Members_TAG);
                    }
                    mBinding.llayoutChatMembers.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.llayout_chat_members:
                mBinding.llayoutChatMembers.setVisibility(View.GONE);
                mBinding.barIvPeopleArrow.setSelected(false);
                break;
            case R.id.bar_left_click:
                finish();
                break;
            case R.id.iv_chat_emoji:
                showOrHideEmoji();
                break;
            case R.id.iv_emoji_delete:
                int keyCode = KeyEvent.KEYCODE_DEL;
                KeyEvent keyEventDown = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
                KeyEvent keyEventUp = new KeyEvent(KeyEvent.ACTION_UP, keyCode);
                mBinding.editContent.onKeyDown(keyCode, keyEventDown);
                mBinding.editContent.onKeyUp(keyCode, keyEventUp);
                break;
            case R.id.flayout_chat_video:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (selectPics.size() <= 1)
                        showAddVideoView();
                } else {
                    ToastUtil.showToast("请升级到安卓5.0以上");
                }
                break;
            case R.id.iv_chatmenu_video_emptyadd:
                Intent intent1 = new Intent(provideActivity(), TCVideoChooseActivity.class);
                startActivity(intent1);
                break;
            case R.id.iv_chat_videopath_del:
                selectVideos.clear();
                updataVideoView();
                break;
            case R.id.iv_chat_videopath_preview:
                if (selectVideos.size() > 0) {
                    Intent intent = new Intent(provideActivity(), NewVodPlayerActivity.class);
                    intent.putExtra("frompath", 4);
                    intent.putExtra("videopath", selectVideos.get(0));
                    intent.putExtra("coverpath", selectVideos.get(0));
                    startActivity(intent);
                }
                break;
            case R.id.iv_chat_add:
                if (selectVideos.size() == 0)
                    showAddPicsView();
                break;
            case R.id.flayout_chat_send:
                sendPublishButton();
                break;
            case R.id.iv_chatmenu_pic_emptyadd:
                selectPicsFromPhotos();
                break;
            case R.id.bar_right_click:
                Intent intent = new Intent(provideActivity(), TopicDeatilSelfActivity.class);
                intent.putExtra("topicid", topicId + "");
                intent.putExtra("fromchat", true);
                startActivity(intent);
                break;
            case R.id.iv_chat_record:
                new RxPermissions(this).request(Manifest.permission.RECORD_AUDIO).subscribe(aBoolean -> {
                    if (aBoolean) {
                        hideFirstSplash();
                        hideKeyboard();
                        mBinding.editContent.clearFocus();
                        mBinding.llayoutAttachFunction.setVisibility(View.GONE);
                        mBinding.llayoutTxt.setVisibility(View.GONE);
                        mBinding.flayoutRecord.setVisibility(View.VISIBLE);
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mBinding.recycleChatting.scrollToPosition(chattingAdapter.getItemCount() - 1);
                            }
                        }, 50);
                    } else {
                        ToastUtil.showToast("请开启录音权限");
                    }
                });

                break;
            case R.id.iv_chat_keyboard:
                mBinding.flayoutRecord.setVisibility(View.GONE);
                mBinding.llayoutTxt.setVisibility(View.VISIBLE);
                break;

        }
    }

    private void refreshHot() {
        mPresenter.getThemeList(topicId, null, 0, 1, themeList_HOT_TAG);
    }

    private void showAddVideoView() {
        hideKeyboard();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mBinding.flayoutChatVideoRoot.getVisibility() == View.GONE) {
                    mBinding.llayoutChatEmojiRoot.setVisibility(View.GONE);
                    mBinding.ivChatEmoji.setSelected(false);
                    mBinding.flayoutChatPicRoot.setVisibility(View.GONE);
                    mBinding.ivChatAdd.setSelected(false);
                    mBinding.ivChatAdd.setAlpha(selectVideos.size() > 0 ? 0.35f : 1.0f);

                    mBinding.flayoutChatVideoRoot.setVisibility(View.VISIBLE);
                    mBinding.ivChatAddVideo.setSelected(true);
                    mBinding.ivChatAddVideo.setAlpha(1.0f);

                    mBinding.ivChatmenuVideoEmptyadd.setVisibility(selectVideos.size() > 0 ? View.GONE : View.VISIBLE);
                    mBinding.fllayoutVideopath.setVisibility(selectVideos.size() > 0 ? View.VISIBLE : View.GONE);
                    if (selectVideos.size() == 0) {
                        Intent intent1 = new Intent(provideActivity(), TCVideoChooseActivity.class);
                        startActivity(intent1);
                    }
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mBinding.recycleChatting.scrollToPosition(chattingAdapter.getItemCount() - 1);
                        }
                    }, 50);
                } else {
                    mBinding.flayoutChatVideoRoot.setVisibility(View.GONE);
                    mBinding.ivChatAddVideo.setSelected(false);
                    mBinding.ivChatAddVideo.setAlpha(1.0f);
                }
            }
        }, 50);


    }

    private void hideFirstSplash() {
        if (SharedPrefUtil.getBoolean(Constant.firstchat_key, true)) {
            mBinding.ivChatFirstSplash.setVisibility(View.GONE);
            SharedPrefUtil.putBoolean(Constant.firstchat_key, false);
        }
    }

    private void showOrHideEmoji() {
        hideKeyboard();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mBinding.llayoutChatEmojiRoot.getVisibility() == View.VISIBLE) {
                    mBinding.llayoutChatEmojiRoot.setVisibility(View.GONE);
                    mBinding.ivChatEmoji.setSelected(false);
                } else {
                    mBinding.flayoutChatPicRoot.setVisibility(View.GONE);
                    mBinding.ivChatAdd.setSelected(false);
                    mBinding.flayoutChatVideoRoot.setVisibility(View.GONE);
                    mBinding.ivChatAddVideo.setSelected(false);
                    mBinding.llayoutChatEmojiRoot.setVisibility(View.VISIBLE);
                    mBinding.ivChatEmoji.setSelected(true);
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mBinding.recycleChatting.scrollToPosition(chattingAdapter.getItemCount() - 1);
                        }
                    }, 50);
                }
            }
        }, 50);

    }

    private void clickEditView() {
        hideFirstSplash();

        mBinding.llayoutAttachFunction.setVisibility(View.VISIBLE);
        mBinding.flayoutChatPicRoot.setVisibility(View.GONE);
        mBinding.ivChatAdd.setSelected(false);
        mBinding.flayoutChatVideoRoot.setVisibility(View.GONE);
        mBinding.ivChatAddVideo.setSelected(false);
        mBinding.llayoutChatEmojiRoot.setVisibility(View.GONE);
        mBinding.ivChatEmoji.setSelected(false);
        showKeyboard(mBinding.editContent);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mBinding.recycleChatting.scrollToPosition(chattingAdapter.getItemCount() - 1);
            }
        }, 200);

    }

    private void selectPicsFromPhotos() {
        selectPics.remove(addButton);
        backupSelectPics.clear();
        backupSelectPics.addAll(selectPics);
        PhotoPicker.builder()
                .setPhotoCount(9)
                .setShowCamera(true)
                .setSelected(selectPics)
                .setPreviewEnabled(true)
                .start(provideActivity(), PhotoPicker.REQUEST_CODE);
    }

    private void showAddPicsView() {
        hideKeyboard();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mBinding.flayoutChatPicRoot.getVisibility() == View.GONE) {
                    mBinding.llayoutChatEmojiRoot.setVisibility(View.GONE);
                    mBinding.ivChatEmoji.setSelected(false);
                    mBinding.flayoutChatVideoRoot.setVisibility(View.GONE);
                    mBinding.ivChatAddVideo.setSelected(false);
                    mBinding.ivChatAddVideo.setAlpha(selectPics.size() > 1 ? 0.35f : 1.0f);

                    mBinding.flayoutChatPicRoot.setVisibility(View.VISIBLE);
                    mBinding.ivChatAdd.setSelected(true);
                    mBinding.ivChatAdd.setAlpha(1.0f);
                    mBinding.ivChatmenuPicEmptyadd.setVisibility(selectPics.size() > 1 ? View.GONE : View.VISIBLE);
                    mBinding.llayoutChatmenuPicShow.setVisibility(selectPics.size() > 1 ? View.VISIBLE : View.GONE);
                    if (selectPics.size() <= 1)
                        selectPicsFromPhotos();
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mBinding.recycleChatting.scrollToPosition(chattingAdapter.getItemCount() - 1);
                        }
                    }, 50);
                } else {
                    mBinding.flayoutChatPicRoot.setVisibility(View.GONE);
                    mBinding.ivChatAdd.setSelected(false);
                    mBinding.ivChatAdd.setAlpha(1.0f);
                }
            }
        }, 50);


    }

    private void publishPicView() {

    }

    //预览图片
    private void previewPics(int position) {
        selectPics.remove(addButton);
        backupSelectPics.clear();
        backupSelectPics.addAll(selectPics);
        PhotoPreview.builder()
                .setPhotos(selectPics)
                .setCurrentItem(position)
                .start(provideActivity());
    }

    /**
     * 隐藏软键盘
     */
    protected void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    protected void showKeyboard(EditText et) {
        et.requestFocus();
        ((InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(NoticeChangeBean.DataBean e) {
        if (e.getTypeX() == 19) {//19：主题新增
           /* StringBuffer ids = new StringBuffer();
            for (List<Integer> id : e.getIds()) {
                if (id.size() >= 2) {
                    ids.append(id.get(1));
                    ids.append(",");
                }
            }
            if (ids.length() > 0) {
                ids.deleteCharAt(ids.length() - 1);
                addNewTheme(ids.toString());
            }*/
            addNewTheme("");
        } else if (e.getTypeX() == 26 || e.getTypeX() == 22 || e.getTypeX() == 24) {//扩展信息变更，回复信息变更
            StringBuffer ids = new StringBuffer();
            for (List<Integer> id : e.getIds()) {
                if (id.size() >= 2) {
                    ids.append(id.get(1));
                    ids.append(",");
                }
            }
            if (ids.length() > 0) {
                ids.deleteCharAt(ids.length() - 1);
                notifyTheme(ids.toString());
            }
        } else if (e.getTypeX() == 21) {//主题删除
            for (List<Integer> id : e.getIds()) {
                if (id.size() >= 2) {
                    for (int i = 0; i < mData.size(); i++) {
                        if (mData.get(i).getId() == id.get(1)) {
                            mData.remove(i);
                            chattingAdapter.notifyItemRemoved(i);
                            /*if (sendInPostion >= 0 && sendInPostion > i)
                                sendInPostion--;*/
                            if (selectPosition >= 0 && selectPosition > i)
                                selectPosition--;
                            if (selectPosition >= 0 && selectPosition == i)
                                selectPosition = -1;

                            break;
                        }

                    }

                }
            }
            showEmptyView();
        }
    }

    private void notifyTheme(String themeIds) {
        mPresenter.getThemeList(topicId, themeIds, 0, 0, ThemeNotify_TAG);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(OutTopicBus e) {
        if (e.isOutSuccess)
            finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BackTopicBus e) {
//        if (!topicId.equals((e.topicId + ""))) {
        isOtherClose = true;
        finish();
//        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(VideoSelectBus e) {
        if (e.isFinish) {
            selectVideos.clear();
            if (!TextUtils.isEmpty(e.videoPath)) {
                videoDuration = e.duration;
                selectVideos.add(e.videoPath);
                if (!e.videoPath.startsWith("http"))
                    GlideApp.with(this).load(Uri.fromFile(new File(e.videoPath))).centerCrop().into(mBinding.ivChatVideopath);
                else
                    GlideApp.with(this).load(e.videoPath).centerCrop().into(mBinding.ivChatVideopath);
            }
            updataVideoView();
        }
    }

    private void showEmptyView() {
        if (mData.size() > 0) {
            mBinding.llayoutEmpty.setVisibility(View.GONE);
        } else {
            mBinding.llayoutEmpty.setVisibility(View.VISIBLE);
            mBinding.txtEmptyDes.setText(label == 1 ? "暂无热帖" : "来当第一个发表观点的人吧");
        }
    }

    private boolean isSoftShowing() {
        //获取当前屏幕内容的高度
        int screenHeight = getWindow().getDecorView().getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return screenHeight - rect.bottom - getSoftButtonsBarHeight() >= 128;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private int getSoftButtonsBarHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        //这个方法获取可能不是真实屏幕的高度
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        //获取当前屏幕的真实高度
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        } else {
            return 0;
        }
    }

    @Override
    public void onBackPressed() {
        if (chattingAdapter.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected boolean adapterFitSystemWindows() {
        return true;
    }
}
