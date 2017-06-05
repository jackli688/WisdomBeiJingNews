package www.learn.jackli.baidu.com.wisdombeijingnews.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import www.learn.jackli.baidu.com.wisdombeijingnews.MyApplication;

/**
 * Created by jackli on 2017/5/23.
 */

public class LocalCacheUtil {
    private String CACHE_DIR;
    private MemoryCacheUtil memoryCacheUtil;

    public LocalCacheUtil(MemoryCacheUtil memoryCacheUtil) {
        this.memoryCacheUtil = memoryCacheUtil;
//        CACHE_DIR = Environment.getExternalStorageDirectory().toString() + "/zhbj87/";
        CACHE_DIR = MyApplication.getContext().getCacheDir().toString() + "/zhbj87/";
    }

    /**
     * 往sdcard存图片
     */
    public void saveBitmap(String url, Bitmap bitmpap) {
        try {
            //把url进行加密作为保存的图片文件名
            String fileName = MD5Encoder.encode(url);
            File file = new File(CACHE_DIR, fileName);
            //如果父目录不存在，需要创建
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            try {
                //创建文件输出流
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                bitmpap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从sdcard取图片
     */
    public Bitmap getBitmap(String url) {
        //把url进行加密作为保存的图片文件名
        Bitmap bitmap = null;
        try {
            String fileName = MD5Encoder.encode(url);
            LogUtil.i("CACHE_DIR" + CACHE_DIR);
            File file = new File(CACHE_DIR, fileName);
            if (file.exists()) {
                //如果文件存在，转换成图片对象
                bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                //保存到内存中
                memoryCacheUtil.saveBitmap(url, bitmap);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
