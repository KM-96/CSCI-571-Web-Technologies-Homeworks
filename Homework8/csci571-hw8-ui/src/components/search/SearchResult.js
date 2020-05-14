import React from 'react';
import * as Constants from "../common/Constants";
import Spinner from "../common/Spinner";
import SearchCard from "./SearchCard";
import Container from "react-bootstrap/Container";
import Col from "react-bootstrap/Col";
import Row from "react-bootstrap/Row";
import { Redirect } from "react-router-dom";

class SearchResult extends React.Component {

    constructor() {
        super();
        this.state = {
            isError: false,
            searchString: "",
            isLoading: true
        }
        this.getWebsiteUrlByToggleState = this.getWebsiteUrlByToggleState.bind(this);
        this.callSearchApi = this.callSearchApi.bind(this);
    }


    getWebsiteUrlByToggleState() {
        let newsWebsite = window.localStorage.getItem(Constants.IS_GUARDIAN_KEY);
        return (newsWebsite === "true") ? Constants.GUARDIAN_URL : Constants.NEW_YORK_TIMES_URL;
    }

    callSearchApi() {
        let newsWebsite = this.getWebsiteUrlByToggleState();
        let searchString = (this.props.location !== null || this.props.location !== undefined) ? this.props.location.search : "";
        let url = Constants.BASE_URL + newsWebsite + Constants.SEARCH + searchString;
        fetch(url)
            .then(Constants.handleErrors)
            .then(data => {
                let articles = (newsWebsite === Constants.GUARDIAN_URL) ? data.response.results : data.response.docs;
                this.setState({
                    isError: false,
                    articles: articles,
                    isLoading: false
                })
            }).catch((error) => {
                  console.log("Error: " + error);
                  this.setState({
                      isError: true
                  });
              })
    }

    componentDidMount() {
        this.callSearchApi();
    }

    componentDidUpdate(prevprops) {
        if(prevprops.location.search !== this.props.location.search) {
            this.callSearchApi();
        }
    }

    render() {
        return (
            this.state.isError ? <Redirect to="/error" /> :
            this.state.isLoading ? <Spinner /> :
                (this.state.articles === null || this.state.articles === undefined || this.state.articles.length === 0)
                    ? <p className="no-articles-paragraph">You have no search results</p>
                    :
                     <Container fluid>
                        <Row>
                            <Col xs={12} sm={12} md={12} lg={12} xl={12}><h2>Results</h2></Col>
                        </Row>
                        <Row>
                         {this.state.articles.map((result, index) =>
                                <Col key={index} xs={12} sm={12} md={6} lg={4} xl={3}>
                                    <SearchCard result={result} />
                                </Col>
                         )}
                         </Row>
                     </Container>
        );

    }
}

export default SearchResult