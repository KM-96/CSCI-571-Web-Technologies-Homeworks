import React from "react";
import "./ErrorPage.css";
import Container from "react-bootstrap/Container";
import Col from "react-bootstrap/Col";
import Row from "react-bootstrap/Row";
import { Link } from "react-router-dom";

class ErrorPage extends React.Component {
    render() {
        return (
            <Container fluid className="error-page-container">
                <Row>
                    <Col>
                        <h4>Page Not Found or Error loading data from API</h4>
                    </Col>
                </Row>
                <Row>
                    <Col xs={12} sm={12} md={12} lg={12} xl={12}>
                        <p>To redirect to the home page click <Link to ="/articles/home">here</Link></p>
                    </Col>
                </Row>
            </Container>
        )
    }
}

export default ErrorPage;