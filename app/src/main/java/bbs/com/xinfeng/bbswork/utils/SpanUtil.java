package bbs.com.xinfeng.bbswork.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.base.App;
import bbs.com.xinfeng.bbswork.base.Constant;
import bbs.com.xinfeng.bbswork.ui.activity.UserInfoActivity;
import bbs.com.xinfeng.bbswork.ui.activity.WebviewActivity;
import bbs.com.xinfeng.bbswork.widget.VerticalImageSpan;

/**
 * Created by dell on 2018/4/27.
 */

public class SpanUtil {
    static Drawable drawable;
    static Drawable drawableVideo;


    public static SpannableStringBuilder getExpressionString(Context mContext, String str, boolean isclick, TextView textView) {
        if (drawable == null) {
            drawable = App.getInstance().getApplicationContext().getResources().getDrawable(R.drawable.icon_link);
            drawable.setBounds(0, 0, ArmsUtils.dip2px(App.getInstance(), 15), ArmsUtils.dip2px(App.getInstance(), 15));
        }
        if (drawableVideo == null) {
            drawableVideo = App.getInstance().getApplicationContext().getResources().getDrawable(R.drawable.icon_link_video);
            drawableVideo.setBounds(0, 0, ArmsUtils.dip2px(App.getInstance(), 19), ArmsUtils.dip2px(App.getInstance(), 15));
        }

        SpannableStringBuilder spannableString = new SpannableStringBuilder(str + " ");
        String zhengzeAll = "\\~\\^\\\\(.+?)\\/\\^\\~";
        // 通过传入的正则表达式来生成一个pattern
        Pattern atPatten = Pattern.compile(zhengzeAll);
        Matcher matcher = atPatten.matcher(spannableString);
        int mixLength = 0;
        while (matcher.find()) {
            String group = matcher.group();
            String[] split = group.split("\\\0");
            if (split.length >= 2) {
                if (isclick) {
                    spannableString.setSpan(new ClickableSpan() {
                        @Override
                        public void onClick(View widget) {
                            if ("1".equals(split[0].substring(3))) {
                                Intent intent = new Intent(mContext, UserInfoActivity.class);
                                intent.putExtra("userid", Integer.parseInt(split[2].substring(0, split[2].length() - 3)));
                                mContext.startActivity(intent);
                            } else if ("2".equals(split[0].substring(3))) {
                                Intent intent = new Intent(mContext, WebviewActivity.class);
                                intent.putExtra("weburl", split[1]);
                                intent.putExtra("head", true);
                                mContext.startActivity(intent);
                            } else if ("3".equals(split[0].substring(3))) {
                                Intent intent = new Intent(mContext, WebviewActivity.class);
                                intent.putExtra("webtitle", split[2].substring(0, split[2].length() - 3));
                                intent.putExtra("weburl", split[1]);
                                intent.putExtra("head", true);
                                mContext.startActivity(intent);
                            } else if ("4".equals(split[0].substring(3))) {
                                Intent intent = new Intent(mContext, WebviewActivity.class);
                                intent.putExtra("weburl", split[1].substring(0, split[1].length() - 3));
                                intent.putExtra("linksrc", true);
                                intent.putExtra("head", true);
                                mContext.startActivity(intent);
                            } else if ("5".equals(split[0].substring(3))) {
                                Intent intent = new Intent(mContext, UserInfoActivity.class);
                                intent.putExtra("userid", Integer.parseInt(split[2].substring(0, split[2].length() - 3)));
                                mContext.startActivity(intent);
                            }
                        }

                        @Override
                        public void updateDrawState(@NonNull TextPaint ds) {
                            super.updateDrawState(ds);
                            ds.setUnderlineText(false);
                            ds.clearShadowLayer();
                        }
                    }, matcher.start() - mixLength, matcher.end() - mixLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    textView.setMovementMethod(LinkMovementClickMethod.getInstance());
                    textView.setFocusable(false);
                }


                if ("1".equals(split[0].substring(3))) {
                    spannableString.setSpan(new ForegroundColorSpan(0xff3B7CFF),
                            matcher.start() - mixLength, matcher.end() - mixLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    spannableString.replace(matcher.start() - mixLength, matcher.end() - mixLength, "@" + split[1]);
                    mixLength += group.length() - split[1].length() - 1;
                } else if ("2".equals(split[0].substring(3))) {
                    spannableString.setSpan(new ForegroundColorSpan(0xff3E69F6),
                            matcher.start() - mixLength, matcher.end() - mixLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    spannableString.replace(matcher.start() - mixLength, matcher.end() - mixLength, "啊啊" + split[2].substring(0, split[2].length() - 3) + " ");

                    spannableString.setSpan(new VerticalImageSpan(drawable), matcher.start() - mixLength, matcher.start() - mixLength + 2, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    mixLength += group.length() - split[2].substring(0, split[2].length() - 3).length() - 3;
                } else if ("3".equals(split[0].substring(3))) {
                    spannableString.setSpan(new ForegroundColorSpan(0xff3E69F6),
                            matcher.start() - mixLength, matcher.end() - mixLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    spannableString.replace(matcher.start() - mixLength, matcher.end() - mixLength, "啊啊" + split[2].substring(0, split[2].length() - 3) + " ");

                    spannableString.setSpan(new VerticalImageSpan(drawableVideo), matcher.start() - mixLength, matcher.start() - mixLength + 2, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    mixLength += group.length() - split[2].substring(0, split[2].length() - 3).length() - 3;
                } else if ("4".equals(split[0].substring(3))) {
                    spannableString.setSpan(new ForegroundColorSpan(0xff3B7CFF),
                            matcher.start() - mixLength, matcher.end() - mixLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    spannableString.replace(matcher.start() - mixLength, matcher.end() - mixLength, split[1].substring(0, split[1].length() - 3));

                    mixLength += group.length() - split[1].length() + 3;
/*
                    spannableString.replace(matcher.start() - mixLength, matcher.end() - mixLength, "啊啊" + split[1].substring(0, split[1].length() - 3));

                    spannableString.setSpan(new VerticalImageSpan(drawable), matcher.start() - mixLength, matcher.start() - mixLength + 2, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    mixLength += group.length() - split[1].length() - 2;
*/
                } else if ("5".equals(split[0].substring(3))) {
                    spannableString.setSpan(new ForegroundColorSpan(0xff3B7CFF),
                            matcher.start() - mixLength, matcher.end() - mixLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    spannableString.replace(matcher.start() - mixLength, matcher.end() - mixLength, split[1]);
                    mixLength += group.length() - split[1].length();
                }

            }

        }


        return spannableString;
    }

    public static class LinkMovementClickMethod extends LinkMovementMethod {

        private long lastClickTime;

        private static final long CLICK_DELAY = 500l;

        @Override
        public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
            int action = event.getAction();

            if (action == MotionEvent.ACTION_UP ||
                    action == MotionEvent.ACTION_DOWN) {
                int x = (int) event.getX();
                int y = (int) event.getY();

                x -= widget.getTotalPaddingLeft();
                y -= widget.getTotalPaddingTop();

                x += widget.getScrollX();
                y += widget.getScrollY();

                Layout layout = widget.getLayout();
                int line = layout.getLineForVertical(y);
                int off = layout.getOffsetForHorizontal(line, x);

                ClickableSpan[] link = buffer.getSpans(off, off, ClickableSpan.class);

                if (link.length != 0) {
                    if (action == MotionEvent.ACTION_UP) {
                        if (System.currentTimeMillis() - lastClickTime < CLICK_DELAY) {
                            link[0].onClick(widget);
                        }
                    } else if (action == MotionEvent.ACTION_DOWN) {
                        Selection.setSelection(buffer,
                                buffer.getSpanStart(link[0]),
                                buffer.getSpanEnd(link[0]));
                        lastClickTime = System.currentTimeMillis();
                    }

                    return true;
                } else {
                    Selection.removeSelection(buffer);
                }
            }
            return super.onTouchEvent(widget, buffer, event);
        }

        public static LinkMovementClickMethod getInstance() {
            if (null == sInstance) {
                sInstance = new LinkMovementClickMethod();
            }
            return sInstance;
        }

        private static LinkMovementClickMethod sInstance;

    }
}
