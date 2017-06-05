package www.learn.jackli.baidu.com.wisdombeijingnews.pagers;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import www.learn.jackli.baidu.com.wisdombeijingnews.MyApplication;
import www.learn.jackli.baidu.com.wisdombeijingnews.activity.HomeActivity;
import www.learn.jackli.baidu.com.wisdombeijingnews.base.pager.BasePager;
import www.learn.jackli.baidu.com.wisdombeijingnews.base.pager.MenuBasePager;
import www.learn.jackli.baidu.com.wisdombeijingnews.beans.NewCenterBean;
import www.learn.jackli.baidu.com.wisdombeijingnews.beans.NewsMenuBeanDetails;
import www.learn.jackli.baidu.com.wisdombeijingnews.constants.Urls;
import www.learn.jackli.baidu.com.wisdombeijingnews.fragment.LeftFragment;
import www.learn.jackli.baidu.com.wisdombeijingnews.pagers.menu.InterractDetailPager;
import www.learn.jackli.baidu.com.wisdombeijingnews.pagers.menu.NewsDetailPager;
import www.learn.jackli.baidu.com.wisdombeijingnews.pagers.menu.PhotoDetailPager;
import www.learn.jackli.baidu.com.wisdombeijingnews.pagers.menu.TopicDetailPager;
import www.learn.jackli.baidu.com.wisdombeijingnews.utils.LogUtil;
import www.learn.jackli.baidu.com.wisdombeijingnews.utils.SPUtils;
import www.learn.jackli.baidu.com.wisdombeijingnews.utils.http.okhttp.OkHttpUtils;
import www.learn.jackli.baidu.com.wisdombeijingnews.utils.http.okhttp.callback.StringCallback;


public class NewscenterPager extends BasePager {


    private NewCenterBean newCenterBean;
    private ArrayList<MenuBasePager> menuBasePagers = new ArrayList<>();
    private final String NEWSCENTER_CACHE_JSON = "newscenter_cache_json";
    private String cacheJson = "";

    public NewscenterPager(Context context) {
        super(context);
    }



    /**
     * 流程：
     * 1、访问网络前先获取缓存
     * 2、获取到缓存就展示数据
     * 3、访问网络获取数据
     * 3.1、缓存数据
     * 3.2、展示数据
     */
    @Override
    public void initData() {
        LogUtil.i("新闻中心加载数据了");
        tvBasepagerTitle.setText("新闻中心");
        TextView textView = new TextView(mContext);
        textView.setText("新闻中心");
        textView.setGravity(Gravity.CENTER);
        flBasepagerContainer.removeAllViews();
        flBasepagerContainer.addView(textView);
        // 加载新闻中心 数据
        cacheJson = (String) SPUtils.get(MyApplication.getContext(), NEWSCENTER_CACHE_JSON, cacheJson);
        if (!TextUtils.isEmpty(cacheJson)) {
            //有缓存数据，展示数据
            parseJson(cacheJson);
        }
        getDataFromServer();
    }

    private void parseJson(String cacheJson) {
        Gson gson = new Gson();
        try {
            newCenterBean = gson.fromJson(cacheJson, NewCenterBean.class);
        } catch (Exception e) {
            LogUtil.d(e.getMessage());
        }
        LogUtil.d("newCenterBean:" + newCenterBean.toString());
        //获取左侧的菜单控件
        HomeActivity mHomeUI = (HomeActivity) mContext;
        LeftFragment leftFragment = mHomeUI.getLeftFragment();
        //初始化左侧菜单对应的4个对象
        if (newCenterBean.news == null) {
            LogUtil.d("newCenterBean.news == null");
        }
        List<NewsMenuBeanDetails> newsMenuDetails = newCenterBean.news.get(0).newsMenuDetails;
        if (newsMenuDetails == null) {
            LogUtil.d("newsMenuDetails == null");
        }
        menuBasePagers.add(new NewsDetailPager(mContext, newsMenuDetails));
        menuBasePagers.add(new TopicDetailPager(mContext));
        menuBasePagers.add(new PhotoDetailPager((mContext)));
        menuBasePagers.add(new InterractDetailPager(mContext));
        //给左侧菜单攒是数据l
        leftFragment.setMenuData(newCenterBean.news);
        //默认新闻中心展示新闻详细页
        switchPager(0);
    }


    private void getDataFromServer() {
        OkHttpUtils
                .get()
                .url(Urls.NESCENTER_URL)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
//                {"retcode":200,"data":[{"id":10000,"title":"新闻","type":1,"children":[{"id":10007,"title":"北京","type":1,"url":"/10007/list_1.json"},{"id":10006,"title":"中国","type":1,"url":"/10006/list_1.json"},{"id":10008,"title":"国际","type":1,"url":"/10008/list_1.json"},{"id":10010,"title":"体育","type":1,"url":"/10010/list_1.json"},{"id":10091,"title":"生活","type":1,"url":"/10091/list_1.json"},{"id":10012,"title":"旅游","type":1,"url":"/10012/list_1.json"},{"id":10095,"title":"科技","type":1,"url":"/10095/list_1.json"},{"id":10009,"title":"军事","type":1,"url":"/10009/list_1.json"},{"id":10093,"title":"时尚","type":1,"url":"/10093/list_1.json"},{"id":10011,"title":"财经","type":1,"url":"/10011/list_1.json"},{"id":10094,"title":"育儿","type":1,"url":"/10094/list_1.json"},{"id":10105,"title":"汽车","type":1,"url":"/10105/list_1.json"}]},{"id":10002,"title":"专题","type":10,"url":"/10006/list_1.json","url1":"/10007/list1_1.json"},{"id":10003,"title":"组图","type":2,"url":"/10008/list_1.json"},{"id":10004,"title":"互动","type":3,"excurl":"","dayurl":"","weekurl":""}],"extend":[10007,10006,10008,10014,10012,10091,10009,10010,10095]}
                LogUtil.i("网络请求回调所在线程:" + Thread.currentThread().getName());
                LogUtil.d(response.toString());
                cacheJson = response.toString();
                SPUtils.put(MyApplication.getContext(), NEWSCENTER_CACHE_JSON, cacheJson);
                parseJson(cacheJson);
            }
        });
    }

    public void switchPager(int position) {
//        根据左侧擦弹的点击位置更新标题
        tvBasepagerTitle.setText(newCenterBean.news.get(position).parentTitle);
//        根据点击的位置获取对应的菜单对象
        MenuBasePager menuBasePager = menuBasePagers.get(position);
        flBasepagerContainer.removeAllViews();
        //把菜单对象上的布局添加到新闻中的Framelayout
        flBasepagerContainer.addView(menuBasePager.rootView);
        //更新菜单对象的布局
        menuBasePager.initData();
        if (position == 2) {
            ibTitlebarPhototype.setVisibility(View.VISIBLE);
            final PhotoDetailPager photoDetailPager = (PhotoDetailPager) menuBasePagers.get(2);
            ibTitlebarPhototype.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    photoDetailPager.switchPhotoType(ibTitlebarTextsize);
                }
            });
        } else {
            ibTitlebarPhototype.setVisibility(View.GONE);
        }
    }


}
