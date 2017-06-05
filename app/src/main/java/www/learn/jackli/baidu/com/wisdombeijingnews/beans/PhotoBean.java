package www.learn.jackli.baidu.com.wisdombeijingnews.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jackli on 2017/5/22.
 */

public class PhotoBean {
    @SerializedName("retcode")
    public int code;
    public TabData data;

    @Override
    public String toString() {
        return "PhotoBean{" +
                "code=" + code +
                ", data=" + data +
                '}';
    }
}
