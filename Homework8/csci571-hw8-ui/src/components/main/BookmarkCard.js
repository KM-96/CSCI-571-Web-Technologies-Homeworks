import React from 'react';
import Card from "react-bootstrap/Card";
import Badge from "react-bootstrap/Badge";
import { Redirect, withRouter } from "react-router-dom";
import * as ArticleProcessor from "./ArticleProcessor";
import * as Constants from "../common/Constants";
import Share from "../common/Share";
import { MdDelete } from "react-icons/md";
import "./BookmarkCard.css";

class BookmarkCard extends React.Component {
    constructor() {
        super();
        this.state = {
            isRedirect: false
        };
        this.handleCardClick = this.handleCardClick.bind(this);
    }

    handleCardClick(event) {
        this.setState({
            isRedirect: true
        });
    }

    render() {
        let sectionId = this.props.json.section;
        let sectionStyle = ArticleProcessor.getSectionBackgroundColor(sectionId ? sectionId.toLowerCase() : sectionId);
        let website = this.props.json.website;
        let sectionStyleForWebsite = ArticleProcessor.getSectionBackgroundColor(website ? website.toLowerCase() : website);
        let websiteNameForModal = website === Constants.GUARDIAN_BADGE ? Constants.GUARDIAN_TEXT_FOR_MODAL : Constants.NEW_YORK_TIMES_TEXT_FOR_MODAL;
        let websiteUrl = website === Constants.GUARDIAN_BADGE ? Constants.GUARDIAN_URL : Constants.NEW_YORK_TIMES_URL;
        return(
            this.state.isRedirect ?
                <Redirect to={{
                        pathname: Constants.ARTICLES,
                        state: {
                            website: websiteUrl
                        },
                        search: Constants.ARTICLE_ID_QUERY_PARAM + this.props.json.web_url
                    }}
                />
            :
                <Card className="favorites-card" onClick={(event) => this.handleCardClick(event)}>
                    <Card.Title className="favorites-title">
                        {this.props.json.title}
                        <Share title={this.props.json.title} url={this.props.json.id} website={websiteNameForModal} showWebsite={true}/>
                        <MdDelete onClick={this.props.action}/>
                    </Card.Title>
                    <Card.Img variant="top" className="favorites-image" src={this.props.json.image_url}/>
                    <Card.Body className="favorites-body">
                        <Card.Text className="favorites-date">{this.props.json.date}</Card.Text>
                        <span className="favorites-badges">
                        <Badge style={sectionStyle}>{sectionId && sectionId.toUpperCase()}</Badge>
                        <Badge style={sectionStyleForWebsite} className="favorites-website-badge">{website && website.toUpperCase()} </Badge>
                        </span>
                    </Card.Body>
                </Card>
        );
    }
}

export default withRouter(BookmarkCard);