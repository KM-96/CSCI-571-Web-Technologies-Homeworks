var HEADLINES_URL = "/headlines";
var SOURCES_URL = "/sources";
var SEARCH_URL = "/search";
var FREQUENT_WORDS_URL = "/frequent-words";
var DEFAULT_NUMBER_OF_SOURCES = 10;
var MIN_CHARACTERS_IN_A_LINE_FOR_SEARCH_RESULT = 60;
var GET_METHOD = "GET";
var frequency_list = [];
var slideIndex = 0;

on_load = function() {
    /* Load homepage */
	load_home_page();
	/* Hide the search page */
	document.getElementById("search").style.display = "none";
}

openTab = function (tab, tabName) {
	var i, tabcontent, tablinks;
	tabcontent = document.getElementsByClassName("tabcontent");
	for (i = 0; i < tabcontent.length; i++) {
		tabcontent[i].style.display = "none";
	}
	tablinks = document.getElementsByClassName("tablinks");
	for (i = 0; i < tablinks.length; i++) {
		tablinks[i].className = tablinks[i].className.replace(" active", "");
	}
	document.getElementById(tabName).style.display = "block";
	tab.currentTarget.className += " active";
	if(tabName === "search") {
	    load_search_page();
	}
}

load_home_page = function() {
    load_top_headlines();
    load_cnn_headlines();
    load_fox_news_headlines();
    load_frequent_words();
}

load_top_headlines = function() {
     /* Make call to server */
    var url = HEADLINES_URL + "?sources=all";
    var xmlHttpRequest = new XMLHttpRequest();
    xmlHttpRequest.open(GET_METHOD, url, true);
    xmlHttpRequest.onreadystatechange = function() {
        if (xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200) {
            var json = JSON.parse(xmlHttpRequest.responseText);
            if(json !== undefined && json != null && json.hasOwnProperty("status") && json.status === "ok") {
                populate_top_headlines(json);
            } else {
                display_alert_message(json.message);
            }
        }
    };
    xmlHttpRequest.send(null);
}

load_cnn_headlines  = function() {
     /* Make call to server */
    var url = HEADLINES_URL + "?sources=cnn";
    var xmlHttpRequest = new XMLHttpRequest();
    xmlHttpRequest.open(GET_METHOD, url, true);
    xmlHttpRequest.onreadystatechange = function() {
        if (xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200) {
            var json = JSON.parse(xmlHttpRequest.responseText);
            if(json !== undefined && json != null && json.hasOwnProperty("status") && json.status === "ok") {
                populate_cnn_headlines(json);
            } else {
                display_alert_message(json.message);
            }
        }
    };
    xmlHttpRequest.send(null);
}


load_fox_news_headlines  = function() {
     /* Make call to server */
    var url = HEADLINES_URL + "?sources=fox-news";
    var xmlHttpRequest = new XMLHttpRequest();
    xmlHttpRequest.open(GET_METHOD, url, true);
    xmlHttpRequest.onreadystatechange = function() {
        if (xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200) {
            var json = JSON.parse(xmlHttpRequest.responseText);
            if(json !== undefined && json != null && json.hasOwnProperty("status") && json.status === "ok") {
                populate_fox_news_headlines(json);
            } else {
                display_alert_message(json.message);
            }
        }
    };
    xmlHttpRequest.send(null);
}

load_frequent_words = function() {
     /* Make call to server */
    var xmlHttpRequest = new XMLHttpRequest();
    xmlHttpRequest.open(GET_METHOD, FREQUENT_WORDS_URL, true);
    xmlHttpRequest.onreadystatechange = function() {
        if (xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200) {
            var json = JSON.parse(xmlHttpRequest.responseText);
            if(json !== undefined && json != null && json.hasOwnProperty("frequent_words")) {
                create_word_cloud(json.frequent_words);
            } else {
                display_alert_message(json.message);
            }
        }
    };
    xmlHttpRequest.send(null);
}

