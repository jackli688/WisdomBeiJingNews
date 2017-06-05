package www.learn.jackli.baidu.com.wisdombeijingnews.pagers.menu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import okhttp3.Call;
import www.learn.jackli.baidu.com.wisdombeijingnews.MyApplication;
import www.learn.jackli.baidu.com.wisdombeijingnews.R;
import www.learn.jackli.baidu.com.wisdombeijingnews.activity.NewsDetailUI;
import www.learn.jackli.baidu.com.wisdombeijingnews.base.pager.MenuBasePager;
import www.learn.jackli.baidu.com.wisdombeijingnews.beans.New;
import www.learn.jackli.baidu.com.wisdombeijingnews.beans.NewsMenuBeanDetails;
import www.learn.jackli.baidu.com.wisdombeijingnews.beans.TabDetailBean;
import www.learn.jackli.baidu.com.wisdombeijingnews.beans.TopicNew;
import www.learn.jackli.baidu.com.wisdombeijingnews.constants.Urls;
import www.learn.jackli.baidu.com.wisdombeijingnews.utils.DensityUtils;
import www.learn.jackli.baidu.com.wisdombeijingnews.utils.LogUtil;
import www.learn.jackli.baidu.com.wisdombeijingnews.utils.SPUtils;
import www.learn.jackli.baidu.com.wisdombeijingnews.utils.ToastUtil;
import www.learn.jackli.baidu.com.wisdombeijingnews.utils.http.okhttp.OkHttpUtils;
import www.learn.jackli.baidu.com.wisdombeijingnews.utils.http.okhttp.callback.StringCallback;
import www.learn.jackli.baidu.com.wisdombeijingnews.view.HorizontalScrollViewPager;
import www.learn.jackli.baidu.com.wisdombeijingnews.view.RefreshListView;

/**
 * Created by jackli on 2017/5/19.
 */

public class TabDetailPager extends MenuBasePager {
    //    @BindView(R.id.lv_tabdetail_news)
    private RefreshListView lvTabdetailNews;
    //    @BindView(R.id.vp_tabdetail_topimage)
    private HorizontalScrollViewPager vpTabdetailTopimage;
    //    @BindView(R.id.tv_tabdetail_info)
    private TextView tvTabdetailInfo;
    //    @BindView(R.id.ll_tabdetail_points)
    private LinearLayout llTabdetailPoints;
    private NewsMenuBeanDetails mData;
    private String cacheJson = "";
    private List<TopicNew> topnews;
    private int preRedPointIndex;
    private List<New> newsData;
    private boolean isRefreshing, isLoadMore;
    private String moreUrl;
    private NewsAdapter newsAdapter;
    private String CACHE_READ_NEWSID = "cache_read_newsid";// 保存已读新闻id
    private Handler mHandler;
    private final int DELAYEDTIME = 3000;


    public TabDetailPager(Context context, NewsMenuBeanDetails newsMenuBeanDetails) {
        super(context);
        this.mData = newsMenuBeanDetails;
    }

