package org.usc.csci571.newsapp.api;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.usc.csci571.newsapp.R;
import org.usc.csci571.newsapp.activities.ErrorActivity;
import org.usc.csci571.newsapp.adapters.CardAdapter;
import org.usc.csci571.newsapp.models.NewsCardModel;
import org.usc.csci571.newsapp.utils.BookmarkManager;
import org.usc.csci571.newsapp.utils.Constants;

import java.util.ArrayList;

public class NewsApi {

    private RequestQueue requestQueue;
    private View view;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static BookmarkManager bookmarkManager;
    private ArrayList<NewsCardModel> newsCardModelList;
    private CardAdapter.OnCardListener onCardListener;
    private LinearLayout progressBarRelativeLayout;
    private SwipeRefreshLayout homeSectionLayout;
    private SwipeRefreshLayout headlinesSectionLayout;

    public NewsApi(View view, RecyclerView recyclerView, CardAdapter.OnCardListener onCardListener) {
        this.view = view;
        this.recyclerView = recyclerView;
        this.onCardListener = onCardListener;
        this.requestQueue = Volley.newRequestQueue(view.getContext());
        this.bookmarkManager = new BookmarkManager(view);
        this.recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        this.progressBarRelativeLayout = view.findViewById(R.id.progress_bar_linear_layout);
        this.homeSectionLayout = view.findViewById(R.id.swipe_to_refresh_home);
        this.headlinesSectionLayout = view.findViewById(R.id.swipe_to_refresh_headlines);
    }

    public void loadNewsCards(final String section, final boolean isSwipeToRefresh) {
        newsCardModelList = new ArrayList<>();
        String url = Constants.NODE_APP_BASE_URL + this.getGuardianUrl(section);
        displayLoadingBar(section, isSwipeToRefresh);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            newsCardModelList = parseNewsResults(response, section);
                            if (Constants.HOME.equals(section)) {
                                layoutManager = new LinearLayoutManager(view.getContext()) {
                                    @Override
                                    public boolean canScrollVertically() {
                                        return false;
                                    }
                                };
                            } else {
                                layoutManager = new LinearLayoutManager(view.getContext());
                            }
                            hideLoadingBar(section, isSwipeToRefresh);
                            recyclerView.setHasFixedSize(true);
                            adapter = new CardAdapter(newsCardModelList, onCardListener);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            System.out.println("Error processing the data from the news API");
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error loading data from News API");
                error.printStackTrace();
                Intent intent = new Intent(view.getContext(), ErrorActivity.class);
                view.getContext().startActivity(intent);
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
        requestQueue.add(jsonObjectRequest);
    }

    public static ArrayList<NewsCardModel> parseNewsResults(JSONObject response, String section) throws JSONException {
        ArrayList<NewsCardModel> newsCardModelList = new ArrayList<>();
        JSONArray results = response.getJSONObject("response").getJSONArray("results");
        for (int i = 0; i < results.length(); i++) {
            JSONObject current = results.getJSONObject(i);
            NewsCardModel newsCard = new NewsCardModel();
            if (Constants.HOME.equals(section)) {
                newsCard.setImageUri(current.getJSONObject("fields").getString("thumbnail"));
            } else {
                JSONArray elements = current.getJSONObject("blocks").getJSONObject("main").getJSONArray("elements");
                String imageUri = elements.getJSONObject(0).getJSONArray("assets").getJSONObject(0).getString("file");
                newsCard.setImageUri(imageUri);
            }
            newsCard.setSection(current.getString("sectionName"));
            newsCard.setTime(current.getString("webPublicationDate"));
            newsCard.setTitle(current.getString("webTitle"));
            newsCard.setId(current.getString("id"));
            newsCard.setWebUrl(current.getString("webUrl"));
            newsCard.setBookmarkTag(getBookmarkTag(newsCard.getId()));
            newsCardModelList.add(newsCard);
        }
        return newsCardModelList;
    }

    private void hideLoadingBar(final String section, final boolean isSwipeToRefresh) {
        if (!isSwipeToRefresh) {
            progressBarRelativeLayout.setVisibility(View.GONE);
            if (Constants.HOME.equals(section)) {
                homeSectionLayout.setVisibility(View.VISIBLE);
            } else {
                headlinesSectionLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    private void displayLoadingBar(final String section, final boolean isSwipeToRefresh) {
        if (!isSwipeToRefresh) {
            progressBarRelativeLayout.setVisibility(View.VISIBLE);
            if (Constants.HOME.equals(section)) {
                homeSectionLayout.setVisibility(View.GONE);
            } else {
                headlinesSectionLayout.setVisibility(View.GONE);
            }
        }
    }

    private String getGuardianUrl(String section) {
        String url = "";
        switch (section) {
            case Constants.HOME:
                url = Constants.GUARDIAN_HOME;
                break;
            case Constants.BUSINESS:
                url = Constants.GUARDIAN_BUSINESS;
                break;
            case Constants.POLITICS:
                url = Constants.GUARDIAN_POLITICS;
                break;
            case Constants.TECHNOLOGY:
                url = Constants.GUARDIAN_TECHNOLOGY;
                break;
            case Constants.SPORTS:
                url = Constants.GUARDIAN_SPORTS;
                break;
            case Constants.SCIENCE:
                url = Constants.GUARDIAN_SCIENCE;
                break;
            case Constants.WORLD:
            default:
                url = Constants.GUARDIAN_WORLD;
        }
        System.out.println("Current tab: " + url);
        return url;
    }

    private static int getBookmarkTag(String id) {
        return bookmarkManager.isBookmarked(id);
    }

    public ArrayList<NewsCardModel> getNewsCardModelList() {
        return newsCardModelList;
    }

    public void onResume() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}
