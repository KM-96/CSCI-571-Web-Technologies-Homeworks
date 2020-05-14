import * as Constants from "../common/Constants";

function isDetail() {
     let location = window.location.href;
     if (location.includes(Constants.ARTICLES + Constants.QUESTION_MARK + Constants.ARTICLE_ID_QUERY_PARAM)) {
         return true;
     }
     return false;
}

function isSearch() {
     let location = window.location.href;
     if (location.includes(Constants.SEARCH)) {
         return true;
     }
     return false;
}

export function getDescription(props, website) {
     if (website === Constants.GUARDIAN) {
         return props.blocks.body[0].bodyTextSummary;
     } else {
         let location = window.location.href;
         if (location.includes(Constants.ARTICLES + Constants.QUESTION_MARK + Constants.ARTICLE_ID_QUERY_PARAM)) {
             return props.response.docs[0].abstract;
         }
         return  props.abstract;
     }
 }

export function getTitle(props, website) {
    if (website === Constants.GUARDIAN) {
        return props.webTitle;
    } else {
        if (isDetail()) {
            return props.response.docs[0].headline.main;
        } else if (isSearch()) {
            return props.headline.main;
        }
        return  props.title;
    }
}

 export function getSection(props, website) {
    if (website === Constants.GUARDIAN) {
        return props.sectionId;
    } else {
        if (isSearch()) {
            return props.news_desk;
        }
        if(isDetail()) {
            return props.response.docs[0].section_name;
        }
        return  props.section;
    }
}

export function getSectionBackgroundColor(sectionId) {
    switch(sectionId) {
        case "world":
           return {
                backgroundColor: "#a64dff",
                color: "white",
                fontStyle: "normal"
           }
        case "politics":
            return {
                backgroundColor: "#009999",
                color: "white",
                fontStyle: "normal"
            }
        case "business":
            return {
                backgroundColor: " #4dc3ff",
                color: "white",
                fontStyle: "normal"
            }
        case "technology":
            return {
                backgroundColor: "#cccc00",
                color: "black",
                fontStyle: "normal"
            }
        case "sport":
            return {
                backgroundColor: "#ffa31a",
                color: "black",
                fontStyle: "normal"
            }
        case "sports":
            return {
                backgroundColor: "#ffa31a",
                color: "black",
                fontStyle: "normal"
            }
        case "guardian":
            return {
                backgroundColor: "black",
                color: "white",
                fontStyle: "normal"
            }
        case "nytimes":
            return {
                backgroundColor: "#f2f2f2",
                color: "black",
                fontStyle: "normal"
            }
        default:
            return {
                backgroundColor: "#999999",
                color: "white",
                fontStyle: "normal"
            }
    }
}

export function getImageUrl(props, website) {
   if (website === Constants.GUARDIAN) {
       return props.blocks.main.elements[0].assets[0].file;
   } else {
       let location = window.location.href;
       if (location.includes(Constants.ARTICLES + Constants.QUESTION_MARK + Constants.ARTICLE_ID_QUERY_PARAM)) {
           return props.response.docs[0].multimedia[0].url;
       }
       return  props.multimedia[0].url;
   }
}

export function getArticleUrl(props, website) {
    if (website === Constants.GUARDIAN) {
       return props.id;
    } else {
       if(isDetail()) {
            return props.response.docs[0].web_url;
       }
       if(isSearch()) {
            return props.web_url;
       }
       return  props.url;
    }
}

export function getDate(props, website) {
    if (website === Constants.GUARDIAN) {
        return getDateString(props.webPublicationDate);
    } else {
        if (isDetail()) {
            return getDateString(props.response.docs[0].pub_date);
        } else if (isSearch()) {
            return getDateString(props.pub_date);
        }
        return  getDateString(props.published_date);
    }
}

export function getDateString(webPublicationDate) {
    let year = webPublicationDate.substring(0, 4);
    let month = webPublicationDate.substring(5,7);
    let day = webPublicationDate.substring(8,10);
    return year + "-" + month + "-" + day;
}

export function getDetailedArticleUrlId(props, website) {
    if (website === Constants.GUARDIAN) {
        return props.id;
    } else {
        if (isSearch()) {
            return props.web_url;
        }
        return props.url;
    }
}

export function getId(props, website) {
    if(website === Constants.GUARDIAN) {
        return props.webUrl;
    }
    if(website === Constants.NEW_YORK_TIMES) {
        if(isDetail()) {
            return props.response.docs[0].web_url;
       }
        if(isSearch()) {
            return props.web_url;
        }
        return  props.url;
    }
}
