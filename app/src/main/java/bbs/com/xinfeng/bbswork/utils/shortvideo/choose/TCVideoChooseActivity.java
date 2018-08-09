package bbs.com.xinfeng.bbswork.utils.shortvideo.choose;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.domin.VideoSelectBus;
import bbs.com.xinfeng.bbswork.module.GlideApp;
import bbs.com.xinfeng.bbswork.utils.LogUtil;
import bbs.com.xinfeng.bbswork.utils.ScreenUtils;

public class TCVideoChooseActivity extends Activity implements View.OnClickListener, ActivityCompat.OnRequestPermissionsResultCallback {
    private static final String TAG = TCVideoChooseActivity.class.getSimpleName();

    public static final int TYPE_SINGLE_CHOOSE = 0;
    public static final int TYPE_MULTI_CHOOSE = 1;
    public static final int TYPE_PUBLISH_CHOOSE = 2; //

    private Button mBtnOk;
    private RecyclerView mRecyclerView;
    private TextView mTvRight;

    private int mType;

    private TCVideoEditerListAdapter mAdapter;

    private Handler mHandler;
    private HandlerThread mHandlerThread;

    private Handler mMainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ArrayList<TCVideoFileInfo> fileInfoArrayList = (ArrayList<TCVideoFileInfo>) msg.obj;
            mAdapter.addAll(fileInfoArrayList);
        }
    };

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_ugc_video_list);

        mHandlerThread = new HandlerThread("LoadList");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());

        mType = getIntent().getIntExtra("CHOOSE_TYPE", TYPE_SINGLE_CHOOSE);
        EventBus.getDefault().register(this);
        init();
        loadVideoList();
