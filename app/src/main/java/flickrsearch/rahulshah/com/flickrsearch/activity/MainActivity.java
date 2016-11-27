package flickrsearch.rahulshah.com.flickrsearch.activity;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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

    private String TAG = MainActivity.class.getSimpleName();
    private static final String endpoint = "https://api.flickr.com/services/feeds/photos_public.gne?&tags=";
    private ArrayList<ImageHolder> mListOfImages;
    private ProgressDialog mProgressDialog;
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

        mProgressDialog = new ProgressDialog(this);
        mListOfImages = new ArrayList<>();
        mAdapter = new ImageGalleryAdapter(getApplicationContext(), mListOfImages);

        setUpViews();

        fetchImages("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mProgressDialog.hide();
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
                bundle.putSerializable("images", mListOfImages);
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
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle(R.string.share_image_context_menu);
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
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, mListOfImages.get(imagePosition).getFullImage());
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent,"send to"));
    }

    private void fetchImages(String query)
    {
        String apiFinal = endpoint + query + "&format=json&nojsoncallback=1";
        mProgressDialog.setMessage(getResources().getString(R.string.searching_for_images));
        mProgressDialog.show();

        StringRequest req = new StringRequest(apiFinal,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Log.d(TAG, response.toString());
                        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                        JsonAPIResponse apiResponseObject = gson.fromJson(response,JsonAPIResponse.class);

                        mProgressDialog.hide();
                        mListOfImages.clear();

                        for (int i = 0; i < apiResponseObject.getItems().size(); i++)
                        {
                            ImageHolder image = new ImageHolder();
                            image.setName(apiResponseObject.getItems().get(i).getTitle());
                            image.setImage(apiResponseObject.getItems().get(i).getMedia().getM());
                            image.setTimestamp(apiResponseObject.getItems().get(i).getDate_taken());
                            image.setFullImage(apiResponseObject.getItems().get(i).getLink());
                            image.setImageDescription(apiResponseObject.getItems().get(i).getDescription());
                            mListOfImages.add(image);
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
                        mProgressDialog.hide();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        // Retrieve the SearchView and plug it into SearchManager
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchImages(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconified(false);
        searchView.clearFocus();
        searchView.setFocusable(true);
        searchView.requestFocusFromTouch();
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode)
        {
            case 1:
                if (ActivityCompat.checkSelfPermission(MainActivity.this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(FlickrSearchApp.getInstance(),R.string.storage_permission_needed,Toast.LENGTH_SHORT).show();
                }

        }

    }
}