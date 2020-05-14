package org.usc.csci571.newsapp.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.usc.csci571.newsapp.R;
import org.usc.csci571.newsapp.activities.ErrorActivity;
import org.usc.csci571.newsapp.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Trending extends Fragment {

    private View view;
    private RequestQueue requestQueue;
    private EditText searchTextView;

    public Trending() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.fragment_trending, container, false);
        init();
        loadTrendingData();
        return view;
    }

    private void init() {
        this.requestQueue = Volley.newRequestQueue(view.getContext());
        this.searchTextView = view.findViewById(R.id.search_term);
        this.searchTextView.setOnEditorActionListener(this.onEditorActionListener);
    }

    private TextView.OnEditorActionListener onEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            loadTrendingData();
            return true;
        }
    };

    private void loadTrendingData() {
        final String search = this.searchTextView.getText() != null && !this.searchTextView.getText().toString().isEmpty()
                ? this.searchTextView.getText().toString() : Constants.DEFAULT_SEARCH_TERM_FOR_TRENDING;
        String url = Constants.NODE_APP_BASE_URL + Constants.GOOGLE_TRENDS_API + search;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray results = response.getJSONObject("default").getJSONArray("timelineData");
                            List<Entry> list = new ArrayList<>();
                            for (int i = 0; i < results.length(); i++) {
                                list.add(new Entry(i, results.getJSONObject(i).getJSONArray("value").getInt(0)));
                            }
                            LineDataSet dataSet = new LineDataSet(list, "Trending chart for " + search);
                            dataSet.setValueTextSize(10f);
                            dataSet.setColor(Color.parseColor("#6200EE"));
                            dataSet.setCircleColor(Color.parseColor("#6200EE"));
                            dataSet.setCircleHoleColor(Color.parseColor("#6200EE"));
                            List<ILineDataSet> dataSets = new ArrayList<>();
                            dataSets.add(dataSet);
                            LineData data = new LineData(dataSets);
                            LineChart chart = (LineChart) view.findViewById(R.id.line_chart);
                            chart.getAxisLeft().setDrawAxisLine(false);
                            chart.getAxisLeft().setDrawGridLines(false);
                            chart.getXAxis().setDrawGridLines(false);
                            chart.getAxisRight().setDrawGridLines(false);
                            chart.setData(data);
                            chart.invalidate();
                            styleLegend(chart);
                        } catch (JSONException e) {
                            System.out.println("Error parsing response from trending API");
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error loading data from trending API");
                error.printStackTrace();
                Intent intent = new Intent(view.getContext(), ErrorActivity.class);
                startActivity(intent);
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void styleLegend(LineChart chart) {
        Legend legend = chart.getLegend();
        legend.setWordWrapEnabled(true);
        legend.setEnabled(true);
        legend.setTextSize(18f);
        legend.setYOffset(18f);
        legend.setTextColor(Color.BLACK);
    }
}