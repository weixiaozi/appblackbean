package bbs.com.xinfeng.bbswork.ui.adapter;

import android.app.Activity;
import android.content.Context;
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
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import bbs.com.xinfeng.bbswork.domin.InnerThemeListBean;
import bbs.com.xinfeng.bbswork.module.GlideApp;
import bbs.com.xinfeng.bbswork.utils.ArmsUtils;
import bbs.com.xinfeng.bbswork.utils.FormatUtils;
import bbs.com.xinfeng.bbswork.utils.LogUtil;
import bbs.com.xinfeng.bbswork.utils.SpanUtil;
import bbs.com.xinfeng.bbswork.utils.ToastUtil;
import bbs.com.xinfeng.bbswork.widget.ChattingRootView;
import bbs.com.xinfeng.bbswork.widget.imageShow.ShowImagesDialog;
//import linwg.ImageBrowser;
import me.iwf.photopicker.PhotoPreview;
import okhttp3.OkHttpClient;

import static bbs.com.xinfeng.bbswork.domin.InnerThemeListBean.LOADING;

/**
 * Created by dell on 2017/10/27.
 */

public class ChattingAdapter extends BaseRvAdapter implements ChattingRootView.NotifyAdapterLoading {
    private List<InnerThemeListBean> datas;
    private ItemChattingLoadingBinding loadingBinding;
    private int max = ArmsUtils.dip2px(mContext, 180);
    private RecyclerView recyclerView;


    private final MediaPlayer mp = new MediaPlayer();
    private AnimationDrawable mVoiceAnimation;
    private boolean mSetData = false;
    private int mPosition = -1;// 和mSetData一起组成判断播放哪条录音的依据
    private float mDensity;
    private ShowImagesDialog imagesDialog;

