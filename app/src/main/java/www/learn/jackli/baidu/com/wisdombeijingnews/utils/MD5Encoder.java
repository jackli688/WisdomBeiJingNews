package www.learn.jackli.baidu.com.wisdombeijingnews.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by jackli on 2017/5/23.
 */

public class MD5Encoder {
    public static String encode(String string) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        byte[] digest = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF_8"));
        StringBuilder stringBuilder = new StringBuilder(digest.length * 2);
        for (byte b : digest) {
            if ((b & 0xFF) < 0x10) {
                stringBuilder.append("0");
            }
            stringBuilder.append(Integer.toHexString(b & 0xFF));
        }
        return stringBuilder.toString();
    }
}
