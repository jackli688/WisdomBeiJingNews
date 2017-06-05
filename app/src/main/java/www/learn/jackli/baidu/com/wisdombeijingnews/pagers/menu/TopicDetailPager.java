package www.learn.jackli.baidu.com.wisdombeijingnews.pagers.menu;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import www.learn.jackli.baidu.com.wisdombeijingnews.base.pager.MenuBasePager;

/**
 * Created by jackli on 2017/5/16.
 */

public class TopicDetailPager extends MenuBasePager {
    public TopicDetailPager(Context context) {
        super(context);
    }

    @Override
    protected View initView() {
        TextView textView = new TextView(mContext);
        textView.setText("专题详情");
        return textView;
    }
}
