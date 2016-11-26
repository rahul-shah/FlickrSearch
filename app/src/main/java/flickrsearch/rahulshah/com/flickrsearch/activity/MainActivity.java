package flickrsearch.rahulshah.com.flickrsearch.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import flickrsearch.rahulshah.com.flickrsearch.FlickrSearchApp;
import flickrsearch.rahulshah.com.flickrsearch.R;
import flickrsearch.rahulshah.com.flickrsearch.adapter.ImageGalleryAdapter;
import flickrsearch.rahulshah.com.flickrsearch.model.ImageHolder;
import flickrsearch.rahulshah.com.flickrsearch.model.JsonAPIResponse;

public class MainActivity extends AppCompatActivity
{

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_container) SwipeRefreshLayout mSwipeRefreshLayout;

    private String TAG = MainActivity.class.getSimpleName();
    private static final String endpoint = "https://api.flickr.com/services/feeds/photos_public.gne?&tags=\"cats\"&format=json&nojsoncallback=1";
    private ArrayList<ImageHolder> images;
    private ProgressDialog pDialog;
    private ImageGalleryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(mToolbar);

        ButterKnife.bind(this);

        setUpViews();

        pDialog = new ProgressDialog(this);
        images = new ArrayList<>();
        mAdapter = new ImageGalleryAdapter(getApplicationContext(), images);

        fetchImages();
    }

    private void setUpViews()
    {
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(new ImageGalleryAdapter.RecyclerTouchListener(getApplicationContext(), mRecyclerView, new ImageGalleryAdapter.ClickListener()
        {
            @Override
            public void onClick(View view, int position)
            {
                /*Bundle bundle = new Bundle();
                bundle.putSerializable("images", images);
                bundle.putInt("position", position);

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");*/
            }

            @Override
            public void onLongClick(View view, int position)
            {

            }
        }));

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(false);
                fetchImages();
            }
        });
    }

    private void fetchImages()
    {
        pDialog.setMessage("Searching for images");
        pDialog.show();

        StringRequest req = new StringRequest(endpoint,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Log.d(TAG, response.toString());
                        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                        JsonAPIResponse temp = gson.fromJson(response,JsonAPIResponse.class);

                        pDialog.hide();
                        images.clear();

                        for (int i = 0; i < temp.items.size(); i++)
                        {
                            ImageHolder image = new ImageHolder();
                            image.setImage(temp.items.get(i).media.m);
                            image.setTimestamp(temp.items.get(i).date_taken);
                            images.add(image);
                        }

                        mAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.e(TAG, "Error: " + error.getMessage());
                        pDialog.hide();
                    }
        });

        // Adding request to request queue
        FlickrSearchApp.getInstance().addToRequestQueue(req);
    }
}