package bbs.com.xinfeng.bbswork.ui.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.base.App;
import bbs.com.xinfeng.bbswork.base.BaseActivity;
import bbs.com.xinfeng.bbswork.base.BaseRvAdapter;
import bbs.com.xinfeng.bbswork.base.Constant;
import bbs.com.xinfeng.bbswork.databinding.ActivityThemeDetailBinding;
import bbs.com.xinfeng.bbswork.databinding.ReplyDetailHeadBinding;
import bbs.com.xinfeng.bbswork.databinding.ThemeDetailHeadBinding;
import bbs.com.xinfeng.bbswork.domin.AttachThemeBean;
import bbs.com.xinfeng.bbswork.domin.BackTopicBus;
import bbs.com.xinfeng.bbswork.domin.CollectionThemeBus;
import bbs.com.xinfeng.bbswork.domin.DeleteReplyBus;
import bbs.com.xinfeng.bbswork.domin.DeleteThemeBus;
import bbs.com.xinfeng.bbswork.domin.ErrorBean;
import bbs.com.xinfeng.bbswork.domin.InnerThemeListBean;
import bbs.com.xinfeng.bbswork.domin.MemberAllBean;
import bbs.com.xinfeng.bbswork.domin.MesReduceBus;
import bbs.com.xinfeng.bbswork.domin.NoticeChangeBean;
import bbs.com.xinfeng.bbswork.domin.PublishReplyBean;
import bbs.com.xinfeng.bbswork.domin.SendReplyListInfo;
import bbs.com.xinfeng.bbswork.domin.ThemeDetailReplyBean;
import bbs.com.xinfeng.bbswork.domin.ThemeListBean;
import bbs.com.xinfeng.bbswork.domin.UploadBean;
import bbs.com.xinfeng.bbswork.domin.VideoInfoPackage;
import bbs.com.xinfeng.bbswork.module.GlideApp;
import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.presenter.ThemeDetailPresenter;
import bbs.com.xinfeng.bbswork.ui.adapter.ChatPicsAdapter;
import bbs.com.xinfeng.bbswork.ui.adapter.ChatSelectPicAdapter;
import bbs.com.xinfeng.bbswork.ui.adapter.ReplyTwoAdapter;
import bbs.com.xinfeng.bbswork.ui.adapter.ThemeDetailReplyAdapter;
import bbs.com.xinfeng.bbswork.ui.adapter.layoutmanager.ChatPicsTouchHelperCallback;
import bbs.com.xinfeng.bbswork.ui.fragment.HomeFragment;
import bbs.com.xinfeng.bbswork.utils.ArmsUtils;
import bbs.com.xinfeng.bbswork.utils.FormatUtils;
import bbs.com.xinfeng.bbswork.utils.ImageUtil;
import bbs.com.xinfeng.bbswork.utils.LogUtil;
import bbs.com.xinfeng.bbswork.utils.RxUtil;
import bbs.com.xinfeng.bbswork.utils.SharedPrefUtil;
import bbs.com.xinfeng.bbswork.utils.SpanUtil;
import bbs.com.xinfeng.bbswork.utils.ToastUtil;
import bbs.com.xinfeng.bbswork.utils.emoji.EmojiUtil;
import bbs.com.xinfeng.bbswork.utils.shortvideo.videoplay.NewVodPlayerActivity;
import bbs.com.xinfeng.bbswork.videoupload.TXUGCPublish;
import bbs.com.xinfeng.bbswork.videoupload.TXUGCPublishTypeDef;
import bbs.com.xinfeng.bbswork.widget.InterruptRv;
import bbs.com.xinfeng.bbswork.widget.ReplyTwoSlideView;
import bbs.com.xinfeng.bbswork.widget.imageShow.ShowImagesDialog;
import bbs.com.xinfeng.bbswork.widget.popwindow.ChatRepublishPoppubWindow;
import bbs.com.xinfeng.bbswork.widget.popwindow.QPopuWindow;
import bbs.com.xinfeng.bbswork.widget.popwindow.ThemeDetailPoppubWindow;
import bbs.com.xinfeng.bbswork.widget.recordvoice.RecordVoiceButton;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
//import linwg.ImageBrowser;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

import static android.view.KeyEvent.KEYCODE_BACK;
import static bbs.com.xinfeng.bbswork.base.Constant.head_key;
import static bbs.com.xinfeng.bbswork.base.Constant.name_key;
import static bbs.com.xinfeng.bbswork.base.Constant.userid_key;
import static bbs.com.xinfeng.bbswork.ui.activity.ChattingActivity.addButton;
import static bbs.com.xinfeng.bbswork.widget.popwindow.ThemeDetailPoppubWindow.THEMEREPLY_TYPE;
import static bbs.com.xinfeng.bbswork.widget.popwindow.ThemeDetailPoppubWindow.THEME_TYPE;

public class ThemeDetailActivity extends BaseActivity<ActivityThemeDetailBinding, ThemeDetailPresenter> implements NetContract.INetView, View.OnClickListener, TXUGCPublishTypeDef.ITXVideoPublishListener {
    private static final int themeReplyList_LandLord_Tag = 297;
    private static final int themeReplyList_refresh_Tag = 298;
    private static final int themeReplyList_refresh_hot_Tag = 299;
    private static final int themeReplyList_Tag = 300;
    private static final int themeDetail_Tag = 301;
    private static final int Like_TAG = 302;
    private static final int Collect_TAG = 303;
    private static final int Publish_TAG = 304;
    private static final int DeleteReply_Tag = 305;
    private static final int DeleteTheme_Tag = 306;
    private static final int ReplyLike_Tag = 307;
    private static final int ReplyTwoLike_Tag = 308;

    private static final int ReplyTwoList_Tag = 310;
    private static final int PublishTwo_Tag = 311;
    private static final int replyDetail_Tag = 312;
    private static final int DeleteReplyTwo_Tag = 313;
    private static final int Jpush_ReplyDeatil_Tag = 314;
    private static final int ReadTheme_TAG = 315;
    private static final int PublishPic_TAG = 316;
    private static final int PublishPicTwo_TAG = 317;

    private final int uploadpicone_TAG = 500;
    private final int uploadpicone_realSend_TAG = 501;

    private Handler mHandler = new Handler();
    private int AT_request = 10;
    private int AT_request_pop = 11;
    private List<ThemeDetailReplyBean.DataBean> mDatas = new ArrayList<>();
    private List<ThemeDetailReplyBean.DataBean> mDatasTwo = new ArrayList<>();
    private int topicid;
    private int themeid;
    private ThemeDetailReplyAdapter mAdapter;
    private ReplyTwoAdapter mAdapterTwo;
    private ThemeDetailHeadBinding headBinding;
    private ReplyDetailHeadBinding headBindingTwo;
    private InnerThemeListBean detailInfo;

    private int pid;//分页加载
    private int pidTwo;//二级回复分页
    private ThemeDetailPoppubWindow replyPop;
    private ThemeDetailPoppubWindow replyPopTwo;

    //    private int selectPostion;//选择到的position
    private int selectPostionTwo;//选择到二级回复的position
    private boolean isDelete;
    private ValueAnimator inAnimator;
    private ValueAnimator outAnimator;
    private int replyPostionOne = -1;
    private ThemeDetailReplyBean.DataBean jpushReplyDetail;//极光推送来显示二级回复使用

    private final MediaPlayer mp = new MediaPlayer();
    private AnimationDrawable mVoiceAnimation;
    private boolean mSetData = false;
    private TXUGCPublish mVideoPublish;

    private ArrayList<String> selectPics = new ArrayList<>();
    private ArrayList<String> backupSelectPics = new ArrayList<>();//备份，防止选择图片时，减掉图片后返回图片减少
    private ChatSelectPicAdapter picAdapter;
    private HashMap<String, String> picMap = new HashMap();
    private HashMap<String, VideoInfoPackage> audioMap = new HashMap();
    private ShowImagesDialog imagesDialog;
    private ChatRepublishPoppubWindow republishPop;
    private int rawX;
    private int rawY;
    private int label;
    private int lastPostId;

    @Override
    protected ThemeDetailPresenter creatPresenter() {
        return new ThemeDetailPresenter(this);
    }

