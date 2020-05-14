package org.usc.csci571.newsapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.usc.csci571.newsapp.R;
import org.usc.csci571.newsapp.models.Bookmark;
import org.usc.csci571.newsapp.models.NewsCardModel;
import org.usc.csci571.newsapp.utils.BookmarkManager;
import org.usc.csci571.newsapp.utils.Constants;
import org.usc.csci571.newsapp.utils.Twitter;

public class DetailCardActivity extends AppCompatActivity {

    private String articleId;
    private String title;
    private int bookmarkTag;
    private RequestQueue requestQueue;
    private View view;
    private ImageView imageView;
    private TextView titleTextView;
    private TextView descriptionTextView;
    private TextView dateTextView;
    private TextView sectionTextView;
    private TextView viewFullArticle;
    private Menu menu;
    private NewsCardModel newsCardModel;
    private BookmarkManager bookmarkManager;
    private LinearLayout progressBarRelativeLayout;
    private ScrollView detailActivityLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            setTheme(R.style.AppTheme);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_detail);

            Intent intent = getIntent();
            this.articleId = intent.getStringExtra(Constants.ARTICLE_ID);
            this.title = intent.getStringExtra(Constants.ARTICLE_TITLE);
            this.bookmarkTag = intent.getIntExtra(Constants.ARTICLE_TAG, Constants.BOOKMARK_UNCHECKED);

            init();

            loadDetailedCard();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        initToolbar();
        newsCardModel = new NewsCardModel();
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.BOOKMARKS_SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        this.bookmarkManager = new BookmarkManager(sharedPreferences);
        this.view = findViewById(R.id.detail_card_relative_layout);
        this.imageView = findViewById(R.id.detail_image);
        this.titleTextView = findViewById(R.id.detail_title);
        this.sectionTextView = findViewById(R.id.detail_section);
        this.dateTextView = findViewById(R.id.detail_date);
        this.descriptionTextView = findViewById(R.id.detail_description);
        this.viewFullArticle = findViewById(R.id.view_full_article);
        this.progressBarRelativeLayout = findViewById(R.id.progress_bar_linear_layout);
        this.detailActivityLayout = findViewById(R.id.detail_card_scroll_view);
        this.requestQueue = Volley.newRequestQueue(this);
    }


    private void initToolbar() {
        //Add the toolbar to the setSupportActionBar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.detail_page_toolbar);
        myToolbar.setTitle(this.title);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    private void loadDetailedCard() {
        String url = Constants.NODE_APP_BASE_URL + Constants.GUARDIAN_DETAILED_ARTICLE + "?" + Constants.ARTICLE_ID_QUERY_PARAM + this.articleId;
        displayLoadingBar();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            newsCardModel.setId(response.getString("id"));
                            newsCardModel.setTitle(response.getString("webTitle"));
                            newsCardModel.setTime(response.getString("webPublicationDate"));
                            JSONArray mainElements = response.getJSONObject("blocks").getJSONObject("main").getJSONArray("elements");
                            String imageUri = mainElements.getJSONObject(0).getJSONArray("assets").getJSONObject(0).getString("file");
                            newsCardModel.setImageUri(imageUri);
                            newsCardModel.setSection(response.getString("sectionName"));
                            newsCardModel.setWebUrl(response.getString("webUrl"));
                            JSONArray bodyElements = response.getJSONObject("blocks").getJSONArray("body");
                            StringBuilder description = new StringBuilder();
                            for (int i = 0; i < bodyElements.length(); i++) {
                                JSONObject current = bodyElements.getJSONObject(i);
                                if (current.has("bodyHtml")) {
                                    description.append(current.getString("bodyHtml"));
                                }
                            }
                            newsCardModel.setDescription(description.toString());
                            hideLoadingBar();
                            populateView(newsCardModel);
                        } catch (JSONException e) {
                            System.out.println("Error processing the data of the detailed article from server");
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error loading detailed article from the server");
                error.printStackTrace();
                Intent intent = new Intent(view.getContext(), ErrorActivity.class);
                startActivity(intent);
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

    private void displayLoadingBar() {
        this.progressBarRelativeLayout.setVisibility(View.VISIBLE);
        this.detailActivityLayout.setVisibility(View.GONE);
        if (this.menu != null) {
            if (this.menu.findItem(R.id.bookmarked_border_image) != null) {
                this.menu.findItem(R.id.bookmarked_border_image).setVisible(false);
            }
            if (this.menu.findItem(R.id.bookmarked_filled_image) != null) {
                this.menu.findItem(R.id.bookmarked_filled_image).setVisible(false);
            }
            if (this.menu.findItem(R.id.twitter_image) != null) {
                this.menu.findItem(R.id.twitter_image).setVisible(false);
            }
        }
    }

    private void hideLoadingBar() {
        this.progressBarRelativeLayout.setVisibility(View.GONE);
        this.detailActivityLayout.setVisibility(View.VISIBLE);

        if (this.bookmarkTag == Constants.BOOKMARK_UNCHECKED) {
            this.menu.findItem(R.id.bookmarked_border_image).setVisible(true);
            this.menu.findItem(R.id.bookmarked_filled_image).setVisible(false);
        } else {
            this.menu.findItem(R.id.bookmarked_border_image).setVisible(false);
            this.menu.findItem(R.id.bookmarked_filled_image).setVisible(true);
        }
        this.menu.findItem(R.id.twitter_image).setVisible(true);
    }

    private void populateView(NewsCardModel newsCardModel) {
        Picasso.with(this).load(newsCardModel.getImageUri()).fit().into(this.imageView);
        this.titleTextView.setText(newsCardModel.getTitle());
        this.sectionTextView.setText(newsCardModel.getSection());
        this.dateTextView.setText(newsCardModel.getDetailedArticleTimeFormat());
        this.descriptionTextView.setText(Html.fromHtml(newsCardModel.getDescription()));
        String anchor = "<a href='" + newsCardModel.getWebUrl() + "'> View Full Article </a>";
        this.viewFullArticle.setText(Html.fromHtml(anchor));
        this.viewFullArticle.setTextColor(Color.parseColor(Constants.DARK_GRAY));
        this.viewFullArticle.setClickable(true);
        this.viewFullArticle.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.detail_card_toolbar_menu, menu);

        this.menu.findItem(R.id.bookmarked_filled_image).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                updateBookmarks();
                return true;
            }
        });
        this.menu.findItem(R.id.bookmarked_border_image).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                updateBookmarks();
                return true;
            }
        });
        this.menu.findItem(R.id.twitter_image).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Twitter.launchActivity(view, newsCardModel.getWebUrl());
                return true;
            }
        });
        return true;
    }

    private void updateBookmarks() {
        if (this.menu.findItem(R.id.bookmarked_filled_image).isVisible()) {
            String text = "'" + titleTextView.getText() + "' was removed from Bookmarks";
            removeBookmark(newsCardModel.getId().toString());
            Toast.makeText(this, text, Toast.LENGTH_LONG).show();
            this.menu.findItem(R.id.bookmarked_filled_image).setVisible(false);
            this.menu.findItem(R.id.bookmarked_border_image).setVisible(true);
        } else {
            String text = "'" + newsCardModel.getTitle() + "' was added to Bookmarks";
            addBookmark();
            Toast.makeText(this, text, Toast.LENGTH_LONG).show();
            this.menu.findItem(R.id.bookmarked_filled_image).setVisible(true);
            this.menu.findItem(R.id.bookmarked_border_image).setVisible(false);
        }
    }

    private void addBookmark() {
        Bookmark bookmark = new Bookmark();
        bookmark.setId(newsCardModel.getId());
        bookmark.setImageUri(newsCardModel.getImageUri());
        bookmark.setSection(newsCardModel.getSection());
        bookmark.setTime(newsCardModel.getBookmarkTimeFormat());
        bookmark.setTitle(newsCardModel.getTitle());
        bookmark.setWebUrl(newsCardModel.getWebUrl());
        bookmarkManager.add(bookmark);
    }

    private void removeBookmark(String id) {
        bookmarkManager.remove(id);
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
                this.finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}