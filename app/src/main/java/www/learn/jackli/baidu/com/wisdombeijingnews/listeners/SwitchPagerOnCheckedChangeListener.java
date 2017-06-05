package www.learn.jackli.baidu.com.wisdombeijingnews.listeners;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.support.v4.view.ViewPager;
import android.widget.RadioGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import www.learn.jackli.baidu.com.wisdombeijingnews.R;
import www.learn.jackli.baidu.com.wisdombeijingnews.activity.HomeActivity;

/**
 * Created by jackli on 2017/5/12.
 * 底部按钮的监听事件，用来切换上面viewPager对应的界面
 */

public class SwitchPagerOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

    private ViewPager vpContentPagers;
    private Activity mActivity;

    public SwitchPagerOnCheckedChangeListener(ViewPager vpContentPagers, Activity mActivity) {
        this.vpContentPagers = vpContentPagers;
        this.mActivity = mActivity;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.rb_content_home:
                vpContentPagers.setCurrentItem(0, false);
                enableSlidingMenu(false);
                break;
            case R.id.rb_content_newscenter:
                vpContentPagers.setCurrentItem(1, false);
                enableSlidingMenu(true);
                break;
            case R.id.rb_content_smartservice:
                vpContentPagers.setCurrentItem(2, false);
                enableSlidingMenu(true);
                break;
            case R.id.rb_content_govaffairs:
                vpContentPagers.setCurrentItem(3, false);
                enableSlidingMenu(true);
                break;
            case R.id.rb_content_setting:
                vpContentPagers.setCurrentItem(4, false);
                enableSlidingMenu(false);
                break;
            default:
                break;
        }
    }

    private void enableSlidingMenu( boolean enable ) {
        HomeActivity mHomeActivity = (HomeActivity) mActivity;
        SlidingMenu slidingMenu = mHomeActivity.getSlidingMenu();
        if (enable) {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        } else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }


}
