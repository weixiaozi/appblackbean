package bbs.com.xinfeng.bbswork.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.io.IOException;
import java.util.List;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.base.App;
import bbs.com.xinfeng.bbswork.base.BaseRvAdapter;
import bbs.com.xinfeng.bbswork.databinding.ItemChattingBinding;
import bbs.com.xinfeng.bbswork.databinding.ItemThemeBinding;
import bbs.com.xinfeng.bbswork.domin.TopicDetailBean;
import bbs.com.xinfeng.bbswork.module.GlideApp;
import bbs.com.xinfeng.bbswork.ui.activity.WebviewActivity;
import bbs.com.xinfeng.bbswork.utils.ArmsUtils;
import bbs.com.xinfeng.bbswork.utils.FormatUtils;
import bbs.com.xinfeng.bbswork.utils.SpanUtil;
import bbs.com.xinfeng.bbswork.utils.ToastUtil;
import bbs.com.xinfeng.bbswork.utils.shortvideo.videoplay.NewVodPlayerActivity;
import bbs.com.xinfeng.bbswork.widget.imageShow.ShowImagesDialog;

/**
 * Created by dell on 2017/10/27.
 */

public class ThemeListAdapter extends BaseRvAdapter<ItemThemeBinding> {
    private List<TopicDetailBean.ThreadBean> datas;


    private final MediaPlayer mp = new MediaPlayer();
    private AnimationDrawable mVoiceAnimation;
    private boolean mSetData = false;
    private int mPosition = -1;// 和mSetData一起组成判断播放哪条录音的依据
    private float mDensity;
    private ShowImagesDialog imagesDialog;

    public ThemeListAdapter(Context mContext, List datas) {
        super(mContext);
        this.datas = datas;

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

    @Override
    protected MyViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_theme, parent, false);
        ItemThemeBinding mBinding = DataBindingUtil.bind(view);
        ContentHolder holder = new ContentHolder(view, mBinding);

