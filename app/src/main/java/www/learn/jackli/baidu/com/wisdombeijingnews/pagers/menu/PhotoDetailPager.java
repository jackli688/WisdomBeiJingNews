package www.learn.jackli.baidu.com.wisdombeijingnews.pagers.menu;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import okhttp3.Call;
import www.learn.jackli.baidu.com.wisdombeijingnews.MyApplication;
import www.learn.jackli.baidu.com.wisdombeijingnews.R;
import www.learn.jackli.baidu.com.wisdombeijingnews.base.pager.MenuBasePager;
import www.learn.jackli.baidu.com.wisdombeijingnews.beans.New;
import www.learn.jackli.baidu.com.wisdombeijingnews.beans.PhotoBean;
import www.learn.jackli.baidu.com.wisdombeijingnews.constants.Urls;
import www.learn.jackli.baidu.com.wisdombeijingnews.utils.ImageCacheUtil;
import www.learn.jackli.baidu.com.wisdombeijingnews.utils.LogUtil;
import www.learn.jackli.baidu.com.wisdombeijingnews.utils.SPUtils;
import www.learn.jackli.baidu.com.wisdombeijingnews.utils.http.okhttp.OkHttpUtils;
import www.learn.jackli.baidu.com.wisdombeijingnews.utils.http.okhttp.callback.StringCallback;

import static android.view.View.inflate;

/**
 * Created by jackli on 2017/5/16.
 */

public class PhotoDetailPager extends MenuBasePager {
    private boolean isListType = true; //当前展示时ListView
    private ListView lv_photo_list;
    private GridView gv_photo_grid;
    private String cacheJson;
    private List<New> newsData;
    private ImageCacheUtil imageCacheUtil;
    private final String PHOTODETAILPAGERCACHE = "PhotoDetailPagerCache";


    public PhotoDetailPager(Context context) {
        super(context);
        imageCacheUtil = ImageCacheUtil.getImageCacheUtil();

    }

    @Override
    protected View initView() {
        View view = inflate(mContext, R.layout.photo, null);
        lv_photo_list = (ListView) view.findViewById(R.id.lv_photo_list);
        gv_photo_grid = (GridView) view.findViewById(R.id.gv_photo_grid);
        return view;
    }

    @Override
    public void initData() {
        cacheJson = (String) SPUtils.get(MyApplication.getContext(), PHOTODETAILPAGERCACHE, PHOTODETAILPAGERCACHE);
        parseJson(cacheJson);
        getDataFromServer();
    }

    private void getDataFromServer() {
        OkHttpUtils
                .get()
                .url(Urls.PHOTO_URL)
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
                //保存数据
                SPUtils.put(MyApplication.getContext(), PHOTODETAILPAGERCACHE, cacheJson);
                parseJson(cacheJson);
            }
        });
    }

    private void parseJson(String cacheJson) {
        Gson gson = new Gson();
        try {
            PhotoBean photoBean = gson.fromJson(cacheJson, PhotoBean.class);
            newsData = photoBean.data.news;
            PhotoDetailAdapter photoDetailAdapter = new PhotoDetailAdapter();
            lv_photo_list.setAdapter(photoDetailAdapter);
        } catch (Exception e) {
            LogUtil.d(e.getMessage());
        }
    }

    private class PhotoDetailAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            if (newsData != null) {
                return newsData.size();
            } else {
                return 0;
            }
        }

        @Override
        public Object getItem(int position) {
            if (newsData != null) {

                return newsData.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            PhototDetailHolder holder = null;
            if (convertView == null) {
                holder = new PhototDetailHolder();
                convertView = View.inflate(mContext, R.layout.photo_item, null);
                holder.photoView = (ImageView) convertView.findViewById(R.id.iv_photoitem_photo);
                holder.title = (TextView) convertView.findViewById(R.id.tv_photoitem_title);
                convertView.setTag(holder);
            } else {
                holder = (PhototDetailHolder) convertView.getTag();
            }
            if (newsData != null) {
                New mNewBean = newsData.get(position);
                //设置缩放类型
                holder.title.setText(mNewBean.title);
                holder.photoView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                holder.photoView.setTag(position);
                String imageurl = mNewBean.listimage;
                imageurl = imageurl.replace("http://10.0.2.2:8080/zhbj", Urls.SERVER_URL);
                imageCacheUtil.display(holder.photoView, imageurl);
            }
            return convertView;

        }

        class PhototDetailHolder {
            public ImageView photoView;
            public TextView title;
        }
    }


    public void switchPhotoType(ImageButton phototype) {
        if (isListType) {
            //切换到Gridview
            lv_photo_list.setVisibility(View.GONE);
            gv_photo_grid.setVisibility(View.VISIBLE);
            phototype.setBackgroundResource(R.drawable.icon_pic_list_type);
            gv_photo_grid.setAdapter(new PhotoDetailAdapter());
        } else {
            //切换到Listview
            lv_photo_list.setVisibility(View.VISIBLE);
            gv_photo_grid.setVisibility(View.GONE);
            phototype.setBackgroundResource(R.drawable.icon_pic_grid_type);
            lv_photo_list.setAdapter(new PhotoDetailAdapter());
        }
        isListType = !isListType;
    }

}
