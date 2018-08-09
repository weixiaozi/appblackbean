package bbs.com.xinfeng.bbswork.utils.shortvideo.videoplay;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tencent.rtmp.TXPlayerAuthBuilder;
import com.tencent.ugc.TXRecordCommon;
import com.tencent.ugc.TXVideoEditConstants;
import com.tencent.ugc.TXVideoEditer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.domin.VideoSelectBus;
import bbs.com.xinfeng.bbswork.utils.shortvideo.choose.TCConstants;
import bbs.com.xinfeng.bbswork.utils.shortvideo.choose.TCVideoFileInfo;
import bbs.com.xinfeng.bbswork.utils.shortvideo.videorecord.TCVideoCutterActivity;
import bbs.com.xinfeng.bbswork.utils.shortvideo.videorecord.utils.PlayState;
import bbs.com.xinfeng.bbswork.utils.shortvideo.videorecord.utils.TCEditerUtil;
import bbs.com.xinfeng.bbswork.utils.shortvideo.videorecord.utils.TCVideoEditerWrapper;
import bbs.com.xinfeng.bbswork.utils.shortvideo.videorecord.view.VideoWorkProgressFragment;

/**
 * Created by annidy on 2017/12/27.
 */

public class NewVodPlayerActivity extends FragmentActivity implements View.OnClickListener, TXVideoEditer.TXVideoGenerateListener {
    public static final String TAG = "NewVodPlayerActivity";
    private SuperVideoPlayer mSuperVideoPlayer;
    private ImageView mIvBack;
    private ImageView iv_delete;

    private boolean isPlayDefaultVideo = true; // true:播放默认视频, false:播放从demo上传入口上传的视频
    private String videoIdFrom;
    private String titleFrom;
    private String videoPath;
    private String mVideoOutputPath;
    private VideoWorkProgressFragment mWorkLoadingProgress;
    private TXVideoEditer mTXVideoEditer;
    public int videoDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏

