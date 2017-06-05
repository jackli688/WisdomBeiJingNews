package www.learn.jackli.baidu.com.wisdombeijingnews.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jackli on 2017/5/15.
 */

public class NewsMenuBeanDetails {
    @SerializedName("id")
    public int SonId;
    @SerializedName("title")
    public String sonTitle;
    @SerializedName("type")
    public int sonType;
    @SerializedName("url")
    public String contentPath;

    @Override
    public String toString() {
        return "NewsMenuBeanDetails{" +
                "SonId=" + SonId +
                ", sonTitle='" + sonTitle + '\'' +
                ", sonType=" + sonType +
                ", contentPath='" + contentPath + '\'' +
                '}';
    }
}