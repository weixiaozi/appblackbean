package bbs.com.xinfeng.bbswork.ui.adapter;

import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.domin.MemberAllBean;
import bbs.com.xinfeng.bbswork.domin.TopListInnerBean;
import bbs.com.xinfeng.bbswork.module.GlideApp;
import bbs.com.xinfeng.bbswork.utils.ArmsUtils;
import bbs.com.xinfeng.bbswork.utils.FormatUtils;
import bbs.com.xinfeng.bbswork.utils.ScreenUtils;
import bbs.com.xinfeng.bbswork.utils.SpanUtil;
import bbs.com.xinfeng.bbswork.widget.CircleImageView;

import static bbs.com.xinfeng.bbswork.ui.activity.MembersListActivity.ACTION_IN;

/**
 * Created by dell on 2018/4/10.
 */

public class HomeTopicListAdapter extends BaseQuickAdapter<TopListInnerBean, BaseViewHolder> {
    private int picHeight;

    public HomeTopicListAdapter(@Nullable List<TopListInnerBean> data) {
        super(R.layout.item_home_topic_two, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, TopListInnerBean item) {
        if (picHeight == 0)
            picHeight = (ScreenUtils.getScreenWidth(mContext) - ArmsUtils.dip2px(mContext, 54)) / 2;
        helper.getView(R.id.iv_topic_page).getLayoutParams().width = picHeight;
        helper.getView(R.id.iv_topic_page).getLayoutParams().height = picHeight;
        GlideApp.with(mContext).load(item.getTopic_img()).override(picHeight).placeholder(R.drawable.icon_home_default).error(R.drawable.icon_home_default).into((ImageView) helper.getView(R.id.iv_topic_page));
        helper.setText(R.id.txt_topic_name, item.getTopic_name());
        helper.setText(R.id.txt_topic_members, item.getFans_number() + " 名成员");
        helper.setText(R.id.txt_topic_comments, item.getComments() + " 条发言");
        helper.getView(R.id.txt_creat_byme).setVisibility(item.getIsauthor() == 1 ? View.VISIBLE : View.GONE);
//        helper.setText(R.id.txt_topic_lastcontent, SpanUtil.getExpressionString(mContext, item.getLast_content(), false));
//        helper.setText(R.id.txt_topic_time, FormatUtils.dataToUseData(item.getLast_at()));
        if (item.getUnreadNum() == 0) {
            helper.getView(R.id.txt_topic_mes).setVisibility(View.GONE);
        } else {
            helper.getView(R.id.txt_topic_mes).setVisibility(View.VISIBLE);
//            helper.setText(R.id.txt_topic_mes, item.getUnreadNum() < 100 ? item.getUnreadNum() + "" : "99+");
        }
    }

    public interface onItemOperateListen {
        void operateItem(int position);
    }

    private onItemOperateListen mListen;

    public void setmListen(onItemOperateListen mListen) {
        this.mListen = mListen;
    }
}