        setContentView(R.layout.activity_new_vod);
        EventBus.getDefault().register(this);
        initView();
        getWindow().addFlags(WindowManager.LayoutParams.
                FLAG_KEEP_SCREEN_ON);
    }


    private void initView() {
        mSuperVideoPlayer = (SuperVideoPlayer) findViewById(R.id.video_player_item_1);
        mSuperVideoPlayer.setVideoPlayCallback(mVideoPlayCallback);


        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvBack.setOnClickListener(this);
        iv_delete = (ImageView) findViewById(R.id.iv_delete);
        iv_delete.setOnClickListener(this);

        mSuperVideoPlayer.setVideoPlayInfoCallback(new SuperVideoPlayer.OnPlayInfoCallback() {


            @Override
            public void onBack() {
                finish();
            }

            @Override
            public void onGoEdit() {
                Intent intent = new Intent(NewVodPlayerActivity.this, TCVideoCutterActivity.class);
                intent.putExtra(TCConstants.VIDEO_EDITER_PATH, videoPath);
                startActivity(intent);
            }

            @Override
            public void onVideoSure(int duration) {
                videoDuration = duration;
                startGenerateVideo(duration);
            }
        });

        int frompath = getIntent().getIntExtra("frompath", 1);
        if (frompath == 1) {//录制界面
            videoPath = getIntent().getStringExtra("videopath");
            mSuperVideoPlayer.setThumVideo(videoPath);
            mSuperVideoPlayer.isPreview(1);
            playVideoWithUrl(videoPath, true);
            mIvBack.setVisibility(View.GONE);
        } else if (frompath == 2) {//选择视频界面
            TCVideoFileInfo sourceInfo = (TCVideoFileInfo) getIntent().getSerializableExtra(TCConstants.VIDEO_PLAY_PATH);
            videoPath = sourceInfo.getFilePath();
            mSuperVideoPlayer.setThumVideo(videoPath);
            mSuperVideoPlayer.isPreview(2);
            playVideoWithUrl(videoPath, false);
        } else if (frompath == 3) {//已选视频预览
            iv_delete.setVisibility(View.VISIBLE);
            mIvBack.setImageResource(R.drawable.icon_preview_back);
            videoPath = getIntent().getStringExtra("videopath");
            mSuperVideoPlayer.setThumVideo(videoPath);
            mSuperVideoPlayer.isPreview(3);
            playVideoWithUrl(videoPath, false);
        } else if (frompath == 4) {//网络
            videoPath = getIntent().getStringExtra("videopath");
            mSuperVideoPlayer.setThumVideo(getIntent().getStringExtra("coverpath"));
            mSuperVideoPlayer.isPreview(4);
            playVideoWithUrl(videoPath, true);
        }

//        playVideo();
    }


    private void playVideoWithFileId(TXPlayerAuthParam param) {
        TXPlayerAuthBuilder authBuilder = new TXPlayerAuthBuilder();
        try {
            if (!TextUtils.isEmpty(param.appId)) {
                authBuilder.setAppId(Integer.parseInt(param.appId));
            }
            String fileId = param.fileId;
            authBuilder.setFileId(fileId);

            if (mSuperVideoPlayer != null) {
                mSuperVideoPlayer.playFileID(authBuilder);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSuperVideoPlayer.loadVideo();
                    }
                });
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "请输入正确的AppId和FileId", Toast.LENGTH_SHORT).show();
        }
    }

    private void playVideoWithUrl(String url, boolean isAutoPlay) {
        if (mSuperVideoPlayer != null) {
            mSuperVideoPlayer.setPlayUrl(url, isAutoPlay);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mSuperVideoPlayer.loadVideo();
                }
            });
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mSuperVideoPlayer != null) {
            mSuperVideoPlayer.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mSuperVideoPlayer != null) {
            mSuperVideoPlayer.onPause();
        }
    }

    private SuperVideoPlayer.VideoPlayCallbackImpl mVideoPlayCallback = new SuperVideoPlayer.VideoPlayCallbackImpl() {
        @Override
        public void onCloseVideo() {
            mSuperVideoPlayer.onDestroy();
            mSuperVideoPlayer.setVisibility(View.GONE);
            resetPageToPortrait();
        }

        @Override
        public void onSwitchPageType() {
            if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                mSuperVideoPlayer.setPageType(MediaController.PageType.SHRINK);
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                mSuperVideoPlayer.setPageType(MediaController.PageType.EXPAND);
            }
        }

        @Override
        public void onPlayFinish() {
        }

        @Override
        public void onBack() {
            if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                mSuperVideoPlayer.setPageType(MediaController.PageType.SHRINK);
            } else {
                finish();
            }
        }

        @Override
        public void onLoadVideoInfo(VodRspData data) {
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_delete:
                EventBus.getDefault().post(new VideoSelectBus(true, ""));
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null || data.getExtras() == null || TextUtils.isEmpty(data.getExtras().getString("result"))) {
            return;
        }
    }


    private void playVideo() {
        mSuperVideoPlayer.setVisibility(View.VISIBLE);
        mSuperVideoPlayer.setAutoHideController(false);
        playVideoWithUrl("/storage/emulated/0/TXUGC/TXUGC_20180530_181037643.mp4", true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSuperVideoPlayer != null) {
            mSuperVideoPlayer.onDestroy();
        }
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(VideoSelectBus e) {
        if (e.isFinish) {
            finish();
        }
    }

    /***
     * 旋转屏幕之后回调
     *
     * @param newConfig newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (null == mSuperVideoPlayer) return;
        /***
         * 根据屏幕方向重新设置播放器的大小
         */
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().getDecorView().invalidate();
            float height = DensityUtil.getWidthInPx(this);
            float width = DensityUtil.getHeightInPx(this);
            mSuperVideoPlayer.getLayoutParams().height = (int) width;
            mSuperVideoPlayer.getLayoutParams().width = (int) height;
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            final WindowManager.LayoutParams attrs = getWindow().getAttributes();
            attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(attrs);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            float width = DensityUtil.getWidthInPx(this);
            float height = DensityUtil.dip2px(this, 200.f);
            mSuperVideoPlayer.getLayoutParams().height = (int) height;
            mSuperVideoPlayer.getLayoutParams().width = (int) width;
        }
    }

    /***
     * 恢复屏幕至竖屏
     */
    private void resetPageToPortrait() {
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            mSuperVideoPlayer.setPageType(MediaController.PageType.SHRINK);
        }
    }

    private void startGenerateVideo(int duration) {
        // 停止播放
        if (mSuperVideoPlayer != null) {
            mSuperVideoPlayer.onPause();
        }
//        TCVideoEditerWrapper wrapper = TCVideoEditerWrapper.getInstance();
//
//        mTXVideoEditer = wrapper.getEditer();

        mTXVideoEditer = new TXVideoEditer(this);
        mTXVideoEditer.setVideoPath(videoPath);
        /*if (mTXVideoEditer == null || wrapper.getTXVideoInfo() == null) {
            Toast.makeText(this, "状态异常，结束编辑", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }*/
        // 生成视频输出路径
        mVideoOutputPath = TCEditerUtil.generateVideoPath();

        if (mWorkLoadingProgress == null) {
            initWorkLoadingProgress();
        }
        mWorkLoadingProgress.setProgress(0);
        mWorkLoadingProgress.setCancelable(false);
        mWorkLoadingProgress.show(getSupportFragmentManager(), "progress_dialog");

        mTXVideoEditer.setCutFromTime(0, duration * 1000);
        mTXVideoEditer.setVideoGenerateListener(this);

        mTXVideoEditer.setVideoBitrate(1300);
        mTXVideoEditer.generateVideo(TXVideoEditConstants.VIDEO_COMPRESSED_360P, mVideoOutputPath);
    }

    private void initWorkLoadingProgress() {
        if (mWorkLoadingProgress == null) {
            mWorkLoadingProgress = new VideoWorkProgressFragment();
            mWorkLoadingProgress.setOnClickStopListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stopGenerate();
                }
            });
        }
        mWorkLoadingProgress.setProgress(0);
    }

    private void stopGenerate() {
        mWorkLoadingProgress.dismiss();
        mWorkLoadingProgress.setProgress(0);
        if (mTXVideoEditer != null) {
            mTXVideoEditer.cancel();
        }
    }

    @Override
    public void onGenerateProgress(float progress) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mWorkLoadingProgress.setProgress((int) (progress * 100));
            }
        });
    }

    @Override
    public void onGenerateComplete(TXVideoEditConstants.TXGenerateResult result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mWorkLoadingProgress != null && mWorkLoadingProgress.isAdded()) {
                    mWorkLoadingProgress.dismiss();
                }
                if (result.retCode == TXVideoEditConstants.GENERATE_RESULT_OK) {
                    // 生成成功
                    EventBus.getDefault().post(new VideoSelectBus(true, mVideoOutputPath, videoDuration));
                } else {
                    Toast.makeText(NewVodPlayerActivity.this, result.descMsg, Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