    @Override
    protected View initView() {
        View view = View.inflate(mContext, R.layout.tabdetail, null);
        lvTabdetailNews = (RefreshListView) view.findViewById(R.id.lv_tabdetail_news);
//        ButterKnife.bind(view);

        //加载顶部轮播图布局
        View topnews = View.inflate(mContext, R.layout.topnews, null);
        vpTabdetailTopimage = (HorizontalScrollViewPager) topnews.findViewById(R.id.vp_tabdetail_topimage);
        tvTabdetailInfo = (TextView) topnews.findViewById(R.id.tv_tabdetail_info);
        llTabdetailPoints = (LinearLayout) topnews.findViewById(R.id.ll_tabdetail_points);
//        ButterKnife.bind(topnews);
        //把顶部轮播图添加到ListView的头上
        lvTabdetailNews.addHeaderView(topnews);
        lvTabdetailNews.setOnItemClickListener(new TabDetailPagerListViewListener());
        //给自己的下拉刷新listview设置监听
        lvTabdetailNews.setOnNewTabRefreshListener(new RefreshListView.OnNewTabRefreshListener() {
            @Override
            public void onRefreshing() {
                isRefreshing = true;
                //真正的刷新业务
                getDataFromServer();
            }

            @Override
            public void onLoadMore() {
                //判断moreUrl是否为空，如果为空是没有更多数据
                if (!TextUtils.isEmpty(moreUrl)) {
                    //为了访问网络解析json时。
                    isLoadMore = true;
                    //加载更多业务
                    getMoreDataFromServer();
                } else {
                    //恢复加载更多界面状态
                    lvTabdetailNews.loadMoreFinished();
                    ToastUtil.show(mContext, "没有更多数据了，亲", Toast.LENGTH_SHORT);
                }
            }
        });
        return view;
    }

    private void getMoreDataFromServer() {
        OkHttpUtils
                .get()
                .url(Urls.SERVER_URL + moreUrl)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                //加载更多完成后，需要把isloadMore再置为false
                isLoadMore = false;
                lvTabdetailNews.loadMoreFinished();
            }

