package bbs.com.xinfeng.bbswork.widget;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.UUID;


import android.content.Context;
import android.graphics.Rect;
import android.text.Editable;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.MotionEvent;
import android.widget.TextView;

import bbs.com.xinfeng.bbswork.utils.CompatibilityUtils;

/**
 * The TextView that handles correctly clickable spans.
 */
public class CustomTextView extends TextView {
    private long lastClickTime;

    private static final long CLICK_DELAY = 500l;
    public static final String TAG = "CustomTextView";

    private boolean mBaseEditorCopied = false;
    private Object mBaseEditor = null;
    private Field mDiscardNextActionUpField = null;
    private Field mIgnoreActionUpEventField = null;

    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onStartTemporaryDetach() {
        super.onStartTemporaryDetach();

        // listview scrolling behaves incorrectly after you select and copy some text, so I've added this code
        if (this.isFocused()) {
            this.clearFocus();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // the base TextView class checks if getAutoLinkMask != 0, so I added a similar code for == 0
        if (CompatibilityUtils.isTextSelectable(this)
                && this.getText() instanceof Spannable
                && this.getAutoLinkMask() == 0
                && this.getLinksClickable()
                && this.isEnabled()
                && this.getLayout() != null) {
            return this.checkLinksOnTouch(event);
        }

        return super.onTouchEvent(event);
    }

    private boolean checkLinksOnTouch(MotionEvent event) {
        this.copyBaseEditorIfNecessary();

        int action = event.getAction() & 0xff; // getActionMasked()
        if (action == MotionEvent.ACTION_DOWN) {
            lastClickTime = System.currentTimeMillis();
        }
        boolean discardNextActionUp = this.getDiscardNextActionUp();

        // call the base method anyway
        final boolean superResult = super.onTouchEvent(event);

        // the same check as in the super.onTouchEvent(event)
        if (discardNextActionUp && action == MotionEvent.ACTION_UP) {
            return superResult;
        }

        final boolean touchIsFinished = (action == MotionEvent.ACTION_UP) && !this.getIgnoreActionUpEvent() && isFocused();

        // Copied from the LinkMovementMethod class
        if (touchIsFinished) {

            Spannable spannable = (Spannable) this.getText();
            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= this.getTotalPaddingLeft();
            y -= this.getTotalPaddingTop();

            x += this.getScrollX();
            y += this.getScrollY();

            Layout layout = this.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            ClickableSpan[] link = spannable.getSpans(off, off, ClickableSpan.class);

            if (link.length != 0) {
                if ((System.currentTimeMillis() - lastClickTime) < CLICK_DELAY) {
                    link[0].onClick(this);
                    return true;
                }
            }
        }

        return superResult;
    }

    private void copyBaseEditorIfNecessary() {
        if (this.mBaseEditorCopied) {
            return;
        }

        try {
            Field field = TextView.class.getDeclaredField("mEditor");
            field.setAccessible(true);
            this.mBaseEditor = field.get(this);

            if (this.mBaseEditor != null) {
                Class editorClass = this.mBaseEditor.getClass();
                this.mDiscardNextActionUpField = editorClass.getDeclaredField("mDiscardNextActionUp");
                this.mDiscardNextActionUpField.setAccessible(true);

                this.mIgnoreActionUpEventField = editorClass.getDeclaredField("mIgnoreActionUpEvent");
                this.mIgnoreActionUpEventField.setAccessible(true);
            }

        } catch (Exception e) {
//            MyLog.e(TAG, e);
        } finally {
            this.mBaseEditorCopied = true;
        }
    }

    private boolean getDiscardNextActionUp() {
        if (this.mBaseEditor == null) {
            return false;
        }

        try {
            return this.mDiscardNextActionUpField.getBoolean(this.mBaseEditor);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean getIgnoreActionUpEvent() {
        if (this.mBaseEditor == null) {
            return false;
        }

        try {
            return this.mIgnoreActionUpEventField.getBoolean(this.mBaseEditor);
        } catch (Exception e) {
            return false;
        }
    }
}