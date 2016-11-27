package flickrsearch.rahulshah.com.flickrsearch.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
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

    @BindView(R.id.activity_main_toolbar) Toolbar mToolbar;
    @BindView(R.id.activity_main_recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.activity_main_refresh_container) SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.activity_main_fab) FloatingActionButton mSearchFAB;

    private String TAG = MainActivity.class.getSimpleName();
    private static final String endpoint = "https://api.flickr.com/services/feeds/photos_public.gne?&tags=";
    private ArrayList<ImageHolder> images;
    private ProgressDialog pDialog;
    private ImageGalleryAdapter mAdapter;
    private String mCurrentQuery = "";
    private int mContextMenuItemSelected = 0;
    public final static int SEARCH_QUERY_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        launchWelcomeScreen();

        mToolbar.setTitle(R.string.main_screen_title);
        setSupportActionBar(mToolbar);

        pDialog = new ProgressDialog(this);
        images = new ArrayList<>();
        mAdapter = new ImageGalleryAdapter(getApplicationContext(), images);

        setUpViews();

        fetchImages("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pDialog.hide();
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
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", images);
                bundle.putInt("position", position);

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ImageDetailFragment newFragment = ImageDetailFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");
            }

            @Override
            public void onLongClick(View view, int position)
            {
                mContextMenuItemSelected = position;
                registerForContextMenu(view);
            }
        }));

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(false);
                fetchImages(mCurrentQuery);
            }
        });

        mSearchFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivityForResult(intent,SEARCH_QUERY_REQUEST);
            }
        });
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Share this image");
        menu.add(0, v.getId(), 0, "Share");
    }

    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals("Share")) {
            createSharingMenu(mContextMenuItemSelected);
        }
        return true;
    }

    private void createSharingMenu(int imagePosition)
    {
        //Uri uri = Uri.parse(images.get(imagePosition).getFullImage());
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, images.get(imagePosition).getFullImage());
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent,"send to"));
    }

    private void fetchImages(String query)
    {
        String apiFinal = endpoint + query + "&format=json&nojsoncallback=1";
        pDialog.setMessage("Searching for images");
        pDialog.show();

        StringRequest req = new StringRequest(apiFinal,
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
                            image.setImage(temp.items.get(i).title);
                            image.setImage(temp.items.get(i).media.m);
                            image.setTimestamp(temp.items.get(i).date_taken);
                            image.setFullImage(temp.items.get(i).link);
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
        req.setShouldCache(true);

        // Adding request to request queue
        FlickrSearchApp.getInstance().addToRequestQueue(req);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case SEARCH_QUERY_REQUEST:
                if(data != null && data.hasExtra("USER_QUERY")) {
                    String query = data.getStringExtra("USER_QUERY");
                    mCurrentQuery = query;
                    fetchImages(query);
                }
               break;

            default:
                break;
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void launchWelcomeScreen()
    {
        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        //  Create a new boolean and preference and set it to true
        boolean isFirstStart = getPrefs.getBoolean("firstStart", true);

        //  If the activity has never started before...
        if (isFirstStart)
        {
            //  Launch app intro
            startActivity(new Intent(MainActivity.this, WelcomeIntroActivity.class));

            //  Make a new preferences editor
            SharedPreferences.Editor e = getPrefs.edit();

            //  Edit preference to make it false because we don't want this to run again
            e.putBoolean("firstStart", false);

            //  Apply changes
            e.apply();
        }
    }
}