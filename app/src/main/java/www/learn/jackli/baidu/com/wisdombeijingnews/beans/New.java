package www.learn.jackli.baidu.com.wisdombeijingnews.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jackli on 2017/5/19.
 */

public class New {
    @SerializedName("comment")
    public boolean iscomment;
    public String commentlist;
    public String commenturl;
    public int id;
    public String listimage;
    public String pubdate;
    public String title;
    public String type;
    public String url;


    @Override
    public String toString() {
        return "New{" +
                "iscomment=" + iscomment +
                ", commentlist='" + commentlist + '\'' +
                ", commenturl='" + commenturl + '\'' +
                ", id=" + id +
                ", listimage='" + listimage + '\'' +
                ", pubdate='" + pubdate + '\'' +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