//        copyLicenceToSdcard();
    }

    private void copyLicenceToSdcard() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                String sdcardFolder = getExternalFilesDir(null).getAbsolutePath();
                File sdcardLicenceFile = new File(sdcardFolder + File.separator + TCConstants.UGC_LICENCE_NAME);
                if (sdcardLicenceFile.exists()) {
                    return;
                }
                try {
                    FileUtils.copyFromAssetToSdcard(TCVideoChooseActivity.this, TCConstants.UGC_LICENCE_NAME, sdcardFolder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(VideoSelectBus e) {
        if (e.isFinish) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
        mHandlerThread.quit();
        mHandlerThread = null;
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void loadVideoList() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    ArrayList<TCVideoFileInfo> fileInfoArrayList = TCVideoEditerMgr.getAllVideo(TCVideoChooseActivity.this);
                    fileInfoArrayList.add(0, new TCVideoFileInfo());
                    Message msg = new Message();
                    msg.obj = fileInfoArrayList;
                    mMainHandler.sendMessage(msg);
                }
            });
        } else {
            if (Build.VERSION.SDK_INT >= 23) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults != null && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadVideoList();
        }
    }

    private void init() {
        immerseUI(getResources().getColor(R.color.line_low_color));
        TextView txtTitle = (TextView) findViewById(R.id.bar_txt_title);
        txtTitle.setText("相机胶卷");
        findViewById(R.id.bar_left_click).setVisibility(View.VISIBLE);
        findViewById(R.id.bar_left_pic).setVisibility(View.INVISIBLE);
        TextView txtLeft = findViewById(R.id.bar_left_txt);
        txtLeft.setText("取消");
        findViewById(R.id.bar_left_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /*LinearLayout backLL = (LinearLayout) findViewById(R.id.back_ll);
        backLL.setOnClickListener(this);

        mBtnOk = (Button) findViewById(R.id.btn_ok);
        mBtnOk.setOnClickListener(this);

        mTvRight = (TextView) findViewById(R.id.tv_right);
        mTvRight.setOnClickListener(this);*/

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mAdapter = new TCVideoEditerListAdapter(TCVideoChooseActivity.this);
        mRecyclerView.setAdapter(mAdapter);

        if (mType == TYPE_SINGLE_CHOOSE || mType == TYPE_PUBLISH_CHOOSE) {
            mAdapter.setMultiplePick(false);
        } else {
            mAdapter.setMultiplePick(true);
        }

       /* if (mType == TYPE_PUBLISH_CHOOSE) {
            mTvRight.setText("回放");
            mTvRight.setVisibility(View.VISIBLE);
        }*/
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_ok:
                doSelect();
                break;
            case R.id.back_ll:
                finish();
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        GlideApp.with(this).pauseRequests();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GlideApp.with(this).resumeRequests();
    }

    private void doSelect() {
        /*if (mType == TYPE_SINGLE_CHOOSE) {
            Intent intent = new Intent(this, TCVideoPreprocessActivity.class);
            TCVideoFileInfo fileInfo = mAdapter.getSingleSelected();
            if (fileInfo == null) {
                LogUtil.d(TAG, "select file null");
                return;
            }
            if (isVideoDamaged(fileInfo)) {
                showErrorDialog("该视频文件已经损坏");
                return;
            }
            File file = new File(fileInfo.getFilePath());
            if (!file.exists()) {
                showErrorDialog("选择的文件不存在");
                return;
            }
            intent.putExtra(TCConstants.VIDEO_EDITER_PATH, fileInfo.getFilePath());
            startActivity(intent);
        } else if(mType == TYPE_MULTI_CHOOSE){
            Intent intent = new Intent(this, TCVideoJoinerActivity.class);
            ArrayList<TCVideoFileInfo> videoFileInfos = mAdapter.getMultiSelected();
            if (videoFileInfos == null || videoFileInfos.size() == 0) {
                LogUtil.d(TAG, "select file null");
                return;
            }
            if (videoFileInfos.size() < 2) {
                Toast.makeText(this, "必须选择两个以上视频文件", Toast.LENGTH_SHORT).show();
                return;
            }
            if (isVideoDamaged(videoFileInfos)) {
                showErrorDialog("包含已经损坏的视频文件");
                return;
            }
            for (TCVideoFileInfo info : videoFileInfos) {
                File file = new File(info.getFilePath());
                if (!file.exists()) {
                    showErrorDialog("选择的文件不存在");
                    return;
                }
            }
            intent.putExtra(TCConstants.INTENT_KEY_MULTI_CHOOSE, videoFileInfos);
            startActivity(intent);
        }else if(mType == TYPE_PUBLISH_CHOOSE){
            Intent intent = new Intent(this, TCCompressActivity.class);
            TCVideoFileInfo fileInfo = mAdapter.getSingleSelected();
            if (fileInfo == null) {
                LogUtil.d(TAG, "select file null");
                return;
            }
            if (isVideoDamaged(fileInfo)) {
                showErrorDialog("该视频文件已经损坏");
                return;
            }
            File file = new File(fileInfo.getFilePath());
            if (!file.exists()) {
                showErrorDialog("选择的文件不存在");
                return;
            }
            intent.putExtra(TCConstants.VIDEO_EDITER_PATH, fileInfo.getFilePath());
            startActivity(intent);
        }
        finish();*/
    }

    /**
     * 检测视频是否损坏
     *
     * @param info
     * @return
     */
    private boolean isVideoDamaged(TCVideoFileInfo info) {
        if (info.getDuration() == 0) {
            //数据库获取到的时间为0，使用Retriever再次确认是否损坏
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            try {
                retriever.setDataSource(info.getFilePath());
            } catch (Exception e) {
                return true;//无法正常打开，也是错误
            }
            String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            if (TextUtils.isEmpty(duration))
                return true;
            return Integer.valueOf(duration) == 0;
        }
        return false;
    }

    private boolean isVideoDamaged(List<TCVideoFileInfo> list) {
        for (TCVideoFileInfo info : list) {
            if (isVideoDamaged(info)) {
                return true;
            }
        }
        return false;
    }

    private void showErrorDialog(String msg) {
        AlertDialog.Builder normalDialog = new AlertDialog.Builder(TCVideoChooseActivity.this, R.style.ConfirmDialogStyle);
        normalDialog.setMessage(msg);
        normalDialog.setCancelable(false);
        normalDialog.setPositiveButton("知道了", null);
        normalDialog.show();
    }

    public void immerseUI(int color) {
//        ViewGroup rootView = getWindow().getDecorView().findViewById(android.R.id.content);
//        rootView.setPadding(0, ScreenUtils.getStatusBarHeight(this), 0, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0 以上直接设置状态栏颜色
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(color);
        } else {
            ViewGroup rootView = getWindow().getDecorView().findViewById(android.R.id.content);
            rootView.setPadding(0, ScreenUtils.getStatusBarHeight(this), 0, 0);
            //根布局添加占位状态栏
            ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
            View statusBarView = new View(this);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ScreenUtils.getStatusBarHeight(this));
            statusBarView.setBackgroundColor(color);
            decorView.addView(statusBarView, lp);
        }

    }
}