load_search_page = function() {
    set_time_fields();
    get_sources("");
    /* Reset the category to 'all' */
    document.getElementById("search_category").value = "";
    /* Reset the source to 'all' */
    document.getElementById("search_sources").value = "";
    /* Clear the search text box */
    document.getElementById("search_text").value = "";
    /* Hide the search content */
    document.getElementById("search_content").innerHTML = "";
    /* Hide both the show more and show less buttons */
    document.getElementById("show_more_button").style.display = "none";
    document.getElementById("show_less_button").style.display = "none";
}

set_time_fields = function() {
    /* Set the to_date field */
    var today = new Date();
    document.getElementById("search_to").value =  get_date_string(today);

    /* Set the from_date field */
    var one_week_before_date = new Date();
    one_week_before_date.setDate(one_week_before_date.getDate() - 7);
    document.getElementById("search_from").value = get_date_string(one_week_before_date);
}

function get_date_string(date) {
    var day = date.getDate();
    if(day <= 9) {
        day = "0" + day;
    }
    var month = date.getMonth() + 1;
    if(month <= 9) {
        month = "0" + month;
    }
    return date.getFullYear() + "-" + month + "-" + day;
}

function format_date_for_search_result(date) {
    var day = date.getDate();
    if(day <= 9) {
        day = "0" + day;
    }
    var month = date.getMonth() + 1;
    if(month <= 9) {
        month = "0" + month;
    }
    return month + "/" + day + "/" + date.getFullYear() ;
}

function populate_sources(json) {
    var search_sources = document.getElementById("search_sources");
    search_sources.innerHTML = "";
    var all_option = document.createElement("option");
    all_option.setAttribute("value", "");
    all_option.setAttribute("class", "select_options");
    all_option.innerHTML = "all";
    search_sources.appendChild(all_option);
    for(var i = 0; i < Math.min(json.sources.length, DEFAULT_NUMBER_OF_SOURCES); i++) {
        var option = document.createElement("option");
        option.setAttribute("value", json.sources[i].id);
        option.setAttribute("class", "select_options");
        option.innerHTML = json.sources[i].name;
        search_sources.appendChild(option);
    }
}

function get_sources (category) {
    var url = SOURCES_URL + "?category=" + category;
    var xmlHttpRequest = new XMLHttpRequest();
    xmlHttpRequest.open(GET_METHOD, url, true);
    xmlHttpRequest.onreadystatechange = function() {
        if (xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200) {
            var json = JSON.parse(xmlHttpRequest.responseText);
            if(json !== undefined && json != null && json.hasOwnProperty("status") && json.status === "ok") {
                populate_sources(json);
            } else {
                display_alert_message(json.message);
            }
        }
    };
    xmlHttpRequest.send(null);
}

populate_top_headlines = function(top_headlines) {
	var headlines = document.getElementById("headlines");
	for(var i = 0; i < top_headlines.articles.length; i++) {
		var current_article = top_headlines.articles[i];
		var parentDiv = document.createElement("div");
		parentDiv.setAttribute("class", "slides_container");
		var anchor = document.createElement("a");
		anchor.setAttribute("href", current_article.url);
		anchor.setAttribute("target", "_blank");
		var imageElement = document.createElement("img");
		imageElement.setAttribute("src", current_article.urlToImage);
		imageElement.setAttribute("class", "mySlides");
		imageElement.setAttribute("style", "display: none;");
		imageElement.setAttribute("alt", "Image Not Available.")
		anchor.appendChild(imageElement);
		var childDiv = document.createElement("div");
		childDiv.setAttribute("class", "top_headlines_text");
		var paragraphTitle = document.createElement("p");
		paragraphTitle.setAttribute("class", "top_headlines_title");
		paragraphTitle.innerHTML = current_article.title;
		var paragraphDescription = document.createElement("p");
		paragraphDescription.setAttribute("class", "top_headlines_description");
		paragraphDescription.innerHTML = current_article.description;
		childDiv.appendChild(paragraphTitle);
		childDiv.appendChild(paragraphDescription);
		anchor.appendChild(childDiv);
		parentDiv.appendChild(anchor);
		headlines.appendChild(parentDiv);
	}
	showSlides();
}

populate_cnn_headlines = function(cnn_headlines) {
	var cnn_cards = document.getElementById("cnn_cards");
	build_cards(cnn_headlines, cnn_cards);
}