            @Override
            public void onResponse(String response, int id) {
                parseJson(response.toString());
                //加载更多完成后，需要把isLoadMore再置为false
                isLoadMore = false;
                lvTabdetailNews.loadMoreFinished();
            }
        });

    }

    @Override
    public void initData() {
        //访问网络前，先获取缓存
        String contentPath = mData.contentPath;
        cacheJson = (String) SPUtils.get(MyApplication.getContext(), contentPath, cacheJson);
        if (!TextUtils.isEmpty(cacheJson)) {
            //如果有缓存，那就拿着缓存展示界面
            parseJson(cacheJson);
            LogUtil.i("网页的缓存是:" + cacheJson);
        }
        //访问页签详情数据
        getDataFromServer();
    }

    private void getDataFromServer() {
        OkHttpUtils
                .get()
                .url(Urls.SERVER_URL + mData.contentPath)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                doRefreshStep();
            }

            @Override
            public void onResponse(String response, int id) {
//                {"retcode":200,"data":[{"id":10000,"title":"新闻","type":1,"children":[{"id":10007,"title":"北京","type":1,"url":"/10007/list_1.json"},{"id":10006,"title":"中国","type":1,"url":"/10006/list_1.json"},{"id":10008,"title":"国际","type":1,"url":"/10008/list_1.json"},{"id":10010,"title":"体育","type":1,"url":"/10010/list_1.json"},{"id":10091,"title":"生活","type":1,"url":"/10091/list_1.json"},{"id":10012,"title":"旅游","type":1,"url":"/10012/list_1.json"},{"id":10095,"title":"科技","type":1,"url":"/10095/list_1.json"},{"id":10009,"title":"军事","type":1,"url":"/10009/list_1.json"},{"id":10093,"title":"时尚","type":1,"url":"/10093/list_1.json"},{"id":10011,"title":"财经","type":1,"url":"/10011/list_1.json"},{"id":10094,"title":"育儿","type":1,"url":"/10094/list_1.json"},{"id":10105,"title":"汽车","type":1,"url":"/10105/list_1.json"}]},{"id":10002,"title":"专题","type":10,"url":"/10006/list_1.json","url1":"/10007/list1_1.json"},{"id":10003,"title":"组图","type":2,"url":"/10008/list_1.json"},{"id":10004,"title":"互动","type":3,"excurl":"","dayurl":"","weekurl":""}],"extend":[10007,10006,10008,10014,10012,10091,10009,10010,10095]}
                LogUtil.i("网络请求回调所在线程:" + Thread.currentThread().getName());
                LogUtil.d("当前新闻详细页请求的数据:" + response.toString());
                cacheJson = response.toString();
                LogUtil.d("TabDetailPager缓存的数据是:" + cacheJson);
                //页签详情缓存数据
                SPUtils.put(MyApplication.getContext(), mData.contentPath, cacheJson);
                parseJson(cacheJson);
                doRefreshStep();
            }
        });
    }

    private void doRefreshStep() {
        if (isRefreshing) {
            lvTabdetailNews.refreshFinished(isRefreshing);
            isRefreshing = false;
        }
    }

    private void parseJson(String cacheJson) {
        Gson gson = new Gson();
        TabDetailBean tabDetailBean = null;
        try {
            tabDetailBean = gson.fromJson(cacheJson, TabDetailBean.class);
        } catch (Exception e) {
            LogUtil.d(e.getMessage());
        }
        //获取加载更多应用url
        moreUrl = tabDetailBean.data.more;
        if (!isLoadMore) {
            //展示轮播图
            topnews = tabDetailBean.data.topnews;
            vpTabdetailTopimage.setAdapter(new TopNewsAdapter());
            //监听轮播图控件，根据滑动的位置更新图片描述
            vpTabdetailTopimage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    //把当前一个红点变成白色
                    llTabdetailPoints.getChildAt(preRedPointIndex).setEnabled(false);
//                    当轮播图滑动时，根据滑动的位置更新图片描述
                    tvTabdetailInfo.setText(topnews.get(position).title);
                    //当轮播图滑动时，把相应位置的点变成红色
                    llTabdetailPoints.getChildAt(position).setEnabled(true);
                    preRedPointIndex = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            //初始化轮播图第一页的图片描述
            tvTabdetailInfo.setText(topnews.get(0).title);
            //根据轮播图的数量，创建点的指示器并且清除之前的点
            llTabdetailPoints.removeAllViews();
            for (int i = 0; i < topnews.size(); i++) {
                ImageView imageView = new ImageView(mContext);
                imageView.setBackgroundResource(R.drawable.tabdetail_point_selector);
                int width = DensityUtils.dp2px(MyApplication.getContext(), 5);
                int height = width;
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
                params.leftMargin = 2 * width;
                imageView.setLayoutParams(params);
                imageView.setEnabled(false);
                //把创建的点添加到容器中
                llTabdetailPoints.addView(imageView);
            }
            //初始化轮播图中的第一个红点
            llTabdetailPoints.getChildAt(0).setEnabled(true);
            //当重新展示某一个页签详情界面时，重新把第一个红点位置归零
            preRedPointIndex = 0;
            //更新新闻列表
            newsData = tabDetailBean.data.news;
            newsAdapter = new NewsAdapter();
            lvTabdetailNews.setAdapter(newsAdapter);
        } else {
            newsData.addAll(tabDetailBean.data.news);
            if (newsAdapter != null) {
                newsAdapter.notifyDataSetChanged();
            }
        }
        //将数据展示在界面上之后，准备自动轮播
        if (mHandler == null) {
            mHandler = new MyHandler();
        }
        //清除之前的消息
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendMessageDelayed(Message.obtain(), DELAYEDTIME);
    }

    private class TopNewsAdapter extends PagerAdapter {


        @Override
        public int getCount() {
            return topnews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(mContext);
            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            //删除消息，实现停止轮播
                            mHandler.removeCallbacksAndMessages(null);
                            break;
                        case MotionEvent.ACTION_UP:
                            mHandler.sendMessageDelayed(Message.obtain(), DELAYEDTIME);
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            mHandler.sendMessageDelayed(Message.obtain(), DELAYEDTIME);
                            break;
                    }
                    return true;
                }
            });
            container.addView(imageView);
            //展示图片
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            String topimage = topnews.get(position).topimage;
            LogUtil.d(topimage);
            topimage = topimage.replace("http://10.0.2.2:8080/zhbj", Urls.SERVER_URL);
            Picasso.with(MyApplication.getContext()).load(topimage).into(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private class NewsAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return newsData.size();
        }

        @Override
        public Object getItem(int position) {
            return newsData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            NewsHolder holder = null;
            New mNewBean = newsData.get(position);
            LogUtil.i(mNewBean.toString());
            if (convertView == null) {
                holder = new NewsHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.tabdetail_newsitem, parent, false);
                holder.img = (ImageView) convertView.findViewById(R.id.iv_newsitem_img);
                holder.title = (TextView) convertView.findViewById(R.id.tv_newsitem_title);
                holder.time = (TextView) convertView.findViewById(R.id.tv_newsitem_time);
                convertView.setTag(holder);
            } else {
                holder = (NewsHolder) convertView.getTag();
            }
            //获取保存的已读新闻id，根据当前展示的新闻id判断是否需要变换颜色
            String cacheIds = (String) SPUtils.get(MyApplication.getContext(), CACHE_READ_NEWSID, "");
            if (cacheIds.contains(String.valueOf(mNewBean.id))) {
                holder.title.setTextColor(Color.RED);
            } else {
                holder.title.setTextColor(Color.BLACK);
            }
            //http://10.0.2.2:8080/zhbj/10007/2078369924F9UO.jpg
            String listimage = mNewBean.listimage;
            listimage = listimage.replace("http://10.0.2.2:8080/zhbj", Urls.SERVER_URL);
            Picasso.with(MyApplication.getContext())
                    .load(listimage)
                    .into(holder.img);
            holder.title.setText(mNewBean.title);
            holder.time.setText(mNewBean.pubdate);
            return convertView;
        }
    }

    static class NewsHolder {
        public ImageView img;
        public TextView title;
        public TextView time;
    }

    class TabDetailPagerListViewListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //由于添加了两个头,所以实际条目的位置realPosition = position -2;
            int realPosition = position - 2;
            New news = newsData.get(realPosition);
            //保存新闻的id
            //取出之前保存的新闻id
            String cacheIds = (String) SPUtils.get(MyApplication.getContext(), CACHE_READ_NEWSID, "");
            String newsId = String.valueOf(news.id);
            StringBuffer sbf = new StringBuffer("");
            if (!cacheIds.contains(newsId)) {
                sbf.append(cacheIds);
                sbf.append(",");
                sbf.append(newsId);
                SPUtils.put(MyApplication.getContext(), CACHE_READ_NEWSID, sbf.toString());
                //通知listview刷新数据
                if (newsAdapter != null) {
                    newsAdapter.notifyDataSetChanged();
                }
            }
            //打开新闻详情界面
            Intent intent = new Intent(MyApplication.getContext(), NewsDetailUI.class);
            String url = news.url;
            url = url.replace("http://10.0.2.2:8080/zhbj", Urls.SERVER_URL);
            intent.putExtra("url", url);
            mContext.startActivity(intent);
        }
    }


    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            //判断当前ViewPager是否存在界面上，如果不显示就不在发送消息了
            if (vpTabdetailTopimage.getWindowVisibility() == View.GONE) {
                mHandler.removeCallbacksAndMessages(null);
                return;
            }
//            切换到最后一页时，重新到第一页，需要把当前的(vpTabdetailTopimage.getCurrentItem() + 1)和topnews.size()取模。
//            收到3s后的延迟消息，切换轮播图4 -> 0 5->1
            int nextIndex = (vpTabdetailTopimage.getCurrentItem() + 1) % topnews.size();
//            切到第一页时，不要切换动画
            if (nextIndex == 0) {
                vpTabdetailTopimage.setCurrentItem(nextIndex, false);
            } else {
                vpTabdetailTopimage.setCurrentItem(nextIndex, true);
            }
            //再次发送消息，实现无限循环
            mHandler.sendMessageDelayed(Message.obtain(), DELAYEDTIME);
            LogUtil.d("当前的mHandler的HashCode:"+mHandler.hashCode());
        }
    }
}
