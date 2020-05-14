import React from "react";
import * as Constants from "../common/Constants";
import * as ArticleProcessor from "./ArticleProcessor";
import Card from "react-bootstrap/Card";
import "./DisplayDetailedArticle.css";
import { FaAngleDown, FaAngleUp } from "react-icons/fa";
import Bookmark from "./Bookmark";
import ShareIcons from "../common/ShareIcons";
import Comment from "./Comment";
import Container from "react-bootstrap/Container";
import Col from "react-bootstrap/Col";
import Row from "react-bootstrap/Row";
import { Element, Link, animateScroll as scroll } from "react-scroll";

class DisplayDetailedArticle extends React.Component {

   constructor() {
       super();
       this.state = {
           isMinimizedView: true,
       }
       this.handleDownArrow = this.handleDownArrow.bind(this);
       this.scrollToTop = this.scrollToTop.bind(this);
       this.handleUpArrow = this.handleUpArrow.bind(this);
   }

   scrollToTop() {
        scroll.scrollToTop({
             duration: 300,
             delay: 0,
             spy: true,
             smooth: true
       });
   }

   handleDownArrow() {
       this.setState(prevState => {
           return {
               isMinimizedView: !prevState.isMinimizedView,
           }
       });
   }

   handleUpArrow() {
       this.scrollToTop();
       this.setState(prevState => {
              return {
                  isMinimizedView: !prevState.isMinimizedView,
              }
        });
   }

    render() {
        let title = ArticleProcessor.getTitle(this.props.result, this.props.website);
        let description = ArticleProcessor.getDescription(this.props.result, this.props.website);
        //Process description
        let description_sentences = description.split(". ");
        let descriptionPara1 = "";
        let descriptionPara2 = "";
        let isEllipsis = false;
        if(description_sentences.length > Constants.NUMBER_OF_SENTENCES_IN_ARTICLE_DETAIL_VIEW) {
            isEllipsis = true;
            for(let i = 0; i < description_sentences.length; i++) {
                if(i < Constants.NUMBER_OF_SENTENCES_IN_ARTICLE_DETAIL_VIEW) {
                    descriptionPara1 = descriptionPara1 + description_sentences[i];
                    if(i === Constants.NUMBER_OF_SENTENCES_IN_ARTICLE_DETAIL_VIEW - 1) {
                        descriptionPara1 = descriptionPara1 + ".";
                    } else {
                        descriptionPara1 = descriptionPara1 + ". ";
                    }
                } else {
                    if(i === description_sentences.length - 1) {
                        descriptionPara2 = descriptionPara2 + description_sentences[i];
                    } else {
                        descriptionPara2 = descriptionPara2 + description_sentences[i] + ". ";
                    }
                }
            }
        } else {
            descriptionPara1 = description;
        }

        let section = ArticleProcessor.getSection(this.props.result, this.props.website);
        let imageUrl = ArticleProcessor.getImageUrl(this.props.result, this.props.website);
        let date = ArticleProcessor.getDate(this.props.result, this.props.website);
        let articleUrl = ArticleProcessor.getArticleUrl(this.props.result, this.props.website);
        let id = ArticleProcessor.getId(this.props.result, this.props.website);
        const shareIconsStyles = {
            isRound: true,
            size: 32,
            url: id,
            disable: false
        }
        return(
        <Container fluid>
                <Row>
                <Card className="detail-article-card">
                    <Container fluid>
                        <Row>
                            <Col xs={12} sm={12} md={12} lg={12} xl={12}>
                                <Card.Title className="detail-card-title"><h4>{title}</h4></Card.Title>
                            </Col>
                        </Row>
                        <Row className="detail-date-icons-container">
                            <Col className="no-padding-right" xs={5} sm={3} md={3} lg={3} xl={3}>
                                <Card.Text className="detail-date-style">{date}</Card.Text>
                            </Col>
                            <Col className="no-padding detail-share-icons" xs={{span: 5}} sm={{span: 3, offset: 4}} md={{span: 3, offset: 4}} lg={{span: 3, offset: 4}} xl={{span: 3, offset: 4}}>
                                <ShareIcons share={shareIconsStyles}/>
                            </Col>
                            <Col xs={{span: 2}} sm={{span: 1, offset: 1}} md={{span: 1, offset: 1}} lg={{span: 1, offset: 1}} xl={{span: 1, offset: 1}}>
                                <Bookmark
                                    id={id}
                                    title={title}
                                    web_url={articleUrl}
                                    website={this.props.website === Constants.GUARDIAN ? Constants.GUARDIAN_BADGE : Constants.NYTIMES_BADGE}
                                    date={date}
                                    section={section}
                                    image_url={imageUrl}
                                />
                            </Col>
                        </Row>
                        <Row className="detail-card-image-container">
                            <Col xs={12} sm={12} md={12} lg={12} xl={12}>
                                <Card.Img src={imageUrl}/>
                            </Col>
                        </Row>
                        <Row className="detail-description-container">
                            <Col className="text-align-justify" xs={12} sm={12} md={12} lg={12} xl={12}>
                                { this.state.isMinimizedView ?
                                <>
                                <p className="description">{descriptionPara1}</p>
                                {isEllipsis && <span>...</span>}
                                {isEllipsis &&
                                    <Link
                                        to="expanded-description"
                                        spy={true}
                                        smooth={true}
                                        duration={300}
                                        className="expanded-description detail-card-arrow-container"
                                    >
                                        <FaAngleDown className="detail-card-arrow" onClick={this.handleDownArrow}/>
                                    </Link>
                                }
                                </>
                                :
                                <>
                                <p className="text-align-justify">{descriptionPara1}</p>
                                <Element id="expanded-description" className="element">
                                    <p className="text-align-justify">{descriptionPara2}</p>
                                    <div className="detail-card-arrow-container">
                                        <FaAngleUp className="detail-card-arrow" onClick={this.handleUpArrow} />
                                    </div>
                                </Element>
                                </>
                                }
                            </Col>
                        </Row>
                    </Container>
                </Card>
                </Row>
                <Row>
                    <Col xs={12} sm={12} md={12} lg={12} xl={12}>
                        <Comment id={id}/>
                    </Col>
                </Row>
        </Container>
        )
    }
}

export default DisplayDetailedArticle;