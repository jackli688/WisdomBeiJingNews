package www.learn.jackli.baidu.com.wisdombeijingnews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.learn.jackli.baidu.com.wisdombeijingnews.MyApplication;
import www.learn.jackli.baidu.com.wisdombeijingnews.R;
import www.learn.jackli.baidu.com.wisdombeijingnews.utils.SPUtils;

import static www.learn.jackli.baidu.com.wisdombeijingnews.Constant.IS_APP_FIRST_OPEN;

/**
 * Splash页面中要想
 */
public class SplashActivity extends AppCompatActivity {
    @BindView(R.id.rl_welcome_bg)
    ImageView rlWelcomeBg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        //旋转动画
        RotateAnimation rotateAnimation = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setFillAfter(true);
//        viewById.setAnimation(rotateAnimation);
        //缩放动画
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(1000);
        scaleAnimation.setFillAfter(true);
//        viewById.setAnimation(scaleAnimation);
        //渐变动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(2000);
        alphaAnimation.setFillAfter(true);
//        viewById.setAnimation(alphaAnimation);
        //动画集合
        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
        rlWelcomeBg.startAnimation(animationSet);

        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //当动画结束后，根据是否打开过软件，判断进入引导页还是主页。
                boolean isFirstOpen = (Boolean) SPUtils.get(MyApplication.getContext(), IS_APP_FIRST_OPEN, true);
                if (isFirstOpen) {
                    //进入引导界面
                    startActivity(new Intent(MyApplication.getContext(), GuideUI.class));
                } else {
                    //进入主界面
                    startActivity(new Intent(MyApplication.getContext(), MainUI.class));
                }
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


}