        holder.mBinding.llayoutRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListen != null)
                    mListen.operateItem(holder.getLayoutPosition());
            }
        });
        return holder;
    }

    @Override
    protected void onBindHolder(MyViewHolder holder, int position) {
        TopicDetailBean.ThreadBean chattingBean = datas.get(position);
        GlideApp.with(mContext).load(chattingBean.getAuthor_user_portrait_thumb()).placeholder(R.drawable.icon_head_default).error(R.drawable.icon_head_default).override(48).into(holder.mBinding.ivHead);

        holder.mBinding.txtName.setText(chattingBean.getAuthor_user_name());
        holder.mBinding.txtTime.setText(chattingBean.getCreated_at2());
        holder.mBinding.txtThemeContent.setVisibility(TextUtils.isEmpty(chattingBean.getContent()) ? View.GONE : View.VISIBLE);
        holder.mBinding.txtThemeContent.setText(SpanUtil.getExpressionString(mContext, chattingBean.getContent(), false, holder.mBinding.txtThemeContent));
        holder.mBinding.txtThemeInfo.setText(chattingBean.getComments() + " 回复  " + chattingBean.getLike() + " 赞");

        ItemThemeBinding otherTxtBinding = holder.mBinding;

        if (chattingBean.getVideoType() == 1) {
            otherTxtBinding.flayoutThemePics.setVisibility(View.VISIBLE);
            otherTxtBinding.llayoutAudioRoot.setVisibility(View.GONE);
            otherTxtBinding.fllayoutThemeVideo.setVisibility(View.GONE);
            int size = chattingBean.getThumb().size();
            if (size == 1) {
                otherTxtBinding.recycleThemePics.setVisibility(View.GONE);
                otherTxtBinding.flayoutOneParent.setVisibility(View.VISIBLE);
                try {
                    Uri uri = Uri.parse(chattingBean.getSrc().get(0));
                    String s = uri.getQueryParameter("s");
                    if (!TextUtils.isEmpty(s)) {
                        otherTxtBinding.txtLongpic.setVisibility(Integer.parseInt(s) == 1 ? View.VISIBLE : View.GONE);
                    } else {
                        otherTxtBinding.txtLongpic.setVisibility(View.GONE);
                    }
                    int width = Integer.parseInt(uri.getQueryParameter("sw"));
                    int height = Integer.parseInt(uri.getQueryParameter("sh"));
                    otherTxtBinding.ivThemeOnepic.getLayoutParams().width = width;
                    otherTxtBinding.ivThemeOnepic.getLayoutParams().height = height;
                } catch (Exception e) {
                    otherTxtBinding.txtLongpic.setVisibility(View.GONE);
                    otherTxtBinding.ivThemeOnepic.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
                }
                GlideApp.with(mContext).load(chattingBean.getThumb().get(0)).centerCrop().placeholder(R.drawable.icon_theme_default_big).error(R.drawable.icon_theme_default_big).into(otherTxtBinding.ivThemeOnepic);

                otherTxtBinding.ivThemeOnepic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListen != null) {
                            mListen.clickPic(holder.getLayoutPosition());
                        }

                        if (imagesDialog == null) {
                            imagesDialog = new ShowImagesDialog(mContext);
                            imagesDialog.setHasSave(true);
                        }
                        imagesDialog.setPicList(chattingBean.getSrc(), chattingBean.getThumb(), 0);
                        imagesDialog.show();
                    }
                });

            } else {
                otherTxtBinding.recycleThemePics.setVisibility(View.VISIBLE);
                otherTxtBinding.flayoutOneParent.setVisibility(View.GONE);
                ContentHolder mContentHolder = (ContentHolder) holder;
                if (mContentHolder.picsAdapter == null) {
                    mContentHolder.picsAdapter = new ChatPicsAdapter(mContext, chattingBean.getThumb());
                    otherTxtBinding.recycleThemePics.setLayoutManager(mContentHolder.gridLayoutManager);
                    otherTxtBinding.recycleThemePics.setNestedScrollingEnabled(false);
                    otherTxtBinding.recycleThemePics.setHasFixedSize(true);
                    otherTxtBinding.recycleThemePics.setAdapter(mContentHolder.picsAdapter);
                } else {
                    mContentHolder.picsAdapter.setMdatas(chattingBean.getThumb());
                    mContentHolder.picsAdapter.notifyDataSetChanged();
                }

                mContentHolder.picsAdapter.setmListen(new ChatPicsAdapter.onItemOperateListen() {
                    @Override
                    public void operateItem(int position, View v) {
                        if (mListen != null) {
                            mListen.clickPic(holder.getLayoutPosition());
                        }

                        if (imagesDialog == null) {
                            imagesDialog = new ShowImagesDialog(mContext);
                            imagesDialog.setHasSave(true);
                        }
                        imagesDialog.setPicList(chattingBean.getSrc(), chattingBean.getThumb(), position);
                        imagesDialog.show();

                    }
                });
            }
        } else if (chattingBean.getVideoType() == 2) {
            otherTxtBinding.flayoutThemePics.setVisibility(View.VISIBLE);
            otherTxtBinding.flayoutOneParent.setVisibility(View.GONE);
            otherTxtBinding.recycleThemePics.setVisibility(View.GONE);
            otherTxtBinding.fllayoutThemeVideo.setVisibility(View.GONE);
            otherTxtBinding.llayoutAudioRoot.setVisibility(View.VISIBLE);
            otherTxtBinding.voiceReadnew.setVisibility(chattingBean.getAudioread() == 1 ? View.GONE : View.VISIBLE);
            otherTxtBinding.voiceLengthTv.setText(chattingBean.getDuration() + mContext.getString(R.string.jmui_symbol_second));

            //控制语音长度显示，长度增幅随语音长度逐渐缩小
            int width = (int) (-0.04 * chattingBean.getDuration() * chattingBean.getDuration() + 4.526 * chattingBean.getDuration() + 75.214);
            if (chattingBean.isAnimator() > 0) {
                if (mVoiceAnimation != null) {
                    otherTxtBinding.voiceIv.setImageResource(chattingBean.isAnimator() == 1 ? R.drawable.jmui_voice_loading_send : R.drawable.jmui_voice_send);
                    mVoiceAnimation = (AnimationDrawable) otherTxtBinding.voiceIv.getDrawable();
                    mVoiceAnimation.start();
                } else {
                    chattingBean.setAnimator(0);
                    otherTxtBinding.voiceIv.setImageResource(R.drawable.jmui_send_3);
                }
            } else {
                otherTxtBinding.voiceIv.setImageResource(R.drawable.jmui_send_3);
            }
            otherTxtBinding.msgContent.setWidth((int) (width * mDensity));


            otherTxtBinding.voiceFl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        Toast.makeText(mContext, mContext.getString(R.string.jmui_sdcard_not_exist_toast),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        if (mListen != null) {
                            mListen.clickAudio(holder.getLayoutPosition());
                            mListen.clickPic(holder.getLayoutPosition());
                            otherTxtBinding.voiceReadnew.setVisibility(View.GONE);
                            chattingBean.setAudioread(1);
                        }

                        // 如果之前存在播放动画，无论这次点击触发的是暂停还是播放，停止上次播放的动画
                        if (mVoiceAnimation != null) {
                            mVoiceAnimation.stop();
                            mVoiceAnimation = null;
                        }
                        // 播放中点击了正在播放的Item 则暂停播放
                        if (mp.isPlaying() && mPosition == position) {
                            otherTxtBinding.voiceIv.setImageResource(R.drawable.jmui_voice_send);
                            mVoiceAnimation = (AnimationDrawable) otherTxtBinding.voiceIv.getDrawable();
                            pauseVoice();
                            if (mVoiceAnimation != null) {
                                mVoiceAnimation.stop();
                                mVoiceAnimation = null;
                            }
                            // 开始播放录音
                        } else {
                            try {

                                // 继续播放之前暂停的录音
                                if (mSetData && mPosition == position) {
                                    otherTxtBinding.voiceIv.setImageResource(R.drawable.jmui_voice_send);
                                    mVoiceAnimation = (AnimationDrawable) otherTxtBinding.voiceIv.getDrawable();
                                    if (mVoiceAnimation != null) {
                                        chattingBean.setAnimator(2);
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
                                    mPosition = position;
//                                mFIS = new FileInputStream(path);
//                                mFD = mFIS.getFD();
//                                mp.setDataSource(mFD);
                                    mp.setDataSource(mContext, Uri.parse(chattingBean.getVideoPath()));
                                    if (App.getInstance().mIsEarPhoneOn) {
                                        mp.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
                                    } else {
                                        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                    }

                                    otherTxtBinding.voiceIv.setImageResource(R.drawable.jmui_voice_loading_send);
                                    mVoiceAnimation = (AnimationDrawable) otherTxtBinding.voiceIv.getDrawable();
                                    if (mVoiceAnimation != null) {
                                        chattingBean.setAnimator(1);
                                        mVoiceAnimation.start();
                                    }

                                    if (lastPostion >= 0 && lastPostion != mPosition) {
                                        datas.get(lastPostion).setAnimator(0);
                                        notifyItemChanged(lastPostion);
                                    }
                                    mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                        @Override
                                        public void onPrepared(MediaPlayer mp) {
                                            otherTxtBinding.voiceIv.setImageResource(R.drawable.jmui_voice_send);
                                            mVoiceAnimation = (AnimationDrawable) otherTxtBinding.voiceIv.getDrawable();
                                            if (mVoiceAnimation != null) {
                                                chattingBean.setAnimator(2);
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
                                otherTxtBinding.voiceIv.setImageResource(R.drawable.jmui_send_3);
                                ToastUtil.showToast(mContext.getString(R.string.jmui_file_not_found_toast));
                            } catch (IllegalArgumentException e) {
                                if (mVoiceAnimation != null) {
                                    mVoiceAnimation.stop();
                                    mVoiceAnimation = null;
                                }
                                otherTxtBinding.voiceIv.setImageResource(R.drawable.jmui_send_3);
                                e.printStackTrace();
                            } catch (SecurityException e) {
                                if (mVoiceAnimation != null) {
                                    mVoiceAnimation.stop();
                                    mVoiceAnimation = null;
                                }
                                otherTxtBinding.voiceIv.setImageResource(R.drawable.jmui_send_3);
                                e.printStackTrace();
                            } catch (IllegalStateException e) {
                                if (mVoiceAnimation != null) {
                                    mVoiceAnimation.stop();
                                    mVoiceAnimation = null;
                                }
                                otherTxtBinding.voiceIv.setImageResource(R.drawable.jmui_send_3);
                                e.printStackTrace();
                            } catch (IOException e) {
                                if (mVoiceAnimation != null) {
                                    mVoiceAnimation.stop();
                                    mVoiceAnimation = null;
                                }
                                otherTxtBinding.voiceIv.setImageResource(R.drawable.jmui_send_3);
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
                            chattingBean.setAnimator(0);
                            mp.reset();
                            mSetData = false;
                            // 播放完毕，恢复初始状态
                            otherTxtBinding.voiceIv.setImageResource(R.drawable.jmui_send_3);
                        }
                    });
                }

                private void pauseVoice() {
                    mp.pause();
                    mSetData = true;
                }
            });

        } else if (chattingBean.getVideoType() == 3 || chattingBean.getVideoType() == 4) {
            otherTxtBinding.flayoutThemePics.setVisibility(View.VISIBLE);
            otherTxtBinding.flayoutOneParent.setVisibility(View.GONE);
            otherTxtBinding.recycleThemePics.setVisibility(View.GONE);
            otherTxtBinding.llayoutAudioRoot.setVisibility(View.GONE);
            otherTxtBinding.fllayoutThemeVideo.setVisibility(View.VISIBLE);
            try {
                Uri uri = Uri.parse(chattingBean.getVideoPath());
                Float width = Float.parseFloat(uri.getQueryParameter("w"));
                Float height = Float.parseFloat(uri.getQueryParameter("h"));
                if (height > width) {
                    otherTxtBinding.fllayoutThemeVideo.getLayoutParams().width = ArmsUtils.dip2px(mContext, 136);
                    otherTxtBinding.fllayoutThemeVideo.getLayoutParams().height = ArmsUtils.dip2px(mContext, 181);
                } else {
                    otherTxtBinding.fllayoutThemeVideo.getLayoutParams().width = ArmsUtils.dip2px(mContext, 181);
                    otherTxtBinding.fllayoutThemeVideo.getLayoutParams().height = ArmsUtils.dip2px(mContext, 136);
                }
            } catch (Exception e) {
                otherTxtBinding.fllayoutThemeVideo.getLayoutParams().width = ArmsUtils.dip2px(mContext, 181);
                otherTxtBinding.fllayoutThemeVideo.getLayoutParams().height = ArmsUtils.dip2px(mContext, 136);
            }
            otherTxtBinding.ivThemeVideopathPreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListen != null) {
                        mListen.clickPic(holder.getLayoutPosition());
                    }

                    if (chattingBean.getVideoType() == 3) {
                        Intent intent = new Intent(mContext, NewVodPlayerActivity.class);
                        intent.putExtra("frompath", 4);
                        intent.putExtra("videopath", chattingBean.getVideoPath());
                        intent.putExtra("coverpath", chattingBean.getCoverPath());
                        mContext.startActivity(intent);
                    } else if (chattingBean.getVideoType() == 4) {
                        Intent intent = new Intent(mContext, WebviewActivity.class);
                        intent.putExtra("weburl", chattingBean.getVideoPath());
                        intent.putExtra("head", true);
                        mContext.startActivity(intent);
                    }
                }
            });
            otherTxtBinding.txtVideoDuration.setText(chattingBean.getDuration() != 0 ? chattingBean.getDuration() + "\"" : "");
            if (!chattingBean.getCoverPath().startsWith("http"))
                GlideApp.with(mContext).load(Uri.fromFile(new File(chattingBean.getCoverPath()))).override(540, 408).centerCrop().into(otherTxtBinding.ivThemeVideopath);
            else
                GlideApp.with(mContext).load(chattingBean.getCoverPath()).centerCrop().override(540, 408).into(otherTxtBinding.ivThemeVideopath);
        } else {
            otherTxtBinding.flayoutThemePics.setVisibility(View.GONE);
        }


        /*if (info.getImg().getThumb() != null && info.getImg().getThumb().size() > 0) {
            int size = info.getImg().getThumb().size();
            holder.mBinding.llayoutThemePics.setVisibility(View.VISIBLE);
            if (size == 1) {
                holder.mBinding.ivPic1.setVisibility(View.VISIBLE);
                holder.mBinding.ivPic2.setVisibility(View.GONE);
                holder.mBinding.ivPic3.setVisibility(View.GONE);
                GlideApp.with(mContext).load(info.getImg().getThumb().get(0)).override(172).placeholder(R.drawable.icon_theme_default).error(R.drawable.icon_theme_default).into(holder.mBinding.ivPic1);
            } else if (size == 2) {

                holder.mBinding.ivPic1.setVisibility(View.VISIBLE);
                holder.mBinding.ivPic2.setVisibility(View.VISIBLE);
                holder.mBinding.ivPic3.setVisibility(View.GONE);
                GlideApp.with(mContext).load(info.getImg().getThumb().get(0)).override(172).placeholder(R.drawable.icon_theme_default).error(R.drawable.icon_theme_default).into(holder.mBinding.ivPic1);
                GlideApp.with(mContext).load(info.getImg().getThumb().get(1)).override(172).placeholder(R.drawable.icon_theme_default).error(R.drawable.icon_theme_default).into(holder.mBinding.ivPic2);

            } else {
                holder.mBinding.ivPic1.setVisibility(View.VISIBLE);
                holder.mBinding.ivPic2.setVisibility(View.VISIBLE);
                holder.mBinding.ivPic3.setVisibility(View.VISIBLE);
                GlideApp.with(mContext).load(info.getImg().getThumb().get(0)).override(172).placeholder(R.drawable.icon_theme_default).error(R.drawable.icon_theme_default).into(holder.mBinding.ivPic1);
                GlideApp.with(mContext).load(info.getImg().getThumb().get(1)).override(172).placeholder(R.drawable.icon_theme_default).error(R.drawable.icon_theme_default).into(holder.mBinding.ivPic2);
                GlideApp.with(mContext).load(info.getImg().getThumb().get(2)).override(172).placeholder(R.drawable.icon_theme_default).error(R.drawable.icon_theme_default).into(holder.mBinding.ivPic3);
            }

        } else {
            holder.mBinding.llayoutThemePics.setVisibility(View.GONE);
        }*/
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class ContentHolder extends MyViewHolder {
        ChatPicsAdapter picsAdapter;
        GridLayoutManager gridLayoutManager;

        public ContentHolder(View itemView, ViewDataBinding mBinding) {
            super(itemView, (ItemThemeBinding) mBinding);
            gridLayoutManager = new GridLayoutManager(itemView.getContext(), 3) {
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
                datas.get(mPosition).setAnimator(0);
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

        void clickPic(int position);

        void clickAudio(int position);
    }

    private onItemOperateListen mListen;

    public void setmListen(onItemOperateListen mListen) {
        this.mListen = mListen;
    }
}
