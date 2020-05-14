import operator

from flask import Flask, current_app, request
from newsapi import NewsApiClient
from newsapi.newsapi_exception import NewsAPIException

newsapi = NewsApiClient(api_key="8a80c118dea0453792506eca531e0193")

application = Flask(__name__)

NUMBER_OF_TOP_HEADLINES = 5
NUMBER_OF_CHANNEL_SPECIFIC_HEADLINES = 4
NUMBER_OF_STOP_WORDS = 30
STOP_WORDS = ["-", "a", "a's", "able", "about", "above", "according", "accordingly", "across", "actually", "after",
              "afterwards", "again", "against", "ain't", "all", "allow", "allows", "almost", "alone", "along",
              "already", "also", "although", "always", "am", "among", "amongst", "an", "and", "another", "any",
              "anybody", "anyhow", "anyone", "anything", "anyway", "anyways", "anywhere", "apart", "appear",
              "appreciate", "appropriate", "are", "aren't", "around", "as", "aside", "ask", "asking", "associated",
              "at", "available", "away", "awfully", "b", "be", "became", "because", "become", "becomes", "becoming",
              "been", "before", "beforehand", "behind", "being", "believe", "below", "beside", "besides", "best",
              "better", "between", "beyond", "both", "brief", "but", "by", "c", "c'mon", "c's", "came", "can", "can't",
              "cannot", "cant", "cause", "causes", "certain", "certainly", "changes", "clearly", "co", "com", "come",
              "comes", "concerning", "consequently", "consider", "considering", "contain", "containing", "contains",
              "corresponding", "could", "couldn't", "course", "currently", "d", "definitely", "described", "despite",
              "did", "didn't", "different", "do", "does", "doesn't", "doing", "don't", "done", "down", "downwards",
              "during", "e", "each", "edu", "eg", "eight", "either", "else", "elsewhere", "enough", "entirely",
              "especially", "et", "etc", "even", "ever", "every", "everybody", "everyone", "everything", "everywhere",
              "ex", "exactly", "example", "except", "f", "far", "few", "fifth", "first", "five", "followed",
              "following", "follows", "for", "former", "formerly", "forth", "four", "from", "further", "furthermore",
              "g", "get", "gets", "getting", "given", "gives", "go", "goes", "going", "gone", "got", "gotten",
              "greetings", "h", "had", "hadn't", "happens", "hardly", "has", "hasn't", "have", "haven't", "having",
              "he", "he's", "hello", "help", "hence", "her", "here", "here's", "hereafter", "hereby", "herein",
              "hereupon", "hers", "herself", "hi", "him", "himself", "his", "hither", "hopefully", "how", "howbeit",
              "however", "i", "i'd", "i'll", "i'm", "i've", "ie", "if", "ignored", "immediate", "in", "inasmuch", "inc",
              "indeed", "indicate", "indicated", "indicates", "inner", "insofar", "instead", "into", "inward", "is",
              "isn't", "it", "it'd", "it'll", "it's", "its", "itself", "j", "just", "k", "keep", "keeps", "kept",
              "know", "knows", "known", "l", "last", "lately", "later", "latter", "latterly", "least", "less", "lest",
              "let", "let's", "like", "liked", "likely", "little", "look", "looking", "looks", "ltd", "m", "mainly",
              "many", "may", "maybe", "me", "mean", "meanwhile", "merely", "might", "more", "moreover", "most",
              "mostly", "much", "must", "my", "myself", "n", "name", "namely", "nd", "near", "nearly", "necessary",
              "need", "needs", "neither", "never", "nevertheless", "new", "next", "nine", "no", "nobody", "non", "none",
              "noone", "nor", "normally", "not", "nothing", "novel", "now", "nowhere", "o", "obviously", "of", "off",
              "often", "oh", "ok", "okay", "old", "on", "once", "one", "ones", "only", "onto", "or", "other", "others",
              "otherwise", "ought", "our", "ours", "ourselves", "out", "outside", "over", "overall", "own", "p",
              "particular", "particularly", "per", "perhaps", "placed", "please", "plus", "possible", "presumably",
              "probably", "provides", "q", "que", "quite", "qv", "r", "rather", "rd", "re", "really", "reasonably",
              "regarding", "regardless", "regards", "relatively", "respectively", "right", "s", "said", "same", "saw",
              "say", "saying", "says", "second", "secondly", "see", "seeing", "seem", "seemed", "seeming", "seems",
              "seen", "self", "selves", "sensible", "sent", "serious", "seriously", "seven", "several", "shall", "she",
              "should", "shouldn't", "since", "six", "so", "some", "somebody", "somehow", "someone", "something",
              "sometime", "sometimes", "somewhat", "somewhere", "soon", "sorry", "specified", "specify", "specifying",
              "still", "sub", "such", "sup", "sure", "t", "t's", "take", "taken", "tell", "tends", "th", "than",
              "thank", "thanks", "thanx", "that", "that's", "thats", "the", "their", "theirs", "them", "themselves",
              "then", "thence", "there", "there's", "thereafter", "thereby", "therefore", "therein", "theres",
              "thereupon", "these", "they", "they'd", "they'll", "they're", "they've", "think", "third", "this",
              "thorough", "thoroughly", "those", "though", "three", "through", "throughout", "thru", "thus", "to",
              "together", "too", "took", "toward", "towards", "tried", "tries", "truly", "try", "trying", "twice",
              "two", "u", "un", "under", "unfortunately", "unless", "unlikely", "until", "unto", "up", "upon", "us",
              "use", "used", "useful", "uses", "using", "usually", "uucp", "v", "value", "various", "very", "via",
              "viz", "vs", "w", "want", "wants", "was", "wasn't", "way", "we", "we'd", "we'll", "we're", "we've",
              "welcome", "well", "went", "were", "weren't", "what", "what's", "whatever", "when", "whence", "whenever",
              "where", "where's", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether",
              "which", "while", "whither", "who", "who's", "whoever", "whole", "whom", "whose", "why", "will",
              "willing", "wish", "with", "within", "without", "won't", "wonder", "would", "would", "wouldn't", "x", "y",
              "yes", "yet", "you", "you'd", "you'll", "you're", "you've", "your", "yours", "yourself", "yourselves",
              "z", "zero"]


