import React from "react";
import BounceLoader from "react-spinners/BounceLoader";
import "./Spinner.css";

class Spinner extends React.Component {
    render() {
        return (
            <div className="spinner">
                <BounceLoader
                  size={30}
                  color={"#123ABC"}
                  loading={true}
                >
                </BounceLoader>
                <p className="loading">Loading</p>
            </div>
        )
    }
}

export default Spinner;