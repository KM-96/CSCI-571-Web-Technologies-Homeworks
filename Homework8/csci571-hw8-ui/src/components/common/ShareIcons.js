import React from "react";
import { FacebookIcon, TwitterIcon, EmailIcon } from "react-share";
import { FacebookShareButton, TwitterShareButton, EmailShareButton } from "react-share";
import * as Constants from "./Constants";
import ReactTooltip from "react-tooltip";

class ShareIcons extends React.Component {
    render() {
        return (
            <div className="share-icons-container">
                <FacebookShareButton
                    url={this.props.share.url}
                    hashtag={"#" + Constants.SOCIAL_MEDIA_SHARE_HASHTAG}
                >
                    <FacebookIcon
                        round={this.props.share.isRound}
                        size={this.props.share.size}
                        data-tip="Facebook"
                        data-tip-disable={this.props.share.disable}
                    />
                    <ReactTooltip
                        place="top"
                        type="dark"
                        effect="solid"
                    />
                </FacebookShareButton>


                <TwitterShareButton
                    url={this.props.share.url}
                    hashtags={[Constants.SOCIAL_MEDIA_SHARE_HASHTAG]}
                >
                    <TwitterIcon
                        round={this.props.share.isRound}
                        size={this.props.share.size}
                        data-tip="Twitter"
                        data-tip-disable={this.props.share.disable}
                    />
                    <ReactTooltip
                        place="top"
                        type="dark"
                        effect="solid"
                    />
                </TwitterShareButton>


                <EmailShareButton
                    url={this.props.share.url}
                    subject={"#" + Constants.SOCIAL_MEDIA_SHARE_HASHTAG}
                >
                    <EmailIcon
                        round={this.props.share.isRound}
                        size={this.props.share.size}
                        data-tip="Email"
                        data-tip-disable={this.props.share.disable}
                    />
                    <ReactTooltip
                        place="top"
                        type="dark"
                        effect="solid"
                    />
                </EmailShareButton>
            </div>
        )
    }
}

export default ShareIcons;