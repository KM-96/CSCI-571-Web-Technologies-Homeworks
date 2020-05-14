const request = require('request');
const express = require('express');
const router = express.Router();
const base_url_for_new_york_times_api = "https://api.nytimes.com/svc/topstories/v2/";
const base_url_for_detailed_article = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
const json_extension = ".json";
const new_york_times_api_key = "qFWAPS4rjnOtuGmzkzZQ7Tj11ReAJqpS";
const api_key_parameter = "api-key=";
const web_url_parameter = "fq=web_url";
const default_new_york_times_image_url = "https://upload.wikimedia.org/wikipedia/commons/0/0e/Nytimes_hq.jpg";
const minimum_width_of_the_image = 2000;
const minimum_number_of_articles = 10;
const base_url_for_image = "https://nyt.com/";
const error = {
    status: 500,
    message: "Internal server error occoured. Please try later"
}

/*
 * For all the sectionbased articles
 * including home, world, sport, business, technology, politics
 */

router.get("/articles/:section_name", function(req, res) {
    try {
        let section_name = req.params.section_name;
        let url = base_url_for_new_york_times_api + section_name + json_extension + "?" + api_key_parameter + new_york_times_api_key;
        request.get(url, {json: true}, function(err, response, body) {
            try {
                let processed_article = process_new_york_times_article(body);
                return res.status(200).json(processed_article);
            }catch (e) {
                 console.log(e);
                 return res.status(500).json(error);
             }
        });
    } catch (e) {
        console.log(e);
        return res.status(500).json(error);
    }
});

router.get("/articles", function(req, res) {
    try {
        let article_id = req.query.article_id;
        let url = base_url_for_detailed_article + "?" + web_url_parameter + encodeURIComponent(":(\"") + article_id + encodeURIComponent("\")")
                    + "&"+ api_key_parameter + new_york_times_api_key;
        request.get(url, {json: true}, function(err, response, body) {
            try {
                let processed_article = process_article_detail_response(body.response.docs[0]);
                body.response.docs = [];
                body.response.docs.push(processed_article);
                return res.status(200).json(body);
            } catch(e) {
                console.log(e);
                return res.status(500).json(error);
            }
        });
    } catch (e) {
        console.log(e);
        return res.status(500).json(error);
    }
});


router.get("/search", function(req, res) {
    try {
        let searchString = req.query.q;
        let url = base_url_for_detailed_article + "?q=" + searchString + "&"+ api_key_parameter + new_york_times_api_key;
        request.get(url, {json: true}, function(err, response, body) {
            try {
                let modified_docs = [];
                for(let i = 0; i < body.response.docs.length; i++) {
                    let processed_article = process_article_detail_response(body.response.docs[i]);
                    modified_docs.push(processed_article);
                }
                body.response.docs = [];
                body.response.docs = modified_docs;
                return res.status(200).json(body);
            } catch (e) {
                console.log(e);
                return res.status(500).json(error);
            }
        });
    } catch (e) {
        return res.status(500).json(error);
    }
})

process_article_detail_response = function(current_article) {
    let multimedia_array = current_article.hasOwnProperty("multimedia") ? current_article.multimedia : null;
    let modified_multimedia_object = {
        url: default_new_york_times_image_url
    };
    if(multimedia_array != null) {
        for(let j = 0; j < multimedia_array.length; j++) {
            if(multimedia_array[j].width >= minimum_width_of_the_image) {
                if(!multimedia_array[j].url.includes("http")) {
                    let image_url = base_url_for_image + multimedia_array[j].url;
                    multimedia_array[j].url = image_url;
                }
                modified_multimedia_object = multimedia_array[j];
                break;
            }
        }
    }
    current_article.multimedia = [];
    current_article.multimedia.push(modified_multimedia_object);
    return current_article;
}

process_new_york_times_article = function(json) {
    let modified_results = [];
    for (let i = 0; i < Math.min(json.results.length, minimum_number_of_articles); i++) {
        let current_article = json.results[i];
        if(is_valid_article(current_article)) {
            process_article_detail_response(current_article);
            modified_results.push(current_article);
        } else {
            console.log("Invalid New York Times Article Found");
        }
    }
    json.results = modified_results;
    return json;
}

is_empty = function(string) {
    return (string === null || string === undefined || string === "");
}

is_article_title_valid = function(article) {
    return (article.hasOwnProperty("title") && !is_empty(article.title));
}

is_article_image_path_valid = function(article) {
    return article.hasOwnProperty("multimedia");
}

is_article_section_valid = function(article) {
    return (article.hasOwnProperty("section") && !is_empty(article.section));
}

is_article_date_valid = function(article) {
    return (article.hasOwnProperty("published_date") && !is_empty(article.published_date));
}

is_article_description_valid = function(article) {
    return (article.hasOwnProperty("abstract") && !is_empty(article.abstract));
}

is_valid_article = function(article) {
    return (
        is_article_title_valid(article) &&
        is_article_image_path_valid(article) &&
        is_article_section_valid(article) &&
        is_article_date_valid(article) &&
        is_article_description_valid(article)
    )
}

module.exports = router;