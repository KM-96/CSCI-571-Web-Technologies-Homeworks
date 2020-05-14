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
import org.usc.csci571.newsapp.models.NewsCardModel;
import org.usc.csci571.newsapp.utils.BookmarkManager;
import org.usc.csci571.newsapp.utils.Constants;
import org.usc.csci571.newsapp.utils.Twitter;

import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {
    private OnCardListener onCardListener;
    private ArrayList<NewsCardModel> newsCardModelList;
    private View view;
    private static BookmarkManager bookmarkManager;

    public static class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        NewsCardModel newsCardModel;
        View view;
        TextView idView;
        ImageView imageView;
        TextView titleTextView;
        TextView dateTextView;
        TextView sectionTextView;
        ImageView bookmarkImageView;
        OnCardListener onCardListener;
        String webUrl;

        public CardViewHolder(@NonNull final View itemView, OnCardListener onCardListener) {
            super(itemView);
            view = itemView;
            this.onCardListener = onCardListener;
            idView = itemView.findViewById(R.id.news_id);
            imageView = itemView.findViewById(R.id.news_image);
            imageView.setOnClickListener(null);
            titleTextView = itemView.findViewById(R.id.news_title);
            dateTextView = itemView.findViewById(R.id.news_time);
            sectionTextView = itemView.findViewById(R.id.news_section);
            bookmarkImageView = itemView.findViewById(R.id.news_bookmark_image);
            bookmarkImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateBookmarks(bookmarkImageView);
                }
            });

            itemView.setOnClickListener(this);

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final Dialog dialog = new Dialog(view.getContext());
                    dialog.setContentView(R.layout.news_article_dialog);
                    dialog.setTitle(titleTextView.getText());

                    final ImageView dialogImage = (ImageView) dialog.findViewById(R.id.dialog_image);
                    dialogImage.setImageDrawable(imageView.getDrawable());

                    final TextView dialogText = (TextView) dialog.findViewById(R.id.dialog_title);
                    dialogText.setText(titleTextView.getText());

                    final ImageView dialogTwitterImage = (ImageView) dialog.findViewById(R.id.dialog_twitter_image);
                    dialogTwitterImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Twitter.launchActivity(v, webUrl);
                        }
                    });

                    final ImageView dialogBookmarkImage = (ImageView) dialog.findViewById(R.id.dialog_bookmark_image);
                    initializeDialogBookmark(dialogBookmarkImage);
                    dialogBookmarkImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            updateBookmarks(dialogBookmarkImage);
                            updateBookmarks(bookmarkImageView);
                        }
                    });
                    dialog.show();
                    return true;
                }
            });

        }


        private void initializeDialogBookmark(ImageView imageView) {
            if (bookmarkImageView.getTag() == null) {
                imageView.setImageResource(R.drawable.ic_bookmark_border_news_card);
                imageView.setTag(Constants.BOOKMARK_UNCHECKED);
            } else if ((int) bookmarkImageView.getTag() == Constants.BOOKMARK_UNCHECKED) {
                imageView.setImageResource(R.drawable.ic_bookmark_border_news_card);
                imageView.setTag(Constants.BOOKMARK_UNCHECKED);
            } else {
                imageView.setImageResource(R.drawable.ic_bookmark_filled_news_card);
                imageView.setTag(Constants.BOOKMARK_CHECKED);
            }
        }

        private void updateBookmarks(ImageView bookmarkImageView) {
            if ((int) bookmarkImageView.getTag() == Constants.BOOKMARK_UNCHECKED) {
                String text = "'" + titleTextView.getText() + "' was added to Bookmarks";
                addBookmark();
                Toast.makeText(itemView.getContext(), text, Toast.LENGTH_LONG).show();
                bookmarkImageView.setImageResource(R.drawable.ic_bookmark_filled_news_card);
                bookmarkImageView.setTag(Constants.BOOKMARK_CHECKED);
            } else {
                String text = "'" + titleTextView.getText() + "' was removed from Bookmarks";
                removeBookmark(idView.getText().toString());
                Toast.makeText(itemView.getContext(), text, Toast.LENGTH_LONG).show();
                bookmarkImageView.setImageResource(R.drawable.ic_bookmark_border_news_card);
                bookmarkImageView.setTag(Constants.BOOKMARK_UNCHECKED);
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
        public void onClick(View v) {
            onCardListener.onCardClick(getAdapterPosition());
        }
    }

    public CardAdapter(ArrayList<NewsCardModel> newsCardModelList, OnCardListener onCardListener) {
        this.newsCardModelList = newsCardModelList;
        this.onCardListener = onCardListener;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_card, parent, false);
        bookmarkManager = new BookmarkManager(view);
        return new CardViewHolder(view, onCardListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        NewsCardModel currentNewsCard = this.newsCardModelList.get(position);
        holder.newsCardModel = currentNewsCard;
        holder.idView.setText(currentNewsCard.getId());
        holder.titleTextView.setText(currentNewsCard.getTitle());
        holder.sectionTextView.setText(currentNewsCard.getSection());
        holder.dateTextView.setText(currentNewsCard.getTime());
        holder.webUrl = currentNewsCard.getWebUrl();
        int tag = bookmarkManager.isBookmarked(currentNewsCard.getId());
        if (tag == Constants.BOOKMARK_UNCHECKED) {
            holder.bookmarkImageView.setImageResource(R.drawable.ic_bookmark_border_news_card);
            holder.bookmarkImageView.setTag(Constants.BOOKMARK_UNCHECKED);
        } else {
            holder.bookmarkImageView.setImageResource(R.drawable.ic_bookmark_filled_news_card);
            holder.bookmarkImageView.setTag(Constants.BOOKMARK_CHECKED);
        }
        Picasso.with(holder.view.getContext()).load(currentNewsCard.getImageUri()).fit().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return this.newsCardModelList.size();
    }

    public interface OnCardListener {
        void onCardClick(int position);
    }

    public void onResume() {
        notifyDataSetChanged();
    }
}
