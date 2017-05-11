package www.learn.jackli.baidu.com.wisdombeijingnews;

import android.app.Application;
import android.content.Context;

/**
 * Created by jackli on 2017/5/10.
 */

public class MyApplication extends Application {
    private static Context context;
    

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
