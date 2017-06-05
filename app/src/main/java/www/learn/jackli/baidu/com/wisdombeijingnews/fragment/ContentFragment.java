package www.learn.jackli.baidu.com.wisdombeijingnews.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import www.learn.jackli.baidu.com.wisdombeijingnews.R;
import www.learn.jackli.baidu.com.wisdombeijingnews.adapter.TabAdapter;
import www.learn.jackli.baidu.com.wisdombeijingnews.base.fragment.BaseFragment;
import www.learn.jackli.baidu.com.wisdombeijingnews.base.pager.BasePager;
import www.learn.jackli.baidu.com.wisdombeijingnews.listeners.SwitchPagerOnCheckedChangeListener;
import www.learn.jackli.baidu.com.wisdombeijingnews.pagers.GovaffairsPager;
import www.learn.jackli.baidu.com.wisdombeijingnews.pagers.HomePager;
import www.learn.jackli.baidu.com.wisdombeijingnews.pagers.NewscenterPager;
import www.learn.jackli.baidu.com.wisdombeijingnews.pagers.SettingPager;
import www.learn.jackli.baidu.com.wisdombeijingnews.pagers.SmartservicePager;
import www.learn.jackli.baidu.com.wisdombeijingnews.view.NoScrollViewPager;
import static www.learn.jackli.baidu.com.wisdombeijingnews.R.id.rb_content_newscenter;

/**
 * Created by jackli on 2017/5/11.
 */

public class ContentFragment extends BaseFragment {

    @BindView(R.id.vp_content_pagers)
    NoScrollViewPager vpContentPagers;
    @BindView(R.id.rg_content_bottom)
    RadioGroup rgContentBottom;
    @BindView(R.id.rb_content_home)
    RadioButton rbContentHome;
    @BindView(rb_content_newscenter)
    RadioButton rbContentNewscenter;
    @BindView(R.id.rb_content_smartservice)
    RadioButton rbContentSmartservice;
    @BindView(R.id.rb_content_govaffairs)
    RadioButton rbContentGovaffairs;
    @BindView(R.id.rb_content_setting)
    RadioButton rbContentSetting;
    private List<BasePager> pagers;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_fragment, container, false);
        return view;
    }

    @Override
    protected void initData() {
        //初始化数据
        // 更新ViewPager
        // 设置数据
        pagers = new ArrayList<BasePager>();
        pagers.add(new HomePager(mActivity));
        pagers.add(new NewscenterPager(mActivity));
        pagers.add(new SmartservicePager(mActivity));
        pagers.add(new GovaffairsPager(mActivity));
        pagers.add(new SettingPager(mActivity));
        pagers.get(0).initData();
        vpContentPagers.setAdapter(new TabAdapter(pagers));
        vpContentPagers.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //选中某一页数据时，才加载数据
                BasePager basePager = pagers.get(position);
                basePager.initData();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        //初始化加载首页数据

        //ViewPager关联RadioGroup
        rgContentBottom.setOnCheckedChangeListener(new SwitchPagerOnCheckedChangeListener(vpContentPagers,mActivity));
        //默认让底部单选按钮选中首页
        rgContentBottom.check(R.id.rb_content_home);
    }

    public NewscenterPager getNewscenterPager(){
        return (NewscenterPager) pagers.get(1);
    }

}
