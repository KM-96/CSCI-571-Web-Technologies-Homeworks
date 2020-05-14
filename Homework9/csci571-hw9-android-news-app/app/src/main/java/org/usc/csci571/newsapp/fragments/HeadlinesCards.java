package org.usc.csci571.newsapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.usc.csci571.newsapp.R;
import org.usc.csci571.newsapp.activities.DetailCardActivity;
import org.usc.csci571.newsapp.adapters.CardAdapter;
import org.usc.csci571.newsapp.api.NewsApi;
import org.usc.csci571.newsapp.models.NewsCardModel;
import org.usc.csci571.newsapp.utils.BookmarkManager;
import org.usc.csci571.newsapp.utils.Constants;

public class HeadlinesCards extends Fragment implements CardAdapter.OnCardListener {

    private String tab;
    private View view;
    private RecyclerView recyclerView;
    private NewsApi newsApi;
    private SwipeRefreshLayout swipeRefreshLayout;
    private BookmarkManager bookmarkManager;
    private boolean isSwipeToRefresh = false;

    public HeadlinesCards(String tab) {
        this.tab = tab;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_headlines_cards_layout, container, false);
        recyclerView = view.findViewById(R.id.headlines_recycler_view);
        newsApi = new NewsApi(this.view, this.recyclerView, this);
        swipeRefreshLayout = view.findViewById(R.id.swipe_to_refresh_headlines);
        bookmarkManager = new BookmarkManager(view);
        loadCardData(view, isSwipeToRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isSwipeToRefresh = true;
                loadCardData(view, isSwipeToRefresh);
                swipeRefreshLayout.setRefreshing(false);
                isSwipeToRefresh = false;
            }
        });
        return view;
    }

    private void loadCardData(final View view, final boolean isSwipeToRefresh) {
        newsApi.loadNewsCards(this.tab, isSwipeToRefresh);
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