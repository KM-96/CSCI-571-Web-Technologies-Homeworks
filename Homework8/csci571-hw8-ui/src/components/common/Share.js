import React, { useState } from "react";
import { MdShare } from "react-icons/md";
import Modal from "react-bootstrap/Modal";
import ShareIcons from "./ShareIcons";
import "./Share.css";

function Share(props) {
    const [show, setShow] = useState(false);

    const handleClose = () => {
        setShow(false);
    }

    const handleShow = (event) => {
        event.preventDefault();
        event.stopPropagation();
        setShow(true);
    }

    const shareIconProperties = {
        isRound: true,
        size: 60,
        disable: true,
        url: props.url
    }

    const shareSpanProperties = {
        display: "inline-block"
    }

    return (
        <span style={shareSpanProperties}>
            <MdShare onClick={(event) => handleShow(event)}>
            </MdShare>

            <div className="share-modal-container" onClick={event => event.stopPropagation()}>
              <Modal show={show}  animation={false} onHide={handleClose}>
                <Modal.Header closeButton>
                  <Modal.Title>
                    {props.showWebsite && <h2>{props.website}</h2>}
                    <p>{props.title}</p>
                  </Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <h4 className="share-via-text">Share via</h4>
                    <ShareIcons share={shareIconProperties} />
                </Modal.Body>
              </Modal>
            </div>
        </span>
    )
}

export default Share;