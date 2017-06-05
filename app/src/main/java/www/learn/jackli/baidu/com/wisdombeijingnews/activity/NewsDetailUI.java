package www.learn.jackli.baidu.com.wisdombeijingnews.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import www.learn.jackli.baidu.com.wisdombeijingnews.R;

/**
 * Created by jackli on 2017/5/21.
 */

public class NewsDetailUI extends AppCompatActivity {

    @BindView(R.id.tv_basepager_title)
    TextView tvBasepagerTitle;
    @BindView(R.id.ib_basepager_menu)
    ImageButton ibBasepagerMenu;
    @BindView(R.id.ib_titlebar_back)
    ImageButton ibTitlebarBack;
    @BindView(R.id.ib_titlebar_share)
    ImageButton ibTitlebarShare;
    @BindView(R.id.ib_titlebar_textsize)
    ImageButton ibTitlebarTextsize;
    @BindView(R.id.wv_newsdetail_web)
    WebView wvNewsdetailWeb;
    @BindView(R.id.pb_newsdetail_progress)
    ProgressBar pbNewsdetailProgress;
    private Unbinder bind;
    private String url;
    private AlertDialog alertDialog;
    private WebSettings settings;
    private Context newsDetailUI;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        url = getIntent().getStringExtra("url"); //湖区配置Webview
        newsDetailUI = NewsDetailUI.this;
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_newsdetailui);
        bind = ButterKnife.bind(this);
        tvBasepagerTitle.setVisibility(View.GONE);
        ibBasepagerMenu.setVisibility(View.GONE);
        ibTitlebarBack.setVisibility(View.VISIBLE);
        ibTitlebarShare.setVisibility(View.VISIBLE);
        ibTitlebarTextsize.setVisibility(View.VISIBLE);
        initWebView();
    }

    private void initWebView() {
        settings = wvNewsdetailWeb.getSettings();
        settings.setBuiltInZoomControls(true);//支持缩放按钮
        settings.setUseWideViewPort(true);//支持双击缩放
        settings.setJavaScriptEnabled(true);//支持JavaScript
        //监听Webview
        wvNewsdetailWeb.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                //网页加载完成后回调
                pbNewsdetailProgress.setVisibility(View.GONE);
                super.onPageFinished(view, url);
            }
        });
        if (!TextUtils.isEmpty(url)) {
            wvNewsdetailWeb.loadUrl(url);
        }
    }


    @Override
    protected void onDestroy() {
        if (bind != null) {
            bind.unbind();
        }
        super.onDestroy();
    }

    @OnClick({R.id.ib_titlebar_back, R.id.ib_titlebar_share, R.id.ib_titlebar_textsize})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
            case R.id.ib_titlebar_share:
                showShare();
                break;
            case R.id.ib_titlebar_textsize:
                showTextsize();
                break;
            default:
                break;
        }
    }

    private int currentIndex = 2;
    private int tempIndex;

    private void showTextsize() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(MyApplication.getContext());
        /**
         * 生成Dialog的时候这个上下文对象，必须使用对应activity的activity对象本身作为context
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(NewsDetailUI.this);
        String[] items = new String[]{"超大号字体", "大号字体", "正常字体", "小号字体", "超小号字体"};
        builder.setSingleChoiceItems(items, currentIndex, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tempIndex = which;
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                currentIndex = tempIndex;
                //根据位置改变字体
                switchTextSize();

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (alertDialog != null) {
                    alertDialog.dismiss();
                }
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }

    private void switchTextSize() {
        switch (currentIndex) {
            case 0:
                //放大2倍
                settings.setTextZoom(200);
                break;
            case 1:
                settings.setTextZoom(150);
                break;
            case 2:
                settings.setTextZoom(100);
                break;
            case 3:
                settings.setTextZoom(75);
                break;
            case 4:
                settings.setTextZoom(50);
                break;
            default:
                break;
        }
    }

    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        // 关闭sso授权
        oks.disableSSOWhenAuthorize();
        // 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
        // oks.setNotification(R.drawable.ic_launcher,
        // getString(R.string.app_name));
        // text是分享文本，所有平台都需要这个字段
        oks.setText("世界那么大，想出去看看");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath("/sdcard/test.jpg");// 确保SDcard下面存在此张图片
        // 启动分享GUI
        oks.show(this);
    }
}
