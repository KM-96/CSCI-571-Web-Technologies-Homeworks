//Application Base URL
export const BASE_URL = "https://csci-571-hw8-node-app-km69564.appspot.com";

//Local storage key for toggle switch
export const IS_GUARDIAN_KEY = "isGuardian";

//URLs to differnt endpoints
export const GUARDIAN_URL = "/guardian-news";
export const NEW_YORK_TIMES_URL = "/new-york-times";
export const HOME = "/articles/home";
export const WORLD = "/articles/world";
export const SPORT = "/articles/sport";
export const SPORTS = "/articles/sports";
export const TECHNOLOGY = "/articles/technology";
export const BUSINESS = "/articles/business";
export const POLITICS = "/articles/politics";
export const ARTICLES = "/articles";
export const SEARCH = "/search";

//News Sections
export const HOME_SECTION = "home";
export const WORLD_SECTION = "world";
export const SPORT_SECTION = "sport";
export const SPORTS_SECTION = "sports";
export const TECHNOLOGY_SECTION = "technology";
export const BUSINESS_SECTION = "business";
export const POLITICS_SECTION = "politics";

//Detailed articles url attributes
export const ARTICLE_ID_QUERY_PARAM = "article_id=";
export const Q_QUERY_PARAM = "q=";

//Other
export const NUMBER_OF_LINES_IN_DESCRIPTION = 3;
export const NUMBER_OF_SENTENCES_IN_ARTICLE_DETAIL_VIEW = 4;
export const QUESTION_MARK = "?";

//News Websites
export const GUARDIAN = "guardian";
export const NEW_YORK_TIMES = "new_york_times";

//News websites badge
export const GUARDIAN_BADGE = "GUARDIAN";
export const NYTIMES_BADGE = "NYTIMES";

//News websites name for favorites modal
export const GUARDIAN_TEXT_FOR_MODAL = "GUARDIAN";
export const NEW_YORK_TIMES_TEXT_FOR_MODAL = "NY TIMES";

//Default views of the detailed articles
export const MINIFIED_DETAIL_VIEW = "minified";
export const FULL_DETAIL_VIEW = "full";

//Hashtag for sharing
export const SOCIAL_MEDIA_SHARE_HASHTAG = "CSCI_571_NewsApp";

//Comment Box Project ID
export const COMMENT_BOX_PROJECT_ID = "5690210423668736-proj";

//Bing API
export const BING_API = "https://krishna-manoj.cognitiveservices.azure.com/bing/v7.0/suggestions";

//Bookmarks
export const BOOKMARK_KEY = "bookmark";


//Functions
export function handleErrors(response) {
    if (!response.ok) {
        throw Error(response.statusText);
    }
    return response.json();
}
