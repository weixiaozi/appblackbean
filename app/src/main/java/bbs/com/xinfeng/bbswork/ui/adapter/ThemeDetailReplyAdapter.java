package bbs.com.xinfeng.bbswork.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
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
import bbs.com.xinfeng.bbswork.domin.ThemeDetailReplyBean;
import bbs.com.xinfeng.bbswork.module.GlideApp;
import bbs.com.xinfeng.bbswork.ui.activity.WebviewActivity;
import bbs.com.xinfeng.bbswork.utils.ArmsUtils;
import bbs.com.xinfeng.bbswork.utils.FormatUtils;
import bbs.com.xinfeng.bbswork.utils.SpanUtil;
import bbs.com.xinfeng.bbswork.utils.ToastUtil;
import bbs.com.xinfeng.bbswork.widget.CircleImageView;
import bbs.com.xinfeng.bbswork.widget.imageShow.ShowImagesDialog;

/**
 * Created by dell on 2018/4/10.
 */

public class ThemeDetailReplyAdapter extends BaseQuickAdapter<ThemeDetailReplyBean.DataBean, ThemeDetailReplyAdapter.ContentHolder> {
    private float mDensity;
    private ShowImagesDialog imagesDialog;
    private AnimationDrawable mVoiceAnimation;
    private boolean mSetData = false;
    private int mPosition = -1;// 和mSetData一起组成判断播放哪条录音的依据
    private MediaPlayer mp;
    private int max;


    public ThemeDetailReplyAdapter(@Nullable List<ThemeDetailReplyBean.DataBean> data, MediaPlayer mediaPlayer) {
        super(R.layout.item_detail_reply, data);
        this.mp = mediaPlayer;
    }

    public void setImagesDialog(ShowImagesDialog imagesDialog) {
        this.imagesDialog = imagesDialog;
    }

