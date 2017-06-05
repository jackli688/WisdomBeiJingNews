package www.learn.jackli.baidu.com.wisdombeijingnews.utils;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import www.learn.jackli.baidu.com.wisdombeijingnews.utils.http.okhttp.OkHttpUtils;
import www.learn.jackli.baidu.com.wisdombeijingnews.utils.http.okhttp.callback.BitmapCallback;

/**
 * Created by jackli on 2017/5/23.
 */

public class NetCacheUtil {
    private MemoryCacheUtil memoryCacheUtil;
    private LocalCacheUtil localCacheUtil;
    private ListView lsitview;
    private final ExecutorService threadPool;
    private final int wattingTime = 3000;
    private final int SUCCESS = 1;


    public NetCacheUtil(MemoryCacheUtil memoryCacheUtil, LocalCacheUtil localCacheUtil) {
        threadPool = Executors.newFixedThreadPool(5); //拥有5个核心线程的线程池
        this.memoryCacheUtil = memoryCacheUtil;
        this.localCacheUtil = localCacheUtil;
    }

    //下载图片，展示
    public void display(String url, ImageView imageView) {
        threadPool.execute(new DownLoadRunnable(url, imageView));
    }


    private class DownLoadRunnable implements Runnable {
        private String mUrl;
        private ImageView mImageView;
        private int startPosition;

        public DownLoadRunnable(String url, ImageView imageView) {
            this.mUrl = url;
            this.mImageView = imageView;
            //开始下载任务时，ImageView的位置，线程执行完成后，要找到startPositiont在listview中是否还存在ImageView
            startPosition = (int) mImageView.getTag();

        }

        @Override
        public void run() {
            //下载图片
            try {
                Thread.sleep(wattingTime);
                OkHttpUtils.get().url(mUrl).build().execute(new BitmapCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtil.d("异常的消息:" + e.getMessage() + "\n");
                    }

                    @Override
                    public void onResponse(Bitmap response, int id) {
                        Message.obtain(new NetCacheUtilHandler(), SUCCESS, new Result(response, mImageView, startPosition)).sendToTarget();
                        //保存到内存和本地
                        memoryCacheUtil.saveBitmap(mUrl, response);
                        localCacheUtil.saveBitmap(mUrl, response);
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    class Result {
        public Bitmap bitmap;
        public ImageView imageView;
        private int startPosition;


        public Result(Bitmap bitmap, int startPosition) {

            this.bitmap = bitmap;
            this.startPosition = startPosition;
        }

        public Result(Bitmap bitmap, ImageView imageView) {

            this.bitmap = bitmap;
            this.imageView = imageView;
        }

        public Result(Bitmap bitmap, ImageView imageView, int startPosition) {
            this.bitmap = bitmap;
            this.imageView = imageView;
            this.startPosition = startPosition;
        }
    }

    class NetCacheUtilHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == SUCCESS) {
                Result result = (Result) msg.obj;
                int endPosition = (Integer) result.imageView.getTag();
                if (result.startPosition == endPosition) {
                    result.imageView.setImageBitmap(result.bitmap);
                }
            }
        }
    }

}
