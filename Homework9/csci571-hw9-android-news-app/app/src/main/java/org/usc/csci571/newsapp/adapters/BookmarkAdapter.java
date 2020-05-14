package org.usc.csci571.newsapp.adapters;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.usc.csci571.newsapp.R;
import org.usc.csci571.newsapp.models.Bookmark;
import org.usc.csci571.newsapp.utils.BookmarkManager;
import org.usc.csci571.newsapp.utils.Twitter;

import java.util.Iterator;
import java.util.List;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.BookmarkViewHolder> {

    private List<Bookmark> bookmarks;
    private View view;
    private BookmarkManager bookmarkManager;
    private BookmarkAdapter.OnCardListener onCardListener;

    public BookmarkAdapter(List<Bookmark> bookmarks, BookmarkAdapter.OnCardListener onCardListener) {
        this.bookmarks = bookmarks;
        this.onCardListener = onCardListener;
    }

    @NonNull
    @Override
    public BookmarkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmarks_card, parent, false);
        this.bookmarkManager = new BookmarkManager(view);
        return new BookmarkViewHolder(this.view, onCardListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final BookmarkViewHolder holder, final int position) {
        final Bookmark bookmark = this.bookmarks.get(position);
        holder.idView.setText(bookmark.getId());
        Picasso.with(this.view.getContext()).load(bookmark.getImageUri()).fit().into(holder.imageView);
        holder.titleTextView.setText(bookmark.getTitle());
        holder.dateTextView.setText(bookmark.getTime());
        holder.sectionTextView.setText(bookmark.getSection());

        holder.bookmarkImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBookmarks(bookmark.getTitle(), bookmark.getId());
            }
        });

        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dialog = new Dialog(view.getContext());
                dialog.setContentView(R.layout.news_article_dialog);
                dialog.setTitle(holder.titleTextView.getText());

                final ImageView dialogImage = (ImageView) dialog.findViewById(R.id.dialog_image);
                dialogImage.setImageDrawable(holder.imageView.getDrawable());

                final TextView dialogText = (TextView) dialog.findViewById(R.id.dialog_title);
                dialogText.setText(holder.titleTextView.getText());

                final ImageView dialogTwitterImage = (ImageView) dialog.findViewById(R.id.dialog_twitter_image);
                dialogTwitterImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Twitter.launchActivity(v, bookmark.getWebUrl());
                    }
                });

                final ImageView dialogBookmarkImage = (ImageView) dialog.findViewById(R.id.dialog_bookmark_image);
                dialogBookmarkImage.setImageResource(R.drawable.ic_bookmark_filled_news_card);

                dialogBookmarkImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        updateBookmarks(bookmark.getTitle(), bookmark.getId());
                    }
                });
                dialog.show();
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.bookmarks.size();
    }

    public static class BookmarkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View view;
        TextView idView;
        ImageView imageView;
        TextView titleTextView;
        TextView dateTextView;
        TextView sectionTextView;
        ImageView bookmarkImageView;
        OnCardListener onCardListener;

        public BookmarkViewHolder(@NonNull View itemView, OnCardListener onCardListener) {
            super(itemView);
            view = itemView;
            itemView.setOnClickListener(this);
            idView = itemView.findViewById(R.id.bookmark_id);
            imageView = itemView.findViewById(R.id.bookmark_image);
            titleTextView = itemView.findViewById(R.id.bookmark_title);
            dateTextView = itemView.findViewById(R.id.bookmark_date);
            sectionTextView = itemView.findViewById(R.id.bookmark_section);
            bookmarkImageView = itemView.findViewById(R.id.image);
            this.onCardListener = onCardListener;
        }

        @Override
        public void onClick(View v) {
            onCardListener.onCardClick(getAdapterPosition());
        }
    }

    private void updateBookmarks(String title, String id) {
        String text = "'" + title + "' was removed from Bookmarks";
        bookmarkManager.remove(id);
        Iterator iterator = this.bookmarks.iterator();
        while (iterator.hasNext()) {
            Bookmark current = (Bookmark) iterator.next();
            if (current.getId().equals(id)) {
                iterator.remove();
                break;
            }
        }
        Toast.makeText(view.getContext(), text, Toast.LENGTH_LONG).show();
        notifyDataSetChanged();
    }

    public interface OnCardListener {
        void onCardClick(int position);
    }
}
