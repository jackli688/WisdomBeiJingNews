package www.learn.jackli.baidu.com.wisdombeijingnews.base.pager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import www.learn.jackli.baidu.com.wisdombeijingnews.MyApplication;
import www.learn.jackli.baidu.com.wisdombeijingnews.R;
import www.learn.jackli.baidu.com.wisdombeijingnews.activity.HomeActivity;

/**
 * Created by jackli on 2017/5/12.
 */

public abstract class BasePager {
    protected Context mContext;// 提供给子类使用的上下文对象

    @BindView(R.id.tv_basepager_title)
    public TextView tvBasepagerTitle;
    @BindView(R.id.fl_basepager_container)
    public FrameLayout flBasepagerContainer;
    @BindView(R.id.ib_basepager_menu)
    public ImageButton ibBasepagerMenu;
    @BindView(R.id.ib_titlebar_back)
    public ImageButton ibTitlebarBack;
    @BindView(R.id.ib_titlebar_share)
    public ImageButton ibTitlebarShare;
    @BindView(R.id.ib_titlebar_textsize)
    public ImageButton ibTitlebarTextsize;
    @BindView(R.id.ib_titlebar_phototype)
    public ImageButton ibTitlebarPhototype;
    private View rootView;

    public View getRootView() {
        return rootView;
    }

    public BasePager(Context context) {
        this.mContext = context;
        rootView = initView(null);

    }

    /**
     * 让子类实现，返回具体的子类布局
     *
     * @param container
     * @return animView
     */
    public View initView(ViewGroup container) {
        View rootView = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.basepager, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    /**
     * 让子类更新布局，不必须实现
     */
    public void initData() {
    }

    @OnClick(R.id.ib_basepager_menu)
    public void onViewClicked() {
        HomeActivity mHomeUI = (HomeActivity) mContext;
        mHomeUI.getSlidingMenu().toggle();
    }
}