    @Override
    protected int initView(Bundle savedInstanceState) {
        return R.layout.activity_theme_detail;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        topicid = getIntent().getIntExtra("topicid", 0);
        themeid = getIntent().getIntExtra("themeid", 0);

        mBinding.barLeftClick.setVisibility(View.VISIBLE);

        mBinding.barLeftClick.setOnClickListener(v -> finish());
        imagesDialog = new ShowImagesDialog(provideActivity());
        imagesDialog.setHasSave(true);
        initRecord();
        initAdapter();
        initAdapterPics();
        mPresenter.getThemeDetail(topicid, themeid, themeDetail_Tag);
        EmojiUtil emojiUtil = new EmojiUtil();
        emojiUtil.setSelfDrawable(getApplicationContext(), mBinding.vpEmoji, mBinding.llDot, mBinding.editContent, 0);
        if (getIntent().getBooleanExtra("showkeyboard", false))
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showKeyboard(mBinding.editContent);
                }
            }, 100);

        initDataTwo();
        int replyid = getIntent().getIntExtra("replyid", 0);
        if (replyid > 0) {
            mPresenter.getReplyDetail(themeid, replyid, Jpush_ReplyDeatil_Tag);
        }
        EventBus.getDefault().post(new MesReduceBus(getIntent().getIntExtra("jpushtype", 0), themeid));
    }

    private void initRecordTwo() {
        if (mVideoPublish == null) {
            mVideoPublish = new TXUGCPublish(provideActivity().getApplicationContext(), "blackbean");
            mVideoPublish.setListener(this);
        }
        mBinding.replyRoot.buttonRecord.setRecordListener(new RecordVoiceButton.OnRecordVoiceListener() {
            @Override
            public void onRecordFinished(int duration, String path) {
                if (replyPostionOne > 0) {
                    realPublish(readyPublishVideo(2, 2, path, duration, mDatas.get(replyPostionOne - 1).getId() + ""));
                } else if (jpushReplyDetail != null) {
                    realPublish(readyPublishVideo(2, 2, path, duration, jpushReplyDetail.getId() + ""));
                }
            }

            @Override
            public void onStartRecord() {
                stopMediaPlayer();
                mAdapterTwo.stopMediaPlayer();
            }
        });
        mBinding.replyRoot.buttonRecord.setFilePath(Constant.STORAGE_PATH + "/voice");
//        initMediaPlayer();
    }

    private void initRecord() {
        if (mVideoPublish == null) {
            mVideoPublish = new TXUGCPublish(provideActivity().getApplicationContext(), "blackbean");
            mVideoPublish.setListener(this);
        }
        mBinding.buttonRecord.setRecordListener(new RecordVoiceButton.OnRecordVoiceListener() {
            @Override
            public void onRecordFinished(int duration, String path) {
                realPublish(readyPublishVideo(1, 2, path, duration, null));
//                readySendAudio(1, path, duration, null);
            }

            @Override
            public void onStartRecord() {
                stopMediaPlayer();
                mAdapter.stopMediaPlayer();
            }
        });
        mBinding.buttonRecord.setFilePath(Constant.STORAGE_PATH + "/voice");
        initMediaPlayer();
    }

    private void initMediaPlayer() {

        mp.setAudioStreamType(AudioManager.STREAM_RING);
        mp.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                if (mVoiceAnimation != null) {
                    mVoiceAnimation.stop();
                    mVoiceAnimation = null;
                }
                if (headBinding != null)
                    headBinding.voiceIv.setImageResource(R.drawable.jmui_send_3);
                if (headBindingTwo != null)
                    headBindingTwo.voiceIv.setImageResource(R.drawable.jmui_send_3);
                ToastUtil.showToast("播放失败");
                return false;
            }
        });

    }

    @Override
    public void onPublishProgress(long uploadBytes, long totalBytes) {

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
                    videoInfoPackage.webUrl = "v;" + result.videoURL + ";" + result.coverURL + ";" + result.duration + ";1;1";
                    audioMap.put(result.localPath, videoInfoPackage);
                }
            }
        } else {
//            if (result.retCode != TVCConstants.ERR_CLIENT_BUSY && result.retCode != TVCConstants.ERR_UGC_PUBLISHING)
            if (result.level == 1) {
                for (int i = mDatas.size() - 1; i >= 0; i--) {
                    if (mDatas.get(i).getSelfStatus() == 1 && result.localPath.equals(mDatas.get(i).getVideoPath())) {
                        mDatas.get(i).setSelfStatus(2);
                        mAdapter.refreshNotifyItemChanged(i);
                        break;
                    }
                }
            } else if (result.level == 2) {
                for (int i = mDatasTwo.size() - 1; i >= 0; i--) {
                    if (mDatasTwo.get(i).getSelfStatus() == 1 && result.localPath.equals(mDatasTwo.get(i).getVideoPath())) {
                        mDatasTwo.get(i).setSelfStatus(2);
                        mAdapterTwo.refreshNotifyItemChanged(i);
                        break;
                    }
                }
            }

        }
        if (result.level == 1) {
            for (int i = mDatas.size() - 1; i >= 0; i--) {
                if (mDatas.get(i).getSelfStatus() == 1 && result.localPath.equals(mDatas.get(i).getVideoPath())) {
                    realPublish(mDatas.get(i));
                    break;
                }
            }
        } else if (result.level == 2) {
            for (int i = mDatasTwo.size() - 1; i >= 0; i--) {
                if (mDatasTwo.get(i).getSelfStatus() == 1 && result.localPath.equals(mDatasTwo.get(i).getVideoPath())) {
                    realPublish(mDatasTwo.get(i));
                    break;
                }
            }
        }


        LogUtil.i("publishcomplete", result.retCode + " Msg:" + (result.retCode == 0 ? result.videoURL : result.descMsg));
    }


    private void initDataTwo() {
        mBinding.replyRoot.llayoutRoot.attachRecycleview(mBinding.replyRoot.recyclerReply, new ReplyTwoSlideView.OnLoadingListening() {

            @Override
            public void onSlide(float currentY, boolean isUp) {
                LogUtil.i("themedetialaaaaa", currentY + "__" + isUp);
                if (isUp) {
                    openReplyView(false, currentY < 0 ? 0 : currentY, true);
                } else {
                    closeReplyView(currentY);
                }
            }
        });
        mBinding.replyRoot.llayoutRoot.setOnClickListener(this);
        mBinding.replyRoot.barLeftClick.setVisibility(View.VISIBLE);
        mBinding.replyRoot.barLeftClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                mBinding.replyRoot.llayoutChatEmojiRoot.setVisibility(View.GONE);
                mBinding.replyRoot.ivChatEmoji.setSelected(false);
                closeReplyView(0);

            }
        });
        initRecordTwo();
        initAdapterTwo();
        addHeaderTwo();
        EmojiUtil emojiUtil = new EmojiUtil();
        emojiUtil.setSelfDrawable(provideActivity(), mBinding.replyRoot.vpEmoji, mBinding.replyRoot.llDot, mBinding.replyRoot.editContent, 0);
        initEventTwo();
    }


    private void initEventTwo() {
        mBinding.replyRoot.ivChatEmoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOrHideEmojiTwo();
            }
        });
        mBinding.replyRoot.ivChatSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPublishButtonTwo();
            }
        });
        mBinding.replyRoot.ivEmojiDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int keyCode = KeyEvent.KEYCODE_DEL;
                KeyEvent keyEventDown = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
                KeyEvent keyEventUp = new KeyEvent(KeyEvent.ACTION_UP, keyCode);
                mBinding.replyRoot.editContent.onKeyDown(keyCode, keyEventDown);
                mBinding.replyRoot.editContent.onKeyUp(keyCode, keyEventUp);
            }
        });
        mBinding.replyRoot.editContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    clickEditViewTwo();
                }
                return false;
            }
        });
        mBinding.replyRoot.editContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (before == 0 && s.toString().length() > 0 && s.toString().substring(s.length() - 1).equals("@")) {
                    Intent intent = new Intent(provideActivity(), AtMemberListActivity.class);
                    intent.putExtra("topicid", detailInfo.getTopic_id() + "");
                    startActivityForResult(intent, AT_request_pop);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                updataSendButtonTwo();

            }
        });
        mBinding.replyRoot.ivChatRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RxPermissions(provideActivity()).request(Manifest.permission.RECORD_AUDIO).subscribe(aBoolean -> {
                    if (aBoolean) {
                        hideKeyboard();
                        mBinding.replyRoot.editContent.clearFocus();
//                        mBinding.replyRoot.llayoutAttachFunction.setVisibility(View.GONE);
                        mBinding.replyRoot.llayoutTxt.setVisibility(View.GONE);
                        mBinding.replyRoot.flayoutRecord.setVisibility(View.VISIBLE);
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mBinding.replyRoot.recyclerReply.scrollToPosition(mAdapterTwo.getItemCount() - 1);
                            }
                        }, 50);

                    } else {
                        ToastUtil.showToast("请开启录音权限");
                    }
                });
            }
        });
        mBinding.replyRoot.ivChatKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.replyRoot.flayoutRecord.setVisibility(View.GONE);
                mBinding.replyRoot.llayoutTxt.setVisibility(View.VISIBLE);
            }
        });
        mBinding.replyRoot.ivChatClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.replyRoot.editContent.getText().insert(mBinding.replyRoot.editContent.getText().length(), "@");
            }
        });
    }

    private void addHeaderTwo() {
        View headView = getLayoutInflater().inflate(R.layout.reply_detail_head, (ViewGroup) mBinding.replyRoot.recyclerReply.getParent(), false);
        headBindingTwo = DataBindingUtil.bind(headView);
        mAdapterTwo.addHeaderView(headView);
    }

    private void initAdapterTwo() {
        mAdapterTwo = new ReplyTwoAdapter(mDatasTwo, mp);
        mAdapterTwo.setEnableLoadMore(false);
        mAdapterTwo.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMoreTwo("");
            }
        });
        mAdapterTwo.setmListen(new ReplyTwoAdapter.onItemOperateListen() {
            @Override
            public void zanOperate(int position) {
                mPresenter.likeOperate(mDatasTwo.get(position - 1).getThread_id(), mDatasTwo.get(position - 1).getIslike(), mDatasTwo.get(position - 1).getId(), ReplyTwoLike_Tag);
                if (mDatasTwo.get(position - 1).getIslike() == 1) {
                    mDatasTwo.get(position - 1).setIslike(0);
                    try {
                        mDatasTwo.get(position - 1).setLikecn((Integer.parseInt(mDatasTwo.get(position - 1).getLikecn()) - 1) + "");
                    } catch (Exception e) {
                    }
                    ToastUtil.showToast("取消点赞");
                } else {
                    mDatasTwo.get(position - 1).setIslike(1);
                    try {
                        mDatasTwo.get(position - 1).setLikecn((Integer.parseInt(mDatasTwo.get(position - 1).getLikecn()) + 1) + "");
                    } catch (Exception e) {
                    }
                    ToastUtil.showToast("点赞成功");
                }
                mAdapterTwo.notifyItemChanged(position);
            }

            @Override
            public void operateItem(int position) {
                if (isPreventGoTwo()) {
                    Intent intent = new Intent(provideActivity(), UserInfoActivity.class);
                    intent.putExtra("userid", mDatasTwo.get(position - 1).getAuthor_userid());
                    startActivity(intent);
                }
            }

            @Override
            public void moreClick(int position) {
                showOperatePopTwo(position, THEMEREPLY_TYPE);
            }

            @Override
            public void clickAudio(int position) {
                if (mp.isPlaying() && mVoiceAnimation != null)
                    stopMediaPlayer();
                if (!mDatasTwo.get(position - 1).isSelf() && mDatasTwo.get(position - 1).getAudioread() != 1) {
                    mPresenter.readThemeAudio(themeid, mDatasTwo.get(position - 1).getId(), ReadTheme_TAG);
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

                        mAdapterTwo.remove(position - 1);
                    }

                    @Override
                    public void onRepublish() {
                        mDatasTwo.get(position - 1).setSelfStatus(1);
                        mAdapterTwo.refreshNotifyItemChanged(position - 1);
                        realPublish(mDatasTwo.get(position - 1));

                    }
                });
                republishPop.show();
            }

            @Override
            public void atPerson(int position) {
                showKeyboardTwo(mBinding.replyRoot.editContent);
                mBinding.replyRoot.editContent.setText("回复");
                mBinding.replyRoot.editContent.setSelection(mBinding.replyRoot.editContent.getText().length());
                mBinding.replyRoot.editContent.addAtSpan("", mDatasTwo.get(position - 1).getUser().getName(), mDatasTwo.get(position - 1).getId(), true);
                mBinding.replyRoot.editContent.getText().insert(mBinding.replyRoot.editContent.getText().length(), ":");
            }

            @Override
            public void longContentItemclick(View v, String content, int postion) {
                QPopuWindow.getInstance(provideActivity()).builder
                        .bindView(v, postion)
                        .setPopupItemList(new String[]{"复制"})
                        .setPointers(rawX, rawY)
                        .setOnPopuListItemClickListener(new QPopuWindow.OnPopuListItemClickListener() {
                            @Override
                            public void onPopuListItemClick(View anchorView, int anchorViewPosition, int position) {
                                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData mClipData = ClipData.newPlainText("Label", content);
                                cm.setPrimaryClip(mClipData);
                                ToastUtil.showToast("已复制");
                            }
                        }).show();
            }

        });
        mBinding.replyRoot.recyclerReply.setLayoutManager(new LinearLayoutManager(provideActivity()));
        mBinding.replyRoot.recyclerReply.setAdapter(mAdapterTwo);
        mBinding.replyRoot.recyclerReply.setOnRecyclerViewTouchDownListen(new InterruptRv.OnRecyclerViewTouchDownListen() {
            @Override
            public void onTouchDown() {
                hideKeyboard();
                mBinding.replyRoot.editContent.clearFocus();
//                mBinding.replyRoot.llayoutAttachFunction.setVisibility(View.GONE);
                mBinding.replyRoot.llayoutChatEmojiRoot.setVisibility(View.GONE);
                mBinding.replyRoot.flayoutRecord.setVisibility(View.GONE);
                mBinding.replyRoot.llayoutTxt.setVisibility(View.VISIBLE);
                mBinding.replyRoot.ivChatEmoji.setSelected(false);
            }
        });
    }

    private void closeReplyView(float start) {
        stopMediaPlayer();
        if (mAdapter != null) {
            mAdapter.stopMediaPlayer();
        }
        if (mAdapterTwo != null) {
            mAdapterTwo.stopMediaPlayer();
        }
        if (outAnimator == null) {
            outAnimator = ValueAnimator.ofFloat(0, getResources().getDisplayMetrics().heightPixels);
            outAnimator.addUpdateListener(animation -> {
                mBinding.replyRoot.llayoutRoot.setTranslationY((Float) animation.getAnimatedValue());
            });
            outAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mBinding.viewMiddle.setClickable(false);
                    mBinding.replyRoot.editContent.setText("");
                    mBinding.replyRoot.editContent.clearFocus();
//                    mBinding.replyRoot.llayoutAttachFunction.setVisibility(View.GONE);
                    mBinding.replyRoot.llayoutChatEmojiRoot.setVisibility(View.GONE);
                    mBinding.replyRoot.flayoutRecord.setVisibility(View.GONE);
                    mBinding.replyRoot.llayoutTxt.setVisibility(View.VISIBLE);
                    mBinding.replyRoot.ivChatEmoji.setSelected(false);
                    mBinding.replyRoot.llayoutRoot.setVisibility(View.GONE);
                    mDatasTwo.clear();
                }
            });
        }
        outAnimator.setFloatValues(start, getResources().getDisplayMetrics().heightPixels);
        outAnimator.setDuration(200).start();
        if (replyPostionOne > 0) {
            mPresenter.getReplyDetail(themeid, mDatas.get(replyPostionOne - 1).getId(), replyDetail_Tag);
        } else if (jpushReplyDetail != null) {
            for (int i = 0; i < mDatas.size(); i++) {
                if (mDatas.get(i).getId() == jpushReplyDetail.getId()) {
                    replyPostionOne = i + 1;
                    break;
                }
            }
            jpushReplyDetail = null;
            if (replyPostionOne > 0)
                mPresenter.getReplyDetail(themeid, mDatas.get(replyPostionOne - 1).getId(), replyDetail_Tag);
        }
    }

    private void openReplyView(boolean showKeyboard, float start, boolean isSlide) {
        stopMediaPlayer();
        if (mAdapter != null) {
            mAdapter.stopMediaPlayer();
        }
        if (mAdapterTwo != null) {
            mAdapterTwo.stopMediaPlayer();
        }

        if (inAnimator == null) {
            inAnimator = ValueAnimator.ofFloat(getResources().getDisplayMetrics().heightPixels, 0);
            inAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mBinding.replyRoot.llayoutRoot.setVisibility(View.VISIBLE);
                    mBinding.replyRoot.llayoutRoot.setTranslationY((Float) animation.getAnimatedValue());
                }
            });
        }
        inAnimator.setFloatValues(start, 0);
        inAnimator.removeAllListeners();
        inAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mBinding.viewMiddle.setClickable(true);
                mBinding.replyRoot.llayoutChatEmojiRoot.setVisibility(View.GONE);
                mBinding.replyRoot.ivChatEmoji.setSelected(false);
                if (!isSlide)
                    loadMoreTwo("");
                if (showKeyboard)
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mBinding.replyRoot.editContent.requestFocus();
                            ((InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(mBinding.replyRoot.editContent, InputMethodManager.SHOW_IMPLICIT);
                        }
                    }, 50);
            }
        });
        inAnimator.setDuration(200).start();
    }


    protected void showKeyboard(EditText et) {
//        mBinding.llayoutMenu.setVisibility(View.GONE);
        mBinding.flayoutRecord.setVisibility(View.GONE);
        mBinding.llayoutEdit.setVisibility(View.VISIBLE);
        mBinding.llayoutTxt.setVisibility(View.VISIBLE);
        mBinding.llayoutAttachFunction.setVisibility(View.VISIBLE);
        et.requestFocus();
        ((InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);

    }

    protected void showKeyboardTwo(EditText et) {
        et.requestFocus();
        ((InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
    }

    private void addHeader() {
        View headView = getLayoutInflater().inflate(R.layout.theme_detail_head, (ViewGroup) mBinding.recyclerReply.getParent(), false);
        headBinding = DataBindingUtil.bind(headView);
        mAdapter.addHeaderView(headView);
    }

    private void initAdapter() {
        mAdapter = new ThemeDetailReplyAdapter(mDatas, mp);
        mAdapter.setImagesDialog(imagesDialog);
        mAdapter.setEnableLoadMore(false);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore("", label);
            }
        });
        mAdapter.setmListen(new ThemeDetailReplyAdapter.onItemOperateListen() {
            @Override
            public void zanOperate(int position) {
                mPresenter.likeOperate(mDatas.get(position - 1).getThread_id(), mDatas.get(position - 1).getIslike(), mDatas.get(position - 1).getId(), ReplyLike_Tag);
                if (mDatas.get(position - 1).getIslike() == 1) {
                    mDatas.get(position - 1).setIslike(0);
                    try {
                        mDatas.get(position - 1).setLikecn((Integer.parseInt(mDatas.get(position - 1).getLikecn()) - 1) + "");
                    } catch (Exception e) {
                    }

                    ToastUtil.showToast("取消点赞");
                } else {
                    mDatas.get(position - 1).setIslike(1);
                    try {
                        mDatas.get(position - 1).setLikecn((Integer.parseInt(mDatas.get(position - 1).getLikecn()) + 1) + "");
                    } catch (Exception e) {
                    }
                    ToastUtil.showToast("点赞成功");
                }
                mAdapter.notifyItemChanged(position);
            }

            @Override
            public void twoReplyOperate(int position) {
                if (isPreventGo()) {
                    replyPostionOne = position;
                    fillHeadDataTwo(mDatas.get(position - 1));
                    openReplyView(true, getResources().getDisplayMetrics().heightPixels, false);
                }
            }

            @Override
            public void operateItem(int position) {
                if (isPreventGo()) {
                    Intent intent = new Intent(provideActivity(), UserInfoActivity.class);
                    intent.putExtra("userid", mDatas.get(position - 1).getAuthor_userid());
                    startActivity(intent);
                }
            }

            @Override
            public void moreClick(int position) {
                showOperatePop(position, THEMEREPLY_TYPE);
            }

            @Override
            public void itemclick(int position) {
                if (isPreventGo()) {
                    replyPostionOne = position;
                    fillHeadDataTwo(mDatas.get(position - 1));
                    openReplyView(false, getResources().getDisplayMetrics().heightPixels, false);
                }
            }

            @Override
            public void clickAudio(int position) {
                if (mp.isPlaying() && mVoiceAnimation != null)
                    stopMediaPlayer();
                if (!mDatas.get(position - 1).isSelf() && mDatas.get(position - 1).getAudioread() != 1) {
                    mPresenter.readThemeAudio(themeid, mDatas.get(position - 1).getId(), ReadTheme_TAG);
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

                        mAdapter.remove(position - 1);
                    }

                    @Override
                    public void onRepublish() {
                        mDatas.get(position - 1).setSelfStatus(1);
                        mAdapter.refreshNotifyItemChanged(position - 1);
                        realPublish(mDatas.get(position - 1));

                    }
                });
                republishPop.show();
            }

            @Override
            public void longContentItemclick(View v, String content, int postion) {
                QPopuWindow.getInstance(provideActivity()).builder
                        .bindView(v, postion)
                        .setPopupItemList(new String[]{"复制"})
                        .setPointers(rawX, rawY)
                        .setOnPopuListItemClickListener(new QPopuWindow.OnPopuListItemClickListener() {
                            @Override
                            public void onPopuListItemClick(View anchorView, int anchorViewPosition, int position) {
                                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData mClipData = ClipData.newPlainText("Label", content);
                                cm.setPrimaryClip(mClipData);
                                ToastUtil.showToast("已复制");
                            }
                        }).show();
            }
        });
        mBinding.recyclerReply.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerReply.setAdapter(mAdapter);
        mBinding.recyclerReply.setOnRecyclerViewTouchDownListen(new InterruptRv.OnRecyclerViewTouchDownListen() {
            @Override
            public void onTouchDown() {
                hideKeyboard();
                mBinding.editContent.clearFocus();
                mBinding.llayoutAttachFunction.setVisibility(View.GONE);
                mBinding.llayoutChatEmojiRoot.setVisibility(View.GONE);
                mBinding.flayoutRecord.setVisibility(View.GONE);
                mBinding.flayoutChatPicRoot.setVisibility(View.GONE);
                mBinding.llayoutTxt.setVisibility(View.VISIBLE);
                mBinding.ivChatEmoji.setSelected(false);
//                mBinding.llayoutEdit.setVisibility(View.GONE);
//                mBinding.llayoutMenu.setVisibility(View.VISIBLE);
            }
        });
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

    private void showOperatePop(int position, int type) {
        if (replyPop == null)
            replyPop = new ThemeDetailPoppubWindow(provideActivity());
        replyPop.setDismissListener(new ThemeDetailPoppubWindow.DismissListener() {
            @Override
            public void dismiss() {

            }

            @Override
            public void onReply() {
                showKeyboard(mBinding.editContent);
                mBinding.editContent.setText("回复");
                mBinding.editContent.setSelection(mBinding.editContent.getText().length());
                mBinding.editContent.addAtSpan("", mDatas.get(position - 1).getUser().getName(), mDatas.get(position - 1).getId(), true);
                mBinding.editContent.getText().insert(mBinding.editContent.getText().length(), ":");
            }

            @Override
            public void onDelete(int type) {
                if (type == THEMEREPLY_TYPE) {
                    replyPostionOne = position;
                    mPresenter.deleteReply(themeid, mDatas.get(position - 1).getId(), DeleteReply_Tag);
                } else if (type == THEME_TYPE) {
                    mPresenter.deleteTheme(themeid, DeleteTheme_Tag);
                }
            }

            @Override
            public void onReport(int type) {
                Intent intent = new Intent(provideActivity(), WebviewActivity.class);
                if (type == THEME_TYPE)
                    intent.putExtra("weburl", Constant.BASEURL + "/report?thread=" + themeid);
                else if (type == THEMEREPLY_TYPE)
                    intent.putExtra("weburl", Constant.BASEURL + "/report?thread=" + themeid + "&post=" + mDatas.get(position - 1).getId());
                startActivity(intent);

            }
        });
        if (position != -1)
            replyPop.setIsSelf(mDatas.get(position - 1).getUser().getIsme() == 1);
        else
            replyPop.setIsSelf(detailInfo.getAuthor_userid() == SharedPrefUtil.getInt(Constant.userid_key, 0));
        replyPop.setType(type);
        replyPop.show();
    }

    private void showOperatePopTwo(int position, int type) {
        if (replyPopTwo == null)
            replyPopTwo = new ThemeDetailPoppubWindow(provideActivity());
        replyPopTwo.setDismissListener(new ThemeDetailPoppubWindow.DismissListener() {
            @Override
            public void dismiss() {

            }

            @Override
            public void onReply() {
                showKeyboardTwo(mBinding.replyRoot.editContent);
                mBinding.replyRoot.editContent.setText("回复");
                mBinding.replyRoot.editContent.setSelection(mBinding.replyRoot.editContent.getText().length());
                mBinding.replyRoot.editContent.addAtSpan("", mDatasTwo.get(position - 1).getUser().getName(), mDatasTwo.get(position - 1).getId(), true);
                mBinding.replyRoot.editContent.getText().insert(mBinding.replyRoot.editContent.getText().length(), ":");
            }

            @Override
            public void onDelete(int type) {
                if (type == THEMEREPLY_TYPE) {
                    selectPostionTwo = position;
                    mPresenter.deleteReply(themeid, mDatasTwo.get(selectPostionTwo - 1).getId(), DeleteReplyTwo_Tag);
                } else if (type == THEME_TYPE) {
                    if (replyPostionOne > 0)
                        mPresenter.deleteReply(themeid, mDatas.get(replyPostionOne - 1).getId(), DeleteReply_Tag);
                    else if (jpushReplyDetail != null)
                        mPresenter.deleteReply(themeid, jpushReplyDetail.getId(), DeleteReply_Tag);
                }
            }

            @Override
            public void onReport(int type) {
                Intent intent = new Intent(provideActivity(), WebviewActivity.class);
                if (type == THEME_TYPE) {
                    if (replyPostionOne > 0)
                        intent.putExtra("weburl", Constant.BASEURL + "/report?thread=" + themeid + "&post=" + mDatas.get(replyPostionOne - 1).getId());
                    else if (jpushReplyDetail != null)
                        intent.putExtra("weburl", Constant.BASEURL + "/report?thread=" + themeid + "&post=" + jpushReplyDetail.getId());
                } else if (type == THEMEREPLY_TYPE)
                    intent.putExtra("weburl", Constant.BASEURL + "/report?thread=" + themeid + "&post=" + mDatasTwo.get(position - 1).getId());
                startActivity(intent);

            }
        });
        if (position != -1)
            replyPopTwo.setIsSelf(mDatasTwo.get(position - 1).getUser().getIsme() == 1);
        else {
            if (replyPostionOne > 0)
                replyPopTwo.setIsSelf(mDatas.get(replyPostionOne - 1).getUser().getIsme() == 1);
            else if (jpushReplyDetail != null)
                replyPopTwo.setIsSelf(jpushReplyDetail.getUser().getIsme() == 1);

        }
        replyPopTwo.setType(type);
        replyPopTwo.showReplyView();
        replyPopTwo.show();
    }

    private void loadMore(String key, int label) {
        pid = 0;
        for (int i = mDatas.size() - 1; i >= 0; i--) {
            if (mDatas.get(i).getId() != 0 && label == 0 ? true : !mDatas.get(i).isSelfInLandlord()) {
                pid = mDatas.get(i).getId();
                break;
            }
        }
        mPresenter.getReplyList(themeid, pid, "1", label, key, themeReplyList_Tag);
    }

    private void loadMoreAfterPublish(String key, int postId, int label) {
        if (label == 0) {
            for (int i = mDatas.size() - 1; i >= 0; i--) {
                if (mDatas.get(i).getId() != 0) {
                    pid = mDatas.get(i).getId();
                    break;
                }
            }
            mPresenter.getReplyList(themeid, pid, "1", 0, key, themeReplyList_Tag);
        } else {
            mPresenter.getReplyList(themeid, postId, "0", 0, key, themeReplyList_LandLord_Tag);
        }
    }

    private void refresh(int label) {
        if (label == 1)
            mPresenter.getReplyList(themeid, 0, "1", label, "", themeReplyList_refresh_hot_Tag);
        else
            mPresenter.getReplyList(themeid, 0, "1", label, "", themeReplyList_refresh_Tag);
    }

    @Override
    protected void initEvent() {
        mBinding.ivSeeLandlord.setOnClickListener(this);
        mBinding.viewMiddle.setOnClickListener(this);
        mBinding.viewMiddle.setClickable(false);
        mBinding.ivChatClick.setOnClickListener(this);
        mBinding.flayoutChatVideo.setOnClickListener(this);
        mBinding.ivChatAdd.setOnClickListener(this);
        mBinding.ivChatmenuPicEmptyadd.setOnClickListener(this);
        mBinding.ivChatRecord.setOnClickListener(this);
        mBinding.ivChatKeyboard.setOnClickListener(this);


        mBinding.ivChatEmoji.setOnClickListener(this);
        mBinding.ivChatSend.setOnClickListener(this);
        mBinding.ivEmojiDelete.setOnClickListener(this);
//        mBinding.llayoutMenu.setOnClickListener(this);
//        mBinding.txtShowEdit.setOnClickListener(this);

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
                    intent.putExtra("topicid", topicid + "");
                    startActivityForResult(intent, AT_request);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                updataSendButton();

            }
        });
    }

    //更新发送按钮状态
    private void updataSendButton() {
        if (mBinding.editContent.getEditableText().toString().length() > 0 || selectPics.size() > 1) {
            mBinding.ivChatSend.setSelected(true);
        } else {
            mBinding.ivChatSend.setSelected(false);
        }
    }

    //更新发送按钮状态
    private void updataSendButtonTwo() {
        if (mBinding.replyRoot.editContent.getEditableText().toString().length() > 0) {
            mBinding.replyRoot.ivChatSend.setSelected(true);
        } else {
            mBinding.replyRoot.ivChatSend.setSelected(false);
        }
    }

    @Override
    public void showLoading(int tag) {
        if (tag == themeDetail_Tag)
            startLoading();
    }

    @Override
    public void hideLoading(int tag) {
        stopLoading();
    }

    @Override
    public void showError(ErrorBean errorBean, int tag) {
        if (errorBean.androidType.equals(ErrorBean.TYPE_SHOW))
            ToastUtil.showToast(errorBean.desc);
        if (errorBean.androidcode.equals("130001")) {//观点不存在
            EventBus.getDefault().post(new DeleteThemeBus(themeid));
            finish();
        }
        switch (tag) {
            case uploadpicone_realSend_TAG:
                for (int i = mDatas.size() - 1; i >= 0; i--) {
                    if (mDatas.get(i).getSelfStatus() == 1 && mDatas.get(i).getThumb() != null && mDatas.get(i).getThumb().size() > 0) {
                        mDatas.get(i).setSelfStatus(2);
                        mAdapter.refreshNotifyItemChanged(i);
                    }
                }
                break;
            case PublishPic_TAG:
            case Publish_TAG:
                for (int i = mDatas.size() - 1; i >= 0; i--) {
                    if (mDatas.get(i).getSelfStatus() == 1 && errorBean.key.equals(mDatas.get(i).getTerminal_id() + "")) {
                        mDatas.get(i).setSelfStatus(2);
                        mAdapter.refreshNotifyItemChanged(i);
                        break;
                    }
                }
                for (ThemeDetailReplyBean.DataBean info : mDatas) {
                    if (info.getSelfStatus() == 1 && info.getVideoType() > 0) {
                        realPublish(info);
                        break;
                    }
                }
                break;
            case PublishPicTwo_TAG:
            case PublishTwo_Tag:
                for (int i = mDatasTwo.size() - 1; i >= 0; i--) {
                    if (mDatasTwo.get(i).getSelfStatus() == 1 && errorBean.key.equals(mDatasTwo.get(i).getTerminal_id() + "")) {
                        mDatasTwo.get(i).setSelfStatus(2);
                        mAdapterTwo.refreshNotifyItemChanged(i);
                        break;
                    }
                }
                for (ThemeDetailReplyBean.DataBean info : mDatasTwo) {
                    if (info.getSelfStatus() == 1 && info.getVideoType() > 0) {
                        realPublish(info);
                        break;
                    }
                }
                break;
            case themeReplyList_Tag:
                mAdapter.setEnableLoadMore(false);
                mAdapter.loadMoreComplete();
                break;
            case themeReplyList_LandLord_Tag:
                mAdapter.setEnableLoadMore(false);
                mAdapter.loadMoreComplete();
                break;
            case ReplyTwoList_Tag:
                mAdapterTwo.setEnableLoadMore(false);
                mAdapterTwo.loadMoreComplete();
                break;
        }
    }

    @Override
    public void setData(ErrorBean errorBean, int tag) {
        switch (tag) {
            case replyDetail_Tag:
                if (errorBean instanceof ThemeDetailReplyBean) {
                    ThemeDetailReplyBean bean = (ThemeDetailReplyBean) errorBean;
                    if (bean.getData() != null && bean.getData().size() > 0) {
                        dealReplyDatasForDB(bean.getData());
                        if (replyPostionOne > 0) {
                            mAdapter.setData(replyPostionOne - 1, bean.getData().get(0));
                            replyPostionOne = -1;
                        }
                    }
                }
                break;
            case Jpush_ReplyDeatil_Tag:
                if (errorBean instanceof ThemeDetailReplyBean) {
                    ThemeDetailReplyBean bean = (ThemeDetailReplyBean) errorBean;
                    if (bean.getData() != null && bean.getData().size() > 0) {
                        dealReplyDatasForDB(bean.getData());
                        jpushReplyDetail = bean.getData().get(0);
                        fillHeadDataTwo(jpushReplyDetail);
                        openReplyView(false, getResources().getDisplayMetrics().heightPixels, false);
                    }
                }
                break;
            case ReplyTwoList_Tag:
                if (errorBean instanceof ThemeDetailReplyBean) {
                    ThemeDetailReplyBean bean = (ThemeDetailReplyBean) errorBean;
                    if (bean.getData() != null && bean.getData().size() > 0) {
                        dealReplyDatasForDB(bean.getData());
                        mAdapterTwo.addData(bean.getData());
                        mAdapterTwo.setEnableLoadMore(true);
                        mAdapterTwo.loadMoreComplete();
                    } else {
                        if (pidTwo == 0) {
                        }
                        mAdapterTwo.setEnableLoadMore(false);
                        mAdapterTwo.loadMoreEnd();
                    }
                    if (!TextUtils.isEmpty(errorBean.key))
                        for (int i = mDatasTwo.size() - 1; i >= 0; i--) {
                            if (errorBean.key.equals(mDatasTwo.get(i).getTerminal_id() + "")) {
                                mAdapterTwo.remove(i);
                                break;
                            }
                        }
                }
                break;
            case themeReplyList_Tag:
                if (errorBean instanceof ThemeDetailReplyBean) {
                    ThemeDetailReplyBean bean = (ThemeDetailReplyBean) errorBean;
                    if (bean.getData() != null && bean.getData().size() > 0) {
                        dealReplyDatasForDB(bean.getData());
//                        headBinding.llayoutWithReply.setVisibility(View.VISIBLE);
                        mAdapter.addData(bean.getData());
                        mAdapter.setEnableLoadMore(true);
                        mAdapter.loadMoreComplete();
                    } else {
                        if (pid == 0) {
//                            ToastUtil.showToast("还没有人发表评论");
                        }
                        mAdapter.setEnableLoadMore(false);
                        mAdapter.loadMoreEnd();
                    }
                    if (!TextUtils.isEmpty(errorBean.key))
                        for (int i = mDatas.size() - 1; i >= 0; i--) {
                            if (errorBean.key.equals(mDatas.get(i).getTerminal_id() + "")) {
                                mAdapter.remove(i);
                                break;
                            }
                        }
                }
                break;
            case themeReplyList_LandLord_Tag:
                if (errorBean instanceof ThemeDetailReplyBean) {
                    ThemeDetailReplyBean bean = (ThemeDetailReplyBean) errorBean;
                    if (bean.getData() != null && bean.getData().size() > 0) {
                        dealReplyDatasForDB(bean.getData());
                        for (ThemeDetailReplyBean.DataBean data : bean.getData()) {
                            data.setSelfInLandlord(true);
                        }
//                        headBinding.llayoutWithReply.setVisibility(View.VISIBLE);
                        mAdapter.addData(bean.getData());
                        mAdapter.setEnableLoadMore(true);
                        mAdapter.loadMoreComplete();
                    } else {
                        if (pid == 0) {
//                            ToastUtil.showToast("还没有人发表评论");
                        }
                        mAdapter.setEnableLoadMore(false);
                        mAdapter.loadMoreEnd();
                    }
                    if (!TextUtils.isEmpty(errorBean.key))
                        for (int i = mDatas.size() - 1; i >= 0; i--) {
                            if (errorBean.key.equals(mDatas.get(i).getTerminal_id() + "")) {
                                mAdapter.remove(i);
                                break;
                            }
                        }
                }
                break;
            case themeReplyList_refresh_Tag:
                if (errorBean instanceof ThemeDetailReplyBean) {

                    label = 0;
                    mBinding.ivSeeLandlord.setSelected(false);
                    mDatas.clear();
                    mAdapter.notifyDataSetChanged();
                    ThemeDetailReplyBean bean = (ThemeDetailReplyBean) errorBean;
                    if (bean.getData() != null && bean.getData().size() > 0) {
                        dealReplyDatasForDB(bean.getData());
//                        headBinding.llayoutWithReply.setVisibility(View.VISIBLE);
                        mAdapter.addData(bean.getData());
                        mAdapter.setEnableLoadMore(true);
                        mAdapter.loadMoreComplete();
                    } else {
                        if (pid == 0) {
//                            ToastUtil.showToast("还没有人发表评论");
                        }
                        mAdapter.setEnableLoadMore(false);
                        mAdapter.loadMoreEnd();
                    }

                }
                break;
            case themeReplyList_refresh_hot_Tag:
                if (errorBean instanceof ThemeDetailReplyBean) {
                    label = 1;
                    mBinding.ivSeeLandlord.setSelected(true);
                    mDatas.clear();
                    mAdapter.notifyDataSetChanged();
                    ThemeDetailReplyBean bean = (ThemeDetailReplyBean) errorBean;
                    if (bean.getData() != null && bean.getData().size() > 0) {
                        dealReplyDatasForDB(bean.getData());
//                        headBinding.llayoutWithReply.setVisibility(View.VISIBLE);
                        mAdapter.addData(bean.getData());
                        mAdapter.setEnableLoadMore(true);
                        mAdapter.loadMoreComplete();
                    } else {
                        if (pid == 0) {
//                            ToastUtil.showToast("还没有人发表评论");
                        }
                        mAdapter.setEnableLoadMore(false);
                        mAdapter.loadMoreEnd();
                    }

                }
                break;
            case themeDetail_Tag:
                if (errorBean instanceof ThemeListBean) {
                    ThemeListBean bean = (ThemeListBean) errorBean;
                    mBinding.barTxtTitle.setText(bean.getTopic().getName());
                    mBinding.barTxtTitle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (getIntent().getBooleanExtra("fromchat", false) && HomeFragment.resumeTopicid == topicid) {
                                finish();
                            } else {
                                EventBus.getDefault().post(new BackTopicBus(topicid));
                                HomeFragment.resumeTopicid = topicid;
                                Intent intent = new Intent(provideActivity(), ChattingActivity.class);
                                intent.putExtra("topoicname", bean.getTopic().getName());
                                intent.putExtra("topoicpic", bean.getTopic().getImg_url_thumb());
                                startActivity(intent);
                                finish();
                            }

                        }
                    });

                    if (bean.getData() != null && bean.getData().size() > 0) {
                        addHeader();
                        dealDatasForDB(bean.getData());
                        detailInfo = bean.getData().get(0);
                        fillDetailData(detailInfo);
                        refresh(0);
                    }
                }
                break;
            case Like_TAG:
                if (detailInfo.getHasLike() == 1) {
                    detailInfo.setHasLike(0);
                    try {
                        detailInfo.setLikecn("" + (Integer.parseInt(detailInfo.getLikecn()) - 1));
                    } catch (Exception e) {
                    }
                    ToastUtil.showToast("取消点赞");
                } else {
                    detailInfo.setHasLike(1);
                    try {
                        detailInfo.setLikecn("" + (Integer.parseInt(detailInfo.getLikecn()) + 1));
                    } catch (Exception e) {
                    }
                    ToastUtil.showToast("点赞成功");
                }
                fillDetailData(detailInfo);
                break;
            case Collect_TAG:
                if (detailInfo.getHasCollect() == 1) {
                    detailInfo.setHasCollect(0);
                    ToastUtil.showToast("取消收藏");
                    EventBus.getDefault().post(new CollectionThemeBus(themeid, false));
                } else {
                    detailInfo.setHasCollect(1);
                    ToastUtil.showToast("收藏成功");
                    EventBus.getDefault().post(new CollectionThemeBus(themeid, true));
                }
                fillDetailData(detailInfo);
                break;
            case PublishPicTwo_TAG:
            case PublishTwo_Tag:
                for (int i = mDatasTwo.size() - 1; i >= 0; i--) {
                    if (errorBean.key.equals(mDatasTwo.get(i).getTerminal_id() + "")) {
                        mDatasTwo.get(i).setSelfStatus(0);
//                        mAdapter.refreshNotifyItemChanged(i);
                        break;
                    }
                }
                loadMoreTwo(errorBean.key);
