package flickrsearch.rahulshah.com.flickrsearch;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class FlickrSearchApp extends Application
{
    public static final String TAG = FlickrSearchApp.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private static FlickrSearchApp mStaticAppInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mStaticAppInstance = this;
    }

    public static synchronized FlickrSearchApp getInstance()
    {
        return mStaticAppInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    //Add to volley queue with tag
    public <T> void addToRequestQueue(Request<T> req, String tag)
    {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    //Add to volley queue without tag
    public <T> void addToRequestQueue(Request<T> req)
    {
        getRequestQueue().add(req);
    }

    //Cancel all pending requests
    public void cancelPendingRequests(Object tag)
    {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
