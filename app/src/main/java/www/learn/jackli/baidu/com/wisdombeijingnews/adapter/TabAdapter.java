package www.learn.jackli.baidu.com.wisdombeijingnews.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import www.learn.jackli.baidu.com.wisdombeijingnews.base.pager.BasePager;

/**
 * Created by jackli on 2017/5/12.
 * 主界面五个按钮对应的Pager页面
 */

public class TabAdapter extends PagerAdapter implements View.OnClickListener {
    private List<BasePager> pagers;

    public TabAdapter(List<BasePager> pagers) {
        this.pagers = pagers;
    }


    @Override
    public int getCount() {
        return pagers == null ? 0 : pagers.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        BasePager basePager = pagers.get(position);
        View rootView = basePager.getRootView();
        container.addView(rootView);
        //防止预加载数据，浪费流量
//        basePager.initData();
        return rootView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public void onClick(View v) {

    }


}
