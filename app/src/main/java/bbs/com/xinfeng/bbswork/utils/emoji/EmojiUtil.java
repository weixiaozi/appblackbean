package bbs.com.xinfeng.bbswork.utils.emoji;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;


import com.sj.emoji.DefEmoticons;
import com.sj.emoji.EmojiBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bbs.com.xinfeng.bbswork.R;

/**
 * Created by qiang on 2016/4/22.
 */
public class EmojiUtil {
    private static EmojiUtil emojiUtil;
    private int PER_EMOJI_NUM = 32;//每页要显示的表情数，不包括回退删除键
    private int wordMax = 30;
    private Context context;
    private ViewPager vp_emoji;
    private LinearLayout ll_dot;
    private EditText edtEditText;
    private int orientation = 0;
    private EmojiBean[] emojiList = DefEmoticons.getDefEmojiArray();
    ;

    public static EmojiUtil getInstace() {
        if (emojiUtil == null) {
            emojiUtil = new EmojiUtil();
        }
        return emojiUtil;
    }

    /**
     * 加载自定义表情
     */
    public void setSelfDrawable(Context context, ViewPager vp_emoji, LinearLayout ll_dot, EditText edtEditText, int orientation) {
        this.context = context;
        this.vp_emoji = vp_emoji;
        this.ll_dot = ll_dot;
        this.orientation = orientation;
        this.edtEditText = edtEditText;
        if (orientation == 0) {
            PER_EMOJI_NUM = 32;
        } else {
            PER_EMOJI_NUM = 23;
        }
        final List<View> viewsSelf = new ArrayList<>();
        List<EmojiBean> temojiList = Arrays.asList(emojiList);
        for (int i = 0; i <= emojiList.length / PER_EMOJI_NUM; i++) {
            View v = getGridChildViewSelf(i, temojiList);
            viewsSelf.add(v);
        }
        if (emojiList.length > 0 && emojiList.length % PER_EMOJI_NUM == 0) {
            viewsSelf.remove(viewsSelf.size() - 1);
        }
        vp_emoji.setAdapter(new EmojiPagerAdapter(viewsSelf));
        if (viewsSelf.size() > 1) {
            initDots(viewsSelf.size());
        } else {
            ll_dot.removeAllViews();
        }
        vp_emoji.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                updateTextAndDot(viewsSelf.size());
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 加载每页的表情
     *
     * @param i
     * @param temojiList
     * @return
     */
    private View getGridChildViewSelf(final int i, List<EmojiBean> temojiList) {
        View view = View.inflate(context, R.layout.expression_gridview, null);
        GridView gv = (GridView) view.findViewById(R.id.gridview);
        if (orientation == 0) {
            gv.setNumColumns(8);
        } else {
            gv.setNumColumns(8);
        }
        final List<EmojiBean> list = new ArrayList<>();
        list.clear();
        if (i < temojiList.size() / PER_EMOJI_NUM) {
            list.addAll(temojiList.subList(i * PER_EMOJI_NUM, (i + 1) * PER_EMOJI_NUM));
        } else {
            list.addAll(temojiList.subList(i * PER_EMOJI_NUM, temojiList.size()));
        }
        final ExpressionSelfAdapter expressionSelfAdapter = new ExpressionSelfAdapter(context, list);
        gv.setAdapter(expressionSelfAdapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                edtEditText.getText().insert(edtEditText.getSelectionStart(), list.get(position).emoji);
            }
        });
        return view;
    }


    /**
     * 添加点
     *
     * @param size
     */
    private void initDots(int size) {
        //移除点
        ll_dot.removeAllViews();
        for (int i = 0; i < size; i++) {
            View view = new View(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(12, 12);
            if (i != 0) {
                params.leftMargin = 20;
            }
            view.setLayoutParams(params);
            view.setBackgroundResource(R.drawable.chat_expression_dots);
            ll_dot.addView(view);
        }
    }

    /*
    更新点
     */
    private void updateTextAndDot(int size) {
        int currentPage = vp_emoji.getCurrentItem() % size;
        // 改变dot
        for (int i = 0; i < ll_dot.getChildCount(); i++) {
            ll_dot.getChildAt(i).setEnabled(i == currentPage);
        }
    }

    public void setWordMax(int wordMax) {
        this.wordMax = wordMax;
    }
}
