import React from "react";
import CommentBox from "commentbox.io";
import * as Constants from "../common/Constants";

class Comment extends React.Component {

    componentDidMount() {
        this.removeCommentBox = CommentBox(Constants.COMMENT_BOX_PROJECT_ID);
    }

    componentWillUnmount() {
        this.removeCommentBox();
    }

    render() {
        return (
            <div className="commentbox" id={this.props.id}/>
        );
    }
}

export default Comment;