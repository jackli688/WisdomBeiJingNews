package www.learn.jackli.baidu.com.wisdombeijingnews.pagers.menu;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

import www.learn.jackli.baidu.com.wisdombeijingnews.R;
import www.learn.jackli.baidu.com.wisdombeijingnews.activity.HomeActivity;
import www.learn.jackli.baidu.com.wisdombeijingnews.base.pager.MenuBasePager;
import www.learn.jackli.baidu.com.wisdombeijingnews.beans.NewsMenuBeanDetails;

/**
 * Created by jackli on 2017/5/15.
 */

public class NewsDetailPager extends MenuBasePager {

    private TabPageIndicator mIndicator;
    private ViewPager mPager;
    private List<NewsMenuBeanDetails> mData;
    private ArrayList<TabDetailPager> tabDetailPagers;

    public NewsDetailPager(Context context, List<NewsMenuBeanDetails> mData) {
        super(context);
        this.mData = mData;
    }

    @Override
    protected View initView() {
        View view = View.inflate(mContext, R.layout.newsdetail, null);
        mIndicator = (TabPageIndicator) view.findViewById(R.id.news_detail_indicator);
        mPager = (ViewPager) view.findViewById(R.id.news_detail_pager);
        return view;
    }

    @Override
    public void initData() {
        //初始化页签详情界面对象
        tabDetailPagers = new ArrayList<>();
        for (int i = 0; i < mData.size(); i++) {
            NewsMenuBeanDetails newsMenuBeanDetails = mData.get(i);
            tabDetailPagers.add(new TabDetailPager(mContext, newsMenuBeanDetails));
        }
        mPager.setAdapter(new NewsDetailsAdapter());
        mIndicator.setViewPager(mPager);
        //监听ViewPager，让他显示第一页时，才让侧滑菜单处理事件
        mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                HomeActivity homeUI = (HomeActivity) mContext;
                SlidingMenu slidingMenu = homeUI.getSlidingMenu();
                if (position == 0) {
                    slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                } else {
                    slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private class NewsDetailsAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mData.get(position).sonTitle;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabDetailPager tabDetailPager = tabDetailPagers.get(position);
            container.addView(tabDetailPager.rootView);
            tabDetailPager.initData();
            return tabDetailPager.rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

}
