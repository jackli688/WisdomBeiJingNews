package www.learn.jackli.baidu.com.wisdombeijingnews.base.pager;

import android.content.Context;
import android.view.View;

/**
 * Created by jackli on 2017/5/15.
 */

public abstract class MenuBasePager {

    public View rootView;// 给新闻中心的Framelayout展示
    public Context mContext;

    public MenuBasePager(Context context) {
        this.mContext = context;
        rootView = initView();
    }

    /**
     * 让子类返回布局，必须实现
     *
     * @return
     */
    protected abstract View initView();

    /**
     * 子类更新布局，不必须实现
     */
    public void initData() {

    }
}
