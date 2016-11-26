package flickrsearch.rahulshah.com.flickrsearch.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FileItems
{
    @SerializedName("title")
    @Expose
    public String title;

    @SerializedName("link")
    @Expose
    public String link;

    @SerializedName("date_taken")
    @Expose
    public String date_taken;

    @SerializedName("description")
    @Expose
    public String description;

    @SerializedName("media")
    @Expose
    public FileMedia media;
}
