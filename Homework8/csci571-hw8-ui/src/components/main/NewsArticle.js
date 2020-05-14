import React from "react";
import Card from "react-bootstrap/Card";
import Badge from "react-bootstrap/Badge";
import "./NewsArticle.css";
import * as Constants from "../common/Constants";
import Share from "../common/Share.js";
import { Link } from "react-router-dom";
import * as ArticleProcessor from "./ArticleProcessor";
import Container from "react-bootstrap/Container";
import Col from "react-bootstrap/Col";
import Row from "react-bootstrap/Row";

function getCurrentWebsite(websiteUrl) {
    return (websiteUrl === Constants.GUARDIAN_URL) ? Constants.GUARDIAN : Constants.NEW_YORK_TIMES;
}

function NewsArticle(props) {
        let website = getCurrentWebsite(props.website);
        let title = ArticleProcessor.getTitle(props.detail, website);
        let description = ArticleProcessor.getDescription(props.detail, website);
        let sectionId = ArticleProcessor.getSection(props.detail, website);
        let sectionStyle = ArticleProcessor.getSectionBackgroundColor(sectionId);
        let imageUrl = ArticleProcessor.getImageUrl(props.detail, website);
        let date = ArticleProcessor.getDate(props.detail, website);
        let detailedArticleUrl = ArticleProcessor.getDetailedArticleUrlId(props.detail, website);
        let id = ArticleProcessor.getId(props.detail, website);
        return (
                <Link
                    to={"/articles" + Constants.QUESTION_MARK + Constants.ARTICLE_ID_QUERY_PARAM + detailedArticleUrl}
                    className="link-to-detailed-article"
                >
                    <Card className="article-card">
                        <Container fluid>
                            <Row xs={12} sm={12}>
                                <Col xs={12} sm={12} md={5} lg={4} xl={3}>
                                    <Card.Img variant="left" className="article-image" src={imageUrl}/>
                                </Col>
                                <Col xs={12} sm={12} md={7} lg={8} xl={9}>
                                  <Card.Body>
                                    <Card.Title className="article-title">
                                        {title}
                                        <Share title={title} url={id} showWebsite={false}/>
                                    </Card.Title>
                                    <Card.Text className="card-description">
                                        {description}
                                    </Card.Text>
                                    <div className="date-section-style">
                                        <Card.Text className="date-style">{date}</Card.Text>
                                        <Badge style={sectionStyle}>{sectionId.toUpperCase()}</Badge>
                                    </div>
                                  </Card.Body>
                                </Col>
                            </Row>
                        </Container>
                    </Card>
                </Link>
        )
}

export default NewsArticle;