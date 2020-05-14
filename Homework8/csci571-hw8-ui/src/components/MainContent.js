import React from "react";
import Spinner from "./common/Spinner";
import DisplayArticle from "./main/DisplayArticle";
import * as Constants from "./common/Constants";
import { Redirect } from "react-router-dom";

class MainContent extends React.Component {
    constructor() {
        super();
        this.state = {
            isError: false,
            isLoading: true,
            articles: [],
        }
        this.getSection = this.getSection.bind(this);
        this.getUrl = this.getUrl.bind(this);
    }

    getSection(section) {
        let sectionUrl = "";
        if (section === Constants.HOME_SECTION) {
            sectionUrl = Constants.HOME;
        } else if (section === Constants.WORLD_SECTION) {
            sectionUrl = Constants.WORLD;
        } else if (section === Constants.SPORT_SECTION) {
            let website = window.localStorage.getItem(Constants.IS_GUARDIAN_KEY);
            if(website === "true") {
                sectionUrl = Constants.SPORT;
            } else {
                sectionUrl = Constants.SPORTS;
            }
        } else if (section === Constants.TECHNOLOGY_SECTION) {
            sectionUrl = Constants.TECHNOLOGY;
        } else if (section === Constants.BUSINESS_SECTION) {
            sectionUrl = Constants.BUSINESS;
        } else if (section === Constants.POLITICS_SECTION) {
            sectionUrl = Constants.POLITICS;
        } else {
            sectionUrl = Constants.HOME;
        }
        return sectionUrl;
    }

    getUrl(section) {
        let url = Constants.BASE_URL + this.props.website + this.getSection(section);
        return url;
    }

    componentDidMount() {
        let url = this.getUrl(this.props.section);
        fetch(url)
            .then(response => response.json())
            .then(data => {
                let articles = [];
                if(this.props.website === Constants.GUARDIAN_URL) {
                    articles = data.response.results;
                } else {
                    articles = data.results;
                }
                this.setState({
                    isError: false,
                    isLoading: false,
                    articles : articles,
                })
            }).catch((error) => {
                console.log("Error: " + error);
                this.setState({
                  isError: true
                });
            })
    }

    componentDidUpdate(prevProps) {
        if(prevProps.section !== this.props.section || prevProps.website !== this.props.website) {
            this.setState({
                isLoading: true,
            });
            let url = this.getUrl(this.props.section);
            fetch(url)
                .then(Constants.handleErrors)
                .then(data => {
                    let articles = [];
                    if(this.props.website === Constants.GUARDIAN_URL) {
                        articles = data.response ? data.response.results : [];
                    } else {
                        articles = data.results;
                    }
                    this.setState({
                        isError: false,
                        isLoading: false,
                        articles : articles,
                    })
                }).catch((error) => {
                    console.log("Error: " + error);
                    this.setState({
                      isError: true
                    });
                })
        }
    }

    componentWillReceiveProps(prevProps) {
        if(prevProps.website !== this.props.website) {
            this.setState({
                isError: false,
                isLoading: true,
                articles : [],
            })
        }
    }

    componentWillUnmount() {
        this.setState({
            isError: false,
            isLoading: true,
            articles: []
        })
    }

    render() {
        return (
            this.state.isError ? <Redirect to="/error" /> :
            <div id="main-content">
                {this.state.isLoading ? <Spinner /> : <DisplayArticle results={this.state.articles} website={this.props.website}/>}
            </div>
        );
    }
}

export default MainContent;