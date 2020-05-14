package org.usc.csci571.newsapp.models;

import android.os.Build;

import org.usc.csci571.newsapp.utils.Calender;
import org.usc.csci571.newsapp.utils.Constants;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class NewsCardModel {
    private String id;
    private String imageUri;
    private String title;
    private String time;
    private String webPublicationDate;
    private String section;
    private int bookmarkTag;
    private String webUrl;
    private ZoneId zoneId;
    private String description;

    public NewsCardModel() {
        this.bookmarkTag = Constants.BOOKMARK_UNCHECKED;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            zoneId = ZoneId.of(Constants.TIME_ZONE_AMERICA_LA);
        }
    }

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
        this.webPublicationDate = time;
        this.time = convertToTimeString(time);
    }

    private String convertToTimeString(String time) {
        String timeString = "";
        ZonedDateTime localDateTime = null;
        ZonedDateTime zonedDateTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            localDateTime = ZonedDateTime.now(zoneId);
            zonedDateTime = ZonedDateTime.parse(time).withZoneSameInstant(zoneId);

            long numberOfDays = ChronoUnit.DAYS.between(zonedDateTime, localDateTime);
            long numberOfHours = ChronoUnit.HOURS.between(zonedDateTime, localDateTime);
            long numberOfMinutes = ChronoUnit.MINUTES.between(zonedDateTime, localDateTime);
            long numberOfSeconds = ChronoUnit.SECONDS.between(zonedDateTime, localDateTime);

            if (numberOfDays > 0) {
                timeString = numberOfDays + "d ago";
            } else if (numberOfHours > 0) {
                timeString = numberOfHours + "h ago";
            } else if (numberOfMinutes > 0) {
                timeString = numberOfMinutes + "m ago";
            } else if (numberOfSeconds > 0) {
                timeString = numberOfSeconds + "s ago";
            }
        }
        return timeString;
    }

    public String getBookmarkTimeFormat() {
        String bookmarkFormatTimeString = "";
        ZonedDateTime zonedDateTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            zonedDateTime = ZonedDateTime.parse(this.webPublicationDate).withZoneSameInstant(zoneId);
            int day = zonedDateTime.getDayOfMonth();
            String month = Calender.getMonthName(zonedDateTime.getMonth().getValue());
            bookmarkFormatTimeString = day + " " + month;
        }
        return bookmarkFormatTimeString;
    }

    public String getDetailedArticleTimeFormat() {
        String detailedArticleTimeString = "";
        ZonedDateTime zonedDateTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            zonedDateTime = ZonedDateTime.parse(this.webPublicationDate).withZoneSameInstant(zoneId);
            int day = zonedDateTime.getDayOfMonth();
            String dayString = (day < 10) ? "0" + day : String.valueOf(day);
            String month = Calender.getMonthName(zonedDateTime.getMonth().getValue());
            detailedArticleTimeString = dayString + " " + month + " " + zonedDateTime.getYear();
        }
        return detailedArticleTimeString;
    }


    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public int getBookmarkTag() {
        return bookmarkTag;
    }

    public void setBookmarkTag(int bookmarkTag) {
        this.bookmarkTag = bookmarkTag;
    }


    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "NewsCardModel{" +
                "id='" + id + '\'' +
                ", imageUri='" + imageUri + '\'' +
                ", title='" + title + '\'' +
                ", time='" + time + '\'' +
                ", webPublicationDate='" + webPublicationDate + '\'' +
                ", section='" + section + '\'' +
                ", bookmarkTag=" + bookmarkTag +
                ", webUrl='" + webUrl + '\'' +
                '}';
    }
}