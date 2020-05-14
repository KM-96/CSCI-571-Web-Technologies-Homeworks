package org.usc.csci571.newsapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;

import org.usc.csci571.newsapp.R;
import org.usc.csci571.newsapp.activities.DetailCardActivity;
import org.usc.csci571.newsapp.activities.ErrorActivity;
import org.usc.csci571.newsapp.adapters.BookmarkAdapter;
import org.usc.csci571.newsapp.models.Bookmark;
import org.usc.csci571.newsapp.recyclerviews.BookmarkRecyclerView;
import org.usc.csci571.newsapp.utils.BookmarkManager;
import org.usc.csci571.newsapp.utils.Constants;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Bookmarks extends Fragment implements BookmarkAdapter.OnCardListener {

    private View view;
    private BookmarkManager bookmarkManager;
    private BookmarkRecyclerView recyclerView;
    private BookmarkRecyclerView.LayoutManager layoutManager;
    private BookmarkRecyclerView.Adapter adapter;
    private BookmarkAdapter.OnCardListener onCardListener;

    public Bookmarks() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.fragment_bookmarks, container, false);
        this.bookmarkManager = new BookmarkManager(this.view);
        this.recyclerView = this.view.findViewById(R.id.bookmarks_recycler_view);
        //Set Empty view for the adapter observer
        TextView noBookmarksTextView = this.view.findViewById(R.id.no_bookmarks);
        this.recyclerView.emptyRecyclerViewItems(noBookmarksTextView);
        this.recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        this.layoutManager = new GridLayoutManager(this.view.getContext(), Constants.BOOKMARK_COLUMN);
        loadBookmarks();
        return this.view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadBookmarks();
    }

    private void loadBookmarks() {
        try {
            List<Bookmark> bookmarks = this.bookmarkManager.getAllBookmarks();
            this.adapter = new BookmarkAdapter(bookmarks, this);
            this.recyclerView.setLayoutManager(this.layoutManager);
            this.recyclerView.setAdapter(this.adapter);
        } catch (Exception e) {
            System.out.println("Error loading bookmarks!");
            e.printStackTrace();
            Intent intent = new Intent(view.getContext(), ErrorActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onCardClick(int position) {
        Intent intent = new Intent(this.view.getContext(), DetailCardActivity.class);
        Bookmark bookmark = this.bookmarkManager.getAllBookmarks().get(position);
        intent.putExtra(Constants.ARTICLE_ID, bookmark.getId());
        intent.putExtra(Constants.ARTICLE_TITLE, bookmark.getTitle());
        intent.putExtra(Constants.ARTICLE_TAG, Constants.BOOKMARK_CHECKED);
        startActivity(intent);
    }
}