populate_fox_news_headlines = function(fox_news_headlines) {
	var fox_news_cards = document.getElementById("fox_news");	
	build_cards(fox_news_headlines, fox_news_cards);
}

build_cards = function(data, divId) {
	for(var i = 0; i < data.articles.length; i++) {
		var article = data.articles[i];
		var anchorTag = document.createElement("a");
		anchorTag.setAttribute("target", "_blank");
		anchorTag.setAttribute("href", article.url);
		var divCard = document.createElement("div");
		divCard.setAttribute("class", "card");
		var imageElement = document.createElement("img");
		imageElement.setAttribute("src", article.urlToImage);
		imageElement.setAttribute("alt", "image");
		imageElement.setAttribute("class", "images");
		var divContainer = document.createElement("div");
		divContainer.setAttribute("class", "image-text");
		var paragraphTitle = document.createElement("p");
		paragraphTitle.innerHTML = article.title;
		paragraphTitle.setAttribute("class", "image-title");
		var paragraphDescription = document.createElement("p");
		paragraphDescription.innerHTML = article.description;
		paragraphDescription.setAttribute("class", "image-description");
		divContainer.appendChild(paragraphTitle);
		divContainer.appendChild(paragraphDescription);
		divCard.appendChild(imageElement);
		divCard.appendChild(divContainer);
		anchorTag.appendChild(divCard);
		divId.appendChild(anchorTag);
	}
}

load_specific_content = function(sources) {
	var xmlHttpRequest = new XMLHttpRequest();
	try {
		var query_parameters = "sources=" + sources;
		xmlHttpRequest.open("GET", HOMEPAGE_URL + "?" + query_parameters, false);
		xmlHttpRequest.send();
		var json = JSON.parse(xmlHttpRequest.responseText);
		return json;
	} catch(error) {
		alert('Error: Unable to retrieve data from the specified URL: ' + HOMEPAGE_URL);
	}
}

search = function() {
    var keyword = document.getElementById("search_text").value;
    var date_from = document.getElementById("search_from").value;
    var date_to = document.getElementById("search_to").value;
    var category = document.getElementById("search_category")[document.getElementById("search_category").selectedIndex].value;
    var source = document.getElementById("search_sources")[document.getElementById("search_sources").selectedIndex].value;
    var url = SEARCH_URL + "?q=" + keyword + "&date_from=" + date_from + "&date_to=" + date_to + "&category=" + category + "&source=" + source;

    /* If end to_date is less than the from_date then show an error to the user */
    var flag = check_dates(date_from, date_to);
    if(!flag) {
        alert("Incorrect time");
        return false;
    }

    /* Make call to server */
    var xmlHttpRequest = new XMLHttpRequest();
    xmlHttpRequest.open(GET_METHOD, url, true);
    xmlHttpRequest.onreadystatechange = function() {
        if (xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200) {
            var json = JSON.parse(xmlHttpRequest.responseText);
            if(json !== undefined && json != null && json.hasOwnProperty("status") && json.status === "ok") {
                populate_search_data(json);
            } else {
                display_alert_message(json.message);
            }
        }
    };
    xmlHttpRequest.send(null);
    return false;
}

check_dates = function(date_from, date_to) {
    var from = new Date(date_from);
    var to = new Date(date_to);
    if(date_to < date_from) {
        return false;
    }
    return true;
}

