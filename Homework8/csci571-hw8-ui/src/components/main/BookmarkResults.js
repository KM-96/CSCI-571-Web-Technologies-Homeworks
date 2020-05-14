import React from "react";
import * as Constants from "../common/Constants";
import Spinner from "../common/Spinner";
import BookmarkCard from "./BookmarkCard";
import "./BookmarkResults.css";
import Container from "react-bootstrap/Container";
import Col from "react-bootstrap/Col";
import Row from "react-bootstrap/Row";
import { toast } from 'react-toastify';
import { Redirect } from "react-router-dom";

class BookmarkResults extends React.Component {

    constructor() {
        super();
        this.state = {
            isError: false,
            isLoading: true,
            articles: []
        }
        this.isArticlesEmpty = this.isArticlesEmpty.bind(this);
        this.handleClick = this.handleClick.bind(this);
    }

    componentDidMount() {
        let bookmarks = window.localStorage.getItem(Constants.BOOKMARK_KEY);
        if(bookmarks === null || bookmarks === undefined || bookmarks === "") {
            this.setState({
                isError: false,
                isLoading: false,
                articles: []
            })
        } else {
            let bookmarks_json = JSON.parse(bookmarks);
            this.setState({
                isError: false,
                isLoading: false,
                articles: bookmarks_json
            })
        }
    }

    isArticlesEmpty(articles) {
        if(articles === null || articles === undefined) {
            return true;
        }
        return false;
    }


    handleClick(event, web_url, title) {
        event.preventDefault();
        event.stopPropagation();
        let bookmark = window.localStorage.getItem(Constants.BOOKMARK_KEY);
        let bookmark_json = JSON.parse(bookmark);
        let modified_json = bookmark_json.filter(result => result.web_url !== web_url)
        window.localStorage.setItem(Constants.BOOKMARK_KEY, JSON.stringify(modified_json));
        this.setState({
            isError: false,
            isLoading: false,
            articles: modified_json
        });
        toast("Removing "  + title);
    }

    render() {
        return (
            this.state.isError ? <Redirect to="/error" /> :
            this.state.isLoading ? <Spinner /> :
                (this.state.articles === null || this.state.articles === undefined || this.state.articles.length === 0)
                    ? <p className="no-articles-paragraph">You have no saved articles</p>
                    :
                     <Container fluid>
                        <Row>
                            <Col xs={12} sm={12} md={12} lg={12} xl={12}><h2>Favorites</h2></Col>
                        </Row>
                        <Row>
                         {this.state.articles.map(result =>
                                <Col key={result.web_url} xs={12} sm={12} md={6} lg={4} xl={3}>
                                    <BookmarkCard
                                        json={result}
                                        action={(event) => this.handleClick(event, result.web_url, result.title)}
                                    />
                                </Col>
                         )}
                         </Row>
                     </Container>
        );
    }
}

export default BookmarkResults;