package flickrsearch.rahulshah.com.flickrsearch.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FileItems
{
    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("link")
    @Expose
    private String link;

    @SerializedName("date_taken")
    @Expose
    private String date_taken;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("media")
    @Expose
    private FileMedia media;

    @SerializedName("tags")
    @Expose
    private String tags;

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public FileMedia getMedia() {
        return media;
    }

    public String getLink() {
        return link;
    }

    public String getDate_taken() {
        return date_taken;
    }

    public String getTags() {
        return tags;
    }
}
