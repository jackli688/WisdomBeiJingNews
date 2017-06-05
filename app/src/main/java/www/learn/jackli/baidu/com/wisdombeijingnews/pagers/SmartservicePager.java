package www.learn.jackli.baidu.com.wisdombeijingnews.pagers;


import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

import www.learn.jackli.baidu.com.wisdombeijingnews.base.pager.BasePager;

public class SmartservicePager extends BasePager {

    public SmartservicePager(Context context) {
        super(context);
    }


    @Override
    public void initData() {
        System.out.println("智慧服务加载数据了");
        tvBasepagerTitle.setText("智慧服务");

        TextView textView = new TextView(mContext);
        textView.setText("智慧服务");
        textView.setGravity(Gravity.CENTER);
        flBasepagerContainer.removeAllViews();
        flBasepagerContainer.addView(textView);
    }
}
