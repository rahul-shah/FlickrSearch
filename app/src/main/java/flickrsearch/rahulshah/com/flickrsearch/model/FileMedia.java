package flickrsearch.rahulshah.com.flickrsearch.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FileMedia
{
    @SerializedName("m")
    @Expose
    private String m;

    public String getM() {
        return m;
    }
}
