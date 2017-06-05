package www.learn.jackli.baidu.com.wisdombeijingnews.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by jackli on 2017/5/13.
 * 禁止ViewPage滑动
 */

public class NoScrollViewPager extends ViewPager {

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //干掉处理事件的默认行为
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }
    //不拦截孩子的事件
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

}
