package www.learn.jackli.baidu.com.wisdombeijingnews.pagers;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import www.learn.jackli.baidu.com.wisdombeijingnews.base.pager.BasePager;


public class HomePager extends BasePager {
	
	public HomePager(Context context) {
		super(context);
	}
	
	@Override
	public void initData() {
		System.out.println("首页加载数据了");
		tvBasepagerTitle.setText("首页");
		ibBasepagerMenu.setVisibility(View.GONE);
		
		TextView textView = new TextView(mContext);
		textView.setText("首页");
		textView.setGravity(Gravity.CENTER);
		flBasepagerContainer.removeAllViews();
		flBasepagerContainer.addView(textView);
	}

}
