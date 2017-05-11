package www.learn.jackli.baidu.com.wisdombeijingnews.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import www.learn.jackli.baidu.com.wisdombeijingnews.Constant;
import www.learn.jackli.baidu.com.wisdombeijingnews.MyApplication;
import www.learn.jackli.baidu.com.wisdombeijingnews.R;
import www.learn.jackli.baidu.com.wisdombeijingnews.model.SystemBarTintManager;
import www.learn.jackli.baidu.com.wisdombeijingnews.utils.DensityUtils;
import www.learn.jackli.baidu.com.wisdombeijingnews.utils.LogUtil;
import www.learn.jackli.baidu.com.wisdombeijingnews.utils.SPUtils;

/**
 * Created by jackli on 2017/5/10.
 */

public class GuideUI extends AppCompatActivity {
    public int color = R.color.transparent;
    @BindView(R.id.vp_guide_bg)
    ViewPager vpGuideBg;
    @BindView(R.id.iv_guide_redPoint)
    ImageView ivGuideRedPoint;
    @BindView(R.id.ll_guide_points)
    LinearLayout llGuidePoints;
    @BindView(R.id.bt_guide_start)
    Button btGuideStart;
    private ArrayList<ImageView> imgs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 请求去掉标题,必须写在setContentView之前
        initSystemBar(this);
        setContentView(R.layout.guide);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        //将图片转换成ImageView提供给Adapter
        int[] imgIds = new int[]{R.mipmap.guide_1, R.mipmap.guide_2, R.mipmap.guide_3};
        imgs = new ArrayList<ImageView>();
        for (int i = 0; i < imgIds.length; i++) {
            ImageView imageView = new ImageView(MyApplication.getContext());
            //设置ImageButoon的缩放类型，让图片填充ImageView
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageResource(imgIds[i]);
            imgs.add(imageView);
            //根据导航加载的图片有几张，就创建几个灰点。
            ImageView point = new ImageView(MyApplication.getContext());
            point.setBackgroundResource(R.drawable.guide_point_normal);
            //设置宽高
            int width = DensityUtils.dp2px(MyApplication.getContext(), 10.0f);
            int height = width;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
            point.setLayoutParams(layoutParams);
            //设置左边距
            if (i != 0) {
                layoutParams.leftMargin = width;
            }
            //把灰点添加到引导界面底部
            llGuidePoints.addView(point);
        }
        //设置Adapter
        vpGuideBg.setAdapter(new MyAdapter());
        //监听ViewPager获取手指一动的距离
        vpGuideBg.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //当ViewPage华东的时候调用
                LogUtil.i("position:" + position + ":positionOffset:"
                        + positionOffset + ":positionOffsetPixels:"
                        + positionOffsetPixels);
                /**
                 * 计算红点移动的距离
                 * 红点移动的距离/灰点的间距
                 * =  手指移动的距离/屏幕的宽度
                 * 红点移动的距离 = 手指移动的距离/屏幕宽度*灰点的距离
                 * 红点移动距离 = 手指移动比例 * 灰点的间距
                 */
                int redPointX = (int) ((position + positionOffset) * DensityUtils.dp2px(MyApplication.getContext(), 20));
                //移动红点不停的设置红点的左边距实现红点移动
                android.widget.RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivGuideRedPoint.getLayoutParams();
                layoutParams.leftMargin = redPointX;
                ivGuideRedPoint.setLayoutParams(layoutParams);
            }

            @Override
            public void onPageSelected(int position) {
                //当viewpage滑动到最后一页的时候，吧开始按钮显示出来。
                if (position == imgs.size() - 1) {
                    btGuideStart.setVisibility(View.VISIBLE);
                } else {
                    btGuideStart.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    /**
     * 设置状态栏颜色
     *
     * @param activity
     */
    private void initSystemBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(activity, true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(color);
    }

    private static void setTranslucentStatus(Activity activity, boolean on) {

        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @OnClick(R.id.bt_guide_start)
    public void onViewClicked() {
        SPUtils.put(MyApplication.getContext(), Constant.IS_APP_FIRST_OPEN,false);
        startActivity(new Intent(MyApplication.getContext(), MainUI.class));
        finish();
    }

    private class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imgs.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = imgs.get(position);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
