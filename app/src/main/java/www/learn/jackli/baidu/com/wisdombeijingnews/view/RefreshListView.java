package www.learn.jackli.baidu.com.wisdombeijingnews.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import www.learn.jackli.baidu.com.wisdombeijingnews.R;
import www.learn.jackli.baidu.com.wisdombeijingnews.utils.LogUtil;
import www.learn.jackli.baidu.com.wisdombeijingnews.utils.ToastUtil;

/**
 * Created by jackli on 2017/5/21.
 */

public class RefreshListView extends ListView {

    //    @BindView(R.id.iv_refresh_arrow)
    ImageView ivRefreshArrow;
    //    @BindView(R.id.pb_refresh_progress)
    ProgressBar pbRefreshProgress;
    //    @BindView(R.id.tv_refresh_state)
    TextView tvRefreshState;
    //    @BindView(R.id.tv_refresh_time)
    TextView tvRefreshTime;
    private static final int PULLDOWN_STATE = 0;// 下拉刷新状态
    private static final int RELEASE_STATE = 1;// 松开刷新状态
    private static final int REFRESHING_STATE = 2;// 正在刷新状态
    private int CURREN_STATE = PULLDOWN_STATE;
    private int downY = -1;
    private int measuredHeaderHeight;
    private View headerView;
    private OnNewTabRefreshListener mListener;
    private RotateAnimation up;
    private RotateAnimation down;
    private View footerView;
    private int footerMeasuredHeight;
    private boolean isLoadMore = false;

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeader();
        initAnimation();
        initFooter();
    }

    private void initFooter() {
        footerView = View.inflate(getContext(), R.layout.refresh_footer, null);
        //隐藏的脚布局
        footerView.measure(0, 0);
        footerMeasuredHeight = footerView.getMeasuredHeight();
        footerView.setPadding(0, -footerMeasuredHeight, 0, 0);
        this.addFooterView(footerView);
        //监听Listview滚动状态
        this.setOnScrollListener(new NewsTabScrollListener());
    }

    private void initAnimation() {
        up = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        up.setDuration(500);
        up.setFillAfter(true);//保持最后一帧
        down = new RotateAnimation(-180, -360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        down.setDuration(500);
        down.setFillAfter(true);
    }


    private void initHeader() {
        //添加下拉刷新头
        headerView = View.inflate(getContext(), R.layout.refresh_header, null);
//        ButterKnife.bind(headerView);
        ivRefreshArrow = (ImageView) headerView.findViewById(R.id.iv_refresh_arrow);
        pbRefreshProgress = (ProgressBar) headerView.findViewById(R.id.pb_refresh_progress);
        tvRefreshState = (TextView) headerView.findViewById(R.id.tv_refresh_state);
        tvRefreshTime = (TextView) headerView.findViewById(R.id.tv_refresh_time);
        headerView.measure(0, 0);
        measuredHeaderHeight = headerView.getMeasuredHeight();
        headerView.setPadding(0, -measuredHeaderHeight, 0, 0);
        this.addHeaderView(headerView);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //当按到轮播图控件时，获取不到down事件，在move事件给他赋值
                if (downY == -1) {
                    downY = (int) ev.getY();
                }
                int moveY = (int) ev.getY();
                int diffY = moveY - downY;
//                当ListView中轮播图完全展示时，才处理下拉刷新
                if (getFirstVisiblePosition() != 0) {
                    break;
                }
                // 如果已经处于正在刷新，不能再刷新了
                if (CURREN_STATE == REFRESHING_STATE) {
                    break;
                }
                //只处理手从上往下滑动
                if (diffY > 0) {
                    //计算头布局的toppading值
                    int topadding = diffY - measuredHeaderHeight;
//                    根据topadding值是否大于0判断状态切换
                    if (topadding < 0 && CURREN_STATE != PULLDOWN_STATE) {//头布局没有完全显示，切换到下拉刷新状态
                        LogUtil.d("下拉刷新状态中");
                        CURREN_STATE = PULLDOWN_STATE;
                        switchState(CURREN_STATE);
                    } else if (topadding > 0 && CURREN_STATE != RELEASE_STATE) { //头布局已经完全显示，切换到松开刷新状态
                        LogUtil.d("开始刷新状态中");
                        CURREN_STATE = RELEASE_STATE;
                        switchState(CURREN_STATE);
                    }
                    //设置头布局的padding,到达移动的效果
                    headerView.setPadding(0, topadding, 0, 0);
                    //自己消费掉了事件，事件不在继续向下分发。
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                //为了不影响下一次down,donY初始化为-1
                downY = -1;
                //手指抬起时，根据当前状态判断时候切换到正在刷新状态
                if (CURREN_STATE == PULLDOWN_STATE) {
                    headerView.setPadding(0, -measuredHeaderHeight, 0, 0);

                } else if (CURREN_STATE == RELEASE_STATE) {
                    //当前处于松开状态，需要切到正在刷新，把头布局设置成刚好完全展示
                    LogUtil.d("松开刷新中");
                    CURREN_STATE = REFRESHING_STATE;
                    switchState(CURREN_STATE);
                    headerView.setPadding(0, 0, 0, 0);
                    //当处于下拉刷新状态时，调用外界传来的监听器执行真正的业务
                    if (mListener != null) {
                        mListener.onRefreshing();
                    }
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void switchState(int curren_state) {
        switch (curren_state) {
            //下拉刷新
            case PULLDOWN_STATE:
                tvRefreshState.setText(R.string.pull_refresh);
                ivRefreshArrow.setVisibility(View.VISIBLE);
                pbRefreshProgress.setVisibility(View.INVISIBLE);
                ivRefreshArrow.startAnimation(down);
                break;
            //松手
            case RELEASE_STATE:
                tvRefreshState.setText(R.string.leftup_refresh);
                ivRefreshArrow.startAnimation(up);
                break;
            //刷新状态
            case REFRESHING_STATE:
                //清除箭头的动画
                ivRefreshArrow.clearAnimation();
                tvRefreshState.setText(R.string.refreshing);
                ivRefreshArrow.setVisibility(View.INVISIBLE);
                pbRefreshProgress.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    //对外暴露出接口
    public interface OnNewTabRefreshListener {
        //下拉刷新方法
        void onRefreshing();

        //加载更多方法
        void onLoadMore();
    }

    //    让外界传递监听器
    public void setOnNewTabRefreshListener(OnNewTabRefreshListener l) {
        this.mListener = l;
    }


    //    提供刷新完成方法
    public void refreshFinished(boolean success) {
        tvRefreshState.setText(R.string.pull_refresh);
        ivRefreshArrow.setVisibility(View.VISIBLE);
        pbRefreshProgress.setVisibility(View.INVISIBLE);
        CURREN_STATE = PULLDOWN_STATE;
        headerView.setPadding(0, -measuredHeaderHeight, 0, 0);
        if (success) {
            SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
            String time = format.format(new Date());
            tvRefreshTime.setText("最后刷新时间:" + time);
        } else {
            ToastUtil.show(getContext(), "亲，网络出了问题", Toast.LENGTH_SHORT);
        }
    }

    //恢复加载更多状态
    public void loadMoreFinished() {
        if (isLoadMore) {
            footerView.setPadding(0, -footerMeasuredHeight, 0, 0);
            isLoadMore = false;
        }

    }


    private class NewsTabScrollListener implements OnScrollListener {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            //当处于停止或惯性停止状态时，且ListView展示的条目最后一条数据，显示加载更多布局
            if (OnScrollListener.SCROLL_STATE_IDLE == scrollState || OnScrollListener.SCROLL_STATE_FLING == scrollState) {
                if (getLastVisiblePosition() == getCount() - 1 && !isLoadMore) {
                    isLoadMore = true;
                    footerView.setPadding(0, 0, 0, 0);
                    //让加载更多脚布局自动显示出来
                    setSelection(getCount());
//                    当处于加载更多时，调用外界的临时的监听的业务
                    if (mListener != null) {
                        mListener.onLoadMore();
                    }
                }
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
    }
}
