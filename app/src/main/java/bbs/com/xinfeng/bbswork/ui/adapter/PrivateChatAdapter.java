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
import android.os.Environment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.base.App;
import bbs.com.xinfeng.bbswork.base.BaseRvAdapter;
import bbs.com.xinfeng.bbswork.base.Constant;
import bbs.com.xinfeng.bbswork.databinding.ItemChattingBinding;
import bbs.com.xinfeng.bbswork.databinding.ItemChattingLoadingBinding;
import bbs.com.xinfeng.bbswork.databinding.ItemPrivateChatBinding;
import bbs.com.xinfeng.bbswork.databinding.ItemPrivateChatSelfBinding;
import bbs.com.xinfeng.bbswork.databinding.ItemPrivateChatSystemBinding;
import bbs.com.xinfeng.bbswork.domin.InnerThemeListBean;
import bbs.com.xinfeng.bbswork.domin.PrivateChatItemBean;
import bbs.com.xinfeng.bbswork.module.GlideApp;
import bbs.com.xinfeng.bbswork.ui.activity.WebviewActivity;
import bbs.com.xinfeng.bbswork.utils.ArmsUtils;
import bbs.com.xinfeng.bbswork.utils.FormatUtils;
import bbs.com.xinfeng.bbswork.utils.ScreenUtils;
import bbs.com.xinfeng.bbswork.utils.SharedPrefUtil;
import bbs.com.xinfeng.bbswork.utils.SpanUtil;
import bbs.com.xinfeng.bbswork.utils.ToastUtil;
import bbs.com.xinfeng.bbswork.widget.ChattingRootView;
import bbs.com.xinfeng.bbswork.widget.imageShow.ShowImagesDialog;

import static bbs.com.xinfeng.bbswork.domin.InnerThemeListBean.LOADING;

//import linwg.ImageBrowser;

/**
 * Created by dell on 2017/10/27.
 */

public class PrivateChatAdapter extends BaseRvAdapter implements ChattingRootView.NotifyAdapterLoading {
    private List<PrivateChatItemBean> datas;
    private ItemChattingLoadingBinding loadingBinding;
    private int max = ArmsUtils.dip2px(mContext, 180);

    private final MediaPlayer mp = new MediaPlayer();
    private AnimationDrawable mVoiceAnimation;
    private boolean mSetData = false;
    private int mPosition = -1;// 和mSetData一起组成判断播放哪条录音的依据
    private float mDensity;


    private int authorId;

    public PrivateChatAdapter(Context mContext, List datas) {
        super(mContext);
        this.datas = datas;
        authorId = SharedPrefUtil.getInt(Constant.userid_key, 0);
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
        if (viewType == LOADING)
            return bind(R.layout.item_chatting_loading, parent);
        else if (viewType == 0) {
            return bind(R.layout.item_private_chat, parent);
        } else if (viewType == 1) {
            MyViewHolder holder = bind(R.layout.item_private_chat_self, parent);
            ItemPrivateChatSelfBinding mBinding = (ItemPrivateChatSelfBinding) holder.mBinding;
            mBinding.tvPchatTxt.setMaxWidth(ScreenUtils.getScreenWidth(mContext) - ArmsUtils.dip2px(mContext, 110));
            return holder;
        } else {
            return bind(R.layout.item_private_chat_system, parent);
        }
    }

