package bbs.com.xinfeng.bbswork.utils.shortvideo.videoplay;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.tencent.rtmp.TXBitrateItem;

import java.util.ArrayList;

import bbs.com.xinfeng.bbswork.R;

/**
 * Created by annidy on 2018/1/2.
 */

public class ResolutionPanel extends FrameLayout implements View.OnClickListener {

    private Context mContext;
    private ArrayList<Button> mButtons;
    private Button mCurBtn;
    private Button mLastBtn;
    private boolean mChangeResolution = false;
    private int mLastSize = 0;


    public ResolutionPanel(Context context) {
        super(context);
        mContext = context;

        initView(context);
    }

    public ResolutionPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        initView(context);
    }

    public ResolutionPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        initView(context);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.biz_video_media_resolution_panel, this);

        mButtons = new ArrayList<>();
        mButtons.add((Button) findViewById(R.id.button_r1));
        mButtons.add((Button) findViewById(R.id.button_r2));
        mButtons.add((Button) findViewById(R.id.button_r3));
        mButtons.add((Button) findViewById(R.id.button_r4));

        for (int i = 0; i < mButtons.size(); i++) {
            mButtons.get(i).setVisibility(GONE);
            mButtons.get(i).setOnClickListener(this);
        }
        mCurBtn = mButtons.get(0);
        mLastBtn = mCurBtn;
    }

    private ArrayList<TXBitrateItem> mSource;

    public void setDataSource(ArrayList<TXBitrateItem> source) {
        int size = 0;
        if (source == null || source.size() == 0) {
            size = 0;
        }
        if (mLastSize == 0 || mLastSize != size) {
            for (int i = 0; i < mButtons.size(); i++) {
                mButtons.get(i).setVisibility(GONE);
            }
        }
        mLastSize = size;

        if (source == null || source.size() == 0) {
            mButtons.get(1).setVisibility(View.VISIBLE);
            mCurBtn = mButtons.get(1);
            mLastBtn = mCurBtn;
            return;
        }
        mSource = new ArrayList<>(source);
        for (int i = 0; i < source.size(); i++) {
            mButtons.get(i).setVisibility(VISIBLE);
        }
        if (!mChangeResolution) {
            if (mLastBtn != null) {
                mLastBtn.setBackground(null);
                mLastBtn.setTextColor(getResources().getColor(R.color.white));
                mCurBtn = mButtons.get(0);
                mLastBtn = mCurBtn;
            }
            mChangeResolution = false;
        }
    }

    @Override
    public void onClick(View v) {
        if (mCurBtn == v)
            return;

        if (mLastBtn != null) {
            mLastBtn.setBackground(null);
            mLastBtn.setTextColor(getResources().getColor(R.color.white));
        }
        mCurBtn = (Button) v;

        int index = 0;
        switch (mCurBtn.getId()) {
            case R.id.button_r1:
                index = 0;
                break;
            case R.id.button_r2:
                index = 1;
                break;
            case R.id.button_r3:
                index = 2;
                break;
            case R.id.button_r4:
                index = 3;
                break;
        }

        if (mImp != null && mSource.size() > 0) {
            TXBitrateItem item = mSource.get(index);
            mImp.onResolutionChange(item.index);
            mChangeResolution = true;
        }
        mLastBtn = mCurBtn;
    }

    private ResolutionChangeImpl mImp;

    public void setResolutionChangeListener(ResolutionChangeImpl imp) {
        mImp = imp;
    }

    public interface ResolutionChangeImpl {
        void onResolutionChange(int index);
    }
}
