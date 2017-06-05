package www.learn.jackli.baidu.com.wisdombeijingnews.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import www.learn.jackli.baidu.com.wisdombeijingnews.MyApplication;
import www.learn.jackli.baidu.com.wisdombeijingnews.R;
import www.learn.jackli.baidu.com.wisdombeijingnews.activity.HomeActivity;
import www.learn.jackli.baidu.com.wisdombeijingnews.base.fragment.BaseFragment;
import www.learn.jackli.baidu.com.wisdombeijingnews.beans.NewsMenuBean;
import www.learn.jackli.baidu.com.wisdombeijingnews.pagers.NewscenterPager;
import www.learn.jackli.baidu.com.wisdombeijingnews.utils.DensityUtils;
import www.learn.jackli.baidu.com.wisdombeijingnews.utils.LogUtil;

/**
 * Created by jackli on 2017/5/11.
 */

public class LeftFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    private List<NewsMenuBean> mData;
    private int currentIndex;// 当前点击的条目位置
    private NewsLeftMenuListAdapter newsLeftMenuListAdapter;
    private ListView listView;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        listView = new ListView(MyApplication.getContext());
//         设置listview的背景
        listView.setBackgroundColor(Color.BLACK);
//        去掉listview条目选中效果
        listView.setSelection(android.R.color.transparent);
//        去掉listview中的分割线
        listView.setDivider(null);
//        把listview的内容往下移动
        listView.setPadding(0, DensityUtils.dp2px(MyApplication.getContext(), 15), 0, 0);
//        监听listview的条目点击事件
        listView.setOnItemClickListener(this);
        return listView;
    }

    public void setMenuData(List<NewsMenuBean> data) {
        this.mData = data;
        LogUtil.d("左侧菜单栏的数据："+data.toString());
        newsLeftMenuListAdapter = new NewsLeftMenuListAdapter(MyApplication.getContext(), R.layout.left_menu_item, mData);
        listView.setAdapter(newsLeftMenuListAdapter);
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        currentIndex = position;
        if (newsLeftMenuListAdapter!=null){
            newsLeftMenuListAdapter.notifyDataSetChanged();
        }
        //让左侧菜单自动关闭
        HomeActivity mHomeUI = (HomeActivity) mActivity;
        mHomeUI.getSlidingMenu().toggle();
        //获取新闻中心对象
        NewscenterPager newscenterPager = mHomeUI.getContentFragment().getNewscenterPager();
        //根据左侧菜单点击的位置更新新闻中心的界面。
        newscenterPager.switchPager(position);

    }
    private class NewsLeftMenuListAdapter extends ArrayAdapter<NewsMenuBean> {
        private int resourceId;

        public NewsLeftMenuListAdapter(@NonNull Context context, @LayoutRes int resource, List<NewsMenuBean> objects) {
            super(context, resource, objects);
            this.resourceId = resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            NewsMenuBean item = getItem(position);
            TextView view = (TextView) LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            view.setText(item.parentTitle);
            view.setEnabled(position == currentIndex);
            return view;
        }
    }
}
