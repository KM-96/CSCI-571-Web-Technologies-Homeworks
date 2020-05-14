package org.usc.csci571.newsapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;
import org.usc.csci571.newsapp.R;
import org.usc.csci571.newsapp.adapters.CardAdapter;
import org.usc.csci571.newsapp.api.NewsApi;
import org.usc.csci571.newsapp.models.NewsCardModel;
import org.usc.csci571.newsapp.utils.BookmarkManager;
import org.usc.csci571.newsapp.utils.Constants;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements CardAdapter.OnCardListener {

    private String queryString;
    private LinearLayout progressBarLinearLayout;
    private SwipeRefreshLayout searchLayout;
    private RequestQueue requestQueue;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CardAdapter adapter;
    private CardAdapter.OnCardListener onCardListener;
    private boolean isSwipeToRefresh = false;
    private ArrayList<NewsCardModel> newsCardModelList;
    private BookmarkManager bookmarkManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        Intent intent = getIntent();
        this.queryString = intent.getStringExtra(Constants.QUERY_STRING);

        init();

        searchLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isSwipeToRefresh = true;
                loadSearchResults();
                searchLayout.setRefreshing(false);
                isSwipeToRefresh = false;
            }
        });

        loadSearchResults();
    }

    private void init() {
        initToolbar();
        this.requestQueue = Volley.newRequestQueue(this);
        this.recyclerView = findViewById(R.id.search_results_recycler_view);
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        this.layoutManager = new LinearLayoutManager(this);
        this.onCardListener = this;
        this.progressBarLinearLayout = findViewById(R.id.progress_bar_linear_layout);
        this.searchLayout = findViewById(R.id.swipe_to_refresh_search_results);
        this.bookmarkManager = new BookmarkManager(findViewById(R.id.search_relative_layout));
    }


    private void initToolbar() {
        //Add the toolbar to the setSupportActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.search_toolbar);
        String title = "Search Results for " + this.queryString;
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void loadSearchResults() {
        String url = Constants.NODE_APP_BASE_URL + Constants.GUARDIAN_SEARCH + "?" + Constants.Q_QUERY_PARAM + this.queryString;
        displayLoadingBar();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            newsCardModelList = NewsApi.parseNewsResults(response, Constants.SEARCH);
                            hideLoadingBar();
                            adapter = new CardAdapter(newsCardModelList, onCardListener);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(adapter);
                        } catch (Exception e) {
                            System.out.println("Error parsing search results!");
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Error getting search results from the API");
                        error.printStackTrace();
                    }
                });
        jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {
                error.printStackTrace();
            }
        });
        this.requestQueue.add(jsonObjectRequest);
    }

    private void hideLoadingBar() {
        if (!this.isSwipeToRefresh) {
            this.progressBarLinearLayout.setVisibility(View.GONE);
            this.searchLayout.setVisibility(View.VISIBLE);
        }
    }

    private void displayLoadingBar() {
        if (!this.isSwipeToRefresh) {
            this.progressBarLinearLayout.setVisibility(View.VISIBLE);
            this.searchLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCardClick(int position) {
        Intent intent = new Intent(this, DetailCardActivity.class);
        NewsCardModel currentArticle = newsCardModelList.get(position);
        intent.putExtra(Constants.ARTICLE_ID, currentArticle.getId());
        intent.putExtra(Constants.ARTICLE_TITLE, currentArticle.getTitle());
        intent.putExtra(Constants.ARTICLE_TAG, bookmarkManager.isBookmarked(currentArticle.getId()));
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}