    @Override
    public ContentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent, viewType);
    }

    public boolean isplaying() {
        return (mp.isPlaying() && mVoiceAnimation != null);
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

    @Override
    protected void convert(ContentHolder helper, ThemeDetailReplyBean.DataBean item) {
        if (mDensity == 0) {
            max = ArmsUtils.dip2px(mContext, 180);
            DisplayMetrics dm = new DisplayMetrics();
            ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
            mDensity = dm.density;
        }
        helper.setText(R.id.tv_islandlord, item.getLabel());
        helper.getView(R.id.tv_islandlord).setVisibility(!TextUtils.isEmpty(item.getLabel()) ? View.VISIBLE : View.GONE);
        if (item.isSelf()) {
            helper.getView(R.id.voice_readnew).setVisibility(View.GONE);
            if (item.getSelfStatus() == 1) {
                helper.getView(R.id.fllayout_more).setVisibility(View.GONE);
                helper.getView(R.id.iv_chat_senderror).setVisibility(View.GONE);
                helper.getView(R.id.iv_chat_sendloading).setVisibility(View.VISIBLE);
                ((AnimationDrawable) ((ImageView) helper.getView(R.id.iv_chat_sendloading)).getDrawable()).start();
            } else if (item.getSelfStatus() == 2) {
                helper.getView(R.id.fllayout_more).setVisibility(View.GONE);
                helper.getView(R.id.iv_chat_senderror).setVisibility(View.VISIBLE);
                ((AnimationDrawable) ((ImageView) helper.getView(R.id.iv_chat_sendloading)).getDrawable()).stop();
                helper.getView(R.id.iv_chat_sendloading).setVisibility(View.GONE);
                helper.getView(R.id.iv_chat_senderror).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListen != null)
                            mListen.sendEerror(helper.getLayoutPosition());
                    }
                });
            } else {
                helper.getView(R.id.fllayout_more).setVisibility(View.VISIBLE);
                helper.getView(R.id.iv_chat_senderror).setVisibility(View.GONE);
                ((AnimationDrawable) ((ImageView) helper.getView(R.id.iv_chat_sendloading)).getDrawable()).stop();
                helper.getView(R.id.iv_chat_sendloading).setVisibility(View.GONE);
            }
        } else {
            helper.getView(R.id.voice_readnew).setVisibility(item.getAudioread() == 1 ? View.GONE : View.VISIBLE);
            helper.getView(R.id.fllayout_more).setVisibility(View.VISIBLE);
            helper.getView(R.id.iv_chat_senderror).setVisibility(View.GONE);
            ((AnimationDrawable) ((ImageView) helper.getView(R.id.iv_chat_sendloading)).getDrawable()).stop();
            helper.getView(R.id.iv_chat_sendloading).setVisibility(View.GONE);
        }


        GlideApp.with(mContext).load(item.getUser().getPortrait_thumb()).override(ArmsUtils.dip2px(mContext, 24)).placeholder(R.drawable.icon_head_default).error(R.drawable.icon_head_default).into((CircleImageView) helper.getView(R.id.iv_head));
        helper.setText(R.id.txt_name, item.getUser().getName());
        helper.setText(R.id.txt_time, item.getCreated_at2());
        helper.getView(R.id.txt_reply_content).setVisibility(TextUtils.isEmpty(item.getContent()) ? View.GONE : View.VISIBLE);
        helper.setText(R.id.txt_reply_content, SpanUtil.getExpressionString(mContext, item.getContent(),
                true, helper.getView(R.id.txt_reply_content)));
        helper.getView(R.id.txt_reply_content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!item.isSelf() && mListen != null)
                    mListen.itemclick(helper.getLayoutPosition());
            }
        });
        helper.getView(R.id.txt_reply_content).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mListen != null)
                    mListen.longContentItemclick(helper.getView(R.id.txt_reply_content), ((TextView) helper.getView(R.id.txt_reply_content)).getText().toString(), helper.getLayoutPosition());
                return true;
            }
        });
        helper.getView(R.id.iv_reply_zan).setSelected(item.getIslike() == 1);
        helper.getView(R.id.txt_reply_zan_num).setVisibility(!TextUtils.equals(item.getLikecn(), "0") ? View.VISIBLE : View.GONE);
        helper.setText(R.id.txt_reply_zan_num, item.getLikecn() + "");
        if (item.getLast_reply_post() != null && item.getLast_reply_post().size() > 0) {
            helper.getView(R.id.llayout_theme_comments).setVisibility(View.VISIBLE);
            helper.getView(R.id.txt_theme_comment_first).setVisibility(View.VISIBLE);
            helper.getView(R.id.txt_theme_comment_second).setVisibility(View.GONE);
            try {
                helper.getView(R.id.txt_theme_comment_more).setVisibility(Integer.parseInt(item.getCommentscn()) > 2 ? View.VISIBLE : View.GONE);
            } catch (Exception e) {
                helper.getView(R.id.txt_theme_comment_more).setVisibility(View.VISIBLE);
            }
            helper.setText(R.id.txt_theme_comment_more, "共" + item.getCommentscn() + "条回复>");
            SpannableStringBuilder firstcomment = SpanUtil.getExpressionString(mContext, item.getLast_reply_post().get(0).getAuthor_user_name() + " " + item.getLast_reply_post().get(0).getContent(), false, helper.getView(R.id.txt_theme_comment_first));
            firstcomment.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.black)), 0, item.getLast_reply_post().get(0).getAuthor_user_name().length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            helper.setText(R.id.txt_theme_comment_first, firstcomment);
            if (item.getLast_reply_post().size() > 1) {
                helper.getView(R.id.txt_theme_comment_second).setVisibility(View.VISIBLE);
                SpannableStringBuilder secondcomment = SpanUtil.getExpressionString(mContext, item.getLast_reply_post().get(1).getAuthor_user_name() + " " + item.getLast_reply_post().get(1).getContent(), false, helper.getView(R.id.txt_theme_comment_second));
                secondcomment.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.black)), 0, item.getLast_reply_post().get(1).getAuthor_user_name().length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                helper.setText(R.id.txt_theme_comment_second, secondcomment);
            }
        } else {
            helper.getView(R.id.llayout_theme_comments).setVisibility(View.GONE);
        }


        if (item.getVideoType() == 1) {
            helper.getView(R.id.flayout_theme_pics).setVisibility(View.VISIBLE);
            helper.getView(R.id.llayout_audio_root).setVisibility(View.GONE);
            helper.getView(R.id.fllayout_theme_video).setVisibility(View.GONE);
            int size = item.getThumb().size();
            if (size == 1) {
                helper.getView(R.id.recycle_theme_pics).setVisibility(View.GONE);
                helper.getView(R.id.flayout_one_parent).setVisibility(View.VISIBLE);
                if (item.isSelf()) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(item.getThumb().get(0), options);
                    int width = options.outWidth;
                    int height = options.outHeight;
                    options.inJustDecodeBounds = false;
                    if (width >= max || height >= max) {
                        if (width > height) {
                            helper.getView(R.id.iv_theme_onepic).getLayoutParams().width = max;
                            if (height <= max * 4 / 5) {
                                helper.getView(R.id.iv_theme_onepic).getLayoutParams().height = height;
                            } else {
                                helper.getView(R.id.iv_theme_onepic).getLayoutParams().height = max * 4 / 5;
                            }
                        } else {
                            helper.getView(R.id.iv_theme_onepic).getLayoutParams().height = max;
                            if (width <= max * 4 / 5) {
                                helper.getView(R.id.iv_theme_onepic).getLayoutParams().width = width;
                            } else {
                                helper.getView(R.id.iv_theme_onepic).getLayoutParams().width = max * 4 / 5;
                            }
                        }
                    } else {
                        helper.getView(R.id.iv_theme_onepic).getLayoutParams().width = width;
                        helper.getView(R.id.iv_theme_onepic).getLayoutParams().height = height;
                    }
                    helper.getView(R.id.txt_longpic).setVisibility(View.GONE);
                    GlideApp.with(mContext).load(Uri.fromFile(new File(item.getThumb().get(0)))).centerCrop().placeholder(R.drawable.icon_theme_default_big).error(R.drawable.icon_theme_default_big).into((ImageView) helper.getView(R.id.iv_theme_onepic));

                } else {
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
                }

            } else {
                helper.getView(R.id.recycle_theme_pics).setVisibility(View.VISIBLE);
                helper.getView(R.id.flayout_one_parent).setVisibility(View.GONE);
                ContentHolder mContentHolder = (ContentHolder) helper;
                if (mContentHolder.picsAdapter == null) {
                    mContentHolder.picsAdapter = new ChatPicsAdapter(mContext, item.getThumb());
                    ((RecyclerView) helper.getView(R.id.recycle_theme_pics)).setLayoutManager(mContentHolder.gridLayoutManager);
                    ((RecyclerView) helper.getView(R.id.recycle_theme_pics)).setNestedScrollingEnabled(false);
                    ((RecyclerView) helper.getView(R.id.recycle_theme_pics)).setHasFixedSize(true);
                    ((RecyclerView) helper.getView(R.id.recycle_theme_pics)).setAdapter(mContentHolder.picsAdapter);
                } else {
                    mContentHolder.picsAdapter.setMdatas(item.getThumb());
                    mContentHolder.picsAdapter.notifyDataSetChanged();
                }

                mContentHolder.picsAdapter.setmListen(new ChatPicsAdapter.onItemOperateListen() {
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
                        if (mListen != null) {
                            mListen.clickAudio(helper.getLayoutPosition());
                            helper.getView(R.id.voice_readnew).setVisibility(View.GONE);
                            item.setAudioread(1);
                        }

                        // 如果之前存在播放动画，无论这次点击触发的是暂停还是播放，停止上次播放的动画
                        if (mVoiceAnimation != null) {
                            mVoiceAnimation.stop();
                            mVoiceAnimation = null;
                        }
                        // 播放中点击了正在播放的Item 则暂停播放
                        if (mp.isPlaying() && mPosition == helper.getLayoutPosition() - 1) {
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
                                if (mSetData && mPosition == helper.getLayoutPosition() - 1) {
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
                                    mPosition = helper.getLayoutPosition() - 1;
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
                                        notifyItemChanged(lastPostion + 1);
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

        } else if (item.getVideoType() == 4) {
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
                    Intent intent = new Intent(mContext, WebviewActivity.class);
                    intent.putExtra("weburl", item.getVideoPath());
                    intent.putExtra("head", true);
                    mContext.startActivity(intent);
                }
            });
        } else {
            helper.getView(R.id.flayout_theme_pics).setVisibility(View.GONE);
        }


        helper.getView(R.id.fllayout_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!item.isSelf() && mListen != null)
                    mListen.moreClick(helper.getLayoutPosition());
            }
        });
        helper.getView(R.id.iv_head).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListen != null)
                    mListen.operateItem(helper.getLayoutPosition());
            }
        });
        helper.getView(R.id.llayout_reply_zan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!item.isSelf() && mListen != null)
                    mListen.zanOperate(helper.getLayoutPosition());
            }
        });
        helper.getView(R.id.flayout_reply_two).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!item.isSelf() && mListen != null)
                    mListen.twoReplyOperate(helper.getLayoutPosition());
            }
        });
        /*helper.getView(R.id.llayout_root).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mListen != null)
                    mListen.longItemclick(helper.getLayoutPosition());
                return true;
            }
        });*/
        helper.getView(R.id.llayout_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!item.isSelf() && mListen != null)
                    mListen.itemclick(helper.getLayoutPosition());
            }
        });
    }

    public void stopMediaPlayer() {
        if (mp.isPlaying())
            mp.stop();
        if (mVoiceAnimation != null) {
            mVoiceAnimation.stop();
            mVoiceAnimation = null;
            if (mPosition >= 0) {
                mData.get(mPosition).setAnimator(0);
                notifyItemChanged(mPosition + 1);
            }
        }
        mPosition = -1;
    }

    public interface onItemOperateListen {
        void zanOperate(int position);

        void twoReplyOperate(int position);

        void operateItem(int position);

        void moreClick(int position);

        void itemclick(int position);

        void clickAudio(int position);

        void sendEerror(int position);

        void longContentItemclick(View v, String content, int postion);

    }

    private onItemOperateListen mListen;

    public void setmListen(onItemOperateListen mListen) {
        this.mListen = mListen;
    }
}