populate_search_data = function(json) {
    var search_content = document.getElementById("search_content");
    search_content.innerHTML = "";

    /* Hide both the show more and show less buttons */
    document.getElementById("show_more_button").style.display = "none";
    document.getElementById("show_less_button").style.display = "none";

    if(json.articles.length == 0) {
        var paragraph = document.createElement("p");
        paragraph.setAttribute("id", "no_search_results");
        paragraph.innerHTML = "No results";
        search_content.appendChild(paragraph);
        return;
    }
    var max_articles = Math.min(15, json.articles.length);
    for(var i = 0; i < Math.max(max_articles, 5); i++) {
        var current_article = json.articles[i];
        var div_card = document.createElement("div");
        div_card.setAttribute("class", "search_card");
        div_card.setAttribute("onclick", "enlarge_search_card(this);");

        /* Add the image to the card */
        var image = document.createElement("img");
        image.setAttribute("src", current_article.urlToImage);
        image.setAttribute("alt", "Image Not Available");
        image.setAttribute("class", "search_images");

        /* Add the title */
        var title = document.createElement("p");
        title.innerHTML = current_article.title;
        title.setAttribute("class", "search_title");

        /* Add a cross mark to collapse the div */
        var cross_span = document.createElement("button");
        cross_span.setAttribute("class", "cross_mark");
        onclick_function = "reset_search_card(this.parentElement); event.stopPropagation();"
        cross_span.setAttribute("onclick", onclick_function);
        cross_span.innerHTML = "X";
        cross_span.style.display = "none";

        /* Add all the content that a card has */
        var search_card_content = document.createElement("div");
        search_card_content.setAttribute("class", "search_card_content");

        /* Add description */
        var description = document.createElement("p");
        var original_description = current_article.description;
        var formatted_description = original_description.substring(0, MIN_CHARACTERS_IN_A_LINE_FOR_SEARCH_RESULT);
        if(original_description.length > MIN_CHARACTERS_IN_A_LINE_FOR_SEARCH_RESULT) {
            var last_index = formatted_description.lastIndexOf(" ");
            formatted_description = formatted_description.substring(0, last_index) + "...";
        }
        description.innerHTML = formatted_description;
        description.setAttribute("class", "search_description");
        search_card_content.appendChild(description);

        div_card.appendChild(image);
        div_card.appendChild(title);
        div_card.appendChild(cross_span);
        div_card.appendChild(search_card_content);
        search_content.appendChild(div_card);

        /* Add all the Hidden attributes */
        /* Adding author */
        var author_label = document.createElement("label");
        author_label.setAttribute("class", "search_content_labels");
        author_label.innerHTML = "Author: ";
        author_label.style.display = "none";
        search_card_content.appendChild(author_label);
        var author_content = document.createElement("p");
        author_content.setAttribute("class", "search_content_values");
        author_content.innerHTML = current_article.author;
        author_content.style.display = "none";
        search_card_content.appendChild(author_content);
        var br1 = document.createElement("br");
        br1.style.display = "none";
        search_card_content.appendChild(br1);

        /* Adding Source */
        var source = document.createElement("label");
        source.setAttribute("class", "search_content_labels");
        source.innerHTML = "Source: ";
        source.style.display = "none";
        search_card_content.appendChild(source);
        var source_content = document.createElement("span");
        source_content.setAttribute("class", "search_content_values");
        source_content.innerHTML = current_article.source.name;
        source_content.style.display = "none";
        search_card_content.appendChild(source_content);
        var br2 = document.createElement("br");
        br2.style.display = "none";
        search_card_content.appendChild(br2);


        /* Adding Date */
        var date = document.createElement("label");
        date.setAttribute("class", "search_content_labels");
        date.innerHTML = "Date: ";
        date.style.display = "none";
        search_card_content.appendChild(date);
        var date_content = document.createElement("span");
        date_content.setAttribute("class", "search_content_values");
        var published_date = new Date(current_article.publishedAt);
        var date_value = format_date_for_search_result(published_date)
        date_content.innerHTML = date_value;
        date_content.style.display = "none";
        search_card_content.appendChild(date_content);
        var br3 = document.createElement("br");
        br3.style.display = "none";
        search_card_content.appendChild(br3);

        /* Adding Full Description */
        var full_description = document.createElement("p");
        full_description.innerHTML = current_article.description;
        full_description.setAttribute("class", "search_content_values");
        full_description.style.display = "none";
        search_card_content.appendChild(full_description);
        var br4 = document.createElement("br");
        br4.style.display = "none";
        search_card_content.appendChild(br4);

        /* Adding See Original Post Hyperlink */
        var anchor = document.createElement("a");
        anchor.setAttribute("href", current_article.url);
        anchor.setAttribute("class", "search_content_values");
        anchor.setAttribute("target", "_blank");
        anchor.innerHTML = "See Original Post";
        anchor.style.display = "none";
        search_card_content.appendChild(anchor);

        if(i >= 5) {
            div_card.style.display = "None";
        }
    }
    if(json.articles.length > 5) {
        document.getElementById("show_more_button").style.display = "block";
        document.getElementById("show_less_button").style.display = "none";
    }
}