def validate_article(article):
    source = article["source"]
    if not source or not source["id"] or not source["name"] or not article["author"] or not article["url"] or \
            not article["title"] or not article["description"]  or \
            not article["urlToImage"] or not article["publishedAt"]:
        return False
    return True


def validate_search_result(search_result):
    source = search_result["source"]
    if not source or not source["name"] or not search_result["author"] or not search_result["url"] or \
            not search_result["title"] or not search_result["description"] or not search_result["urlToImage"] or \
            not search_result["publishedAt"]:
        return False
    return True


def get_list_of_sources(sources_list):
    list_of_source_names = []
    for source in sources_list["sources"]:
        list_of_source_names.append({'id': source["id"], 'name': source["name"]})
    sources_list["sources"] = list_of_source_names
    return sources_list


def process_top_headlines(headlines, number_of_records):
    processed_articles = []
    articles = headlines["articles"]
    for article in articles:
        flag = validate_article(article)
        if flag:
            processed_articles.append(article)
        if len(processed_articles) is number_of_records:
            break
    headlines["totalResults"] = len(processed_articles)
    headlines["articles"] = processed_articles
    return headlines


def find_all_frequent_words(headlines):
    frequency = dict()
    for article in headlines["articles"]:
        if article["title"]:
            title_words = article["title"].split(" ")
            for title_word in title_words:
                title_word = title_word.lower()
                if title_word not in STOP_WORDS:
                    if title_word in frequency:
                        value = frequency[title_word]
                        frequency[title_word] = value + 1
                    else:
                        frequency[title_word] = 1
    sorted_list = sorted(frequency.items(), key=operator.itemgetter(1), reverse=True)
    sorted_list_capitalize = []
    for item in sorted_list[0:NUMBER_OF_STOP_WORDS]:
        new_item = (item[0].capitalize(), item[1])
        sorted_list_capitalize.append(new_item)
    return sorted_list_capitalize


