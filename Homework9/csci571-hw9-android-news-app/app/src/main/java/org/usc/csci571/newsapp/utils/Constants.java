package org.usc.csci571.newsapp.utils;

public final class Constants {
    //Open weather API related constants
    public static final String OPEN_WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather" +
            "?units=metric&appid=94ca7e20398ce666e15fa855a174fdaf&";

    //Open Weather summary
    public static final String CLOUDS = "Clouds";
    public static final String CLEAR = "Clear";
    public static final String SNOW = "Snow";
    public static final String RAIN = "Rain";
    public static final String DRIZZLE = "Drizzle";
    public static final String THUNDERSTORM = "Thunderstorm";
    public static final String DEGREE_CENTIGRADE = " \u00B0" + "C";

    //Weather card type for home page
    public static final int WEATHER_CARD_TYPE = 1;

    //Generic query parameters
    public static final String Q_QUERY_PARAM = "q=";

    //Node Application related Constants
    public static final String NODE_APP_BASE_URL = "https://csci-571-hw9-node-app-km69564.wl.r.appspot.com/";

    //Guardian APIs
    public static final String GUARDIAN_HOME = "guardian-news/articles/home";
    public static final String GUARDIAN_WORLD = "guardian-news/articles/world";
    public static final String GUARDIAN_BUSINESS = "guardian-news/articles/business";
    public static final String GUARDIAN_POLITICS = "guardian-news/articles/politics";
    public static final String GUARDIAN_TECHNOLOGY = "guardian-news/articles/technology";
    public static final String GUARDIAN_SPORTS = "guardian-news/articles/sport";
    public static final String GUARDIAN_SCIENCE = "guardian-news/articles/science";
    public static final String GUARDIAN_DETAILED_ARTICLE = "guardian-news/articles";
    public static final String GUARDIAN_SEARCH = "guardian-news/search";
    public static final String ARTICLE_ID_QUERY_PARAM = "article_id=";

    //Google Trends API
    public static final String GOOGLE_TRENDS_API = "google-trends?search=";

    //Auto Suggest API
    public static final String BING_AUTO_SUGGEST_API = "https://krishna-manoj-maddipatla.cognitiveservices.azure.com/bing/v7.0/suggestions?q=";
    public static final String SUBSCRIPTION_KEY_HEADER = "Ocp-Apim-Subscription-Key";
    public static final String SUBSCRIPTION_KEY_VALUE = "28bb6aa85b734637b400611e40fcdd4f";
    public static final int TRIGGER_AUTO_COMPLETE = 1000;
    public static final long AUTO_COMPLETE_DELAY = 2000;

    //Bookmark checked and unchecked flags
    public static final int BOOKMARK_UNCHECKED = 0;
    public static final int BOOKMARK_CHECKED = 1;
    public static final int BOOKMARK_COLUMN = 2;

    //Headlines Tabs
    public static final String[] HEADLINES_TABS = {"WORLD", "BUSINESS", "POLITICS", "SPORTS", "TECHNOLOGY", "SCIENCE"};

    //Section tabs
    public static final String HOME = "HOME";
    public static final String WORLD = "WORLD";
    public static final String BUSINESS = "BUSINESS";
    public static final String POLITICS = "POLITICS";
    public static final String SPORTS = "SPORTS";
    public static final String TECHNOLOGY = "TECHNOLOGY";
    public static final String SCIENCE = "SCIENCE";
    public static final String SEARCH = "SEARCH";

    //Trending
    public static final String DEFAULT_SEARCH_TERM_FOR_TRENDING = "Coronavirus";

    //Time Zone
    public static final String TIME_ZONE_AMERICA_LA = "America/Los_Angeles";

    //Shared Preferences
    public static final String BOOKMARKS_SHARED_PREFERENCES_FILE_NAME = "Bookmarks";
    public static final String BOOKMARKS_KEY = "bookmarks";

    //Migration parameters between activities
    public static final String ARTICLE_ID = "org.usc.csci571.newsapp.ARTICLE_ID";
    public static final String ARTICLE_TITLE = "org.usc.csci571.newsapp.ARTICLE_TITLE";
    public static final String ARTICLE_TAG = "org.usc.csci571.newsapp.ARTICLE_TAG";
    public static final String QUERY_STRING = "org.usc.csci571.newsapp.QUERY_STRING";

    //Colors
    public static final String DARK_GRAY = "#404040";

    public static final int NUMBER_OF_LETTERS_BEFORE_SEARCH_QUERY_FIRES = 3;
    public static final int DEFAULT_NUMBER_OF_SEARCH_SUGGESTIONS = 5;
}
