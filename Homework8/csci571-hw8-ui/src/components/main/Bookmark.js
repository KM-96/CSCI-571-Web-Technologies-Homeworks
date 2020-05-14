import React from "react";
import { FaRegBookmark, FaBookmark } from "react-icons/fa";
import ReactTooltip from "react-tooltip";
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import * as Constants from "../common/Constants";

class Bookmark extends React.Component {

    constructor() {
        super();
        this.state = {
            isBookmarked: false
        }
    }

    notify = () => {
        let bookmark = window.localStorage.getItem(Constants.BOOKMARK_KEY);
        let bookmark_json = JSON.parse(bookmark);
        if(bookmark_json === undefined || bookmark_json === null) {
            bookmark_json = [];
        }
        let new_bookmark_article = {
            id: this.props.id,
            web_url: this.props.web_url,
            website: this.props.website,
            title: this.props.title,
            date: this.props.date,
            section: this.props.section,
            image_url: this.props.image_url
        };
        bookmark_json.push(new_bookmark_article);
        window.localStorage.setItem(Constants.BOOKMARK_KEY, JSON.stringify(bookmark_json));

        this.setState(prevState => {
            return {
                isBookmarked: !this.state.isBookmarked
            }
        });
        toast("Saving " + this.props.title);
    }

    removeBookmark = () => {
        let bookmark = window.localStorage.getItem(Constants.BOOKMARK_KEY);
        if(bookmark === null || bookmark === undefined || bookmark === "") {
            return;
        }
        let bookmark_json = JSON.parse(bookmark);
        let modified_json = bookmark_json.filter(result => result.web_url !== this.props.web_url)
        window.localStorage.setItem(Constants.BOOKMARK_KEY, JSON.stringify(modified_json));
        this.setState(prevState => {
            return {
                isBookmarked: !this.state.isBookmarked
            }
        });
        toast("Removing " + this.props.title);
    }

    componentDidMount() {
        let bookmark = window.localStorage.getItem(Constants.BOOKMARK_KEY);
        let bookmark_json = JSON.parse(bookmark);
        let isBookmarked = false;
        if(bookmark_json !== undefined && bookmark_json !== null) {
            let result = bookmark_json.find(bk => bk.web_url === this.props.web_url);
            if(result !== null && result !== undefined) {
                isBookmarked = true;
            }
        }
        this.setState(prevState => {
            return {
                isBookmarked: isBookmarked
            }
        })
    }

    render() {
        return (
        <>
            { this.state.isBookmarked ?
                <span>
                   <FaBookmark
                        data-tip="Bookmark"
                        className="article-detail-bookmark-icon"
                        onClick={this.removeBookmark}
                    />
                    <ReactTooltip
                        place="top"
                        type="dark"
                        effect="solid"
                    />
                </span>
             :
               <FaRegBookmark
                    data-tip="Bookmark"
                    className="article-detail-bookmark-icon"
                    onClick={this.notify}
                />
            }

            <ReactTooltip
                place="top"
                type="dark"
                effect="solid"
            />
        </>
        )
    }
}

export default Bookmark;