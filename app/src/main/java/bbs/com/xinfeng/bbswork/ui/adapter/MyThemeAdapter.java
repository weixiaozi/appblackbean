package bbs.com.xinfeng.bbswork.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.io.File;
import java.io.IOException;
import java.util.List;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.base.App;
import bbs.com.xinfeng.bbswork.base.Constant;
import bbs.com.xinfeng.bbswork.domin.MyThemeListBean;
import bbs.com.xinfeng.bbswork.module.GlideApp;
import bbs.com.xinfeng.bbswork.ui.activity.WebviewActivity;
import bbs.com.xinfeng.bbswork.utils.ArmsUtils;
import bbs.com.xinfeng.bbswork.utils.FormatUtils;
import bbs.com.xinfeng.bbswork.utils.SpanUtil;
import bbs.com.xinfeng.bbswork.utils.ToastUtil;
import bbs.com.xinfeng.bbswork.utils.shortvideo.videoplay.NewVodPlayerActivity;
import bbs.com.xinfeng.bbswork.widget.imageShow.ShowImagesDialog;

/**
 * Created by dell on 2018/4/10.
 */

public class MyThemeAdapter extends BaseQuickAdapter<MyThemeListBean.DataBean, MyThemeAdapter.ContentHolder> {

    private final MediaPlayer mp = new MediaPlayer();
    private AnimationDrawable mVoiceAnimation;
    private boolean mSetData = false;
    private int mPosition = -1;// 和mSetData一起组成判断播放哪条录音的依据
    private float mDensity;
    private ShowImagesDialog imagesDialog;

    public MyThemeAdapter(@Nullable List<MyThemeListBean.DataBean> data, Context context) {
        super(R.layout.item_mytheme, data);
        mContext = context;
    }

    @Override
    protected void convert(ContentHolder helper, MyThemeListBean.DataBean item) {
        if (mDensity == 0) {
            DisplayMetrics dm = new DisplayMetrics();
            ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
            mDensity = dm.density;

            mp.setAudioStreamType(AudioManager.STREAM_RING);
            mp.setOnErrorListener(new MediaPlayer.OnErrorListener() {

                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    if (mVoiceAnimation != null) {
                        mVoiceAnimation.stop();
                        mVoiceAnimation = null;
                    }
                    if (mPosition >= 0) {
                        notifyItemChanged(mPosition);
                    }
                    ToastUtil.showToast("播放失败");
                    return false;
                }
            });
        }

