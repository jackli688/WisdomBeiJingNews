package www.learn.jackli.baidu.com.wisdombeijingnews.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by jackli on 2017/5/23.
 */

public class ImageCacheUtil {
    private MemoryCacheUtil memoryCacheUtil;
    private LocalCacheUtil localCacheUtil;
    private NetCacheUtil netCacheUtil;
    private static ImageCacheUtil mImageCacheUtil;

    public static synchronized ImageCacheUtil getImageCacheUtil() {
        if (mImageCacheUtil == null) {
            mImageCacheUtil = new ImageCacheUtil();
        }
        return mImageCacheUtil;
    }

    private ImageCacheUtil() {
        memoryCacheUtil = new MemoryCacheUtil();
        localCacheUtil = new LocalCacheUtil(memoryCacheUtil);
        netCacheUtil = new NetCacheUtil(memoryCacheUtil, localCacheUtil);
    }

    /**
     * 1、从内存缓存取图片，取到就展示，取不到往下走
     * 2、从文件缓存取图片，取到就展示，取不到往下走
     * 3、访问网络下载图片，通过Handler展示
     */
    public void display(ImageView imageView, String url) {
        Bitmap bitmap = null;
        bitmap = memoryCacheUtil.getBitmap(url);//从内存中缓存图片
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            LogUtil.i("从内存缓存取出了图片");
            return;
        }
        bitmap = localCacheUtil.getBitmap(url);//从文件缓存取图片
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            LogUtil.i("从文件缓存取图片");
            return;
        }
        netCacheUtil.display(url, imageView);
        LogUtil.i("访问网路下载图片");

    }
}
