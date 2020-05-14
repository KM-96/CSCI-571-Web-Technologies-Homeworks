import React from "react";
import * as Constants from "../common/Constants";
import Spinner from "../common/Spinner";
import DisplayDetailedArticle from "./DisplayDetailedArticle";
import { Redirect } from "react-router-dom";

class DetailedArticle extends React.Component {

    constructor() {
        super();
        this.state = {
            isError: false,
            isLoading: true,
            article: {},
            website: ""
        };
        this.getWebsiteNameByToggleState = this.getWebsiteNameByToggleState.bind(this);
        this.getWebsite = this.getWebsite.bind(this);
    }

    getWebsiteNameByToggleState() {
        return (this.getWebsite() === Constants.GUARDIAN_URL) ? Constants.GUARDIAN : Constants.NEW_YORK_TIMES;
    }

    getWebsite() {
        if(this.props.location.state !== null &&
            this.props.location.state !== undefined &&
            this.props.location.state.website !== null &&
            this.props.location.state.website !== undefined) {
            return this.props.location.state.website;
        }
        return this.props.website;
    }

    componentDidMount() {
        let article_id = this.props.location.search;
        let newsWebsite = this.getWebsite();
        let url = Constants.BASE_URL + newsWebsite + Constants.ARTICLES + article_id;
        fetch(url)
            .then(Constants.handleErrors)
            .then(data => {
                this.setState({
                    isLoading: false,
                    article: data,
                    website: newsWebsite
                })
            }).catch((error) => {
                console.log("Error: " + error);
                this.setState({
                    isError: true
                });
            })
    }

    render() {
        return (
            this.state.isError ? <Redirect to="/error" /> :
            this.state.isLoading ? <Spinner /> : <DisplayDetailedArticle result={this.state.article} website={this.getWebsiteNameByToggleState()}/>
        )
    }
}

export default DetailedArticle;