//                ToastUtil.showToast("发布成功");

                mBinding.replyRoot.llayoutChatEmojiRoot.setVisibility(View.GONE);
                mBinding.replyRoot.ivChatEmoji.setSelected(false);
                if (replyPostionOne > 0) {
                    try {
                        mDatas.get(replyPostionOne - 1).setCommentscn((Integer.parseInt(mDatas.get(replyPostionOne - 1).getCommentscn()) + 1) + "");
                        mBinding.replyRoot.barTxtTitle.setText(Integer.parseInt(mDatas.get(replyPostionOne - 1).getCommentscn()) > 0 ? mDatas.get(replyPostionOne - 1).getCommentscn() + "条回复" : "暂无回复");
                    } catch (Exception e) {
                    }
                } else if (jpushReplyDetail != null) {
                    try {
                        jpushReplyDetail.setCommentscn((Integer.parseInt(jpushReplyDetail.getCommentscn()) + 1) + "");
                        mBinding.replyRoot.barTxtTitle.setText(Integer.parseInt(jpushReplyDetail.getCommentscn()) > 0 ? jpushReplyDetail.getCommentscn() + "条回复" : "暂无回复");
                    } catch (Exception e) {
                    }
                }
                for (ThemeDetailReplyBean.DataBean info : mDatasTwo) {
                    if (info.getSelfStatus() == 1 && info.getVideoType() > 0) {
                        realPublish(info);
                        break;
                    }
                }
                break;
            case PublishPic_TAG:
            case Publish_TAG:
                for (int i = mDatas.size() - 1; i >= 0; i--) {
                    if (errorBean.key.equals(mDatas.get(i).getTerminal_id() + "")) {
                        mDatas.get(i).setSelfStatus(0);
//                        mAdapter.refreshNotifyItemChanged(i);
                        break;
                    }
                }
                PublishReplyBean bean1 = (PublishReplyBean) errorBean;
                loadMoreAfterPublish(errorBean.key, bean1.getPost_id(), label);