@application.route("/")
def index():
    return current_app.send_static_file("index.html")


@application.route("/styles.css")
def css():
    return current_app.send_static_file("styles.css"), {"Content-Type": "text/css"}


@application.route("/scripts.js")
def scripts():
    return current_app.send_static_file("scripts.js")


@application.route("/headlines")
def headlines():
    try:
        source = request.args.get("sources", "")
        if source == "all":
            top_headlines = newsapi.get_top_headlines(page_size=30, country="us", language="en")
            top_headlines = process_top_headlines(top_headlines, NUMBER_OF_TOP_HEADLINES)
        else:
            top_headlines = newsapi.get_top_headlines(sources=source, page_size=30, language="en")
            top_headlines = process_top_headlines(top_headlines, NUMBER_OF_CHANNEL_SPECIFIC_HEADLINES)
        return top_headlines
    except NewsAPIException as news_api_exception:
        return {'status': news_api_exception.get_status(), 'code': news_api_exception.get_code(),
                'message': news_api_exception.get_message()}
    except Exception as exception:
        return {'status': 'error', 'code': 'Internal Server Error.',
                'message': 'There is an internal error. Please try the same query after sometime.'}


@application.route("/sources")
def sources():
    try:
        category = request.args.get("category", "")
        if category == "":
            return get_list_of_sources(newsapi.get_sources(language="en", country="us"))
        else:
            return get_list_of_sources(newsapi.get_sources(category=category, language="en", country="us"))
    except NewsAPIException as news_api_exception:
        return {'status': news_api_exception.get_status(), 'code': news_api_exception.get_code(),
                'message': news_api_exception.get_message()}
    except Exception as exception:
        return {'status': 'error', 'code': 'Internal Server Error.',
                'message': 'There is an internal error. Please try the same query after sometime.'}


@application.route("/search")
def search_news():
    search_results = None
    try:
        q = request.args.get("q", "")
        date_from = request.args.get("date_from", "")
        date_to = request.args.get("date_to", "")
        source = request.args.get("source", "")
        if source == "":
            search_results = newsapi.get_everything(language="en", q=q, from_param=date_from, to=date_to,
                                                    page_size=30, sort_by="publishedAt")
        else:
            search_results = newsapi.get_everything(language="en", q=q, from_param=date_from, to=date_to,
                                                    sources=source, page_size=30, sort_by="publishedAt")
        modified_search_results = []
        for result in search_results["articles"]:
            flag = validate_search_result(result)
            if flag:
                modified_search_results.append(result)
            search_results["articles"] = modified_search_results
        return search_results
    except NewsAPIException as news_api_exception:
        return {'status': news_api_exception.get_status(), 'code': news_api_exception.get_code(),
                'message': news_api_exception.get_message()}
    except Exception as exception:
        return {'status': 'error', 'code': 'Internal Server Error.',
                'message': 'There is an internal error. Please try the same query after sometime.'}


@application.route('/frequent-words')
def get_frequent_words():
    try:
        top_headlines = newsapi.get_top_headlines(page_size=70, country="us", language="en")
        frequent_words = find_all_frequent_words(top_headlines)
        return {'frequent_words': frequent_words}
    except NewsAPIException as news_api_exception:
        return {'status': news_api_exception.get_status(), 'code': news_api_exception.get_code(),
                'message': news_api_exception.get_message()}
    except Exception as exception:
        return {'status': 'error', 'code': 'Internal Server Error.',
                'message': 'There is an internal error. Please try the same query after sometime.'}