show_more_search_results = function() {
    var search_cards = document.getElementsByClassName("search_card");
    for(var i = 0; i < search_cards.length; i++) {
        search_cards[i].style.display = "block";
    }

    /* Activate the show less button and hide the show more button */
    document.getElementById("show_more_button").style.display = "none";
    document.getElementById("show_less_button").style.display = "block";
}

show_less_search_results = function() {
    var search_cards = document.getElementsByClassName("search_card");
    for(var i = 5; i < search_cards.length; i++) {
        search_cards[i].style.display = "none";
    }

    /* Activate the show more button and hide the show less button*/
    document.getElementById("show_more_button").style.display = "block";
    document.getElementById("show_less_button").style.display = "none";
}

enlarge_search_card = function(card) {
    card.style.height = "auto";
    card.setAttribute("onclick", "null");
    for(var i = 0; i < card.children.length; i++) {
        var current_child = card.children[i];
        if(current_child.className === "cross_mark") {
            current_child.style.display = "inline";
        }
        if(current_child.className === "search_card_content") {
            for(var j = 0; j < current_child.children.length; j++) {
                var grand_child = current_child.children[j];
                //  if(grand_child)
                if(grand_child.style.display == "none") {
                    grand_child.style.display = "inline";
                } else {
                    grand_child.style.display = "none";
                }
            }
        }
    }
}

reset_search_card = function(card) {
    card.setAttribute("onclick", "enlarge_search_card(this)");
    for(var i = 0; i < card.children.length; i++) {
        var current_child = card.children[i];
        if(current_child.className === "cross_mark") {
            current_child.style.display = "none";
        }
        card.style.height = "120px";
        if(current_child.className === "search_card_content") {
            for(var j = 0; j < current_child.children.length; j++) {
                var grand_child = current_child.children[j];
                if(grand_child.className == "search_description") {
                    grand_child.style.display = "block";
                } else {
                    grand_child.style.display = "none";
                }
            }
        }
    }
}

showSlides = function() {
    var slides = document.getElementsByClassName("mySlides");
	var titles = document.getElementsByClassName("top_headlines_title");
	var descriptions = document.getElementsByClassName("top_headlines_description");
	
    for (var i = 0; i < slides.length; i++) {
		slides[i].style.display = "none";	
		titles[i].style.display = "none";
		descriptions[i].style.display = "none";
    }
    slideIndex++;
    if (slideIndex > slides.length) {
		slideIndex = 1;
	}
    slides[slideIndex-1].style.display = "block";
	titles[slideIndex-1].style.display = "block";
	descriptions[slideIndex-1].style.display = "block";
	// Change image every 4 seconds
	setTimeout(showSlides, 3000);
}

create_word_cloud = function(frequent_words) {
    for(var i = 0; i < frequent_words.length; i++) {
        frequency_list.push({"text": frequent_words[i][0], "size": Math.min(frequent_words[i][1] * 8, 30)});
    }

    d3.layout.cloud().size([240, 240])
            .words(frequency_list)
            .padding(4)
            .rotate(function() { return ~~(Math.random() * 2) * 90; })
            .fontSize(function(d) { return d.size; })
            .text(function(d) { return d.text; })
            .on("end", draw)
            .start();

}

draw = function (words) {
    d3.select("#frequent_words").append("svg")
            .attr("width", 245)
            .attr("height", 240)
            .append("g")
            // without the transform, words words would get cutoff to the left and top, they would
            // appear outside of the SVG area
            .attr("width", 245)
            .attr("height", 240)
            .attr("transform", "translate(120,125)")
            .attr("text-anchor", "middle")
            .selectAll("text")
            .data(words)
            .enter().append("text")
            .style("font-family", "Impact")
            .style("font-weight", "bold")
            .style("font-size", function(d) { return d.size + "px"; })
            .style("color", "black")
            .attr("transform", function(d) {
                return "translate(" + [d.x, d.y] + ")rotate(" + d.rotate + ")";
            })
            .text(function(d) {return d.text; });
    }

display_alert_message = function(message) {
    alert(message);
}