package org.usc.csci571.newsapp.fragments;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.usc.csci571.newsapp.R;
import org.usc.csci571.newsapp.activities.DetailCardActivity;
import org.usc.csci571.newsapp.activities.ErrorActivity;
import org.usc.csci571.newsapp.adapters.CardAdapter;
import org.usc.csci571.newsapp.api.NewsApi;
import org.usc.csci571.newsapp.models.NewsCardModel;
import org.usc.csci571.newsapp.models.OpenWeatherModel;
import org.usc.csci571.newsapp.utils.BookmarkManager;
import org.usc.csci571.newsapp.utils.Constants;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment implements CardAdapter.OnCardListener {

    private View view;
    private RequestQueue requestQueue;
    private RecyclerView recyclerView;
    private NewsApi newsApi;
    private SwipeRefreshLayout swipeRefreshLayout;
    private BookmarkManager bookmarkManager;
    private Location location;
    private boolean isSwipeToRefresh = false;

    public Home(final Location location) {
        this.location = location;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            // Inflate the layout for this fragment
            this.view = inflater.inflate(R.layout.fragment_home_tab_news, container, false);

            init();

            this.bookmarkManager = new BookmarkManager(view);

            //Initialize Volley
            this.requestQueue = Volley.newRequestQueue(getContext());

            //Initialize news API
            this.newsApi = new NewsApi(this.view, this.recyclerView, this);

            //Load Weather Data
            loadWeatherData(isSwipeToRefresh);

            //Load News Cards
            loadCardData(isSwipeToRefresh);

            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    isSwipeToRefresh = true;
                    loadWeatherData(isSwipeToRefresh);
                    loadCardData(isSwipeToRefresh);
                    swipeRefreshLayout.setRefreshing(false);
                    isSwipeToRefresh = false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.view;
    }

    private void init() {
        swipeRefreshLayout = view.findViewById(R.id.swipe_to_refresh_home);
        recyclerView = view.findViewById(R.id.news_recycler_view);
    }

    private void loadCardData(boolean isSwipeToRefresh) {
        newsApi.loadNewsCards(Constants.HOME, isSwipeToRefresh);
    }

    private void loadWeatherData(boolean isSwipeToRefresh) {
        try {
            Geocoder geocoder = new Geocoder(view.getContext());
            final RelativeLayout relativeLayout = view.findViewById(R.id.weather_card_layout);
            if (location == null) {
                TextView defaultTextView = view.findViewById(R.id.default_weather_card);
                defaultTextView.setVisibility(View.VISIBLE);
                relativeLayout.setVisibility(View.GONE);
                return;
            }
            final List<Address> address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String url = Constants.OPEN_WEATHER_URL + Constants.Q_QUERY_PARAM + address.get(0).getLocality();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {

                                TextView cityTextView = view.findViewById(R.id.weather_city);
                                TextView stateTextView = view.findViewById(R.id.weather_state);
                                TextView tempTextView = view.findViewById(R.id.weather_temperature);
                                TextView mainTextView = view.findViewById(R.id.weather_main);

                                OpenWeatherModel model = new OpenWeatherModel();
                                JSONObject mainJsonObject = response.getJSONObject("main");
                                model.setTemperature(mainJsonObject.getString("temp"));

                                JSONArray weatherJsonArray = response.getJSONArray("weather");
                                JSONObject weatherJsonObject = weatherJsonArray.getJSONObject(0);
                                model.setMain(weatherJsonObject.getString("main"));

                                switch (model.getMain()) {
                                    case Constants.CLOUDS:
                                        relativeLayout.setBackgroundResource(R.drawable.cloudy_weather);
                                        break;
                                    case Constants.CLEAR:
                                        relativeLayout.setBackgroundResource(R.drawable.clear_weather);
                                        break;
                                    case Constants.SNOW:
                                        relativeLayout.setBackgroundResource(R.drawable.snowy_weather);
                                        break;
                                    case Constants.RAIN:
                                    case Constants.DRIZZLE:
                                        relativeLayout.setBackgroundResource(R.drawable.rainy_weather);
                                        break;
                                    case Constants.THUNDERSTORM:
                                        relativeLayout.setBackgroundResource(R.drawable.thunder_weather);
                                        break;
                                    default:
                                        relativeLayout.setBackgroundResource(R.drawable.sunny_weather);
                                }
                                cityTextView.setText(address.get(0).getLocality());
                                stateTextView.setText(address.get(0).getAdminArea());
                                tempTextView.setText(String.valueOf(model.getTemperature()));
                                mainTextView.setText(model.getMain());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("Error processing data from the weather API");
                    error.printStackTrace();
                }
            });

            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            System.out.println("Error loading weather data!");
            e.printStackTrace();
            Intent intent = new Intent(view.getContext(), ErrorActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onCardClick(int position) {
        Intent intent = new Intent(this.view.getContext(), DetailCardActivity.class);
        NewsCardModel currentArticle = newsApi.getNewsCardModelList().get(position);
        intent.putExtra(Constants.ARTICLE_ID, currentArticle.getId());
        intent.putExtra(Constants.ARTICLE_TITLE, currentArticle.getTitle());
        intent.putExtra(Constants.ARTICLE_TAG, bookmarkManager.isBookmarked(currentArticle.getId()));
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        newsApi.onResume();
    }
}