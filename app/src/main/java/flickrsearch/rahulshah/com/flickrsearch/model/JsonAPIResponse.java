package flickrsearch.rahulshah.com.flickrsearch.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class JsonAPIResponse
{
    @SerializedName("title")
    @Expose
    public String title;

    @SerializedName("link")
    @Expose
    public String link;

    @SerializedName("description")
    @Expose
    public String description;

    @SerializedName("modified")
    @Expose
    public String modified;

    @SerializedName("generator")
    @Expose
    public String generator;

    @SerializedName("items")
    @Expose
    public ArrayList<FileItems> items;
}