    public ChattingAdapter(Context mContext, List datas, RecyclerView recyclerView) {
        super(mContext);
        this.recyclerView = recyclerView;
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
        if (viewType == LOADING)
            return bind(R.layout.item_chatting_loading, parent);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_chatting, parent, false);
        ItemChattingBinding mBinding = DataBindingUtil.bind(view);
        ContentHolder holder = new ContentHolder(view, mBinding);
        if (holder.mBinding instanceof ItemChattingBinding) {
            ItemChattingBinding chatBinding = (ItemChattingBinding) holder.mBinding;
            chatBinding.llayoutChatZan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListen != null && !datas.get(holder.getLayoutPosition()).isSelf())
                        mListen.zanOperate(holder.getLayoutPosition());
                }
            });
            chatBinding.flayoutChatCollect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListen != null && !datas.get(holder.getLayoutPosition()).isSelf())
                        mListen.collectOperate(holder.getLayoutPosition());
                }
            });
            chatBinding.flayoutChatReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListen != null && !datas.get(holder.getLayoutPosition()).isSelf())
                        mListen.commentOperate(holder.getLayoutPosition());
                }
            });
            chatBinding.ivChatSenderror.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListen != null)
                        mListen.sendEerror(holder.getLayoutPosition());
                }
            });
            chatBinding.ivHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListen != null)
                        mListen.clickHead(holder.getLayoutPosition());
                }
            });
            chatBinding.llayoutChatRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListen != null && !datas.get(holder.getLayoutPosition()).isSelf())
                        mListen.onClick(holder.getLayoutPosition());
                }
            });
            chatBinding.ivThemeVideopathPreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListen != null) {
                        mListen.clickVideo(holder.getLayoutPosition());
                        mListen.clickPic(holder.getLayoutPosition());
                    }
                }
            });
           /* chatBinding.flayoutThemePics.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListen != null && !datas.get(holder.getLayoutPosition()).isSelf())
                        mListen.onClick(holder.getLayoutPosition());
                }
            });*/
            chatBinding.llayoutChatRoot.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mListen != null && !datas.get(holder.getLayoutPosition()).isSelf())
                        mListen.longClick(holder.getLayoutPosition());
                    return true;
                }
            });
            /*chatBinding.flayoutThemePics.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mListen != null && !datas.get(holder.getLayoutPosition()).isSelf())
                        mListen.longClick(holder.getLayoutPosition());
                    return true;
                }
            });*/
        }
        return holder;
    }

    @Override
    protected void onBindHolder(MyViewHolder holder, int position) {
        ViewDataBinding mBinding = holder.mBinding;
        InnerThemeListBean chattingBean = datas.get(position);
        if (mBinding instanceof ItemChattingLoadingBinding) {
            loadingBinding = (ItemChattingLoadingBinding) mBinding;
            ((AnimationDrawable) loadingBinding.imgLoading.getDrawable()).start();
        } else if (mBinding instanceof ItemChattingBinding) {
            ItemChattingBinding otherTxtBinding = (ItemChattingBinding) mBinding;
            if (chattingBean.isSelf()) {
                otherTxtBinding.voiceReadnew.setVisibility(View.GONE);
                otherTxtBinding.llayoutThemeReadnum.setVisibility(View.GONE);
                otherTxtBinding.txtTime.setText(FormatUtils.dataToUseData(chattingBean.getCreated_at2()));
                if (chattingBean.getSelfStatus() == 1) {
                    otherTxtBinding.ivChatSenderror.setVisibility(View.GONE);
                    ((AnimationDrawable) otherTxtBinding.ivChatSendloading.getDrawable()).start();
                    otherTxtBinding.ivChatSendloading.setVisibility(View.VISIBLE);
                } else if (chattingBean.getSelfStatus() == 2) {
                    otherTxtBinding.ivChatSenderror.setVisibility(View.VISIBLE);
                    ((AnimationDrawable) otherTxtBinding.ivChatSendloading.getDrawable()).stop();
                    otherTxtBinding.ivChatSendloading.setVisibility(View.GONE);
                } else {
                    otherTxtBinding.llayoutThemeReadnum.setVisibility(View.VISIBLE);
                    otherTxtBinding.ivHotTheme.setVisibility(View.GONE);
                    otherTxtBinding.ivHotTheme.setVisibility(chattingBean.getIshot() == 1 ? View.VISIBLE : View.GONE);
                    otherTxtBinding.txtThemeReadnum.setText("1 已读");
                    otherTxtBinding.ivChatSenderror.setVisibility(View.GONE);
                    ((AnimationDrawable) otherTxtBinding.ivChatSendloading.getDrawable()).stop();
                    otherTxtBinding.ivChatSendloading.setVisibility(View.GONE);
                }
            } else {
                otherTxtBinding.txtTime.setText(chattingBean.getCreated_at2());
                otherTxtBinding.voiceReadnew.setVisibility(chattingBean.getAudioread() == 1 ? View.GONE : View.VISIBLE);
                otherTxtBinding.llayoutThemeReadnum.setVisibility(View.VISIBLE);
                otherTxtBinding.ivHotTheme.setVisibility(chattingBean.getIshot() == 1 ? View.VISIBLE : View.GONE);
                otherTxtBinding.txtThemeReadnum.setText(chattingBean.getReadnumcn() + " 已读");
                otherTxtBinding.ivChatSenderror.setVisibility(View.GONE);
                ((AnimationDrawable) otherTxtBinding.ivChatSendloading.getDrawable()).stop();
                otherTxtBinding.ivChatSendloading.setVisibility(View.GONE);
            }


            GlideApp.with(mContext).load(chattingBean.getPortrait_thumb()).override(108).placeholder(R.drawable.icon_head_default).error(R.drawable.icon_head_default).into(otherTxtBinding.ivHead);
            otherTxtBinding.txtName.setText(chattingBean.getAuthor_username());

            otherTxtBinding.txtThemeContent.setVisibility(TextUtils.isEmpty(chattingBean.getContent()) ? View.GONE : View.VISIBLE);
            if (!TextUtils.isEmpty(chattingBean.getContent())) {
                otherTxtBinding.txtThemeContent.setMaxLines(chattingBean.getVideoType() == 0 ? 6 : 3);
                otherTxtBinding.txtThemeContent.setText(SpanUtil.getExpressionString(mContext, chattingBean.getContent(), false, otherTxtBinding.txtThemeContent));
            }
            otherTxtBinding.txtZanNum.setText(chattingBean.getLikecn() + " 赞");
            otherTxtBinding.txtReplyNum.setVisibility(TextUtils.equals(chattingBean.getCommentscn(), "0") ? View.GONE : View.VISIBLE);
            if (TextUtils.isEmpty(chattingBean.getLast_at())) {
                otherTxtBinding.txtReplyNum.setText(chattingBean.getCommentscn() + " 回复");
            } else {
                otherTxtBinding.txtReplyNum.setText(chattingBean.getCommentscn() + " 回复（最新回复" + chattingBean.getLast_at() + "）");
            }
           /* otherTxtBinding.ivThemeZan.setSelected(chattingBean.getHasLike() == 1);
            otherTxtBinding.txtThemeZanNum.setVisibility(chattingBean.getLike() > 0 ? View.VISIBLE : View.GONE);
            otherTxtBinding.txtThemeZanNum.setText(chattingBean.getLike() + "");
            otherTxtBinding.ivChatCollect.setSelected(chattingBean.getHasCollect() == 1);
            if (chattingBean.getReplyPosts() != null && chattingBean.getReplyPosts().size() > 0) {
                otherTxtBinding.llayoutThemeComments.setVisibility(View.VISIBLE);
                otherTxtBinding.txtThemeCommentFirst.setVisibility(View.VISIBLE);
                otherTxtBinding.txtThemeCommentSecond.setVisibility(View.GONE);
                otherTxtBinding.txtThemeCommentMore.setVisibility(chattingBean.getComments() > 2 ? View.VISIBLE : View.GONE);
                otherTxtBinding.txtThemeCommentMore.setText("共" + chattingBean.getComments() + "条评论>");
                SpannableStringBuilder firstcomment = SpanUtil.getExpressionString(mContext, chattingBean.getReplyPosts().get(0).getAuthor_user_name() + ":" + chattingBean.getReplyPosts().get(0).getContent(), false, otherTxtBinding.txtThemeCommentFirst);
                firstcomment.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.black)), 0, chattingBean.getReplyPosts().get(0).getAuthor_user_name().length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                otherTxtBinding.txtThemeCommentFirst.setText(firstcomment);
                if (chattingBean.getReplyPosts().size() > 1) {
                    otherTxtBinding.txtThemeCommentSecond.setVisibility(View.VISIBLE);
                    SpannableStringBuilder secondcomment = SpanUtil.getExpressionString(mContext, chattingBean.getReplyPosts().get(1).getAuthor_user_name() + ":" + chattingBean.getReplyPosts().get(1).getContent(), false, otherTxtBinding.txtThemeCommentSecond);
                    secondcomment.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.black)), 0, chattingBean.getReplyPosts().get(1).getAuthor_user_name().length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    otherTxtBinding.txtThemeCommentSecond.setText(secondcomment);
                }
            } else {
                otherTxtBinding.llayoutThemeComments.setVisibility(View.GONE);
            }*/


            if (chattingBean.getVideoType() == 1) {
                otherTxtBinding.flayoutThemePics.setVisibility(View.VISIBLE);
                otherTxtBinding.llayoutAudioRoot.setVisibility(View.GONE);
                otherTxtBinding.fllayoutThemeVideo.setVisibility(View.GONE);
                int size = chattingBean.getThumb().size();
                if (size == 1) {
                    otherTxtBinding.recycleThemePics.setVisibility(View.GONE);
                    otherTxtBinding.flayoutOneParent.setVisibility(View.VISIBLE);
                    if (chattingBean.isSelf()) {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        BitmapFactory.decodeFile(chattingBean.getThumb().get(0), options);
                        int width = options.outWidth;
                        int height = options.outHeight;
                        options.inJustDecodeBounds = false;
                        if (width >= max || height >= max) {
                            if (width > height) {
                                otherTxtBinding.ivThemeOnepic.getLayoutParams().width = max;
                                if (height <= max * 4 / 5) {
                                    otherTxtBinding.ivThemeOnepic.getLayoutParams().height = height;
                                } else {
                                    otherTxtBinding.ivThemeOnepic.getLayoutParams().height = max * 4 / 5;
                                }
                            } else {
                                otherTxtBinding.ivThemeOnepic.getLayoutParams().height = max;
                                if (width <= max * 4 / 5) {
                                    otherTxtBinding.ivThemeOnepic.getLayoutParams().width = width;
                                } else {
                                    otherTxtBinding.ivThemeOnepic.getLayoutParams().width = max * 4 / 5;
                                }
                            }
                        } else {
                            otherTxtBinding.ivThemeOnepic.getLayoutParams().width = width;
                            otherTxtBinding.ivThemeOnepic.getLayoutParams().height = height;
                        }
                        otherTxtBinding.txtLongpic.setVisibility(View.GONE);
                        GlideApp.with(mContext).load(Uri.fromFile(new File(chattingBean.getThumb().get(0)))).centerCrop().placeholder(R.drawable.icon_theme_default_big).error(R.drawable.icon_theme_default_big).into(otherTxtBinding.ivThemeOnepic);

                    } else {
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
                            otherTxtBinding.ivThemeOnepic.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
                            otherTxtBinding.txtLongpic.setVisibility(View.GONE);
                        }
                        GlideApp.with(mContext).load(chattingBean.getThumb().get(0)).centerCrop().placeholder(R.drawable.icon_theme_default_big).error(R.drawable.icon_theme_default_big).into(otherTxtBinding.ivThemeOnepic);
                    }


                    otherTxtBinding.ivThemeOnepic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!chattingBean.isSelf()) {
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
                        }
                    });

                } else {
                    otherTxtBinding.recycleThemePics.setVisibility(View.VISIBLE);
                    otherTxtBinding.flayoutOneParent.setVisibility(View.GONE);
                    ContentHolder mContentHolder = (ContentHolder) holder;
                    if (mContentHolder.picsAdapter == null) {
                        mContentHolder.picsAdapter = new ChatPicsAdapter(mContext, chattingBean.getThumb());
                        mContentHolder.picsAdapter.setItemCount(3);
                        mContentHolder.picsAdapter.setPicWidht(86);
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
                            if (!chattingBean.isSelf()) {
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
                        }
                    });
                }
            } else if (chattingBean.getVideoType() == 2) {
                otherTxtBinding.flayoutThemePics.setVisibility(View.VISIBLE);
                otherTxtBinding.flayoutOneParent.setVisibility(View.GONE);
                otherTxtBinding.recycleThemePics.setVisibility(View.GONE);
                otherTxtBinding.fllayoutThemeVideo.setVisibility(View.GONE);
                otherTxtBinding.llayoutAudioRoot.setVisibility(View.VISIBLE);
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
                            if (!chattingBean.isSelf() && mListen != null) {
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
                if (chattingBean.isSelf()) {
                    if (chattingBean.getVideoHeight() > chattingBean.getVideoWidth()) {
                        otherTxtBinding.fllayoutThemeVideo.getLayoutParams().width = ArmsUtils.dip2px(mContext, 136);
                        otherTxtBinding.fllayoutThemeVideo.getLayoutParams().height = ArmsUtils.dip2px(mContext, 181);
                    } else {
                        otherTxtBinding.fllayoutThemeVideo.getLayoutParams().width = ArmsUtils.dip2px(mContext, 181);
                        otherTxtBinding.fllayoutThemeVideo.getLayoutParams().height = ArmsUtils.dip2px(mContext, 136);
                    }
                } else {
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
                }

                otherTxtBinding.txtVideoDuration.setText(chattingBean.getDuration() != 0 ? chattingBean.getDuration() + "\"" : "");
                if (!chattingBean.getCoverPath().startsWith("http"))
                    GlideApp.with(mContext).load(Uri.fromFile(new File(chattingBean.getCoverPath()))).override(540, 408).centerCrop().into(otherTxtBinding.ivThemeVideopath);
                else
                    GlideApp.with(mContext).load(chattingBean.getCoverPath()).centerCrop().override(540, 408).into(otherTxtBinding.ivThemeVideopath);
            } else {
                otherTxtBinding.flayoutThemePics.setVisibility(View.GONE);
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
            InnerThemeListBean item = datas.get(position);
            return item.getType();
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
            InnerThemeListBean chattingBean = datas.get(0);
            if (chattingBean.getType() != LOADING) {
                datas.add(0, new InnerThemeListBean(LOADING));
                notifyItemInserted(0);
            }
        }
    }

    @Override
    public void removeLoading() {
        if (datas.size() > 1) {
            if (loadingBinding != null)
                ((AnimationDrawable) loadingBinding.imgLoading.getDrawable()).stop();
            InnerThemeListBean chattingBean = datas.get(0);
            if (chattingBean.getType() == LOADING) {
                datas.remove(0);
                notifyItemRemoved(0);
            }
        }
    }

    public boolean onBackPressed() {
        if (imagesDialog != null && imagesDialog.isShowing()) {
            imagesDialog.dismiss();
            return true;
        }
        return false;
    }

    public interface onItemOperateListen {
        void zanOperate(int position);

        void collectOperate(int position);

        void commentOperate(int position);

        void clickHead(int position);

        void longClick(int position);

        void onClick(int position);

        void sendEerror(int position);

        void clickPic(int position);

        void clickAudio(int position);

        void clickVideo(int position);

    }

    private onItemOperateListen mListen;

    public void setOnItemOperateListen(onItemOperateListen mListen) {
        this.mListen = mListen;
    }

    public class ContentHolder extends MyViewHolder {
        ChatPicsAdapter picsAdapter;
        GridLayoutManager gridLayoutManager;

        public ContentHolder(View itemView, ViewDataBinding mBinding) {
            super(itemView, mBinding);
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
}
