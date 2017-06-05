package www.learn.jackli.baidu.com.wisdombeijingnews.beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jackli on 2017/5/15.
 */
public class NewCenterBean {
    @SerializedName("data")
    public List<NewsMenuBean> news;
    @SerializedName("extend")
    public List<Integer> extendNews;
    @SerializedName("retcode")
    public int code;

    @Override
    public String toString() {
        return "NewCenterBean{" +
                "news=" + news +
                ", extendNews=" + extendNews +
                ", code=" + code +
                '}';
    }
}
