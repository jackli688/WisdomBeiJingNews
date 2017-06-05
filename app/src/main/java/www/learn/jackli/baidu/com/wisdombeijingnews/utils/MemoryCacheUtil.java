package www.learn.jackli.baidu.com.wisdombeijingnews.utils;


import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * Created by jackli on 2017/5/23.
 */

public class MemoryCacheUtil {
    private int maxSize;
    private LruCache<String, Bitmap> lruCache;

    public MemoryCacheUtil() {
        maxSize = (int) (Runtime.getRuntime().maxMemory() / 8);
        lruCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //返回当前图片的大小
                return value.getRowBytes() * value.getHeight();
            }
        };
    }
    /**
     * 从内存缓存取图片
     */
    public Bitmap getBitmap(String url){
        return lruCache.get(url);
    }

    /**
     * 往内存缓存图片
     */
    public void saveBitmap(String url,Bitmap bitmap){
        lruCache.put(url,bitmap);
    }

}
