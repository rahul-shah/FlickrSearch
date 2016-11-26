package flickrsearch.rahulshah.com.flickrsearch.model;

import java.io.Serializable;

public class ImageHolder implements Serializable
{
    private String mName;
    private String mImage;
    private String mTimestamp;

    //Default constructor
    public ImageHolder()
    {

    }

    public ImageHolder(String name, String image, String timestamp)
    {
        mName = name;
        mImage = image;
        mTimestamp = timestamp;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        mImage = image;
    }

    public String getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(String timestamp) {
        mTimestamp = timestamp;
    }
}