//                ToastUtil.showToast("发布成功");

//                hideKeyboard();
                mBinding.llayoutChatEmojiRoot.setVisibility(View.GONE);
                mBinding.ivChatEmoji.setSelected(false);
                try {
                    detailInfo.setCommentscn((Integer.parseInt(detailInfo.getCommentscn()) + 1) + "");
                } catch (Exception e) {
                }
                if (headBinding != null)
                    headBinding.txtThemeReplyNum.setText(detailInfo.getCommentscn() + " 回复");
                for (ThemeDetailReplyBean.DataBean info : mDatas) {
                    if (info.getSelfStatus() == 1 && info.getVideoType() > 0) {
                        realPublish(info);
                        break;
                    }
                }
                break;
            case DeleteReply_Tag:
                if (replyPostionOne > 0) {
                    EventBus.getDefault().post(new DeleteReplyBus(mDatas.get(replyPostionOne - 1).getId()));
                    mAdapter.remove(replyPostionOne - 1);
                    ToastUtil.showToast("评论删除成功");
                    try {
                        detailInfo.setCommentscn((Integer.parseInt(detailInfo.getCommentscn()) - 1) + "");
                    } catch (Exception e) {
                    }

                    if (headBinding != null)
                        headBinding.txtThemeReplyNum.setText(detailInfo.getCommentscn() + " 回复");
                    replyPostionOne = -1;
                    closeReplyView(0);
                } else if (jpushReplyDetail != null) {
                    EventBus.getDefault().post(new DeleteReplyBus(jpushReplyDetail.getId()));
                    ToastUtil.showToast("评论删除成功");
                    if (detailInfo != null)
                        try {
                            detailInfo.setCommentscn((Integer.parseInt(detailInfo.getCommentscn()) - 1) + "");
                        } catch (Exception e) {
                        }
                    for (int i = 0; i < mDatas.size(); i++) {
                        if (mDatas.get(i).getId() == jpushReplyDetail.getId()) {
                            mAdapter.remove(i);
                            break;
                        }
                    }
                    replyPostionOne = -1;
                    jpushReplyDetail = null;
                    closeReplyView(0);
                }
                break;
            case DeleteReplyTwo_Tag:
                if (selectPostionTwo > 0) {
                    EventBus.getDefault().post(new DeleteReplyBus(mDatasTwo.get(selectPostionTwo - 1).getId()));
                    mAdapterTwo.remove(selectPostionTwo - 1);
                    ToastUtil.showToast("评论删除成功");
                    try {
                        if (replyPostionOne > 0) {
                            mDatas.get(replyPostionOne - 1).setCommentscn((Integer.parseInt(mDatas.get(replyPostionOne - 1).getCommentscn()) - 1) + "");
                            mBinding.replyRoot.barTxtTitle.setText(Integer.parseInt(mDatas.get(replyPostionOne - 1).getCommentscn()) > 0 ? mDatas.get(replyPostionOne - 1).getCommentscn() + "条回复" : "暂无回复");
                        } else if (jpushReplyDetail != null) {
                            mDatas.get(replyPostionOne - 1).setCommentscn((Integer.parseInt(jpushReplyDetail.getCommentscn()) - 1) + "");
                            mBinding.replyRoot.barTxtTitle.setText(Integer.parseInt(jpushReplyDetail.getCommentscn()) > 0 ? jpushReplyDetail.getCommentscn() + "条回复" : "暂无回复");
                        }
                    } catch (Exception e) {
                    }
                }
                break;
            case DeleteTheme_Tag:
                isDelete = true;
                ToastUtil.showToast("观点删除成功");
                EventBus.getDefault().post(new DeleteThemeBus(themeid));

                NoticeChangeBean.DataBean noticeBean = new NoticeChangeBean.DataBean();
                noticeBean.setTypeX(21);
                noticeBean.setIds(new ArrayList<>());
                noticeBean.getIds().add(new ArrayList<>());
                noticeBean.getIds().get(0).add(topicid);
                noticeBean.getIds().get(0).add(themeid);
                EventBus.getDefault().post(noticeBean);
                finish();
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
                    for (int i = mDatas.size() - 1; i >= 0; i--) {
                        if (mDatas.get(i).getSelfStatus() == 1 && errorBean.key.equals(mDatas.get(i).getTerminal_id() + "")) {
                            if (updataPicMapReal(mDatas.get(i).getThumb(), mDatas.get(i).getTerminal_id(), uploadpicone_realSend_TAG))
                                realPublishWithPics(mDatas.get(i));
                            break;
                        }
                    }
                    /*if (picSendInfo != null && updataPicMap(picSendInfo.getThumb(), uploadpicone_realSend_TAG))
                        realPublishWithPics(picSendInfo);*/
                }
                break;
        }
    }

    @Override
    protected void onStop() {
        stopMediaPlayer();
        super.onStop();
    }

    public void stopMediaPlayer() {
        try {
            if (mp.isPlaying())
                mp.stop();
        } catch (IllegalStateException e) {
        }
        if (mVoiceAnimation != null) {
            mVoiceAnimation.stop();
            mVoiceAnimation = null;
            if (headBinding != null)
                headBinding.voiceIv.setImageResource(R.drawable.jmui_send_3);
            if (headBindingTwo != null)
                headBindingTwo.voiceIv.setImageResource(R.drawable.jmui_send_3);
        }

    }

    public void releaseMediaPlayer() {
        if (mp != null)
            mp.release();
    }

    @Override
    protected void onDestroy() {
        if (!isDelete) {
            NoticeChangeBean.DataBean noticeBean = new NoticeChangeBean.DataBean();
            noticeBean.setTypeX(24);
            noticeBean.setIds(new ArrayList<>());
            noticeBean.getIds().add(new ArrayList<>());
            noticeBean.getIds().get(0).add(topicid);
            noticeBean.getIds().get(0).add(themeid);
            EventBus.getDefault().post(noticeBean);
        }
        if (mVideoPublish != null) {
            mVideoPublish.canclePublish();
            mVideoPublish.setListener(null);
        }
        mBinding.buttonRecord.releaseRecorder();
        mBinding.replyRoot.buttonRecord.releaseRecorder();
        releaseMediaPlayer();
        picMap.clear();
        audioMap.clear();
        super.onDestroy();
    }

    private void fillDetailData(InnerThemeListBean info) {
        if (headBinding != null) {
            GlideApp.with(provideActivity()).load(info.getPortrait_thumb()).placeholder(R.drawable.icon_head_default).error(R.drawable.icon_head_default).into(headBinding.ivHead);
            headBinding.ivHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isPreventGo()) {
                        Intent intent = new Intent(provideActivity(), UserInfoActivity.class);
                        intent.putExtra("userid", info.getAuthor_userid());
                        startActivity(intent);
                    }
                }
            });
            headBinding.tvLandlord.setText(info.getLabel());
            headBinding.tvLandlord.setVisibility(!TextUtils.isEmpty(info.getLabel()) ? View.VISIBLE : View.GONE);
            headBinding.txtName.setText(info.getAuthor_username());
            mBinding.editContent.setHint("回复 " + info.getAuthor_username() + " ...");
