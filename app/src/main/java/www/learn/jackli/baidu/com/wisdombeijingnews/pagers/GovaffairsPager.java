package www.learn.jackli.baidu.com.wisdombeijingnews.pagers;


import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

import www.learn.jackli.baidu.com.wisdombeijingnews.base.pager.BasePager;

public class GovaffairsPager extends BasePager {

	public GovaffairsPager(Context context) {
		super(context);
	}

	
	@Override
	public void initData() {
		System.out.println("政务加载数据了");
		tvBasepagerTitle.setText("政务");
		
		TextView textView = new TextView(mContext);
		textView.setText("政务");
		textView.setGravity(Gravity.CENTER);
		flBasepagerContainer.removeAllViews();
		flBasepagerContainer.addView(textView);
	}

}