        helper.getView(R.id.llayout_collect_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListen != null)
                    mListen.operateItem(helper.getLayoutPosition());
            }
        });

        helper.setText(R.id.txt_time, item.getCreated_at2());
        if (TextUtils.isEmpty(item.getContent())) {
            helper.getView(R.id.txt_theme_content).setVisibility(View.GONE);
        } else {
            helper.getView(R.id.txt_theme_content).setVisibility(View.VISIBLE);
            helper.setText(R.id.txt_theme_content, SpanUtil.getExpressionString(mContext, item.getContent(),
                    true, helper.getView(R.id.txt_theme_content)));
            helper.getView(R.id.txt_theme_content).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListen != null)
                        mListen.operateItem(helper.getLayoutPosition());
                }
            });
        }
        helper.setText(R.id.txt_theme_topicname, item.getTopic().getName());
        helper.setText(R.id.txt_theme_info, item.getCommentscn() + " 回复  " + item.getLikecn() + " 赞");

        if (item.getVideoType() == 1) {
            helper.getView(R.id.flayout_theme_pics).setVisibility(View.VISIBLE);
            helper.getView(R.id.llayout_audio_root).setVisibility(View.GONE);
            helper.getView(R.id.fllayout_theme_video).setVisibility(View.GONE);
            int size = item.getThumb().size();
            if (size == 1) {
                helper.getView(R.id.recycle_theme_pics).setVisibility(View.GONE);
                helper.getView(R.id.flayout_one_parent).setVisibility(View.VISIBLE);
                try {
                    Uri uri = Uri.parse(item.getSrc().get(0));
                    String s = uri.getQueryParameter("s");
                    if (!TextUtils.isEmpty(s)) {
                        helper.getView(R.id.txt_longpic).setVisibility(Integer.parseInt(s) == 1 ? View.VISIBLE : View.GONE);
                    } else {
                        helper.getView(R.id.txt_longpic).setVisibility(View.GONE);
                    }
                    int width = Integer.parseInt(uri.getQueryParameter("sw"));
                    int height = Integer.parseInt(uri.getQueryParameter("sh"));
                    helper.getView(R.id.iv_theme_onepic).getLayoutParams().width = width;
                    helper.getView(R.id.iv_theme_onepic).getLayoutParams().height = height;
                } catch (Exception e) {
                    helper.getView(R.id.txt_longpic).setVisibility(View.GONE);
                    helper.getView(R.id.iv_theme_onepic).setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
                }
                GlideApp.with(mContext).load(item.getThumb().get(0)).centerCrop().placeholder(R.drawable.icon_theme_default_big).error(R.drawable.icon_theme_default_big).into((ImageView) helper.getView(R.id.iv_theme_onepic));


                helper.getView(R.id.iv_theme_onepic).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (imagesDialog == null) {
                            imagesDialog = new ShowImagesDialog(mContext);
                            imagesDialog.setHasSave(true);
                        }
                        imagesDialog.setPicList(item.getSrc(), item.getThumb(), 0);
                        imagesDialog.show();
                    }
                });

            } else {
                helper.getView(R.id.recycle_theme_pics).setVisibility(View.VISIBLE);
                helper.getView(R.id.flayout_one_parent).setVisibility(View.GONE);
//                ContentHolder mContentHolder = (ContentHolder) helper;
                if (helper.picsAdapter == null) {
                    helper.picsAdapter = new ChatPicsAdapter(mContext, item.getThumb());
                    ((RecyclerView) helper.getView(R.id.recycle_theme_pics)).setLayoutManager(helper.gridLayoutManager);
                    ((RecyclerView) helper.getView(R.id.recycle_theme_pics)).setNestedScrollingEnabled(false);
                    ((RecyclerView) helper.getView(R.id.recycle_theme_pics)).setHasFixedSize(true);
                    ((RecyclerView) helper.getView(R.id.recycle_theme_pics)).setAdapter(helper.picsAdapter);
                } else {
                    helper.picsAdapter.setMdatas(item.getThumb());
                    helper.picsAdapter.notifyDataSetChanged();
                }

                helper.picsAdapter.setmListen(new ChatPicsAdapter.onItemOperateListen() {
                    @Override
                    public void operateItem(int position, View v) {

                        if (imagesDialog == null) {
                            imagesDialog = new ShowImagesDialog(mContext);
                            imagesDialog.setHasSave(true);
                        }
                        imagesDialog.setPicList(item.getSrc(), item.getThumb(), position);
                        imagesDialog.show();

                    }
                });
            }
        } else if (item.getVideoType() == 2) {
            helper.getView(R.id.flayout_theme_pics).setVisibility(View.VISIBLE);
            helper.getView(R.id.flayout_one_parent).setVisibility(View.GONE);
            helper.getView(R.id.recycle_theme_pics).setVisibility(View.GONE);
            helper.getView(R.id.fllayout_theme_video).setVisibility(View.GONE);
            helper.getView(R.id.llayout_audio_root).setVisibility(View.VISIBLE);
            helper.setText(R.id.voice_length_tv, item.getDuration() + mContext.getString(R.string.jmui_symbol_second));
//            helper.getView(R.id.voice_readnew).setVisibility(item.getAudioread() == 1 ? View.GONE : View.VISIBLE);

            //控制语音长度显示，长度增幅随语音长度逐渐缩小
            int width = (int) (-0.04 * item.getDuration() * item.getDuration() + 4.526 * item.getDuration() + 75.214);
            if (item.isAnimator() > 0) {
                if (mVoiceAnimation != null) {
                    ((ImageView) helper.getView(R.id.voice_iv)).setImageResource(item.isAnimator() == 1 ? R.drawable.jmui_voice_loading_send : R.drawable.jmui_voice_send);
                    mVoiceAnimation = (AnimationDrawable) ((ImageView) helper.getView(R.id.voice_iv)).getDrawable();
                    mVoiceAnimation.start();
                } else {
                    item.setAnimator(0);
                    ((ImageView) helper.getView(R.id.voice_iv)).setImageResource(R.drawable.jmui_send_3);
                }
            } else {
                ((ImageView) helper.getView(R.id.voice_iv)).setImageResource(R.drawable.jmui_send_3);
            }
            ((TextView) helper.getView(R.id.msg_content)).setWidth((int) (width * mDensity));

            helper.getView(R.id.voice_fl).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        Toast.makeText(mContext, mContext.getString(R.string.jmui_sdcard_not_exist_toast),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        /*if (mListen != null) {
                            mListen.clickAudio(helper.getLayoutPosition());
                            helper.getView(R.id.voice_readnew).setVisibility(View.GONE);
                            item.setAudioread(1);
                        }*/

                        // 如果之前存在播放动画，无论这次点击触发的是暂停还是播放，停止上次播放的动画
                        if (mVoiceAnimation != null) {
                            mVoiceAnimation.stop();
                            mVoiceAnimation = null;
                        }
                        // 播放中点击了正在播放的Item 则暂停播放
                        if (mp.isPlaying() && mPosition == helper.getLayoutPosition()) {
                            ((ImageView) helper.getView(R.id.voice_iv)).setImageResource(R.drawable.jmui_voice_send);
                            mVoiceAnimation = (AnimationDrawable) ((ImageView) helper.getView(R.id.voice_iv)).getDrawable();
                            pauseVoice();
                            if (mVoiceAnimation != null) {
                                mVoiceAnimation.stop();
                                mVoiceAnimation = null;
                            }
                            // 开始播放录音
                        } else {
                            try {
                                // 继续播放之前暂停的录音
                                if (mSetData && mPosition == helper.getLayoutPosition()) {
                                    ((ImageView) helper.getView(R.id.voice_iv)).setImageResource(R.drawable.jmui_voice_send);
                                    mVoiceAnimation = (AnimationDrawable) ((ImageView) helper.getView(R.id.voice_iv)).getDrawable();
                                    if (mVoiceAnimation != null) {
                                        item.setAnimator(2);
                                        mVoiceAnimation.start();
                                    }
                                    playVoice();
                                    // 否则重新播放该录音或者其他录音
                                } else {
                                    int lastPostion = -1;
                                    if (mPosition >= 0)
                                        lastPostion = mPosition;
                                    mp.reset();
                                    // 记录播放录音的位置
                                    mPosition = helper.getLayoutPosition();
                                    mp.setDataSource(mContext, Uri.parse(item.getVideoPath()));
                                    if (App.getInstance().mIsEarPhoneOn) {
                                        mp.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
                                    } else {
                                        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                    }
                                    ((ImageView) helper.getView(R.id.voice_iv)).setImageResource(R.drawable.jmui_voice_loading_send);
                                    mVoiceAnimation = (AnimationDrawable) ((ImageView) helper.getView(R.id.voice_iv)).getDrawable();
                                    if (mVoiceAnimation != null) {
                                        item.setAnimator(1);
                                        mVoiceAnimation.start();
                                    }
                                    if (lastPostion >= 0 && lastPostion != mPosition) {
                                        mData.get(lastPostion).setAnimator(0);
                                        notifyItemChanged(lastPostion);
                                    }
                                    mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                        @Override
                                        public void onPrepared(MediaPlayer mp) {
                                            ((ImageView) helper.getView(R.id.voice_iv)).setImageResource(R.drawable.jmui_voice_send);
                                            mVoiceAnimation = (AnimationDrawable) ((ImageView) helper.getView(R.id.voice_iv)).getDrawable();
                                            if (mVoiceAnimation != null) {
                                                item.setAnimator(2);
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
                                ((ImageView) helper.getView(R.id.voice_iv)).setImageResource(R.drawable.jmui_send_3);
                                ToastUtil.showToast(mContext.getString(R.string.jmui_file_not_found_toast));
                            } catch (IllegalArgumentException e) {
                                if (mVoiceAnimation != null) {
                                    mVoiceAnimation.stop();
                                    mVoiceAnimation = null;
                                }
                                ((ImageView) helper.getView(R.id.voice_iv)).setImageResource(R.drawable.jmui_send_3);
                                e.printStackTrace();
                            } catch (SecurityException e) {
                                if (mVoiceAnimation != null) {
                                    mVoiceAnimation.stop();
                                    mVoiceAnimation = null;
                                }
                                ((ImageView) helper.getView(R.id.voice_iv)).setImageResource(R.drawable.jmui_send_3);
                                e.printStackTrace();
                            } catch (IllegalStateException e) {
                                if (mVoiceAnimation != null) {
                                    mVoiceAnimation.stop();
                                    mVoiceAnimation = null;
                                }
                                ((ImageView) helper.getView(R.id.voice_iv)).setImageResource(R.drawable.jmui_send_3);
                                e.printStackTrace();
                            } catch (IOException e) {
                                if (mVoiceAnimation != null) {
                                    mVoiceAnimation.stop();
                                    mVoiceAnimation = null;
                                }
                                ((ImageView) helper.getView(R.id.voice_iv)).setImageResource(R.drawable.jmui_send_3);
                                ToastUtil.showToast(mContext.getString(R.string.jmui_file_not_found_toast));
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
                            item.setAnimator(0);
                            mp.reset();
                            mSetData = false;
                            // 播放完毕，恢复初始状态
                            ((ImageView) helper.getView(R.id.voice_iv)).setImageResource(R.drawable.jmui_send_3);
                        }
                    });
                }

                private void pauseVoice() {
                    mp.pause();
                    mSetData = true;
                }
            });

        } else if (item.getVideoType() == 3 || item.getVideoType() == 4) {
            helper.getView(R.id.flayout_theme_pics).setVisibility(View.VISIBLE);
            helper.getView(R.id.flayout_one_parent).setVisibility(View.GONE);
            helper.getView(R.id.recycle_theme_pics).setVisibility(View.GONE);
            helper.getView(R.id.llayout_audio_root).setVisibility(View.GONE);
            helper.getView(R.id.fllayout_theme_video).setVisibility(View.VISIBLE);
            try {
                Uri uri = Uri.parse(item.getVideoPath());
                Float width = Float.parseFloat(uri.getQueryParameter("w"));
                Float height = Float.parseFloat(uri.getQueryParameter("h"));
                if (height > width) {
                    helper.getView(R.id.fllayout_theme_video).getLayoutParams().width = ArmsUtils.dip2px(mContext, 136);
                    helper.getView(R.id.fllayout_theme_video).getLayoutParams().height = ArmsUtils.dip2px(mContext, 181);
                } else {
                    helper.getView(R.id.fllayout_theme_video).getLayoutParams().width = ArmsUtils.dip2px(mContext, 181);
                    helper.getView(R.id.fllayout_theme_video).getLayoutParams().height = ArmsUtils.dip2px(mContext, 136);
                }
            } catch (Exception e) {
                helper.getView(R.id.fllayout_theme_video).getLayoutParams().width = ArmsUtils.dip2px(mContext, 181);
                helper.getView(R.id.fllayout_theme_video).getLayoutParams().height = ArmsUtils.dip2px(mContext, 136);
            }
            helper.setText(R.id.txt_video_duration, item.getDuration() != 0 ? item.getDuration() + "\"" : "");
            if (!item.getCoverPath().startsWith("http"))
                GlideApp.with(mContext).load(Uri.fromFile(new File(item.getCoverPath()))).override(540, 408).centerCrop().into((ImageView) helper.getView(R.id.iv_theme_videopath));
            else
                GlideApp.with(mContext).load(item.getCoverPath()).centerCrop().override(540, 408).into((ImageView) helper.getView(R.id.iv_theme_videopath));
            helper.getView(R.id.iv_theme_videopath_preview).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.getVideoType() == 3) {
                        Intent intent = new Intent(mContext, NewVodPlayerActivity.class);
                        intent.putExtra("frompath", 4);
                        intent.putExtra("videopath", item.getVideoPath());
                        intent.putExtra("coverpath", item.getCoverPath());
                        mContext.startActivity(intent);
                    } else if (item.getVideoType() == 4) {
                        Intent intent = new Intent(mContext, WebviewActivity.class);
                        intent.putExtra("weburl", item.getVideoPath());
                        intent.putExtra("head", true);
                        mContext.startActivity(intent);
                    }

                }
            });
        } else {
            helper.getView(R.id.flayout_theme_pics).setVisibility(View.GONE);
        }


        /*if (item.getThumb() != null && item.getThumb().size() > 0) {
            int size = item.getThumb().size();
            helper.getView(R.id.llayout_theme_pics).setVisibility(View.VISIBLE);
            if (size == 1) {
                helper.getView(R.id.iv_pic1).setVisibility(View.VISIBLE);
                helper.getView(R.id.iv_pic2).setVisibility(View.GONE);
                helper.getView(R.id.iv_pic3).setVisibility(View.GONE);
                GlideApp.with(mContext).load(item.getThumb().get(0)).override(172).placeholder(R.drawable.icon_theme_default).error(R.drawable.icon_theme_default).into((ImageView) helper.getView(R.id.iv_pic1));
            } else if (size == 2) {
                helper.getView(R.id.iv_pic1).setVisibility(View.VISIBLE);
                helper.getView(R.id.iv_pic2).setVisibility(View.VISIBLE);
                helper.getView(R.id.iv_pic3).setVisibility(View.GONE);
                GlideApp.with(mContext).load(item.getThumb().get(0)).override(172).placeholder(R.drawable.icon_theme_default).error(R.drawable.icon_theme_default).into((ImageView) helper.getView(R.id.iv_pic1));
                GlideApp.with(mContext).load(item.getThumb().get(1)).override(172).placeholder(R.drawable.icon_theme_default).error(R.drawable.icon_theme_default).into((ImageView) helper.getView(R.id.iv_pic2));
            } else {
                helper.getView(R.id.iv_pic1).setVisibility(View.VISIBLE);
                helper.getView(R.id.iv_pic2).setVisibility(View.VISIBLE);
                helper.getView(R.id.iv_pic3).setVisibility(View.VISIBLE);
                GlideApp.with(mContext).load(item.getThumb().get(0)).override(172).placeholder(R.drawable.icon_theme_default).error(R.drawable.icon_theme_default).into((ImageView) helper.getView(R.id.iv_pic1));
                GlideApp.with(mContext).load(item.getThumb().get(1)).override(172).placeholder(R.drawable.icon_theme_default).error(R.drawable.icon_theme_default).into((ImageView) helper.getView(R.id.iv_pic2));
                GlideApp.with(mContext).load(item.getThumb().get(2)).override(172).placeholder(R.drawable.icon_theme_default).error(R.drawable.icon_theme_default).into((ImageView) helper.getView(R.id.iv_pic3));
            }

        } else {
            helper.getView(R.id.llayout_theme_pics).setVisibility(View.GONE);
        }*/
    }

    public class ContentHolder extends BaseViewHolder {
        ChatPicsAdapter picsAdapter;
        GridLayoutManager gridLayoutManager;

        public ContentHolder(View view) {
            super(view);
            gridLayoutManager = new GridLayoutManager(view.getContext(), 3) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
        }
    }

    public void stopMediaPlayer() {
        if (mp.isPlaying())
            mp.stop();
        if (mVoiceAnimation != null) {
            mVoiceAnimation.stop();
            mVoiceAnimation = null;
            if (mPosition >= 0) {
                mData.get(mPosition).setAnimator(0);
                notifyItemChanged(mPosition);
            }
        }
        mPosition = -1;
    }

    public void initMediaPlayer() {
        mp.reset();
    }

    public void releaseMediaPlayer() {
        if (mp != null)
            mp.release();
    }

    public interface onItemOperateListen {
        void operateItem(int position);

        void clickAudio(int position);
    }

    private onItemOperateListen mListen;

    public void setmListen(onItemOperateListen mListen) {
        this.mListen = mListen;
    }
}