//            mBinding.txtShowEdit.setText("回复 " + info.getAuthor_username() + " ...");

            headBinding.txtTime.setText(info.getCreated_at2());
            headBinding.txtThemeContent.setVisibility(TextUtils.isEmpty(info.getContent()) ? View.GONE : View.VISIBLE);
            if (!TextUtils.isEmpty(info.getContent())) {
                headBinding.txtThemeContent.setText(SpanUtil.getExpressionString(provideActivity(), info.getContent(),
                        true, headBinding.txtThemeContent));
                headBinding.txtThemeContent.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        QPopuWindow.getInstance(provideActivity()).builder
                                .bindView(v, 0)
                                .setPopupItemList(new String[]{"复制"})
                                .setPointers(rawX, rawY)
                                .setOnPopuListItemClickListener(new QPopuWindow.OnPopuListItemClickListener() {
                                    @Override
                                    public void onPopuListItemClick(View anchorView, int anchorViewPosition, int position) {
                                        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                        ClipData mClipData = ClipData.newPlainText("Label", headBinding.txtThemeContent.getText().toString());
                                        cm.setPrimaryClip(mClipData);
                                        ToastUtil.showToast("已复制");
                                    }
                                }).show();
                        return true;
                    }
                });
            }

            headBinding.txtThemeReplyNum.setText(info.getCommentscn() + " 回复");
            headBinding.txtThemeZanNum.setText(info.getLikecn() + " 赞");
            headBinding.txtThemeReadnum.setText(info.getReadnumcn() + " 已读");
            headBinding.ivThemeZan.setSelected(info.getHasLike() == 1);
            headBinding.ivThemeCollect.setSelected(info.getHasCollect() == 1);

            headBinding.ivThemeZan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.likeOperate(info.getId(), info.getHasLike(), 0, Like_TAG);
                }
            });
            headBinding.ivThemeCollect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.operateCollect(info.getHasCollect() == 1 ? -1 : 1, info.getId(), Collect_TAG);

                }
            });
            mBinding.ivThemeMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideKeyboard();
                    showOperatePop(-1, THEME_TYPE);
                }
            });

            if (info.getVideoType() == 1) {
                headBinding.flayoutThemePics.setVisibility(View.VISIBLE);
                headBinding.llayoutAudioRoot.setVisibility(View.GONE);
                headBinding.fllayoutThemeVideo.setVisibility(View.GONE);

                int max = ArmsUtils.dip2px(provideActivity(), 180);
                int size = info.getThumb().size();
                if (size == 1) {
                    headBinding.recycleThemePics.setVisibility(View.GONE);
                    headBinding.flayoutOneParent.setVisibility(View.VISIBLE);
                    try {
                        Uri uri = Uri.parse(info.getSrc().get(0));
                        String s = uri.getQueryParameter("s");
                        if (!TextUtils.isEmpty(s)) {
                            headBinding.txtLongpic.setVisibility(Integer.parseInt(s) == 1 ? View.VISIBLE : View.GONE);
                        } else {
                            headBinding.txtLongpic.setVisibility(View.GONE);
                        }
                        int width = Integer.parseInt(uri.getQueryParameter("sw"));
                        int height = Integer.parseInt(uri.getQueryParameter("sh"));
                        headBinding.ivThemeOnepic.getLayoutParams().width = width;
                        headBinding.ivThemeOnepic.getLayoutParams().height = height;
                    } catch (Exception e) {
                        headBinding.txtLongpic.setVisibility(View.GONE);
                    }
                    GlideApp.with(provideActivity()).load(info.getThumb().get(0)).centerCrop().placeholder(R.drawable.icon_theme_default_big).error(R.drawable.icon_theme_default_big).into(headBinding.ivThemeOnepic);

                    headBinding.ivThemeOnepic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            imagesDialog.setPicList(info.getSrc(), info.getThumb(), 0);
                            imagesDialog.show();
                        }
                    });
                } else {
                    headBinding.recycleThemePics.setVisibility(View.VISIBLE);
                    headBinding.flayoutOneParent.setVisibility(View.GONE);
                    ChatPicsAdapter picsAdapter = new ChatPicsAdapter(provideActivity(), info.getThumb());
                    headBinding.recycleThemePics.setLayoutManager(new GridLayoutManager(provideActivity(), 3) {
                        @Override
                        public boolean canScrollVertically() {
                            return false;
                        }
                    });
                    headBinding.recycleThemePics.setNestedScrollingEnabled(false);
                    headBinding.recycleThemePics.setHasFixedSize(true);
                    headBinding.recycleThemePics.setAdapter(picsAdapter);
                    picsAdapter.setmListen(new ChatPicsAdapter.onItemOperateListen() {
                        @Override
                        public void operateItem(int position, View v) {
                            imagesDialog.setPicList(info.getSrc(), info.getThumb(), position);
                            imagesDialog.show();

                        }
                    });
                }

            } else if (info.getVideoType() == 2) {
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                headBinding.flayoutThemePics.setVisibility(View.VISIBLE);
                headBinding.flayoutOneParent.setVisibility(View.GONE);
                headBinding.recycleThemePics.setVisibility(View.GONE);
                headBinding.fllayoutThemeVideo.setVisibility(View.GONE);
                headBinding.llayoutAudioRoot.setVisibility(View.VISIBLE);
                headBinding.voiceLengthTv.setText(info.getDuration() + getString(R.string.jmui_symbol_second));

                //控制语音长度显示，长度增幅随语音长度逐渐缩小
                int width = (int) (-0.04 * info.getDuration() * info.getDuration() + 4.526 * info.getDuration() + 75.214);
                headBinding.voiceIv.setImageResource(R.drawable.jmui_send_3);
                headBinding.msgContent.setWidth((int) (width * dm.density));
                headBinding.voiceReadnew.setVisibility(info.getAudioread() == 1 ? View.GONE : View.VISIBLE);
                headBinding.voiceFl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                            Toast.makeText(provideActivity(), getString(R.string.jmui_sdcard_not_exist_toast),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            if (mAdapter.isplaying())
                                mAdapter.stopMediaPlayer();
                            if (info.getAudioread() != 1) {
                                mPresenter.readThemeAudio(themeid, 0, ReadTheme_TAG);
                                headBinding.voiceReadnew.setVisibility(View.GONE);
                                detailInfo.setAudioread(1);
                            }
                            // 如果之前存在播放动画，无论这次点击触发的是暂停还是播放，停止上次播放的动画
                            if (mVoiceAnimation != null) {
                                mVoiceAnimation.stop();
                                mVoiceAnimation = null;
                            }
                            // 播放中点击了正在播放的Item 则暂停播放
                            if (mp.isPlaying()) {
                                headBinding.voiceIv.setImageResource(R.drawable.jmui_voice_send);
                                mVoiceAnimation = (AnimationDrawable) headBinding.voiceIv.getDrawable();
                                pauseVoice();
                                if (mVoiceAnimation != null) {
                                    mVoiceAnimation.stop();
                                    mVoiceAnimation = null;
                                }
                                // 开始播放录音
                            } else {
                                try {

                                    // 继续播放之前暂停的录音
                                    if (mSetData) {
                                        headBinding.voiceIv.setImageResource(R.drawable.jmui_voice_send);
                                        mVoiceAnimation = (AnimationDrawable) headBinding.voiceIv.getDrawable();
                                        if (mVoiceAnimation != null) {
                                            mVoiceAnimation.start();

                                        }
                                        playVoice();
                                        // 否则重新播放该录音或者其他录音
                                    } else {
                                        mp.reset();
                                        // 记录播放录音的位置
                                        mp.setDataSource(provideActivity(), Uri.parse(info.getVideoPath()));
                                        if (App.getInstance().mIsEarPhoneOn) {
                                            mp.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
                                        } else {
                                            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                        }
                                        headBinding.voiceIv.setImageResource(R.drawable.jmui_voice_loading_send);
                                        mVoiceAnimation = (AnimationDrawable) headBinding.voiceIv.getDrawable();
                                        if (mVoiceAnimation != null) {
                                            mVoiceAnimation.start();
                                        }
                                        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                            @Override
                                            public void onPrepared(MediaPlayer mp) {
                                                headBinding.voiceIv.setImageResource(R.drawable.jmui_voice_send);
                                                mVoiceAnimation = (AnimationDrawable) headBinding.voiceIv.getDrawable();
                                                if (mVoiceAnimation != null) {
                                                    mVoiceAnimation.start();
                                                }
                                                playVoice();
                                            }
                                        });
                                        mp.prepareAsync();

                                    }
                                } catch (NullPointerException e) {
                                    if (mVoiceAnimation != null) {
                                        mVoiceAnimation.stop();
                                        mVoiceAnimation = null;
                                    }
                                    headBinding.voiceIv.setImageResource(R.drawable.jmui_send_3);
                                    ToastUtil.showToast(getString(R.string.jmui_file_not_found_toast));
                                } catch (IllegalArgumentException e) {
                                    if (mVoiceAnimation != null) {
                                        mVoiceAnimation.stop();
                                        mVoiceAnimation = null;
                                    }
                                    headBinding.voiceIv.setImageResource(R.drawable.jmui_send_3);
                                    e.printStackTrace();
                                } catch (SecurityException e) {
                                    if (mVoiceAnimation != null) {
                                        mVoiceAnimation.stop();
                                        mVoiceAnimation = null;
                                    }
                                    headBinding.voiceIv.setImageResource(R.drawable.jmui_send_3);
                                    e.printStackTrace();
                                } catch (IllegalStateException e) {
                                    if (mVoiceAnimation != null) {
                                        mVoiceAnimation.stop();
                                        mVoiceAnimation = null;
                                    }
                                    headBinding.voiceIv.setImageResource(R.drawable.jmui_send_3);
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    if (mVoiceAnimation != null) {
                                        mVoiceAnimation.stop();
                                        mVoiceAnimation = null;
                                    }
                                    headBinding.voiceIv.setImageResource(R.drawable.jmui_send_3);
                                    ToastUtil.showToast(getString(R.string.jmui_file_not_found_toast));
                                } /*finally {
                                try {
                                    if (mFIS != null) {
                                        mFIS.close();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }*/
                            }
                        }
                    }

                    private void playVoice() {
//                    mVoiceAnimation.start();
                        mp.start();
                        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer arg0) {
                                if (mVoiceAnimation != null) {
                                    mVoiceAnimation.stop();
                                    mVoiceAnimation = null;
                                }
                                mp.reset();
                                mSetData = false;
                                // 播放完毕，恢复初始状态
                                headBinding.voiceIv.setImageResource(R.drawable.jmui_send_3);
                            }
                        });
                    }

                    private void pauseVoice() {
                        mp.pause();
                        mSetData = true;
                    }
                });

            } else if (info.getVideoType() == 3 || info.getVideoType() == 4) {
                headBinding.flayoutThemePics.setVisibility(View.VISIBLE);
                headBinding.flayoutOneParent.setVisibility(View.GONE);
                headBinding.recycleThemePics.setVisibility(View.GONE);
                headBinding.llayoutAudioRoot.setVisibility(View.GONE);
                headBinding.fllayoutThemeVideo.setVisibility(View.VISIBLE);
                try {
                    Uri uri = Uri.parse(info.getVideoPath());
                    Float width = Float.parseFloat(uri.getQueryParameter("w"));
                    Float height = Float.parseFloat(uri.getQueryParameter("h"));
                    if (height > width) {
                        headBinding.fllayoutThemeVideo.getLayoutParams().width = ArmsUtils.dip2px(provideActivity(), 136);
                        headBinding.fllayoutThemeVideo.getLayoutParams().height = ArmsUtils.dip2px(provideActivity(), 181);
                    } else {
                        headBinding.fllayoutThemeVideo.getLayoutParams().width = ArmsUtils.dip2px(provideActivity(), 181);
                        headBinding.fllayoutThemeVideo.getLayoutParams().height = ArmsUtils.dip2px(provideActivity(), 136);
                    }
                } catch (Exception e) {
                    headBinding.fllayoutThemeVideo.getLayoutParams().width = ArmsUtils.dip2px(provideActivity(), 181);
                    headBinding.fllayoutThemeVideo.getLayoutParams().height = ArmsUtils.dip2px(provideActivity(), 136);
                }
                headBinding.txtVideoDuration.setText(info.getDuration() != 0 ? info.getDuration() + "\"" : "");
                if (!info.getCoverPath().startsWith("http"))
                    GlideApp.with(provideActivity()).load(Uri.fromFile(new File(info.getCoverPath()))).override(540, 408).centerCrop().into(headBinding.ivThemeVideopath);
                else
                    GlideApp.with(provideActivity()).load(info.getCoverPath()).centerCrop().override(540, 408).into(headBinding.ivThemeVideopath);
                headBinding.ivThemeVideopathPreview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isPreventGo()) {
                            if (info.getVideoType() == 3) {
                                Intent intent = new Intent(provideActivity(), NewVodPlayerActivity.class);
                                intent.putExtra("frompath", 4);
                                intent.putExtra("videopath", info.getVideoPath());
                                intent.putExtra("coverpath", info.getCoverPath());
                                startActivity(intent);
                            } else if (info.getVideoType() == 4) {
                                Intent intent = new Intent(provideActivity(), WebviewActivity.class);
                                intent.putExtra("weburl", info.getVideoPath());
                                intent.putExtra("head", true);
                                startActivity(intent);
                            }
                        }
                    }
                });
            } else {
                headBinding.flayoutThemePics.setVisibility(View.GONE);
            }

        }
    }

    private void dealDatasForDB(List<InnerThemeListBean> datas) {
        StringBuffer srcBuffer = new StringBuffer();
        StringBuffer thumBuffer = new StringBuffer();
        for (InnerThemeListBean data : datas) {
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

    private void dealReplyDatasForDB(List<ThemeDetailReplyBean.DataBean> datas) {
        for (ThemeDetailReplyBean.DataBean data : datas) {
            if (data.getAttch() != null && data.getAttch().size() > 0) {
                for (AttachThemeBean attach : data.getAttch()) {
                    if (attach.getMime() == 1) {
                        data.setVideoType(attach.getMime());
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
            }
        }
    }

    @Override
    public void progress(int precent, int tag) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_see_landlord:
                if (label == 1)
                    refresh(0);
                else {
                    refresh(1);
                }
                break;
            case R.id.iv_chat_click:
                mBinding.editContent.getText().insert(mBinding.editContent.getText().length(), "@");
                break;
            case R.id.flayout_chat_video:
//                ToastUtil.showToast("敬请期待");
                break;
            case R.id.iv_chat_add:
                showAddPicsView();
                break;
            case R.id.iv_chatmenu_pic_emptyadd:
                selectPicsFromPhotos();
                break;
            case R.id.iv_chat_record:
                new RxPermissions(this).request(Manifest.permission.RECORD_AUDIO).subscribe(aBoolean -> {
                    if (aBoolean) {
                        hideKeyboard();
                        mBinding.editContent.clearFocus();
                        mBinding.llayoutAttachFunction.setVisibility(View.GONE);
                        mBinding.llayoutTxt.setVisibility(View.GONE);
                        mBinding.flayoutRecord.setVisibility(View.VISIBLE);
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mBinding.recyclerReply.scrollToPosition(mAdapter.getItemCount() - 1);
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

            case R.id.txt_show_edit:
                showKeyboard(mBinding.editContent);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mBinding.recyclerReply.scrollToPosition(mAdapter.getItemCount() - 1);
                    }
                }, 100);
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
            case R.id.iv_chat_send:
                sendPublishButton();
                break;
        }
    }

    private void showAddPicsView() {
        hideKeyboard();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mBinding.flayoutChatPicRoot.getVisibility() == View.GONE) {
                    mBinding.llayoutChatEmojiRoot.setVisibility(View.GONE);
                    mBinding.ivChatEmoji.setSelected(false);

                    mBinding.flayoutChatPicRoot.setVisibility(View.VISIBLE);
                    mBinding.ivChatAdd.setSelected(true);
                    mBinding.ivChatmenuPicEmptyadd.setVisibility(selectPics.size() > 1 ? View.GONE : View.VISIBLE);
                    mBinding.llayoutChatmenuPicShow.setVisibility(selectPics.size() > 1 ? View.VISIBLE : View.GONE);
                    if (selectPics.size() <= 1)
                        selectPicsFromPhotos();
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mBinding.recyclerReply.scrollToPosition(mAdapter.getItemCount() - 1);
                        }
                    }, 50);
                } else {
                    mBinding.flayoutChatPicRoot.setVisibility(View.GONE);
                    mBinding.ivChatAdd.setSelected(false);
                }
            }
        }, 50);


    }

    private void sendPublishButtonTwo() {
        if (!TextUtils.isEmpty(mBinding.replyRoot.editContent.getWebString().trim())) {
            if (replyPostionOne > 0) {
                realPublish(readyPublish(mBinding.replyRoot.editContent.getWebString().trim(), TextUtils.isEmpty(mBinding.replyRoot.editContent.getWebReplyId()) ? mDatas.get(replyPostionOne - 1).getId() + "" : mBinding.replyRoot.editContent.getWebReplyId(), 2));
            } else if (jpushReplyDetail != null) {
                realPublish(readyPublish(mBinding.replyRoot.editContent.getWebString().trim(), TextUtils.isEmpty(mBinding.replyRoot.editContent.getWebReplyId()) ? jpushReplyDetail.getId() + "" : mBinding.replyRoot.editContent.getWebReplyId(), 2));
            }
        } else {
            ToastUtil.showToast("请填写评论内容");
        }
    }

    private void sendPublishButton() {
        if (!TextUtils.isEmpty(mBinding.editContent.getWebString().trim()) || selectPics.size() > 1) {
            realPublish(readyPublish(mBinding.editContent.getWebString().trim(), mBinding.editContent.getWebReplyId(), 1));

           /* if (selectPics.size() > 1) {
                readySendPics();
            } else {
                publishReply(themeid, mBinding.editContent.getWebReplyId(), mBinding.editContent.getWebString().trim(), null, info.getTerminal_id(), Publish_TAG);
                mBinding.editContent.setText("");
            }*/
        } else {
            ToastUtil.showToast("请填写评论内容");
        }
    }


    private void realPublish(ThemeDetailReplyBean.DataBean info) {
        if (info.getThumb() != null && info.getThumb().size() > 0) {//图片+文本
            if (updataPicMapReal(info.getThumb(), info.getTerminal_id(), uploadpicone_realSend_TAG)) {
                realPublishWithPics(info);
            }
        } else if (info.getVideoType() == 3) {//视频
            if (updataAudioMap(info.getLevel(), 3, info.getVideoPath(), info.getCoverPath(), info.getDuration())) {
                realPublishWithAudio(info);
            }
        } else if (info.getVideoType() == 2) {//语音
            if (updataAudioMap(info.getLevel(), 2, info.getVideoPath(), info.getCoverPath(), info.getDuration())) {
                realPublishWithAudio(info);
            }
        } else {
            publishReply(themeid, info.getReplyId(), info.getContent(), info.getWebPhotos(), info.getTerminal_id(), info.getLevel() == 1 ? Publish_TAG : PublishTwo_Tag);
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

    private void startUploadOneReal(File file, String localpath, long terminal_id, int tag) {
        mPresenter.uploadOneReal(file, localpath, terminal_id, tag);
    }

    //预发布图片文字
    private ThemeDetailReplyBean.DataBean readyPublish(String content, String replyId, int level) {
        ThemeDetailReplyBean.DataBean contentBean = new ThemeDetailReplyBean.DataBean();
        contentBean.setSelf(true);
        contentBean.setSelfStatus(1);
        contentBean.setLevel(level);
        contentBean.setReplyId(replyId);
        ThemeDetailReplyBean.DataBean.UserBean userInfo = new ThemeDetailReplyBean.DataBean.UserBean();
        userInfo.setName(SharedPrefUtil.getString(name_key, ""));
        userInfo.setId(SharedPrefUtil.getInt(userid_key, 0));
        userInfo.setIsme(1);
        userInfo.setPortrait_thumb(SharedPrefUtil.getString(head_key, ""));
        contentBean.setUser(userInfo);
        contentBean.setContent(content);
        contentBean.setAuthor_userid(SharedPrefUtil.getInt(userid_key, 0));
        contentBean.setCreated_at2(FormatUtils.stampToDate("" + System.currentTimeMillis() / 1000));
        contentBean.setTerminal_id(System.currentTimeMillis() / 1000);

        if (level == 1) {
            contentBean.setThumb(new ArrayList<>());
            if (selectPics.size() > 1) {
                contentBean.getThumb().addAll(selectPics);
                contentBean.getThumb().remove(addButton);
                contentBean.setVideoType(1);
            }
//            headBinding.llayoutWithReply.setVisibility(View.VISIBLE);
            mAdapter.addData(contentBean);
            mBinding.recyclerReply.scrollToPosition(mAdapter.getItemCount() - 1);
            selectPics.clear();
            selectPics.add(addButton);
            mBinding.editContent.setText("");
            picAdapter.notifyDataSetChanged();
            updataPicsView();
        } else {
            mAdapterTwo.addData(contentBean);
            mBinding.replyRoot.recyclerReply.scrollToPosition(mAdapterTwo.getItemCount() - 1);
            mBinding.replyRoot.editContent.setText("");
            updataSendButtonTwo();
        }

        return contentBean;
    }

    //预发布语音
    private ThemeDetailReplyBean.DataBean readyPublishVideo(int level, int type, String videoPath, int duration, String replyId) {
        ThemeDetailReplyBean.DataBean contentBean = new ThemeDetailReplyBean.DataBean();
        contentBean.setSelf(true);
        contentBean.setSelfStatus(1);
        contentBean.setReplyId(replyId);
        contentBean.setLevel(level);
        ThemeDetailReplyBean.DataBean.UserBean userInfo = new ThemeDetailReplyBean.DataBean.UserBean();
        userInfo.setName(SharedPrefUtil.getString(name_key, ""));
        userInfo.setId(SharedPrefUtil.getInt(userid_key, 0));
        userInfo.setIsme(1);
        userInfo.setPortrait_thumb(SharedPrefUtil.getString(head_key, ""));
        contentBean.setUser(userInfo);
        contentBean.setAuthor_userid(SharedPrefUtil.getInt(userid_key, 0));
        contentBean.setCreated_at2(FormatUtils.stampToDate("" + System.currentTimeMillis() / 1000));
        contentBean.setTerminal_id(System.currentTimeMillis() / 1000);

        contentBean.setVideoType(type);
        contentBean.setVideoPath(videoPath);
        contentBean.setDuration(duration);
        if (level == 1) {
            mAdapter.addData(contentBean);
            mBinding.recyclerReply.scrollToPosition(mAdapter.getItemCount() - 1);
//            headBinding.llayoutWithReply.setVisibility(View.VISIBLE);
        } else if (level == 2) {
            mAdapterTwo.addData(contentBean);
            mBinding.replyRoot.recyclerReply.scrollToPosition(mAdapterTwo.getItemCount() - 1);
        }
        return contentBean;
    }


    private void realPublishWithAudio(ThemeDetailReplyBean.DataBean info) {
        info.getWebPhotos().clear();
        info.getWebPhotos().add(audioMap.get(info.getVideoPath()).webUrl);
        publishReply(themeid, info.getReplyId(), "", info.getWebPhotos(), info.getTerminal_id(), info.getLevel() == 1 ? PublishPic_TAG : PublishPicTwo_TAG);
    }

    //上传音视频
    private boolean updataAudioMap(int level, int videoType, String videoPath, String coverPath, int duration) {
        boolean isFinish = true;
        VideoInfoPackage web = audioMap.get(videoPath);
        if (web == null) {
            web = new VideoInfoPackage(videoType, videoPath, coverPath, duration);
            audioMap.put(videoPath, web);
        }
        if (TextUtils.isEmpty(web.webUrl)) {
            isFinish = false;
            uploadVideoOne(level, videoType, videoPath, coverPath, duration);
        }
        return isFinish;
    }

    private void uploadVideoOne(int level, int videoType, String videoPath, String coverPath, int duration) {
        TXUGCPublishTypeDef.TXPublishParam param = new TXUGCPublishTypeDef.TXPublishParam();
        // signature计算规则可参考 https://www.qcloud.com/document/product/266/9221
        param.signature = Constant.videoSecret;
        param.videoPath = videoPath;
        param.coverPath = coverPath;
        param.duration = duration;
        param.type = videoType;
        param.level = level;
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

    private void realPublishWithPics(ThemeDetailReplyBean.DataBean info) {
        info.getWebPhotos().clear();
        info.getThumb().remove(addButton);
        for (String key : info.getThumb()) {
            info.getWebPhotos().add(picMap.get(key));
        }
        publishReply(themeid, info.getReplyId(), info.getContent(), info.getWebPhotos(), info.getTerminal_id(), info.getLevel() == 1 ? PublishPic_TAG : PublishPicTwo_TAG);
    }

    private void publishReply(int themeid, String pid, String content, List<String> picList, long terminal_id, int tag) {
        mPresenter.publishReply(themeid, pid, content, picList, terminal_id, tag);

    }

    private void clickEditView() {
        mBinding.llayoutAttachFunction.setVisibility(View.VISIBLE);
        mBinding.flayoutChatPicRoot.setVisibility(View.GONE);
        mBinding.ivChatAdd.setSelected(false);
        mBinding.llayoutChatEmojiRoot.setVisibility(View.GONE);
        mBinding.ivChatEmoji.setSelected(false);
        showKeyboard(mBinding.editContent);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mBinding.recyclerReply.scrollToPosition(mAdapter.getItemCount() - 1);
            }
        }, 200);


    }

    private void clickEditViewTwo() {
//        mBinding.replyRoot.llayoutAttachFunction.setVisibility(View.VISIBLE);
        mBinding.replyRoot.llayoutChatEmojiRoot.setVisibility(View.GONE);
        mBinding.replyRoot.ivChatEmoji.setSelected(false);
        showKeyboardTwo(mBinding.replyRoot.editContent);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mBinding.replyRoot.recyclerReply.scrollToPosition(mAdapterTwo.getItemCount() - 1);
            }
        }, 200);

    }


    private void showOrHideEmojiTwo() {
        hideKeyboard();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mBinding.replyRoot.llayoutChatEmojiRoot.getVisibility() == View.VISIBLE) {
                    mBinding.replyRoot.llayoutChatEmojiRoot.setVisibility(View.GONE);
                    mBinding.replyRoot.ivChatEmoji.setSelected(false);
                } else {
                    mBinding.replyRoot.llayoutChatEmojiRoot.setVisibility(View.VISIBLE);
                    mBinding.replyRoot.ivChatEmoji.setSelected(true);
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mBinding.replyRoot.recyclerReply.scrollToPosition(mAdapter.getItemCount() - 1);
                        }
                    }, 100);
                }
            }
        }, 100);

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
                    mBinding.llayoutChatEmojiRoot.setVisibility(View.VISIBLE);
                    mBinding.ivChatEmoji.setSelected(true);
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mBinding.recyclerReply.scrollToPosition(mAdapter.getItemCount() - 1);
                        }
                    }, 50);
                }
            }
        }, 50);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AT_request) {
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
        } else if (requestCode == AT_request_pop) {
            if (resultCode == RESULT_OK) {
                List<MemberAllBean.DataBean> list = (List<MemberAllBean.DataBean>) data.getSerializableExtra("atmemberlist");
                if (list.size() > 0) {
                    mBinding.replyRoot.editContent.getText().delete(mBinding.replyRoot.editContent.getText().length() - 1, mBinding.replyRoot.editContent.getText().length());
                    for (MemberAllBean.DataBean bean : list) {
                        mBinding.replyRoot.editContent.addAtSpan("@", bean.getUser_name(), bean.getUser_id(), false);
                        mBinding.replyRoot.editContent.getText().insert(mBinding.replyRoot.editContent.getText().length(), " ");
                    }
                }
            }
        } else if (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_Normal_CODE) {
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

        }
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

    private void startUploadOne(File file, String localpath, int tag) {
        mPresenter.upLoadPicOne(file, localpath, tag);
    }

    public void fillHeadDataTwo(ThemeDetailReplyBean.DataBean dataBean) {
        pidTwo = 0;
        mDatasTwo.clear();
        mAdapterTwo.notifyDataSetChanged();
        fillDetailDataTwo(dataBean);

    }

    private void loadMoreTwo(String key) {
        for (int i = mDatasTwo.size() - 1; i >= 0; i--) {
            if (mDatasTwo.get(i).getId() != 0) {
                pidTwo = mDatasTwo.get(i).getId();
                break;
            }
        }
        if (replyPostionOne > 0)
            mPresenter.getReplyTwoList(themeid, mDatas.get(replyPostionOne - 1).getId(), pidTwo, key, ReplyTwoList_Tag);
        else if (jpushReplyDetail != null)
            mPresenter.getReplyTwoList(themeid, jpushReplyDetail.getId(), pidTwo, key, ReplyTwoList_Tag);
        else {
            mAdapterTwo.setEnableLoadMore(false);
            mAdapterTwo.loadMoreEnd();
        }
    }

    private void fillDetailDataTwo(ThemeDetailReplyBean.DataBean info) {
        if (headBindingTwo != null) {
            GlideApp.with(provideActivity()).load(info.getUser().getPortrait_thumb()).placeholder(R.drawable.icon_head_default).error(R.drawable.icon_head_default).into(headBindingTwo.ivHead);
            headBindingTwo.ivHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isPreventGoTwo()) {
                        Intent intent = new Intent(provideActivity(), UserInfoActivity.class);
                        intent.putExtra("userid", info.getAuthor_userid());
                        startActivity(intent);
                    }
                }
            });
            headBindingTwo.txtName.setText(info.getUser().getName());
            mBinding.replyRoot.editContent.setHint("回复 " + info.getUser().getName() + " ...");
