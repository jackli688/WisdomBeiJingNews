package www.learn.jackli.baidu.com.wisdombeijingnews.beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jackli on 2017/5/19.
 */

public class TabData {
    @SerializedName("countcommenturl")
    public String countUrl;
    public String more;
    public List<New> news;
    public String title;
    public List<Topic> topics;
    public List<TopicNew> topnews;
    @SerializedName("retcode")
    public int code;

}
