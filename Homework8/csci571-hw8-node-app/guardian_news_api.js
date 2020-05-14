const request = require('request');
const express = require('express');
const router = express.Router();

const home = 'home';
const base_url_for_guardian_api = 'https://content.guardianapis.com';
const path_for_home_tab = '/search';
const guardian_news_api_key = '4ba2b18c-8ef5-418c-a050-e2b7b9f112a0';
const default_sections = '(sport%7Cbusiness%7Ctechnology%7Cpolitics)'
const show_blocks_parameter = 'show-blocks=all';
const default_image_url = 'https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png';
const base_url_for_search = 'https://content.guardianapis.com/search';
const error = {
    status: 500,
    message: "Internal server error occoured. Please try later"
}

/*
 * For all the sectionbased articles
 * including home, world, sport, business, technology, politics
 */

router.get("/articles/:section_name", function(req, res){
    try {
        let section_name = req.params.section_name;
        let url = '';
        if(section_name == home) {
            url = base_url_for_guardian_api + path_for_home_tab + "?" + "api-key=" + guardian_news_api_key +
                    "&section=" + default_sections + "&" + show_blocks_parameter;
        } else {
            url = base_url_for_guardian_api + "/" + section_name +  "?" + "api-key=" + guardian_news_api_key +
                     "&" + show_blocks_parameter;
        }
        request.get(url, {json: true}, function(err, response, body) {
            try {
                let processed_json = process_guardian_response(body);
                return res.status(200).json(processed_json);
            } catch (e) {
                console.log(e);
                return res.status(500).json(error);
            }
        });
    } catch (e) {
        console.log(e);
        return res.status(500).json(error);
    }
});

/*
 * For retrieving the article detail for the detail implementation page
 */
router.get("/articles", function(req, res){
    try {
        let article_id = req.query.article_id;
        let url = base_url_for_guardian_api + "/" + article_id +  "?" + "api-key=" + guardian_news_api_key +
                    "&" + show_blocks_parameter;
        request.get(url, {json: true}, function(err, response, body) {
            try {
                if(is_valid_article(body.response.content)) {
                    let processed_json = process_guardian_article_detail_response(body.response.content);
                    return res.status(200).json(processed_json);
                } else {
                    return res.status(500).json(error);
                }
            } catch (e) {
                    console.log(e);
                    return res.status(500).json(error);
            }
        });
    } catch (e) {
        console.log(e);
        return res.status(500).json(error);
    }
});

/*
 * For retrieving search articles
 */
router.get("/search", function(req, res) {
    try {
        let searchString = req.query.q;
        let url = base_url_for_search + "?q=" + searchString + "&api-key=" + guardian_news_api_key +
                         "&" + show_blocks_parameter;
        request.get(url, {json: true}, function(err, response, body) {
            try {
                let processed_json = process_guardian_response(body);
                return res.status(200).json(processed_json);
            } catch (e) {
                console.log(e);
                return res.status(500).json(error);
            }
        });
    } catch (e) {
        console.log(e);
        return res.status(500).json(error);
    }
})

process_guardian_article_detail_response = function(current_article) {
        let size = current_article.blocks.main.elements[0].assets.length;
        let custom_assets = {};
        if(size === 0) {
            custom_assets = {
                file: default_image_url
            }
        } else {
            let url = current_article.blocks.main.elements[0].assets[size - 1].file;
            if(url === undefined || url === null || url === "") {
                custom_assets = {
                    file: default_image_url
                }
            } else {
                custom_assets = current_article.blocks.main.elements[0].assets[size - 1];
            }
        }
        current_article.blocks.main.elements[0].assets = [];
        current_article.blocks.main.elements[0].assets.push(custom_assets);
        return current_article;
}

function is_empty(string) {
    return (string === null || string === undefined || string === "");
}

function is_article_title_valid(article) {
    return (article.hasOwnProperty("webTitle") && !is_empty(article.webTitle));
}

function is_article_image_path_valid(article) {
    if(article.hasOwnProperty("blocks") &&
        article.blocks.hasOwnProperty("main") &&
        article.blocks.main.hasOwnProperty("elements")) {
            return true;
        }
    return false;
}

function is_article_section_valid(article) {
    return (article.hasOwnProperty("sectionId") && !is_empty(article.sectionId));
}

function is_article_date_valid(article) {
    return (article.hasOwnProperty("webPublicationDate") && !is_empty(article.webPublicationDate));
}

function is_article_description_valid(article) {
    return (
        article.blocks.hasOwnProperty("body") &&
        article.blocks.body[0].hasOwnProperty("bodyTextSummary") &&
        !is_empty(article.blocks.body[0].bodyTextSummary)
    )
}

function is_valid_article(article) {
    return (
        article !== null &&
        article !== undefined &&
        is_article_title_valid(article) &&
        is_article_image_path_valid(article) &&
        is_article_section_valid(article) &&
        is_article_date_valid(article) &&
        is_article_description_valid(article)
    )
}

process_guardian_response = function(json) {
    let modified_results = [];
    for(let i = 0; i < json.response.results.length; i++) {
        let current_article = json.response.results[i];
        if(is_valid_article(current_article)) {
            current_article = process_guardian_article_detail_response(current_article);
            modified_results.push(current_article);
        } else {
            console.log("Invalid Guardian Article Found");
        }
    }
    json.response.results = modified_results;
    return json;
}

module.exports = router;