//            mBinding.replyRoot.txtShowEdit.setText("回复 " + info.getUser().getName() + " ...");
            mBinding.replyRoot.barTxtTitle.setText(!TextUtils.equals(info.getCommentscn(), "0") ? info.getCommentscn() + "条回复" : "暂无回复");
            headBindingTwo.txtTime.setText(info.getCreated_at2());
            headBindingTwo.txtThemeContent.setVisibility(TextUtils.isEmpty(info.getContent()) ? View.GONE : View.VISIBLE);
            if (!TextUtils.isEmpty(info.getContent())) {
                headBindingTwo.txtThemeContent.setText(SpanUtil.getExpressionString(provideActivity(), info.getContent(),
                        true, headBindingTwo.txtThemeContent));
                headBindingTwo.txtThemeContent.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        QPopuWindow.getInstance(provideActivity()).builder
                                .bindView(v, 0)
                                .setPopupItemList(new String[]{"复制"})
                                .setPointers(rawX, rawY)
                                .setOnPopuListItemClickListener(new QPopuWindow.OnPopuListItemClickListener() {
                                    @Override
                                    public void onPopuListItemClick(View anchorView, int anchorViewPosition, int position) {
                                        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                        ClipData mClipData = ClipData.newPlainText("Label", headBindingTwo.txtThemeContent.getText().toString());
                                        cm.setPrimaryClip(mClipData);
                                        ToastUtil.showToast("已复制");
                                    }
                                }).show();
                        return true;
                    }
                });
            }
            headBindingTwo.ivReplyZan.setSelected(info.getIslike() == 1);
            headBindingTwo.txtReplyZanNum.setVisibility(!TextUtils.equals(info.getLikecn(), "0") ? View.VISIBLE : View.GONE);
            headBindingTwo.txtReplyZanNum.setText(info.getLikecn() + "");

            headBindingTwo.llayoutReplyZan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.likeOperate(themeid, info.getIslike(), info.getId(), ReplyLike_Tag);
                    if (info.getIslike() == 1) {
                        info.setIslike(0);
                        try {
                            info.setLikecn((Integer.parseInt(info.getLikecn()) - 1) + "");
                        } catch (Exception e) {
                        }
                        ToastUtil.showToast("取消点赞");
                    } else {
                        info.setIslike(1);
                        try {
                            info.setLikecn((Integer.parseInt(info.getLikecn()) + 1) + "");
                        } catch (Exception e) {
                        }
                        ToastUtil.showToast("点赞成功");
                    }
                    headBindingTwo.ivReplyZan.setSelected(info.getIslike() == 1);
                    headBindingTwo.txtReplyZanNum.setVisibility(!TextUtils.equals(info.getLikecn(), "0") ? View.VISIBLE : View.GONE);
                    headBindingTwo.txtReplyZanNum.setText(info.getLikecn() + "");
                    if (replyPostionOne > 0)
                        mAdapter.notifyItemChanged(replyPostionOne);
                }
            });
            mBinding.replyRoot.ivThemeMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideKeyboard();
                    showOperatePopTwo(-1, THEME_TYPE);
                }
            });


            if (info.getVideoType() == 1) {
                headBindingTwo.flayoutThemePics.setVisibility(View.VISIBLE);
                headBindingTwo.fllayoutThemeVideo.setVisibility(View.GONE);
                headBindingTwo.llayoutAudioRoot.setVisibility(View.GONE);

                int size = info.getThumb().size();
                if (size == 1) {
                    headBindingTwo.recycleThemePics.setVisibility(View.GONE);
                    headBindingTwo.flayoutOneParent.setVisibility(View.VISIBLE);
                    try {
                        Uri uri = Uri.parse(info.getSrc().get(0));
                        String s = uri.getQueryParameter("s");
                        if (!TextUtils.isEmpty(s)) {
                            headBindingTwo.txtLongpic.setVisibility(Integer.parseInt(s) == 1 ? View.VISIBLE : View.GONE);
                        } else {
                            headBindingTwo.txtLongpic.setVisibility(View.GONE);
                        }
                        int width = Integer.parseInt(uri.getQueryParameter("sw"));
                        int height = Integer.parseInt(uri.getQueryParameter("sh"));
                        headBindingTwo.ivThemeOnepic.getLayoutParams().width = width;
                        headBindingTwo.ivThemeOnepic.getLayoutParams().height = height;
                    } catch (Exception e) {
                        headBindingTwo.txtLongpic.setVisibility(View.GONE);
                    }
                    GlideApp.with(provideActivity()).load(info.getThumb().get(0)).centerCrop().placeholder(R.drawable.icon_theme_default_big).error(R.drawable.icon_theme_default_big).into(headBindingTwo.ivThemeOnepic);

                    headBindingTwo.ivThemeOnepic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            imagesDialog.setPicList(info.getSrc(), info.getThumb(), 0);
                            imagesDialog.show();
                        }
                    });
                } else {
                    headBindingTwo.recycleThemePics.setVisibility(View.VISIBLE);
                    headBindingTwo.flayoutOneParent.setVisibility(View.GONE);
                    ChatPicsAdapter picsAdapter = new ChatPicsAdapter(provideActivity(), info.getThumb());
                    headBindingTwo.recycleThemePics.setLayoutManager(new GridLayoutManager(provideActivity(), 3) {
                        @Override
                        public boolean canScrollVertically() {
                            return false;
                        }
                    });
                    headBindingTwo.recycleThemePics.setNestedScrollingEnabled(false);
                    headBindingTwo.recycleThemePics.setHasFixedSize(true);
                    headBindingTwo.recycleThemePics.setAdapter(picsAdapter);
                    picsAdapter.setmListen(new ChatPicsAdapter.onItemOperateListen() {
                        @Override
                        public void operateItem(int position, View v) {
                            imagesDialog.setPicList(info.getSrc(), info.getThumb(), position);
                            imagesDialog.show();

                        }
                    });
                }

            } else if (info.getVideoType() == 2) {
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                headBindingTwo.flayoutThemePics.setVisibility(View.VISIBLE);
                headBindingTwo.flayoutOneParent.setVisibility(View.GONE);
                headBindingTwo.recycleThemePics.setVisibility(View.GONE);
                headBindingTwo.fllayoutThemeVideo.setVisibility(View.GONE);
                headBindingTwo.llayoutAudioRoot.setVisibility(View.VISIBLE);
                headBindingTwo.voiceLengthTv.setText(info.getDuration() + getString(R.string.jmui_symbol_second));

                //控制语音长度显示，长度增幅随语音长度逐渐缩小
                int width = (int) (-0.04 * info.getDuration() * info.getDuration() + 4.526 * info.getDuration() + 75.214);
                headBindingTwo.voiceIv.setImageResource(R.drawable.jmui_send_3);
                headBindingTwo.msgContent.setWidth((int) (width * dm.density));
                headBindingTwo.voiceReadnew.setVisibility(info.getAudioread() == 1 ? View.GONE : View.VISIBLE);
                headBindingTwo.voiceFl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                            Toast.makeText(provideActivity(), getString(R.string.jmui_sdcard_not_exist_toast),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            if (mAdapterTwo.isplaying())
                                mAdapterTwo.stopMediaPlayer();
                            if (info.getAudioread() != 1) {
                                mPresenter.readThemeAudio(themeid, info.getId(), ReadTheme_TAG);
                                headBindingTwo.voiceReadnew.setVisibility(View.GONE);
                                info.setAudioread(1);
                            }
                            // 如果之前存在播放动画，无论这次点击触发的是暂停还是播放，停止上次播放的动画
                            if (mVoiceAnimation != null) {
                                mVoiceAnimation.stop();
                                mVoiceAnimation = null;
                            }
                            // 播放中点击了正在播放的Item 则暂停播放
                            if (mp.isPlaying()) {
                                headBindingTwo.voiceIv.setImageResource(R.drawable.jmui_voice_send);
                                mVoiceAnimation = (AnimationDrawable) headBindingTwo.voiceIv.getDrawable();
                                pauseVoice();
                                if (mVoiceAnimation != null) {
                                    mVoiceAnimation.stop();
                                    mVoiceAnimation = null;
                                }
                                // 开始播放录音
                            } else {
                                try {
                                    // 继续播放之前暂停的录音
                                    if (mSetData) {
                                        headBindingTwo.voiceIv.setImageResource(R.drawable.jmui_voice_send);
                                        mVoiceAnimation = (AnimationDrawable) headBindingTwo.voiceIv.getDrawable();

                                        if (mVoiceAnimation != null) {
                                            mVoiceAnimation.start();
                                        }
                                        playVoice();
                                        // 否则重新播放该录音或者其他录音
                                    } else {
                                        mp.reset();
                                        // 记录播放录音的位置
                                        mp.setDataSource(provideActivity(), Uri.parse(info.getVideoPath()));
                                        if (App.getInstance().mIsEarPhoneOn) {
                                            mp.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
                                        } else {
                                            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                        }
                                        headBindingTwo.voiceIv.setImageResource(R.drawable.jmui_voice_loading_send);
                                        mVoiceAnimation = (AnimationDrawable) headBindingTwo.voiceIv.getDrawable();
                                        if (mVoiceAnimation != null) {
                                            mVoiceAnimation.start();
                                        }
                                        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                            @Override
                                            public void onPrepared(MediaPlayer mp) {
                                                headBindingTwo.voiceIv.setImageResource(R.drawable.jmui_voice_send);
                                                mVoiceAnimation = (AnimationDrawable) headBindingTwo.voiceIv.getDrawable();
                                                if (mVoiceAnimation != null) {
                                                    mVoiceAnimation.start();
                                                }
                                                playVoice();
                                            }
                                        });
                                        mp.prepareAsync();

                                    }
                                } catch (NullPointerException e) {
                                    if (mVoiceAnimation != null) {
                                        mVoiceAnimation.stop();
                                        mVoiceAnimation = null;
                                    }
                                    headBindingTwo.voiceIv.setImageResource(R.drawable.jmui_send_3);
                                    ToastUtil.showToast(getString(R.string.jmui_file_not_found_toast));
                                } catch (IllegalArgumentException e) {
                                    if (mVoiceAnimation != null) {
                                        mVoiceAnimation.stop();
                                        mVoiceAnimation = null;
                                    }
                                    headBindingTwo.voiceIv.setImageResource(R.drawable.jmui_send_3);
                                    e.printStackTrace();
                                } catch (SecurityException e) {
                                    if (mVoiceAnimation != null) {
                                        mVoiceAnimation.stop();
                                        mVoiceAnimation = null;
                                    }
                                    headBindingTwo.voiceIv.setImageResource(R.drawable.jmui_send_3);
                                    e.printStackTrace();
                                } catch (IllegalStateException e) {
                                    if (mVoiceAnimation != null) {
                                        mVoiceAnimation.stop();
                                        mVoiceAnimation = null;
                                    }
                                    headBindingTwo.voiceIv.setImageResource(R.drawable.jmui_send_3);
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    if (mVoiceAnimation != null) {
                                        mVoiceAnimation.stop();
                                        mVoiceAnimation = null;
                                    }
                                    headBindingTwo.voiceIv.setImageResource(R.drawable.jmui_send_3);
                                    ToastUtil.showToast(getString(R.string.jmui_file_not_found_toast));
                                }
                            }
                        }
                    }

                    private void playVoice() {
                        mp.start();
                        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer arg0) {
                                if (mVoiceAnimation != null) {
                                    mVoiceAnimation.stop();
                                    mVoiceAnimation = null;
                                }
                                mp.reset();
                                mSetData = false;
                                // 播放完毕，恢复初始状态
                                headBindingTwo.voiceIv.setImageResource(R.drawable.jmui_send_3);
                            }
                        });
                    }

                    private void pauseVoice() {
                        mp.pause();
                        mSetData = true;
                    }
                });

            } else if (info.getVideoType() == 3 || info.getVideoType() == 4) {
                headBindingTwo.flayoutThemePics.setVisibility(View.VISIBLE);
                headBindingTwo.flayoutOneParent.setVisibility(View.GONE);
                headBindingTwo.recycleThemePics.setVisibility(View.GONE);
                headBindingTwo.llayoutAudioRoot.setVisibility(View.GONE);
                headBindingTwo.fllayoutThemeVideo.setVisibility(View.VISIBLE);
                try {
                    Uri uri = Uri.parse(info.getVideoPath());
                    Float width = Float.parseFloat(uri.getQueryParameter("w"));
                    Float height = Float.parseFloat(uri.getQueryParameter("h"));
                    if (height > width) {
                        headBindingTwo.fllayoutThemeVideo.getLayoutParams().width = ArmsUtils.dip2px(provideActivity(), 136);
                        headBindingTwo.fllayoutThemeVideo.getLayoutParams().height = ArmsUtils.dip2px(provideActivity(), 181);
                    } else {
                        headBindingTwo.fllayoutThemeVideo.getLayoutParams().width = ArmsUtils.dip2px(provideActivity(), 181);
                        headBindingTwo.fllayoutThemeVideo.getLayoutParams().height = ArmsUtils.dip2px(provideActivity(), 136);
                    }
                } catch (Exception e) {
                    headBindingTwo.fllayoutThemeVideo.getLayoutParams().width = ArmsUtils.dip2px(provideActivity(), 181);
                    headBindingTwo.fllayoutThemeVideo.getLayoutParams().height = ArmsUtils.dip2px(provideActivity(), 136);
                }
                headBindingTwo.txtVideoDuration.setText(info.getDuration() != 0 ? info.getDuration() + "\"" : "");
                if (!info.getCoverPath().startsWith("http"))
                    GlideApp.with(provideActivity()).load(Uri.fromFile(new File(info.getCoverPath()))).override(540, 408).centerCrop().into(headBindingTwo.ivThemeVideopath);
                else
                    GlideApp.with(provideActivity()).load(info.getCoverPath()).centerCrop().override(540, 408).into(headBindingTwo.ivThemeVideopath);
                headBindingTwo.ivThemeVideopathPreview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isPreventGoTwo()) {
                            if (info.getVideoType() == 3) {
                                Intent intent = new Intent(provideActivity(), NewVodPlayerActivity.class);
                                intent.putExtra("frompath", 4);
                                intent.putExtra("videopath", info.getVideoPath());
                                intent.putExtra("coverpath", info.getCoverPath());
                                startActivity(intent);
                            } else if (info.getVideoType() == 4) {
                                Intent intent = new Intent(provideActivity(), WebviewActivity.class);
                                intent.putExtra("weburl", info.getVideoPath());
                                intent.putExtra("head", true);
                                startActivity(intent);
                            }
                        }
                    }
                });
            } else {
                headBindingTwo.flayoutThemePics.setVisibility(View.GONE);
            }


        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KEYCODE_BACK)) {
            if (mBinding.replyRoot.llayoutRoot.getVisibility() == View.VISIBLE) {
                closeReplyView(0);
                return true;
            }
            return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (onBackPressedself()) {
            return;
        }
        super.onBackPressed();
    }

    public boolean onBackPressedself() {
        if (imagesDialog != null && imagesDialog.isShowing()) {
            imagesDialog.dismiss();
            return true;
        }
        return false;
    }

    @Override
    protected boolean adapterFitSystemWindows() {
        return true;
    }

    private boolean isPreventGo() {
        return !isSoftShowing() && mBinding.flayoutChatPicRoot.getVisibility() == View.GONE
                && mBinding.llayoutChatEmojiRoot.getVisibility() == View.GONE
                && mBinding.flayoutRecord.getVisibility() == View.GONE;
    }

    private boolean isPreventGoTwo() {
        return !isSoftShowing()
                && mBinding.replyRoot.llayoutChatEmojiRoot.getVisibility() == View.GONE
                && mBinding.replyRoot.flayoutRecord.getVisibility() == View.GONE;
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
    public boolean dispatchTouchEvent(MotionEvent ev) {
        rawX = (int) ev.getRawX();
        rawY = (int) ev.getRawY();
        return super.dispatchTouchEvent(ev);
    }
}
