package bbs.com.xinfeng.bbswork.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
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
import bbs.com.xinfeng.bbswork.utils.ScreenUtils;
import bbs.com.xinfeng.bbswork.utils.SpanUtil;
import bbs.com.xinfeng.bbswork.utils.ToastUtil;
import bbs.com.xinfeng.bbswork.widget.CircleImageView;
import bbs.com.xinfeng.bbswork.widget.imageShow.ShowImagesDialog;

/**
 * Created by dell on 2018/4/10.
 */

public class ReplyTwoAdapter extends BaseQuickAdapter<ThemeDetailReplyBean.DataBean, BaseViewHolder> {
    private float mDensity;
    private AnimationDrawable mVoiceAnimation;
    private boolean mSetData = false;
    private int mPosition = -1;// 和mSetData一起组成判断播放哪条录音的依据
    private MediaPlayer mp;
    private int textWidth;

    public ReplyTwoAdapter(@Nullable List<ThemeDetailReplyBean.DataBean> data, MediaPlayer mediaPlayer) {
        super(R.layout.item_reply_two, data);
        this.mp = mediaPlayer;
    }


    @Override
    protected void convert(BaseViewHolder helper, ThemeDetailReplyBean.DataBean item) {
        if (mDensity == 0) {
            DisplayMetrics dm = new DisplayMetrics();
            ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
            mDensity = dm.density;
            textWidth = ScreenUtils.getScreenWidth(mContext) - ArmsUtils.dip2px(mContext, 56);
        }

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

        String content = item.getUser().getName() + " " + item.getContent();
        String time = " " + item.getCreated_at2();
        SpannableStringBuilder expressionString = SpanUtil.getExpressionString(mContext, content + time, true, helper.getView(R.id.txt_name));
        expressionString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, item.getUser().getName().length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        expressionString.setSpan(new ForegroundColorSpan(0xFFA4A4A4), expressionString.toString().length() - time.length(), expressionString.toString().length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        expressionString.setSpan(new AbsoluteSizeSpan(12, true), expressionString.toString().length() - time.length(), expressionString.toString().length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        TextView txtView = helper.getView(R.id.txt_name);
        TextPaint paint = txtView.getPaint();
        float textLength = paint.measureText(expressionString.toString());
        float timeLength = paint.measureText(time);
        float remainLength = (textLength % textWidth);
        if (remainLength < timeLength) {
            String timeNew = "\n" + item.getCreated_at2();
            SpannableStringBuilder expressionStringnew = SpanUtil.getExpressionString(mContext, content + timeNew, true, helper.getView(R.id.txt_name));
            expressionStringnew.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, item.getUser().getName().length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            expressionStringnew.setSpan(new ForegroundColorSpan(0xFFA4A4A4), expressionString.toString().length() - timeNew.length(), expressionString.toString().length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            expressionStringnew.setSpan(new AbsoluteSizeSpan(12, true), expressionString.toString().length() - timeNew.length(), expressionString.toString().length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            helper.setText(R.id.txt_name, expressionStringnew);
        } else {
            helper.setText(R.id.txt_name, expressionString);
        }
//        helper.setText(R.id.txt_time, FormatUtils.dataToUseData(item.getCreated_at()));
        helper.getView(R.id.txt_name).setOnClickListener(v -> {
            if (mListen != null)
                mListen.atPerson(helper.getLayoutPosition());

        });
        helper.getView(R.id.txt_name).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mListen != null)
                    mListen.longContentItemclick(helper.getView(R.id.txt_name), ((TextView) helper.getView(R.id.txt_name)).getText().toString(), helper.getLayoutPosition());
                return true;
            }
        });
       /* helper.getView(R.id.txt_reply_content).setVisibility(!TextUtils.isEmpty(item.getContent()) ? View.VISIBLE : View.GONE);
        helper.setText(R.id.txt_reply_content, SpanUtil.getExpressionString(mContext, item.getContent(),
                true, helper.getView(R.id.txt_reply_content)));
        helper.getView(R.id.txt_reply_content).setOnClickListener(v -> {
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
        helper.getView(R.id.txt_reply_zan_num).setVisibility(item.getLikenum() > 0 ? View.VISIBLE : View.GONE);
        helper.setText(R.id.txt_reply_zan_num, item.getLikenum() + "");*/

        if (item.getVideoType() == 2) {
            helper.getView(R.id.fllayout_theme_video).setVisibility(View.GONE);
            helper.getView(R.id.flayout_theme_pics).setVisibility(View.VISIBLE);
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
        helper.getView(R.id.fllayout_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!item.isSelf() && mListen != null)
                    mListen.moreClick(helper.getLayoutPosition());
            }
        });
    }

    public interface onItemOperateListen {
        void zanOperate(int position);

        void operateItem(int position);

        void moreClick(int position);

        void clickAudio(int position);

        void sendEerror(int position);

        void atPerson(int position);

        void longContentItemclick(View v, String content, int postion);

    }

    private onItemOperateListen mListen;

    public void setmListen(onItemOperateListen mListen) {
        this.mListen = mListen;
    }

    public boolean isplaying() {
        return (mp.isPlaying() && mVoiceAnimation != null);
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
}
