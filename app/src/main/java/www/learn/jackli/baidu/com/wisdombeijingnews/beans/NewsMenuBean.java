package www.learn.jackli.baidu.com.wisdombeijingnews.beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;


/**
 * Created by jackli on 2017/5/15.
 */

public class NewsMenuBean {
    @SerializedName("children")
    public List<NewsMenuBeanDetails> newsMenuDetails;
    @SerializedName("id")
    public int parentId;
    @SerializedName("title")
    public String parentTitle;
    @SerializedName("type")
    public int parentType;

    @Override
    public String toString() {
        return "NewsMenuBean{" +
                "newsMenuDetails=" + newsMenuDetails +
                ", parentId=" + parentId +
                ", parentTitle='" + parentTitle + '\'' +
                ", parentType=" + parentType +
                '}';
    }
}
