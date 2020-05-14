import React from 'react';
import Card from "react-bootstrap/Card";
import Badge from "react-bootstrap/Badge";
import * as Constants from "../common/Constants";
import Share from "../common/Share";
import * as ArticleProcessor from "../main/ArticleProcessor";
import { Redirect } from "react-router-dom";
import "./SearchCard.css";

class SearchCard extends React.Component {

    constructor() {
        super();
        this.state = {
            isRedirect: false
        };
        this.getCurrentWebsite = this.getCurrentWebsite.bind(this);
        this.handleCardClick = this.handleCardClick.bind(this);
    }

    handleCardClick(event) {
        this.setState({
            isRedirect: true
        });
    }

    getCurrentWebsite() {
        let isGuardianKey = window.localStorage.getItem(Constants.IS_GUARDIAN_KEY);
        return (isGuardianKey === "true") ? Constants.GUARDIAN : Constants.NEW_YORK_TIMES;
    }

    render() {
        let website = this.getCurrentWebsite();
        let websiteUrl = (website === Constants.GUARDIAN) ? Constants.GUARDIAN_URL : Constants.NEW_YORK_TIMES_URL;
        let title = ArticleProcessor.getTitle(this.props.result, website);
        let sectionId = ArticleProcessor.getSection(this.props.result, website);
        let sectionStyle = ArticleProcessor.getSectionBackgroundColor(sectionId && sectionId.toLowerCase());
        let imageUrl = ArticleProcessor.getImageUrl(this.props.result, website);
        let date = ArticleProcessor.getDate(this.props.result, website);
        let articleUrl = ArticleProcessor.getArticleUrl(this.props.result, website);
        let id = ArticleProcessor.getId(this.props.result, website);

        return (
            this.state.isRedirect ?
                <Redirect to={{
                        pathname: Constants.ARTICLES,
                        state: {
                            website: websiteUrl
                        },
                        search: Constants.ARTICLE_ID_QUERY_PARAM + articleUrl
                    }}
                />
            :
            <Card className="search-article-card" onClick={this.handleCardClick}>
                <Card.Title className="search-article-title">
                    {title}
                    <Share title={title} url={id} showWebsite={false}/>
                </Card.Title>
                <Card.Img variant="top" className="search-article-image" src={imageUrl}/>
                <Card.Body className="search-article-body">
                    <Card.Text className="search-date-style">{date}</Card.Text>
                    <Badge className="search-section-badge" style={sectionStyle}>{sectionId && sectionId.toUpperCase()}</Badge>
                </Card.Body>
            </Card>
        );
    }
}

export default SearchCard;