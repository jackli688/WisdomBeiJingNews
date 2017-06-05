package www.learn.jackli.baidu.com.wisdombeijingnews.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import www.learn.jackli.baidu.com.wisdombeijingnews.constants.Constant;
import www.learn.jackli.baidu.com.wisdombeijingnews.MyApplication;
import www.learn.jackli.baidu.com.wisdombeijingnews.R;
import www.learn.jackli.baidu.com.wisdombeijingnews.fragment.ContentFragment;
import www.learn.jackli.baidu.com.wisdombeijingnews.fragment.LeftFragment;
import www.learn.jackli.baidu.com.wisdombeijingnews.utils.DensityUtils;

/**
 * Created by jackli on 2017/5/10.
 */

public class HomeActivity extends SlidingFragmentActivity {

    private FragmentManager supportFragmentManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //正文
        setContentView(R.layout.activity_home);
        //设置左侧菜单页面
        setBehindContentView(R.layout.left_menu);
        //设置菜单模式为仅使用左侧菜单
        SlidingMenu slidingMenu = getSlidingMenu();
        slidingMenu.setMode(SlidingMenu.LEFT);
        //设置正文界面保留的宽度
        slidingMenu.setBehindOffset(DensityUtils.dp2px(MyApplication.getContext(), 100));
        //获取Fragement管理器
        supportFragmentManager = getSupportFragmentManager();
        //开启事务
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        //替换Fragment
        fragmentTransaction.replace(R.id.ll_main_content, new ContentFragment(), Constant.CONTENT_TAG);
        fragmentTransaction.replace(R.id.ll_main_left, new LeftFragment(), Constant.CONTENT_LEFTTAG);
        // 提交事务
        fragmentTransaction.commit();
    }

    //提供获取左侧菜单对象的方法
     public LeftFragment getLeftFragment(){
         return (LeftFragment) supportFragmentManager.findFragmentByTag(Constant.CONTENT_LEFTTAG);
     }
     //提供获取正文对象的方法
    public ContentFragment getContentFragment(){
        return (ContentFragment) supportFragmentManager.findFragmentByTag(Constant.CONTENT_TAG);
    }
}
