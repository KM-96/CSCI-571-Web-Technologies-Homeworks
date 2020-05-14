import React from "react";
import NewsArticle from "./NewsArticle";
import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import Spinner from "../common/Spinner";

class DisplayArticle extends React.Component {
    render() {
        if(this.props.results !== null && this.props.results !== undefined && this.props.results.length > 0) {
            const newsArticles = this.props.results.map((article, index) => (
                <Row key={index}>
                    <NewsArticle detail={article} website={this.props.website}/>
                </Row>
            ));

            return(
                <Container fluid>
                    {newsArticles}
                </Container>
            );
        }
        return (
            <Spinner />
        )
    }
}

export default DisplayArticle;