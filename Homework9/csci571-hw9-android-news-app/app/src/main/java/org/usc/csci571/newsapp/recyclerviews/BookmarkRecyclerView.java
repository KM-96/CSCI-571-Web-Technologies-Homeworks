package org.usc.csci571.newsapp.recyclerviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BookmarkRecyclerView extends RecyclerView {

    private List<View> emptyRecyclerViewListItems = Collections.emptyList();

    private AdapterDataObserver adapterDataObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            toggleViews();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            toggleViews();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
            toggleViews();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            toggleViews();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            toggleViews();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            toggleViews();
        }
    };

    private void toggleViews() {
        if (getAdapter() != null) {
            if (getAdapter().getItemCount() == 0) {
                setVisibility(View.GONE);
                for (View view : this.emptyRecyclerViewListItems) {
                    view.setVisibility(VISIBLE);
                }
            } else {
                setVisibility(View.VISIBLE);
                for (View view : this.emptyRecyclerViewListItems) {
                    view.setVisibility(GONE);
                }
            }
        }
    }

    public BookmarkRecyclerView(@NonNull Context context) {
        super(context);
    }

    public BookmarkRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BookmarkRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void emptyRecyclerViewItems(View... views) {
        this.emptyRecyclerViewListItems = Arrays.asList(views);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(this.adapterDataObserver);
        }
        this.adapterDataObserver.onChanged();
    }
}