    @Override
    protected void onBindHolder(MyViewHolder holder, int position) {
        ViewDataBinding mBinding = holder.mBinding;
        PrivateChatItemBean chattingBean = datas.get(position);
        if (mBinding instanceof ItemChattingLoadingBinding) {
            loadingBinding = (ItemChattingLoadingBinding) mBinding;
            ((AnimationDrawable) loadingBinding.imgLoading.getDrawable()).start();
        } else if (mBinding instanceof ItemPrivateChatSystemBinding) {
            ItemPrivateChatSystemBinding systemBinding = (ItemPrivateChatSystemBinding) mBinding;
            systemBinding.txtPchatSystem.setText(chattingBean.getC1());
        } else if (mBinding instanceof ItemPrivateChatBinding) {
            ItemPrivateChatBinding otherBinding = (ItemPrivateChatBinding) mBinding;
            if (position > 0) {
                PrivateChatItemBean itemBean = datas.get(position - 1);
                ((FrameLayout.LayoutParams) otherBinding.llayoutRoot.getLayoutParams()).setMargins(0, itemBean.getSu() != authorId ? 0 : ArmsUtils.dip2px(mContext, 10), 0, 0);
            } else {
                ((FrameLayout.LayoutParams) otherBinding.llayoutRoot.getLayoutParams()).setMargins(0, 0, 0, 0);
            }
            otherBinding.ivPchatHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListen != null)
                        mListen.clickHead(holder.getLayoutPosition());
                }
            });
            otherBinding.flayoutContentRoot.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mListen != null)
                        mListen.longClick(v, holder.getLayoutPosition(), otherBinding.tvPchatTxt.getText().toString());
                    return true;
                }
            });
            otherBinding.ivPchatPic.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mListen != null)
                        mListen.longClick(v, holder.getLayoutPosition(), otherBinding.tvPchatTxt.getText().toString());
                    return true;
                }
            });
            otherBinding.voiceFl.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mListen != null)
                        mListen.longClick(v, holder.getLayoutPosition(), otherBinding.tvPchatTxt.getText().toString());
                    return true;
                }
            });
            GlideApp.with(mContext).load(chattingBean.getImg()).override(120).placeholder(R.drawable.icon_head_default).error(R.drawable.icon_head_default).into(otherBinding.ivPchatHead);

            if (TextUtils.isEmpty(chattingBean.getTm())) {
                otherBinding.txtPchatTime.setVisibility(View.GONE);
            } else {
                otherBinding.txtPchatTime.setVisibility(View.VISIBLE);
                otherBinding.txtPchatTime.setText(chattingBean.getTm());
            }
            if (chattingBean.getMt() == 1) {
                otherBinding.llayoutRoot.setVisibility(View.VISIBLE);
                otherBinding.llayoutAudioRoot.setVisibility(View.GONE);
                otherBinding.flayoutPchatPic.setVisibility(View.GONE);
                otherBinding.llayoutLink.setVisibility(View.GONE);
                otherBinding.tvPchatTxt.setVisibility(View.VISIBLE);
//                otherBinding.tvPchatTxt.setText(chattingBean.getC1());
                otherBinding.tvPchatTxt.setText(SpanUtil.getExpressionString(mContext, chattingBean.getC1(), true, otherBinding.tvPchatTxt));
                otherBinding.tvPchatTxt.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (mListen != null)
                            mListen.longClick(v, holder.getLayoutPosition(), otherBinding.tvPchatTxt.getText().toString());
                        return true;
                    }
                });
            } else if (chattingBean.getMt() == 2) {
                otherBinding.llayoutRoot.setVisibility(View.VISIBLE);
                otherBinding.llayoutAudioRoot.setVisibility(View.GONE);
                otherBinding.tvPchatTxt.setVisibility(View.GONE);
                otherBinding.llayoutLink.setVisibility(View.GONE);
                otherBinding.flayoutPchatPic.setVisibility(View.VISIBLE);
                try {
                    Uri uri = Uri.parse(chattingBean.getC1());
                    String s = uri.getQueryParameter("s");
                    if (!TextUtils.isEmpty(s)) {
                        otherBinding.txtLongpic.setVisibility(Integer.parseInt(s) == 1 ? View.VISIBLE : View.GONE);
                    } else {
                        otherBinding.txtLongpic.setVisibility(View.GONE);
                    }
                    int width = Integer.parseInt(uri.getQueryParameter("sw"));
                    int height = Integer.parseInt(uri.getQueryParameter("sh"));
                    otherBinding.ivPchatPic.getLayoutParams().width = width;
                    otherBinding.ivPchatPic.getLayoutParams().height = height;
                } catch (Exception e) {
                    otherBinding.ivPchatPic.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
                    otherBinding.txtLongpic.setVisibility(View.GONE);
                }
                GlideApp.with(mContext).load(chattingBean.getC2()).centerCrop().placeholder(R.drawable.icon_theme_default_big).error(R.drawable.icon_theme_default_big).into(otherBinding.ivPchatPic);
                otherBinding.ivPchatPic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!chattingBean.isSelf()) {
                            if (mListen != null)
                                mListen.clickPic(holder.getLayoutPosition());
                        }
                    }
                });

            } else if (chattingBean.getMt() == 3) {
                otherBinding.llayoutRoot.setVisibility(View.VISIBLE);
                otherBinding.flayoutPchatPic.setVisibility(View.GONE);
                otherBinding.tvPchatTxt.setVisibility(View.GONE);
                otherBinding.llayoutLink.setVisibility(View.GONE);
                otherBinding.llayoutAudioRoot.setVisibility(View.VISIBLE);
                otherBinding.voiceReadnew.setVisibility(chattingBean.getS1() == 2 ? View.GONE : View.VISIBLE);
                otherBinding.voiceLengthTv.setText(chattingBean.getDuration() + mContext.getString(R.string.jmui_symbol_second));

                //控制语音长度显示，长度增幅随语音长度逐渐缩小
                int width = (int) (-0.04 * chattingBean.getDuration() * chattingBean.getDuration() + 4.526 * chattingBean.getDuration() + 75.214);
                if (chattingBean.isAnimator() > 0) {
                    if (mVoiceAnimation != null) {
                        otherBinding.voiceIv.setImageResource(chattingBean.isAnimator() == 1 ? R.drawable.jmui_voice_loading_send : R.drawable.jmui_voice_send);
                        mVoiceAnimation = (AnimationDrawable) otherBinding.voiceIv.getDrawable();
                        mVoiceAnimation.start();
                    } else {
                        chattingBean.setAnimator(0);
                        otherBinding.voiceIv.setImageResource(R.drawable.jmui_send_3);
                    }
                } else {
                    otherBinding.voiceIv.setImageResource(R.drawable.jmui_send_3);
                }
                otherBinding.msgContent.setWidth((int) (width * mDensity));


                otherBinding.voiceFl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                            Toast.makeText(mContext, mContext.getString(R.string.jmui_sdcard_not_exist_toast),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            if (!chattingBean.isSelf() && mListen != null) {
                                mListen.clickAudio(holder.getLayoutPosition());
                                otherBinding.voiceReadnew.setVisibility(View.GONE);
                                chattingBean.setS1(2);
                            }

                            // 如果之前存在播放动画，无论这次点击触发的是暂停还是播放，停止上次播放的动画
                            if (mVoiceAnimation != null) {
                                mVoiceAnimation.stop();
                                mVoiceAnimation = null;
                            }
                            // 播放中点击了正在播放的Item 则暂停播放
                            if (mp.isPlaying() && mPosition == position) {
                                otherBinding.voiceIv.setImageResource(R.drawable.jmui_voice_send);
                                mVoiceAnimation = (AnimationDrawable) otherBinding.voiceIv.getDrawable();
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
                                        otherBinding.voiceIv.setImageResource(R.drawable.jmui_voice_send);
                                        mVoiceAnimation = (AnimationDrawable) otherBinding.voiceIv.getDrawable();
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
                                        mp.setDataSource(mContext, Uri.parse(chattingBean.getC1()));
                                        if (App.getInstance().mIsEarPhoneOn) {
                                            mp.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
                                        } else {
                                            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                        }

                                        otherBinding.voiceIv.setImageResource(R.drawable.jmui_voice_loading_send);
                                        mVoiceAnimation = (AnimationDrawable) otherBinding.voiceIv.getDrawable();
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
                                                otherBinding.voiceIv.setImageResource(R.drawable.jmui_voice_send);
                                                mVoiceAnimation = (AnimationDrawable) otherBinding.voiceIv.getDrawable();
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
                                    otherBinding.voiceIv.setImageResource(R.drawable.jmui_send_3);
                                    ToastUtil.showToast(mContext.getString(R.string.jmui_file_not_found_toast));
                                } catch (IllegalArgumentException e) {
                                    if (mVoiceAnimation != null) {
                                        mVoiceAnimation.stop();
                                        mVoiceAnimation = null;
                                    }
                                    otherBinding.voiceIv.setImageResource(R.drawable.jmui_send_3);
                                    e.printStackTrace();
                                } catch (SecurityException e) {
                                    if (mVoiceAnimation != null) {
                                        mVoiceAnimation.stop();
                                        mVoiceAnimation = null;
                                    }
                                    otherBinding.voiceIv.setImageResource(R.drawable.jmui_send_3);
                                    e.printStackTrace();
                                } catch (IllegalStateException e) {
                                    if (mVoiceAnimation != null) {
                                        mVoiceAnimation.stop();
                                        mVoiceAnimation = null;
                                    }
                                    otherBinding.voiceIv.setImageResource(R.drawable.jmui_send_3);
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    if (mVoiceAnimation != null) {
                                        mVoiceAnimation.stop();
                                        mVoiceAnimation = null;
                                    }
                                    otherBinding.voiceIv.setImageResource(R.drawable.jmui_send_3);
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
                                otherBinding.voiceIv.setImageResource(R.drawable.jmui_send_3);
                            }
                        });
                    }

                    private void pauseVoice() {
                        mp.pause();
                        mSetData = true;
                    }
                });
            } else if (chattingBean.getMt() == 5) {
                otherBinding.llayoutRoot.setVisibility(View.VISIBLE);
                otherBinding.flayoutPchatPic.setVisibility(View.GONE);
                otherBinding.tvPchatTxt.setVisibility(View.GONE);
                otherBinding.llayoutAudioRoot.setVisibility(View.GONE);
                otherBinding.llayoutLink.setVisibility(View.VISIBLE);
                otherBinding.txtLinkTitle.setVisibility(TextUtils.isEmpty(chattingBean.getUt()) ? View.GONE : View.VISIBLE);
                otherBinding.txtLinkTitle.setText(chattingBean.getUt());
                otherBinding.txtLinkDes.setVisibility(TextUtils.isEmpty(chattingBean.getUd()) ? View.GONE : View.VISIBLE);
                otherBinding.txtLinkDes.setText(chattingBean.getUd());
                otherBinding.txtLinkLink.setText(chattingBean.getC1());
                otherBinding.llayoutLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, WebviewActivity.class);
                        intent.putExtra("weburl", chattingBean.getC1());
                        intent.putExtra("linksrc", true);
                        intent.putExtra("head", true);
                        mContext.startActivity(intent);
                    }
                });
                otherBinding.llayoutLink.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (mListen != null)
                            mListen.longClick(v, holder.getLayoutPosition(), chattingBean.getC1());
                        return true;
                    }
                });
            } else {
                otherBinding.llayoutRoot.setVisibility(View.GONE);
            }


        } else if (mBinding instanceof ItemPrivateChatSelfBinding) {
            ItemPrivateChatSelfBinding selfBinding = (ItemPrivateChatSelfBinding) mBinding;
            if (position > 0) {
                PrivateChatItemBean itemBean = datas.get(position - 1);
                ((FrameLayout.LayoutParams) selfBinding.llayoutRoot.getLayoutParams()).setMargins(0, itemBean.getSu() == authorId ? 0 : ArmsUtils.dip2px(mContext, 10), 0, 0);
            } else {
                ((FrameLayout.LayoutParams) selfBinding.llayoutRoot.getLayoutParams()).setMargins(0, 0, 0, 0);
            }
            GlideApp.with(mContext).load(chattingBean.getImg()).override(120).placeholder(R.drawable.icon_head_default).error(R.drawable.icon_head_default).into(selfBinding.ivPchatHead);
            selfBinding.ivPchatHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListen != null)
                        mListen.clickHead(holder.getLayoutPosition());
                }
            });
            selfBinding.flayoutContentRoot.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mListen != null)
                        mListen.longClick(v, holder.getLayoutPosition(), selfBinding.tvPchatTxt.getText().toString());
                    return true;
                }
            });
            selfBinding.ivPchatPic.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mListen != null)
                        mListen.longClick(v, holder.getLayoutPosition(), selfBinding.tvPchatTxt.getText().toString());
                    return true;
                }
            });
            selfBinding.voiceFl.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mListen != null)
                        mListen.longClick(v, holder.getLayoutPosition(), selfBinding.tvPchatTxt.getText().toString());
                    return true;
                }
            });
            if (chattingBean.getMt() == 1) {
                selfBinding.llayoutRoot.setVisibility(View.VISIBLE);
                selfBinding.llayoutAudioRoot.setVisibility(View.GONE);
                selfBinding.flayoutPchatPic.setVisibility(View.GONE);
                selfBinding.llayoutLink.setVisibility(View.GONE);
                selfBinding.tvPchatTxt.setVisibility(View.VISIBLE);
//                selfBinding.tvPchatTxt.setText(chattingBean.getC1());
                selfBinding.tvPchatTxt.setText(SpanUtil.getExpressionString(mContext, chattingBean.getC1(), true, selfBinding.tvPchatTxt));
                selfBinding.tvPchatTxt.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (mListen != null)
                            mListen.longClick(v, holder.getLayoutPosition(), selfBinding.tvPchatTxt.getText().toString());
                        return true;
                    }
                });
            } else if (chattingBean.getMt() == 2) {
                selfBinding.llayoutRoot.setVisibility(View.VISIBLE);
                selfBinding.llayoutAudioRoot.setVisibility(View.GONE);
                selfBinding.tvPchatTxt.setVisibility(View.GONE);
                selfBinding.llayoutLink.setVisibility(View.GONE);
                selfBinding.flayoutPchatPic.setVisibility(View.VISIBLE);
                if (chattingBean.isSelf()) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(chattingBean.getC2(), options);
                    int width = options.outWidth;
                    int height = options.outHeight;
                    options.inJustDecodeBounds = false;
                    if (width >= max || height >= max) {
                        if (width > height) {
                            selfBinding.ivPchatPic.getLayoutParams().width = max;
                            if (height <= max * 4 / 5) {
                                selfBinding.ivPchatPic.getLayoutParams().height = height;
                            } else {
                                selfBinding.ivPchatPic.getLayoutParams().height = max * 4 / 5;
                            }
                        } else {
                            selfBinding.ivPchatPic.getLayoutParams().height = max;
                            if (width <= max * 4 / 5) {
                                selfBinding.ivPchatPic.getLayoutParams().width = width;
                            } else {
                                selfBinding.ivPchatPic.getLayoutParams().width = max * 4 / 5;
                            }
                        }
                    } else {
                        selfBinding.ivPchatPic.getLayoutParams().width = width;
                        selfBinding.ivPchatPic.getLayoutParams().height = height;
                    }
                    selfBinding.txtLongpic.setVisibility(View.GONE);
                    GlideApp.with(mContext).load(Uri.fromFile(new File(chattingBean.getC2()))).centerCrop().placeholder(R.drawable.icon_theme_default_big).error(R.drawable.icon_theme_default_big).into(selfBinding.ivPchatPic);

                } else {
                    try {
                        Uri uri = Uri.parse(chattingBean.getC1());
                        String s = uri.getQueryParameter("s");
                        if (!TextUtils.isEmpty(s)) {
                            selfBinding.txtLongpic.setVisibility(Integer.parseInt(s) == 1 ? View.VISIBLE : View.GONE);
                        } else {
                            selfBinding.txtLongpic.setVisibility(View.GONE);
                        }
                        int width = Integer.parseInt(uri.getQueryParameter("sw"));
                        int height = Integer.parseInt(uri.getQueryParameter("sh"));
                        selfBinding.ivPchatPic.getLayoutParams().width = width;
                        selfBinding.ivPchatPic.getLayoutParams().height = height;
                    } catch (Exception e) {
                        selfBinding.ivPchatPic.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
                        selfBinding.txtLongpic.setVisibility(View.GONE);
                    }
                    GlideApp.with(mContext).load(chattingBean.getC2()).centerCrop().placeholder(R.drawable.icon_theme_default_big).error(R.drawable.icon_theme_default_big).into(selfBinding.ivPchatPic);
                }
                selfBinding.ivPchatPic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!chattingBean.isSelf()) {
                            if (mListen != null)
                                mListen.clickPic(holder.getLayoutPosition());
                        }
                    }
                });

            } else if (chattingBean.getMt() == 3) {
                selfBinding.llayoutRoot.setVisibility(View.VISIBLE);
                selfBinding.flayoutPchatPic.setVisibility(View.GONE);
                selfBinding.tvPchatTxt.setVisibility(View.GONE);
                selfBinding.llayoutLink.setVisibility(View.GONE);
                selfBinding.llayoutAudioRoot.setVisibility(View.VISIBLE);

                selfBinding.voiceLengthTv.setText(chattingBean.getDuration() + mContext.getString(R.string.jmui_symbol_second));

                //控制语音长度显示，长度增幅随语音长度逐渐缩小
                int width = (int) (-0.04 * chattingBean.getDuration() * chattingBean.getDuration() + 4.526 * chattingBean.getDuration() + 75.214);
                if (chattingBean.isAnimator() > 0) {
                    if (mVoiceAnimation != null) {
                        selfBinding.voiceIv.setImageResource(chattingBean.isAnimator() == 1 ? R.drawable.jmui_voice_loading_send : R.drawable.jmui_voice_send_self);
                        mVoiceAnimation = (AnimationDrawable) selfBinding.voiceIv.getDrawable();
                        mVoiceAnimation.start();
                    } else {
                        chattingBean.setAnimator(0);
                        selfBinding.voiceIv.setImageResource(R.drawable.jmui_send_self_3);
                    }
                } else {
                    selfBinding.voiceIv.setImageResource(R.drawable.jmui_send_self_3);
                }
                selfBinding.msgContent.getLayoutParams().width = (int) (width * mDensity);


                selfBinding.voiceFl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                            Toast.makeText(mContext, mContext.getString(R.string.jmui_sdcard_not_exist_toast),
                                    Toast.LENGTH_SHORT).show();
                        } else {

                            // 如果之前存在播放动画，无论这次点击触发的是暂停还是播放，停止上次播放的动画
                            if (mVoiceAnimation != null) {
                                mVoiceAnimation.stop();
                                mVoiceAnimation = null;
                            }
                            // 播放中点击了正在播放的Item 则暂停播放
                            if (mp.isPlaying() && mPosition == position) {
                                selfBinding.voiceIv.setImageResource(R.drawable.jmui_voice_send_self);
                                mVoiceAnimation = (AnimationDrawable) selfBinding.voiceIv.getDrawable();
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
                                        selfBinding.voiceIv.setImageResource(R.drawable.jmui_voice_send_self);
                                        mVoiceAnimation = (AnimationDrawable) selfBinding.voiceIv.getDrawable();
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
                                        mp.setDataSource(mContext, Uri.parse(chattingBean.getC1()));
                                        if (App.getInstance().mIsEarPhoneOn) {
                                            mp.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
                                        } else {
                                            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                        }

                                        selfBinding.voiceIv.setImageResource(R.drawable.jmui_voice_loading_send);
                                        mVoiceAnimation = (AnimationDrawable) selfBinding.voiceIv.getDrawable();
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
                                                selfBinding.voiceIv.setImageResource(R.drawable.jmui_voice_send_self);
                                                mVoiceAnimation = (AnimationDrawable) selfBinding.voiceIv.getDrawable();
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
                                    selfBinding.voiceIv.setImageResource(R.drawable.jmui_send_self_3);
                                    ToastUtil.showToast(mContext.getString(R.string.jmui_file_not_found_toast));
                                } catch (IllegalArgumentException e) {
                                    if (mVoiceAnimation != null) {
                                        mVoiceAnimation.stop();
                                        mVoiceAnimation = null;
                                    }
                                    selfBinding.voiceIv.setImageResource(R.drawable.jmui_send_self_3);
                                    e.printStackTrace();
                                } catch (SecurityException e) {
                                    if (mVoiceAnimation != null) {
                                        mVoiceAnimation.stop();
                                        mVoiceAnimation = null;
                                    }
                                    selfBinding.voiceIv.setImageResource(R.drawable.jmui_send_self_3);
                                    e.printStackTrace();
                                } catch (IllegalStateException e) {
                                    if (mVoiceAnimation != null) {
                                        mVoiceAnimation.stop();
                                        mVoiceAnimation = null;
                                    }
                                    selfBinding.voiceIv.setImageResource(R.drawable.jmui_send_self_3);
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    if (mVoiceAnimation != null) {
                                        mVoiceAnimation.stop();
                                        mVoiceAnimation = null;
                                    }
                                    selfBinding.voiceIv.setImageResource(R.drawable.jmui_send_self_3);
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
                                selfBinding.voiceIv.setImageResource(R.drawable.jmui_send_self_3);
                            }
                        });
                    }

                    private void pauseVoice() {
                        mp.pause();
                        mSetData = true;
                    }
                });

            } else if (chattingBean.getMt() == 5) {
                selfBinding.llayoutRoot.setVisibility(View.VISIBLE);
                selfBinding.flayoutPchatPic.setVisibility(View.GONE);
                selfBinding.tvPchatTxt.setVisibility(View.GONE);
                selfBinding.llayoutAudioRoot.setVisibility(View.GONE);
                selfBinding.llayoutLink.setVisibility(View.VISIBLE);
                selfBinding.txtLinkTitle.setVisibility(TextUtils.isEmpty(chattingBean.getUt()) ? View.GONE : View.VISIBLE);
                selfBinding.txtLinkTitle.setText(chattingBean.getUt());
                selfBinding.txtLinkDes.setVisibility(TextUtils.isEmpty(chattingBean.getUd()) ? View.GONE : View.VISIBLE);
                selfBinding.txtLinkDes.setText(chattingBean.getUd());
                selfBinding.txtLinkLink.setText(chattingBean.getC1());
                selfBinding.llayoutLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, WebviewActivity.class);
                        intent.putExtra("weburl", chattingBean.getC1());
                        intent.putExtra("linksrc", true);
                        intent.putExtra("head", true);
                        mContext.startActivity(intent);
                    }
                });
                selfBinding.llayoutLink.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (mListen != null)
                            mListen.longClick(v, holder.getLayoutPosition(), chattingBean.getC1());
                        return true;
                    }
                });
            } else {
                selfBinding.llayoutRoot.setVisibility(View.GONE);
            }

            if (chattingBean.isSelf()) {
//                selfBinding.txtChatTest.setText("true" + "__" + chattingBean.getId() + "__" + chattingBean.getTime() + "___" + chattingBean.getS());
               /* if (position != 0) {
                    if ((chattingBean.getTime() - datas.get(position - 1).getMs() * 1000) / 1000 > 300) {
                        selfBinding.txtPchatTime.setVisibility(View.VISIBLE);
                        selfBinding.txtPchatTime.setText(FormatUtils.dataToUseData(FormatUtils.stampToDate("" + (chattingBean.getTime()) / 1000)));
                    } else {
                        selfBinding.txtPchatTime.setVisibility(View.GONE);
                    }
                } else {
                    selfBinding.txtPchatTime.setVisibility(View.VISIBLE);
                    selfBinding.txtPchatTime.setText(FormatUtils.dataToUseData(FormatUtils.stampToDate("" + (chattingBean.getTime()) / 1000)));

                }*/
                selfBinding.txtPchatTime.setVisibility(TextUtils.isEmpty(chattingBean.getTm()) ? View.GONE : View.VISIBLE);
                selfBinding.txtPchatTime.setText(chattingBean.getTm());

//                selfBinding.voiceReadnew.setVisibility(View.GONE);
                if (chattingBean.getSelfStatus() == 1) {
                    selfBinding.txtChatReadstatus.setVisibility(View.GONE);
                    selfBinding.ivChatSenderror.setVisibility(View.GONE);
                    ((AnimationDrawable) selfBinding.ivChatSendloading.getDrawable()).start();
                    selfBinding.ivChatSendloading.setVisibility(View.VISIBLE);
//                    selfBinding.ivChatSendloading.startAnimation(rotatTheme);
                } else if (chattingBean.getSelfStatus() == 2) {
                    selfBinding.txtChatReadstatus.setVisibility(View.GONE);
                    selfBinding.ivChatSendloading.setVisibility(View.GONE);
                    selfBinding.ivChatSenderror.setVisibility(View.VISIBLE);
                    ((AnimationDrawable) selfBinding.ivChatSendloading.getDrawable()).stop();
                    selfBinding.ivChatSenderror.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mListen != null)
                                mListen.sendEerror(holder.getLayoutPosition());
                        }
                    });
                } else {
                    selfBinding.txtChatReadstatus.setVisibility(View.VISIBLE);
                    selfBinding.txtChatReadstatus.setText("未读");
                    selfBinding.ivChatSenderror.setVisibility(View.GONE);
                    ((AnimationDrawable) selfBinding.ivChatSendloading.getDrawable()).stop();
                    selfBinding.ivChatSendloading.setVisibility(View.GONE);
                }
            } else {
//                selfBinding.txtChatTest.setText("false" + "__" + chattingBean.getId() + "__" + chattingBean.getTime() + "__" + chattingBean.getS());
                if (TextUtils.isEmpty(chattingBean.getTm())) {
                    selfBinding.txtPchatTime.setVisibility(View.GONE);
                } else {
                    selfBinding.txtPchatTime.setVisibility(View.VISIBLE);
                    selfBinding.txtPchatTime.setText(chattingBean.getTm());
                }

                selfBinding.ivChatSenderror.setVisibility(View.GONE);
                ((AnimationDrawable) selfBinding.ivChatSendloading.getDrawable()).stop();
                selfBinding.ivChatSendloading.setVisibility(View.GONE);
                selfBinding.txtChatReadstatus.setVisibility(View.VISIBLE);
                selfBinding.txtChatReadstatus.setText(chattingBean.getS1() == 2 ? "已读" : "未读");

            }
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (datas != null) {
            PrivateChatItemBean item = datas.get(position);
            if (item.getType() == LOADING) {
                return item.getType();
            } else {
                if (item.getMt() == 53 || item.getMt() == 54) {
                    return 2;
                } else {
                    return item.getSu() == authorId ? 1 : 0;
                }
            }
        }
        return super.getItemViewType(position);
    }

    public void LoadingIsStart(boolean isStart) {
        if (loadingBinding != null) {
            if (isStart)
                loadingBinding.imgLoading.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            else
                loadingBinding.imgLoading.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1));
        }
    }

    @Override
    public void addLoading() {
        if (datas.size() > 1) {
            PrivateChatItemBean chattingBean = datas.get(0);
            if (chattingBean.getType() != LOADING) {
                datas.add(0, new PrivateChatItemBean(LOADING));
                notifyItemInserted(0);
            }
        }
    }

    @Override
    public void removeLoading() {
        if (datas.size() > 1) {
            if (loadingBinding != null)
                ((AnimationDrawable) loadingBinding.imgLoading.getDrawable()).stop();
            PrivateChatItemBean chattingBean = datas.get(0);
            if (chattingBean.getType() == LOADING) {
                datas.remove(0);
                notifyItemRemoved(0);
            }
        }
    }


    public interface onItemOperateListen {

        void clickHead(int position);

        void longClick(View v, int postion, String content);

        void onClick(int position);

        void sendEerror(int position);

        void clickAudio(int position);

        void clickPic(int position);

    }

    private onItemOperateListen mListen;

    public void setOnItemOperateListen(onItemOperateListen mListen) {
        this.mListen = mListen;
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
}
