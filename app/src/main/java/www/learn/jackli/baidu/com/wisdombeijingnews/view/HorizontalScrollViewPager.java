package www.learn.jackli.baidu.com.wisdombeijingnews.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by jackli on 2017/5/19.
 */

public class HorizontalScrollViewPager extends ViewPager {

    private float downX;
    private float downY;

    public HorizontalScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 1、不处理上下滑动，让父容器拦截，getParent().requestDiallowInterceptTouchEvent(false);
     * 2、左右滑动
     * 2.1、第一页时，且手指从左往右，不处理事件
     * 2.2、最后一页时，且手指从右往左，不处理事件
     * 2.3、其他情况，自己处理事件，不让父容器拦截事件getParent().requestDiallowInterceptTouchEvent(true);
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                downY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = ev.getX();
                float moveY = ev.getY();
                float diffX = moveX - downX;
                float diffY = moveY - downY;
                if (Math.abs(diffX) < Math.abs(diffY)) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                //2.左右滑动
                else {
                    //2.1第一页时，且手指从左往右，不处理事件
                    if (getCurrentItem() == 0 && diffX > 0) {
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                    //2.2最后一页时，且手指从右往左，不处理事件
                    else if (getCurrentItem() == getAdapter().getCount() - 1 && diffX < 0) {
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                    //2.3其他情况，自己处理事件
                    else {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }
                break;
            default:
                break;

        }

        return super.dispatchTouchEvent(ev);
    }
}
