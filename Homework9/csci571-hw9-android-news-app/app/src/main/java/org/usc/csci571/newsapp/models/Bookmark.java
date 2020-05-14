package org.usc.csci571.newsapp.models;

public class Bookmark {
    private String id;
    private String imageUri;
    private String title;
    private String time;
    private String section;
    private String webUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    @Override
    public String toString() {
        return "Bookmark{" +
                "id='" + id + '\'' +
                ", imageUri='" + imageUri + '\'' +
                ", title='" + title + '\'' +
                ", time='" + time + '\'' +
                ", section='" + section + '\'' +
                ", webUrl='" + webUrl + '\'' +
                '}';
    }
}
