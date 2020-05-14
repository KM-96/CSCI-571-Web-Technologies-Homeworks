package org.usc.csci571.newsapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.usc.csci571.newsapp.activities.ErrorActivity;
import org.usc.csci571.newsapp.activities.SearchActivity;
import org.usc.csci571.newsapp.adapters.AutoSuggestAdapter;
import org.usc.csci571.newsapp.api.AutoSuggestApi;
import org.usc.csci571.newsapp.fragments.Bookmarks;
import org.usc.csci571.newsapp.fragments.Headlines;
import org.usc.csci571.newsapp.fragments.Home;
import org.usc.csci571.newsapp.fragments.Trending;
import org.usc.csci571.newsapp.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private LocationManager locationManager;
    private Location location;
    private Handler handler;
    private AutoSuggestAdapter autoSuggestAdapter;
    private String queryString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            //Display the splash screen
            setTheme(R.style.AppTheme);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            getLocation();
            //Add the toolbar to the setSupportActionBar
            Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(myToolbar);

            //Functionality for bottom navigation tab
            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_bar);
            bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemReselectedListener);

        } catch (Exception e) {
            System.out.println("There is an error starting the application!");
            e.printStackTrace();
            Intent intent = new Intent(this, ErrorActivity.class);
            startActivity(intent);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemReselectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            selectedFragment = new Home(location);
                            break;
                        case R.id.navigation_headlines:
                            selectedFragment = new Headlines();
                            break;
                        case R.id.navigation_trending:
                            selectedFragment = new Trending();
                            break;
                        case R.id.navigation_bookmarks:
                            selectedFragment = new Bookmarks();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.news_container, selectedFragment)
                            .addToBackStack(null).commit();
                    return true;
                }
            };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_toolbar_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search_button);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        final SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) searchView.findViewById(R.id.search_src_text);
        this.autoSuggestAdapter = new AutoSuggestAdapter(this, android.R.layout.simple_dropdown_item_1line);
        searchAutoComplete.setAdapter(this.autoSuggestAdapter);
        searchAutoComplete.setThreshold(Constants.NUMBER_OF_LETTERS_BEFORE_SEARCH_QUERY_FIRES);

        // Listen to search view item on click event.
        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long id) {
                queryString = (String) adapterView.getItemAtPosition(itemIndex);
                searchAutoComplete.setText("" + queryString);
            }
        });

        searchAutoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int
                    count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                handler.removeMessages(Constants.TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(Constants.TRIGGER_AUTO_COMPLETE,
                        Constants.AUTO_COMPLETE_DELAY);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                if (queryString != null && queryString.length() > 0) {
                    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                    intent.putExtra(Constants.QUERY_STRING, queryString);
                    startActivity(intent);
                }
                if (autoSuggestAdapter != null) {
                    autoSuggestAdapter.setData(new ArrayList<String>());
                    autoSuggestAdapter.notifyDataSetChanged();
                }
                return false;
            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == Constants.TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(searchAutoComplete.getText())) {
                        callBingAutoSuggestApi(searchAutoComplete.getText().toString());
                    }
                }
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setLocation();
                    getSupportFragmentManager().beginTransaction().replace(R.id.news_container,
                            new Home(this.location)).addToBackStack(null).commit();
                }
            default:
                getSupportFragmentManager().beginTransaction().replace(R.id.news_container,
                        new Home(this.location)).addToBackStack(null).commit();
        }
    }

    private void callBingAutoSuggestApi(String text) {
        AutoSuggestApi.make(this, text, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //parsing logic, please change it as per your requirement
                List<String> stringList = new ArrayList<>();
                try {
                    System.out.println(response);
                    JSONObject responseObject = new JSONObject(response);
                    JSONArray array = responseObject.getJSONArray("suggestionGroups").getJSONObject(0).getJSONArray("searchSuggestions");

                    for (int i = 0; i < Math.min(array.length(), Constants.DEFAULT_NUMBER_OF_SEARCH_SUGGESTIONS); i++) {
                        JSONObject row = array.getJSONObject(i);
                        stringList.add(row.getString("displayText"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //IMPORTANT: set data here and notify
                autoSuggestAdapter.setData(stringList);
                autoSuggestAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error getting data from Bing API");
                error.printStackTrace();
                Intent intent = new Intent(MainActivity.this, ErrorActivity.class);
                startActivity(intent);
            }
        });
    }


    private void getLocation() {
        System.out.println("Trying to fetch location!");
        if (!canAccessLocation()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                }, 1);
            }
            setLocation();
        } else {
            setLocation();
            getSupportFragmentManager().beginTransaction().replace(R.id.news_container,
                    new Home(this.location)).addToBackStack(null).commit();
        }
    }

    private void setLocation() {
        try {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true));

            if (canAccessLocation()) {
                System.out.println("Location access is enabled! Fetching the actual location");
                //You can still do this if you like, you might get lucky:
                Location location = locationManager.getLastKnownLocation(bestProvider);
                if (location != null) {
                    this.location = location;
                } else {
                    locationManager.requestLocationUpdates(bestProvider, 1000, 0, this);
                    this.location = locationManager.getLastKnownLocation(bestProvider);
                }
            }
        } catch (Exception e) {
            System.out.println("There is a problem setting the location");
            e.printStackTrace();
        }
    }

    private boolean canAccessLocation() {
        return (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) && hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    private boolean hasPermission(String perm) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return (PackageManager.PERMISSION_GRANTED == checkSelfPermission(perm));
        }
        return false;
    }
}