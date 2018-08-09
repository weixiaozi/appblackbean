package bbs.com.xinfeng.bbswork.utils.shortvideo.choose;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.ArrayList;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.module.GlideApp;
import bbs.com.xinfeng.bbswork.utils.ArmsUtils;
import bbs.com.xinfeng.bbswork.utils.ScreenUtils;
import bbs.com.xinfeng.bbswork.utils.ToastUtil;
import bbs.com.xinfeng.bbswork.utils.shortvideo.videoplay.NewVodPlayerActivity;
import bbs.com.xinfeng.bbswork.utils.shortvideo.videorecord.TCVideoCutterActivity;
import bbs.com.xinfeng.bbswork.utils.shortvideo.videorecord.TCVideoRecordActivity;

public class TCVideoEditerListAdapter extends RecyclerView.Adapter<TCVideoEditerListAdapter.ViewHolder> {

    private final int picHeight;
    private Context mContext;
    private ArrayList<TCVideoFileInfo> data = new ArrayList<TCVideoFileInfo>();
    private int mLastSelected = -1;
    private boolean mMultiplePick;

    public TCVideoEditerListAdapter(Context context) {
        mContext = context;
        picHeight = (ScreenUtils.getScreenWidth(mContext) - ArmsUtils.dip2px(mContext, 20)) / 4;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_ugc_video, null);
        view.findViewById(R.id.frmQueue).getLayoutParams().width = picHeight;
        view.findViewById(R.id.frmQueue).getLayoutParams().height = picHeight;
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        TCVideoFileInfo fileInfo = data.get(position);
        if (position == 0) {
            holder.frmQueue.setBackgroundColor(mContext.getResources().getColor(R.color.black));
            holder.thumb.setImageResource(R.drawable.icon_video_record);
            holder.duration.setVisibility(View.GONE);
            holder.frmQueue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new RxPermissions((Activity) mContext).request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO).subscribe(aBoolean -> {
                        if (aBoolean) {
                            Intent intent = new Intent(mContext, TCVideoRecordActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(intent);
                        } else {
                            ToastUtil.showToast("请开启存储，相机，录音权限");
                        }
                    });

                }
            });
        } else {
            holder.frmQueue.setBackgroundColor(mContext.getResources().getColor(R.color.low_white));
            holder.duration.setVisibility(View.VISIBLE);
            holder.duration.setText(TCUtils.formattedTime(fileInfo.getDuration() / 1000));
            GlideApp.with(mContext).load(Uri.fromFile(new File(fileInfo.getFilePath()))).override(picHeight).into(holder.thumb);
            holder.frmQueue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new RxPermissions((Activity) mContext).request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(aBoolean -> {
                        if (aBoolean) {
                            Intent intent = new Intent(mContext, NewVodPlayerActivity.class);
                            intent.putExtra("frompath", 2);
                            intent.putExtra(TCConstants.VIDEO_PLAY_PATH, fileInfo);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(intent);
                        } else {
                            ToastUtil.showToast("请开启存储权限");
                        }
                    });

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setMultiplePick(boolean multiplePick) {
        mMultiplePick = multiplePick;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView thumb;
        private final TextView duration;
        private final FrameLayout frmQueue;

        public ViewHolder(final View itemView) {
            super(itemView);
            thumb = (ImageView) itemView.findViewById(R.id.iv_thumb);
            duration = (TextView) itemView.findViewById(R.id.tv_duration);
            frmQueue = (FrameLayout) itemView.findViewById(R.id.frmQueue);
        }
    }

    public ArrayList<TCVideoFileInfo> getMultiSelected() {
        ArrayList<TCVideoFileInfo> infos = new ArrayList<TCVideoFileInfo>();

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).isSelected()) {
                infos.add(data.get(i));
            }
        }
        return infos;
    }

    public TCVideoFileInfo getSingleSelected() {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).isSelected()) {
                return data.get(i);
            }
        }
        return null;
    }

    public void addAll(ArrayList<TCVideoFileInfo> files) {
        try {
            this.data.clear();
            this.data.addAll(files);
        } catch (Exception e) {
            e.printStackTrace();
        }
        notifyDataSetChanged();
    }

    public void changeSingleSelection(int position) {
        if (mLastSelected != -1) {
            data.get(mLastSelected).setSelected(false);
        }
        notifyItemChanged(mLastSelected);

        TCVideoFileInfo info = data.get(position);
        info.setSelected(true);
        notifyItemChanged(position);

        mLastSelected = position;
    }

    public void changeMultiSelection(int position) {
        if (data.get(position).isSelected()) {
            data.get(position).setSelected(false);
        } else {
            data.get(position).setSelected(true);
        }
        notifyItemChanged(position);
    }

}
