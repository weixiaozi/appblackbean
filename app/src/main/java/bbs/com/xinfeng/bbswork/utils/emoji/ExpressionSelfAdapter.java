/**

 */
package bbs.com.xinfeng.bbswork.utils.emoji;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sj.emoji.EmojiBean;

import java.util.List;

import bbs.com.xinfeng.bbswork.R;

public class ExpressionSelfAdapter extends BaseAdapter {
    private Context context;
    private List<EmojiBean> list;

    public ExpressionSelfAdapter(Context context, List<EmojiBean> list) {
        this.context = context;
        this.list = list;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_expressionself_adpter, null);
        }

        TextView imageView = (TextView) convertView.findViewById(R.id.img_expression_self);
        imageView.setText(list.get(position).emoji);

        return convertView;
    }
}
