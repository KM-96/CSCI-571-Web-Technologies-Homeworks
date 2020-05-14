package org.usc.csci571.newsapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.usc.csci571.newsapp.models.Bookmark;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class BookmarkManager {
    private View view;
    private SharedPreferences sharedPreferences;
    private Gson gson;
    private SharedPreferences.Editor editor;

    public BookmarkManager(View view) {
        this.view = view;
        this.sharedPreferences = view.getContext().getSharedPreferences(Constants.BOOKMARKS_SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
        this.gson = new Gson();
    }

    public BookmarkManager(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        this.editor = sharedPreferences.edit();
        this.gson = new Gson();
    }

    public List<Bookmark> getAllBookmarks() {
        String json = sharedPreferences.getString(Constants.BOOKMARKS_KEY, null);
        Type type = new TypeToken<ArrayList<Bookmark>>() {
        }.getType();
        List<Bookmark> bookmarks = gson.fromJson(json, type);
        if (bookmarks == null) {
            bookmarks = Collections.emptyList();
        }
        return bookmarks;
    }

    public void add(Bookmark bookmark) {
        String json = sharedPreferences.getString(Constants.BOOKMARKS_KEY, null);
        Type type = new TypeToken<ArrayList<Bookmark>>() {
        }.getType();
        List<Bookmark> bookmarks = gson.fromJson(json, type);
        if (bookmarks == null || bookmarks.size() == 0) {
            bookmarks = new ArrayList<>();
        }
        if (isBookmarked(bookmark.getId()) == 0) {
            bookmarks.add(bookmark);
            String updatedJson = gson.toJson(bookmarks);
            editor.putString(Constants.BOOKMARKS_KEY, updatedJson);
            editor.apply();
        }
    }

    public int isBookmarked(String id) {
        String json = sharedPreferences.getString(Constants.BOOKMARKS_KEY, null);
        Type type = new TypeToken<ArrayList<Bookmark>>() {
        }.getType();
        List<Bookmark> bookmarks = gson.fromJson(json, type);
        if (bookmarks != null) {
            for (Bookmark bookmark : bookmarks) {
                if (bookmark.getId().equals(id)) {
                    return Constants.BOOKMARK_CHECKED;
                }
            }
        }
        return Constants.BOOKMARK_UNCHECKED;
    }

    public void remove(Bookmark bookmark) {
        if (bookmark != null) {
            this.remove(bookmark.getId());
        }
    }

    public void remove(String id) {
        List<Bookmark> bookmarks = getAllBookmarks();
        Iterator iterator = bookmarks.iterator();
        while (iterator.hasNext()) {
            Bookmark current = (Bookmark) iterator.next();
            if (id.equals(current.getId())) {
                iterator.remove();
            }
        }
        String updatedJson = gson.toJson(bookmarks);
        editor.putString(Constants.BOOKMARKS_KEY, updatedJson);
        editor.apply();
    }
}