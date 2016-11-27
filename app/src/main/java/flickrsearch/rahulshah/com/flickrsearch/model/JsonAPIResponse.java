package flickrsearch.rahulshah.com.flickrsearch.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class JsonAPIResponse
{
    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("link")
    @Expose
    private String link;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("modified")
    @Expose
    private String modified;

    @SerializedName("generator")
    @Expose
    private String generator;

    @SerializedName("items")
    @Expose
    private ArrayList<FileItems> items;

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<FileItems> getItems() {
        return items;
    }
}
