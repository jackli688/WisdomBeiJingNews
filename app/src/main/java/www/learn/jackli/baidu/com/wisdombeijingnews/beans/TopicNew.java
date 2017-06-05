package www.learn.jackli.baidu.com.wisdombeijingnews.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jackli on 2017/5/19.
 */

public class TopicNew {
    @SerializedName("comment")
    public boolean iscomment;
    public String commentlist;
    public String commenturl;
    public int id;
    public String pubdate;
    public String title;
    public String topimage;
    public String type;
    public String url;
}
