package flickrsearch.rahulshah.com.flickrsearch.model;

import java.io.Serializable;

public class ImageHolder implements Serializable
{
    private String mName;
    private String mImage;
    private String mFullSizeImage;
    private String mTimestamp;
    private String mDescription;

    //Default constructor
    public ImageHolder()
    {

    }

    public ImageHolder(String name, String image, String timestamp, String fullSizeImage,String description)
    {
        mName = name;
        mImage = image;
        mFullSizeImage = fullSizeImage;
        mTimestamp = timestamp;
        mDescription = description;
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

    public String getFullImage() {
        return mFullSizeImage;
    }

    public void setFullImage(String image) {
        mFullSizeImage = image;
    }

    public String getImageDescription() {
        return mDescription;
    }

    public void setImageDescription(String description) {
        mDescription = description;
    }

    public String getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(String timestamp) {
        mTimestamp = timestamp;
    }
}
