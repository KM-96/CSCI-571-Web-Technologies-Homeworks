import React from "react";
import Navbar from "react-bootstrap/Navbar";
import Nav from "react-bootstrap/Nav";
import { NavLink, withRouter} from "react-router-dom";
import "./Header.css";
import ToggleSwitch from "./header-components/ToggleSwitch";
import SearchBar from "./header-components/SearchBar";
import { FaRegBookmark, FaBookmark } from "react-icons/fa";
import ReactTooltip from "react-tooltip";

class Header extends React.Component {

    constructor() {
        super();
        this.handleBookmarkClick = this.handleBookmarkClick.bind(this);
        this.handleSearchChange = this.handleSearchChange.bind(this);
    }

    handleSearchChange(searchObject) {
        this.props.history.push("/articles/search?q=" + searchObject.label);
    }

    handleBookmarkClick() {
        this.props.history.push("/bookmarks");
    }

    render() {
        const searchBarProperties = {
            placeholder: "Enter keyword ..",
            noOptionsMessage: "No Match"
        }

        return (
            <Navbar collapseOnSelect expand="lg" className="navigation-bar">
              {/*Search bar*/}
              <SearchBar
                    searchBar={searchBarProperties}
                    handleInputChange={(inputValue) => this.props.handleInputChange(inputValue)}
                    search={this.props.search}
                    action={(searchObject) => this.props.handleSearchChange(searchObject)}
              />
              <Navbar.Toggle aria-controls="responsive-navbar-nav" />


              {/*Navbar.Collapse will make the entire content in this element responsive. On smaller devices they collapse*/}
              <Navbar.Collapse className="navigation-tabs-container" id="responsive-navbar-nav">
                {/*All the navigation items that are aligned to the left*/}
                <Nav className="navigation-items-list" >
                    <Navbar.Text><NavLink exact className="nav-section" to="/articles/home" href="/articles/home">Home</NavLink></Navbar.Text>
                    <Navbar.Text><NavLink className="nav-section" to="/articles/world" href="/articles/world">World</NavLink></Navbar.Text>
                    <Navbar.Text><NavLink className="nav-section" to="/articles/politics" href="/articles/politics">Politics</NavLink></Navbar.Text>
                    <Navbar.Text><NavLink className="nav-section" to="/articles/business" href="/articles/business">Business</NavLink></Navbar.Text>
                    <Navbar.Text><NavLink className="nav-section" to="/articles/technology" href="/articles/technology">Technology</NavLink></Navbar.Text>
                    <Navbar.Text><NavLink className="nav-section" to="/articles/sports" href="/articles/sports">Sports</NavLink></Navbar.Text>
                </Nav>
                {/*All the navigation items that are aligned to the right of the header*/}
                <Nav className="justify-content-end navigation-items-list header-bookmark-container">
                      {/*Bookmark start*/}
                          {
                            this.props.isBookmarksPage ?
                                <Navbar.Text>
                                    <FaBookmark className="checked-bookmark-icon"/>
                                </Navbar.Text>
                            :
                            <Navbar.Text>
                                <NavLink href="/bookmarks" to="/bookmarks">
                                    <FaRegBookmark
                                        data-for="header"
                                        data-tip="Bookmark"
                                        className="header-bookmark-icon"
                                    />
                                    <ReactTooltip
                                        place="bottom"
                                        type="dark"
                                        effect="solid"
                                        id="header"
                                    />
                                </NavLink>
                            </Navbar.Text>
                          }
                      {/*Bookmark End*/}

                      {
                         (this.props.displayToggle) &&
                         <>
                            <Navbar.Text className="news-labels">NYTimes</Navbar.Text>
                            <Navbar.Text><ToggleSwitch section={this.props.section}/></Navbar.Text>
                            <Navbar.Text className="news-labels">Guardian</Navbar.Text>
                         </>
                      }

                </Nav>
            </Navbar.Collapse>
            </Navbar>
        );
    }
}

export default withRouter(Header);