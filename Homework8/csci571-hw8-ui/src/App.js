import React from "react";
import { Redirect, withRouter, Switch, Route } from "react-router-dom";
import './App.css';
import Header from './components/Header';
import MainContent from './components/MainContent';
import DetailedArticle from "./components/main/DetailedArticle";
import BookmarkResults from "./components/main/BookmarkResults";
import * as Constants from "./components/common/Constants";
import { Zoom, ToastContainer} from 'react-toastify';
import SearchResult from "./components/search/SearchResult";
import ErrorPage from "./components/common/ErrorPage";

class App extends React.Component {
    constructor() {
        super();
        this.state = {
            queryString: ""
        }
        this.getWebsiteUrlByToggleState = this.getWebsiteUrlByToggleState.bind(this);
        this.handleInputChange = this.handleInputChange.bind(this);
        this.handleSearchChange = this.handleSearchChange.bind(this);
    }

    handleSearchChange(searchObject) {
        this.setState({
            queryString: searchObject.label
        });
        this.props.history.push("/articles/search?q=" + searchObject.label);
    }

    handleInputChange = (word) => {
        if(word === null || word === undefined || word.length === 0) return;
        console.log(word);
        this.setState({
            queryString: word
        })
    }

    getWebsiteUrlByToggleState() {
        let newsWebsite = window.localStorage.getItem(Constants.IS_GUARDIAN_KEY);
        if(newsWebsite === null || newsWebsite === undefined || newsWebsite.length === 0) {
            window.localStorage.setItem(Constants.IS_GUARDIAN_KEY, "true");
            newsWebsite = "true";
        }
        return (newsWebsite === "true") ? Constants.GUARDIAN_URL : Constants.NEW_YORK_TIMES_URL;
    }

    componentDidMount() {
        let flag = window.localStorage.getItem(Constants.IS_GUARDIAN_KEY);
        if(flag === null || flag === undefined) {
            window.localStorage.setItem(Constants.IS_GUARDIAN_KEY , "true");
        }
    }

    render() {
      return (
        <div>
            <ToastContainer
                position="top-center"
                autoClose={2000}
                hideProgressBar
                newestOnTop={false}
                closeOnClick
                rtl={false}
                pauseOnVisibilityChange
                draggable
                pauseOnHover={false}
                transition={Zoom}
            />

            <Switch>
                <Route exact path="/articles/home" render={(routeProps) => (
                    <>
                        <Header
                            displayToggle={true}
                            isBookmarksPage={false}
                            section={Constants.HOME_SECTION}
                            handleInputChange={(inputValue) => this.handleInputChange(inputValue)}
                            handleSearchChange={(searchObject) => this.handleSearchChange(searchObject)}
                        />
                        <MainContent {...routeProps} section={Constants.HOME_SECTION} website={this.getWebsiteUrlByToggleState()} />
                    </>
                )} />

                <Route exact path="/articles/world" render={(routeProps) => (
                    <>
                        <Header
                            displayToggle={true}
                            isBookmarksPage={false}
                            section={Constants.WORLD_SECTION}
                            handleInputChange={(inputValue) => this.handleInputChange(inputValue)}
                            handleSearchChange={(searchObject) => this.handleSearchChange(searchObject)}
                        />
                        <MainContent {...routeProps} section={Constants.WORLD_SECTION} website={this.getWebsiteUrlByToggleState()}/>
                    </>
                )}  />

                <Route exact path="/articles/politics" render={(routeProps) => (
                    <>
                        <Header
                            displayToggle={true}
                            isBookmarksPage={false}
                            section={Constants.POLITICS_SECTION}
                            handleInputChange={(inputValue) => this.handleInputChange(inputValue)}
                            handleSearchChange={(searchObject) => this.handleSearchChange(searchObject)}
                        />
                        <MainContent {...routeProps} section={Constants.POLITICS_SECTION} website={this.getWebsiteUrlByToggleState()}/>
                    </>
                )} />

                <Route exact path="/articles/technology" render={(routeProps) => (
                    <>
                        <Header
                            displayToggle={true}
                            isBookmarksPage={false}
                            section={Constants.TECHNOLOGY_SECTION}
                            handleInputChange={(inputValue) => this.handleInputChange(inputValue)}
                            handleSearchChange={(searchObject) => this.handleSearchChange(searchObject)}
                        />
                        <MainContent {...routeProps} section={Constants.TECHNOLOGY_SECTION} website={this.getWebsiteUrlByToggleState()}/>
                    </>
                )} />

                <Route exact path="/articles/business" render={(routeProps) => (
                    <>
                        <Header
                            displayToggle={true}
                            isBookmarksPage={false}
                            section={Constants.BUSINESS_SECTION}
                            handleInputChange={(inputValue) => this.handleInputChange(inputValue)}
                            handleSearchChange={(searchObject) => this.handleSearchChange(searchObject)}
                        />
                        <MainContent {...routeProps} section={Constants.BUSINESS_SECTION} website={this.getWebsiteUrlByToggleState()}/>
                    </>
                )} />

                <Route exact path="/articles/sports" render={(routeProps) => (
                    <>
                        <Header
                            displayToggle={true}
                            isBookmarksPage={false}
                            section={Constants.SPORTS_SECTION}
                            handleInputChange={(inputValue) => this.handleInputChange(inputValue)}
                            handleSearchChange={(searchObject) => this.handleSearchChange(searchObject)}
                        />
                        <MainContent {...routeProps} section={Constants.SPORT_SECTION} website={this.getWebsiteUrlByToggleState()}/>
                    </>
                )} />

                <Route exact path="/articles/search" render={(routeProps) => (
                    <>
                        <Header
                            displayToggle={false}
                            isBookmarksPage={false}
                            search={this.state.queryString}
                            handleInputChange={(inputValue) => this.handleInputChange(inputValue)}
                            handleSearchChange={(searchObject) => this.handleSearchChange(searchObject)}
                        />
                        <SearchResult {...routeProps} key={window.location.search}/>
                    </>
                )} />

                <Route exact path="/articles" render={(routeProps) => (
                    <>
                        <Header
                            displayToggle={false}
                            isBookmarksPage={false}
                            handleInputChange={(inputValue) => this.handleInputChange(inputValue)}
                            handleSearchChange={(searchObject) => this.handleSearchChange(searchObject)}
                        />
                        <DetailedArticle {...routeProps} website={this.getWebsiteUrlByToggleState()}/>
                    </>
                )} />

                <Route exact path="/bookmarks" render={(routeProps) => (
                    <>
                        <Header
                            displayToggle={false}
                            isBookmarksPage={true}
                            handleInputChange={(inputValue) => this.handleInputChange(inputValue)}
                            handleSearchChange={(searchObject) => this.handleSearchChange(searchObject)}
                        />
                        <BookmarkResults {...routeProps}/>
                    </>
                )} />

                <Route exact path="/" render={(routeProps) => (
                    <Redirect to="/articles/home" />
                )} />
                <Route path="*" component={ErrorPage} />
            </Switch>
        </div>
      );
    }
}

export default withRouter(App);