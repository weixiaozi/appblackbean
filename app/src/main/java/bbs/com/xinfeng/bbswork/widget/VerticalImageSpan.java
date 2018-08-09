package bbs.com.xinfeng.bbswork.widget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;

import bbs.com.xinfeng.bbswork.base.App;
import bbs.com.xinfeng.bbswork.utils.ArmsUtils;

/**
 * Created by dell on 2016/6/12.
 * 图文混排，图片与文字垂直居中
 */
public class VerticalImageSpan extends ImageSpan {
    private float textH;//文字高度

    public VerticalImageSpan(Drawable drawable) {
        super(drawable);
    }

    public int getSize(Paint paint, CharSequence text, int start, int end,
                       Paint.FontMetricsInt fontMetricsInt) {
        Drawable drawable = getDrawable();
        Rect rect = drawable.getBounds();
        if (fontMetricsInt != null) {
            Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
            int fontHeight = fmPaint.bottom - fmPaint.top;
            int drHeight = rect.bottom - rect.top;
            //对于这里我表示,我不知道为啥是这样。不应该是fontHeight/2?但是只有fontHeight/4才能对齐
            //难道是因为TextView的draw的时候top和bottom是大于实际的？具体请看下图
            //所以fontHeight/4是去除偏差?
            int top = drHeight / 2 - fontHeight / 4;
            int bottom = drHeight / 2 + fontHeight / 4;

            fontMetricsInt.ascent = -bottom;
            fontMetricsInt.top = -bottom;
            fontMetricsInt.bottom = top;
            fontMetricsInt.descent = top;
        }
        return rect.right;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end,
                     float x, int top, int y, int bottom, Paint paint) {
        Drawable drawable = getDrawable();
        canvas.save();
        int transY = 0;
        textH = paint.getFontMetrics().descent - paint.getFontMetrics().ascent;
        //获得将要显示的文本高度-图片高度除2等居中位置+top(换行情况)
        if (top == 0) {
            if (bottom >= ArmsUtils.dip2px(App.getInstance(), 40)) {
                transY = (bottom - ArmsUtils.dip2px(App.getInstance(), 10) - drawable.getBounds().bottom) / 2;
            } else if (bottom <= ArmsUtils.dip2px(App.getInstance(), 31) + 1 && bottom >= ArmsUtils.dip2px(App.getInstance(), 31) - 1) {
                transY = (bottom - ArmsUtils.dip2px(App.getInstance(), 10) - drawable.getBounds().bottom) / 2;
            } else if (bottom <= (1.5 + textH + ArmsUtils.dip2px(App.getInstance(), 10)) && bottom >= (-1.5 + textH + ArmsUtils.dip2px(App.getInstance(), 10))) {
                transY = (bottom - ArmsUtils.dip2px(App.getInstance(), 10) - drawable.getBounds().bottom) / 2;
            } else {
                transY = (bottom - drawable.getBounds().bottom) / 2;
            }
        } else {
            transY = top;
        }
//        LogUtil.i("vertiaclimagespan", transY + "___" + bottom + "--" + top + "___" + drawable.getBounds().bottom);
        canvas.translate(x, transY);
        drawable.draw(canvas);
        canvas.restore();
    }
}